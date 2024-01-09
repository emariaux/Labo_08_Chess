package engine;

import chess.ChessController;
import chess.ChessView;
import engine.pieces.*;
import chess.PlayerColor;

import java.util.List;


public class Board implements Rule, ChessController {
    private PlayerColor currentPlayer;
    private ChessView view;

    // The chessboard is represented as a 2D array of pieces.
    private Piece[][] chessboard;

    private final int size;

    private King whiteKing;
    private King blackKing;

    /***
     * Constructor.
     * @param size : The size of the chessboard.
     */
    public Board(int size){
        this.size = size;
        this.chessboard = new Piece[size][size];
        this.currentPlayer = PlayerColor.WHITE;
    }

    /***
     * Starts the game.
     * @param view la vue à utiliser
     */
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

        // If the starting coordinate is empty, the move is not valid.
        if(chessboard[fromX][fromY] == null){
            return false;
        }

        // Check if the move is valid for the piece.
        canMove = currentPiece.isValidMove(new Coordinate(toX, toY));

        // If the move is valid, check if the destination is occupied by a piece of the same color.
        if(canMove && chessboard[toX][toY] != null
                && currentPiece.getPlayerColor() == chessboard[toX][toY].getPlayerColor() ){
            return false;
        }

        // If the move is valid, check if the piece is a king and if it can castle.
        if(!canMove && currentPiece instanceof King && chessboard[toX][toY] instanceof Rook
                && currentPiece.getPlayerColor() == chessboard[toX][toY].getPlayerColor()) {
            if (toX == 7) {
                if(isCastleKingSide((King) currentPiece, (Rook) chessboard[toX][toY]))
                {
                    switchPlayer();
                    return true;
                }
            } else if (toX == 0) {
                if(isCastleQueenSide((King) currentPiece, (Rook) chessboard[toX][toY]))
                {
                    switchPlayer();
                    return true;
                }
            }
        }

        // If the piece is a pawn and the move is not valid, check if it can eat a piece
        if(currentPiece instanceof Pawn && !canMove){
            if(isOccupied(new Coordinate(toX, toY))){
                eatenPiece = pawnEatPiece(new Coordinate(toX, toY), (Pawn) currentPiece);
            }else{
                eatenPiece = enPassant(new Coordinate(toX, toY), (Pawn) currentPiece);
            }
            if(eatenPiece != null) {
                canMove = true;
            }

        }

        //check if si empty between the two coordinates for bishop, rook and queen
        if(canMove && (currentPiece instanceof Bishop || currentPiece instanceof Rook || currentPiece instanceof Queen)){
            List<Coordinate> path = currentPiece.path(new Coordinate(toX, toY));
            canMove = isEmptyBetween(path);
        }

        // If the move is valid, apply the move.
        if(canMove && currentPlayer == currentPiece.getPlayerColor()){
            if(isOccupied(new Coordinate(toX, toY))){
                eatenPiece = chessboard[toX][toY];
            }
            if(simulateMoveCheck(currentPiece, eatenPiece, fromX, fromY, toX, toY)){
                applyMove(currentPiece, eatenPiece, fromX, fromY, toX, toY);
                if(currentPiece instanceof Pawn){
                    checkPromote((Pawn) currentPiece);
                }
            }else{
                canMove = false;
            }
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
    }

    @Override
    public Piece enPassant(Coordinate to, Pawn pawn) {

        int yOffset = pawn.getPlayerColor() == PlayerColor.WHITE ? -1 : 1;
        PlayerColor opponentColor = pawn.getPlayerColor() == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
        Coordinate targetCoord = new Coordinate(to.getX(), to.getY() + yOffset);

        if(isOccupied(targetCoord)){
            Piece eatenPiece = chessboard[targetCoord.getX()][targetCoord.getY()];
            if (eatenPiece instanceof Pawn && eatenPiece.getPlayerColor() == opponentColor) {
                if (((Pawn) eatenPiece).isLastMoveWasDoubleForward()) {
                    return eatenPiece;
                }
            }

        }
        return null;
    }


