package com.exorath.game.api.team;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.Location;

import org.bukkit.Bukkit;

import com.exorath.game.api.BasePlayerProperty;
import com.exorath.game.api.GameListener;
import com.exorath.game.api.Properties;
import com.exorath.game.api.player.GamePlayer;
import com.google.common.collect.Sets;

/**
 * Created by too on 23/05/2015.
 * Base object for a Team.
 */
public class Team {
    
    protected static final String DEFAULT_NAME = "team";
    protected static final int DEFAULT_MAX_PLAYER_AMOUNT = Bukkit.getServer().getMaxPlayers();
    
    private Properties properties = new Properties();
    private Set<GamePlayer> players = new HashSet<GamePlayer>();
    private final Set<GameListener> listeners = Sets.newHashSet();
    
    public Set<GamePlayer> getPlayers() {
        return this.players;
    }
    
    public void addPlayer( GamePlayer player ) {
        this.players.add( player );
    }
    
    public boolean containsPlayer( GamePlayer player ) {
        if ( this.players.contains( player ) ) {
            return true;
        }
        return false;
    }
    
    public void removePlayer( GamePlayer player ) {
        if ( !this.players.contains( player ) ) {
            return;
        }
        this.players.remove( player );
    }
    
    public void startGame() {
        this.updatePlayers();
        for ( GamePlayer player : this.players ) {
            
        }
    }
    
    public void updatePlayers() {
        Set<GamePlayer> tempPlayers = new HashSet<GamePlayer>();
        for ( GamePlayer player : this.players ) {
            if ( !player.isOnline() ) {
                tempPlayers.add( player );
            }
        }
        this.players.removeAll( tempPlayers );
    }
    
    public Properties getProperties() {
        return this.properties;
    }
    
    public void setName( String name ) {
        this.properties.set( TeamProperty.NAME, name );
    }
    
    public String getName() {
        return this.properties.as( TeamProperty.NAME, String.class );
    }
    
    public void setPvpEnabled( boolean enabled ) {
        this.properties.set( BasePlayerProperty.PVP, enabled );
    }
    
    public boolean isPvpEnabled() {
        return this.properties.as( BasePlayerProperty.PVP, boolean.class );
    }
    
    public void setTeamSize( int amount ) {
        this.properties.set( TeamProperty.SIZE, amount );
    }
    
    public int getTeamSize() {
        return this.properties.as( TeamProperty.SIZE, int.class );
    }
    
    public void addSpawnPoint( Location spawn ) {
        this.getSpawns().add( spawn );
    }
    
    public void removeSpawnPoint( Location spawn ) {
        if ( !this.getSpawns().contains( spawn ) ) {
            return;
        }
        this.getSpawns().remove( spawn );
    }
    
    @SuppressWarnings( "unchecked" )
    public List<Location> getSpawns() {
        return this.properties.as( TeamProperty.SPAWNS, List.class );
    }
    
    protected void addListener( GameListener listener ) {
        if ( listener != null ) {
            this.listeners.add( listener );
        }
    }
    
}
