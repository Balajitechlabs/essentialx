/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: UI Module
 * File: FloatingWithFragment.kt
 * Description: UI layout for the Tools tab built entirely with Essentials core design components (RoundedCardContainer, EssentialsBottomSheet), featuring Floating Web Window, High-Contrast QR Code generator, Zero-Cost Expiring URL Shortener, and on-device History & Settings.
 */

package com.sameerasw.essentials.ui.components.linkActions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.core.cards.IconToggleItem
import com.sameerasw.essentials.ui.core.containers.RoundedCardContainer
import com.sameerasw.essentials.ui.core.pickers.SegmentedPicker
import com.sameerasw.essentials.ui.core.sheets.EssentialsBottomSheet
import com.sameerasw.essentials.utils.HapticUtil
import com.sameerasw.essentials.utils.PermissionUtils
import com.sameerasw.essentials.utils.QrCodeGenerator
import com.sameerasw.essentials.utils.UrlShortener
import com.sameerasw.essentials.utils.WindowingUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FloatingWithContent(
    uri: Uri,
    onSelectTab: (Int) -> Unit = {},
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
    openShortenInitially: Boolean = false,
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    var showQrSheet by remember { mutableStateOf(false) }
    var qrContentUri by remember { mutableStateOf(uri.toString()) }

    var showShortenSheet by remember { mutableStateOf(openShortenInitially) }
    var showSettingsSheet by remember { mutableStateOf(false) }
    var selectedShortenTab by remember { mutableIntStateOf(0) } // 0 = Shorten, 1 = History
    var urlToShortenInput by remember { mutableStateOf(uri.toString()) }
    var selectedExpiration by remember { mutableStateOf(UrlShortener.getDefaultExpiration(context)) }
    var customAliasInput by remember { mutableStateOf("") }
    var isCustomAliasExpanded by remember { mutableStateOf(false) }
    var passcodeProtectionInput by remember { mutableStateOf("") }
    var isPasscodeProtectionExpanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var shortenedUrlResult by remember { mutableStateOf<String?>(null) }
    var isShortening by remember { mutableStateOf(false) }
    var historyList by remember { mutableStateOf<List<UrlShortener.ShortLinkHistoryItem>>(emptyList()) }

    var customDomainInput by remember { mutableStateOf(UrlShortener.getCustomDomain(context)) }
    var autoCopySetting by remember { mutableStateOf(UrlShortener.isAutoCopyEnabled(context)) }
    var autoStripSetting by remember { mutableStateOf(UrlShortener.isAutoStripTrackingEnabled(context)) }
    var notificationSetting by remember { mutableStateOf(UrlShortener.isNotificationEnabled(context)) }
    var currentTimeMs by remember { mutableStateOf(System.currentTimeMillis()) }

    val isFloatingSupported = remember { WindowingUtils.isFloatingModeSupported(context) }

    LaunchedEffect(showShortenSheet) {
        if (showShortenSheet) {
            historyList = UrlShortener.getHistory(context)
            selectedExpiration = UrlShortener.getDefaultExpiration(context)
            urlToShortenInput = uri.toString()
        }
    }

    // Live 1-second ticker for real-time seconds countdown in History sub-tab
    LaunchedEffect(showShortenSheet, selectedShortenTab) {
        if (showShortenSheet && selectedShortenTab == 1) {
            while (true) {
                currentTimeMs = System.currentTimeMillis()
                kotlinx.coroutines.delay(1000L)
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        RoundedCardContainer(
            modifier = Modifier.fillMaxWidth(),
        ) {
            // 1. Preview Web (Only shown when floating mode is supported)
            if (isFloatingSupported) {
                val bubblesEnabled = WindowingUtils.areNotificationBubblesEnabled(context)
                val canWriteSecure = PermissionUtils.canWriteSecureSettings(context)
                val previewEnabled = bubblesEnabled || canWriteSecure
                val previewDescription = when {
                    bubblesEnabled -> stringResource(R.string.preview_web_desc)
                    canWriteSecure -> stringResource(R.string.preview_web_desc_enable_bubbles)
                    else -> stringResource(R.string.preview_web_desc_disabled)
                }

                IconToggleItem(
                    title = stringResource(R.string.preview_web_title),
                    description = previewDescription,
                    iconRes = R.drawable.rounded_open_in_browser_24,
                    showToggle = false,
                    enabled = previewEnabled,
                    onDisabledClick = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.preview_web_desc_disabled),
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                    onClick = {
                        WindowingUtils.launchOverlayWindow(context, uri, isPrivate = true)
                        onFinish()
                    },
                )
            }

            // 2. Instant QR Code Generator
            IconToggleItem(
                title = stringResource(R.string.qr_code_title),
                description = stringResource(R.string.qr_code_desc),
                iconRes = R.drawable.rounded_devices_24,
                showToggle = false,
                onClick = {
                    qrContentUri = uri.toString()
                    showQrSheet = true
                },
            )

            // 3. Expiring URL Shortener
            IconToggleItem(
                title = stringResource(R.string.shorten_url_title),
                description = stringResource(R.string.shorten_url_desc),
                iconRes = R.drawable.rounded_link_24,
                showToggle = false,
                onClick = {
                    shortenedUrlResult = null
                    selectedShortenTab = 0
                    showShortenSheet = true
                },
            )
        }
    }

    // Shorten URL Bottom Sheet (with Sub-Tabs: Shorten & History)
    if (showShortenSheet) {
        EssentialsBottomSheet(
            onDismissRequest = { showShortenSheet = false },
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Top Header Row with Segmented Sub-Tabs and Settings Gear (Matches LinkPicker Tab 1/2/3 design)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    RoundedCardContainer(
                        modifier = Modifier.weight(1f),
                    ) {
                        SegmentedPicker(
                            items = listOf(0, 1),
                            selectedItem = selectedShortenTab,
                            onItemSelected = {
                                selectedShortenTab = it
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            },
                            labelProvider = {
                                when (it) {
                                    0 -> context.getString(R.string.shorten_tab_create)
                                    else -> "${context.getString(R.string.shorten_tab_history)} (${historyList.size})"
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    // Settings Button
                    FilledTonalIconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            showSettingsSheet = true
                        },
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(14.dp),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.rounded_settings_24),
                            contentDescription = "Settings",
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }

                // Sub-Tab 0: Create Short Link
                if (selectedShortenTab == 0) {
                    // Target URL Input Card with 📋 Paste & Clear buttons
                    RoundedCardContainer(modifier = Modifier.fillMaxWidth().animateContentSize()) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.rounded_link_24),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                    Text(
                                        text = stringResource(R.string.shorten_target_url_label),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    if (urlToShortenInput.isNotBlank()) {
                                        FilledTonalIconButton(
                                            onClick = {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                urlToShortenInput = ""
                                            },
                                            modifier = Modifier.size(28.dp),
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.rounded_cancel_24),
                                                contentDescription = "Clear",
                                                modifier = Modifier.size(14.dp),
                                            )
                                        }
                                    }

                                    FilledTonalButton(
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            try {
                                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                val clip = clipboard.primaryClip?.getItemAt(0)?.text?.toString()?.trim()
                                                if (!clip.isNullOrBlank()) {
                                                    urlToShortenInput = clip
                                                } else {
                                                    Toast.makeText(context, context.getString(R.string.shorten_qs_no_url_clipboard), Toast.LENGTH_SHORT).show()
                                                }
                                            } catch (_: Exception) {}
                                        },
                                        modifier = Modifier.height(28.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                        shape = RoundedCornerShape(8.dp),
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.rounded_content_paste_24),
                                            contentDescription = null,
                                            modifier = Modifier.size(12.dp),
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = stringResource(R.string.shorten_paste_button),
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = urlToShortenInput,
                                onValueChange = { input ->
                                    errorMessage = null
                                    urlToShortenInput = input
                                },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("https://example.com/long-url...") },
                                shape = RoundedCornerShape(12.dp),
                            )
                        }
                    }

                    // Expiration selector chips inside RoundedCardContainer
                    RoundedCardContainer(modifier = Modifier.fillMaxWidth().animateContentSize()) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.shorten_expiry_label),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                UrlShortener.Expiration.entries.forEach { exp ->
                                    FilterChip(
                                        selected = selectedExpiration == exp,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            selectedExpiration = exp
                                        },
                                        label = { Text(exp.label) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        ),
                                    )
                                }
                            }
                        }
                    }

                    // Custom Alias Field (Expandable & Animated)
                    RoundedCardContainer(modifier = Modifier.fillMaxWidth().animateContentSize()) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    isCustomAliasExpanded = !isCustomAliasExpanded
                                },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.rounded_edit_24),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                    Text(
                                        text = stringResource(R.string.shorten_custom_alias_label),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                }
                                Icon(
                                    painter = painterResource(
                                        id = if (isCustomAliasExpanded) R.drawable.rounded_cancel_24 else R.drawable.rounded_edit_24
                                    ),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp),
                                )
                            }

                            AnimatedVisibility(
                                visible = isCustomAliasExpanded,
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut(),
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    OutlinedTextField(
                                        value = customAliasInput,
                                        onValueChange = { input ->
                                            errorMessage = null
                                            if (input.length <= 32) {
                                                customAliasInput = input.filter { it.isLetterOrDigit() || it == '-' || it == '_' }
                                            }
                                        },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth(),
                                        placeholder = { Text(stringResource(R.string.shorten_custom_alias_hint)) },
                                        supportingText = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End,
                                            ) {
                                                Text(
                                                    text = "${customAliasInput.length} / 32",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                )
                                            }
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                    )
                                }
                            }
                        }
                    }

                    // Optional Passcode / PIN Protection Card
                    RoundedCardContainer(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().clickable {
                                    isPasscodeProtectionExpanded = !isPasscodeProtectionExpanded
                                    if (!isPasscodeProtectionExpanded) {
                                        passcodeProtectionInput = ""
                                    }
                                },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.rounded_lock_24),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp),
                                    )
                                    Text(
                                        text = stringResource(R.string.shorten_passcode_label),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                }
                                Icon(
                                    painter = painterResource(
                                        id = if (isPasscodeProtectionExpanded) R.drawable.rounded_cancel_24 else R.drawable.rounded_edit_24
                                    ),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp),
                                    )
                            }

                            AnimatedVisibility(
                                visible = isPasscodeProtectionExpanded,
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut(),
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    OutlinedTextField(
                                        value = passcodeProtectionInput,
                                        onValueChange = { input ->
                                            errorMessage = null
                                            if (input.length <= 16) {
                                                passcodeProtectionInput = input
                                            }
                                        },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth(),
                                        placeholder = { Text(stringResource(R.string.shorten_passcode_hint)) },
                                        supportingText = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End,
                                            ) {
                                                Text(
                                                    text = "${passcodeProtectionInput.length} / 16",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                )
                                            }
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                    )
                                }
                            }
                        }
                    }

                    // Error Message Banner (Animated)
                    AnimatedVisibility(
                        visible = errorMessage != null,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut(),
                    ) {
                        errorMessage?.let { error ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.errorContainer,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(
                                    text = error,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }

                    // Shorten Action Button with M3 Loading Indicator
                    ElevatedButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isShortening = true
                            errorMessage = null
                            scope.launch {
                                try {
                                    val result = UrlShortener.shortenUrl(
                                        url = urlToShortenInput.trim().ifBlank { uri.toString() },
                                        expiration = selectedExpiration,
                                        customSlug = customAliasInput.trim().takeIf { it.isNotBlank() },
                                        passcode = passcodeProtectionInput.trim().takeIf { it.isNotBlank() },
                                        context = context,
                                    )
                                    shortenedUrlResult = result
                                    historyList = UrlShortener.getHistory(context)

                                    if (UrlShortener.isAutoCopyEnabled(context)) {
                                        try {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("Shortened Link", result)
                                            clipboard.setPrimaryClip(clip)
                                            Toast.makeText(context, context.getString(R.string.action_copy_clipboard), Toast.LENGTH_SHORT).show()
                                        } catch (_: Exception) {
                                            // Ignore background clipboard exceptions on Android 10+
                                        }
                                    }
                                } catch (e: UrlShortener.ShortenException.LockdownException) {
                                    haptic.performHapticFeedback(HapticFeedbackType.Reject)
                                    errorMessage = context.getString(R.string.shorten_error_lockdown)
                                } catch (e: UrlShortener.ShortenException.ReservedAliasException) {
                                    haptic.performHapticFeedback(HapticFeedbackType.Reject)
                                    errorMessage = context.getString(R.string.shorten_error_reserved_alias)
                                } catch (e: UrlShortener.ShortenException.SlugConflictException) {
                                    haptic.performHapticFeedback(HapticFeedbackType.Reject)
                                    errorMessage = context.getString(R.string.shorten_error_slug_conflict)
                                } catch (e: UrlShortener.ShortenException.InvalidUrlException) {
                                    haptic.performHapticFeedback(HapticFeedbackType.Reject)
                                    errorMessage = context.getString(R.string.shorten_error_invalid_url)
                                } catch (e: UrlShortener.ShortenException.InvalidAliasException) {
                                    haptic.performHapticFeedback(HapticFeedbackType.Reject)
                                    errorMessage = context.getString(R.string.shorten_error_invalid_alias)
                                } catch (e: UrlShortener.ShortenException.NetworkException) {
                                    haptic.performHapticFeedback(HapticFeedbackType.Reject)
                                    errorMessage = context.getString(R.string.shorten_error_network)
                                } catch (e: Exception) {
                                    haptic.performHapticFeedback(HapticFeedbackType.Reject)
                                    errorMessage = e.message ?: context.getString(R.string.shorten_error_generic)
                                } finally {
                                    isShortening = false
                                }
                            }
                        },
                        enabled = !isShortening,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    ) {
                        if (isShortening) {
                            LoadingIndicator(
                                modifier = Modifier.size(22.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = stringResource(R.string.shorten_generating),
                                fontWeight = FontWeight.SemiBold,
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.rounded_link_24),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.shorten_url_title),
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }

                    // Shortened URL Result Card
                    AnimatedVisibility(
                        visible = shortenedUrlResult != null,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut(),
                    ) {
                        shortenedUrlResult?.let { shortUrl ->
                            RoundedCardContainer(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                ) {
                                    Text(
                                        text = stringResource(R.string.shorten_success_label),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                    )

                                    Text(
                                        text = shortUrl,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                        ),
                                        color = MaterialTheme.colorScheme.primary,
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    ) {
                                        FilledTonalButton(
                                            onClick = {
                                                try {
                                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                    val clip = ClipData.newPlainText("Shortened Link", shortUrl)
                                                    clipboard.setPrimaryClip(clip)
                                                    Toast.makeText(context, context.getString(R.string.action_copy_clipboard), Toast.LENGTH_SHORT).show()
                                                } catch (_: Exception) {
                                                    Toast.makeText(context, context.getString(R.string.error_could_not_copy), Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            modifier = Modifier.weight(1f).height(36.dp),
                                            shape = RoundedCornerShape(10.dp),
                                        ) {
                                            Text(stringResource(R.string.shorten_copy_link), style = MaterialTheme.typography.labelSmall)
                                        }

                                        FilledTonalButton(
                                            onClick = {
                                                try {
                                                    val sendIntent = Intent().apply {
                                                        action = Intent.ACTION_SEND
                                                        putExtra(Intent.EXTRA_TEXT, shortUrl)
                                                        type = "text/plain"
                                                    }
                                                    context.startActivity(Intent.createChooser(sendIntent, null))
                                                } catch (_: Exception) {
                                                    Toast.makeText(context, context.getString(R.string.error_no_share_app), Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            modifier = Modifier.weight(1f).height(36.dp),
                                            shape = RoundedCornerShape(10.dp),
                                        ) {
                                            Text(stringResource(R.string.shorten_share_link), style = MaterialTheme.typography.labelSmall)
                                        }

                                        FilledTonalButton(
                                            onClick = {
                                                qrContentUri = shortUrl
                                                showShortenSheet = false
                                                showQrSheet = true
                                            },
                                            modifier = Modifier.weight(1f).height(36.dp),
                                            shape = RoundedCornerShape(10.dp),
                                        ) {
                                            Text(stringResource(R.string.shorten_qr_link), style = MaterialTheme.typography.labelSmall)
                                        }

                                        FilledTonalButton(
                                            onClick = {
                                                try {
                                                    val inspectUrl = "$shortUrl+"
                                                    if (isFloatingSupported) {
                                                        WindowingUtils.launchOverlayWindow(context, Uri.parse(inspectUrl), isPrivate = true)
                                                    } else {
                                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(inspectUrl)).apply {
                                                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                        }
                                                        context.startActivity(intent)
                                                    }
                                                } catch (_: Exception) {
                                                    Toast.makeText(context, context.getString(R.string.error_open_link_inspector), Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            modifier = Modifier.weight(1f).height(36.dp),
                                            shape = RoundedCornerShape(10.dp),
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.rounded_visibility_24),
                                                contentDescription = null,
                                                modifier = Modifier.size(14.dp),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Sub-Tab 1: History of Shortened Links
                if (selectedShortenTab == 1) {
                    if (historyList.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Box(
                                modifier = Modifier.size(56.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceBright),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.rounded_history_24),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(28.dp),
                                )
                            }
                            Text(
                                text = stringResource(R.string.shorten_history_empty_title),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = stringResource(R.string.shorten_history_empty_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp),
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "${historyList.size} Saved Links",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )

                            TextButton(
                                onClick = {
                                    UrlShortener.clearHistory(context)
                                    historyList = emptyList()
                                },
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.rounded_delete_24),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(R.string.shorten_clear_history), style = MaterialTheme.typography.labelSmall)
                            }
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth().heightIn(max = 340.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            items(historyList, key = { it.id }) { item ->
                                RoundedCardContainer(modifier = Modifier.fillMaxWidth()) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            ) {
                                                // Status Badge
                                                val isExpired = item.isExpired(currentTimeMs)
                                                val remainingLabel = item.getRemainingTimeLabel(currentTimeMs)

                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = if (isExpired) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
                                                ) {
                                                    Text(
                                                        text = remainingLabel,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = if (isExpired) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    )
                                                }

                                                if (item.isPasswordProtected) {
                                                    Surface(
                                                        shape = RoundedCornerShape(6.dp),
                                                        color = MaterialTheme.colorScheme.secondaryContainer,
                                                    ) {
                                                        Text(
                                                            text = "🔒 PIN",
                                                            style = MaterialTheme.typography.labelSmall,
                                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                        )
                                                    }
                                                }
                                            }

                                            // Delete icon button
                                            FilledTonalIconButton(
                                                onClick = {
                                                    UrlShortener.deleteFromHistory(context, item.id)
                                                    historyList = UrlShortener.getHistory(context)
                                                },
                                                modifier = Modifier.size(28.dp),
                                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                                ),
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.rounded_delete_24),
                                                    contentDescription = "Delete",
                                                    modifier = Modifier.size(14.dp),
                                                )
                                            }
                                        }

                                        Text(
                                            text = item.shortUrl,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Monospace,
                                            ),
                                            color = MaterialTheme.colorScheme.primary,
                                        )

                                        Text(
                                            text = item.originalUrl,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        ) {
                                            FilledTonalButton(
                                                onClick = {
                                                    try {
                                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                        val clip = ClipData.newPlainText("Shortened Link", item.shortUrl)
                                                        clipboard.setPrimaryClip(clip)
                                                        Toast.makeText(context, context.getString(R.string.action_copy_clipboard), Toast.LENGTH_SHORT).show()
                                                    } catch (_: Exception) {
                                                        Toast.makeText(context, context.getString(R.string.error_could_not_copy), Toast.LENGTH_SHORT).show()
                                                    }
                                                },
                                                modifier = Modifier.weight(1f).height(36.dp),
                                                shape = RoundedCornerShape(10.dp),
                                            ) {
                                                Text(stringResource(R.string.shorten_copy_link), style = MaterialTheme.typography.labelSmall)
                                            }

                                            FilledTonalButton(
                                                onClick = {
                                                    try {
                                                        val sendIntent = Intent().apply {
                                                            action = Intent.ACTION_SEND
                                                            putExtra(Intent.EXTRA_TEXT, item.shortUrl)
                                                            type = "text/plain"
                                                        }
                                                        context.startActivity(Intent.createChooser(sendIntent, null))
                                                    } catch (_: Exception) {
                                                        Toast.makeText(context, context.getString(R.string.error_no_share_app), Toast.LENGTH_SHORT).show()
                                                    }
                                                },
                                                modifier = Modifier.weight(1f).height(36.dp),
                                                shape = RoundedCornerShape(10.dp),
                                            ) {
                                                Text(stringResource(R.string.shorten_share_link), style = MaterialTheme.typography.labelSmall)
                                            }

                                            FilledTonalButton(
                                                onClick = {
                                                    qrContentUri = item.shortUrl
                                                    showShortenSheet = false
                                                    showQrSheet = true
                                                },
                                                modifier = Modifier.weight(1f).height(36.dp),
                                                shape = RoundedCornerShape(10.dp),
                                            ) {
                                                Text(stringResource(R.string.shorten_qr_link), style = MaterialTheme.typography.labelSmall)
                                            }

                                            FilledTonalButton(
                                                onClick = {
                                                    try {
                                                        val inspectUrl = "${item.shortUrl}+"
                                                        if (isFloatingSupported) {
                                                            WindowingUtils.launchOverlayWindow(context, Uri.parse(inspectUrl), isPrivate = true)
                                                        } else {
                                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(inspectUrl)).apply {
                                                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                            }
                                                            context.startActivity(intent)
                                                        }
                                                    } catch (_: Exception) {
                                                        Toast.makeText(context, context.getString(R.string.error_open_link_inspector), Toast.LENGTH_SHORT).show()
                                                    }
                                                },
                                                modifier = Modifier.weight(1f).height(36.dp),
                                                shape = RoundedCornerShape(10.dp),
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.rounded_visibility_24),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(14.dp),
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Shortener Settings Bottom Sheet
    if (showSettingsSheet) {
        EssentialsBottomSheet(
            onDismissRequest = { showSettingsSheet = false },
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = stringResource(R.string.shorten_settings_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                RoundedCardContainer(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        // Custom Domain
                        Column {
                            Text(
                                text = stringResource(R.string.shorten_settings_custom_domain_title),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = customDomainInput,
                                onValueChange = {
                                    customDomainInput = it
                                    UrlShortener.setCustomDomain(context, it)
                                },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text(UrlShortener.DEFAULT_DOMAIN) },
                                shape = RoundedCornerShape(12.dp),
                            )
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

                        // Auto-copy Switch Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.shorten_settings_auto_copy_title),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    text = stringResource(R.string.shorten_settings_auto_copy_desc),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Switch(
                                checked = autoCopySetting,
                                onCheckedChange = {
                                    autoCopySetting = it
                                    UrlShortener.setAutoCopyEnabled(context, it)
                                },
                            )
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

                        // Auto-strip tracking Switch Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.shorten_settings_auto_strip_title),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    text = stringResource(R.string.shorten_settings_auto_strip_desc),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Switch(
                                checked = autoStripSetting,
                                onCheckedChange = {
                                    autoStripSetting = it
                                    UrlShortener.setAutoStripTrackingEnabled(context, it)
                                },
                            )
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

                        // Action Notification Switch Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.shorten_settings_notification_title),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    text = stringResource(R.string.shorten_settings_notification_desc),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Switch(
                                checked = notificationSetting,
                                onCheckedChange = {
                                    notificationSetting = it
                                    UrlShortener.setNotificationEnabled(context, it)
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    // QR Code Bottom Sheet
    if (showQrSheet) {
        val appLogo = remember { QrCodeGenerator.getAppLogoBitmap(context) }
        val qrForegroundColor = MaterialTheme.colorScheme.onSurface.toArgb()
        val qrBackgroundColor = MaterialTheme.colorScheme.surfaceBright.toArgb()

        val qrBitmap = remember(qrContentUri, appLogo, qrForegroundColor, qrBackgroundColor) {
            QrCodeGenerator.generateQrBitmap(
                content = qrContentUri,
                size = 800,
                foregroundColor = qrForegroundColor,
                backgroundColor = qrBackgroundColor,
                logo = appLogo,
            )
        }

        EssentialsBottomSheet(
            onDismissRequest = { showQrSheet = false },
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                // Host / URI Subtitle
                Text(
                    text = Uri.parse(qrContentUri).host ?: qrContentUri,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )

                // High-Contrast Rounded QR Code Display with Material 3 Expressive Container (Full Width Fit)
                Surface(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.94f)
                            .aspectRatio(1f),
                    shape = RoundedCornerShape(28.dp),
                    color = MaterialTheme.colorScheme.surfaceBright,
                ) {
                    Box(
                        modifier = Modifier.padding(18.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            bitmap = qrBitmap.asImageBitmap(),
                            contentDescription = stringResource(R.string.qr_code_title),
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }

                // Split Connected Action Buttons Row (Share, Copy, Download) with Primary Styling
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.94f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surfaceBright)
                            .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
                ) {
                    // 1. Share QR Image (with Link Caption)
                    FilledIconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            try {
                                val qrUri = QrCodeGenerator.getShareableQrUri(context, qrBitmap)
                                if (qrUri != null) {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_STREAM, qrUri)
                                        putExtra(Intent.EXTRA_TEXT, qrContentUri)
                                        type = "image/png"
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    val chooser = Intent.createChooser(sendIntent, context.getString(R.string.qr_share_image)).apply {
                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }
                                    context.startActivity(chooser)
                                } else {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, qrContentUri)
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, null))
                                }
                            } catch (_: Exception) {
                                Toast.makeText(context, context.getString(R.string.error_share_qr_image), Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = ButtonGroupDefaults.connectedLeadingButtonShapes().shape,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.rounded_share_24),
                            contentDescription = stringResource(R.string.qr_share_image),
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    // 2. Copy QR Image
                    FilledIconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            try {
                                val qrUri = QrCodeGenerator.getShareableQrUri(context, qrBitmap)
                                if (qrUri != null) {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newUri(context.contentResolver, "QR Code Image", qrUri)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, context.getString(R.string.qr_copied_success), Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, context.getString(R.string.error_could_not_copy), Toast.LENGTH_SHORT).show()
                                }
                            } catch (_: Exception) {
                                Toast.makeText(context, context.getString(R.string.error_could_not_copy), Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = ButtonGroupDefaults.connectedMiddleButtonShapes().shape,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.rounded_content_copy_24),
                            contentDescription = stringResource(R.string.qr_copy_image),
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    // 3. Save QR Image
                    FilledIconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            val success = QrCodeGenerator.saveQrImage(context, qrBitmap)
                            if (success) {
                                Toast.makeText(context, context.getString(R.string.qr_saved_success), Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, context.getString(R.string.error_save_qr_image), Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = ButtonGroupDefaults.connectedTrailingButtonShapes().shape,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.rounded_download_24),
                            contentDescription = stringResource(R.string.qr_save_image),
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
