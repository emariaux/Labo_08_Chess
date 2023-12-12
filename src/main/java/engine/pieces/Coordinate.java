package engine.pieces;

import lombok.Getter;
import lombok.Setter;

public class Coordinate {
    @Getter
    @Setter
    private int x;

    @Getter
    @Setter
    private int y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
}
