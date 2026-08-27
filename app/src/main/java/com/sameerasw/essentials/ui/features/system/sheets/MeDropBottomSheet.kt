/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: UI Sheets
 * File: MeDropBottomSheet.kt
 */

package com.sameerasw.essentials.ui.features.system.sheets

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sameerasw.essentials.R
import com.sameerasw.essentials.domain.model.MeDropProfileType
import com.sameerasw.essentials.domain.model.MeDropSettings
import com.sameerasw.essentials.ui.activities.MeDropSettingsActivity
import com.sameerasw.essentials.ui.core.cards.IconToggleItem
import com.sameerasw.essentials.ui.core.containers.RoundedCardContainer
import com.sameerasw.essentials.ui.core.pickers.SegmentedPicker
import com.sameerasw.essentials.ui.core.sheets.EssentialsBottomSheet
import com.sameerasw.essentials.ui.theme.Shapes
import com.sameerasw.essentials.utils.HapticUtil
import com.sameerasw.essentials.utils.MeDropNfcManager
import com.sameerasw.essentials.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeDropBottomSheet(
    viewModel: MainViewModel,
    onDismissRequest: () -> Unit
) {
    val settings by viewModel.meDropSettings
    val context = LocalContext.current
    val view = LocalView.current

    val safeSettings = settings ?: MeDropSettings()
    val contact = safeSettings.contact
    val activeProfileType = safeSettings.activeProfileType

    // Start broadcast immediately and update whenever settings or active profile changes
    val activity = context as? android.app.Activity
    androidx.compose.runtime.DisposableEffect(safeSettings, activity) {
        if (contact != null && activity != null) {
            MeDropNfcManager.startBroadcast(activity, safeSettings)
        }
        onDispose {
            if (activity != null) {
                MeDropNfcManager.stopBroadcast(activity)
            }
        }
    }

    // Only show available/enabled profiles in the picker
    val availableProfiles = mutableListOf(MeDropProfileType.CONTACT)
    if (safeSettings.professionalProfile.enabled) {
        availableProfiles.add(MeDropProfileType.PROFESSIONAL)
    }
    if (safeSettings.customProfile.enabled) {
        availableProfiles.add(MeDropProfileType.CUSTOM)
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
            if (contact != null) {
                val effectivePhoto = safeSettings.getEffectivePhotoUri(activeProfileType)

                // Top Profile Photo Circle (Bigger 100dp)
                if (!effectivePhoto.isNullOrBlank()) {
                    AsyncImage(
                        model = effectivePhoto,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = contact.displayName.take(1).uppercase(),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Profile Selector Picker inside RoundedCardContainer
                if (availableProfiles.size > 1) {
                    RoundedCardContainer {
                        SegmentedPicker(
                            items = availableProfiles,
                            selectedItem = if (availableProfiles.contains(activeProfileType)) activeProfileType else MeDropProfileType.CONTACT,
                            onItemSelected = { selectedType ->
                                viewModel.setMeDropActiveProfile(context, selectedType)
                            },
                            labelProvider = { type ->
                                when (type) {
                                    MeDropProfileType.CONTACT -> context.getString(R.string.feat_medrop_profile_contact)
                                    MeDropProfileType.PROFESSIONAL -> context.getString(R.string.feat_medrop_profile_professional)
                                    MeDropProfileType.CUSTOM -> context.getString(R.string.feat_medrop_profile_custom)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Contact Preview Card
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
                            Column {
                                Text(
                                    text = contact.displayName,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                val showNickname = safeSettings.isEntrySelected(activeProfileType, "nickname") && !contact.nickname.isNullOrBlank()
                                val showPronouns = safeSettings.isEntrySelected(activeProfileType, "pronouns") && !contact.pronouns.isNullOrBlank()
                                if (showNickname || showPronouns) {
                                    val nickPart = if (showNickname) "\"${contact.nickname}\"" else null
                                    val pronounPart = if (showPronouns) "(${contact.pronouns})" else null
                                    val subName = listOfNotNull(nickPart, pronounPart).joinToString(" ")
                                    Text(
                                        text = subName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                    )
                                }
                                val showOrg = safeSettings.isEntrySelected(activeProfileType, "organization") && !contact.organization.isNullOrBlank()
                                val showDept = safeSettings.isEntrySelected(activeProfileType, "department") && !contact.department.isNullOrBlank()
                                val showTitle = safeSettings.isEntrySelected(activeProfileType, "jobTitle") && !contact.jobTitle.isNullOrBlank()
                                val showRole = safeSettings.isEntrySelected(activeProfileType, "role") && !contact.role.isNullOrBlank()
                                if (showOrg || showDept || showTitle || showRole) {
                                    val roleOrTitle = listOfNotNull(
                                        if (showTitle) contact.jobTitle else null,
                                        if (showRole) contact.role else null
                                    ).filter { it.isNotBlank() }.joinToString(", ")
                                    val orgOrDept = listOfNotNull(
                                        if (showOrg) contact.organization else null,
                                        if (showDept) contact.department else null
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
                            if (safeSettings.isEntrySelected(activeProfileType, "birthday") && !contact.birthday.isNullOrBlank()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(R.drawable.rounded_calendar_today_24),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = contact.birthday,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            contact.getSafePhones().forEachIndexed { i, phone ->
                                if (safeSettings.isEntrySelected(activeProfileType, "phone_$i")) {
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

                            contact.getSafeEmails().forEachIndexed { i, email ->
                                if (safeSettings.isEntrySelected(activeProfileType, "email_$i")) {
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

                            contact.getSafeAddresses().forEachIndexed { i, addr ->
                                if (safeSettings.isEntrySelected(activeProfileType, "address_$i")) {
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

                            contact.getSafeUrls().forEachIndexed { i, url ->
                                if (safeSettings.isEntrySelected(activeProfileType, "url_$i")) {
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

                            if (safeSettings.isEntrySelected(activeProfileType, "note") && !contact.note.isNullOrBlank()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(R.drawable.rounded_info_24),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = contact.note,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                // If Custom profile is active, allow dynamic live-updating of shared fields in the sheet
                AnimatedVisibility(visible = activeProfileType == MeDropProfileType.CUSTOM) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = stringResource(R.string.feat_medrop_share_fields_title),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                        )
                        RoundedCardContainer {
                            if (!contact.nickname.isNullOrBlank()) {
                                val id = "nickname"
                                IconToggleItem(
                                    iconRes = R.drawable.rounded_app_registration_24,
                                    title = contact.nickname,
                                    subtitle = stringResource(R.string.feat_medrop_field_nickname),
                                    isChecked = safeSettings.isEntrySelected(MeDropProfileType.CUSTOM, id),
                                    onCheckedChange = {
                                        viewModel.toggleMeDropProfileEntry(context, MeDropProfileType.CUSTOM, id, it)
                                    }
                                )
                            }
                            if (!contact.pronouns.isNullOrBlank()) {
                                val id = "pronouns"
                                IconToggleItem(
                                    iconRes = R.drawable.rounded_heart_smile_24,
                                    title = contact.pronouns,
                                    subtitle = stringResource(R.string.feat_medrop_field_pronouns),
                                    isChecked = safeSettings.isEntrySelected(MeDropProfileType.CUSTOM, id),
                                    onCheckedChange = {
                                        viewModel.toggleMeDropProfileEntry(context, MeDropProfileType.CUSTOM, id, it)
                                    }
                                )
                            }
                            if (!contact.birthday.isNullOrBlank()) {
                                val id = "birthday"
                                IconToggleItem(
                                    iconRes = R.drawable.rounded_calendar_today_24,
                                    title = contact.birthday,
                                    subtitle = stringResource(R.string.feat_medrop_field_birthday),
                                    isChecked = safeSettings.isEntrySelected(MeDropProfileType.CUSTOM, id),
                                    onCheckedChange = {
                                        viewModel.toggleMeDropProfileEntry(context, MeDropProfileType.CUSTOM, id, it)
                                    }
                                )
                            }
                            contact.getSafePhones().forEachIndexed { i, phone ->
                                val id = "phone_$i"
                                IconToggleItem(
                                    iconRes = R.drawable.rounded_call_log_24,
                                    title = phone,
                                    isChecked = safeSettings.isEntrySelected(MeDropProfileType.CUSTOM, id),
                                    onCheckedChange = {
                                        viewModel.toggleMeDropProfileEntry(context, MeDropProfileType.CUSTOM, id, it)
                                    }
                                )
                            }
                            contact.getSafeEmails().forEachIndexed { i, email ->
                                val id = "email_$i"
                                IconToggleItem(
                                    iconRes = R.drawable.rounded_mail_24,
                                    title = email,
                                    isChecked = safeSettings.isEntrySelected(MeDropProfileType.CUSTOM, id),
                                    onCheckedChange = {
                                        viewModel.toggleMeDropProfileEntry(context, MeDropProfileType.CUSTOM, id, it)
                                    }
                                )
                            }
                            if (!contact.organization.isNullOrBlank()) {
                                val id = "organization"
                                IconToggleItem(
                                    iconRes = R.drawable.rounded_work_24,
                                    title = contact.organization,
                                    subtitle = stringResource(R.string.feat_medrop_field_organization),
                                    isChecked = safeSettings.isEntrySelected(MeDropProfileType.CUSTOM, id),
                                    onCheckedChange = {
                                        viewModel.toggleMeDropProfileEntry(context, MeDropProfileType.CUSTOM, id, it)
                                    }
                                )
                            }
                            if (!contact.department.isNullOrBlank()) {
                                val id = "department"
                                IconToggleItem(
                                    iconRes = R.drawable.rounded_work_24,
                                    title = contact.department,
                                    subtitle = stringResource(R.string.feat_medrop_field_department),
                                    isChecked = safeSettings.isEntrySelected(MeDropProfileType.CUSTOM, id),
                                    onCheckedChange = {
                                        viewModel.toggleMeDropProfileEntry(context, MeDropProfileType.CUSTOM, id, it)
                                    }
                                )
                            }
                            if (!contact.jobTitle.isNullOrBlank()) {
                                val id = "jobTitle"
                                IconToggleItem(
                                    iconRes = R.drawable.rounded_work_24,
                                    title = contact.jobTitle,
                                    subtitle = stringResource(R.string.feat_medrop_field_job_title),
                                    isChecked = safeSettings.isEntrySelected(MeDropProfileType.CUSTOM, id),
                                    onCheckedChange = {
                                        viewModel.toggleMeDropProfileEntry(context, MeDropProfileType.CUSTOM, id, it)
                                    }
                                )
                            }
                            if (!contact.role.isNullOrBlank()) {
                                val id = "role"
                                IconToggleItem(
                                    iconRes = R.drawable.rounded_work_24,
                                    title = contact.role,
                                    subtitle = stringResource(R.string.feat_medrop_field_role),
                                    isChecked = safeSettings.isEntrySelected(MeDropProfileType.CUSTOM, id),
                                    onCheckedChange = {
                                        viewModel.toggleMeDropProfileEntry(context, MeDropProfileType.CUSTOM, id, it)
                                    }
                                )
                            }
                            contact.getSafeAddresses().forEachIndexed { i, addr ->
                                val id = "address_$i"
                                val addrType = contact.getSafeAddressTypes().getOrNull(i)
                                val tag = if (addrType == 2) stringResource(R.string.feat_medrop_address_work) else stringResource(R.string.feat_medrop_address_home)
                                IconToggleItem(
                                    iconRes = R.drawable.rounded_location_on_24,
                                    title = addr.replace("\n", ", "),
                                    subtitle = tag,
                                    isChecked = safeSettings.isEntrySelected(MeDropProfileType.CUSTOM, id),
                                    onCheckedChange = {
                                        viewModel.toggleMeDropProfileEntry(context, MeDropProfileType.CUSTOM, id, it)
                                    }
                                )
                            }
                            contact.getSafeUrls().forEachIndexed { i, url ->
                                val id = "url_$i"
                                IconToggleItem(
                                    iconRes = R.drawable.rounded_globe_24,
                                    title = url,
                                    isChecked = safeSettings.isEntrySelected(MeDropProfileType.CUSTOM, id),
                                    onCheckedChange = {
                                        viewModel.toggleMeDropProfileEntry(context, MeDropProfileType.CUSTOM, id, it)
                                    }
                                )
                            }
                            if (!contact.note.isNullOrBlank()) {
                                val id = "note"
                                IconToggleItem(
                                    iconRes = R.drawable.rounded_info_24,
                                    title = contact.note,
                                    isChecked = safeSettings.isEntrySelected(MeDropProfileType.CUSTOM, id),
                                    onCheckedChange = {
                                        viewModel.toggleMeDropProfileEntry(context, MeDropProfileType.CUSTOM, id, it)
                                    }
                                )
                            }
                        }
                    }
                }

                // NFC Broadcast Indicator placed at very bottom
                NfcBroadcastIndicator()
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
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            HapticUtil.performVirtualKeyHaptic(view)
                            onDismissRequest()
                            val intent = Intent(context, MeDropSettingsActivity::class.java)
                            context.startActivity(intent)
                        },
                        shape = Shapes.medium
                    ) {
                        Text(text = stringResource(R.string.feat_medrop_set_up))
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = stringResource(R.string.feat_medrop_hold_near),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
