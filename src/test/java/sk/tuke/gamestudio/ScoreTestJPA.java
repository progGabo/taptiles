/*package sk.tuke.gamestudio;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.ScoreServiceJPA;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringClient.class
)
class ScoreTestJPA {

    @Autowired
    private ScoreService scoreService;

    @Test
    void addScoreTest() {
        scoreService.addScore(new Score("taptiles", "Gabo", 100, new Date()));

        var scores = scoreService.getTopScores("taptiles");
        assertEquals(1, scores.size());
        assertEquals("taptiles", scores.get(0).getGame());
        assertEquals("Gabo", scores.get(0).getPlayer());
        assertEquals(100, scores.get(0).getPoints());
    }

    @Test
    void topScoresTest(){

        var date = new Date();
        scoreService.addScore(new Score("taptiles", "Miro", 150, date));
        scoreService.addScore(new Score("tiles", "Ema", 180, date));
        scoreService.addScore(new Score("taptiles", "Andrej", 100, date));

        var scores = scoreService.getTopScores("taptiles");

        assertEquals(3, scores.size());

        assertEquals("taptiles", scores.get(0).getGame());
        assertEquals("Gabo", scores.get(0).getPlayer());
        assertEquals(100, scores.get(0).getPoints());

        assertEquals("taptiles", scores.get(1).getGame());
        assertEquals("Miro", scores.get(1).getPlayer());
        assertEquals(150, scores.get(1).getPoints());

        assertEquals("taptiles", scores.get(2).getGame());
        assertEquals("Andrej", scores.get(2).getPlayer());
        assertEquals(100, scores.get(2).getPoints());

    }

}*/
