package Piece;

import chess.PlayerColor;
import engine.pieces.Coordinate;
import engine.pieces.Pawn;
import org.junit.Test;

import static org.junit.Assert.*;

public class PawnTest {
    @Test
    public void moveValid(){
        Pawn pawn = new Pawn(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,2);

        assertTrue(pawn.isValidMove(to));
    }

    @Test
    public void move2CaseValidMove(){
        Pawn pawn = new Pawn(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,3);

        assertTrue(pawn.isValidMove(to));
    }

    @Test
    public void moveUnvalidMove(){
        Pawn pawn = new Pawn(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,4);

        assertFalse(pawn.isValidMove(to));
    }

    @Test
    public void hasMove(){
        Pawn pawn = new Pawn(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,2);
        pawn.isValidMove(to);

        assertTrue(pawn.isHasMoved());
    }

    @Test
    public void lastMoveWasDoubleForward(){
        Pawn pawn = new Pawn(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,3);
        pawn.isValidMove(to);

        assertTrue(pawn.isLastMoveWasDoubleForward());
    }

    @Test
    public void move2CaseNotValidMove(){
        Pawn pawn = new Pawn(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,2);
        pawn.isValidMove(to);

        to = new Coordinate(1,4);
        assertFalse(pawn.isValidMove(to));
    }

    @Test
    public void stay(){
        Pawn pawn = new Pawn(new Coordinate(1,1), PlayerColor.WHITE);
        Coordinate to = new Coordinate(1,1);

        assertFalse(pawn.isValidMove(to));
    }
}
