package net.Mirik9724.yoBackendUltra.Velocity

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import net.Mirik9724.api.bstats.velocity.Metrics
import net.Mirik9724.yoBackendUltra.BuildConstants.VERSION
import net.Mirik9724.yoBackendUltra.YBUCore.config
import javax.inject.Inject

@Plugin(
    id = "yobackendultra",
    name = "YoBackend-Ultra",
    version = VERSION,
    authors = ["Mirik9724"],
    description = "The plugin allows you to send commands from Velocity to Paper servers",
    url = "https://github.com/Mirik9724/YoBackend-Ultra",
    dependencies = arrayOf(Dependency(id = "mirikapi"))
)
class YBU @Inject constructor(
    private val server: ProxyServer,
    private val metricsFactory: Metrics.Factory){

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        if(config["metric"] == "true"){
            metricsFactory.make(this, 28880)
        }
    }
}