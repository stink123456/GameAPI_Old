package com.exorath.game.api.gametype.minigame.maps;

import com.exorath.game.GameAPI;
import com.exorath.game.api.Game;
import com.exorath.game.api.Manager;
import com.exorath.game.api.maps.GameMap;
import com.exorath.game.api.maps.GameSpawn;
import com.exorath.game.api.maps.MapManager;
import com.exorath.game.api.player.GamePlayer;
import com.exorath.game.lib.JoinLeave;

import java.util.Random;

/**
 * Created by TOON on 9/23/2015.
 */
public class MinigameMapManager implements Manager{
    private Game game;
    private MapManager mapManager;

    //Map Selection
    private MapSelection selection = MapSelection.RANDOM;
    private GameMap current;
    private int index = 0;
    public MinigameMapManager(Game game){
        this.game = game;
        mapManager = game.getManager(MapManager.class);
    }
    //** Selection **//
    public MapSelection getSelection() {
        return selection;
    }
    public void setSelection(MapSelection selection) {
        this.selection = selection;
    }
    //** Map rotation **//
    public GameMap getCurrent() {
        return current == null ? nextMap(MapSelection.RANDOM) : current;
    }

    public void nextMap() {
        nextMap(selection);
    }
    public GameMap nextMap(MapSelection selection) {
        if (game.getManager(MapManager.class).getMapList().getMaps().size() == 0) {
            GameAPI.error("Map size == 0: Please add a map!");
            return null;
        }
        switch (selection) {
            case CYCLE:
                current = mapManager.getMapList().getMaps().get(index++);
            case RANDOM:
                current = mapManager.getMapList().getMaps().get(new Random().nextInt(mapManager.getMapList().getMaps().size()));
            case SAME:
                current.reset();
            case VOTE:
                // TODO: Call vote with callback to set map. Something will need to be done about the return type...
                break;
            default:
                break;
        }
        return current;
    }
}
