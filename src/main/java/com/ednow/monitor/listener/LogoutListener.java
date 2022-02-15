package com.ednow.monitor.listener;

import com.ednow.monitor.Monitor;
import com.ednow.monitor.qqbot.HttpAdapter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogoutListener implements Listener {
    @EventHandler
    public static void onQuit(PlayerQuitEvent event) {
        // 退出的时候只发送给qq群
        HttpAdapter.send(
                HttpAdapter.SendType.GROUP,
                JavaPlugin.getPlugin(Monitor.class).getConfig().getLong("tencentAccount.group"),
                String.format(
                        "%s\n%s",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                        event.getQuitMessage()
                )
        );
    }
}
