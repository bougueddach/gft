package com.gft.gft.infrastructure.persistence.repository

import com.gft.gft.application.port.outbound.PriceRepositoryPort
import com.gft.gft.domain.model.Price
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class PriceRepository(
    private val jpaRepository: JpaPriceRepository
) : PriceRepositoryPort {
    override fun findApplicablePrices(productId: Long, brandId: Long, date: Instant): List<Price> {
        return jpaRepository.findApplicablePrices(productId, brandId, date)
    }

    override fun saveAll(prices: Iterable<Price>): Iterable<Price> {
        return jpaRepository.saveAll(prices)
    }

    override fun deleteAll() {
        return jpaRepository.deleteAll()
    }
}