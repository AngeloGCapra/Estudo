package br.upf.userdept;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Angelo G. Capra
 */
@SpringBootApplication
public class UserdeptApplication {
	
	/**
	 * Implementação necessária para utilizar nos controllers de edit.
	 * 
	 * @return
	 */
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		return modelMapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(UserdeptApplication.class, args);
	}

}
