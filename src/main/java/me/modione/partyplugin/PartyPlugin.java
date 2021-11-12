package me.modione.partyplugin;

import me.modione.partyplugin.Utils.FileConfig;
import me.modione.partyplugin.commands.DropPartyCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class PartyPlugin extends JavaPlugin {
    public static PartyPlugin INSTANCE;
    public static String PREFIX = ChatColor.AQUA+"["+ChatColor.RED+"PARTY"+ChatColor.AQUA+"]"+ChatColor.DARK_BLUE;
    public String broadcast;
    public String playermsg;
    private FileConfig config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        saveDefaultConfig();
        config = new FileConfig(getDataFolder().toPath().resolve("config.yml").toString());
        broadcast = config.getString("broadcast-message");
        playermsg = config.getString("player-message");
        DropPartyCommand dropPartyCommand = new DropPartyCommand();
        Objects.requireNonNull(Bukkit.getPluginCommand("dropparty")).setExecutor(dropPartyCommand);
        Bukkit.getPluginManager().registerEvents(dropPartyCommand, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
