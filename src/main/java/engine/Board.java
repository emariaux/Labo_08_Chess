package engine;

import chess.ChessController;
import chess.ChessView;
import chess.PieceType;
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

        if(chessboard[fromX][fromY] == null){
            return false;
        }

        canMove = currentPiece.isValidMove(new Coordinate(toX, toY));

        // If the move is valid, check if the destination is occupied by a piece of the same color.
        if(canMove && chessboard[toX][toY] != null
                && currentPiece.getPlayerColor() == chessboard[toX][toY].getPlayerColor() ){
            return false;
        }

        // If the move is valid, check if the piece is a king and if it can castle.
        if(!canMove && currentPiece.getPieceType() == PieceType.KING
                && chessboard[toX][toY].getPieceType() == PieceType.ROOK
                && currentPiece.getPlayerColor() == chessboard[toX][toY].getPlayerColor()) {
            if (toX == 7) {
                switchPlayer();
                return isCastleKingSide((King) currentPiece, (Rook) chessboard[toX][toY]);
            } else if (toX == 0) {
                switchPlayer();
                return isCastleQueenSide((King) currentPiece, (Rook) chessboard[toX][toY]);
            }
        }

        // If the piece is a pawn and the move is not valid, check if it can eat a piece
        if(currentPiece.getPieceType() == PieceType.PAWN && !canMove){
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
            if(simulateMoveCheck(currentPiece, eatenPiece, fromX, fromY, toX, toY)){
                applyMove(currentPiece, eatenPiece, fromX, fromY, toX, toY);
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
    public Piece enPassant(Coordinate to, Pawn pawn) {

        int yOffset = pawn.getPlayerColor() == PlayerColor.WHITE ? -1 : 1;
        PlayerColor opponentColor = pawn.getPlayerColor() == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
        Coordinate targetCoord = new Coordinate(to.getX(), to.getY() + yOffset);

        if(isOccupied(targetCoord)){
            Piece eatenPiece = chessboard[targetCoord.getX()][targetCoord.getY()];
            if (eatenPiece.getPieceType() == PieceType.PAWN && eatenPiece.getPlayerColor() == opponentColor) {
                if (((Pawn) eatenPiece).isLastMoveWasDoubleForward()) {
                    return eatenPiece;
                }
            }

        }
        return null;
    }

    @Override
    public void promote(Pawn pawn) {

    }

    @Override
    public boolean isCastleKingSide(King king, Rook rook) {
        if(!king.isHasMoved() && !rook.isHasMoved()){
            List<Coordinate> path = rook.path(king.getCoordinate());
            if(isEmptyBetween(path)) {
                applyMove(king,null,king.getCoordinate().getX(), king.getCoordinate().getY(),6,king.getCoordinate().getY());
                applyMove(rook,null,rook.getCoordinate().getX(), rook.getCoordinate().getY(),5,rook.getCoordinate().getY());
                return true;
            }

        }
        return false;
    }

    @Override
    public boolean isCastleQueenSide(King king, Rook rook) {
        if(!king.isHasMoved() && !rook.isHasMoved()){
            List<Coordinate> path = rook.path(king.getCoordinate());
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



        return false;
    }

    private void setCheck(King king){
        king.setCheck(true);

        if(king.getPlayerColor() == PlayerColor.WHITE){
            view.displayMessage("Echec roi blanc");
        } else {
            view.displayMessage("Echec roi noir");
        }
    }

    private boolean horizontalVerticalCheck(King king) {
        int kingX = king.getCoordinate().getX();
        int kingY = king.getCoordinate().getY();

        // Définir les directions horizontales et verticales
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}; // droite, gauche, bas, haut

        // Parcourir chaque direction horizontale et verticale
        for (int[] direction : directions) {
            int dx = direction[0];
            int dy = direction[1];
            int x = kingX;
            int y = kingY;

            // Parcourir la ligne ou la colonne
            while (x >= 0 && x < 8 && y >= 0 && y < 8) {
                x += dx;
                y += dy;

                // Vérifier si la case est dans les limites et occupée
                if (x >= 0 && x < 8 && y >= 0 && y < 8 && chessboard[x][y] != null) {
                    Piece piece = chessboard[x][y];
                    // Vérifier si la pièce est une menace (Rook ou Queen de couleur opposée)
                    if (piece.getPlayerColor() != king.getPlayerColor() &&
                            (piece.getPieceType() == PieceType.ROOK || piece.getPieceType() == PieceType.QUEEN)) {
                        return true; // Roi en échec par une pièce horizontale/verticale
                    }
                    break; // S'il y a une pièce, arrêter de regarder plus loin dans cette direction
                }
            }
        }

        return false; // Aucune menace trouvée horizontalement ou verticalement
    }


    private boolean diagonalCheck(King king) {
        int kingX = king.getCoordinate().getX();
        int kingY = king.getCoordinate().getY();

        // Définition des directions diagonales : (dx, dy)
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        // Parcourir chaque direction diagonale
        for (int[] direction : directions) {
            int dx = direction[0];
            int dy = direction[1];
            int x = kingX;
            int y = kingY;

            // Parcourir la diagonale
            while (x >= 0 && x < 8 && y >= 0 && y < 8) {
                x += dx;
                y += dy;

                // Vérifier si la case est dans les limites du plateau et occupée
                if (x >= 0 && x < 8 && y >= 0 && y < 8 && chessboard[x][y] != null) {
                    Piece piece = chessboard[x][y];
                    // Vérifier si la pièce est une menace (Bishop ou Queen de couleur opposée)
                    if (piece.getPlayerColor() != king.getPlayerColor() &&
                            (piece.getPieceType() == PieceType.BISHOP || piece.getPieceType() == PieceType.QUEEN)) {
                        return true; // Roi en échec par une pièce diagonale
                    }
                    break; // S'il y a une pièce sur la diagonale, arrêter de regarder plus loin
                }
            }
        }

        return false; // Aucune menace trouvée en diagonale
    }

    private boolean knightCheck(King king) {
        int kingX = king.getCoordinate().getX();
        int kingY = king.getCoordinate().getY();

        // Les mouvements possibles d'un cavalier à partir d'une position donnée
        int[][] knightMoves = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1}, // Mouvements verticaux longs
                {1, 2}, {-1, 2}, {1, -2}, {-1, -2}  // Mouvements horizontaux longs
        };

        // Vérifier chaque mouvement possible du cavalier
        for (int[] move : knightMoves) {
            int x = kingX + move[0];
            int y = kingY + move[1];

            // Vérifier si la position est dans les limites du plateau
            if (x >= 0 && x < 8 && y >= 0 && y < 8) {
                Piece piece = chessboard[x][y];
                // Vérifier si une pièce est un cavalier ennemi
                if (piece != null && piece.getPlayerColor() != king.getPlayerColor() &&
                        piece.getPieceType() == PieceType.KNIGHT) {
                    return true; // Le roi est en échec par un cavalier
                }
            }
        }

        return false; // Aucun cavalier ennemi menaçant trouvé
    }

    private boolean pawnCheck(King king) {
        int kingX = king.getCoordinate().getX();
        int kingY = king.getCoordinate().getY();
        PlayerColor kingColor = king.getPlayerColor();

        // Déterminer la direction de l'attaque du pion en fonction de la couleur du roi
        int pawnDirection = (kingColor == PlayerColor.WHITE) ? 1 : -1; // Les pions blancs se déplacent vers le haut (-1), les noirs vers le bas (+1)

        // Les positions en diagonale où un pion pourrait attaquer
        int[][] pawnAttacks = {
                {1, pawnDirection},  // diagonale droite
                {-1, pawnDirection}  // diagonale gauche
        };

        // Vérifier les deux cases en diagonale devant le roi pour une menace de pion
        for (int[] attack : pawnAttacks) {
            int x = kingX + attack[0];
            int y = kingY + attack[1];

            // Vérifier si la position est dans les limites du plateau
            if (x >= 0 && x < 8 && y >= 0 && y < 8) {
                Piece piece = chessboard[x][y];
                // Vérifier si une pièce est un pion ennemi
                if (piece != null && piece.getPlayerColor() != kingColor &&
                        piece.getPieceType() == PieceType.PAWN) {
                    return true; // Le roi est en échec par un pion
                }
            }
        }

        return false; // Aucun pion ennemi menaçant trouvé
    }


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
