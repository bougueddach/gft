package com.gft.gft.application.service

import com.gft.generated.model.ProductPriceResponse
import com.gft.gft.application.port.inbound.GetProductPricePort
import com.gft.gft.application.port.outbound.PriceRepositoryPort
import com.gft.gft.domain.exceptions.PriceNotFoundException
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneId


@Service
class GetProductPriceUseCase(
    private val priceRepositoryPort: PriceRepositoryPort
) : GetProductPricePort {

    override fun getProductPrice(
        productId: Long,
        date: OffsetDateTime,
        brandId: Long,
        timeZoneId: String
    ): ProductPriceResponse {
        val applicablePrices = priceRepositoryPort.findApplicablePrices(productId, brandId, date.toInstant())
        val selectedPrice = applicablePrices
            .maxByOrNull { it.priority }
            ?: throw PriceNotFoundException("No price found for product $productId at $date for brand $brandId")

        return ProductPriceResponse(
            productId = selectedPrice.productId,
            brandId = selectedPrice.brandId,
            priceList = selectedPrice.priceList,
            startDate = selectedPrice.startDate.atZone(ZoneId.of(timeZoneId)).toOffsetDateTime(),
            endDate = selectedPrice.endDate.atZone(ZoneId.of(timeZoneId)).toOffsetDateTime(),
            price = selectedPrice.price,
            currency = selectedPrice.currency
        )
    }
}