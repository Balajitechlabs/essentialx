package com.sameerasw.essentials.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sameerasw.essentials.FeatureSettingsActivity
import com.sameerasw.essentials.ui.composables.FreezeGridUI
import com.sameerasw.essentials.ui.modifiers.BlurDirection
import com.sameerasw.essentials.ui.modifiers.progressiveBlur
import com.sameerasw.essentials.ui.state.LocalMenuStateManager
import com.sameerasw.essentials.ui.state.MenuStateManager
import com.sameerasw.essentials.ui.theme.EssentialsTheme
import com.sameerasw.essentials.viewmodels.MainViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.MenuDefaults
import androidx.compose.ui.unit.DpOffset
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.components.menus.SegmentedDropdownMenu
import com.sameerasw.essentials.ui.components.menus.SegmentedDropdownMenuItem
import com.sameerasw.essentials.utils.HapticUtil

class AppFreezingActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        setContent {
            val viewModel: MainViewModel = viewModel()
            val context = LocalContext.current
            val view = LocalView.current

            LaunchedEffect(Unit) {
                viewModel.check(context)
                viewModel.refreshFreezePickedApps(context)
            }

            val isPitchBlackThemeEnabled by viewModel.isPitchBlackThemeEnabled
            val isBlurEnabled by viewModel.isBlurEnabled

            EssentialsTheme(pitchBlackTheme = isPitchBlackThemeEnabled) {
                CompositionLocalProvider(
                    LocalMenuStateManager provides remember { MenuStateManager() }
                ) {
                    Scaffold(
                        contentWindowInsets = WindowInsets(0, 0, 0, 0),
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ) { innerPadding ->
                        val density = LocalDensity.current
                        val statusBarHeightPx = with(density) {
                            WindowInsets.statusBars.asPaddingValues().calculateTopPadding().toPx()
                        }

                        val isShizukuAvailable by viewModel.isShizukuAvailable
                        val isShizukuPermissionGranted by viewModel.isShizukuPermissionGranted
                        var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }
                        androidx.activity.compose.BackHandler(fabMenuExpanded) { fabMenuExpanded = false }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .progressiveBlur(
                                    blurRadius = if (isBlurEnabled) 40f else 0f,
                                    height = statusBarHeightPx * 1.15f,
                                    direction = BlurDirection.TOP
                                )
                                .progressiveBlur(
                                    blurRadius = if (isBlurEnabled) 40f else 0f,
                                    height = with(density) { 80.dp.toPx() },
                                    direction = BlurDirection.BOTTOM
                                )
                        ) {
                            FreezeGridUI(
                                viewModel = viewModel,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(
                                    top = WindowInsets.statusBars.asPaddingValues()
                                        .calculateTopPadding(),
                                    bottom = WindowInsets.navigationBars.asPaddingValues()
                                        .calculateBottomPadding() + 130.dp,
                                    start = 0.dp,
                                    end = 0.dp
                                ),
                                onAppLaunched = {
                                    finish()
                                },
                                onSettingsClick = {
                                    val intent =
                                        Intent(context, FeatureSettingsActivity::class.java).apply {
                                            putExtra("feature", "Freeze")
                                        }
                                    context.startActivity(intent)
                                }
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 16.dp, end = 16.dp)
                            ) {
                                FloatingActionButton(
                                    onClick = {
                                        HapticUtil.performVirtualKeyHaptic(view)
                                        fabMenuExpanded = !fabMenuExpanded
                                    },
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ) {
                                    Icon(
                                        painter = painterResource(id = if (fabMenuExpanded) R.drawable.rounded_close_24 else R.drawable.rounded_more_vert_24),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                SegmentedDropdownMenu(
                                    expanded = fabMenuExpanded,
                                    onDismissRequest = { fabMenuExpanded = false },
                                    offset = DpOffset(0.dp, (-8).dp)
                                ) {
                                    val fabColors = MenuDefaults.itemColors(
                                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        leadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        trailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    val itemBg = MaterialTheme.colorScheme.primaryContainer

                                    SegmentedDropdownMenuItem(
                                        text = { Text(stringResource(R.string.action_freeze)) },
                                        onClick = {
                                            HapticUtil.performVirtualKeyHaptic(view)
                                            fabMenuExpanded = false
                                            viewModel.freezeAllAuto(context)
                                        },
                                        itemContainerColor = itemBg,
                                        colors = fabColors,
                                        leadingIcon = {
                                            Icon(
                                                painter = painterResource(id = R.drawable.rounded_mode_cool_24),
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    )
                                    SegmentedDropdownMenuItem(
                                        text = { Text(stringResource(R.string.action_unfreeze)) },
                                        onClick = {
                                            HapticUtil.performVirtualKeyHaptic(view)
                                            fabMenuExpanded = false
                                            viewModel.unfreezeAllAuto(context)
                                        },
                                        itemContainerColor = itemBg,
                                        colors = fabColors,
                                        leadingIcon = {
                                            Icon(
                                                painter = painterResource(id = R.drawable.rounded_mode_cool_off_24),
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    )
                                    SegmentedDropdownMenuItem(
                                        text = { Text(stringResource(R.string.action_freeze_all)) },
                                        onClick = {
                                            HapticUtil.performVirtualKeyHaptic(view)
                                            fabMenuExpanded = false
                                            viewModel.freezeAllManual(context)
                                        },
                                        itemContainerColor = itemBg,
                                        colors = fabColors,
                                        leadingIcon = {
                                            Icon(
                                                painter = painterResource(id = R.drawable.rounded_mode_cool_24),
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    )
                                    SegmentedDropdownMenuItem(
                                        text = { Text(stringResource(R.string.action_unfreeze_all)) },
                                        onClick = {
                                            HapticUtil.performVirtualKeyHaptic(view)
                                            fabMenuExpanded = false
                                            viewModel.unfreezeAllManual(context)
                                        },
                                        itemContainerColor = itemBg,
                                        colors = fabColors,
                                        leadingIcon = {
                                            Icon(
                                                painter = painterResource(id = R.drawable.rounded_mode_cool_off_24),
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    )
                                    SegmentedDropdownMenuItem(
                                        text = { Text(stringResource(R.string.label_settings)) },
                                        onClick = {
                                            HapticUtil.performVirtualKeyHaptic(view)
                                            fabMenuExpanded = false
                                            val intent = Intent(context, FeatureSettingsActivity::class.java).apply {
                                                putExtra("feature", "Freeze")
                                            }
                                            context.startActivity(intent)
                                        },
                                        itemContainerColor = itemBg,
                                        colors = fabColors,
                                        leadingIcon = {
                                            Icon(
                                                painter = painterResource(id = R.drawable.rounded_settings_24),
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
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
    }
}
