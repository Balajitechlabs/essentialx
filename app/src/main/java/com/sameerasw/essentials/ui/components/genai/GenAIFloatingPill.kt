package com.sameerasw.essentials.ui.components.genai

import android.view.Gravity
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.modifiers.BlurDirection
import com.sameerasw.essentials.ui.modifiers.progressiveBlur
import com.sameerasw.essentials.utils.HapticUtil

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GenAIFloatingPill(
    onSend: (String) -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    var promptText by remember { mutableStateOf("") }
    val isPromptValid = promptText.isNotBlank()

    val pulseDuration = if (isLoading) 500 else 1750
    val infiniteTransition = rememberInfiniteTransition(label = "blurPulse")
    val animatedBlurRadius by infiniteTransition.animateFloat(
        initialValue = 20f,
        targetValue = 80f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = pulseDuration),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blurRadius"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
        SideEffect {
            dialogWindow?.let { window ->
                window.setGravity(Gravity.BOTTOM)
                window.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                window.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                )
                window.setDimAmount(0.32f)
                window.setBackgroundDrawableResource(android.R.color.transparent)
            }
        }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .progressiveBlur(
                        blurRadius = animatedBlurRadius,
                        height = 150f,
                        direction = BlurDirection.TOP,
                        showGradientOverlay = false
                    )
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(32.dp)
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = promptText,
                    onValueChange = { promptText = it },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.diy_genai_describe_placeholder),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                        )
                    },
                    singleLine = true,
                    enabled = !isLoading,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        disabledTextColor = MaterialTheme.colorScheme.onPrimary,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )

                AnimatedVisibility(
                    visible = isPromptValid || isLoading,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                if (isPromptValid && !isLoading) {
                                    HapticUtil.performVirtualKeyHaptic(view)
                                    onSend(promptText.trim())
                                }
                            },
                            enabled = isPromptValid && !isLoading,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onPrimary)
                        ) {
                            if (isLoading) {
                                LoadingIndicator(
                                    modifier = Modifier.size(48.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            } else {
                                Icon(
                                    painter = painterResource(R.drawable.rounded_arrow_upward_24),
                                    contentDescription = stringResource(R.string.diy_genai_describe_send),
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
