package pl.asku.tests.magazine

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test
import pl.asku.tests.BaseTest

class HealthTest : BaseTest() {

    private val path: String = "/auth/actuator/health"

    @Test
    fun `service is healthy`() {
        Given {
            spec(requestSpecification)
        } When {
            get(path)
        } Then {
            statusCode(HttpStatus.SC_OK)
        }
    }
}