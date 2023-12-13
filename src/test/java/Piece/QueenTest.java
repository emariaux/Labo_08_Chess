package Piece;

import chess.PlayerColor;
import engine.pieces.Coordinate;
import engine.pieces.Queen;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QueenTest {

    @Test
    public void moveValidTopRightDiagonal(){
        Queen queen = new Queen(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(3,3);

        assertTrue(queen.isValidMove(to));
    }

    @Test
    public void moveValidTopLeftDiagonal(){
        Queen queen = new Queen(new Coordinate(4,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,4);

        assertTrue(queen.isValidMove(to));
    }
    @Test
    public void moveValidDownRightDiagonal(){
        Queen queen = new Queen(new Coordinate(1,4), PlayerColor.WHITE);
        Coordinate to = new Coordinate(4,1);

        assertTrue(queen.isValidMove(to));
    }

    @Test
    public void moveValidDownLeftDiagonal(){
        Queen queen = new Queen(new Coordinate(3,3), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,1);

        assertTrue(queen.isValidMove(to));
    }

    @Test
    public void moveValidTop(){
        Queen queen = new Queen(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,8);

        assertTrue(queen.isValidMove(to));
    }

    @Test
    public void moveValidRight(){
        Queen queen = new Queen(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(7,1);

        assertTrue(queen.isValidMove(to));
    }

    @Test
    public void moveValidLeft(){
        Queen queen = new Queen(new Coordinate(7,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,1);

        assertTrue(queen.isValidMove(to));
    }

    @Test
    public void moveValidDown(){
        Queen queen = new Queen(new Coordinate(7,7), PlayerColor.WHITE);
        Coordinate to = new Coordinate(7,1);

        assertTrue(queen.isValidMove(to));
    }
    
    
    @Test
    public void moveNotValid(){
        Queen queen = new Queen(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(2,4);

        assertFalse(queen.isValidMove(to));
    }

    @Test
    public void stay(){
        Queen queen = new Queen(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,1);

        assertFalse(queen.isValidMove(to));
    }
}
