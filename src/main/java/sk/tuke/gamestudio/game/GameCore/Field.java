package sk.tuke.gamestudio.game.GameCore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class Field {

    private Tile[][] tiles;
    private GameState state;
    private int column_count;
    private final String fileInput;
    private int row_count;
    private Tile sourceTile = null;
    private Tile destinationTile = null;
    private Difficulty difficulty = null;
    private long startTime;
    private int score;

    public Field(){
        this.fileInput = null;
        this.state = GameState.PLAYING;
        this.score = 0;
    }

    public void checkMove(Tile sourceTile, Tile destinationTile){
        if(sourceTile.getCharacter() == destinationTile.getCharacter()) {
            if(findSource(tiles, row_count,column_count)){
                deleteTiles(sourceTile, destinationTile);
            }
            else{
                unmarkTiles(sourceTile, destinationTile);
            }
        }
        else{
            unmarkTiles(sourceTile, destinationTile);
        }
    }


    public boolean findSource(Tile[][] tiles, int row_count, int column_count){
        int visited[][] = new int[row_count][column_count];
        boolean flag = false;
        for(int i = 0; i < row_count; i++){
            for(int j = 0; j < column_count; j++){
                if(tiles[i][j].getState() == TileState.SOURCE && visited[i][j] == 0){
                    if(findPath(tiles, i, j, visited)){
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    private boolean findPath(Tile[][] tiles, int i, int j, int visited[][]) {
        if (isSafe(i, j, tiles) && tiles[i][j].getState() != TileState.ACTIVE && visited[i][j] == 0) {

            visited[i][j] = 1;

            if (tiles[i][j].getState() == TileState.DEST)
                return true;

            boolean up = findPath(tiles, i - 1, j, visited);

            if(up) return true;

            boolean left = findPath(tiles, i, j - 1, visited);

            if(left) return true;

            boolean down = findPath(tiles, i + 1, j, visited);

            if(down) return true;

            boolean right = findPath(tiles, i, j + 1, visited);

            if(right) return true;
        }
        return false;
    }

    public static boolean isSafe(int i, int j, Tile[][] tiles) {
        if (i >= 0 && i < tiles.length && j >= 0 && j < tiles[0].length) {
            return true;
        }
        return false;
    }


    public void unmarkTiles(Tile sourceTile, Tile destinationTile){
        sourceTile.setState(TileState.ACTIVE);
        destinationTile.setState(TileState.ACTIVE);
        this.sourceTile = null;
        this.destinationTile = null;
    }

    public void deleteTiles(Tile sourceTile, Tile destinationTile){
        sourceTile.setState(TileState.DELETED);
        destinationTile.setState(TileState.DELETED);
        sourceTile.setCharacter(' ');
        destinationTile.setCharacter(' ');
        this.sourceTile = null;
        this.destinationTile = null;
        score += 5;
    }

    public void generateFromFile(String fileInput){
        File file = new File(fileInput);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            int num;
            num = br.read();
            this.row_count = num + 2 - '0';
            num = br.read();
            this.column_count = num + 2 - '0';
            tiles = new Tile[row_count][column_count];
            for(int i = 1; i < row_count-1; i++){
                for(int j = 1; j < column_count-1; j++){
                    num = br.read();
                    char buff = (char) num;
                    tiles[i][j] = new Tile(buff, i, j);
                }
            }
            for(int i = 0; i < row_count; i++){
                tiles[i][0] = new Tile(' ', i , 0);
            }
            for(int i = 0; i < row_count; i++){
                tiles[i][column_count-1] = new Tile(' ', i, column_count-1);
            }
            for(int i = 1; i < column_count-1; i++){
                tiles[0][i] = new Tile(' ', 0, i);
            }
            for(int i = 1; i < column_count-1; i++){
                tiles[row_count-1][i] = new Tile(' ', row_count-1, i);
            }
        }catch (IOException e){
            System.out.println("Wrong input file");
        }
        setState(GameState.PLAYING);
        startTime = System.currentTimeMillis();
    }

    public void generateRandom(Difficulty difficulty){
        this.column_count = difficulty.difficulty;
        this.row_count = difficulty.difficulty;
        int tiles_count = row_count*column_count;
        int rnd_row_first, rnd_column_second, rnd_column_first, rnd_row_second;
        int random_range = tiles_count/2;
        int character;
        row_count = row_count + 2;
        column_count = column_count +2;
        generateEmptyField(row_count, column_count);
        int n = 'A' + random_range;
        for(int i = 0; i < tiles_count*10; i++){
            character = ThreadLocalRandom.current().nextInt('A', 'P');

            rnd_row_first = ThreadLocalRandom.current().nextInt(1, (row_count-1));
            rnd_row_second = ThreadLocalRandom.current().nextInt(1, (row_count-1));
            rnd_column_first = ThreadLocalRandom.current().nextInt(1, (column_count-1));
            rnd_column_second = ThreadLocalRandom.current().nextInt(1, (column_count-1));

            if(!isOccupied(rnd_row_first, rnd_column_first) && !isOccupied(rnd_row_second, rnd_column_second)) {
                this.tiles[rnd_row_first][rnd_column_first].setCharacter((char)character); //new Tile((char) character);
                this.tiles[rnd_row_second][rnd_column_second].setCharacter((char)character);
                this.tiles[rnd_row_first][rnd_column_first].setState(TileState.SOURCE);
                this.tiles[rnd_row_second][rnd_column_second].setState(TileState.DEST);

                if(findSource(this.tiles, row_count, column_count)){
                    this.tiles[rnd_row_first][rnd_column_first].setState(TileState.ACTIVE);
                    this.tiles[rnd_row_second][rnd_column_second].setState(TileState.ACTIVE);
                }
                else{
                    this.tiles[rnd_row_first][rnd_column_first].setCharacter(' ');
                    this.tiles[rnd_row_second][rnd_column_second].setCharacter(' '); //= new Tile(' ');
                    this.tiles[rnd_row_first][rnd_column_first].setState(TileState.DELETED);
                    this.tiles[rnd_row_second][rnd_column_second].setState(TileState.DELETED);
                }

            }
        }
        if(!randomGeneratorCheck()){
            fillField();
        }
        setState(GameState.PLAYING);
        startTime = System.currentTimeMillis();
    }

    public int getScore(){
        return score;
    }

    private void fillField() {
        for(int i = 1; i < row_count-1; i++){
            for(int j = 1; j < column_count-1; j++){
                if(tiles[i][j].getState() == TileState.DELETED){
                    tiles[i][j] = new Tile('Z', i , j);
                }
            }
        }
    }

    public boolean randomGeneratorCheck(){
        for(int i = 1; i < row_count-1; i++){
            for(int j = 1; j < column_count-1; j++){
                if(tiles[i][j].getState() == TileState.DELETED){
                    return false;
                }
            }
        }
        return true;
    }

    public void generateEmptyField(int rows, int columns){
        this.tiles = new Tile[rows][columns];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                this.tiles[i][j] = new Tile(' ', i, j);
                this.tiles[i][j].setState(TileState.DELETED);
            }
        }
    }

    public boolean isOccupied(int row, int column) {
        if(tiles[row][column].getState() == TileState.DELETED)
            return false;
        return true;
    }

    public void markTile(int row, int column){
        if (sourceTile == null && tiles[row][column].getState() != TileState.DELETED) {
            sourceTile = tiles[row][column];
            tiles[row][column].setState(TileState.SOURCE);
        } else if (destinationTile == null && tiles[row][column].getState() != TileState.DELETED) {
            destinationTile = tiles[row][column];
            tiles[row][column].setState(TileState.DEST);
        }
    }

    public boolean isSolved(){
        checkGameState();
        if(this.getState() != GameState.SOLVED){
            return false;
        }
        return true;
    }

    public void checkGameState(){
        for(int i = 0; i < row_count; i++){
            for(int j = 0; j < column_count; j++){
                if(tiles[i][j].getState() != TileState.DELETED) {
                    return;
                }
            }
        }
        setState(GameState.SOLVED);
    }

    public void resetScore(){
        score = 0;
    }
    public String getFileInput() {
        return fileInput;
    }

    public Tile getTile(int row, int column){
        return tiles[row][column];
    }

    public int getRow_count() {
        return row_count;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public int getColumn_count() {
        return column_count;
    }

    public Tile getSourceTile() {
        return sourceTile;
    }

    public Tile getDestinationTile() {
        return destinationTile;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
