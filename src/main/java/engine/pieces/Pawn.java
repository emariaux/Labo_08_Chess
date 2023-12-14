package engine.pieces;

import chess.PlayerColor;

public class Pawn extends Piece{

    private boolean hasMoved = false;
    private boolean lastMoveWasDoubleForward = false;

    public Pawn(Coordinate coordinate, PlayerColor playerColor) {
        super(coordinate, playerColor);
    }



    @Override
    public boolean isValidMove(Coordinate to) {
        super.isValidMove(to);

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
