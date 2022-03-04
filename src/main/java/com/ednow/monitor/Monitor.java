package com.ednow.monitor;


import com.ednow.monitor.httphandlers.CommandHandler;
import com.ednow.monitor.listener.AdvancementDoneListener;
import com.ednow.monitor.listener.LoginListener;
import com.ednow.monitor.listener.LogoutListener;
import com.sun.net.httpserver.HttpServer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Monitor extends JavaPlugin implements Listener {
    private static HttpServer httpServer = null;

    @Override
    public void onEnable() {

        // 如果没有配置文件，创建一份
        saveDefaultConfig();
        // 如果插件没有被启用则，退出
        // 由于不知道相关配置信息，默认不启用
        if (!getConfig().getBoolean("enable")) {
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        // 建立http服务器
        try {
            // 建立实例并绑定端口号
            httpServer = HttpServer.create(
                    new InetSocketAddress(getConfig().getInt("http.port")),
                    0
            );
            // 配置handlers
            httpServer.createContext("/command", new CommandHandler());

            //设置服务器的线程池对象
            httpServer.setExecutor(Executors.newFixedThreadPool(10));

            //启动服务器
            httpServer.start();

        } catch (IOException e) {
            getLogger().warning(e.getMessage());
        }

        // 将监听器注册到组件中
        getServer().getPluginManager().registerEvents(new LoginListener(), this);
        getServer().getPluginManager().registerEvents(new LogoutListener(), this);
        getServer().getPluginManager().registerEvents(new AdvancementDoneListener(), this);


    }

}
