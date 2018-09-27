package cn.taroco.oauth2.server.config;

import cn.taroco.oauth2.config.AbstractSecurityConfig;
import cn.taroco.oauth2.config.FilterIgnorePropertiesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * WebSecurity配置类
 *
 * @author liuht
 * @date 2018/7/24 15:53
 */
@Configuration
@EnableConfigurationProperties(FilterIgnorePropertiesConfig.class)
public class WebSecurityConfig extends AbstractSecurityConfig {

    @Autowired
    private FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry =
                http.formLogin().loginPage("/authentication/require")
                        .loginProcessingUrl("/authentication/form")
                        .successForwardUrl("/authentication/loginSuccess")
                        .failureUrl("/authentication/require?error=true")
                        .and()
                        .authorizeRequests();
        filterIgnorePropertiesConfig
                .getUrls()
                .forEach(url -> registry.antMatchers(url).permitAll());
        registry.anyRequest().authenticated()
                .and()
                .csrf().disable();
    }
}
