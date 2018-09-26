package cn.taroco.auth2.sso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述
 *
 * @author liuht
 * @date 2018/8/31 9:48
 */
@EnableOAuth2Sso
@SpringBootApplication
public class TarocoSsoDemo2Application {

    public static void main(String[] args) {
        SpringApplication.run(TarocoSsoDemo2Application.class, args);
    }

    @Bean
    OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext, OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oauth2ClientContext);
    }

    @RestController
    static class HomeController {

        @Autowired
        OAuth2RestTemplate restTemplate;

        @GetMapping("/")
        public Authentication user(Authentication authentication) {
            return authentication;
        }

        @GetMapping("/user")
        public Object userinfo(Authentication authentication) {
            return authentication.getPrincipal();
        }

    }

}
