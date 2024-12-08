package org.crimsonmc.text

import com.google.common.collect.Maps
import java.util.regex.Pattern

/**
 * All supported formatting values for chat and console.
 */
enum class TextFormat(
    /**
     * Gets the char value associated with this color
     *
     * @return A char value of this color code
     */
    val char: Char, private val intCode: Int,
    /**
     * Checks if this code is a format code as opposed to a color code.
     */
    val isFormat: Boolean = false
) {
    /**
     * Represents black.
     */
    BLACK('0', 0x00),

    /**
     * Represents dark blue.
     */
    DARK_BLUE('1', 0x1),

    /**
     * Represents dark green.
     */
    DARK_GREEN('2', 0x2),

    /**
     * Represents dark blue (aqua).
     */
    DARK_AQUA('3', 0x3),

    /**
     * Represents dark red.
     */
    DARK_RED('4', 0x4),

    /**
     * Represents dark purple.
     */
    DARK_PURPLE('5', 0x5),

    /**
     * Represents gold.
     */
    GOLD('6', 0x6),

    /**
     * Represents gray.
     */
    GRAY('7', 0x7),

    /**
     * Represents dark gray.
     */
    DARK_GRAY('8', 0x8),

    /**
     * Represents blue.
     */
    BLUE('9', 0x9),

    /**
     * Represents green.
     */
    GREEN('a', 0xA),

    /**
     * Represents aqua.
     */
    AQUA('b', 0xB),

    /**
     * Represents red.
     */
    RED('c', 0xC),

    /**
     * Represents light purple.
     */
    LIGHT_PURPLE('d', 0xD),

    /**
     * Represents yellow.
     */
    YELLOW('e', 0xE),

    /**
     * Represents white.
     */
    WHITE('f', 0xF),

    /**
     * Makes the text obfuscated. Not present in the game yet, it's here for future versions and for
     * compatibility with previous TextFormat version (ex-PM).
     */
    OBFUSCATED('k', 0x10, true),

    /**
     * Makes the text bold.
     */
    BOLD('l', 0x11, true),

    /**
     * Makes a line appear through the text.
     */
    STRIKETHROUGH('m', 0x12, true),

    /**
     * Makes the text appear underlined.
     */
    UNDERLINE('n', 0x13, true),

    /**
     * Makes the text italic.
     */
    ITALIC('o', 0x14, true),

    /**
     * Resets all previous chat colors or formats.
     */
    RESET('r', 0x15);

    override fun toString(): String {
        return "$ESCAPE$char"
    }

    val isColor: Boolean
        /**
         * Checks if this code is a color code as opposed to a format code.
         */
        get() = !isFormat && this != RESET

    companion object {
        /**
         * The special character which prefixes all format codes. Use this if you need to dynamically
         * convert format codes from your custom format.
         */
        const val ESCAPE: Char = '\u00A7'

        private val CLEAN_PATTERN: Pattern = Pattern.compile("(?i)$ESCAPE[0-9A-FK-OR]")

        private val BY_ID: MutableMap<Int, TextFormat> = Maps.newTreeMap()

        private val BY_CHAR: MutableMap<Char, TextFormat> = HashMap()

        init {
            for (color in entries) {
                BY_ID[color.intCode] = color
                BY_CHAR[color.char] = color
            }
        }

        /**
         * Gets the TextFormat represented by the specified format code.
         *
         * @param code Code to check
         * @return Associative [TextFormat] with the given code, or null if it doesn't exist
         */
        @JvmStatic
        fun getByChar(code: Char): TextFormat? {
            return BY_CHAR[code]
        }

        /**
         * Gets the TextFormat represented by the specified format code.
         *
         * @param code Code to check
         * @return Associative [TextFormat] with the given code, or null if it doesn't exist
         */
        @JvmStatic
        fun getByChar(code: String?): TextFormat? {
            if (code == null || code.length <= 1) {
                return null
            }

            return BY_CHAR[code[0]]
        }

        /**
         * Cleans the given message of all format codes.
         *
         * @param input String to clean.
         * @return A copy of the input string, without any formatting.
         */
        @JvmStatic
        fun clean(input: String?): String {
            if (input == null) {
                return ""
            }

            return CLEAN_PATTERN.matcher(input).replaceAll("")
        }

        /**
         * Translates a string using an alternate format code character into a string that uses the
         * internal TextFormat.ESCAPE format code character. The alternate format code character will only
         * be replaced if it is immediately followed by 0-9, A-F, a-f, K-O, k-o, R or r.
         *
         * @param altFormatChar   The alternate format code character to replace. Ex: &
         * @param textToTranslate Text containing the alternate format code character.
         * @return Text containing the TextFormat.ESCAPE format code character.
         */
        @JvmStatic
        fun colorize(altFormatChar: Char, textToTranslate: String): String {
            val b = textToTranslate.toCharArray()
            for (i in 0..<b.size - 1) {
                if (b[i] == altFormatChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                    b[i] = ESCAPE
                    b[i + 1] = b[i + 1].lowercaseChar()
                }
            }
            return String(b)
        }

        /**
         * Translates a string, using an ampersand (&) as an alternate format code character, into a
         * string that uses the internal TextFormat.ESCAPE format code character. The alternate format
         * code character will only be replaced if it is immediately followed by 0-9, A-F, a-f, K-O, k-o,
         * R or r.
         *
         * @param textToTranslate Text containing the alternate format code character.
         * @return Text containing the TextFormat.ESCAPE format code character.
         */
        @JvmStatic
        fun colorize(textToTranslate: String): String {
            return colorize('&', textToTranslate)
        }

        /**
         * Gets the chat color used at the end of the given input string.
         *
         * @param input Input string to retrieve the colors from.
         * @return Any remaining chat color to pass onto the next line.
         */
        @JvmStatic
        fun getLastColors(input: String): String {
            var result = ""
            val length = input.length

            // Search backwards from the end as it is faster
            for (index in length - 1 downTo -1 + 1) {
                val section = input[index]
                if (section == ESCAPE && index < length - 1) {
                    val c = input[index + 1]
                    val color = getByChar(c)

                    if (color != null) {
                        result = color.toString() + result

                        // Once we find a color or reset we can stop searching
                        if (color.isColor || color == RESET) {
                            break
                        }
                    }
                }
            }

            return result
        }
    }
}