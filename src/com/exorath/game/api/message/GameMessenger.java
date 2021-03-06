package com.exorath.game.api.message;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import com.exorath.game.GameAPI;
import com.exorath.game.api.Game;
import com.exorath.game.api.player.GamePlayer;

/**
 * @author Nick Robson
 */
public class GameMessenger {

    /**
     * Sends a formatted message to all online players.
     * 
     * @param game
     *            The game sending the message.
     * @param path
     *            The format string path in the game's config.
     */
    public static void sendStructured(Game game, String path, Object... params) {
        for (GamePlayer player : GameAPI.getOnlinePlayers()) {
            GameMessenger.sendStructured(game, player, path, params);
        }
    }

    /**
     * Sends a formatted message to a player.
     * 
     * @param game
     *            The game sending the message.
     * @param player
     *            The player to send the message to.
     * @param path
     *            The format string path in the game's config.
     */
    public static void sendStructured(Game game, GamePlayer player, String path, Object... params) {
        FileConfiguration config = game.getConfig("messages");
        String message = config == null ? String.format(ChatColor.RED + "Missing message %s. Tell a developer!", path)
                : String.format(config.getString(path), params);
        player.sendMessage(message);
    }

    public static void sendInfo(Game game, String message) {
        for (GamePlayer player : GameAPI.getOnlinePlayers()) {
            GameMessenger.sendInfo(game, player, message);
        }
        GameAPI.printConsole("[Player Info] " + message);
    }

    public static void sendInfo(Game game, GamePlayer player, String message) {
        player.sendMessage(message);
    }

}
