package com.gft.gft.infrastructure.api


import com.gft.generated.model.ProductPriceResponse
import com.gft.gft.application.port.inbound.GetProductPricePort
import com.gft.gft.domain.exceptions.PriceNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.OffsetDateTime
import java.time.ZoneOffset

private const val VALID_PRODUCT_ID = 35455L
private const val VALID_BRAND_ID = 1L
private val VALID_DATE: OffsetDateTime = OffsetDateTime.of(2020, 6, 14, 16, 0, 0, 0, ZoneOffset.UTC)
private const val VALID_TIMEZONE = "Europe/Madrid"

@ExtendWith(MockitoExtension::class)
class PricesControllerTest {

    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var getProductPricePort: GetProductPricePort

    @InjectMocks
    private lateinit var pricesController: PricesController


    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(pricesController)
            .setControllerAdvice(ExceptionHandler())
            .build()
    }

    @Test
    fun `should return the correct price when valid parameters are provided`() {
        val mockResponse = ProductPriceResponse(
            productId = VALID_PRODUCT_ID,
            brandId = VALID_BRAND_ID,
            priceList = 2,
            startDate = VALID_DATE,
            endDate = VALID_DATE.plusHours(3),
            price = 25.45,
            currency = "EUR"
        )

        whenever(getProductPricePort.getProductPrice(any(), any(), any(), any()))
            .thenReturn(mockResponse)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/products/$VALID_PRODUCT_ID/price")
                .param("date", VALID_DATE.toString())
                .param("brandId", VALID_BRAND_ID.toString())
                .header("x-timezone", VALID_TIMEZONE)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.productId").value(VALID_PRODUCT_ID))
            .andExpect(MockMvcResultMatchers.jsonPath("$.brandId").value(VALID_BRAND_ID))
            .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(25.45))
            .andExpect(MockMvcResultMatchers.jsonPath("$.currency").value("EUR"))
    }

    @Test
    fun `should return 404 Not Found when PriceNotFoundException is thrown`() {
        whenever(getProductPricePort.getProductPrice(any(), any(), any(), any()))
            .thenThrow(PriceNotFoundException("No price found for product $VALID_PRODUCT_ID at $VALID_DATE for brand $VALID_BRAND_ID"))

        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/products/$VALID_PRODUCT_ID/price")
                .param("date", VALID_DATE.toString())
                .param("brandId", VALID_BRAND_ID.toString())
                .header("x-timezone", VALID_TIMEZONE)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Price Not Found"))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.message")
                    .value("No price found for product $VALID_PRODUCT_ID at $VALID_DATE for brand $VALID_BRAND_ID")
            )
    }

    @Test
    fun `should return 400 Bad Request when required query parameters are missing`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/products/$VALID_PRODUCT_ID/price")
                .header("x-timezone", VALID_TIMEZONE)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `should return 400 Bad Request when required header is missing`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/products/$VALID_PRODUCT_ID/price")
                .param("date", VALID_DATE.toString())
                .param("brandId", VALID_BRAND_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}