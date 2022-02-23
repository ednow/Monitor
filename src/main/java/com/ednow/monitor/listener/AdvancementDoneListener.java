package com.ednow.monitor.listener;


import com.ednow.monitor.Monitor;
import com.ednow.monitor.qqbot.HttpAdapter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 当玩家完成一个成就时触发
 */
public class AdvancementDoneListener implements Listener {
    /**
     * 当玩家获得成就时，向群里面发送消息
     * @param event 玩家获得的成就事件，
     *              https://bukkit.windit.net/javadoc/org/bukkit/event/player/PlayerAdvancementDoneEvent.html
     */
    @EventHandler
    public static void onAdvancementDone(PlayerAdvancementDoneEvent event) {
        String message = String.format(
                "%s\n%s %s",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                event.getPlayer(),
                event.getEventName()
        );
        HttpAdapter.send(
                HttpAdapter.SendType.GROUP,
                JavaPlugin.getPlugin(Monitor.class).getConfig().getLong("tencentAccount.admin"),
                message
        );
    }
}
