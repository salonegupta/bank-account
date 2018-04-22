package com.banking;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.banking.dao.AccountDao;
import com.banking.model.Account;

@SpringBootApplication
@ComponentScan(value = "com.banking")
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		setRegisterErrorPageFilter(false);
		return application.sources(Application.class);
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:i18n/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public CommandLineRunner init(AccountDao accountDao) {
		return (args) -> {
			// create a Global account
			accountDao.save(new Account("GLOBAL", 0));
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
