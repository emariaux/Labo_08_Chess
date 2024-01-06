package engine;

import engine.pieces.*;

public interface Rule {
    public Piece enPassant(Coordinate to, Pawn pawn);

    public void promote(Pawn pawn);

    public boolean isCastleKingSide(King king, Rook rook);

    public boolean isCastleQueenSide(King king, Rook rook);
}
