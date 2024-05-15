package sk.tuke.gamestudio.game.GameCore;

public class Tile {
    private TileState state;
    private char character;
    private final int row;
    private final int column;

    public Tile(char character, int row, int column) {
        this.row = row;
        this.column = column;
        this.character = character;
        if(character != ' ') {
            this.state = TileState.ACTIVE;
        }else{
            this.state = TileState.DELETED;
        }
    }

    public void setState(TileState state) {
        this.state = state;
    }
    public TileState getState() {
        return state;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character){
        this.character = character;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