    /***
     * Checks if a pawn can be promoted.
     * @param pawn : The pawn to check.
     */
    public void checkPromote(Pawn pawn) {

        if(pawn.getPlayerColor() == PlayerColor.WHITE){
            if(pawn.getCoordinate().getY() == 7){
                promote(pawn);
            }
        }

        if(pawn.getPlayerColor() == PlayerColor.BLACK){
            if(pawn.getCoordinate().getY() == 0){
                promote(pawn);
            }
        }
    }

    /***
     * Promotes a pawn to a piece chosen by the user.
     * @param pawn : The pawn to promote.
     */
    @Override
    public void promote(Pawn pawn) {

        // The choices for the user.
        Piece[] choices = {
                new Queen(pawn.getCoordinate(), pawn.getPlayerColor()),
                new Knight(pawn.getCoordinate(), pawn.getPlayerColor()),
                new Rook(pawn.getCoordinate(), pawn.getPlayerColor()),
                new Bishop(pawn.getCoordinate(), pawn.getPlayerColor())
        };

        Piece userChoice;

        do {
            userChoice = view.askUser("Promotion", "Choose a piece to promote to", choices);
        } while (userChoice == null);

        removePiece(pawn);
        addPiece(userChoice);
    }

    /***
     * Castle king side if possible.
     * @param king : The king for the castle.
     * @param rook : The rook for the castle.
     * @return : True if the castle was successful, false otherwise.
     */
    @Override
    public boolean isCastleKingSide(King king, Rook rook) {
        //Check if the king and the rook have already moved
        if(!king.isHasMoved() && !rook.isHasMoved()){
            List<Coordinate> path = rook.path(king.getCoordinate());
            //Check if the path is empty
            if(isEmptyBetween(path)) {
                applyMove(king,null,king.getCoordinate().getX(), king.getCoordinate().getY(),6,king.getCoordinate().getY());
                applyMove(rook,null,rook.getCoordinate().getX(), rook.getCoordinate().getY(),5,rook.getCoordinate().getY());
                return true;
            }

        }
        return false;
    }

    /***
     * Castle queen side if possible.
     * @param king : The king for the castle.
     * @param rook : The rook for the castle.
     * @return : True if the castle was successful, false otherwise.
     */
    @Override
    public boolean isCastleQueenSide(King king, Rook rook) {
        //Check if the king and the rook have already moved
        if(!king.isHasMoved() && !rook.isHasMoved()){
            List<Coordinate> path = rook.path(king.getCoordinate());
            //Check if the path is empty
            if(isEmptyBetween(path)) {
                applyMove(king,null,king.getCoordinate().getX(), king.getCoordinate().getY(),2,king.getCoordinate().getY());
                applyMove(rook,null,rook.getCoordinate().getX(), rook.getCoordinate().getY(),3,rook.getCoordinate().getY());
                return true;
            }

        }
        return false;
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
        whiteKing = (King) chessboard[4][0];
        blackKing = (King) chessboard[4][7];
    }

    /***
     * Adds a piece to the chessboard.
     * @param piece : The piece to add.
     */
    public void addPiece(Piece piece){
        int cordX = piece.getCoordinate().getX();
        int cordY = piece.getCoordinate().getY();
        chessboard[cordX][cordY] = piece;
        view.putPiece(piece.getPieceType(), piece.getPlayerColor(), cordX, cordY);
    }

    /***
     * Removes a piece from the chessboard.
     * @param piece : The piece to remove.
     */
    public void removePiece(Piece piece){
        int cordX = piece.getCoordinate().getX();
        int cordY = piece.getCoordinate().getY();
        chessboard[cordX][cordY] = null;
        view.removePiece(cordX, cordY);
    }

