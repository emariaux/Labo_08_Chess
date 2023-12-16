package engine;

import chess.ChessController;
import chess.ChessView;
import chess.PieceType;
import engine.pieces.*;
import chess.PlayerColor;

import java.util.List;


public class Board implements Rule, ChessController {

    //Current turn. False = white, true = black
    private PlayerColor currentPlayer;
    private ChessView view;

    private Piece[][] chessboard;


    private final int size;
    //private HashMap<Coordinate, Piece> chessboard = new HashMap();

    public Board(int size){
        this.size = size;
        this.chessboard = new Piece[size][size];
        this.currentPlayer = PlayerColor.WHITE;
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

        if(canMove && currentPlayer == currentPiece.getPlayerColor()){
            chessboard[toX][toY] = currentPiece;
            currentPiece.setCoordinate(new Coordinate(toX, toY));
            chessboard[fromX][fromY] = null;
            view.putPiece(currentPiece.getPieceType(), currentPiece.getPlayerColor(), toX, toY);
            view.removePiece(fromX, fromY);

            // Sets the turn to play to the other color.
            switchPlayer();
        }

        return canMove;
    }

    @Override
    public void newGame() {
        init();

        for(int i = 0; i < chessboard.length; ++i){
            for(int j = 0; j < chessboard[i].length; ++j){
                if(chessboard[i][j] != null){
                   view.putPiece(chessboard[i][j].getPieceType(), chessboard[i][j].getPlayerColor(), chessboard[i][j].getCoordinate().getX(), chessboard[i][j].getCoordinate().getY());
                }
            }
        }
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

    private void switchPlayer(){
        this.currentPlayer = (this.currentPlayer == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE);
    }
    public void init(){
        for (PlayerColor col : PlayerColor.values()){
            int row = (col == PlayerColor.WHITE) ? 0 : size - 1;
            addPiece(new Rook(new Coordinate(0, row ), col));
            addPiece(new Rook(new Coordinate(7, row ), col));
            addPiece(new Knight(new Coordinate(1, row), col));
            addPiece(new Knight(new Coordinate(6, row), col));
            addPiece(new Bishop(new Coordinate(2, row), col));
            addPiece(new Bishop(new Coordinate(5, row), col));
            addPiece(new Queen(new Coordinate(3, row), col));
            addPiece(new King(new Coordinate(4, row), col));

            // Initialize Pawns row
            int pawnRow = (col == PlayerColor.WHITE) ? 1 : size - 2;
            for(int i = 0; i < size; ++i){
                addPiece(new Pawn(new Coordinate(i, pawnRow), col));
            }
        }
    }

    public void addPiece(Piece piece){
        int cordX = piece.getCoordinate().getX();
        int cordY = piece.getCoordinate().getY();
        chessboard[cordX][cordY] = piece;
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
