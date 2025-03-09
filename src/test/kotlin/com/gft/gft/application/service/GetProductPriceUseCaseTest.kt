package com.gft.gft.application.service

import com.gft.generated.model.ProductPriceResponse
import com.gft.gft.domain.exceptions.PriceNotFoundException
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.whenever
import com.gft.gft.application.port.outbound.PriceRepositoryPort
import com.gft.gft.domain.model.Price
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.*

private const val PRODUCT_ID = 35455L
private const val BRAND_ID = 1L
private const val CET = "Europe/Madrid"
private val PRICE_LOW_PRIORITY = Price(
    id = 1,
    brandId = 1,
    startDate = Instant.parse("2020-06-14T00:00:00Z"),
    endDate = Instant.parse("2020-12-31T23:59:59Z"),
    priceList = 1,
    productId = PRODUCT_ID,
    priority = 0,
    price = 35.50,
    currency = "EUR"
)
private val PRICE_HIGH_PRIORITY = Price(
    id = 2,
    brandId = 1L,
    startDate = Instant.parse("2020-06-14T15:00:00Z"),
    endDate = Instant.parse("2020-06-14T18:30:00Z"),
    priceList = 2,
    productId = PRODUCT_ID,
    priority = 1,
    price = 25.45,
    currency = "EUR"
)
@ExtendWith(MockitoExtension::class)
class GetProductPriceUseCaseTest {

    @Mock
    private lateinit var priceRepositoryPort: PriceRepositoryPort

    @InjectMocks
    private lateinit var getProductPriceUseCase: GetProductPriceUseCase

    private val requestDate = OffsetDateTime.of(2020, 6, 14, 16, 0, 0, 0, ZoneOffset.UTC)

    @Test
    fun `should return the highest-priority price`() {


        whenever(priceRepositoryPort.findApplicablePrices(PRODUCT_ID, BRAND_ID, requestDate.toInstant()))
            .thenReturn(listOf(PRICE_LOW_PRIORITY, PRICE_HIGH_PRIORITY))

        val result: ProductPriceResponse = getProductPriceUseCase.getProductPrice(PRODUCT_ID, requestDate, BRAND_ID, CET)

        assertEquals(35455L, result.productId)
        assertEquals(1L, result.brandId)
        assertEquals(2, result.priceList) // Highest-priority price
        assertEquals(25.45, result.price)
        assertEquals("EUR", result.currency)
        assertEquals("Europe/Madrid", CET)
    }

    @Test
    fun `should throw PriceNotFoundException when no applicable prices exist`() {
        whenever(priceRepositoryPort.findApplicablePrices(PRODUCT_ID, BRAND_ID, requestDate.toInstant()))
            .thenReturn(emptyList())

        val exception = assertThrows<PriceNotFoundException> {
            getProductPriceUseCase.getProductPrice(PRODUCT_ID, requestDate, BRAND_ID, CET)
        }

        assertEquals("No price found for product $PRODUCT_ID at $requestDate for brand $BRAND_ID", exception.message)
    }

    @Test
    fun `should correctly convert startDate and endDate to the requested timezone`() {

        whenever(priceRepositoryPort.findApplicablePrices(PRODUCT_ID, BRAND_ID, requestDate.toInstant()))
            .thenReturn(listOf(PRICE_HIGH_PRIORITY))

        val result = getProductPriceUseCase.getProductPrice(PRODUCT_ID, requestDate, BRAND_ID, CET)

        val expectedStartDate = PRICE_HIGH_PRIORITY.startDate.atZone(ZoneId.of(CET)).toOffsetDateTime()
        val expectedEndDate = PRICE_HIGH_PRIORITY.endDate.atZone(ZoneId.of(CET)).toOffsetDateTime()

        assertEquals(expectedStartDate, result.startDate)
        assertEquals(expectedEndDate, result.endDate)
    }
}