    /**
     * Checks if a coordinate is occupied by a piece.
     * @param coordinate : The coordinate to check.
     * @return           : True if the coordinate is occupied, false otherwise.
     */
    public boolean isOccupied(Coordinate coordinate){
        return chessboard[coordinate.getX()][coordinate.getY()] != null;
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

    /***
     * Checks if the king of the current player is in check.
     * @return : True if the king is in check, false otherwise.
     */
    private boolean verifyCheck(){

        King king = currentPlayer == PlayerColor.WHITE ? whiteKing : blackKing;

        if(horizontalVerticalCheck(king)){
            setCheck(king);
            return true;
        }

        if(diagonalCheck(king)){
            setCheck(king);
            return true;
        }

        if(knightCheck(king)){
            setCheck(king);
            return true;
        }

        if(pawnCheck(king)){
            setCheck(king);
            return true;
        }

        if(kingCheck(king)){
            setCheck(king);
            return true;
        }

        return false;
    }

    /***
     * Sets the check status of the king in parameter.
     * @param king : The king to set the check status.
     */
    private void setCheck(King king){
        king.setCheck(true);

        if(king.getPlayerColor() == PlayerColor.WHITE){
            view.displayMessage("Echec roi blanc");
        } else {
            view.displayMessage("Echec roi noir");
        }
    }

    /***
     * Checks if the king is in check by a piece on the horizontal or vertical axis.
     * @param king : The king to check.
     * @return     : True if the king is in check by a piece on the horizontal or vertical axis, false otherwise.
     */
    private boolean horizontalVerticalCheck(King king) {
        int kingX = king.getCoordinate().getX();
        int kingY = king.getCoordinate().getY();

        // Define horizontal and vertical directions
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}; // right, left, down, up

        // Check each horizontal and vertical direction
        for (int[] direction : directions) {
            int dx = direction[0];
            int dy = direction[1];
            int x = kingX;
            int y = kingY;

            // Check the line or column
            while (x >= 0 && x < 8 && y >= 0 && y < 8) {
                x += dx;
                y += dy;

                // Check if the square is within bounds and occupied
                if (x >= 0 && x < 8 && y >= 0 && y < 8 && chessboard[x][y] != null) {
                    Piece piece = chessboard[x][y];
                    // Check if the piece is a threat (Rook or Queen of opposite color)
                    if (piece.getPlayerColor() != king.getPlayerColor() && (piece instanceof Rook || piece instanceof Queen)){
                        return true; // King in check by a horizontal or vertical piece
                    }
                    break; // If there is a piece on the line or column, stop looking further
                }
            }
        }

        return false; // No horizontal or vertical threat found
    }

    /***
     * Checks if the king is in check by a piece on the diagonal axis.
     * @param king : The king to check.
     * @return     : True if the king is in check by a piece on the diagonal axis, false otherwise.
     */
    private boolean diagonalCheck(King king) {
        int kingX = king.getCoordinate().getX();
        int kingY = king.getCoordinate().getY();

        // Define diagonal directions: (dx, dy)
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

       // Check each diagonal direction
        for (int[] direction : directions) {
            int dx = direction[0];
            int dy = direction[1];
            int x = kingX;
            int y = kingY;

            // Check the diagonal
            while (x >= 0 && x < 8 && y >= 0 && y < 8) {
                x += dx;
                y += dy;

               // Check if the square is within bounds and occupied
                if (x >= 0 && x < 8 && y >= 0 && y < 8 && chessboard[x][y] != null) {
                    Piece piece = chessboard[x][y];
                    // Check if the piece is a threat (Bishop or Queen of opposite color)
                    if (piece.getPlayerColor() != king.getPlayerColor() && (piece instanceof Bishop || piece instanceof Queen)){
                        return true; // King in check by a diagonal piece
                    }
                    break; // If there is a piece on the diagonal, stop looking further
                }
            }
        }

        return false; // No diagonal threat found
    }

