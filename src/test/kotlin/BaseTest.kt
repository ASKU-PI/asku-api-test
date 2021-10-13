package pl.asku.tests

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.LogConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.LogDetail
import io.restassured.http.ContentType
import io.restassured.http.Header
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.specification.RequestSpecification
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.auth.Login
import org.apache.http.HttpStatus
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BaseTest {

    companion object {
        lateinit var requestSpecification: RequestSpecification
        lateinit var userAuthorizationHeader: Header
        lateinit var moderatorAuthorizationHeader: Header
        lateinit var adminAuthorizationHeader: Header
    }

    @BeforeAll
    fun setUp() {

        val logConfig = LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)
        val config = RestAssuredConfig.config().logConfig(logConfig)

        requestSpecification = RequestSpecBuilder()
            .setBaseUri("http://localhost:4000")
            .setContentType(ContentType.JSON)
            .setRelaxedHTTPSValidation()
            .setConfig(config)
            .build()

        userAuthorizationHeader = getAuthorizationHeader("testUser@gmail.com", "testUser")
        moderatorAuthorizationHeader = getAuthorizationHeader("testModerator@gmail.com", "testModerator")
        adminAuthorizationHeader = getAuthorizationHeader("testAdmin@gmail.com", "testAdmin")
    }

    @AfterAll
    fun tearDown() {
        RestAssured.reset()
    }

    private fun getAuthorizationHeader(email: String, password: String): Header {
        val token: String = Given {
            spec(requestSpecification)
            body(Json.encodeToString(Login(email, password)))
        } When {
            post("/auth/api/login")
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            path("token")
        }

        return Header("Authorization", "Bearer $token")
    }
}