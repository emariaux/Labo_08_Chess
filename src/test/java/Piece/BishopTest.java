package Piece;

import chess.PlayerColor;
import engine.pieces.Bishop;
import engine.pieces.Coordinate;
import engine.pieces.Pawn;
import org.junit.Test;

import static org.junit.Assert.*;

public class BishopTest {
    @Test
    public void moveValidRightDiagonal(){
        Bishop bishop = new Bishop(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(3,3);

        assertTrue(bishop.isValidMove(to));
    }

    @Test
    public void moveValidLeftDiagonal(){
        Bishop bishop = new Bishop(new Coordinate(4,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,4);

        assertTrue(bishop.isValidMove(to));
    }

    @Test
    public void moveUnvalid(){
        Bishop bishop = new Bishop(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,4);

        assertFalse(bishop.isValidMove(to));
    }

    @Test
    public void stay(){
        Bishop bishop = new Bishop(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,1);

        assertFalse(bishop.isValidMove(to));
    }
}
