package com.jycx.minipro

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

import java.util.concurrent.CountDownLatch

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableCaching
//@EnableConfigurationProperties 使ConfigurationProperties生效，测试不使用该注解也可以生效，猜测是@Component起了类似作用
public class App {
	public static void main(String[] args) throws Exception {
		System.setProperty("spring.devtools.restart.enabled", "true");
		ApplicationContext ctx = SpringApplication.run(App.class, args);
		CountDownLatch latch = ctx.getBean(CountDownLatch.class);
		latch.await();
	}

}