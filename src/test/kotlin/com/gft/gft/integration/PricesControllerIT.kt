package com.gft.gft.integration


import com.gft.gft.domain.model.Price
import com.gft.gft.infrastructure.persistence.repository.PriceRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.Instant

private const val VALID_PRODUCT_ID = 35455L
private const val VALID_BRAND_ID = 1L

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PricesControllerIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var priceRepository: PriceRepository

    companion object {
        private const val VALID_PRODUCT_ID = 35455L
        private const val VALID_BRAND_ID = 1L
    }

    @BeforeEach
    fun setup() {
        priceRepository.deleteAll()

        val testPrices = listOf(
            Price(
                brandId = VALID_BRAND_ID,
                startDate = Instant.parse("2020-06-14T00:00:00Z"),
                endDate = Instant.parse("2020-12-31T23:59:59Z"),
                priceList = 1,
                productId = VALID_PRODUCT_ID,
                priority = 0,
                price = 35.50,
                currency = "EUR"
            ),
            Price(
                brandId = VALID_BRAND_ID,
                startDate = Instant.parse("2020-06-14T15:00:00Z"),
                endDate = Instant.parse("2020-06-14T18:30:00Z"),
                priceList = 2,
                productId = VALID_PRODUCT_ID,
                priority = 1,
                price = 25.45,
                currency = "EUR"
            )
        )

        priceRepository.saveAll(testPrices)
    }

    @Test
    fun `should return the highest-priority price when multiple prices exist`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/products/$VALID_PRODUCT_ID/price")
                .param("date", "2020-06-14T16:00:00Z")
                .param("brandId", VALID_BRAND_ID.toString())
                .header("x-timezone", "Europe/Madrid")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.productId").value(VALID_PRODUCT_ID))
            .andExpect(MockMvcResultMatchers.jsonPath("$.brandId").value(VALID_BRAND_ID))
            .andExpect(MockMvcResultMatchers.jsonPath("$.priceList").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(25.45))
            .andExpect(MockMvcResultMatchers.jsonPath("$.currency").value("EUR"))
    }

    @Test
    fun `should return 404 Not Found when no applicable prices exist`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/products/$VALID_PRODUCT_ID/price")
                .param("date", "2025-06-14T16:00:00Z")
                .param("brandId", VALID_BRAND_ID.toString())
                .header("x-timezone", "Europe/Madrid")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Price Not Found"))
    }

    @Test
    fun `should return 400 Bad Request when required parameters are missing`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/products/$VALID_PRODUCT_ID/price")
                .header("x-timezone", "Europe/Madrid")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `should return 400 Bad Request when required header is missing`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/products/$VALID_PRODUCT_ID/price")
                .param("date", "2020-06-14T16:00:00Z")
                .param("brandId", VALID_BRAND_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}