    /***
     * Checks if the king is in check by a knight.
     * @param king : The king to check.
     * @return     : True if the king is in check by a knight, false otherwise.
     */
    private boolean knightCheck(King king) {
        int kingX = king.getCoordinate().getX();
        int kingY = king.getCoordinate().getY();

        // Define knight moves: (dx, dy)
        int[][] knightMoves = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1}, // Long vertical moves
                {1, 2}, {-1, 2}, {1, -2}, {-1, -2}  // Long horizontal moves
        };

        // Check each knight move
        for (int[] move : knightMoves) {
            int x = kingX + move[0];
            int y = kingY + move[1];

            // Check if the position is within bounds
            if (x >= 0 && x < 8 && y >= 0 && y < 8) {
                Piece piece = chessboard[x][y];
                // Check if a piece is a knight of opposite color
                if (piece != null && piece.getPlayerColor() != king.getPlayerColor() && piece instanceof  Knight){
                    return true; // King in check by a knight
                }
            }
        }

        return false; // No knight threat found
    }

    /***
     * Checks if the king is in check by a pawn.
     * @param king : The king to check.
     * @return     : True if the king is in check by a pawn, false otherwise.
     */
    private boolean pawnCheck(King king) {
        int kingX = king.getCoordinate().getX();
        int kingY = king.getCoordinate().getY();
        PlayerColor kingColor = king.getPlayerColor();

        // Determine the direction of the pawn attack based on the king's color
        int pawnDirection = (kingColor == PlayerColor.WHITE) ? 1 : -1; // Les pions blancs se déplacent vers le haut (-1), les noirs vers le bas (+1)

        // Diagonal positions where a pawn could attack
        int[][] pawnAttacks = {
                {1, pawnDirection},  // right diagonal
                {-1, pawnDirection}  // left diagonal
        };

        // Check each diagonal position
        for (int[] attack : pawnAttacks) {
            int x = kingX + attack[0];
            int y = kingY + attack[1];

            // Check if the position is within bounds
            if (x >= 0 && x < 8 && y >= 0 && y < 8) {
                Piece piece = chessboard[x][y];
                // Check if a piece is a pawn of opposite color
                if (piece != null && piece.getPlayerColor() != kingColor && piece instanceof Pawn){
                    return true; // King in check by a pawn
                }
            }
        }

        return false; // No pawn threat found
    }

    /***
     * Checks if the king is in check by the other king.
     * @param king : The king to check.
     * @return     : True if the king is in check by the other king, false otherwise.
     */
    private boolean kingCheck(King king) {

        King oppositeKing = king.getPlayerColor() == PlayerColor.WHITE ? blackKing : whiteKing;

        int kingX = king.getCoordinate().getX();
        int kingY = king.getCoordinate().getY();
        int opposingKingX = oppositeKing.getCoordinate().getX();
        int opposingKingY = oppositeKing.getCoordinate().getY();

        // Check if the kings are adjacent to each other
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if ((kingX + dx == opposingKingX) && (kingY + dy == opposingKingY)) {
                    return true; // King in check by the other king
                }
            }
        }
        return false;
    }


    /***
     * Simulates a move to check if the king is in check after the move.
     * @param currentPiece  : The piece to move.
     * @param eatenPiece    : The piece to eat.
     * @param fromX         : The x coordinate of the piece to move.
     * @param fromY         : The y coordinate of the piece to move.
     * @param toX           : The x coordinate of the destination.
     * @param toY           : The y coordinate of the destination.
     * @return              : True if the king is not in check after the move, false otherwise.
     */
    private boolean simulateMoveCheck(Piece currentPiece, Piece eatenPiece, int fromX, int fromY, int toX, int toY){

        boolean valideMove;

        Coordinate currentPieceCoordBCK = currentPiece.getCoordinate();

        if(eatenPiece != null){
            chessboard[eatenPiece.getCoordinate().getX()][eatenPiece.getCoordinate().getY()] = null;
        }

        chessboard[toX][toY] = currentPiece;
        currentPiece.setCoordinate(new Coordinate(toX, toY));
        chessboard[fromX][fromY] = null;

        valideMove = !verifyCheck();

        if (!valideMove) {
            currentPiece.setCoordinate(currentPieceCoordBCK);
            chessboard[fromX][fromY] = currentPiece;
            chessboard[toX][toY] = eatenPiece;
        }
        return valideMove;
    }

}
