package com.gft.gft.infrastructure.api

import com.gft.generated.api.ProductPricesApi
import com.gft.generated.model.ProductPriceResponse
import com.gft.gft.application.port.inbound.GetProductPricePort
import com.gft.gft.application.service.PriceService
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import lombok.extern.slf4j.Slf4j
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@Slf4j
class PricesController(
    private val getProductPricePort: GetProductPricePort
) : ProductPricesApi {

    override fun getProductPrice(
        @Parameter(
            description = "Product ID to query.",
            required = true
        ) @PathVariable("productId") productId: Long,
        @NotNull @Parameter(
            description = "Application date and time (ISO-8601 format).",
            required = true
        ) @Valid @RequestParam(
            value = "date",
            required = true
        ) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) date: OffsetDateTime,
        @NotNull @Parameter(
            description = "Brand ID (e.g., 1 for ZARA).",
            required = true
        ) @Valid @RequestParam(value = "brandId", required = true) brandId: Long,
        @Parameter(description = "", `in` = ParameterIn.HEADER, required = true) @RequestHeader(
            value = "x-timezone",
            required = true
        ) xTimezone: String
    ): ResponseEntity<ProductPriceResponse> {
        val response = getProductPricePort.getProductPrice(productId, date, brandId, xTimezone)
        return ResponseEntity.ok(response)
    }
}