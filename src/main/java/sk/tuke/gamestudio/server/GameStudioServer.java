package sk.tuke.gamestudio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.tuke.gamestudio.service.*;

@SpringBootApplication
@Configuration
@EntityScan("sk.tuke.gamestudio.entity")
public class GameStudioServer {
    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class, args);
    }

    @Bean
    public CommentService commentServiceR(){
        return new CommentServiceJPA();
    }
    @Bean
    public RatingService ratingServiceR(){
        return new RatingServiceJPA();
    }
    @Bean
    public ScoreService scoreServiceR(){
        return new ScoreServiceJPA();
    }
    @Bean
    public UserService userServiceR(){
        return new UserServiceJPA();
    }
}
