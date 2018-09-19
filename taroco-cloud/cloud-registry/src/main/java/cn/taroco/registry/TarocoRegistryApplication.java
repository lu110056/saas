package cn.taroco.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 注册中心
 *
 * @author liuht
 */
@EnableEurekaServer
@SpringBootApplication
public class TarocoRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(TarocoRegistryApplication.class, args);
	}
}
