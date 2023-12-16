package engine.pieces;

import chess.PlayerColor;
import chess.PieceType;

import java.util.ArrayList;
import java.util.List;

public class Piece implements MoveValidator{

    private Coordinate coordinate;
    private final PlayerColor playerColor;
    private final PieceType pieceType;

    public PieceType getPieceType() {
        return pieceType;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public Piece(Coordinate coordinate, PlayerColor playerColor, PieceType pieceType){
        this.coordinate = coordinate;
        this.playerColor = playerColor;
        this.pieceType = pieceType;
    }

    public List<Coordinate> path(Coordinate to){
        List<Coordinate> path = new ArrayList<>();
        return  path;

    }


    @Override
    public boolean isValidMove(Coordinate to) {

        return false;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
