/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Utilities - QR Code
 * File: QrCodeGenerator.kt
 * Description: Pure Kotlin high-performance QR Code matrix generator and bitmap renderer with rounded modules, center app logo badge, and high error correction.
 */

package com.sameerasw.essentials.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.sameerasw.essentials.R
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.min

object QrCodeGenerator {

    /**
     * Generates a rounded Material 3 QR code bitmap with optional center app logo badge.
     */
    fun generateQrBitmap(
        content: String,
        size: Int = 600,
        foregroundColor: Int = Color.BLACK,
        backgroundColor: Int = Color.WHITE,
        logo: Bitmap? = null,
    ): Bitmap {
        val qr = QrCode.encodeText(content, QrCode.Ecc.HIGH)
        val moduleCount = qr.size
        val border = 4
        val totalModules = moduleCount + border * 2
        val moduleSize = size.toFloat() / totalModules

        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(backgroundColor)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = foregroundColor
            style = Paint.Style.FILL
        }

        val cornerRadius = moduleSize * 0.35f
        val rect = RectF()

        for (y in 0 until moduleCount) {
            for (x in 0 until moduleCount) {
                if (qr.getModule(x, y)) {
                    val left = (x + border) * moduleSize
                    val top = (y + border) * moduleSize
                    val right = left + moduleSize
                    val bottom = top + moduleSize
                    rect.set(left, top, right, bottom)
                    canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
                }
            }
        }

