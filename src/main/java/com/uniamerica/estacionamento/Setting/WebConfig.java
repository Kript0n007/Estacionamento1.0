package com.uniamerica.estacionamento.Setting;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
@CrossOrigin("http://localhost:3001")
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/marca")
                .allowedOrigins("http://localhost:3001/marca")
                .allowedMethods("GET, POST, PUT, DELETE")
                .allowCredentials(true);

        registry.addMapping("/modelo")
                .allowedOrigins("http://localhost:3001/modelo")
                .allowedMethods("GET, POST, PUT, DELETE")
                .allowCredentials(true);


        registry.addMapping("/movimentacao")
                .allowedOrigins("http://localhost:3001/movimentacao")
                .allowedMethods("GET, POST, PUT, DELETE")
                .allowCredentials(true);


        registry.addMapping("/veiculo")
                .allowedOrigins("http://localhost:3001/veiculo")
                .allowedMethods("GET, POST, PUT, DELETE")
                .allowCredentials(true);


        registry.addMapping("/condutor")
                .allowedOrigins("http://localhost:3001/condutor")
                .allowedMethods("GET, POST, PUT, DELETE")
                .allowCredentials(true);
}
}