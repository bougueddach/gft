package com.gft.gft.domain.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "prices")
data class Price(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val brandId: Long,

    val startDate: Instant,

    val endDate: Instant,

    val priceList: Int,

    val productId: Long,

    val priority: Int,

    val price: Double,

    val currency: String
) {
}