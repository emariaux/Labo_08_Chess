package engine.pieces;

import chess.PlayerColor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Piece implements MoveValidator{

    @Getter
    private Coordinate coordinate;
    private final PlayerColor playerColor;
    @Override
    public boolean isValidMove() {
        return false;
    }

    Piece(Coordinate coordinate, PlayerColor playerColor){
        this.coordinate = coordinate;
        this.playerColor = playerColor;
    }

    public List<Coordinate> path(Coordinate to){
        List<Coordinate> path = new ArrayList<>();
        return  path;
    }


}
