package net.Mirik9724.yoBackendUltra.Velocity

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.ProxyServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.Mirik9724.yoBackendUltra.YBUCore.log
import net.Mirik9724.yoBackendUltra.AESCrypt
import net.Mirik9724.yoBackendUltra.YBUCore.config
import net.kyori.adventure.text.Component
import java.net.Socket

class RegCommands(server: ProxyServer) {
    private val serversMap: Map<String, String> =
        config.filterKeys { it.startsWith("servers.") }
            .mapKeys { it.key.removePrefix("servers.") }

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private suspend  fun sendCommandToBackend(
        address: String,
        command: String,
        privateKey: String
    ): String {
        val parts = address.split(":")
        if (parts.size != 2) return "ERR: invalid backend address '$address'"
        val host = parts[0]
        val port = parts[1].toIntOrNull() ?: return "ERR: invalid port in address '$address'"

        Socket(host, port).use { socket ->
            val writer = socket.getOutputStream().bufferedWriter()
            val reader = socket.getInputStream().bufferedReader()

            val encrypted = AESCrypt.en(command, privateKey)
            writer.write(encrypted)
            writer.newLine()
            writer.flush()

            val responseEncrypted = reader.readLine()
                ?: return "ERR: no response from backend"

            return AESCrypt.de(responseEncrypted, privateKey)
        }
    }


    init{
        serversMap.forEach { (name, address) ->

            val commandMeta = server.commandManager.metaBuilder(name).build()
            server.commandManager.register(
                commandMeta,
                SimpleCommand { invocation: SimpleCommand.Invocation ->
                    val source: CommandSource = invocation.source()
                    val args: Array<String> = invocation.arguments()

                    if (!source.hasPermission("ybu.send")) {
                        return@SimpleCommand
                    }

                    if (args.isEmpty()) {
                        source.sendMessage(Component.text("Enter command for server $name"))
                        return@SimpleCommand
                    }

                    val commandText = args.joinToString(" ")


                    coroutineScope.launch {
                        val response = try {
                            sendCommandToBackend(address, commandText, config["privateKey"] ?: "")
                        } catch (e: Exception) {
                            "ERR: ${e.message}"
                        }
                        source.sendMessage(Component.text("[$name] $response"))
                    }


                }
            )
            log.info("Registered command $name")
        }

    }
}

