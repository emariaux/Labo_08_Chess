package engine.pieces;

import chess.PlayerColor;

public class Queen extends Piece{
    public Queen(Coordinate coordinate, PlayerColor playerColor) {
        super(coordinate, playerColor);
    }

    @Override
    public boolean isValidMove(Coordinate to) {

        int deltaX = to.getX() - this.getCoordinate().getX();
        int deltaY = to.getY() - this.getCoordinate().getY();

        //
        if(((to.getX() == this.getCoordinate().getX() || to.getY() == this.getCoordinate().getY())
                ||(Math.abs(deltaX) == Math.abs(deltaY)))
                && (deltaX != 0 || deltaY != 0)){
            return true;
        }

        return false;
    }
}
