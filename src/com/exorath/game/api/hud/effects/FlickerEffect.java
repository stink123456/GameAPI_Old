package com.exorath.game.api.hud.effects;

import org.bukkit.ChatColor;

/**
 * Created by TOON on 8/11/2015.
 */
public class FlickerEffect extends IntervalEffect {
    boolean visible = true;
    ChatColor replaceColor;
    public FlickerEffect(int interval){
        super(interval);
    }
    public FlickerEffect(int interval, ChatColor replaceColor){
        super(interval);
        replaceColor = replaceColor;
    }

    @Override
    public void run() {
        visible = !visible;
        setText(getDisplayText());
    }

    @Override
    public String getDisplayText() {
        if (visible) {
            return getText();
        } else {
            if (replaceColor == null) {
                return "";
            } else {
                return replaceColor + ChatColor.stripColor(getText());
            }
        }
    }
}