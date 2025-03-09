package com.gft.gft.infrastructure.persistence

import com.gft.gft.application.port.outbound.PriceRepositoryPort
import com.gft.gft.domain.model.Price
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class DataSeeder {
    @Bean
    fun seedData(priceRepositoryPort: PriceRepositoryPort): CommandLineRunner {
        return CommandLineRunner {
            val prices = listOf(
                Price(
                    brandId = 1,
                    startDate = Instant.parse("2020-06-14T00:00:00Z"),
                    endDate = Instant.parse("2020-12-31T23:59:59Z"),
                    priceList = 1,
                    productId = 35455,
                    priority = 0,
                    price = 35.50,
                    currency = "EUR"
                ),
                Price(
                    brandId = 1,
                    startDate = Instant.parse("2020-06-14T15:00:00Z"),
                    endDate = Instant.parse("2020-06-14T18:30:00Z"),
                    priceList = 2,
                    productId = 35455,
                    priority = 1,
                    price = 25.45,
                    currency = "EUR"
                ),
                Price(
                    brandId = 1,
                    startDate = Instant.parse("2020-06-15T00:00:00Z"),
                    endDate = Instant.parse("2020-06-15T11:00:00Z"),
                    priceList = 3,
                    productId = 35455,
                    priority = 1,
                    price = 30.50,
                    currency = "EUR"
                ),
                Price(
                    brandId = 1,
                    startDate = Instant.parse("2020-06-15T16:00:00Z"),
                    endDate = Instant.parse("2020-12-31T23:59:59Z"),
                    priceList = 4,
                    productId = 35455,
                    priority = 1,
                    price = 38.95,
                    currency = "EUR"
                )
            )
            priceRepositoryPort.saveAll(prices)
        }
    }
}