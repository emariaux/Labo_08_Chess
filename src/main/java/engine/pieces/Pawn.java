package engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class Pawn extends Piece{

    private boolean hasMoved = false;
    private boolean lastMoveWasDoubleForward = false;

    public Pawn(Coordinate coordinate, PlayerColor playerColor) {
        super(coordinate, playerColor, PieceType.PAWN);
    }


    @Override
    public boolean isValidMove(Coordinate to) {
        super.isValidMove(to);

        int deltaX = to.getX() - this.getCoordinate().getX();
        int deltaY = to.getY() - this.getCoordinate().getY();
        if(this.getPlayerColor() == PlayerColor.WHITE){
            // Single step forward.
            if(deltaX == 0 && deltaY == 1){
                this.hasMoved = true;
            // Double step forward.
            } else if (deltaX == 0 && deltaY == 2) {
                this.hasMoved = true;
                this.lastMoveWasDoubleForward = true;
            // Diagonal step forward.
            } else if (deltaY == 1 && Math.abs(deltaX) == 1) {
                this.hasMoved = true;
            } else {
                return false;
            }
        } else if (getPlayerColor() == PlayerColor.BLACK) {
            // Single step downward.
            if(deltaX == 0 && deltaY == -1){
                this.hasMoved = true;
            // Double step downward.
            } else if (deltaX == 0 && deltaY == -2) {
                this.hasMoved = true;
                this.lastMoveWasDoubleForward = true;
            // Diagonal step downward.
            } else if (deltaY == -1 && Math.abs(deltaX) == 1) {
                this.hasMoved = true;
            }
            else {
                return false;
            }
        }

        return true;

        /*
        if(this.hasMoved){
            System.out.println(verifyStep(to,1));
            return verifyStep(to,1);
        } else if (verifyStep(to,1)) {
            this.hasMoved = true;
        }else if(verifyStep(to,2)){
            this.hasMoved = true;
            this.lastMoveWasDoubleForward = true;
        }else{
            return  false;
        }
        return true;
        */
    }

    private boolean verifyStep(Coordinate to, int nbSteps){
        if(nbSteps == 1 || nbSteps == 2){
            return to.getY() == this.getCoordinate().getY() + nbSteps;
        }
        return false;

    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public boolean isLastMoveWasDoubleForward() {
        return lastMoveWasDoubleForward;
    }
}
