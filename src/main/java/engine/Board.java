package engine;

import chess.ChessController;
import chess.ChessView;
import chess.PieceType;
import engine.pieces.*;
import chess.PlayerColor;

import java.util.List;


public class Board implements Rule, ChessController {

    //Current turn. False = white, true = black
    private boolean currentTurn = false;
    private ChessView view;

    private Piece[][] chessboard;


    private final int size;
    //private HashMap<Coordinate, Piece> chessboard = new HashMap();

    public Board(int size){
        this.size = size;
        this.chessboard = new Piece[size][size];
    }

    @Override
    public void start(ChessView view) {
        this.view = view;
        this.view.startView();
    }

    @Override
    public boolean move(int fromX, int fromY, int toX, int toY) {

        Piece currentPiece = chessboard[fromX][fromY];
        boolean canMove;
        canMove = currentPiece.isValidMove(new Coordinate(toX, toY));

        if(canMove){
            chessboard[toX][toY] = currentPiece;
            currentPiece.setCoordinate(new Coordinate(toX, toY));
            chessboard[fromX][fromY] = null;
            view.putPiece(PieceType.PAWN, PlayerColor.WHITE, toX, toY);
            view.removePiece(fromX, fromY);
        }



        return canMove;
    }

    @Override
    public void newGame() {

        view.putPiece(PieceType.PAWN, PlayerColor.WHITE,0,1);
        chessboard[0][1] = new Pawn(new Coordinate(0,1), PlayerColor.WHITE);

    }


    public boolean isWithinBounds(Coordinate cord) {
        return cord.getX() >= 0 && cord.getX() < size && cord.getY() >= 0 && cord.getY() < size;
    }

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
        for (PlayerColor col : PlayerColor.values()){
            int row = (col == PlayerColor.WHITE) ? 0 : size - 1;
            addPiece(new Rook(new Coordinate(row,0 ), col));
            addPiece(new Knight(new Coordinate(row, 1), col));
            addPiece(new Bishop(new Coordinate(row, 2), col));
            addPiece(new Queen(new Coordinate(row, 3), col));
            addPiece(new King(new Coordinate(row, 3), col));
        }
    }

    public void addPiece(Piece piece){

    }

    public void removePiece(Piece piece){

    }

    public boolean isOccupied(Coordinate coordinate){
        return false;
    }

    public Piece move(Coordinate to, Piece piece){
        if(isWithinBounds(to)){
            piece.isValidMove(to);
        }
        return null;
    }

    public boolean isEmptyBetween(List<Coordinate> path){
        return false;
    }


}
