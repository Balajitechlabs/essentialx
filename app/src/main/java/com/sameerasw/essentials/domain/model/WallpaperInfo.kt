/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Domain Layer Models
 * File: WallpaperInfo.kt
 * Description: Domain model representing live wallpaper metadata, resolution, author, and preview assets.
 */

package com.sameerasw.essentials.domain.model

data class WallpaperInfo(
    val id: String,
    val url: String,
    val urlMobile: String,
    val urlFull: String,
    val authorName: String,
    val authorUsername: String,
    val authorLink: String,
    val photoLink: String,
    val updatedAt: String
)
