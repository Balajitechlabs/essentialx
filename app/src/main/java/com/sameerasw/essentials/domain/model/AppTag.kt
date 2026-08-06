package com.sameerasw.essentials.domain.model

data class AppTag(
    val id: String,
    val name: String,
    val colorHex: String,
    val iconName: String,
    val neverAutoFreeze: Boolean = false
)
