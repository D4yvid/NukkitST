package org.crimsonmc.network.binary

import lombok.extern.log4j.Log4j2
import org.crimsonmc.natives.ZlibNative
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater

@Log4j2
object ZlibCompression {

    private const val BUFFER_SIZE = 0x7FFF // 4K

    @JvmStatic
    fun deflate(data: ByteArray, level: Int): ByteArray {
        val deflater = Deflater(level)
        val buffer = ByteArray(BUFFER_SIZE)
        val output = ByteArrayOutputStream(data.size)

        deflater.setInput(data)
        deflater.finish()

        while (!deflater.finished()) {
            val count = deflater.deflate(buffer)

            output.write(buffer, 0, count)
        }

        deflater.end()

        return output.toByteArray()
    }

    @JvmStatic
    fun inflate(data: ByteArray): ByteArray {
        val inflater = Inflater()
        val buffer = ByteArray(BUFFER_SIZE)
        val output = ByteArrayOutputStream(data.size)

        inflater.setInput(data)

        while (!inflater.finished()) {
            val count = inflater.inflate(buffer)

            output.write(buffer, 0, count)
        }

        inflater.end()

        return output.toByteArray()
    }

    @JvmStatic
    fun inflate(data: ByteArray, maxSize: Int): ByteArray {
        val bytes = inflate(data)

        if (bytes.size > maxSize) {
            return data.sliceArray(IntRange(0, maxSize))
        }

        return bytes
    }
}
