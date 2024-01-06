package engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

import java.util.List;

public class King extends Piece{
    private boolean hasMoved = false;
    private boolean isCheck = false;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }



    public King(Coordinate coordinate, PlayerColor playerColor) {
        super(coordinate, playerColor, PieceType.KING);
    }

    @Override
    public boolean isValidMove(Coordinate to) {

        int deltaX = to.getX() - this.getCoordinate().getX();
        int deltaY = to.getY() - this.getCoordinate().getY();

        if (Math.abs(deltaX) <= 1 && Math.abs(deltaY) <= 1 && (deltaX != 0 || deltaY != 0)) {
            this.hasMoved = true;
            return true;
        }

        return false;
    }

    public List<Coordinate> path(Coordinate to){
        return null;
    }
    @Override
    public String textValue() {
        return "King";
    }


}
