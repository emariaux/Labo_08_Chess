import chess.ChessController;
import chess.ChessView;
import chess.views.gui.GUIView;
import engine.Board;

public class Main {
    public static void main(String [] args) {

        ChessController controller = new Board();

        ChessView view = new GUIView(controller);

        controller.start(view);
    }
}
