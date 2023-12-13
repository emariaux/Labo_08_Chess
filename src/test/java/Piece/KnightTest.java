package Piece;

import chess.PlayerColor;
import engine.pieces.Coordinate;
import engine.pieces.Knight;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KnightTest {

    @Test
    public void moveValidTopRight(){
        Knight knight = new Knight(new Coordinate(2,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(3,3);

        assertTrue(knight.isValidMove(to));
    }
    @Test
    public void moveValidTopLeft(){
        Knight knight = new Knight(new Coordinate(2,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,3);

        assertTrue(knight.isValidMove(to));
    }

    @Test
    public void moveValidMidTopRight(){
        Knight knight = new Knight(new Coordinate(2,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(4,2);

        assertTrue(knight.isValidMove(to));
    }
    @Test
    public void moveValidMidTopLeft(){
        Knight knight = new Knight(new Coordinate(3,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,2);

        assertTrue(knight.isValidMove(to));
    }

    @Test
    public void moveValidMidDownRight(){
        Knight knight = new Knight(new Coordinate(3,2), PlayerColor.WHITE);
        Coordinate to = new Coordinate(5,1);

        assertTrue(knight.isValidMove(to));
    }

    @Test
    public void moveValidMidDownLeft(){
        Knight knight = new Knight(new Coordinate(3,2), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,1);

        assertTrue(knight.isValidMove(to));
    }

    @Test
    public void moveValidDownRight(){
        Knight knight = new Knight(new Coordinate(3,3), PlayerColor.WHITE);
        Coordinate to = new Coordinate(4,1);

        assertTrue(knight.isValidMove(to));
    }

    @Test
    public void moveValidDownLeft(){
        Knight knight = new Knight(new Coordinate(3,2), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,1);

        assertTrue(knight.isValidMove(to));
    }

    @Test
    public void moveNotValid(){
        Knight knight = new Knight(new Coordinate(3,3), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,1);

        assertFalse(knight.isValidMove(to));
    }

    @Test
    public void moveNotValid2(){
        Knight knight = new Knight(new Coordinate(3,3), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,3);

        assertFalse(knight.isValidMove(to));
    }

    @Test
    public void stay(){
        Knight knight = new Knight(new Coordinate(3,3), PlayerColor.WHITE);
        Coordinate to = new Coordinate(3,3);

        assertFalse(knight.isValidMove(to));
    }
}
