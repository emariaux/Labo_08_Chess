package Piece;

import chess.PlayerColor;
import engine.pieces.Coordinate;
import engine.pieces.Rook;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RookTest {

    @Test
    public void moveValidTop(){
        Rook rook = new Rook(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,8);

        assertTrue(rook.isValidMove(to));
    }

    @Test
    public void moveValidRight(){
        Rook rook = new Rook(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(7,1);

        assertTrue(rook.isValidMove(to));
    }

    @Test
    public void moveValidLeft(){
        Rook rook = new Rook(new Coordinate(7,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,1);

        assertTrue(rook.isValidMove(to));
    }

    @Test
    public void moveValidDown(){
        Rook rook = new Rook(new Coordinate(7,7), PlayerColor.WHITE);
        Coordinate to = new Coordinate(7,1);

        assertTrue(rook.isValidMove(to));
    }

    @Test
    public void hasMovedTest(){
        Rook rook = new Rook(new Coordinate(7,7), PlayerColor.WHITE);
        Coordinate to = new Coordinate(7,1);
        rook.isValidMove(to);
        assertTrue(rook.isHasMoved());
    }

    @Test
    public void moveNotValid(){
        Rook rook = new Rook(new Coordinate(7,7), PlayerColor.WHITE);
        Coordinate to = new Coordinate(5,5);

        assertFalse(rook.isValidMove(to));
    }
}
