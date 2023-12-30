package engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece{
    public Queen(Coordinate coordinate, PlayerColor playerColor) {
        super(coordinate, playerColor, PieceType.QUEEN);
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

    @Override
    public List<Coordinate> path(Coordinate to) {
        List<Coordinate> path = new ArrayList<>();

        // Horizontal movement
        if (this.getCoordinate().getY() == to.getY()) {
            for (int x = Math.min(this.getCoordinate().getX(), to.getX()) + 1; x < Math.max(this.getCoordinate().getX(), to.getX()); x++) {
                path.add(new Coordinate(x, this.getCoordinate().getY()));
            }
        }
        // Vertical movement
        else if (this.getCoordinate().getX() == to.getX()) {
            for (int y = Math.min(this.getCoordinate().getY(), to.getY()) + 1; y < Math.max(this.getCoordinate().getY(), to.getY()); y++) {
                path.add(new Coordinate(this.getCoordinate().getX(), y));
            }
        }
        // Diagonal movement
        else if (Math.abs(to.getX() - this.getCoordinate().getX()) == Math.abs(to.getY() - this.getCoordinate().getY())) {
            int xStep = (to.getX() > this.getCoordinate().getX()) ? 1 : -1;
            int yStep = (to.getY() > this.getCoordinate().getY()) ? 1 : -1;
            for (int x = this.getCoordinate().getX() + xStep, y = this.getCoordinate().getY() + yStep; x != to.getX(); x += xStep, y += yStep) {
                path.add(new Coordinate(x, y));
            }
        }

        return path;
    }
}
