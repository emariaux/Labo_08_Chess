package engine.pieces;

import chess.PlayerColor;

public class Rook extends Piece{
    private boolean hasMoved = false;

    public Rook(Coordinate coordinate, PlayerColor playerColor) {
        super(coordinate, playerColor);
    }

    @Override
    public boolean isValidMove(Coordinate to) {

        int deltaX = to.getX() - this.getCoordinate().getX();
        int deltaY = to.getY() - this.getCoordinate().getY();

        //if the piece moves from (X1, Y1) to (X2, Y2), the move is valid if and only if X2=X1 or Y2=Y1.
        if((to.getX() == this.getCoordinate().getX() || to.getY() == this.getCoordinate().getY())
                && (deltaX != 0 || deltaY != 0)) {
            this.hasMoved = true;
            return true;
        }

            return false;

    }

    public boolean isHasMoved() {
        return hasMoved;
    }
}
