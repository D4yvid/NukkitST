package org.crimsonmc.cli

import org.crimsonmc.cli.exception.NoOptionValueProvidedException


class ArgumentParser(
    val args: Array<String>,
    private val options: List<Option>,
) {

    fun parse() {
        for (i in 0..args.lastIndex) {
            val arg = args[i]

            if (!arg.startsWith('-') || arg == "--") {
                break
            }

            val longOption = arg.startsWith("--")
            val name = if (longOption) arg.substring(2) else arg.substring(1)

            val option = if (longOption) {
                options.firstOrNull { it.longName == name }
            } else {
                options.firstOrNull { it.shortName == name[0] }
            }

            if (option == null) {
                continue
            }

            if (option.requireValue && args.lastIndex == i) {
                throw NoOptionValueProvidedException(option)
            }

            val value =
                if (option.requireValue)
                    args[i + 1]
                else
                    null

            option.execute(value)
        }
    }

}