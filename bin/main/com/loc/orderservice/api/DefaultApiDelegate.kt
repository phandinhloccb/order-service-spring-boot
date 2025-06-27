package com.loc.orderservice.api

import com.loc.orderservice.model.ErrorResponse
import com.loc.orderservice.model.OrderRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.core.io.Resource

import java.util.Optional

/**
 * A delegate to be called by the {@link DefaultApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@jakarta.annotation.Generated(value = ["org.openapitools.codegen.languages.KotlinSpringServerCodegen"], comments = "Generator version: 7.4.0")
interface DefaultApiDelegate {

    fun getRequest(): Optional<NativeWebRequest> = Optional.empty()

    /**
     * @see DefaultApi#placeOrder
     */
    fun placeOrder(orderRequest: OrderRequest): ResponseEntity<kotlin.String> {
        getRequest().ifPresent { request ->
            for (mediaType in MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf(""))) {
                    ApiUtil.setExampleResponse(request, "", "")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"path\" : \"/api/order\",  \"error\" : \"Not Found\",  \"message\" : \"Product with Skucode: IPHONE13-128 is not in stock\",  \"timestamp\" : \"2023-05-20T14:30:15.123Z\",  \"status\" : 404}")
                    break
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"path\" : \"/api/order\",  \"error\" : \"Not Found\",  \"message\" : \"Product with Skucode: IPHONE13-128 is not in stock\",  \"timestamp\" : \"2023-05-20T14:30:15.123Z\",  \"status\" : 404}")
                    break
                }
            }
        }
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)

    }

}
