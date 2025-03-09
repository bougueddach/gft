package com.gft.gft.application.port.outbound

import com.gft.gft.domain.model.Price
import java.time.Instant

interface PriceRepositoryPort {
    fun findApplicablePrices(productId: Long, brandId: Long, date: Instant): List<Price>
    fun saveAll(prices: Iterable<Price>): Iterable<Price>
}