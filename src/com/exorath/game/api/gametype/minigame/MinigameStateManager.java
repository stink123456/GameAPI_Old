package com.exorath.game.api.gametype.minigame;

import com.exorath.game.GameAPI;
import com.exorath.game.api.GameRunnable;
import com.exorath.game.api.GameState;
import com.exorath.game.api.gametype.minigame.countdown.MinigameCountdown;

/**
 * Created by Toon on 9/1/2015.
 * Not functional yet
 */
public class MinigameStateManager {
    private Minigame minigame;
    private MinigameCountdown countdown = new MinigameCountdown(minigame);

    public MinigameStateManager(Minigame minigame){
        this.minigame = minigame;
    }
    public void checkStart(){
        if(minigame.getState() != GameState.WAITING)
            return;
        if(minigame.getPlayerCount() >= minigame.getProperties().as(Minigame.MIN_PLAYERS, Integer.class)){
            countdown.start();
        }
    }
    public void checkStop(){
        if(minigame.getState() != GameState.WAITING)
            return;
        if(minigame.getPlayerCount() <= minigame.getProperties().as(Minigame.MIN_PLAYERS, Integer.class)){
            countdown.stop();
        }
    }

    //** State Loop [WAITING -> STARTING -> INGAME -> FINISHING -> RESETTING] **//
    //* Start: *//
    public void start(){
        if(minigame.getState() != GameState.WAITING)
            throw new IllegalStateException("Tried to change state from " + minigame.getState() + " to " + GameState.STARTING);
        minigame.setState(GameState.STARTING);
        minigame.spawnPlayers();
        setIngame();
    }
    protected void setIngame(){
        if(minigame.getState() != GameState.STARTING)
            throw new IllegalStateException("Tried to change state from " + minigame.getState() + " to " + GameState.INGAME);
        minigame.setState(GameState.INGAME);
        //Force end game if max duration is reached
        int delay = minigame.getProperties().as(Minigame.MAX_DURATION, Integer.class);
        if(delay != 0)
            new EndTask().runTaskLater(GameAPI.getInstance(), delay);

    }
    public void stop(StopReason reason){
        if(minigame.getState() != GameState.INGAME)
            throw new IllegalStateException("Tried to change state from " + minigame.getState() + " to " + GameState.FINISHING);
        minigame.setState(GameState.FINISHING);
        minigame.reward();
        setResetting();

    }
    public void setResetting(){
        if(minigame.getState() != GameState.FINISHING)
            throw new IllegalStateException("Tried to change state from " + minigame.getState() + " to " + GameState.RESETTING);
        minigame.setState(GameState.RESETTING);
        minigame.reset();
        setWaiting();
    }
    public void setWaiting(){
        if(minigame.getState() != GameState.RESETTING)
            throw new IllegalStateException("Tried to change state from " + minigame.getState() + " to " + GameState.WAITING);
        minigame.setState(GameState.WAITING);
    }
    private class EndTask extends GameRunnable{
        public EndTask(){super(minigame);}
        @Override
        public void _run() {
            if(minigame.getState() == GameState.INGAME)
                minigame.getStateManager().stop(StopReason.TIME_LIMIT_REACHED);
        }
    }
}
