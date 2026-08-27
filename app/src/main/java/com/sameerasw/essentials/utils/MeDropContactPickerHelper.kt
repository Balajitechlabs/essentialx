/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: NFC / Contact Picker
 * File: MeDropContactPickerHelper.kt
 */

package com.sameerasw.essentials.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import com.sameerasw.essentials.domain.model.MeDropContact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MeDropContactPickerHelper {

    fun buildPickIntent(): Intent =
        Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)

    suspend fun saveCompressedCustomPhoto(uri: Uri, context: Context): String? =
        withContext(Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val originalBitmap = android.graphics.BitmapFactory.decodeStream(stream) ?: return@withContext null
                    val maxDim = 240
                    val width = originalBitmap.width
                    val height = originalBitmap.height
                    val ratio = if (width > height) {
                        maxDim.toFloat() / width
                    } else {
                        maxDim.toFloat() / height
                    }
                    val scaledBitmap = if (ratio < 1.0f) {
                        android.graphics.Bitmap.createScaledBitmap(
                            originalBitmap,
                            (width * ratio).toInt().coerceAtLeast(1),
                            (height * ratio).toInt().coerceAtLeast(1),
                            true
                        )
                    } else {
                        originalBitmap
                    }

                    val photosDir = java.io.File(context.filesDir, "medrop")
                    if (!photosDir.exists()) photosDir.mkdirs()
                    val photoFile = java.io.File(photosDir, "custom_photo.jpg")
                    java.io.FileOutputStream(photoFile).use { out ->
                        scaledBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 70, out)
                    }
                    Uri.fromFile(photoFile).toString()
                }
            } catch (_: Exception) {
                null
            }
        }

    suspend fun processResult(uri: Uri, context: Context): MeDropContact? =
        withContext(Dispatchers.IO) {
            val projection = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            )
            val contactId: Long
            val lookupKey: String
            val displayName: String

            context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                if (!cursor.moveToFirst()) return@withContext null
                contactId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                lookupKey = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.LOOKUP_KEY)) ?: ""
                displayName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)) ?: ""
            } ?: return@withContext null

            val details = try {
                fetchContactDetails(context, contactId.toString())
            } catch (_: SecurityException) {
                ExtractedDetails()
            } catch (_: Exception) {
                ExtractedDetails()
            }

            MeDropContact(
                lookupKey = lookupKey,
                displayName = displayName,
                photoUri = null,
                phones = details.phones.distinct(),
                emails = details.emails.distinct(),
                organization = details.organization,
                jobTitle = details.jobTitle,
                addresses = details.addresses.distinct(),
                urls = details.urls.distinct(),
                note = details.note
            )
        }

    private data class ExtractedDetails(
        val phones: List<String> = emptyList(),
        val emails: List<String> = emptyList(),
        val organization: String? = null,
        val jobTitle: String? = null,
        val addresses: List<String> = emptyList(),
        val urls: List<String> = emptyList(),
        val note: String? = null
    )

    private fun fetchContactDetails(context: Context, contactId: String): ExtractedDetails {
        val phones = mutableListOf<String>()
        val emails = mutableListOf<String>()
        var organization: String? = null
        var jobTitle: String? = null
        val addresses = mutableListOf<String>()
        val urls = mutableListOf<String>()
        var note: String? = null

        context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.DATA1,
                ContactsContract.Data.DATA4
            ),
            "${ContactsContract.Data.CONTACT_ID} = ?",
            arrayOf(contactId),
            null
        )?.use { cursor ->
            val mimeIdx = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE)
            val data1Idx = cursor.getColumnIndex(ContactsContract.Data.DATA1)
            val data4Idx = cursor.getColumnIndex(ContactsContract.Data.DATA4)

            while (cursor.moveToNext()) {
                val mime = if (mimeIdx != -1) cursor.getString(mimeIdx) ?: continue else continue
                val data1 = if (data1Idx != -1) cursor.getString(data1Idx) ?: "" else ""
                val data4 = if (data4Idx != -1) cursor.getString(data4Idx) ?: "" else ""

                when (mime) {
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE -> if (data1.isNotBlank()) phones.add(data1)
                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE -> if (data1.isNotBlank()) emails.add(data1)
                    ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE -> {
                        if (data1.isNotBlank()) organization = data1
                        if (data4.isNotBlank()) jobTitle = data4
                    }
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE -> if (data1.isNotBlank()) addresses.add(data1)
                    ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE -> if (data1.isNotBlank()) urls.add(data1)
                    ContactsContract.CommonDataKinds.Website.URL -> if (data1.isNotBlank()) urls.add(data1)
                    ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE -> if (data1.isNotBlank()) note = data1
                }
            }
        }

        return ExtractedDetails(
            phones = phones,
            emails = emails,
            organization = organization,
            jobTitle = jobTitle,
            addresses = addresses,
            urls = urls,
            note = note
        )
    }
}
