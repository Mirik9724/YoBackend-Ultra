package net.Mirik9724.yoBackendUltra.Velocity

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import net.Mirik9724.api.bstats.velocity.Metrics
import net.Mirik9724.api.copyFileFromJar
import net.Mirik9724.yoBackendUltra.BuildConstants.VERSION
import net.Mirik9724.yoBackendUltra.YBUCore
import net.Mirik9724.yoBackendUltra.YBUCore.conf
import net.Mirik9724.yoBackendUltra.YBUCore.config
import net.Mirik9724.yoBackendUltra.YBUCore.prKy
import net.Mirik9724.yoBackendUltra.YBUCore.pth
import java.io.File
import java.security.SecureRandom
import java.util.Base64
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
    private lateinit var backServers: Map<String, String>


    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        YBUCore
        if(config["metric"] == "true"){
            metricsFactory.make(this, 28880)
        }

        if(config["privateKey"].isNullOrBlank()){
            val prKyFl = File(pth + prKy)
            if (!prKyFl.exists()) {
                prKyFl.createNewFile()
            }
            val keyBytes = ByteArray(16).also { SecureRandom().nextBytes(it) }
            prKyFl.writeText(Base64.getUrlEncoder().withoutPadding().encodeToString(keyBytes))
        }
        backServers = server.allServers.associate { it.serverInfo.name to it.serverInfo.address.toString() }


        RegCommands(server)
    }
}