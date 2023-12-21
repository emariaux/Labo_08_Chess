package engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class Bishop extends Piece{
    public Bishop(Coordinate coordinate, PlayerColor playerColor) {
        super(coordinate, playerColor, PieceType.BISHOP);
    }

    @Override
    public boolean isValidMove(Coordinate to) {
        int deltaX = to.getX() - this.getCoordinate().getX();
        int deltaY = to.getY() - this.getCoordinate().getY();


        // Verify if the move is diagonal (same horizontal and vertical move)
        if (Math.abs(deltaX) == Math.abs(deltaY) && (deltaX != 0 || deltaY != 0)) {
            return true;
        }

        return false;
    }
}
