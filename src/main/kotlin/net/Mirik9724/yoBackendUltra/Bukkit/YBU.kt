package net.Mirik9724.yoBackendUltra.Bukkit

import net.Mirik9724.api.bstats.bukkit.Metrics
import net.Mirik9724.api.copyFileFromJar
import net.Mirik9724.yoBackendUltra.AESCrypt
import net.Mirik9724.yoBackendUltra.YBUCore
import net.Mirik9724.yoBackendUltra.YBUCore.conf
import net.Mirik9724.yoBackendUltra.YBUCore.log
import net.Mirik9724.yoBackendUltra.YBUCore.privateKey
import net.Mirik9724.yoBackendUltra.YBUCore.pth
import org.bukkit.plugin.java.JavaPlugin
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class YBU : JavaPlugin() {
    private var serverSocket: ServerSocket? = null
    private val threadPool = Executors.newCachedThreadPool()
    private val clients = mutableListOf<Socket>()

    private fun handleClient(client: Socket) {
        synchronized(clients) { clients.add(client) }
        try {
            client.getInputStream().bufferedReader().forEachLine { line ->
                try {
                    val command = AESCrypt.de(line, privateKey)
                    server.scheduler.runTask(this@YBU) {
                        val collector = ResponseCollector()
                        server.dispatchCommand(collector, command)

                        val response = collector.getOutput()
                        val encryptedResponse = AESCrypt.en(response, privateKey)

                        client.getOutputStream().bufferedWriter().apply {
                            write(encryptedResponse)
                            newLine()
                            flush()
                        }
                    }
                } catch (e: Exception) {
                    log.warn(e.message ?: "Unknown error")
                }
            }
        } finally {
            synchronized(clients) { clients.remove(client) }
            client.close()
        }
    }


    override fun onEnable() {
        YBUCore
        if(config["metric"] == "true"){
            Metrics(this, 28879)
        }
        val port = (config["port"]?.toString() ?: "0").toInt()

        threadPool.submit {
            serverSocket = ServerSocket(port)
            while (!serverSocket!!.isClosed) {
                val client = serverSocket!!.accept()
                threadPool.submit { handleClient(client) }
            }
        }
    }

    override fun onDisable() {
        serverSocket?.close()
        synchronized(clients) {
            clients.forEach { it.close() }
            clients.clear()
        }
        threadPool.shutdownNow()
    }
}