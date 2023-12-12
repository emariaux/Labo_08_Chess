package engine;

import engine.pieces.*;

import java.util.HashMap;
import java.util.List;


public class Board implements Rule {

    private HashMap<Coordinate, Piece> chessboard = new HashMap();

    @Override
    public boolean enPassant(Coordinate to, Pawn pawn) {
        return false;
    }

    @Override
    public void promote(Pawn pawn) {

    }

    @Override
    public void isCastleKingSide(Coordinate from, Coordinate to) {

    }

    @Override
    public void isCastleQueenSide(Coordinate from, Coordinate to) {

    }

    public void init(){

    }

    public void addPiece(Piece piece){

    }

    public void removePiece(Piece piece){

    }

    public boolean isOccupied(Coordinate coordinate){
        return false;
    }

    public Piece move(Coordinate to, Piece piece){
        piece.isValidMove(to);
        return null;
    }

    public boolean isEmptyBetween(List<Coordinate> path){
        return false;
    }

}
