package com.cocodan.triplan;

import com.cocodan.triplan.util.ExceptionMessageUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
public class TriplanApplication {

	public static void main(String[] args) {
		SpringApplication.run(TriplanApplication.class, args);
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("exceptions");
		messageSource.setDefaultEncoding("UTF-8");

		return messageSource;
	}

	@Bean
	public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource){
		MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource);

		ExceptionMessageUtils.setMessageSourceAccessor(messageSourceAccessor);

		return messageSourceAccessor;
	}
}
