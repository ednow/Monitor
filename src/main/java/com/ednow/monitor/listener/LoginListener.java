package com.ednow.monitor.listener;

import com.ednow.monitor.Monitor;
import com.ednow.monitor.qqbot.HttpAdapter;
import org.bukkit.BanList;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.bukkit.Bukkit.*;

/**
 * 当玩家进入服务器时触发
 */
public class LoginListener implements Listener {
    /**
     * 检查用户是不是在白名单里面，不是的话屏蔽这个用户的ip
     * @param event 用户请求登录的事件
     */
    @EventHandler
    public static void autoBlock(AsyncPlayerPreLoginEvent event) {
        // 如果没有启动自动ban的话忽略
        if (!JavaPlugin.getPlugin(Monitor.class).getConfig().getBoolean("autoBlockEnable")) {
            return;
        }
        var whitelist =  getWhitelistedPlayers();
        var o = whitelist.stream().filter(x -> x.getName().equals(event.getName())).findAny();
        // 不存在白名单中,屏蔽ip
        if (o.isEmpty()) {
            banIP(event.getAddress().getHostAddress());
        }

    }

    /**
     * @param event 用户请求登录的事件
     */
    @EventHandler
    public static void onLogin(AsyncPlayerPreLoginEvent event) {
        // 发送到qq群中的消息将隐藏ip和uuid
        // 发送到通知群的消息将发送
        // 登录时间
        // 昵称
        String messageToGroup = String.format(
                "%s\n%s join the game",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                event.getName()
        );
        HttpAdapter.send(
                HttpAdapter.SendType.GROUP,
                JavaPlugin.getPlugin(Monitor.class).getConfig().getLong("tencentAccount.group"),
                messageToGroup
        );

        // 发送到通知人的消息将发送如下四个信息
        // 登录时间
        // 昵称
        // ip地址
        // uuid
        // 登录信息
        String messageToPerson = String.format(
                "%s\nname:%s,\nip:%s,\nuuid:%s,\nmessage:%s\njoin the game",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                event.getName(),
                event.getAddress().getHostAddress(),
                event.getUniqueId(),
                event.getKickMessage()
        );
        HttpAdapter.send(
                HttpAdapter.SendType.FRIEND,
                JavaPlugin.getPlugin(Monitor.class).getConfig().getLong("tencentAccount.admin"),
                messageToPerson
        );
    }
}
