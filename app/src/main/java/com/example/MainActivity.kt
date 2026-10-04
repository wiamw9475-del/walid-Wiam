package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Platform
import com.example.ui.MainViewModel
import com.example.ui.components.EditSectionDialog
import com.example.ui.components.GenerationProgressDialog
import com.example.ui.components.IdeaGeneratorModal
import com.example.ui.components.ThumbnailPromptStudioModal
import com.example.ui.navigation.Screen
import com.example.ui.screens.FacebookScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InstagramScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TikTokScreen
import com.example.ui.screens.YouTubeScreen
import com.example.ui.theme.Get1MViewsTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.ViralGold

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            val savedList by viewModel.savedProjects.collectAsState()
            val snackbarHostState = remember { SnackbarHostState() }

            // Snackbar listener
            LaunchedEffect(uiState.snackbarMessage) {
                uiState.snackbarMessage?.let { msg ->
                    snackbarHostState.showSnackbar(msg)
                    viewModel.clearSnackbar()
                }
            }

            Get1MViewsTheme(darkTheme = uiState.isDarkMode) {
                // RTL Support as requested for Arabic-first UI
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                        val isExpanded = maxWidth >= 720.dp

                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            snackbarHost = { SnackbarHost(snackbarHostState) },
                            bottomBar = {
                                if (!isExpanded) {
                                    AppBottomNavigationBar(
                                        currentScreen = uiState.currentScreen,
                                        savedCount = savedList.size,
                                        onSelectScreen = { viewModel.setScreen(it) }
                                    )
                                }
                            }
                        ) { innerPadding ->
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
                                // Responsive Sidebar on Tablet / Desktop
                                if (isExpanded) {
                                    AppNavigationRail(
                                        currentScreen = uiState.currentScreen,
                                        savedCount = savedList.size,
                                        onSelectScreen = { viewModel.setScreen(it) }
                                    )
                                }

                                // Main Screen Content with Crossfade
                                Box(modifier = Modifier.weight(1f)) {
                                    // Handle Back button on secondary screens
                                    if (uiState.currentScreen != Screen.HOME) {
                                        BackHandler {
                                            viewModel.setScreen(Screen.HOME)
                                        }
                                    }

                                    Crossfade(
                                        targetState = uiState.currentScreen,
                                        label = "screen_transition"
                                    ) { screen ->
                                        when (screen) {
                                            Screen.HOME -> HomeScreen(
                                                uiState = uiState,
                                                viewModel = viewModel
                                            )
                                            Screen.YOUTUBE -> YouTubeScreen(
                                                uiState = uiState,
                                                viewModel = viewModel
                                            )
                                            Screen.TIKTOK -> TikTokScreen(
                                                uiState = uiState,
                                                viewModel = viewModel
                                            )
                                            Screen.INSTAGRAM -> InstagramScreen(
                                                uiState = uiState,
                                                viewModel = viewModel
                                            )
                                            Screen.FACEBOOK -> FacebookScreen(
                                                uiState = uiState,
                                                viewModel = viewModel
                                            )
                                            Screen.SAVED -> SavedScreen(
                                                uiState = uiState,
                                                viewModel = viewModel
                                            )
                                            Screen.SETTINGS -> SettingsScreen(
                                                uiState = uiState,
                                                viewModel = viewModel
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Dialogs & Modals
                        if (uiState.isGenerating) {
                            GenerationProgressDialog(
                                currentStepIndex = uiState.generationStepIndex
                            )
                        }

                        if (uiState.showIdeaModal) {
                            IdeaGeneratorModal(
                                onSelectIdea = { idea ->
                                    viewModel.setTopic(idea)
                                    viewModel.setShowIdeaModal(false)
                                },
                                onDismiss = { viewModel.setShowIdeaModal(false) }
                            )
                        }

                        if (uiState.showThumbnailStudioModal) {
                            ThumbnailPromptStudioModal(
                                initialTopic = uiState.inputTopic,
                                onCopyPrompt = { prompt ->
                                    viewModel.copyToClipboard(this@MainActivity, prompt, "Thumbnail Prompt")
                                    viewModel.setShowThumbnailStudioModal(false)
                                },
                                onDismiss = { viewModel.setShowThumbnailStudioModal(false) }
                            )
                        }

                        uiState.activeEditSection?.let { (sectionName, currentValue) ->
                            EditSectionDialog(
                                title = sectionName,
                                initialValue = currentValue,
                                onSave = { updated ->
                                    viewModel.saveEditedSection(sectionName, updated)
                                },
                                onDismiss = { viewModel.closeEditSection() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppBottomNavigationBar(
    currentScreen: Screen,
    savedCount: Int,
    onSelectScreen: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .testTag("app_bottom_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        Screen.bottomNavScreens.forEach { screen ->
            val isSelected = currentScreen == screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectScreen(screen) },
                icon = {
                    if (screen == Screen.SAVED && savedCount > 0) {
                        BadgedBox(badge = { Badge { Text("$savedCount") } }) {
                            Icon(
                                imageVector = if (isSelected) screen.iconFilled else screen.iconOutlined,
                                contentDescription = screen.titleAr
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) screen.iconFilled else screen.iconOutlined,
                            contentDescription = screen.titleAr
                        )
                    }
                },
                label = {
                    Text(
                        text = screen.titleAr,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NeonCyan,
                    selectedTextColor = NeonCyan,
                    indicatorColor = NeonCyan.copy(alpha = 0.15f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_item_${screen.route}")
            )
        }
    }
}

@Composable
fun AppNavigationRail(
    currentScreen: Screen,
    savedCount: Int,
    onSelectScreen: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier
            .fillMaxHeight()
            .testTag("app_navigation_rail"),
        containerColor = MaterialTheme.colorScheme.surface,
        header = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "1M Views",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = NeonCyan)
                )
            }
        }
    ) {
        Screen.entries.forEach { screen ->
            val isSelected = currentScreen == screen
            NavigationRailItem(
                selected = isSelected,
                onClick = { onSelectScreen(screen) },
                icon = {
                    if (screen == Screen.SAVED && savedCount > 0) {
                        BadgedBox(badge = { Badge { Text("$savedCount") } }) {
                            Icon(
                                imageVector = if (isSelected) screen.iconFilled else screen.iconOutlined,
                                contentDescription = screen.titleAr
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) screen.iconFilled else screen.iconOutlined,
                            contentDescription = screen.titleAr
                        )
                    }
                },
                label = { Text(screen.titleAr, fontSize = 11.sp) },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = NeonCyan,
                    selectedTextColor = NeonCyan,
                    indicatorColor = NeonCyan.copy(alpha = 0.15f)
                ),
                modifier = Modifier.testTag("rail_item_${screen.route}")
            )
        }
    }
}
