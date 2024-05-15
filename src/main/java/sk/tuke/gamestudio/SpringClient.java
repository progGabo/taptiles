package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.game.GameCore.Field;
import sk.tuke.gamestudio.game.UI.UI;
import sk.tuke.gamestudio.service.*;

@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "sk.tuke.gamestudio.server.*"))
public class SpringClient {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public CommandLineRunner runner(UI ui) {
        return args -> ui.play();
    }
    @Bean
    public UI UI(Field field) {
        return new UI(field);
    }

    @Bean
    public Field consoleUI() {
        return new Field();
    }
    @Bean
    public ScoreService scoreService(){
        //return new ScoreServiceJPA();
        return new ScoreServiceRestClient();
    }
    @Bean
    public RatingService ratingService(){
        //return new RatingServiceJPA();
        return new RatingServiceRestClient();
    }
    @Bean
    public CommentService CommentService(){
        //return new CommentServiceJPA();
        return new CommentServiceRestClient();
    }
}
