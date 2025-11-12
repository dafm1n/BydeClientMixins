package instance.bydeclient.dev.BydeClient.mixin.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Менеджер для управления позициями модулей на экране
 */
public class PositionManager {
    private static PositionManager instance;
    private Map<String, Position> positions = new HashMap<>();

    private PositionManager() {
        initializeDefaultPositions();
    }

    public static PositionManager getInstance() {
        if (instance == null) {
            instance = new PositionManager();
        }
        return instance;
    }

    private void initializeDefaultPositions() {
        // Позиции по умолчанию
        positions.put("Keystrokes", new Position(10, 10));
        positions.put("CPS", new Position(10, 80));
        positions.put("Ping", new Position(10, 110));
    }

    public Position getPosition(String moduleName) {
        return positions.getOrDefault(moduleName, new Position(10, 10));
    }

    public void setPosition(String moduleName, int x, int y) {
        positions.put(moduleName, new Position(x, y));
    }

    public void resetPosition(String moduleName) {
        if (moduleName.equals("Keystrokes")) {
            positions.put(moduleName, new Position(10, 10));
        } else if (moduleName.equals("CPS")) {
            positions.put(moduleName, new Position(10, 80));
        } else if (moduleName.equals("Ping")) {
            positions.put(moduleName, new Position(10, 110));
        }
    }

    public void resetAllPositions() {
        initializeDefaultPositions();
    }

    /**
     * Класс для хранения позиции
     */
    public static class Position {
        public int x;
        public int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void set(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
