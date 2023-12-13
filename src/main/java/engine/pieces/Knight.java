package engine.pieces;

import chess.PlayerColor;

public class Knight extends Piece{
    public Knight(Coordinate coordinate, PlayerColor playerColor) {
        super(coordinate, playerColor);
    }

    @Override
    public boolean isValidMove(Coordinate to) {

        int deltaX = to.getX() - this.getCoordinate().getX();
        int deltaY = to.getY() - this.getCoordinate().getY();

        // if (|X2-X1|=1 and |Y2-Y1|=2) or (|X2-X1|=2 and |Y2-Y1|=1).
        if((Math.abs(deltaX) == 1 && Math.abs(deltaY) == 2)
                ||  (Math.abs(deltaX) == 2 && Math.abs(deltaY) == 1)
                && (deltaX != 0 || deltaY != 0)){
            return true;
        }

        return false;
    }
}
