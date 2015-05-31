package com.exorath.game.api;

<<<<<<< HEAD
import com.exorath.game.GameAPI;
import com.exorath.game.api.lobby.Lobby;
import com.exorath.game.api.teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

=======
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.exorath.game.api.events.GameStateChangedEvent;
import com.exorath.game.api.lobby.Lobby;
import com.exorath.game.api.player.GamePlayer;
import com.exorath.game.api.spectate.SpectateManager;
import com.exorath.game.api.team.TeamManager;
import com.google.common.collect.Sets;
>>>>>>> 34fb0ee790ea229a300837a41beaa21bab6dcb31

/**
 * Created by too on 23/05/2015.
 * Gamemode, RepeatingMinigame and Minigame will extend this.
 */

public abstract class Game implements Listener {

    public static final String DEFAULT_GAME_NAME = "Game";
    public static final String DEFAULT_GAME_DESCRIPTION = "Default game description";
    
    private Lobby lobby = new Lobby();
<<<<<<< HEAD

    private boolean running = false;

    public Game() {
        //register event handler for this game
        Bukkit.getPluginManager().registerEvents(this, GameAPI.getInstance());
        //this.init();

    }

    protected void init() {
        this.properties = new Properties();
        this.lobby = new Lobby();
        this.teamManager = new TeamManager();
    }
    public void startGame(){
        if(running) return;
        running = true;
    }
    public void stopGame() {
        running = false;
=======
    private Properties properties = new Properties();
    private TeamManager teamManager;
    private SpectateManager spectateManager;
    
    private final Map<UUID, PlayerState> playerStates = new HashMap<>();
    
    private final Set<GameListener> listeners = Sets.newHashSet();
    
    private GameState state = GameState.WAITING;
    
    public Game() {
        this.teamManager = new TeamManager( this );
        this.spectateManager = new SpectateManager( this );
    }
    
    public void setState( GameState state ) {
        GameState old = this.state;
        this.state = state;
        GameStateChangedEvent event = new GameStateChangedEvent( this, old, state );
        for ( GameListener listener : this.listeners ) {
            listener.onGameStateChange( event );
        }
>>>>>>> 34fb0ee790ea229a300837a41beaa21bab6dcb31
    }
    
    public Lobby getLobby() {
        return this.lobby;
    }
<<<<<<< HEAD

=======
    
    public GameState getState() {
        return this.state;
    }
    
>>>>>>> 34fb0ee790ea229a300837a41beaa21bab6dcb31
    public TeamManager getTeamManager() {
        return this.teamManager;
    }

    public Properties getProperties() {
        return this.properties;
    }

    protected void setLobby( Lobby lobby ) {
        this.lobby = lobby;
    }

    protected void setTeamManager( TeamManager teamManager ) {
        this.teamManager = teamManager;
    }

    protected void setProperties( Properties properties ) {
        this.properties = properties;
    }
<<<<<<< HEAD

=======
    
    public String getName() {
        return this.properties.as( GameProperty.NAME, String.class );
    }
    
    public String getDescription() {
        return this.properties.as( GameProperty.DESCRIPTION, String.class );
    }
    
>>>>>>> 34fb0ee790ea229a300837a41beaa21bab6dcb31
    public void setName( String name ) {
        this.properties.set( GameProperty.NAME, name );
    }
    
    public void setDescription( String description ) {
        this.properties.set( GameProperty.DESCRIPTION, description );
    }
    
    protected void addListener( GameListener listener ) {
        if ( listener != null ) {
            this.listeners.add( listener );
        }
    }
    
    public PlayerState getPlayerState( GamePlayer player ) {
        PlayerState state = this.playerStates.get( player.getUUID() );
        if ( state == null ) {
            state = PlayerState.UNKNOWN;
            this.playerStates.put( player.getUUID(), state );
        }
        return state;
    }
    
    public boolean isPlaying( GamePlayer player ) {
        return this.getPlayerState( player ) == PlayerState.PLAYING;
    }
    
    public SpectateManager getSpectateManager() {
        return this.spectateManager;
    }
    public boolean isStarted(){
        return running;
    }
}
