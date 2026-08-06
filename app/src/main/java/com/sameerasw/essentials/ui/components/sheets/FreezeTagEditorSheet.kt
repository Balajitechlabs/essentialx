package com.sameerasw.essentials.ui.components.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sameerasw.essentials.R
import com.sameerasw.essentials.domain.model.AppTag
import com.sameerasw.essentials.ui.components.FreezeTagIconPicker
import com.sameerasw.essentials.ui.components.cards.IconToggleItem
import com.sameerasw.essentials.ui.components.containers.RoundedCardContainer
import com.sameerasw.essentials.utils.HapticUtil
import java.util.UUID

val PRESET_PASTEL_COLORS = listOf(
    "#FFB3BA", // Light Pink
    "#FFDFBA", // Light Peach
    "#FFFFBA", // Light Yellow
    "#BAFFC9", // Mint Green
    "#BAE1FF", // Light Blue
    "#E8AEFF", // Light Purple
    "#FFC6FF", // Soft Magenta
    "#BDB2FF", // Periwinkle
    "#FFD6A5", // Soft Orange
    "#CAFFBF"  // Soft Lime
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreezeTagEditorSheet(
    tagToEdit: AppTag? = null,
    onDismissRequest: () -> Unit,
    onSave: (AppTag) -> Unit,
    onDelete: ((String) -> Unit)? = null
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val view = LocalView.current

    var name by remember { mutableStateOf(tagToEdit?.name ?: "") }
    var selectedColorHex by remember {
        mutableStateOf(
            tagToEdit?.colorHex ?: PRESET_PASTEL_COLORS.random()
        )
    }
    var selectedIconName by remember { mutableStateOf(tagToEdit?.iconName ?: "rounded_tag_24") }
    var neverAutoFreeze by remember { mutableStateOf(tagToEdit?.neverAutoFreeze ?: false) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (tagToEdit == null) stringResource(R.string.action_create_tag)
                           else stringResource(R.string.action_update_tag),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (tagToEdit != null && onDelete != null) {
                    IconButton(
                        onClick = {
                            HapticUtil.performVirtualKeyHaptic(view)
                            onDelete(tagToEdit.id)
                            onDismissRequest()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.rounded_delete_24),
                            contentDescription = stringResource(R.string.action_remove),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            RoundedCardContainer(spacing = 2.dp) {
                // Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text(stringResource(R.string.freeze_tag_name_hint)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceBright
                    )
                )

                // Color Picker
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceBright)
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.freeze_tag_color_picker_label),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(PRESET_PASTEL_COLORS) { colorHex ->
                            val parsedColor = try {
                                Color(android.graphics.Color.parseColor(colorHex))
                            } catch (e: Exception) {
                                MaterialTheme.colorScheme.primary
                            }
                            val isSelected = colorHex.equals(selectedColorHex, ignoreCase = true)

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(parsedColor)
                                    .clickable {
                                        HapticUtil.performVirtualKeyHaptic(view)
                                        selectedColorHex = colorHex
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(Color.Black.copy(alpha = 0.5f))
                                    )
                                }
                            }
                        }
                    }
                }

                // Icon Picker
                FreezeTagIconPicker(
                    selectedIconName = selectedIconName,
                    onIconSelected = { selectedIconName = it },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceBright)
                        .padding(16.dp)
                )

                // Never auto freeze
                IconToggleItem(
                    iconRes = R.drawable.rounded_lock_clock_24,
                    title = stringResource(R.string.freeze_tag_never_auto_freeze_title),
                    subtitle = stringResource(R.string.freeze_tag_never_auto_freeze_desc),
                    isChecked = neverAutoFreeze,
                    onCheckedChange = { neverAutoFreeze = it },
                    enabled = true
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        HapticUtil.performVirtualKeyHaptic(view)
                        onDismissRequest()
                    }
                ) {
                    Text(stringResource(R.string.action_cancel))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        HapticUtil.performVirtualKeyHaptic(view)
                        val finalTag = AppTag(
                            id = tagToEdit?.id ?: UUID.randomUUID().toString(),
                            name = name.ifBlank { "Tag" },
                            colorHex = selectedColorHex,
                            iconName = selectedIconName,
                            neverAutoFreeze = neverAutoFreeze
                        )
                        onSave(finalTag)
                        onDismissRequest()
                    },
                    enabled = name.isNotBlank()
                ) {
                    Text(stringResource(R.string.action_save))
                }
            }
        }
    }
}
