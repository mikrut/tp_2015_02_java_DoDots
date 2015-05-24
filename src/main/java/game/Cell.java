package game;

/**
 * Created by mihanik
 * 31.03.15 9:15
 * Package: ${PACKAGE_NAME}
 */
class Cell {
    // Owned: totally yours
    // Captured: enemy cell captured by you
    // Occupied: free cell inside your space
    public enum State {
        FREE,
        FIRST_OWNED, SECOND_OWNED,
        CAPTURED_BY_FIRST, CAPTURED_BY_SECOND,
        OCCUPIED_BY_FIRST, OCCUPIED_BY_SECOND
    }

    private State state = State.FREE;
    // Row: state index
    // Column: transition in case of player(index) captureAround
    private final State[][] transitionsAround = {
            // FREE
            {State.OCCUPIED_BY_FIRST, State.OCCUPIED_BY_SECOND},
            // FIRST_OWNED
            {State.FIRST_OWNED, State.CAPTURED_BY_SECOND},
            // SECOND_OWNED
            {State.CAPTURED_BY_FIRST, State.SECOND_OWNED},
            // CAPTURED_BY_FIRST
            {State.CAPTURED_BY_FIRST, State.SECOND_OWNED},
            // CAPTURED_BY_SECOND
            {State.FIRST_OWNED, State.CAPTURED_BY_SECOND},
            // OCCUPIED_BY_FIRST
            {State.OCCUPIED_BY_FIRST, State.OCCUPIED_BY_SECOND},
            // OCCUPIED_BY_SECOND
            {State.OCCUPIED_BY_FIRST, State.OCCUPIED_BY_SECOND}

    };

    private Boolean isMarked = false;

    public void captureAround(int playerIndex) {
        state = transitionsAround[state.ordinal()][playerIndex];
    }

    public boolean captureUsual(int playerIndex) {
        switch (state) {
            case FREE:
                state = State.values()[State.FIRST_OWNED.ordinal()+playerIndex];
                return true;
            case OCCUPIED_BY_FIRST:
                if (playerIndex == 0) {
                    return false;
                } else {
                    state = State.CAPTURED_BY_FIRST;
                    return true;
                }
            case OCCUPIED_BY_SECOND:
                if (playerIndex == 0) {
                    state = State.CAPTURED_BY_SECOND;
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        }
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setState(int stateId) {
        this.state = State.values()[stateId];
    }

    public State getState() {
        return state;
    }

    public void mark() {
        isMarked = true;
    }

    public void unmark() {
        isMarked = false;
    }

    public Boolean isMarked() {
        return isMarked;
    }

    public boolean canMove(int userIndex) {
        return state.ordinal() != State.FIRST_OWNED.ordinal() + userIndex;
    }

    public static boolean isFreeToCapture(State state) {
        return  state == State.FREE;
    }

    public Integer finalCapture() {
        if (state == State.FIRST_OWNED) {
            setState(State.CAPTURED_BY_FIRST);
            return 0;
        }
        if (state == State.SECOND_OWNED) {
            setState(State.CAPTURED_BY_SECOND);
            return 1;
        }
        return null;
    }
}
