package engine;

import engine.pieces.*;

public class Check {
    private final King whiteKing;
    private final King blackKing;

    public Check(King whiteKing, King blackKing) {
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
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

    public boolean validMoveCheck(){
        return false;
    }
}
