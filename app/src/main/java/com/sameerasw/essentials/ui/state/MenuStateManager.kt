/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: UI Module
 * File: MenuStateManager.kt
 * Description: UI layout element for MenuStateManager.kt.
 */

package com.sameerasw.essentials.ui.state

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MenuStateManager {
    var activeId by mutableStateOf<Any?>(null)
}

val LocalMenuStateManager = compositionLocalOf { MenuStateManager() }
