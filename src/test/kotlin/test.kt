import kotlin.test.Test
import net.Mirik9724.api.loadYmlFile

class Test(){
    @Test
    fun test(){
        val config = loadYmlFile("src/test/resources/config.yml")
        val serversMap = config.filterKeys { it.startsWith("servers.") }

        for ((key, address) in serversMap) {
            println("$key -> $address")
        }

        val allAddresses = serversMap.values.toList()
        println("Все адреса: $allAddresses")
    }
}