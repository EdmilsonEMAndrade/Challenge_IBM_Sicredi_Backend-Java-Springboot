package br.com.edmilson.sicredi.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
    public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.edmilson.sicredi"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, responseMessageForAny())
                .globalResponseMessage(RequestMethod.POST, responseMessageForAny())
                .apiInfo(metaInfo());
    }

    private ApiInfo metaInfo() {

        ApiInfo apiInfo = new ApiInfo(
                "Apis Desafios Sicredi",
                "API REST ",
                "1.0",
                "Terms of Service",
                new Contact("Edmilson Emmanuel Moura Andrade", "https://www.linkedin.com/in/edmilson-emmanuel-moura-andrade/",
                        "edmilson.andrad@gmail.com"),
                "Apache License Version 2.0",
                "https://www.apache.org/licesen.html", new ArrayList<VendorExtension>()
        );

        return apiInfo;
    }
    
    private List<ResponseMessage> responseMessageForAny()
    {
        return new ArrayList<ResponseMessage>() {{
            add(new ResponseMessageBuilder()
                .code(400)
                .message("{\r\n"
    		    		+ "  \"status\": 400,\r\n"
    		    		+ "  \"mensagem\": \"Mensagem de Erro\",\r\n"
    		    		+ "  \"timeStamp\": tempo\r\n"
    		    		+ "}")
                .build());            
        }};
    }
}
