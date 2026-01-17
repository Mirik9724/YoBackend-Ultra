package net.Mirik9724.yoBackendUltra

import net.Mirik9724.api.isAvailableNewVersion
import net.Mirik9724.api.loadYmlFile
import net.Mirik9724.api.log
import net.Mirik9724.api.logInit
import net.Mirik9724.api.tryCreatePath
import net.Mirik9724.yoBackendUltra.BuildConstants.VERSION
import java.io.File

object YBUCore {
    lateinit var config: Map<String, String>
    init {
        logInit("YBU")

        tryCreatePath(File("plugins/YBU"))

        config = loadYmlFile("config.yml")

        if(config["checkUpdates"] == "true") {
            if(isAvailableNewVersion("", VERSION) == true){
                log.info("YBU new version is available")
            }
        }
        log.info("Core ON")
    }
}