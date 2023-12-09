package engine.pieces;

import chess.PlayerColor;

public class Pawn extends Piece{

    private boolean hasMoved = false;
    private boolean lastMoveWasDoubleForward = false;

    Pawn(Coordinate coordinate, PlayerColor playerColor) {
        super(coordinate, playerColor);
    }

    @Override
    public boolean isValidMove() {
        return super.isValidMove();
    }
}
