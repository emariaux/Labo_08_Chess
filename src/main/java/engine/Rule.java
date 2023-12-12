package engine;

import engine.pieces.*;

public interface Rule {
    public boolean enPassant(Coordinate to, Pawn pawn);

    public void promote(Pawn pawn);

    public void isCastleKingSide(Coordinate from, Coordinate to);

    public void isCastleQueenSide(Coordinate from, Coordinate to);
}
