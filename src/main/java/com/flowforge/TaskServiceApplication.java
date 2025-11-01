package com.flowforge;

import com.flowforge.config.CRLFLogConverter;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@SpringBootApplication
public class TaskServiceApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TaskServiceApplication.class);
		Environment env = app.run(args).getEnvironment();
		logApplicationStartup(env);
	}

	//////////////////// DISPLAY API INFO AFTER SUCCESSFUL RUN //////////////////////
	//////////////////// DISPLAY API INFO AFTER SUCCESSFUL RUN //////////////////////
	//////////////////// DISPLAY API INFO AFTER SUCCESSFUL RUN //////////////////////
	//////////////////// DISPLAY API INFO AFTER SUCCESSFUL RUN //////////////////////
	//////////////////// DISPLAY API INFO AFTER SUCCESSFUL RUN //////////////////////
	private static final Logger log = LoggerFactory.getLogger(TaskServiceApplication.class);
	private final Environment env;

	public TaskServiceApplication(Environment env) {
		this.env = env;
	}

	@PostConstruct
	public void initApplication() {
		Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
		if (activeProfiles.contains("dev") && activeProfiles.contains("prod")) {
			log.error(
					"You have misconfigured your application! It should not run " + "with both the 'dev' and 'prod' profiles at the same time."
			);
		}
		if (activeProfiles.contains("dev") && activeProfiles.contains("staging")) {
			log.error(
					"You have misconfigured your application! It should not " + "run with both the 'dev' and 'staging' profiles at the same time."
			);
		}
		if (activeProfiles.contains("dev") && activeProfiles.contains("local")) {
			log.error(
					"You have misconfigured your application! It should not " + "run with both the 'dev' and 'local' profiles at the same time."
			);
		}
	}

	private static void logApplicationStartup(Environment env) {
		String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store"))
				.map(key -> "https").orElse("http");
		String serverPort = env.getProperty("server.port");
		String contextPath = Optional
				.ofNullable(env.getProperty("server.servlet.context-path"))
				.filter(StringUtils::isNotBlank)
				.orElse("/");
		String hostAddress = "localhost";
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.warn("The host name could not be determined, using `localhost` as fallback");
		}
		log.info(
				CRLFLogConverter.CRLF_SAFE_MARKER,
				"""
                        \n
                        █████████████████████████████████████████████████████████████████████████████████████████████████████████
                        █████████████████████████████████████████████████████████████████████████████████████████████████████████
                        █████████████████████████████████████████████████████████████████████████████████████████████████████████
                        █████████████████████████████████████████████████████████████████████████████████████████████████████████
                        █████████████████████████████████████████████████████████████████████████████████████████████████████████
                        █████████████████████████████████████████████████████████████████████████████████████████████████████████
                        ---------------------------------------------------------------------------------------------------------
                        ---------------------------------------------------------------------------------------------------------
                        ---------------------------------------------------------------------------------------------------------
                        \tApplication: \t'{}' is running! Access URLs:
                        \tLocal: \t\t\t{}://localhost:{}{}
                        \tExternal: \t\t{}://{}:{}{}
                        \tProfile(s): \t{}
                        ---------------------------------------------------------------------------------------------------------
                        ---------------------------------------------------------------------------------------------------------
                        ---------------------------------------------------------------------------------------------------------""",
				env.getProperty("spring.application.name"),
				protocol,
				serverPort,
				contextPath,
				protocol,
				hostAddress,
				serverPort,
				contextPath,
				env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
		);

		String configServerStatus = env.getProperty("configserver.status");
		if (configServerStatus == null) {
			configServerStatus = "Not found or not setup for this application";
		}
		log.info(
				CRLFLogConverter.CRLF_SAFE_MARKER,
				"\n---------------------------------------------------------------------------------------------------------" +
						"\n---------------------------------------------------------------------------------------------------------\n" +
						"\tConfig Server: \t{}" +
						"\n---------------------------------------------------------------------------------------------------------" +
						"\n---------------------------------------------------------------------------------------------------------" +
						"\n---------------------------------------------------------------------------------------------------------" +
						"\n█████████████████████████████████████████████████████████████████████████████████████████████████████████" +
						"\n█████████████████████████████████████████████████████████████████████████████████████████████████████████" +
						"\n█████████████████████████████████████████████████████████████████████████████████████████████████████████" +
						"\n█████████████████████████████████████████████████████████████████████████████████████████████████████████" +
						"\n█████████████████████████████████████████████████████████████████████████████████████████████████████████" +
						"\n█████████████████████████████████████████████████████████████████████████████████████████████████████████" +
						"\n█████████████████████████████████████████████████████████████████████████████████████████████████████████",
				configServerStatus
		);
	}
}
