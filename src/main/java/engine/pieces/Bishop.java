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

        // Vérifier si le déplacement est en diagonale (même déplacement horizontal et vertical)
        if (Math.abs(deltaX) == Math.abs(deltaY) && (deltaX != 0 || deltaY != 0)) {
            return true;
        }
        // Le mouvement n'est pas en diagonale
        return false;
    }
}
