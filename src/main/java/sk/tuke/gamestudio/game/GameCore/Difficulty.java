package sk.tuke.gamestudio.game.GameCore;

public enum Difficulty {
    EASY(4),
    MEDIUM(6),
    HARD(8);

    public final int difficulty;
    Difficulty(int difficulty){
        this.difficulty = difficulty;
    }

}
