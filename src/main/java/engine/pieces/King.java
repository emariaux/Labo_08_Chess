package engine.pieces;

import chess.PlayerColor;
import lombok.Setter;

public class King extends Piece{

    private boolean hasMoved = false;

    @Setter
    private boolean isCheck = false;

    King(Coordinate coordinate, PlayerColor playerColor) {
        super(coordinate, playerColor);
    }


}
