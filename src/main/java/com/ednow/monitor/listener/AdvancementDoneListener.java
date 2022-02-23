package com.ednow.monitor.listener;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ednow.monitor.Monitor;
import com.ednow.monitor.qqbot.HttpAdapter;
import com.ednow.utils.TextReader;
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
    public void onAdvancementDone(PlayerAdvancementDoneEvent event) {

//        读取翻译文件
        var string = this.getClass().getClassLoader().getResourceAsStream("chinese.json");
        var lang = (JSONObject) JSON.parse(TextReader.InputStreamToString(string));

//        如果不是成就那么跳过
        if (!event.getAdvancement().getKey().toString().split("/")[0].equals("minecraft:story")) {
            return;
        }

//        发送如下内容给qq群
//        <时间>
//        <用户>
//        因为 <原因>
//        获得成就  <成就>
        var advanceKey = event.getAdvancement().getKey().toString().split("/")[1];
        assert lang != null;
        String message = String.format(
                "%s\n%s\n因为 <%s>\n获得成就 <%s>",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                event.getPlayer().getDisplayName(),
                lang.getString("advancements.story." + advanceKey + ".description"),
                lang.getString("advancements.story." + advanceKey + ".title")
        );
        HttpAdapter.send(
                HttpAdapter.SendType.GROUP,
                JavaPlugin.getPlugin(Monitor.class).getConfig().getLong("tencentAccount.group"),
                message
        );
    }
}
