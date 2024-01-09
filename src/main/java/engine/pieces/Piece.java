package engine.pieces;

import chess.ChessView;
import chess.PlayerColor;
import chess.PieceType;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece implements MoveValidator, ChessView.UserChoice {

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

    public abstract List<Coordinate> path(Coordinate to);

    @Override
    public abstract boolean isValidMove(Coordinate to);

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public abstract String textValue();
}
