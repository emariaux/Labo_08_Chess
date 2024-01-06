import chess.ChessController;
import chess.ChessView;
import chess.views.console.ConsoleView;
import chess.views.gui.GUIView;
import engine.Board;

public class consoleMode {

    public static void main(String [] args) {

        ChessController controller = new Board(8);

        ChessView view = new ConsoleView(controller);

        controller.start(view);
    }

}
