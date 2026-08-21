/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: External
 * File: ExternalHandler.kt
 * Description: Component file for ExternalHandler.kt.
 */

package com.sameerasw.essentials.external

import android.content.Context
import android.database.Cursor
import android.os.Bundle

interface ExternalHandler {
    val path: String

    fun onQuery(
        context: Context,
        remainingPath: String,
        extras: Bundle?,
    ): Cursor?

    fun onUpdate(
        context: Context,
        remainingPath: String,
        value: String?,
        extras: Bundle?,
    ): Boolean

    fun onAction(
        context: Context,
        remainingPath: String,
        action: String?,
        extras: Bundle?,
    ): Bundle?
}
