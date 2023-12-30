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

    // The chessboard is represented as a 2D array of pieces.
    private Piece[][] chessboard;

    private final int size;

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

    /***
     * Moves a piece from one coordinate to another.
     * @param fromX : The x coordinate of the piece to move.
     * @param fromY : The y coordinate of the piece to move.
     * @param toX   : The x coordinate of the destination.
     * @param toY   : The y coordinate of the destination.
     * @return      : True if the move was successful, false otherwise.
     */
    @Override
    public boolean move(int fromX, int fromY, int toX, int toY) {
        Piece currentPiece = chessboard[fromX][fromY];
        Piece eatenPiece = null;

        boolean canMove;
        canMove = currentPiece.isValidMove(new Coordinate(toX, toY));

        // If the piece is a pawn and the move is not valid, check if it can eat a piece
        if(currentPiece.getPieceType() == PieceType.PAWN && !canMove && isOccupied(new Coordinate(toX, toY))){
            eatenPiece = pawnEatPiece(new Coordinate(toX, toY), (Pawn) currentPiece);
            if(eatenPiece != null) {
                canMove = true;
            }
        }

        //check if si empty between the two coordinates for bishop, rook and queen
        if(canMove && (currentPiece.getPieceType() == PieceType.BISHOP
                || currentPiece.getPieceType() == PieceType.ROOK
                || currentPiece.getPieceType() == PieceType.QUEEN)){
            List<Coordinate> path = currentPiece.path(new Coordinate(toX, toY));
            canMove = isEmptyBetween(path);
        }

        // If the move is valid, apply the move.
        if(canMove && currentPlayer == currentPiece.getPlayerColor()){
            if(isOccupied(new Coordinate(toX, toY))){
                eatenPiece = chessboard[toX][toY];
            }
            applyMove(currentPiece, eatenPiece, fromX, fromY, toX, toY);
        }

        return canMove;
    }

    /***
     * Starts a new game.
     */
    @Override
    public void newGame() {
        this.chessboard = new Piece[size][size];
        this.currentPlayer = PlayerColor.WHITE;

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

    /***
     * Switches the current player.
     */
    private void switchPlayer(){
        this.currentPlayer = (this.currentPlayer == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE);
    }

    /***
     * Initializes the chessboard with the starting pieces.
     */
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

    /***
     * Adds a piece to the chessboard.
     * @param piece : The piece to add.
     */
    public void addPiece(Piece piece){
        int cordX = piece.getCoordinate().getX();
        int cordY = piece.getCoordinate().getY();
        chessboard[cordX][cordY] = piece;
    }

    /***
     * Removes a piece from the chessboard.
     * @param piece : The piece to remove.
     */
    public void removePiece(Piece piece){
        int cordX = piece.getCoordinate().getX();
        int cordY = piece.getCoordinate().getY();
        chessboard[cordX][cordY] = null;
    }

    /**
     * Checks if a coordinate is occupied by a piece.
     * @param coordinate : The coordinate to check.
     * @return           : True if the coordinate is occupied, false otherwise.
     */
    public boolean isOccupied(Coordinate coordinate){
        if(chessboard[coordinate.getX()][coordinate.getY()] != null){
            return true;
        }

        return false;
    }

    /***
     * Checks if a path is empty between two coordinates.
     * @param path : The path to check.
     * @return     : True if the path is empty, false otherwise.
     */
    public boolean isEmptyBetween(List<Coordinate> path){

        for(Coordinate cord : path){
            if(isOccupied(cord)){
                return false;
            }
        }

        return true;
    }

    /***
     * Applies a move to the chessboard.
     * @param currentPiece  : The piece to move.
     * @param eatenPiece    : The piece to eat.
     * @param fromX         : The x coordinate of the piece to move.
     * @param fromY         : The y coordinate of the piece to move.
     * @param toX           : The x coordinate of the destination.
     * @param toY           : The y coordinate of the destination.
     */
    private void applyMove(Piece currentPiece, Piece eatenPiece, int fromX, int fromY, int toX, int toY){

        if(eatenPiece != null){
            removePiece(eatenPiece);
        }

        chessboard[toX][toY] = currentPiece;
        currentPiece.setCoordinate(new Coordinate(toX, toY));
        chessboard[fromX][fromY] = null;
        view.putPiece(currentPiece.getPieceType(), currentPiece.getPlayerColor(), toX, toY);
        view.removePiece(fromX, fromY);


        // Sets the turn to play to the other color.
        switchPlayer();
    }

    /***
     * Checks if a pawn can eat a piece.
     * @param to    : The coordinate to check.
     * @param pawn  : The pawn to check.
     * @return      : The piece to eat if the pawn can eat, null otherwise.
     */
    private Piece pawnEatPiece(Coordinate to, Pawn pawn){

        int deltaX = to.getX() - pawn.getCoordinate().getX();
        int deltaY = to.getY() - pawn.getCoordinate().getY();

        if(pawn.getPlayerColor() == PlayerColor.WHITE){
            if(deltaY == 1 && Math.abs(deltaX) == 1){
                return chessboard[to.getX()][to.getY()];
            }
        } else if(pawn.getPlayerColor() == PlayerColor.BLACK) {
            if (deltaY == -1 && Math.abs(deltaX) == 1) {
                return chessboard[to.getX()][to.getY()];
            }
        }

        return null;
    }


}
