package cn.jyl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@EnableCircuitBreaker
public class ServiceBApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceBApplication.class, args);
    }

//    @Configuration
//    public static class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.cors().and().csrf().disable().authorizeRequests()
//                    .antMatchers(HttpMethod.POST, "/users/signup").permitAll()
//                    .anyRequest().authenticated()
//                    .and()
//                    .addFilter(new JWTAuthenticationFilter(authenticationManager()));
//        }
//    }

}