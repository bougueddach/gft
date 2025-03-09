package com.gft.gft.infrastructure.api

import com.gft.gft.application.port.inbound.GetProductPricePort
import com.gft.gft.domain.exceptions.PriceNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.anyString
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
class ExceptionHandlerTest @Autowired constructor(){
    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var getProductPricePort: GetProductPricePort

    @InjectMocks
    private lateinit var pricesController: PricesController

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(pricesController)
            .setControllerAdvice(ExceptionHandler())
            .build()
    }

    @Test
    fun `should return 404 when PriceNotFoundException is thrown`() {
        whenever(getProductPricePort.getProductPrice(anyLong(), anyOrNull(), anyLong(), anyString()))
            .thenThrow(PriceNotFoundException("Requested price not available."))

        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/products/99999/price")
                .param("date", "2024-03-09T12:00:00Z")
                .param("brandId", "1")
                .header("X-Timezone", "Europe/Madrid")  // ✅ Explicitly set the timezone
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Price Not Found"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Requested price not available."))
    }
}