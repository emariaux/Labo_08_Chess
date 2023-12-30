package engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece{
    private boolean hasMoved = false;

    public Rook(Coordinate coordinate, PlayerColor playerColor) {
        super(coordinate, playerColor, PieceType.ROOK);
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

    @Override
    public List<Coordinate> path(Coordinate to) {
        List<Coordinate> path = new ArrayList<>();

        // Horizontal movement
        if (this.getCoordinate().getY() == to.getY()) {
            int step = (to.getX() > this.getCoordinate().getX()) ? 1 : -1;
            for (int x = this.getCoordinate().getX() + step; x != to.getX(); x += step) {
                path.add(new Coordinate(x, this.getCoordinate().getY()));
            }
        }
        // Vertical movement
        else if (this.getCoordinate().getX() == to.getX()) {
            int step = (to.getY() > this.getCoordinate().getY()) ? 1 : -1;
            for (int y = this.getCoordinate().getY() + step; y != to.getY(); y += step) {
                path.add(new Coordinate(this.getCoordinate().getX(), y));
            }
        }

        return path;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }
}
