/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Domain Layer
 * File: MeDropContact.kt
 */

package com.sameerasw.essentials.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class MeDropContact(
    val lookupKey: String,
    val displayName: String,
    val photoUri: String? = null,
    val nickname: String? = null,
    val birthday: String? = null,
    val pronouns: String? = null,
    val phones: List<String> = emptyList(),
    val emails: List<String> = emptyList(),
    val organization: String? = null,
    val department: String? = null,
    val jobTitle: String? = null,
    val role: String? = null,
    val addresses: List<String> = emptyList(),
    val urls: List<String> = emptyList(),
    val impps: List<String> = emptyList(),
    val socialProfiles: List<String> = emptyList(),
    val note: String? = null,
    val selectedEntryIds: Set<String>? = null
) {
    @Suppress("SENSELESS_COMPARISON")
    fun getSafePhones(): List<String> = if (phones != null) phones else emptyList()
    @Suppress("SENSELESS_COMPARISON")
    fun getSafeEmails(): List<String> = if (emails != null) emails else emptyList()
    @Suppress("SENSELESS_COMPARISON")
    fun getSafeAddresses(): List<String> = if (addresses != null) addresses else emptyList()
    @Suppress("SENSELESS_COMPARISON")
    fun getSafeUrls(): List<String> = if (urls != null) urls else emptyList()
    @Suppress("SENSELESS_COMPARISON")
    fun getSafeImpps(): List<String> = if (impps != null) impps else emptyList()
    @Suppress("SENSELESS_COMPARISON")
    fun getSafeSocialProfiles(): List<String> = if (socialProfiles != null) socialProfiles else emptyList()

    @Suppress("SENSELESS_COMPARISON")
    fun getActiveEntryIds(): Set<String> {
        if (selectedEntryIds != null) return selectedEntryIds
        val all = mutableSetOf<String>()
        if (!photoUri.isNullOrBlank()) all.add("photo")
        if (!nickname.isNullOrBlank()) all.add("nickname")
        if (!birthday.isNullOrBlank()) all.add("birthday")
        if (!pronouns.isNullOrBlank()) all.add("pronouns")
        getSafePhones().forEachIndexed { i, _ -> all.add("phone_$i") }
        getSafeEmails().forEachIndexed { i, _ -> all.add("email_$i") }
        if (!organization.isNullOrBlank()) all.add("organization")
        if (!department.isNullOrBlank()) all.add("department")
        if (!jobTitle.isNullOrBlank()) all.add("jobTitle")
        if (!role.isNullOrBlank()) all.add("role")
        getSafeAddresses().forEachIndexed { i, _ -> all.add("address_$i") }
        getSafeUrls().forEachIndexed { i, _ -> all.add("url_$i") }
        getSafeImpps().forEachIndexed { i, _ -> all.add("impp_$i") }
        getSafeSocialProfiles().forEachIndexed { i, _ -> all.add("social_$i") }
        if (!note.isNullOrBlank()) all.add("note")
        return all
    }

    fun isEntrySelected(id: String): Boolean = getActiveEntryIds().contains(id)

    fun toVCard(context: android.content.Context? = null): String {
        val active = getActiveEntryIds()
        val sb = StringBuilder()
        sb.appendLine("BEGIN:VCARD")
        sb.appendLine("VERSION:3.0")
        sb.appendLine("FN:$displayName")
        sb.appendLine("N:${buildNField(displayName)}")

        if (active.contains("nickname") && !nickname.isNullOrBlank()) {
            sb.appendLine("NICKNAME:$nickname")
        }

        if (active.contains("photo") && !photoUri.isNullOrBlank() && context != null) {
            try {
                val uri = android.net.Uri.parse(photoUri)
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val originalBitmap = android.graphics.BitmapFactory.decodeStream(stream)
                    if (originalBitmap != null) {
                        val maxDim = 112
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
                        val baos = java.io.ByteArrayOutputStream()
                        scaledBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 60, baos)
                        val bytes = baos.toByteArray()
                        if (bytes.isNotEmpty()) {
                            val base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
                            sb.appendLine("PHOTO;TYPE=JPEG;ENCODING=b:$base64")
                        }
                    }
                }
            } catch (_: Exception) {}
        }

        if (active.contains("birthday") && !birthday.isNullOrBlank()) {
            sb.appendLine("BDAY:$birthday")
        }

        if (active.contains("pronouns") && !pronouns.isNullOrBlank()) {
            sb.appendLine("PRONOUNS:$pronouns")
            sb.appendLine("X-PRONOUNS:$pronouns")
        }

        val hasOrg = active.contains("organization") && !organization.isNullOrBlank()
        val hasDept = active.contains("department") && !department.isNullOrBlank()
        if (hasOrg || hasDept) {
            val orgPart = if (hasOrg) organization else ""
            val deptPart = if (hasDept) ";$department" else ""
            sb.appendLine("ORG:$orgPart$deptPart")
        }
        if (active.contains("jobTitle") && !jobTitle.isNullOrBlank()) {
            sb.appendLine("TITLE:$jobTitle")
        }
        if (active.contains("role") && !role.isNullOrBlank()) {
            sb.appendLine("ROLE:$role")
        }

        getSafePhones().forEachIndexed { i, phone ->
            if (active.contains("phone_$i")) {
                sb.appendLine("TEL;TYPE=CELL:$phone")
            }
        }
        getSafeEmails().forEachIndexed { i, email ->
            if (active.contains("email_$i")) {
                sb.appendLine("EMAIL;TYPE=INTERNET:$email")
            }
        }
        getSafeAddresses().forEachIndexed { i, addr ->
            if (active.contains("address_$i")) {
                sb.appendLine("ADR;TYPE=HOME:;;${addr.replace("\n", ";")};;;")
            }
        }
        getSafeUrls().forEachIndexed { i, url ->
            if (active.contains("url_$i")) {
                sb.appendLine("URL:$url")
            }
        }
        getSafeImpps().forEachIndexed { i, impp ->
            if (active.contains("impp_$i")) {
                sb.appendLine("IMPP:$impp")
                sb.appendLine("X-IMPP:$impp")
            }
        }
        getSafeSocialProfiles().forEachIndexed { i, social ->
            if (active.contains("social_$i")) {
                sb.appendLine("X-SOCIALPROFILE:$social")
                sb.appendLine("URL:$social")
            }
        }
        if (active.contains("note") && !note.isNullOrBlank()) {
            sb.appendLine("NOTE:${note.replace("\n", " ")}")
        }

        val rev = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US).format(Date())
        sb.appendLine("REV:$rev")
        sb.append("END:VCARD")
        return sb.toString()
    }

    private fun buildNField(displayName: String): String {
        val parts = displayName.trim().split(" ")
        val last = if (parts.size > 1) parts.last() else ""
        val first = if (parts.size > 1) parts.dropLast(1).joinToString(" ") else displayName
        return "$last;$first;;;"
    }
}
