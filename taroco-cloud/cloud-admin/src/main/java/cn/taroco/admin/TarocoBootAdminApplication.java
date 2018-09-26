package cn.taroco.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * taroco 服务治理service
 *
 * @author liuht
 * @date 2017/11/18 20:24
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAdminServer
public class TarocoBootAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(TarocoBootAdminApplication.class, args);
    }

    @Configuration
    public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().permitAll()
                    .and().csrf().disable()
                    //允许使用iframe 嵌套
                    .headers().frameOptions().disable();
        }
    }
}
