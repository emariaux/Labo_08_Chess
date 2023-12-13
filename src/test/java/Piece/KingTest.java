package Piece;

import chess.PlayerColor;
import engine.pieces.King;
import engine.pieces.Coordinate;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KingTest {

    @Test
    public void moveValidTop(){
        King king = new King(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,2);

        assertTrue(king.isValidMove(to));
    }

    @Test
    public void moveValidRight(){
        King king = new King(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(2,1);

        assertTrue(king.isValidMove(to));
    }

    @Test
    public void moveValidLeft(){
        King king = new King(new Coordinate(2,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,1);

        assertTrue(king.isValidMove(to));
    }

    @Test
    public void moveValidDown(){
        King king = new King(new Coordinate(1,2), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,1);

        assertTrue(king.isValidMove(to));
    }

    public void moveUnvalid(){
        King king = new King(new Coordinate(4,2), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,1);

        assertFalse(king.isValidMove(to));
    }

    @Test
    public void stay(){
        King king = new King(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,1);

        assertFalse(king.isValidMove(to));
    }
}
