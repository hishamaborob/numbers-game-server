package numbersgameserver.projection;

public class GameProjection {

    private String gameId;
    private String playerId;
    private long currentNumber;
    private int lastAddition;

    public GameProjection(String gameId, String playerId, long currentNumber, int lastAddition) {

        this.gameId = gameId;
        this.playerId = playerId;
        this.currentNumber = currentNumber;
        this.lastAddition = lastAddition;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public long getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(long currentNumber) {
        this.currentNumber = currentNumber;
    }

    public int getLastAddition() {
        return lastAddition;
    }

    public void setLastAddition(int lastAddition) {
        this.lastAddition = lastAddition;
    }
}
