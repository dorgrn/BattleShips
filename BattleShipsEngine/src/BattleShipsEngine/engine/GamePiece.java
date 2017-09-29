package BattleShipsEngine.engine;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class GamePiece implements Serializable {
    protected Hashtable<Point, BodyPart> body;
    Point position;

    private boolean isActive = true;

    protected GamePiece(int size, Point position) {
        // create new body
        body = new Hashtable<>(size);
        this.position = position;
        // @param@: body should be init by specific piece (createBody)
    }

    protected void takeDamage(Point point) {
        if (body.containsKey(point)) {
            BodyPart bodyPart = body.get(point);
            bodyPart.wasHit = true;
            if (isDead()) {
                isActive = false;
            }
        }
    }

    protected abstract void createBody(NavalFactory.GamePieceType type);

    public Point getPosition() {
        return this.position;
    }

    public boolean isActive() {
        return isActive;
    }

    protected boolean hasPoint(Point point) {
        return body.containsKey(point);
    }

    protected boolean isDead() {
        for ( Map.Entry<Point, BodyPart> entry : body.entrySet() ) {
            if (!entry.getValue().wasHit) {
                return false;
            }
        }

        return true;
    }

    protected List<Point> getHitPoints() {
        return body.entrySet().stream()
                .filter(entry -> entry.getValue().wasHit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public int getLength() {
        return body.size();
    }

    public Hashtable<Point, BodyPart> getBody() {
        return body;
    }

    protected class BodyPart implements Serializable {
        private Point position;
        private boolean wasHit = false;

        public BodyPart(Point position) {
            this.position = position;
        }

        public boolean isHit() {
            return wasHit;
        }

        public Point getPosition() {
            return position;
        }
    }
}
