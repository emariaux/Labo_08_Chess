package engine.pieces;

import chess.PlayerColor;
import lombok.Getter;
import lombok.Setter;

public class King extends Piece{

    private boolean hasMoved = false;

    @Setter
    @Getter
    private boolean isCheck = false;

    public King(Coordinate coordinate, PlayerColor playerColor) {
        super(coordinate, playerColor);
    }

    @Override
    public boolean isValidMove(Coordinate to) {

        int deltaX = to.getX() - this.getCoordinate().getX();
        int deltaY = to.getY() - this.getCoordinate().getY();

        if (Math.abs(deltaX) <= 1 && Math.abs(deltaY) <= 1 && (deltaX != 0 || deltaY != 0)) {
            this.hasMoved = true;
            return true;
        }

        return false;
    }
}
