package com.gft.gft

import com.gft.generated.api.ProductPricesApi
import com.gft.generated.model.GetProductPrice200Response
import io.swagger.v3.oas.annotations.Parameter
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@Slf4j
class PricesController : ProductPricesApi {

    override fun getProductPrice(
        @Parameter(
            description = "Product ID to query.",
            required = true
        ) @PathVariable("productId") productId: kotlin.Long,
        @NotNull @Parameter(
            description = "Application date and time (ISO-8601 format).",
            required = true
        ) @Valid @RequestParam(
            value = "date",
            required = true
        ) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) date: java.time.OffsetDateTime,
        @NotNull @Parameter(
            description = "Brand ID (e.g., 1 for ZARA).",
            required = true
        ) @Valid @RequestParam(value = "brandId", required = true) brandId: kotlin.Long
    ): ResponseEntity<GetProductPrice200Response> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}