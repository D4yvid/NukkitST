package org.crimsonmc.logger

import lombok.extern.log4j.Log4j2
import org.crimsonmc.text.TextFormat
import org.crimsonmc.util.Utils
import org.fusesource.jansi.Ansi
import org.fusesource.jansi.AnsiConsole
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

@Log4j2
class ServerLogger(
    disableAnsiSupport: Boolean,
    private val logfile: File
) : ThreadedLogger() {
    private val replacements = mapOf(
        TextFormat.BLACK to Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString(),
        TextFormat.DARK_BLUE to Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString(),
        TextFormat.DARK_GREEN to Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString(),
        TextFormat.DARK_AQUA to Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString(),
        TextFormat.DARK_RED to Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString(),
        TextFormat.DARK_PURPLE to Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString(),
        TextFormat.GOLD to Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString(),
        TextFormat.GRAY to Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString(),
        TextFormat.DARK_GRAY to Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString(),
        TextFormat.BLUE to Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString(),
        TextFormat.GREEN to Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString(),
        TextFormat.AQUA to Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString(),
        TextFormat.RED to Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString(),
        TextFormat.LIGHT_PURPLE to Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString(),
        TextFormat.YELLOW to Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString(),
        TextFormat.WHITE to Ansi.ansi().a(Ansi.Attribute.RESET).fgBrightDefault().bold().toString(),
        TextFormat.BOLD to Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString(),
        TextFormat.STRIKETHROUGH to Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString(),
        TextFormat.UNDERLINE to Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString(),
        TextFormat.ITALIC to Ansi.ansi().a(Ansi.Attribute.ITALIC).toString(),
        TextFormat.RESET to Ansi.ansi().a(Ansi.Attribute.RESET).toString()
    )

    private val colors = TextFormat.entries.toTypedArray()

    private var ansi = !disableAnsiSupport

    private var logStream: String = ""

    private var running: Boolean = false

    private var debugMessages: Boolean = false

    private var stdout = AnsiConsole.out()
    private var stderr = AnsiConsole.err()

    init {
        AnsiConsole.systemInstall()

        if (logger != null) {
            throw RuntimeException("MainLogger has been already created")
        }

        logger = this
    }

    companion object {
        private var logger: ServerLogger? = null

        @JvmStatic
        fun get(): ServerLogger {
            return logger ?: TODO()
        }
    }

    override fun emergency(message: String) = send("§cEMERGENCY $message")
    override fun alert(message: String)     = send("§cALERT $message")
    override fun critical(message: String)  = send("§4CRITICAL $message")
    override fun error(message: String)     = send("§cERROR $message")
    override fun warning(message: String)   = send("§eWARNING $message")
    override fun notice(message: String)    = send("§bNOTICE $message")
    override fun info(message: String)      = send("§fINFO $message")
    fun exception(e: Exception) = alert(Utils.getExceptionMessage(e))

    override fun debug(message: String) {
        if (this.debugMessages)
            send("§7DEBUG $message")
    }

    override fun emergency(message: String, t: Throwable) = emergency(
        """$message
        ${Utils.getExceptionMessage(t)}""".trimIndent())

    override fun alert(message: String, t: Throwable) = alert(
        """$message
        ${Utils.getExceptionMessage(t)}""".trimIndent())

    override fun critical(message: String, t: Throwable) = critical(
        """$message
        ${Utils.getExceptionMessage(t)}""".trimIndent())

    override fun error(message: String, t: Throwable) = error(
        """$message
        ${Utils.getExceptionMessage(t)}""".trimIndent())

    override fun warning(message: String, t: Throwable) = warning(
        """$message
        ${Utils.getExceptionMessage(t)}""".trimIndent())

    override fun notice(message: String, t: Throwable) = notice(
        """$message
        ${Utils.getExceptionMessage(t)}""".trimIndent())

    override fun info(message: String, t: Throwable) = info(
        """$message
        ${Utils.getExceptionMessage(t)}""".trimIndent())

    override fun debug(message: String, t: Throwable) = debug(
        """$message
        ${Utils.getExceptionMessage(t)}""".trimIndent())

    override fun log(level: LogLevel, message: String, t: Throwable) = log(
        level, """$message
        ${Utils.getExceptionMessage(t)}""".trimIndent())

    fun enableDebugMessages() {
        this.debugMessages = true
    }

    fun disableDebugMessages() {
        this.debugMessages = false
    }

    override fun log(level: LogLevel, message: String) {
        when (level) {
            LogLevel.EMERGENCY -> this.emergency(message)
            LogLevel.ALERT -> this.alert(message)
            LogLevel.CRITICAL -> this.critical(message)
            LogLevel.ERROR -> this.error(message)
            LogLevel.WARNING -> this.warning(message)
            LogLevel.NOTICE -> this.notice(message)
            LogLevel.INFO -> this.info(message)
            LogLevel.DEBUG -> this.debug(message)
        }
    }

    fun shutdown() {
        this.running = false
    }

    private fun send(message: String) {
        val now = Date()

        val outputMessage =
            TextFormat.AQUA.toString() + SimpleDateFormat("HH:mm:ss").format(now) + TextFormat.RESET + " " + message + TextFormat.RESET

        stdout.println(colorize(outputMessage))

        val str = SimpleDateFormat("y-M-d").format(now) + " " + outputMessage + "\r\n"

        this.logStream += str

        synchronized(this) {
            (this as Object).notify()
        }
    }

    private fun colorize(string: String): String {
        if (string.indexOf(TextFormat.ESCAPE) < 0) {
            return string
        }

        if (ansi) {
            var string = string

            for (color in colors) {
                string = if (replacements.containsKey(color)) {
                    string.replace(("(?i)$color").toRegex(), replacements[color]!!)
                } else {
                    string.replace(("(?i)$color").toRegex(), "")
                }
            }

            return string + Ansi.ansi().reset()
        }

        return TextFormat.clean(string)
    }

    override fun run() {
        this.running = true

        while (this.running) {
            while (logStream.length > 0) {
                val chunk = this.logStream
                this.logStream = ""
                try {
                    val writer = OutputStreamWriter(FileOutputStream(this.logfile, true), StandardCharsets.UTF_8)
                    writer.write(chunk)
                    writer.flush()
                    writer.close()
                } catch (e: Exception) {
                    this.exception(e)
                }
            }

            try {
                synchronized(this) {
                    (this as Object).wait(25000)
                }
            } catch (e: InterruptedException) {
                // igonre
            }
        }

        if (logStream.length > 0) {
            val chunk = this.logStream
            this.logStream = ""
            try {
                val writer = OutputStreamWriter(FileOutputStream(this.logfile, true), StandardCharsets.UTF_8)
                writer.write(chunk)
                writer.flush()
                writer.close()
            } catch (e: Exception) {
                this.exception(e)
            }
        }
    }
}
