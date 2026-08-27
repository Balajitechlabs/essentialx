/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: UI Sheets
 * File: MeDropBottomSheet.kt
 */

package com.sameerasw.essentials.ui.features.system.sheets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.rememberCoroutineScope
import coil.compose.AsyncImage
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.core.cards.IconToggleItem
import com.sameerasw.essentials.ui.core.containers.RoundedCardContainer
import com.sameerasw.essentials.ui.core.sheets.EssentialsBottomSheet
import com.sameerasw.essentials.utils.HapticUtil
import com.sameerasw.essentials.utils.MeDropContactPickerHelper
import com.sameerasw.essentials.viewmodels.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeDropBottomSheet(
    viewModel: MainViewModel,
    showSettings: Boolean = true,
    onPickContact: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val contact by viewModel.meDropContact
    val isAllowWhenLocked by viewModel.isMeDropAllowWhenLocked
    val context = LocalContext.current
    val view = LocalView.current
    val scope = rememberCoroutineScope()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val savedUri = MeDropContactPickerHelper.saveCompressedCustomPhoto(uri, context)
                if (savedUri != null) {
                    viewModel.updateMeDropCustomPhoto(context, savedUri)
                }
            }
        }
    }

    EssentialsBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.rounded_contacts_product_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.feat_medrop_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            if (contact != null) {
                val safeContact = contact!!
                if (!showSettings) {
                    // NFC Animation & Broadcast Preview Card (only shown when broadcasting/overlay)
                    NfcBroadcastIndicator()

                    RoundedCardContainer {
                        Column(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceBright)
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                if (!safeContact.photoUri.isNullOrBlank()) {
                                    AsyncImage(
                                        model = safeContact.photoUri,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = safeContact.displayName.take(1).uppercase(),
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Column {
                                     Text(
                                         text = safeContact.displayName,
                                         style = MaterialTheme.typography.titleLarge,
                                         fontWeight = FontWeight.Bold,
                                         color = MaterialTheme.colorScheme.primary
                                     )
                                     val showNickname = safeContact.isEntrySelected("nickname") && !safeContact.nickname.isNullOrBlank()
                                     val showPronouns = safeContact.isEntrySelected("pronouns") && !safeContact.pronouns.isNullOrBlank()
                                     if (showNickname || showPronouns) {
                                         val nickPart = if (showNickname) "\"${safeContact.nickname}\"" else null
                                         val pronounPart = if (showPronouns) "(${safeContact.pronouns})" else null
                                         val subName = listOfNotNull(nickPart, pronounPart).joinToString(" ")
                                         Text(
                                             text = subName,
                                             style = MaterialTheme.typography.bodySmall,
                                             color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                         )
                                     }
                                     val showOrg = safeContact.isEntrySelected("organization") && !safeContact.organization.isNullOrBlank()
                                     val showDept = safeContact.isEntrySelected("department") && !safeContact.department.isNullOrBlank()
                                     val showTitle = safeContact.isEntrySelected("jobTitle") && !safeContact.jobTitle.isNullOrBlank()
                                     val showRole = safeContact.isEntrySelected("role") && !safeContact.role.isNullOrBlank()
                                     if (showOrg || showDept || showTitle || showRole) {
                                         val roleOrTitle = listOfNotNull(
                                             if (showTitle) safeContact.jobTitle else null,
                                             if (showRole) safeContact.role else null
                                         ).filter { it.isNotBlank() }.joinToString(", ")
                                         val orgOrDept = listOfNotNull(
                                             if (showOrg) safeContact.organization else null,
                                             if (showDept) safeContact.department else null
                                         ).filter { it.isNotBlank() }.joinToString(" - ")
                                         val orgText = listOfNotNull(
                                             roleOrTitle.ifBlank { null },
                                             orgOrDept.ifBlank { null }
                                         ).joinToString(" • ")
                                         if (orgText.isNotBlank()) {
                                             Text(
                                                 text = orgText,
                                                 style = MaterialTheme.typography.bodyMedium,
                                                 color = MaterialTheme.colorScheme.onSurfaceVariant
                                             )
                                         }
                                     }
                                 }
                             }

                             Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                 if (safeContact.isEntrySelected("birthday") && !safeContact.birthday.isNullOrBlank()) {
                                     Row(verticalAlignment = Alignment.CenterVertically) {
                                         Icon(
                                             painter = painterResource(R.drawable.rounded_calendar_today_24),
                                             contentDescription = null,
                                             modifier = Modifier.size(16.dp),
                                             tint = MaterialTheme.colorScheme.onSurfaceVariant
                                         )
                                         Spacer(modifier = Modifier.width(8.dp))
                                         Text(
                                             text = safeContact.birthday,
                                             style = MaterialTheme.typography.bodyMedium,
                                             color = MaterialTheme.colorScheme.onSurfaceVariant
                                         )
                                     }
                                 }

                                 safeContact.getSafePhones().forEachIndexed { i, phone ->
                                     if (safeContact.isEntrySelected("phone_$i")) {
                                         Row(verticalAlignment = Alignment.CenterVertically) {
                                             Icon(
                                                 painter = painterResource(R.drawable.rounded_call_log_24),
                                                 contentDescription = null,
                                                 modifier = Modifier.size(16.dp),
                                                 tint = MaterialTheme.colorScheme.onSurfaceVariant
                                             )
                                             Spacer(modifier = Modifier.width(8.dp))
                                             Text(
                                                 text = phone,
                                                 style = MaterialTheme.typography.bodyMedium,
                                                 color = MaterialTheme.colorScheme.onSurfaceVariant
                                             )
                                         }
                                     }
                                 }

                                 safeContact.getSafeEmails().forEachIndexed { i, email ->
                                     if (safeContact.isEntrySelected("email_$i")) {
                                         Row(verticalAlignment = Alignment.CenterVertically) {
                                             Icon(
                                                 painter = painterResource(R.drawable.rounded_mail_24),
                                                 contentDescription = null,
                                                 modifier = Modifier.size(16.dp),
                                                 tint = MaterialTheme.colorScheme.onSurfaceVariant
                                             )
                                             Spacer(modifier = Modifier.width(8.dp))
                                             Text(
                                                 text = email,
                                                 style = MaterialTheme.typography.bodyMedium,
                                                 color = MaterialTheme.colorScheme.onSurfaceVariant
                                             )
                                         }
                                     }
                                 }

                                 safeContact.getSafeAddresses().forEachIndexed { i, addr ->
                                     if (safeContact.isEntrySelected("address_$i")) {
                                         Row(verticalAlignment = Alignment.CenterVertically) {
                                             Icon(
                                                 painter = painterResource(R.drawable.rounded_location_on_24),
                                                 contentDescription = null,
                                                 modifier = Modifier.size(16.dp),
                                                 tint = MaterialTheme.colorScheme.onSurfaceVariant
                                             )
                                             Spacer(modifier = Modifier.width(8.dp))
                                             Text(
                                                 text = addr.replace("\n", ", "),
                                                 style = MaterialTheme.typography.bodyMedium,
                                                 color = MaterialTheme.colorScheme.onSurfaceVariant
                                             )
                                         }
                                     }
                                 }

                                 safeContact.getSafeUrls().forEachIndexed { i, url ->
                                     if (safeContact.isEntrySelected("url_$i")) {
                                         Row(verticalAlignment = Alignment.CenterVertically) {
                                             Icon(
                                                 painter = painterResource(R.drawable.rounded_globe_24),
                                                 contentDescription = null,
                                                 modifier = Modifier.size(16.dp),
                                                 tint = MaterialTheme.colorScheme.onSurfaceVariant
                                             )
                                             Spacer(modifier = Modifier.width(8.dp))
                                             Text(
                                                 text = url,
                                                 style = MaterialTheme.typography.bodyMedium,
                                                 color = MaterialTheme.colorScheme.onSurfaceVariant
                                             )
                                         }
                                     }
                                 }

                                 safeContact.getSafeSocialProfiles().forEachIndexed { i, social ->
                                     if (safeContact.isEntrySelected("social_$i")) {
                                         Row(verticalAlignment = Alignment.CenterVertically) {
                                             Icon(
                                                 painter = painterResource(R.drawable.rounded_share_24),
                                                 contentDescription = null,
                                                 modifier = Modifier.size(16.dp),
                                                 tint = MaterialTheme.colorScheme.onSurfaceVariant
                                             )
                                             Spacer(modifier = Modifier.width(8.dp))
                                             Text(
                                                 text = social,
                                                 style = MaterialTheme.typography.bodyMedium,
                                                 color = MaterialTheme.colorScheme.onSurfaceVariant
                                             )
                                         }
                                     }
                                 }

                                 safeContact.getSafeImpps().forEachIndexed { i, impp ->
                                     if (safeContact.isEntrySelected("impp_$i")) {
                                         Row(verticalAlignment = Alignment.CenterVertically) {
                                             Icon(
                                                 painter = painterResource(R.drawable.rounded_chat_info_24),
                                                 contentDescription = null,
                                                 modifier = Modifier.size(16.dp),
                                                 tint = MaterialTheme.colorScheme.onSurfaceVariant
                                             )
                                             Spacer(modifier = Modifier.width(8.dp))
                                             Text(
                                                 text = impp,
                                                 style = MaterialTheme.typography.bodyMedium,
                                                 color = MaterialTheme.colorScheme.onSurfaceVariant
                                             )
                                         }
                                     }
                                 }

                                 if (safeContact.isEntrySelected("note") && !safeContact.note.isNullOrBlank()) {
                                     Row(verticalAlignment = Alignment.CenterVertically) {
                                         Icon(
                                             painter = painterResource(R.drawable.rounded_info_24),
                                             contentDescription = null,
                                             modifier = Modifier.size(16.dp),
                                             tint = MaterialTheme.colorScheme.onSurfaceVariant
                                         )
                                         Spacer(modifier = Modifier.width(8.dp))
                                         Text(
                                             text = safeContact.note,
                                             style = MaterialTheme.typography.bodyMedium,
                                             color = MaterialTheme.colorScheme.onSurfaceVariant
                                         )
                                     }
                                 }
                             }
                         }
                     }
                 } else {
                     // Settings/Edit Mode: show itemized field toggling UI directly
                     Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                         Text(
                             text = stringResource(R.string.feat_medrop_share_fields_title),
                             style = MaterialTheme.typography.labelLarge,
                             color = MaterialTheme.colorScheme.primary,
                             modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                         )
                         RoundedCardContainer {
                             if (!safeContact.photoUri.isNullOrBlank()) {
                                 val id = "photo"
                                 IconToggleItem(
                                     iconRes = R.drawable.rounded_contacts_product_24,
                                     title = stringResource(R.string.feat_medrop_include_photo),
                                     isChecked = safeContact.isEntrySelected(id),
                                     onCheckedChange = {
                                         viewModel.toggleMeDropContactEntry(context, id, it)
                                     }
                                 )
                             }
                             if (!safeContact.nickname.isNullOrBlank()) {
                                 val id = "nickname"
                                 IconToggleItem(
                                     iconRes = R.drawable.rounded_app_registration_24,
                                     title = safeContact.nickname,
                                     subtitle = stringResource(R.string.feat_medrop_field_nickname),
                                     isChecked = safeContact.isEntrySelected(id),
                                     onCheckedChange = {
                                         viewModel.toggleMeDropContactEntry(context, id, it)
                                     }
                                 )
                             }
                             if (!safeContact.pronouns.isNullOrBlank()) {
                                 val id = "pronouns"
                                 IconToggleItem(
                                     iconRes = R.drawable.rounded_heart_smile_24,
                                     title = safeContact.pronouns,
                                     subtitle = stringResource(R.string.feat_medrop_field_pronouns),
                                     isChecked = safeContact.isEntrySelected(id),
                                     onCheckedChange = {
                                         viewModel.toggleMeDropContactEntry(context, id, it)
                                     }
                                 )
                             }
                             if (!safeContact.birthday.isNullOrBlank()) {
                                 val id = "birthday"
                                 IconToggleItem(
                                     iconRes = R.drawable.rounded_calendar_today_24,
                                     title = safeContact.birthday,
                                     subtitle = stringResource(R.string.feat_medrop_field_birthday),
                                     isChecked = safeContact.isEntrySelected(id),
                                     onCheckedChange = {
                                         viewModel.toggleMeDropContactEntry(context, id, it)
                                     }
                                 )
                             }
                             safeContact.getSafePhones().forEachIndexed { i, phone ->
                                 val id = "phone_$i"
                                 IconToggleItem(
                                     iconRes = R.drawable.rounded_call_log_24,
                                     title = phone,
                                     isChecked = safeContact.isEntrySelected(id),
                                     onCheckedChange = {
                                         viewModel.toggleMeDropContactEntry(context, id, it)
                                     }
                                 )
                             }
                             safeContact.getSafeEmails().forEachIndexed { i, email ->
                                 val id = "email_$i"
                                 IconToggleItem(
                                     iconRes = R.drawable.rounded_mail_24,
                                     title = email,
                                     isChecked = safeContact.isEntrySelected(id),
                                     onCheckedChange = {
                                         viewModel.toggleMeDropContactEntry(context, id, it)
                                     }
                                 )
                             }
                             if (!safeContact.organization.isNullOrBlank()) {
                                 val id = "organization"
                                 IconToggleItem(
                                     iconRes = R.drawable.rounded_work_24,
                                     title = safeContact.organization,
                                     subtitle = stringResource(R.string.feat_medrop_field_organization),
                                     isChecked = safeContact.isEntrySelected(id),
                                     onCheckedChange = {
                                         viewModel.toggleMeDropContactEntry(context, id, it)
                                     }
                                 )
                             }
                             if (!safeContact.department.isNullOrBlank()) {
                                 val id = "department"
                                 IconToggleItem(
                                     iconRes = R.drawable.rounded_work_24,
                                     title = safeContact.department,
                                     subtitle = stringResource(R.string.feat_medrop_field_department),
                                     isChecked = safeContact.isEntrySelected(id),
                                     onCheckedChange = {
                                         viewModel.toggleMeDropContactEntry(context, id, it)
                                     }
                                 )
                             }
                             if (!safeContact.jobTitle.isNullOrBlank()) {
                                 val id = "jobTitle"
                                 IconToggleItem(
                                     iconRes = R.drawable.rounded_work_24,
                                     title = safeContact.jobTitle,
                                     subtitle = stringResource(R.string.feat_medrop_field_job_title),
                                     isChecked = safeContact.isEntrySelected(id),
                                     onCheckedChange = {
                                         viewModel.toggleMeDropContactEntry(context, id, it)
                                     }
                                 )
                             }
                             if (!safeContact.role.isNullOrBlank()) {
                                 val id = "role"
                                 IconToggleItem(
                                     iconRes = R.drawable.rounded_work_24,
                                     title = safeContact.role,
                                     subtitle = stringResource(R.string.feat_medrop_field_role),
                                     isChecked = safeContact.isEntrySelected(id),
                                     onCheckedChange = {
                                         viewModel.toggleMeDropContactEntry(context, id, it)
                                     }
                                 )
                             }
                             safeContact.getSafeAddresses().forEachIndexed { i, addr ->
                                 val id = "address_$i"
                                 IconToggleItem(
                                     iconRes = R.drawable.rounded_location_on_24,
                                     title = addr.replace("\n", ", "),
                                     isChecked = safeContact.isEntrySelected(id),
                                     onCheckedChange = {
                                         viewModel.toggleMeDropContactEntry(context, id, it)
                                     }
                                 )
                             }
                             safeContact.getSafeUrls().forEachIndexed { i, url ->
                                 val id = "url_$i"
                                 IconToggleItem(
                                     iconRes = R.drawable.rounded_globe_24,
                                     title = url,
                                     isChecked = safeContact.isEntrySelected(id),
                                     onCheckedChange = {
                                         viewModel.toggleMeDropContactEntry(context, id, it)
                                     }
                                 )
                             }
                             safeContact.getSafeSocialProfiles().forEachIndexed { i, social ->
                                 val id = "social_$i"
                                 IconToggleItem(
                                     iconRes = R.drawable.rounded_share_24,
                                     title = social,
                                     subtitle = stringResource(R.string.feat_medrop_field_social),
                                     isChecked = safeContact.isEntrySelected(id),
                                     onCheckedChange = {
                                         viewModel.toggleMeDropContactEntry(context, id, it)
                                     }
                                 )
                             }
                             safeContact.getSafeImpps().forEachIndexed { i, impp ->
                                 val id = "impp_$i"
                                 IconToggleItem(
                                     iconRes = R.drawable.rounded_chat_info_24,
                                     title = impp,
                                     subtitle = stringResource(R.string.feat_medrop_field_impp),
                                     isChecked = safeContact.isEntrySelected(id),
                                     onCheckedChange = {
                                         viewModel.toggleMeDropContactEntry(context, id, it)
                                     }
                                 )
                             }
                             if (!safeContact.note.isNullOrBlank()) {
                                 val id = "note"
                                 IconToggleItem(
                                     iconRes = R.drawable.rounded_info_24,
                                     title = safeContact.note,
                                     isChecked = safeContact.isEntrySelected(id),
                                     onCheckedChange = {
                                         viewModel.toggleMeDropContactEntry(context, id, it)
                                     }
                                 )
                             }
                         }
                     }
                 }
            } else {
                // Empty state
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 32.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.rounded_contacts_product_24),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.feat_medrop_no_contact),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.feat_medrop_no_contact_desc),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = {
                        HapticUtil.performVirtualKeyHaptic(view)
                        onPickContact()
                    }) {
                        Text(stringResource(R.string.feat_medrop_select_contact))
                    }
                }
            }

            if (showSettings) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.label_options),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                    )
                    RoundedCardContainer {
                        IconToggleItem(
                            iconRes = R.drawable.rounded_lock_24,
                            title = stringResource(R.string.feat_medrop_allow_when_locked),
                            description = stringResource(R.string.feat_medrop_allow_when_locked_desc),
                            isChecked = isAllowWhenLocked,
                            onCheckedChange = { viewModel.setMeDropAllowWhenLocked(context, it) }
                        )
                        if (contact != null) {
                            ListItem(
                                onClick = {
                                    HapticUtil.performVirtualKeyHaptic(view)
                                    photoPickerLauncher.launch("image/*")
                                },
                                leadingContent = {
                                    Icon(
                                        painter = painterResource(R.drawable.rounded_add_photo_alternate_24),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                colors = ListItemDefaults.colors(
                                    containerColor = MaterialTheme.colorScheme.surfaceBright
                                ),
                                content = {
                                    Text(
                                        text = stringResource(R.string.feat_medrop_choose_custom_photo),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            )
                            if (!contact?.photoUri.isNullOrBlank()) {
                                ListItem(
                                    onClick = {
                                        HapticUtil.performVirtualKeyHaptic(view)
                                        viewModel.updateMeDropCustomPhoto(context, null)
                                    },
                                    leadingContent = {
                                        Icon(
                                            painter = painterResource(R.drawable.rounded_delete_24),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    colors = ListItemDefaults.colors(
                                        containerColor = MaterialTheme.colorScheme.surfaceBright
                                    ),
                                    content = {
                                        Text(
                                            text = stringResource(R.string.feat_medrop_remove_custom_photo),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                )
                            }
                            ListItem(
                                onClick = {
                                    HapticUtil.performVirtualKeyHaptic(view)
                                    onPickContact()
                                },
                                leadingContent = {
                                    Icon(
                                        painter = painterResource(R.drawable.rounded_contacts_product_24),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                colors = ListItemDefaults.colors(
                                    containerColor = MaterialTheme.colorScheme.surfaceBright
                                ),
                                content = {
                                    Text(
                                        text = stringResource(R.string.feat_medrop_change_contact),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NfcBroadcastIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "nfc_waves")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        // Box(contentAlignment = Alignment.Center) {
        //     Icon(
        //         painter = painterResource(R.drawable.rounded_nfc_24),
        //         contentDescription = null,
        //         modifier = Modifier
        //             .size(48.dp)
        //             .graphicsLayer {
        //                 scaleX = scale
        //                 scaleY = scale
        //                 this.alpha = alpha
        //             },
        //         tint = MaterialTheme.colorScheme.primary
        //     )
        // }
        // Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.feat_medrop_hold_near),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        // Text(
        //     text = stringResource(R.string.feat_medrop_broadcasting),
        //     style = MaterialTheme.typography.labelSmall,
        //     color = MaterialTheme.colorScheme.onSurfaceVariant
        // )
    }
}
