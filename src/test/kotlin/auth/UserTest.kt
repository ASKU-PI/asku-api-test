package pl.asku.tests.auth

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.auth.Authority
import model.auth.User
import org.apache.http.HttpStatus
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItems
import org.junit.jupiter.api.Test
import pl.asku.tests.BaseTest

class UserTest : BaseTest() {

    private val path: String = "/auth/api/user"

    @Test
    fun `fails for not signed in user`() {
        Given {
            spec(requestSpecification)
        } When {
            get(path)
        } Then {
            statusCode(HttpStatus.SC_UNAUTHORIZED)
        }
    }

    @Test
    fun `returns regular user`() {
        Given {
            spec(requestSpecification)
            header(userAuthorizationHeader)
        } When {
            get(path)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body(
                equalTo(
                    Json.encodeToString(
                        User(
                            "testUser@gmail.com",
                            arrayOf(
                                Authority("ROLE_USER")
                            )
                        )
                    )
                )
            )
        }
    }

    @Test
    fun `returns moderator user`() {
        Given {
            spec(requestSpecification)
            header(moderatorAuthorizationHeader)
        } When {
            get(path)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("identifier", equalTo("testModerator@gmail.com"))
            body("authorities.authorityName", hasItems("ROLE_USER", "ROLE_MODERATOR"))
        }
    }

    @Test
    fun `returns admin user`() {
        Given {
            spec(requestSpecification)
            header(adminAuthorizationHeader)
        } When {
            get(path)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("identifier", equalTo("testAdmin@gmail.com"))
            body("authorities.authorityName", hasItems("ROLE_USER", "ROLE_MODERATOR", "ROLE_ADMIN"))
        }
    }

    @Test
    fun `fails for not signed in user trying to get a user`() {
        Given {
            spec(requestSpecification)
        } When {
            get("$path/testUser@gmail.com")
        } Then {
            statusCode(HttpStatus.SC_UNAUTHORIZED)
        }
    }

    @Test
    fun `fails for regular user trying to get other user`() {
        Given {
            spec(requestSpecification)
            header(userAuthorizationHeader)
        } When {
            get("$path/moderatorUser@gmail.com")
        } Then {
            statusCode(HttpStatus.SC_UNAUTHORIZED)
        }
    }

    @Test
    fun `returns user info for user requesting themselves`() {
        Given {
            spec(requestSpecification)
            header(userAuthorizationHeader)
        } When {
            get("$path/testUser@gmail.com")
        } Then {
            statusCode(HttpStatus.SC_OK)
            body(
                equalTo(
                    Json.encodeToString(
                        User(
                            "testUser@gmail.com",
                            arrayOf(
                                Authority("ROLE_USER")
                            )
                        )
                    )
                )
            )
        }
    }

    @Test
    fun `returns requested user for moderator`() {
        Given {
            spec(requestSpecification)
            header(moderatorAuthorizationHeader)
        } When {
            get("$path/testAdmin@gmail.com")
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("identifier", equalTo("testAdmin@gmail.com"))
            body("authorities.authorityName", hasItems("ROLE_USER", "ROLE_MODERATOR", "ROLE_ADMIN"))
        }
    }

    @Test
    fun `returns requested user for admin`() {
        Given {
            spec(requestSpecification)
            header(adminAuthorizationHeader)
        } When {
            get("$path/testUser@gmail.com")
        } Then {
            statusCode(HttpStatus.SC_OK)
            body(
                equalTo(
                    Json.encodeToString(
                        User(
                            "testUser@gmail.com",
                            arrayOf(
                                Authority("ROLE_USER")
                            )
                        )
                    )
                )
            )
        }
    }
}