package engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece{
    public Bishop(Coordinate coordinate, PlayerColor playerColor) {
        super(coordinate, playerColor, PieceType.BISHOP);
    }

    @Override
    public boolean isValidMove(Coordinate to) {
        int deltaX = to.getX() - this.getCoordinate().getX();
        int deltaY = to.getY() - this.getCoordinate().getY();


        // Verify if the move is diagonal (same horizontal and vertical move)
        return Math.abs(deltaX) == Math.abs(deltaY) && (deltaX != 0 || deltaY != 0);
    }

    @Override
    public List<Coordinate> path(Coordinate to) {

        List<Coordinate> path = new ArrayList<>();

        // Check if the movement is diagonal by comparing the absolute differences of x and y coordinates
        if (Math.abs(to.getX() - this.getCoordinate().getX()) == Math.abs(to.getY() - this.getCoordinate().getY())) {
            int xStep = (to.getX() > this.getCoordinate().getX()) ? 1 : -1; // Determine if the movement is up-right or down-left
            int yStep = (to.getY() > this.getCoordinate().getY()) ? 1 : -1; // Determine if the movement is up-left or down-right
            int x, y;
            for (x = this.getCoordinate().getX() + xStep, y = this.getCoordinate().getY() + yStep; x != to.getX(); x += xStep, y += yStep) {
                path.add(new Coordinate(x, y));
            }
        }

        return path;
    }

    @Override
    public String textValue() {
        return "Bishop";
    }
}
