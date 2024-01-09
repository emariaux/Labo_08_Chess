package engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

import java.util.List;

public class Pawn extends Piece{
    public List<Coordinate> path(Coordinate to){
        return null;
    }

    private boolean hasMoved = false;
    private boolean lastMoveWasDoubleForward = false;

    public Pawn(Coordinate coordinate, PlayerColor playerColor) {
        super(coordinate, playerColor, PieceType.PAWN);
    }
    
    @Override
    public boolean isValidMove(Coordinate to) {
        int deltaX = to.getX() - this.getCoordinate().getX();
        int deltaY = to.getY() - this.getCoordinate().getY();

        // Detemine the forward step depending on the player's color
        int forwardStep = (this.getPlayerColor() == PlayerColor.WHITE) ? 1 : -1;

        // Check if the move is a single or double step forward
        if (deltaX == 0 && (deltaY == forwardStep || (deltaY == 2 * forwardStep && !this.hasMoved))) {
            this.hasMoved = true;
            this.lastMoveWasDoubleForward = deltaY == 2 * forwardStep;
            return true;
        }

        return false;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public boolean isLastMoveWasDoubleForward() {
        return lastMoveWasDoubleForward;
    }

    @Override
    public String textValue() {
        return "Pawn";
    }
}
