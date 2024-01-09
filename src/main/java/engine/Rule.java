package engine;

import engine.pieces.*;

public interface Rule {
    public Piece enPassant(Coordinate to, Pawn pawn);

    public void promote(Pawn pawn);

    public boolean castleKingSide(King king, Rook rook);

    public boolean castleQueenSide(King king, Rook rook);
}
