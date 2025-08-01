package com.bookshop.edgeservice.web;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class WebEndpoints {

	@Bean
	public RouterFunction<ServerResponse> routerFunction() {
		return RouterFunctions.route()
				.GET("/product-fallback", request ->
						ServerResponse.ok().body(Mono.just(""), String.class))
				.POST("/product-fallback", request ->
						ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build())
				.build();
	}
	
}
