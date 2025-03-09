package com.gft.gft.infrastructure.persistence.repository

import com.gft.gft.domain.model.Price
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.Instant

interface JpaPriceRepository : CrudRepository<Price, Long> {
    @Query(
        """
        SELECT p FROM Price p 
        WHERE p.productId = :productId 
        AND p.brandId = :brandId
        AND :date BETWEEN p.startDate AND p.endDate
    """
    )
    fun findApplicablePrices(productId: Long, brandId: Long, date: Instant): List<Price>
}