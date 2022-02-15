package com.ednow.monitor;


import com.ednow.monitor.listener.LoginListener;
import com.ednow.monitor.listener.LogoutListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Monitor extends JavaPlugin implements Listener {

    private final LoginListener loginListener = new LoginListener();
    private final LogoutListener logoutListener = new LogoutListener();

    @Override
    public void onEnable(){
        // 如果没有配置文件，创建一份
        saveDefaultConfig();
        // 如果插件没有被启用则，退出
        // 由于不知道相关配置信息，默认不启用
        if (!getConfig().getBoolean("enable")) {
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        // 将监听器注册到组件中
        getServer().getPluginManager().registerEvents(loginListener, this);
        getServer().getPluginManager().registerEvents(logoutListener, this);
    }

}