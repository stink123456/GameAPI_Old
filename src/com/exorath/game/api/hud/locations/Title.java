package com.exorath.game.api.hud.locations;

import com.exorath.game.api.hud.HUDText;
import com.exorath.game.api.player.GamePlayer;
import com.exorath.game.lib.hud.title.TitleBase;

/**
 * Created by TOON on 8/11/2015.
 */
public class Title extends Title_SubTitleBase {

    public Title(GamePlayer player) {
        super(player, 32);
    }

    @Override
    public void removeSelf() {
        TitleBase.sendTitle(player.getBukkitPlayer(), getJSON(""));
    }

    @Override
    public void sendWithFadeIn(HUDText text) {
        TitleBase.sendTitle(player.getBukkitPlayer(), 20, Integer.MAX_VALUE / 10, 0, getJSON(text.getDisplayText()));
    }

    @Override
    public void send(HUDText text) {
        TitleBase.sendTitle(player.getBukkitPlayer(), 0, LONG_STAY_TIME, 0, getJSON(text.getDisplayText()));
    }

}
