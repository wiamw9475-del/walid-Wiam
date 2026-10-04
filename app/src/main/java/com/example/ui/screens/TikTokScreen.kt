package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Platform
import com.example.ui.MainUiState
import com.example.ui.MainViewModel
import com.example.ui.components.ContentSectionCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.ViralGold

@Composable
fun TikTokScreen(
    uiState: MainUiState,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var localTopic by remember(uiState.inputTopic) { mutableStateOf(uiState.inputTopic) }
    val scrollState = rememberScrollState()

    val currentPackage = if (uiState.currentPackage?.platform == Platform.TIKTOK) {
        uiState.currentPackage
    } else null

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 96.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 760.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = TikTokCyan.copy(alpha = 0.12f)),
                    border = BorderStroke(1.dp, TikTokCyan.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = TikTokCyan,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Smartphone,
                                contentDescription = null,
                                tint = Color(0xFF00363D),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "TikTok Viral Lab ⚡",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "5 هوكات نارية + سكربت سريع + كابشن وهاشتاقات ForYou",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Input
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "موضوع فيديو تيك توك:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = localTopic,
                            onValueChange = {
                                localTopic = it
                                viewModel.setTopic(it)
                            },
                            placeholder = { Text("مثال: حيلة سرية في هواتف أندرويد لا يعرفها أحد") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                viewModel.setPlatform(Platform.TIKTOK)
                                viewModel.generateContent(localTopic, Platform.TIKTOK)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_tiktok_generate"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TikTokCyan, contentColor = Color(0xFF00363D))
                        ) {
                            Icon(imageVector = Icons.Default.RocketLaunch, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("توليد حزمة تيك توك الفيروسية 🚀", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                if (currentPackage != null) {
                    Spacer(modifier = Modifier.height(20.dp))

                    // 1: 5 Powerful Hooks
                    ContentSectionCard(
                        title = "🔥 5 Hooks قوية ومجربة لمنع التمرير (Stop Scrolling)",
                        iconEmoji = "🔥",
                        badgeText = "Top Priority",
                        onCopy = { viewModel.copyToClipboard(context, currentPackage.hooks.joinToString("\n"), "Hooks") },
                        onRegenerate = { viewModel.regenerateSection("hook") },
                        onEdit = { viewModel.openEditSection("Hooks", currentPackage.hooks.joinToString("\n")) },
                        testTagPrefix = "tt_hooks"
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            currentPackage.hooks.forEachIndexed { i, hook ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "خطاف ${i + 1}: $hook",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Duration Advice & Concept
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = ViralGold.copy(alpha = 0.15f)),
                        border = BorderStroke(1.dp, ViralGold.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "⏱️ المدة المقترحة للفيديو لضمان 100% Completion Rate:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = ViralGold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentPackage.videoDurationAdvice,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Script
                    ContentSectionCard(
                        title = "📜 Script سريع الوتيرة (Fast Jump-Cuts)",
                        iconEmoji = "📜",
                        badgeText = "Short Script",
                        onCopy = { viewModel.copyToClipboard(context, currentPackage.script, "Script") },
                        onRegenerate = { viewModel.regenerateSection("script") },
                        onEdit = { viewModel.openEditSection("Script", currentPackage.script) },
                        testTagPrefix = "tt_script"
                    ) {
                        Text(text = currentPackage.script, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Caption & Hashtags
                    ContentSectionCard(
                        title = "📝 Caption & Hashtags لخوارزمية For You",
                        iconEmoji = "📝",
                        badgeText = "Algorithm Ready",
                        onCopy = {
                            val full = "${currentPackage.description}\n\n${currentPackage.hashtags.joinToString(" ")}"
                            viewModel.copyToClipboard(context, full, "Caption & Hashtags")
                        },
                        onRegenerate = { viewModel.regenerateSection("description") },
                        onEdit = { viewModel.openEditSection("Caption", currentPackage.description) },
                        testTagPrefix = "tt_caption"
                    ) {
                        Text(text = currentPackage.description, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = currentPackage.hashtags.joinToString(" "), color = NeonCyan, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Cover Idea & Cover Prompt
                    ContentSectionCard(
                        title = "🖼️ فكرة غلاف التيك توك وبرومبت التصميم 9:16",
                        iconEmoji = "🖼️",
                        badgeText = "Cover Prompt",
                        onCopy = { viewModel.copyToClipboard(context, currentPackage.thumbnailPrompt, "Cover Prompt") },
                        onRegenerate = { viewModel.regenerateSection("thumbnail") },
                        onEdit = { viewModel.openEditSection("Cover Prompt", currentPackage.thumbnailPrompt) },
                        testTagPrefix = "tt_cover"
                    ) {
                        Text(text = "💡 فكرة الغلاف: ${currentPackage.coverIdea}", style = MaterialTheme.typography.bodySmall, color = ViralGold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = currentPackage.thumbnailPrompt, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3 Additional Angles
                    if (currentPackage.extraAngleIdeas.isNotEmpty()) {
                        ContentSectionCard(
                            title = "💡 3 أفكار زوايا إضافية لنفس الموضوع",
                            iconEmoji = "💡",
                            badgeText = "Content Series",
                            onCopy = { viewModel.copyToClipboard(context, currentPackage.extraAngleIdeas.joinToString("\n"), "Extra Angles") },
                            onRegenerate = { viewModel.regenerateSection("titles") },
                            onEdit = { viewModel.openEditSection("Extra Angles", currentPackage.extraAngleIdeas.joinToString("\n")) },
                            testTagPrefix = "tt_angles"
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                currentPackage.extraAngleIdeas.forEachIndexed { i, angle ->
                                    Text(text = "${i + 1}. $angle", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
