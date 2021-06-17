package br.com.edmilson.sicredi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("spring.datasource")
public class DBConfiguration {

	private String driverClassName;
	private String url;	
	
	@Profile("test")
	@Bean
	public String testDataBaseConnection() {
		System.out.println("DB connection for Test - H2");
		System.out.println(driverClassName);
		System.out.println(url);
		return "DB Connection to H2_TEST - Test instance";
	}
	
	@Profile("dev")
	@Bean
	public String devDataBaseConnection() {
		System.out.println("DB connection for DEV - Postgres");
		System.out.println(driverClassName);
		System.out.println(url);
		return "DB Connection to Postgres - Dev instance";
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}	
	
}
