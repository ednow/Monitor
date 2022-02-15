package com.ednow.monitor.qqbot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ednow.monitor.Monitor;
import com.ednow.monitor.http.HttpRequest;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 只支持通过http协议发送文字消息给个人或者群组
 */
public class HttpAdapter {
    private static final String verifyKey = JavaPlugin.getPlugin(Monitor.class).getConfig().getString("bot.verifyKey");
    private static final long qq = JavaPlugin.getPlugin(Monitor.class).getConfig().getLong("bot.account");
    private static final String url = JavaPlugin.getPlugin(Monitor.class).getConfig().getString("bot.url");

    public enum SendType {
        FRIEND("sendFriendMessage"), GROUP("sendGroupMessage");
        private final String address;

        /**
         * @param address 发送类型对应的api地址
         */
        SendType(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }
    }

    public static String uriBuilder(String address) {
        return url + "/" + address;
    }

    /**
     * @return 返回sessionKey
     */
    public static String getSessionKey() {
        JSONObject o = HttpRequest.sendJson(
                uriBuilder("verify"),
                JSON.toJSONString(new HashMap<String, String>(){{put("verifyKey", verifyKey);}}),
                "POST"
        );
        assert o != null;
        return o.get("session").toString();
    }

    /**
     * @param sessionKey 向qq机器人请求的sessionKey
     * @return 将sessionKey与qq机器人绑定
     */
    public static boolean verifySession(String sessionKey) {
        JSONObject o = HttpRequest.sendJson(
                uriBuilder("bind"),
                JSON.toJSONString(new HashMap<String, Object>(){{
                    put("sessionKey", sessionKey);
                    put("qq", qq);
                }}),
                "POST"
        );
        assert o != null;
        return (int) o.get("code") == 0;
    }

    /**
     * @param sendType FRIEND即发送给好友，GROUP发送给群
     * @param target qq号或者qq群号
     * @param text 需要发送的文字消息
     */
    public static void send(SendType sendType, Long target, String text) {
        String sessionKey = getSessionKey();

        // 验证session通过才发送消息
        if (!verifySession(sessionKey)) {
            return;
        }

        String requestUrl = uriBuilder(sendType.getAddress());

        Map<String, Object> map = new HashMap<>();
        map.put("sessionKey", sessionKey);
        map.put("target", target);
        List<HashMap<String, String>> messageChain = new ArrayList<>(){
            {
                add(new HashMap<>() {{
                        put("type", "Plain");
                        put("text", text);
                    }}
                );
            }
        };

        map.put("messageChain", messageChain);

        HttpRequest.sendJson(requestUrl,
                JSON.toJSONString(map),
                "POST"
        );

    }

}
