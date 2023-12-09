package engine.pieces;

import chess.PlayerColor;

public class Rook extends Piece{
    private boolean hasMoved = false;

    Rook(Coordinate coordinate, PlayerColor playerColor) {
        super(coordinate, playerColor);
    }
}
