// package com.loc.order_service.configuration

// import org.springframework.beans.factory.annotation.Value
// import org.springframework.context.annotation.Bean
// import org.springframework.context.annotation.Configuration
// import org.springframework.http.MediaType
// import org.springframework.web.reactive.function.client.WebClient
// import org.springframework.web.reactive.function.client.bodyToMono
// import org.springframework.web.reactive.function.server.ServerResponse
// import mu.KotlinLogging

// @Configuration
// class InventoryServiceConfig(
//     @Value("\${servers.include-warmup:false}") private val warmup: Boolean
// ) {
//     private val log = KotlinLogging.logger {}

//     @Bean
//     fun inventoryApi(@Value("\${inventory.service.url}") baseUrl: String) = DefaultApi(
//         ApiClient()
//             .apply { basePath = baseUrl }
//             .also { it.webClient.warmup(it.basePath) }
//     )

//     private fun WebClient.warmup(basePath: String) {
//         if (!warmup) return
//         log.info("Sending request $basePath/warmup to reduce lazy load for first slow request")
//         get()
//             .uri("$basePath/warmup")
//             .retrieve().bodyToMono<String>().onErrorReturn("{}")
//             .flatMap { s -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(s) }
//             .block()
//     }
// } 