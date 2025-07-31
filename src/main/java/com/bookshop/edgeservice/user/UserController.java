package com.bookshop.edgeservice.user;

import reactor.core.publisher.Mono;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

	@GetMapping("user")
	public Mono<User> getUser(@AuthenticationPrincipal OidcUser oidcUser) {//현재 인증된 사용자에 대한 정보를 가지고 있는 객체 주입
		var user = new User(
				oidcUser.getPreferredUsername(),
				oidcUser.getGivenName(),
				oidcUser.getFamilyName(),
				//List.of("employee", "customer"),
				oidcUser.getClaimAsStringList("roles") //roles 클레임을 추출해 문자열의 리스트로 가져온다.

		);
		return Mono.just(user); //에지 서비스는 리액티브 애플리케이션이기에 사용자 객체를 리액티브 발행자로 감싼다.
	}

}
