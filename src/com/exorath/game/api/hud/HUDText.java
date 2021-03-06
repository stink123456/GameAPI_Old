package com.exorath.game.api.hud;

import com.exorath.game.api.hud.effects.HUDEffect;
import com.exorath.game.api.hud.locations.scoreboard.Scoreboard;
import com.exorath.game.api.hud.locations.scoreboard.ScoreboardText;

/**
 * Created by TOON on 8/9/2015.
 */
public class HUDText implements Comparable<HUDText> {

    private String text;
    private String displayText;
    private HUDPriority priority;
    private HUDEffect effect;
    private HUDLocation location;

    private int sequence = 0;//This is for HUDText's with the same priority (FIFO behaviour)

    public HUDText(String text, HUDPriority priority) {
        this.text = text;
        displayText = text;
        this.priority = priority;
    }

    @Override
    public HUDText clone() {
        HUDText hudText = new HUDText(text, priority.clone());
        hudText.setDisplayText(displayText);
        if (hudText != null)
            hudText.setDisplayText(text);
        if (effect != null)
            hudText.setEffect(effect.clone());
        return hudText;
    }

    public void setText(String text) {
        this.text = text;
        displayText = effect == null ? text : effect.getDisplayText();
        updateLocation();
    }

    public void setPriority(HUDPriority priority) {
        if (this.priority == priority)
            return;
        this.priority = priority;
        updateLocation();
    }

    public void updateLocation() {
        if (location == null)
            return;
        if (location instanceof HUDDisplay)
            ((HUDDisplay) location).updated(this);
        else if (location instanceof Scoreboard) {
            Scoreboard sb = (Scoreboard) location;
            if (this instanceof ScoreboardText)
                sb.updated((ScoreboardText) this);
            else
                sb.updateTitle();
        }
    }

    public HUDPriority getPriority() {
        return priority;
    }

    public String getText() {
        return text;
    }

    /**
     * Sorting of HUDText's within the priority queue with FIFO behaviour
     * Highest priority to lowest
     */
    @Override
    public int compareTo(HUDText text) {
        if (priority.getPriority() == text.getPriority().getPriority()) {
            if (priority.getSubPriority() == text.getPriority().getSubPriority())
                return text.getSequence() - sequence;
            return text.getPriority().getSubPriority() - priority.getSubPriority();
        }
        return text.getPriority().getPriority().getPriority() - priority.getPriority().getPriority();
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
        updateLocation();
    }

    public void setLocation(HUDLocation location) {
        this.location = location;
        sequence = location.getNewSequence();
    }

    public HUDLocation getLocation() {
        return location;
    }

    public void setEffect(HUDEffect effect) {
        effect.setHUDText(this);
        this.effect = effect;
    }

    public HUDEffect getEffect() {
        return effect;
    }

    /**
     * This occurs every tick while the HUDText is being displayed
     */
    public void tick() {
        if (effect == null)
            return;
        effect.tick();
    }

    protected int getSequence() {
        return sequence;
    }
}
