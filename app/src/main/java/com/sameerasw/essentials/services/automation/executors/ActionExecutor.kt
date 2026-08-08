/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Background Services & Receivers
 * File: ActionExecutor.kt
 * Description: Background service component for ActionExecutor.kt.
 */

package com.sameerasw.essentials.services.automation.executors

import android.content.Context
import com.sameerasw.essentials.domain.diy.Action

interface ActionExecutor {
    suspend fun execute(context: Context, action: Action)
}
