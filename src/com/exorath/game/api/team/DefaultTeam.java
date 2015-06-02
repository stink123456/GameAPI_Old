package com.exorath.game.api.team;

import com.exorath.game.api.BasePlayerProperty;

/**
 * Created by too on 24/05/2015.
 * Placeholder team until teams are created, this is for minimizing code amount
 */
public class DefaultTeam extends Team {
    
    private static final String DEFAULT_TEAM_NAME = "Default team";
    
    public DefaultTeam() {
        this.getProperties().set( TeamProperty.NAME, DefaultTeam.DEFAULT_TEAM_NAME );
        this.getProperties().set( BasePlayerProperty.INTERACT, true );
    }
}