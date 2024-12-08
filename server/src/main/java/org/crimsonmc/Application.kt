package org.crimsonmc

import org.crimsonmc.GlobalApplicationState.DATA_PATH
import org.crimsonmc.GlobalApplicationState.PATH
import org.crimsonmc.GlobalApplicationState.PLUGIN_PATH
import org.crimsonmc.cli.ArgumentParser
import org.crimsonmc.cli.Option
import org.crimsonmc.cli.exception.NoOptionValueProvidedException
import org.crimsonmc.logger.ServerLogger
import org.crimsonmc.platform.NativeLibraryLoader.loadLibrary
import org.crimsonmc.server.Server
import org.crimsonmc.thread.InterruptibleThread
import java.io.File
import kotlin.system.exitProcess

object GlobalApplicationState {

    const val VERSION: String = "0.1-development"
    const val API_VERSION: String = "1.0"
    const val CODENAME: String = "Crimson"
    const val MINECRAFT_VERSION: String = "v0.15.x"
    const val MINECRAFT_VERSION_NETWORK: String = "0.15.x"

    val PATH: String = System.getProperty("user.dir") + "/"
    val DATA_PATH: String = System.getProperty("user.dir") + "/"
    val PLUGIN_PATH: String = DATA_PATH + "plugins"

    @JvmField
    val START_TIME: Long = System.currentTimeMillis()

    @JvmField
    var DEBUG: Int = 1

}

fun main(args: Array<String>) {
    val loader = Thread.currentThread().contextClassLoader

    if (!loadLibrary(loader, "crimsonmc")) {
        println("Couldn't load libcrimsonmc for architecture " + System.getProperty("os.arch"))
        println("Please try compiling the JAR yourself to run CrimsonMC.")

        exitProcess(1)
    }

    var disableAnsi = false
    var logfilePath = DATA_PATH + "/server.log"

    val parser = ArgumentParser(
        args = args,
        options = listOf(
            Option(shortName = 'n', longName = "disable-ansi", requireValue = false) {
                disableAnsi = true
            },

            Option(shortName = 'l', longName = "logfile", requireValue = true) {
                logfilePath = it!!
            },

            Option(shortName = 'h', longName = "help", requireValue = false) {
                help(code = 0)
            },
        )
    )

    try {
        parser.parse()
    } catch (e: NoOptionValueProvidedException) {
        println(e.message)

        help(code = 1)
    }

    val logfile = File(logfilePath)

    if (!logfile.exists()) {
        assert(logfile.createNewFile())
    }

    val logger = ServerLogger(
        disableAnsiSupport = disableAnsi,
        logfile = logfile
    )

    try {
        Server(logger, PATH, DATA_PATH, PLUGIN_PATH)
    } catch (e: Exception) {
        logger.exception(e)
    }

    logger.info("Stopping other threads")

    for (thread in Thread.getAllStackTraces().keys) {
        if (thread !is InterruptibleThread) {
            continue
        }

        logger.debug("Stopping " + thread.javaClass.simpleName + " thread")

        if (thread.isAlive) {
            thread.interrupt()
        }
    }

    logger.info("Shutting down logger")

    logger.interrupt()

    exitProcess(0)
}

fun help(code: Int) {
    println("CrimsonMC API - ")

    exitProcess(code)
}
