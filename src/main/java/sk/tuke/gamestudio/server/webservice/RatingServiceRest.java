package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.RatingService;

import java.util.List;

@RestController
@RequestMapping("/api/rating")
public class RatingServiceRest {

    @Autowired
    private RatingService ratingService;

    @PostMapping
    public void setRating(@RequestBody Rating rating){
        ratingService.setRating(rating);
    }
    @GetMapping("/{game}/{player}")
    public int getRating(@PathVariable String game,@PathVariable String player){
        return ratingService.getRating(game,player);
    }

    @GetMapping("/{game}")
    public List<Rating> getRatings(@PathVariable String game) {
        return ratingService.getRatings(game);
    }

    @GetMapping("/{game}/average")
    public int getAvgRating(@PathVariable String game){
        return ratingService.getAverageRating(game);
    }

}
