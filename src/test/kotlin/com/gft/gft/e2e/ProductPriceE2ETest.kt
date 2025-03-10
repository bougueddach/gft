package com.gft.gft.e2e


import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class ProductPriceE2ETest {

    companion object {
        @JvmStatic
        @BeforeAll
        fun setup() {
            RestAssured.baseURI = "http://localhost:8080"
        }
    }

    @Test
    fun `should return product price successfully`() {
        RestAssured.given()
            .header("x-timezone", "Europe/Paris")
            .contentType(ContentType.JSON)
            .queryParam("date", "2020-06-14T10:00:00Z")
            .queryParam("brandId", "1")
            .`when`()
            .get("/v1/products/35455/price")
            .then()
            .statusCode(200)
            .body("productId", equalTo(35455))
            .body("brandId", equalTo(1))
            .body("price", equalTo(35.50f))
            .body("currency", equalTo("EUR"))
    }
}