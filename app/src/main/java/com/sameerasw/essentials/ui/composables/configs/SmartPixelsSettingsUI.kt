package com.sameerasw.essentials.ui.composables.configs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.components.cards.IconToggleItem
import com.sameerasw.essentials.ui.components.containers.RoundedCardContainer
import com.sameerasw.essentials.ui.components.sliders.ConfigSliderItem
import com.sameerasw.essentials.ui.modifiers.highlight
import com.sameerasw.essentials.utils.HapticUtil
import com.sameerasw.essentials.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SmartPixelsSettingsUI(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    highlightSetting: String? = null
) {
    val context = LocalContext.current
    val view = LocalView.current

    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

        RoundedCardContainer(
            spacing = 2.dp,
            cornerRadius = 24.dp
        ) {
            IconToggleItem(
                iconRes = R.drawable.rounded_grain_24,
                title = stringResource(R.string.smart_pixels_enable_title),
                isChecked = viewModel.isSmartPixelsEnabled.value,
                onCheckedChange = { checked ->
                    HapticUtil.performUIHaptic(view)
                    viewModel.setSmartPixelsEnabled(context, checked)
                },
                modifier = Modifier.highlight(highlightSetting == "smart_pixels_enable_toggle")
            )
        }

        RoundedCardContainer(
            spacing = 2.dp,
            cornerRadius = 24.dp
        ) {
            ConfigSliderItem(
                title = stringResource(R.string.smart_pixels_intensity_title),
                value = viewModel.smartPixelsIntensity.floatValue,
                onValueChange = { value ->
                    HapticUtil.performUIHaptic(view)
                    viewModel.setSmartPixelsIntensity(context, value)
                },
                valueRange = 10f..90f,
                increment = 5f,
                iconRes = R.drawable.rounded_blur_linear_24,
                modifier = Modifier.highlight(highlightSetting == "smart_pixels_intensity_slider")
            )
        }
    }
}
