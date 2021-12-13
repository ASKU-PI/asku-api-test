package pl.asku.tests.magazine

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test
import pl.asku.tests.BaseTest
import java.time.LocalDate

class AddTest : BaseTest() {

    private val path: String = "/magazine/api/add"

    @Test
    fun `succeeds for correct input`() {
        Given {
            spec(requestSpecification)
            header(userAuthorizationHeader)
            multiPart("title", "Super Magazine")
            multiPart("country", "Poland")
            multiPart("city", "Kraków")
            multiPart("street", "Kawiory")
            multiPart("building", "21")
            multiPart("startDate", LocalDate.now())
            multiPart("endDate", LocalDate.now().plusDays(10))
            multiPart("areaInMeters", 100)
            multiPart("pricePerMeter", 3)
            multiPart("type", "CELL")
            multiPart("heating", "NONE")
            multiPart("light", true)
            multiPart("whole", false)
            multiPart("monitoring", true)
            multiPart("description", "Lorem ipsum")
        } When {
            post(path)
        } Then {
            statusCode(HttpStatus.SC_OK)
        }

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
    }
}