package engine.pieces;

import chess.PlayerColor;
import lombok.Getter;
import lombok.Setter;

public class King extends Piece{

    private boolean hasMoved = false;

    @Setter
    @Getter
    private boolean isCheck = false;

    King(Coordinate coordinate, PlayerColor playerColor) {
        super(coordinate, playerColor);
    }


}
