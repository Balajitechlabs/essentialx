package com.sameerasw.essentials.ui.components.buttons

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.theme.Shapes
import com.sameerasw.essentials.utils.HapticUtil

@Composable
fun ListExpandToggleButton(
    isExpanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    expandedText: String = stringResource(R.string.action_show_top_apps),
    collapsedText: String = stringResource(R.string.action_show_all)
) {
    val view = LocalView.current
    val rotationDegree by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "list_expand_chevron_rotation"
    )

    Button(
        onClick = {
            HapticUtil.performVirtualKeyHaptic(view)
            onToggle()
        },
        modifier = modifier.padding(start = 4.dp, top = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.rounded_keyboard_arrow_down_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(22.dp)
                .graphicsLayer { rotationZ = rotationDegree }
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = if (isExpanded) expandedText else collapsedText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
