package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;

import java.util.Arrays;
import java.util.List;


public class RatingServiceRestClient implements RatingService{
    private final String url = "http://localhost:8080/api/rating";

    @Autowired
    private RestTemplate restTemplate;
    @Override
    public void setRating(Rating rating) throws RatingException {
        restTemplate.postForEntity(url, rating, Rating.class);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        return restTemplate.getForEntity(url + "/" + game + "/average", int.class).getBody();
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        return restTemplate.getForEntity(url + "/" + game + "/" + player , int.class).getBody();
    }

    @Override
    public List<Rating> getRatings(String game) {
        return Arrays.asList(restTemplate.getForEntity(url + "/"+ game, Rating[].class).getBody());
    }

    @Override
    public void reset() throws RatingException {
        throw new UnsupportedOperationException("No supported via web service");
    }
}
