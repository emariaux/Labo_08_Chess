package engine;

import chess.PlayerColor;
import engine.pieces.*;

public class Check {
    private final King whiteKing;
    private final King blackKing;

    public Check(King whiteKing, King blackKing) {
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
    }

    public boolean checked(King king){
        return false;
    }

    public boolean whiteCheck(){
        return whiteKing.isCheck();
    }

    public boolean blackCheck(){
        return blackKing.isCheck();
    }

    public void setCheck(King king){
        king.setCheck(true);
    }

    public boolean validMoveCheck(PlayerColor playerColor){
        return false;
    }

    public boolean isChecked(PlayerColor playerColor){
        King king = playerColor == PlayerColor.WHITE ? whiteKing : blackKing;
        return false;
    }

    private boolean horizontalRightCheck(King king) {

        for(int i = king.getCoordinate().getX() + 1; i < 8; i++){

        }


        return false;
    }

}
