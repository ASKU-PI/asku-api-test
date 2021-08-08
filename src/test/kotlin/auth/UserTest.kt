package pl.asku.tests.auth

import pl.asku.tests.BaseTest
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.auth.Authority
import model.auth.User
import org.apache.http.HttpStatus
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test

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
            body(equalTo(Json.encodeToString(
                User(
                    "testUser",
                    arrayOf(
                        Authority("ROLE_USER")
                    ))
            )))
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
            body("username", equalTo("testModerator"))
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
            body("username", equalTo("testAdmin"))
            body("authorities.authorityName", hasItems("ROLE_USER", "ROLE_MODERATOR", "ROLE_ADMIN"))
        }
    }

    @Test
    fun `fails for not signed in user trying to get a user`() {
        Given {
            spec(requestSpecification)
        } When {
            get("$path/testUser")
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
            get("$path/moderatorUser")
        } Then {
            statusCode(HttpStatus.SC_FORBIDDEN)
        }
    }

    @Test
    fun `returns requested user for moderator`() {
        Given {
            spec(requestSpecification)
            header(moderatorAuthorizationHeader)
        } When {
            get("$path/testAdmin")
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("username", equalTo("testAdmin"))
            body("authorities.authorityName", hasItems("ROLE_USER", "ROLE_MODERATOR", "ROLE_ADMIN"))
        }
    }

    @Test
    fun `returns requested user for admin`() {
        Given {
            spec(requestSpecification)
            header(adminAuthorizationHeader)
        } When {
            get("$path/testUser")
        } Then {
            statusCode(HttpStatus.SC_OK)
            body(equalTo(Json.encodeToString(
                User(
                    "testUser",
                    arrayOf(
                        Authority("ROLE_USER")
                    ))
            )))
        }
    }
}