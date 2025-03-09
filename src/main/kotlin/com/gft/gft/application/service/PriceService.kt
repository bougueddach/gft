package com.gft.gft.application.service

import com.gft.generated.model.ProductPriceResponse
import com.gft.gft.domain.exceptions.PriceNotFoundException
import com.gft.gft.infrastructure.persistence.repository.PriceRepository
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneId

@Service
class PriceService(
    val priceRepository: PriceRepository
) {
    fun getProductPrice(productId: Long, date: OffsetDateTime, brandId: Long, timeZone: String): ProductPriceResponse {
        val applicablePrices = priceRepository.findApplicablePrices(productId, brandId, date.toInstant())
        val selectedPrice = applicablePrices
            .maxByOrNull { it.priority }
            ?: throw PriceNotFoundException("No price found for product $productId at $date for brand $brandId")

        System.out.println(ZoneId.getAvailableZoneIds());
        return ProductPriceResponse(
            productId = selectedPrice.productId,
            brandId = selectedPrice.brandId,
            priceList = selectedPrice.priceList,
            startDate = selectedPrice.startDate.atZone(ZoneId.of(timeZone)).toOffsetDateTime(),
            endDate = selectedPrice.endDate.atZone(ZoneId.of(timeZone)).toOffsetDateTime(),
            price = selectedPrice.price,
            currency = selectedPrice.currency
        )
    }

}