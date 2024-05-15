package sk.tuke.gamestudio.game.UI;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.GameCore.Difficulty;
import sk.tuke.gamestudio.game.GameCore.Field;
import sk.tuke.gamestudio.game.GameCore.TileState;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.CommentServiceJDBC;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import javax.swing.*;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UI {
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_YELLOW = "\u001B[33m";
    private final Field field;
    private String player;
    private int score = 0;
    private String choice;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    public UI(Field field) {
        this.field = field;
    }


    public void play(){
        System.out.print("Enter your name: ");
        player = scanner.nextLine();
        System.out.println(TEXT_GREEN + "Welcome " + player + " to Taptiles" + TEXT_RESET);
        menu();
    }

    public void menu(){
        System.out.println("1: Play campaign");
        System.out.println("2: Choose level from campaign");
        System.out.println("3: Play random");
        System.out.println("4: Hall of fame");
        System.out.println("5: Quit");
        choice = scanner.nextLine();
        switch (choice) {
            case "1":
                playCampaign(1);
                break;
            case "2":
                chooseCampaignLevel();
                break;
            case "3":
                chooseDifficulty();
                playRandom();
                break;
            case "4":
                printTopScores();
                menu();
                break;
            case "5":
                saveScore();
                exitOptions();
                break;
            default:
                System.out.println();
                menu();
        }

    }

    public void chooseCampaignLevel(){
        System.out.println("Which level would you like to play?");
        System.out.println("1: Level 1");
        System.out.println("2: Level 2");
        System.out.println("3: Level 3");
        System.out.println("4: Level 4");
        System.out.println("5: Level 5");
        choice = scanner.nextLine();
        switch (choice) {
            case "1":
                playCampaign(1);
                break;
            case "2":
                playCampaign(2);
                break;
            case "3":
                playCampaign(3);
                break;
            case "4":
                playCampaign(4);
                break;
            case "5":
                playCampaign(5);
                break;
            default:
                System.out.println();
                menu();
        }
    }

    public void chooseDifficulty(){
        System.out.println("Choose difficulty:");
        System.out.println("1: Easy");
        System.out.println("2: Medium");
        System.out.println("3: Hard");
        choice = scanner.nextLine();
        switch (choice){
            case "1":
                field.setDifficulty(Difficulty.EASY);
                field.generateRandom(field.getDifficulty());
                break;
            case "2":
                field.setDifficulty(Difficulty.MEDIUM);
                field.generateRandom(field.getDifficulty());
                break;
            case "3":
                field.setDifficulty(Difficulty.HARD);
                field.generateRandom(field.getDifficulty());
                break;
            default:
                menu();
        }
    }

    public void printTopScores(){
        var scores = scoreService.getTopScores("taptiles");
        int count = 1;
        System.out.println(TEXT_YELLOW + "--------------Hall of Fame--------------" + TEXT_RESET);
        for(Score topScore: scores){
            System.out.println(count +". " + topScore.getPlayer() + " with score: "+ topScore.getPoints());
            count++;
        }
        System.out.println(TEXT_YELLOW + "----------------------------------------" + TEXT_RESET);
        System.out.println(TEXT_GREEN + "Press any key to continue." + TEXT_RESET);
        scanner.nextLine();
    }


    public void saveScore(){
        scoreService.addScore(new Score("taptiles", player, score, new Date()));
    }

    public void exitOptions(){
        System.out.println("Would you like to leave a comment and rating? [y/n]");
        choice = scanner.nextLine();
        while(!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n")){
            System.out.println(TEXT_RED + "Wrong input" + TEXT_RESET);
            choice = scanner.nextLine();
        }
        if (!choice.equalsIgnoreCase("y")){
            System.out.println(TEXT_GREEN + "Thank you for playing, have a nice day " + player + " !" + TEXT_RESET);
            System.exit(0);
        }
        System.out.print("Comment: ");
        choice = scanner.nextLine();
        commentService.addComment(new Comment("taptiles", player, choice, new Date()));
        System.out.print("Rate this game in scale 5 to 1 (5 best, 1 worst): ");
        int rating = scanner.nextInt();
        while (rating < 1 || rating > 5){
            System.out.print(TEXT_RED + "Wrong input. Rate this game in scale 5 to 1 (5 best, 1 worst): "  + TEXT_RESET);
            rating = scanner.nextInt();
        }
        ratingService.setRating( new Rating("taptiles", player, rating, new Date()));
        System.out.println(TEXT_GREEN + "Thank you for playing, have a nice day " + player + " !" + TEXT_RESET);
        System.exit(0);
    }


    public void readInput() {
        int row, column;
        Scanner scanner1 = new Scanner(System.in);
        System.out.print("Enter row and column[x x]: ");
        row = scanner1.nextInt();
        if(row == 10) menu();
        column = scanner1.nextInt();
        if(column == 10) menu();
        while(!checkInput(row, column) || !checkActiveTile(row, column)){
            System.out.print("Enter row and column[x x]: ");
            row = scanner1.nextInt();
            if(row == 10) menu();
            column = scanner1.nextInt();
            if(column == 10) menu();
        }
        field.markTile(row, column);
    }

    public boolean checkActiveTile(int row, int column){
        if(field.getTile(row,column).getState() != TileState.ACTIVE) {
            System.out.println(TEXT_RED + "Wrong tile picked. Choose only active tiles." + TEXT_RESET);
            return false;
        }
        return true;
    }

    public boolean checkInput(int row, int column){
        if(row < 1 || row > field.getRow_count()-2){
            System.out.println(TEXT_RED + "Wrong intput. Choose row and column between 1 and " + (field.getRow_count()-2) + TEXT_RESET);
            return false;
        }
        else if(column < 1 || column > field.getColumn_count()-2){
            System.out.println(TEXT_RED + "Wrong intput. Choose row and column between 1 and " + (field.getRow_count()-2) + TEXT_RESET);
            return false;
        }
        return true;
    }

    public void playRandom() {
        while (!field.isSolved()) {
            draw();
            readInput();
            readInput();
            field.checkMove(field.getSourceTile(), field.getDestinationTile());
        }
        draw();
        score += field.getScore();
        System.out.println("Congratulations you won.");
        System.out.println("Your score: " + score);
        menu();
    }


    public void playCampaign( int start){
        for(int i = start; i < 6; i++) {
            field.generateFromFile("src/main/java/sk/tuke/gamestudio/game/Levels/Level_" + i);
            System.out.println(TEXT_YELLOW + "Level " + i + TEXT_RESET);
            while (!field.isSolved()) {
                draw();
                readInput();
                readInput();
                field.checkMove(field.getSourceTile(), field.getDestinationTile());
            }
            draw();
            score += field.getScore();
            System.out.println(TEXT_GREEN + "Congratulations "+ player + ", you passed " + i + " level with score: " + score + TEXT_RESET);
            System.out.println("Continue to next level ? [y/n]");
            System.out.println();
            if(scanner.nextLine().equalsIgnoreCase("n")){
                break;
            }
        }
        menu();
    }

    public void draw(){
        System.out.println( TEXT_YELLOW+  "----------------------------------------------------------" );
        System.out.println("If you want to leave type 10");
        int num = 1;
        System.out.print("  ");
        for(int i = 1; i < field.getRow_count()-1; i++){
            System.out.print(" " + num);
            num++;
        }
        System.out.println(TEXT_RESET);

        System.out.print(TEXT_GREEN + "  +");
        for(int i = 1; i < field.getRow_count()-1; i++){
            System.out.print("-+");
        }
        System.out.println(TEXT_RESET);

        num = 1;
        for(int i = 1; i < field.getRow_count()-1; i++){
            System.out.print(TEXT_YELLOW + num + TEXT_GREEN + " |" + TEXT_RESET);
            num++;
            for(int j = 1; j < field.getColumn_count()-1; j++){
                var tile = field.getTile(i, j);
                System.out.print(tile.getCharacter() + TEXT_GREEN + "|" + TEXT_RESET);
            }
            System.out.println();
            System.out.print(TEXT_GREEN + "  +");
            for(int k = 1; k < field.getRow_count()-1; k++){
                System.out.print("-+");
            }
            System.out.println(TEXT_RESET);
        }
    }

}
