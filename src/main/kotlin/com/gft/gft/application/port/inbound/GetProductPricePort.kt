package com.gft.gft.application.port.inbound

import com.gft.generated.model.ProductPriceResponse
import java.time.OffsetDateTime

interface GetProductPricePort {
    fun getProductPrice(productId: Long, date: OffsetDateTime, brandId: Long, zoneId: String): ProductPriceResponse

}