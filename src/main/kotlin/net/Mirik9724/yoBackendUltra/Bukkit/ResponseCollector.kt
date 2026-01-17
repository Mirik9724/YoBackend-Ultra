package net.Mirik9724.yoBackendUltra.Bukkit

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.permissions.PermissibleBase
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.permissions.PermissionAttachmentInfo
import org.bukkit.permissions.Permission
import org.bukkit.plugin.Plugin

class ResponseCollector : CommandSender {
    private val output = StringBuilder()
    private val permissible = PermissibleBase(this)

    override fun sendMessage(message: String) {
        output.append(message).append("\n")
    }

    override fun sendMessage(messages: Array<out String>) {
        messages.forEach { sendMessage(it) }
    }

    fun getOutput(): String = output.toString()

    override fun getName() = "YBU-Backend"
    override fun getServer() = Bukkit.getServer()
    override fun isOp() = true
    override fun setOp(value: Boolean) {}

    override fun isPermissionSet(name: String) = true
    override fun isPermissionSet(perm: Permission) = true
    override fun hasPermission(name: String) = true
    override fun hasPermission(perm: Permission) = true

    override fun addAttachment(plugin: Plugin): PermissionAttachment =
        permissible.addAttachment(plugin)

    override fun addAttachment(plugin: Plugin, name: String, value: Boolean): PermissionAttachment =
        permissible.addAttachment(plugin, name, value)

    override fun addAttachment(plugin: Plugin, name: String, value: Boolean, ticks: Int): PermissionAttachment =
        permissible.addAttachment(plugin, name, value, ticks)

    override fun addAttachment(plugin: Plugin, ticks: Int): PermissionAttachment =
        permissible.addAttachment(plugin, ticks)

    override fun removeAttachment(attachment: PermissionAttachment) =
        permissible.removeAttachment(attachment)

    override fun recalculatePermissions() =
        permissible.recalculatePermissions()

    override fun getEffectivePermissions(): Set<PermissionAttachmentInfo?>? =
        permissible.effectivePermissions
}

