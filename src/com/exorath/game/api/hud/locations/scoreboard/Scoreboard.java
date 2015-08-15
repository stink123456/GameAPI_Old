package com.exorath.game.api.hud.locations.scoreboard;

import com.exorath.game.api.hud.HUDDisplay;
import com.exorath.game.api.hud.HUDLocation;
import com.exorath.game.api.hud.HUDPriority;
import com.exorath.game.api.hud.HUDText;
import com.exorath.game.api.player.GamePlayer;
import com.exorath.game.lib.hud.scoreboard.ScoreboardBase;
import org.bukkit.ChatColor;

/**
 * Created by TOON on 8/11/2015.
 * TODO: CREATE THIS CLASS
 */
public class Scoreboard extends HUDLocation{
    private HUDText title = new HUDText(ChatColor.BOLD + "Title", HUDPriority.GAME_API);
    private ScoreboardBase scoreboard;

    public Scoreboard(GamePlayer player){
        super(player);
        scoreboard = new ScoreboardBase(title.getDisplayText());
        scoreboard.add(player.getBukkitPlayer());
    }

    public void addText(ScoreboardText text) {
        if(!isActive())
            return;
        if(getTexts().contains(text))
            return;
        getTexts().add(text);
        text.setEntry(scoreboard.add(text.getDisplayText(), -1));
        priorityUpdated();
    }
    public void removeText(ScoreboardText text) {
        if(!isActive())
            return;
        if(!getTexts().contains(text))
            return;
        getTexts().remove(text);
        //update scoreboard etc.
    }

    @Override
    public void updated(HUDText text) {//A text has updated on the scoreboard
        if(!isActive())
            return;
        if(text == title){
            updateTitle();
            return;
        }
        if(!getTexts().contains(text))
            return;
        if(!(text instanceof ScoreboardText))
            return;

        ScoreboardText sbText = (ScoreboardText) text;
        if(sbText.isTextUpdated()){//Text updated
            sbText.getEntry().update(sbText.getDisplayText());
        }else if(sbText.isPriorityUpdated()){//Priority updated
            priorityUpdated();
        }
    }

    /**
     * This method updates all entries their value to the new value
     */
    private void priorityUpdated(){
        ScoreboardText[] visibleTexts = getVisibleTexts();
        int length = visibleTexts.length;
        for(int i = 0; i < length; i++){
            getVisibleTexts()[i].getEntry().setValue(length - 1 - i);
        }
    }
    @Override
    public void setActive(boolean active) {
        this.active = active;
        if(active){//If turned on, display the currentText
            scoreboard.add(player.getBukkitPlayer());
        }else{//If turned off, hide all texts
            scoreboard.remove(player.getBukkitPlayer());
        }
    }
    @Override
    public void run(){
        if(!active)
            return;
        title.tick();
        for(ScoreboardText text : getVisibleTexts()){
            text.tick();
        }

    }
    private ScoreboardText[] getVisibleTexts(){
        HUDText[] texts = getTexts().toArray(new HUDText[getTexts().size()]);
        if(getTexts().size() <= 16) {
            return (ScoreboardText[]) texts;
        }else{
            ScoreboardText[] scoreboardTexts = new ScoreboardText[16];
            for(int i = 0; i < 16; i++){
                scoreboardTexts[i] = (ScoreboardText) texts[i];
            }
            return scoreboardTexts;
        }
    }
    //** ScoreboardBase methods **//
    protected ScoreboardBase getScoreboard(){
        return scoreboard;
    }
    protected void setScoreboard(ScoreboardBase scoreboard){
        this.scoreboard = scoreboard;
    }
    public HUDText getTitle(){
        return title;
    }
    private void updateTitle(){
        if(!isActive())
            return;
        scoreboard.setTitle(title.getDisplayText());
    }
}