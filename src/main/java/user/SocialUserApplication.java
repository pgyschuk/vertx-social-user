package user;


import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import user.repository.UserRepository;

@SpringBootApplication
@Configuration
public class SocialUserApplication extends AbstractVerticle {

    @Autowired
    private SocialUserVerticle socialUserVerticle;

    @Bean
    UserRepository userRepository(){
        return new UserRepository(getVertx());
    }

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
        b.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return b;
    }

    public static void main(String[] args) {
        SpringApplication.run(SocialUserApplication.class, args);
    }

    @PostConstruct
    public void deployVerticle() {
        Vertx.vertx().deployVerticle(socialUserVerticle);
    }
}
