package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.GameCore.Difficulty;
import sk.tuke.gamestudio.game.GameCore.Field;
import sk.tuke.gamestudio.game.GameCore.Tile;
import sk.tuke.gamestudio.game.GameCore.TileState;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;

@Controller
@RequestMapping("/taptiles")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class TaptilesController {
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserController userController;
    private Field field = new Field();
    private int level = 1;
    private boolean randomGame = true;
    public TaptilesController(){
        field.generateRandom(Difficulty.EASY);
        field.resetScore();
    }

    @RequestMapping
    public String taptiles(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column, Model model) {
        if(row != null && column != null){
            field.markTile(row,column);
        }
        if(field.getSourceTile() != null && field.getDestinationTile() != null){
            field.checkMove(field.getSourceTile(), field.getDestinationTile());
        }

        prepareModel(model);
        model.addAttribute("field", getHtmlField());
        return "taptiles";
    }

    @RequestMapping("/campaign")
    public String Campaign(Model model){
        if(randomGame){
            field.resetScore();
        }

        field.generateFromFile("src/main/java/sk/tuke/gamestudio/game/Levels/Level_"+ level);
        level++;

        if(level == 6) {
            resetCampaign();
            setScore();
            field.resetScore();
        }

        randomGame = false;
        prepareModel(model);
        return "taptiles";
    }

    public void resetCampaign(){
        level = 1;
    }

    @PostMapping("/addcomment")
    public String addComment(String comment){
        if(userController.isLogged()){
            commentService.addComment(new Comment("taptiles", userController.getUserName(), comment, new Date()));
        }
        return "redirect:/taptiles";
    }

    @PostMapping("/addrating")
    public String addRating(int rating){
        if(userController.isLogged()){
            ratingService.setRating(new Rating("taptiles", userController.getUserName(), rating, new Date()));
        }
        return "redirect:/taptiles";
    }

    @RequestMapping("/random/easy")
    public String easyGame(Model model){
        setScore();
        field.resetScore();
        field.generateRandom(Difficulty.EASY);
        randomGame = true;
        resetCampaign();

        prepareModel(model);

        return "taptiles";
    }

    @RequestMapping("/random/medium")
    public String mediumGame(Model model){
        setScore();
        field.resetScore();
        field.generateRandom(Difficulty.MEDIUM);
        randomGame = true;
        resetCampaign();

        prepareModel(model);

        return "taptiles";
    }

    @RequestMapping("/random/hard")
    public String hardGame(Model model){
        setScore();
        field.resetScore();
        field.generateRandom(Difficulty.HARD);
        randomGame = true;
        resetCampaign();

        prepareModel(model);

        return "taptiles";
    }

    public void setScore(){
        if(userController.isLogged() && field.getScore() > 0) {
            scoreService.addScore(new Score("taptiles", userController.getLoggedUser().getLogin(), field.getScore(), new Date()));
        }
    }

    public String showScore(){
        return String.valueOf(field.getScore());
    }

    public String getAvgRating(){
        return String.valueOf(ratingService.getAverageRating("taptiles"));
    }
    public String getHtmlField(){
        StringBuilder sb = new StringBuilder();
        sb.append("<table>\n");
        for(int row = 0; row < field.getRow_count(); row++){
            sb.append("<tr>\n");
            for(int column = 0; column < field.getColumn_count(); column++){
                var tile = field.getTile(row, column);
                sb.append("<td>\n");
                sb.append("<a href='/taptiles?row=" + row + "&column=" + column + "'>\n");
                if(tile.getState() == TileState.SOURCE || tile.getState() == TileState.DEST) {
                    sb.append("<img class='imgB' src='/images/" + getTileImage(tile) + ".png'");
                }
                else{
                    sb.append("<img src='/images/" + getTileImage(tile) + ".png'>");
                }
                sb.append("</a>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }

        sb.append("</table>\n");
        return sb.toString();
    }

    public String getTileImage(Tile tile){

        for(int i = 'A'; i <= 'Z'; i++){
            if((int)tile.getCharacter() == i && tile.getState() != TileState.DELETED){
                return "tile" + (char)i;
            }
            else if(tile.getState() == TileState.DELETED){
                return "tileDeleted";
            }
        }

        for(int i = 0; i < 10; i++){
            if((int)tile.getCharacter() - 48 == i  && tile.getState() != TileState.DELETED){
                return "tile" + i;
            }
            else if(tile.getState() == TileState.DELETED){
                return "tileDeleted";
            }
        }

        return "tileZ";
    }

    public Field getField(){
        return field;
    }

    @GetMapping("/logout")
    public String logout() {
        userController.logoutUser();
        field.resetScore();
        resetCampaign();
        setScore();
        return "redirect:/";
    }

    public Model prepareModel(Model model){
        model.addAttribute("comments", commentService.getComments("taptiles"));
        model.addAttribute("ratings", ratingService.getRatings("taptiles"));
        model.addAttribute("scores", scoreService.getTopScores("taptiles"));
        return model;
    }

    public boolean isRandomGame() {
        return randomGame;
    }


    public boolean isSolved(){
        return field.isSolved();
    }

}

