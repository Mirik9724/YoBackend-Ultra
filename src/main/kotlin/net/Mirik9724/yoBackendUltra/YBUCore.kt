package net.Mirik9724.yoBackendUltra

import net.Mirik9724.api.copyFileFromJar
import net.Mirik9724.api.isAvailableNewVersion
import net.Mirik9724.api.loadYmlFile
import net.Mirik9724.api.logInit
import net.Mirik9724.api.tryCreatePath
import net.Mirik9724.api.updateYmlFromJar
import net.Mirik9724.yoBackendUltra.BuildConstants.VERSION
import org.slf4j.Logger
import java.io.File

object YBUCore {
    lateinit var config: Map<String, String>
    const val conf = "config.yml"
    const val prKy = "key.txt"
    const val pth = "plugins/YBU/"
    internal lateinit var privateKey: String
    lateinit var log: Logger

    init {
        log = logInit("YBU")

        tryCreatePath(File(pth))
        copyFileFromJar(conf, pth, this.javaClass.classLoader)
        config = loadYmlFile(pth+conf)
        updateYmlFromJar(conf, pth+config)

        if(config["checkUpdates"] == "true") {
            if(isAvailableNewVersion("https://raw.githubusercontent.com/Mirik9724/YoBackend-Ultra/refs/heads/master/V", VERSION) == true){
                log.info("YBU new version is available")
            }
        }

        privateKey = config["privateKey"] ?: ""

        log.info("Core ON")
    }
}