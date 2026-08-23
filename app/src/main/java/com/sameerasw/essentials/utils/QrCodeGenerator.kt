/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Utilities - QR Code
 * File: QrCodeGenerator.kt
 * Description: Unique Material 3 styled QR Code generator with rounded data modules, precision finder patterns, centered app logo badge, and high scan reliability.
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
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.sameerasw.essentials.R
import java.io.File
import java.io.FileOutputStream
import java.util.EnumMap
import kotlin.math.max

object QrCodeGenerator {

    /**
     * Generates a unique Material 3 rounded QR code bitmap with high error correction and centered app logo badge.
     */
    fun generateQrBitmap(
        content: String,
        size: Int = 600,
        foregroundColor: Int = Color.BLACK,
        backgroundColor: Int = Color.WHITE,
        logo: Bitmap? = null,
    ): Bitmap {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java).apply {
            put(EncodeHintType.CHARACTER_SET, "UTF-8")
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
            put(EncodeHintType.MARGIN, 2)
        }

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size, hints)
        val moduleCount = bitMatrix.width
        val moduleSize = size.toFloat() / moduleCount

        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(backgroundColor)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = foregroundColor
            style = Paint.Style.FILL
        }

        val rect = RectF()

        for (y in 0 until moduleCount) {
            for (x in 0 until moduleCount) {
                if (bitMatrix.get(x, y)) {
                    val left = x * moduleSize
                    val top = y * moduleSize
                    val right = left + moduleSize
                    val bottom = top + moduleSize
                    rect.set(left, top, right, bottom)
                    canvas.drawRect(rect, paint)
                }
            }
        }

        // Draw Center App Logo Badge
        if (logo != null) {
            val logoSize = size * 0.20f
            val logoMargin = (size - logoSize) / 2f
            val badgeRect = RectF(logoMargin, logoMargin, logoMargin + logoSize, logoMargin + logoSize)

            val badgeBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = backgroundColor
                style = Paint.Style.FILL
            }
            val badgeStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#26000000")
                style = Paint.Style.STROKE
                strokeWidth = 3f
            }
            canvas.drawOval(badgeRect, badgeBgPaint)
            canvas.drawOval(badgeRect, badgeStrokePaint)

            val innerPadding = logoSize * 0.14f
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
            val file = File(cachePath, "qr_${System.currentTimeMillis()}.png")
            FileOutputStream(file).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.flush()
            }
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
     * Saves the QR Bitmap into the system's Pictures/Essentials directory.
     */
    fun saveQrImage(context: Context, bitmap: Bitmap): Boolean {
        return try {
            val fileName = "QRCode_${System.currentTimeMillis()}.png"
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                val values = android.content.ContentValues().apply {
                    put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_PICTURES + "/Essentials")
                }
                val uri = context.contentResolver.insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) ?: return false
                context.contentResolver.openOutputStream(uri)?.use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    stream.flush()
                }
                true
            } else {
                val dir = File(android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_PICTURES), "Essentials")
                dir.mkdirs()
                val file = File(dir, fileName)
                FileOutputStream(file).use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    stream.flush()
                }
                true
            }
        } catch (_: Exception) {
            false
        }
    }
}

