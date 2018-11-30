package com.jycx.minipro.init;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


import java.util.concurrent.CountDownLatch;

/**
 * 初始化配置类
 * 
 * @author fengym
 *
 */
@Configuration
@EnableAutoConfiguration
@EnableAsync
@ImportResource({"spring-context.xml"})
public class PrimaryConfiguration {
	private Log log = LogFactory.getLog(PrimaryConfiguration.class);

	/**
	 * 配置 Jetty
	 * 
	 * @return JettyEmbeddedServletContainerFactory
	 */
	
	@Bean
	public JettyServletWebServerFactory servletContainer() {
		JettyServletWebServerFactory jetty = new JettyServletWebServerFactory();
		jetty.addServerCustomizers(new JettyServerCustomizer() {

			@Override
			public void customize(Server server) {
				HandlerCollection handlers = new HandlerCollection();
				for (Handler handler : server.getHandlers()) {
					handlers.addHandler(handler);
				}
				RequestLogHandler reqLogs = new RequestLogHandler();
				NCSARequestLog reqLogImpl = new NCSARequestLog(
						"/opt/logs/mobile/minipro/minipro.request.log.yyyy_mm_dd");
				reqLogImpl.setFilenameDateFormat("yyyy-MM-dd");
				reqLogImpl.setRetainDays(90);
				reqLogImpl.setAppend(true);
				reqLogImpl.setExtended(false);
				reqLogImpl.setLogCookies(false);
				reqLogImpl.setLogTimeZone("GMT+8");
				reqLogImpl.setLogLatency(true);
				reqLogs.setRequestLog(reqLogImpl);
				handlers.addHandler(reqLogs);
				server.setHandler(handlers);

			}
		});
		
		jetty.addConfigurations();

		return jetty;
	}


	@Bean
	public CountDownLatch latch() {
		return new CountDownLatch(1);
	}


	@Autowired
	private org.springframework.core.env.Environment env;


	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(3000);
		return executor;
	}

	@Bean(name="httpExecutor")
	public TaskExecutor httpExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1);
		executor.setMaxPoolSize(1);
		executor.setQueueCapacity(3000);
		return executor;
	}


	@Bean(name = "springCM")
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager("entities", "templates","names");
	}



}
