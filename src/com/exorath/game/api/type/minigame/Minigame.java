package com.exorath.game.api.type.minigame;

import java.util.Arrays;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.exorath.game.api.Game;
import com.exorath.game.api.GameListener;
import com.exorath.game.api.Manager;
import com.exorath.game.api.Property;
import com.exorath.game.api.maps.GameMap;
import com.exorath.game.api.player.GamePlayer;
import com.exorath.game.api.player.PlayerManager;
import com.exorath.game.api.spectate.SpectateManager;
import com.exorath.game.api.team.Team;
import com.exorath.game.api.team.TeamManager;
import com.exorath.game.api.type.minigame.kit.KitManager;
import com.exorath.game.api.type.minigame.maps.MinigameMapManager;

/**
 * @author Nick Robson
 */
public abstract class Minigame extends Game {

    public static final Property MIN_PLAYERS = Property.get("minigame.minplayers", "Minimal amount of players in the whole minigame", 2);
    public static final Property MAX_DURATION = Property.get("minigame.maxduration",
            "The maximum duration of the game in ticks. 0 disables.", 0);
    public static final Property START_DELAY = Property.get("minigame.startdelay",
            "Waiting time after there are enough players before game starts", 200);

    public Minigame() {
        addManager(new MinigameMapManager(this));
        Manager[] managers = new Manager[] { new TeamManager(this), new MinigameStateManager(this), new KitManager(this), new SpectateManager(this)};
        Arrays.asList(managers).forEach(m -> addManager(m));

        addListener(new MinigameListener());

    }

    public boolean hasMinPlayers() {
        return getManager(PlayerManager.class).getPlayerCount() >= getProperties().as(Minigame.MIN_PLAYERS, Integer.class);
    }

    public MinigameStateManager getStateManager() {
        return getManager(MinigameStateManager.class);
    }

    protected void spawnPlayers() {
        for (Team team : getManager(TeamManager.class).getTeams())
            for (GamePlayer player : team.getPlayers())
                player.getBukkitPlayer()
                .teleport(getManager(MinigameMapManager.class).getCurrent().getSpawns(team.getName()).getNextSpawn().getLocation());
    }

    protected void reward() {
        //TODO: Develop method
        //This will call an abstract method that gets a reward package for each player.
    }

    protected void reset() {
        //TODO: Develop method
        //Reset map if enabled

        //Reset player inventories

        //Reset players health & hunger

        //Reset players potion effects

        //Teleport players to hub

        getManager(TeamManager.class).getTeams().forEach(t -> t.getActivePlayers().forEach(p -> getLobby().teleport(p)));
        //Reset teams
        getManager(TeamManager.class).reset();
        //Refill teams
        getManager(PlayerManager.class).getPlayers().forEach(p -> getManager(TeamManager.class).join(p));
    }

    //Maps
    public GameMap getCurrent() {
        return getManager(MinigameMapManager.class).getCurrent();
    }

    private class MinigameListener implements GameListener {

        @Override
        public void onJoin(PlayerJoinEvent event, Game game, GamePlayer player) {

        }

        @Override
        public void onQuit(PlayerQuitEvent event, Game game, GamePlayer player) {

        }
    }
}
