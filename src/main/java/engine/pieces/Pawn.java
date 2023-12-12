package engine.pieces;

import chess.PlayerColor;
import lombok.Getter;

import java.io.IOException;

public class Pawn extends Piece{

    @Getter
    private boolean hasMoved = false;
    @Getter
    private boolean lastMoveWasDoubleForward = false;

    public Pawn(Coordinate coordinate, PlayerColor playerColor) {
        super(coordinate, playerColor);
    }



    @Override
    public boolean isValidMove(Coordinate to) {
        if(hasMoved){
            return verifyStep(to,1);
        } else if (verifyStep(to,1)) {
            hasMoved = true;

        }else if(verifyStep(to,2)){
            hasMoved = true;
            lastMoveWasDoubleForward = true;
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


}
