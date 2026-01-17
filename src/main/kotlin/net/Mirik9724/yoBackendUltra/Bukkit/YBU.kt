package net.Mirik9724.yoBackendUltra.Bukkit

import net.Mirik9724.api.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import net.Mirik9724.yoBackendUltra.YBUCore.config

class YBU : JavaPlugin() {

    override fun onEnable() {
        if(config["metric"] == "true"){
            Metrics(this, 28879)
        }
    }

    override fun onDisable() {

    }
}