package BattleShipsEngine.engine;

import java.io.Serializable;

public class Point implements Serializable {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point DeepClone(Point other) {
        return new Point(other.x, other.y);
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Point && (((Point) obj).x == this.x && ((Point) obj).y == this.y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void addToPoint(int x, int y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public String toString() {
        return String.format("(%d,%c)", x + 1, (char) (y + 'A'));
    }
}