        // Draw Center App Logo Badge
        if (logo != null) {
            val logoSize = size * 0.22f
            val logoMargin = (size - logoSize) / 2f
            val badgeRect = RectF(logoMargin, logoMargin, logoMargin + logoSize, logoMargin + logoSize)

            val badgeBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = backgroundColor
                style = Paint.Style.FILL
            }
            val badgeStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#33000000")
                style = Paint.Style.STROKE
                strokeWidth = 3f
            }
            val badgeRadius = logoSize * 0.28f
            canvas.drawRoundRect(badgeRect, badgeRadius, badgeRadius, badgeBgPaint)
            canvas.drawRoundRect(badgeRect, badgeRadius, badgeRadius, badgeStrokePaint)

            val innerPadding = logoSize * 0.12f
            val innerRect = RectF(
                badgeRect.left + innerPadding,
                badgeRect.top + innerPadding,
                badgeRect.right - innerPadding,
                badgeRect.bottom - innerPadding,
            )
            canvas.drawBitmap(logo, null, innerRect, Paint(Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG))
        }

        return bitmap
    }

    /**
     * Extracts the app logo as a Bitmap.
     */
    fun getAppLogoBitmap(context: Context): Bitmap? {
        return try {
            val drawable = ContextCompat.getDrawable(context, R.mipmap.ic_launcher) ?: return null
            val bitmap = Bitmap.createBitmap(
                max(1, drawable.intrinsicWidth),
                max(1, drawable.intrinsicHeight),
                Bitmap.Config.ARGB_8888,
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Saves QR Bitmap to cache and returns FileProvider content URI for sharing with image attachment.
     */
    fun getShareableQrUri(context: Context, bitmap: Bitmap): Uri? {
        return try {
            val cachePath = File(context.cacheDir, "shared_qr")
            cachePath.mkdirs()
            val file = File(cachePath, "link_qr_code.png")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file,
            )
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Compact QR Code encoder engine based on Nayuki standard QR specification.
     */
    private class QrCode private constructor(
        val version: Int,
        val size: Int,
        val errorCorrectionLevel: Ecc,
        private val modules: Array<BooleanArray>,
    ) {
        enum class Ecc(val formatBits: Int) {
            LOW(1),
            MEDIUM(0),
            QUARTILE(3),
            HIGH(2),
        }

        fun getModule(x: Int, y: Int): Boolean {
            return x in 0 until size && y in 0 until size && modules[y][x]
        }

        companion object {
            fun encodeText(text: String, ecl: Ecc): QrCode {
                val data = text.toByteArray(Charsets.UTF_8)
                val seg = QrSegment(QrSegment.Mode.BYTE, data.size, data)
                return encodeSegments(listOf(seg), ecl)
            }

            fun encodeSegments(segs: List<QrSegment>, ecl: Ecc): QrCode {
                for (version in 1..40) {
                    val dataCapacityBits = getNumDataCodewords(version, ecl) * 8
                    val dataUsedBits = getTotalBits(segs, version)
                    if (dataUsedBits <= dataCapacityBits) {
                        return encodeSegments(segs, ecl, version)
                    }
                }
                throw IllegalArgumentException("Data too long for QR code")
            }

            private fun encodeSegments(segs: List<QrSegment>, ecl: Ecc, version: Int): QrCode {
                val size = version * 4 + 17
                val modules = Array(size) { BooleanArray(size) }
                val isFunction = Array(size) { BooleanArray(size) }

                drawFinderPattern(0, 0, modules, isFunction)
                drawFinderPattern(size - 7, 0, modules, isFunction)
                drawFinderPattern(0, size - 7, modules, isFunction)
                drawTimingPatterns(size, modules, isFunction)
                drawAlignmentPatterns(version, size, modules, isFunction)

                val bitBuffer = mutableListOf<Int>()
                for (seg in segs) {
                    appendBits(seg.mode.modeBits, 4, bitBuffer)
                    appendBits(seg.numChars, seg.mode.numCharCountBits(version), bitBuffer)
                    for (b in seg.data) {
                        appendBits(b.toInt() and 0xFF, 8, bitBuffer)
                    }
                }

                val dataCapacityBits = getNumDataCodewords(version, ecl) * 8
                val terminatorBits = min(4, dataCapacityBits - bitBuffer.size)
                appendBits(0, terminatorBits, bitBuffer)
                while (bitBuffer.size % 8 != 0) bitBuffer.add(0)

                val padBytes = byteArrayOf(0xEC.toByte(), 0x11.toByte())
                var padIndex = 0
                while (bitBuffer.size < dataCapacityBits) {
                    appendBits(padBytes[padIndex % 2].toInt() and 0xFF, 8, bitBuffer)
                    padIndex++
                }

                val dataCodewords = ByteArray(bitBuffer.size / 8) { i ->
                    var b = 0
                    for (j in 0..7) b = (b shl 1) or bitBuffer[i * 8 + j]
                    b.toByte()
                }
                val allCodewords = addEccAndInterleave(dataCodewords, version, ecl)

                drawCodewords(allCodewords, size, modules, isFunction)
                val mask = 0
                applyMask(mask, size, modules, isFunction)
                drawFormatBits(ecl, mask, size, modules, isFunction)

                return QrCode(version, size, ecl, modules)
            }

            private fun drawFinderPattern(x: Int, y: Int, modules: Array<BooleanArray>, isFunction: Array<BooleanArray>) {
                for (dy in -1..7) {
                    for (dx in -1..7) {
                        val xx = x + dx
                        val yy = y + dy
                        if (xx in modules.indices && yy in modules.indices) {
                            val dist = maxOf(Math.abs(dx - 3), Math.abs(dy - 3))
                            modules[yy][xx] = (dist != 2 && dist != 4)
                            isFunction[yy][xx] = true
                        }
                    }
                }
            }

            private fun drawTimingPatterns(size: Int, modules: Array<BooleanArray>, isFunction: Array<BooleanArray>) {
                for (i in 0 until size) {
                    if (!isFunction[6][i]) {
                        modules[6][i] = (i % 2 == 0)
                        isFunction[6][i] = true
                    }
                    if (!isFunction[i][6]) {
                        modules[i][6] = (i % 2 == 0)
                        isFunction[i][6] = true
                    }
                }
            }

            private fun drawAlignmentPatterns(version: Int, size: Int, modules: Array<BooleanArray>, isFunction: Array<BooleanArray>) {
                if (version <= 1) return
                val pos = getAlignmentPatternPositions(version)
                for (y in pos) {
                    for (x in pos) {
                        if (isFunction[y][x]) continue
                        for (dy in -2..2) {
                            for (dx in -2..2) {
                                val dist = maxOf(Math.abs(dx), Math.abs(dy))
                                modules[y + dy][x + dx] = (dist != 1)
                                isFunction[y + dy][x + dx] = true
                            }
                        }
                    }
                }
            }

            private fun drawFormatBits(ecl: Ecc, mask: Int, size: Int, modules: Array<BooleanArray>, isFunction: Array<BooleanArray>) {
                val data = (ecl.formatBits shl 3) or mask
                var rem = data
                for (i in 0 until 10) rem = (rem shl 1) xor ((rem ushr 9) * 0x537)
                var bits = ((data shl 10) or rem) xor 0x5412

                for (i in 0..5) modules[8][i] = ((bits ushr i) and 1) != 0
                modules[8][7] = ((bits ushr 6) and 1) != 0
                modules[8][8] = ((bits ushr 7) and 1) != 0
                modules[7][8] = ((bits ushr 8) and 1) != 0
                for (i in 9..14) modules[14 - i][8] = ((bits ushr i) and 1) != 0

                for (i in 0..7) modules[size - 1 - i][8] = ((bits ushr i) and 1) != 0
                for (i in 8..14) modules[8][size - 15 + i] = ((bits ushr i) and 1) != 0
            }

            private fun drawCodewords(data: ByteArray, size: Int, modules: Array<BooleanArray>, isFunction: Array<BooleanArray>) {
                var bitIndex = 0
                var right = size - 1
                while (right > 0) {
                    if (right == 6) right--
                    for (vert in 0 until size) {
                        for (j in 0..1) {
                            val x = right - j
                            val upward = ((right + 1) and 2) == 0
                            val y = if (upward) size - 1 - vert else vert
                            if (!isFunction[y][x] && bitIndex < data.size * 8) {
                                modules[y][x] = ((data[bitIndex ushr 3].toInt() ushr (7 - (bitIndex and 7))) and 1) != 0
                                bitIndex++
                            }
                        }
                    }
                    right -= 2
                }
            }

            private fun applyMask(mask: Int, size: Int, modules: Array<BooleanArray>, isFunction: Array<BooleanArray>) {
                for (y in 0 until size) {
                    for (x in 0 until size) {
                        val invert = when (mask) {
                            0 -> (x + y) % 2 == 0
                            1 -> y % 2 == 0
                            2 -> x % 3 == 0
                            3 -> (x + y) % 3 == 0
                            4 -> (x / 3 + y / 2) % 2 == 0
                            5 -> ((x * y) % 2 + (x * y) % 3) == 0
                            6 -> ((x * y) % 2 + (x * y) % 3) % 2 == 0
                            7 -> ((x + y) % 2 + (x * y) % 3) % 2 == 0
                            else -> false
                        }
                        if (invert && !isFunction[y][x]) modules[y][x] = !modules[y][x]
                    }
                }
            }

            private fun addEccAndInterleave(data: ByteArray, version: Int, ecl: Ecc): ByteArray {
                val numBlocks = getNumBlocks(version, ecl)
                val blockEccLen = getEccCodewordsPerBlock(version, ecl)
                val rawCodewords = getNumRawDataModules(version) / 8
                val numShortBlocks = numBlocks - rawCodewords % numBlocks
                val shortBlockDataLen = rawCodewords / numBlocks - blockEccLen

                val blocks = Array(numBlocks) { ByteArray(0) }
                val rs = ReedSolomonGenerator(blockEccLen)
                var k = 0
                for (i in 0 until numBlocks) {
                    val blockLen = shortBlockDataLen + (if (i >= numShortBlocks) 1 else 0)
                    val blockData = data.copyOfRange(k, k + blockLen)
                    k += blockLen
                    val ecc = rs.getRemainder(blockData)
                    blocks[i] = blockData + ecc
                }

                val result = ByteArray(rawCodewords)
                var p = 0
                for (i in 0 until blocks[0].size) {
                    for (j in 0 until numBlocks) {
                        if (i < blocks[j].size) result[p++] = blocks[j][i]
                    }
                }
                return result
            }

            private fun appendBits(value: Int, count: Int, buffer: MutableList<Int>) {
                for (i in count - 1 downTo 0) buffer.add((value ushr i) and 1)
            }

            private fun getTotalBits(segs: List<QrSegment>, version: Int): Int {
                var total = 0
                for (seg in segs) {
                    total += 4 + seg.mode.numCharCountBits(version) + seg.data.size * 8
                }
                return total
            }

            private fun getNumDataCodewords(version: Int, ecl: Ecc): Int {
                val raw = getNumRawDataModules(version) / 8
                val ecc = getEccCodewordsPerBlock(version, ecl) * getNumBlocks(version, ecl)
                return raw - ecc
            }

            private fun getNumRawDataModules(version: Int): Int {
                var count = (16 * version + 128) * version + 64
                if (version >= 2) {
                    val numAlign = version / 7 + 2
                    count -= (25 * numAlign - 10) * numAlign - 55
                    if (version >= 7) count -= 36
                }
                return count
            }

            private fun getEccCodewordsPerBlock(version: Int, ecl: Ecc): Int {
                val table = intArrayOf(
                    7, 10, 15, 20, 10, 16, 26, 18, 15, 26, 18, 26, 20, 18, 26, 30,
                    26, 24, 30, 22, 18, 28, 28, 26, 30, 28, 26, 30, 28, 30, 30, 30,
                )
                val idx = ((version - 1) % 8) * 4 + ecl.ordinal
                return table[idx % table.size]
            }

            private fun getNumBlocks(version: Int, ecl: Ecc): Int {
                val table = intArrayOf(
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 2, 2, 4,
                    1, 2, 4, 4, 2, 4, 4, 4, 2, 4, 6, 5, 2, 4, 6, 6,
                )
                val idx = ((version - 1) % 8) * 4 + ecl.ordinal
                return table[idx % table.size]
            }

            private fun getAlignmentPatternPositions(version: Int): IntArray {
                if (version == 1) return intArrayOf()
                val num = version / 7 + 2
                val step = if (version == 32) 26 else (version * 4 + num * 2 + 1) / (num * 2 - 2) * 2
                val result = IntArray(num)
                result[0] = 6
                var pos = version * 4 + 10
                for (i in num - 1 downTo 1) {
                    result[i] = pos
                    pos -= step
                }
                return result
            }
        }
    }

    private class QrSegment(val mode: Mode, val numChars: Int, val data: ByteArray) {
        enum class Mode(val modeBits: Int) {
            NUMERIC(1),
            ALPHANUMERIC(2),
            BYTE(4),
            KANJI(8);

            fun numCharCountBits(version: Int): Int {
                val i = (version + 7) / 17
                return when (this) {
                    NUMERIC -> intArrayOf(10, 12, 14)[i]
                    ALPHANUMERIC -> intArrayOf(9, 11, 13)[i]
                    BYTE -> intArrayOf(8, 16, 16)[i]
                    KANJI -> intArrayOf(8, 10, 12)[i]
                }
            }
        }
    }

    private class ReedSolomonGenerator(val degree: Int) {
        private val coefficients = ByteArray(degree)

        init {
            var root = 1
            coefficients[degree - 1] = 1
            for (i in 0 until degree) {
                for (j in 0 until degree) {
                    val coef = (coefficients[j].toInt() and 0xFF)
                    coefficients[j] = (multiply(coef, root) xor (if (j + 1 < degree) (coefficients[j + 1].toInt() and 0xFF) else 0)).toByte()
                }
                root = multiply(root, 0x02)
            }
        }

        fun getRemainder(data: ByteArray): ByteArray {
            val result = ByteArray(degree)
            for (b in data) {
                val factor = (b.toInt() xor result[0].toInt()) and 0xFF
                System.arraycopy(result, 1, result, 0, degree - 1)
                result[degree - 1] = 0
                for (i in 0 until degree) {
                    result[i] = (result[i].toInt() xor multiply(coefficients[i].toInt() and 0xFF, factor)).toByte()
                }
            }
            return result
        }

        private fun multiply(x: Int, y: Int): Int {
            if (x == 0 || y == 0) return 0
            var z = 0
            var a = x
            var b = y
            while (b > 0) {
                if (b and 1 != 0) z = z xor a
                a = a shl 1
                if (a and 0x100 != 0) a = a xor 0x11D
                b = b ushr 1
            }
            return z
        }
    }
}
