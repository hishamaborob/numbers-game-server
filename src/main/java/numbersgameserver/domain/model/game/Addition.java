package numbersgameserver.domain.model.game;

import java.util.HashMap;
import java.util.Map;

public enum Addition {

    ONE(1), ZERO(0), MINUS_ONE(-1);

    private final int addedNumber;

    private static final Map<Integer, Addition> lookup = new HashMap<>();

    static {
        for (Addition a : Addition.values()) {
            lookup.put(a.getAddedNumber(), a);
        }
    }

    private Addition(int addedNumber) {
        this.addedNumber = addedNumber;
    }

    public int getAddedNumber() {
        return addedNumber;
    }

    public static Addition get(int addition) {
        return lookup.get(addition);
    }
}