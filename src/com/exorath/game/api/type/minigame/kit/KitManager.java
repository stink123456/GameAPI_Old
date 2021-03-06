package com.exorath.game.api.type.minigame.kit;

import java.util.Set;

import com.exorath.game.api.Game;
import com.exorath.game.api.Manager;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * @author Nick Robson
 */
public class KitManager implements Manager {

    private final Game game;
    private Set<Kit> kits = Sets.newHashSet();

    public KitManager(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return this.game;
    }

    public void add(Kit kit) {
        if (kit != null) {
            this.kits.add(kit);
        }
    }

    public Set<Kit> getKits() {
        return ImmutableSet.<Kit> copyOf(this.kits);
    }

}
