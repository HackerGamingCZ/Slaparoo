package cz.hackergamingcz.slaparoo.Handlers;

public enum GameState {

    WAITING(true), INGAME(true), RESET(true);

    private static GameState currentState;

    private boolean canJoin;

    GameState(boolean canJoin){
        this.canJoin = canJoin;
    }

    public static GameState getState(){
        return currentState;
    }

    public static void setState(GameState state){
        currentState = state;
    }

    public static boolean isState(GameState s){
        return currentState == s;
    }
}