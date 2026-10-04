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
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Star
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
import com.example.ui.theme.ViralGold
import com.example.ui.theme.YouTubeRed

@Composable
fun YouTubeScreen(
    uiState: MainUiState,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var localTopic by remember(uiState.inputTopic) { mutableStateOf(uiState.inputTopic) }
    val scrollState = rememberScrollState()

    val currentPackage = if (uiState.currentPackage?.platform == Platform.YOUTUBE) {
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
                    colors = CardDefaults.cardColors(containerColor = YouTubeRed.copy(alpha = 0.12f)),
                    border = BorderStroke(1.dp, YouTubeRed.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = YouTubeRed,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Movie,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "YouTube Viral Studio 🎬",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "عناوين محسنة لـ CTR + SEO + سكربت + برومبت صورة 16:9",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Topic Input Box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "موضوع فيديو يوتيوب:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = localTopic,
                            onValueChange = {
                                localTopic = it
                                viewModel.setTopic(it)
                            },
                            placeholder = { Text("مثال: وثائقي عن أذكى عملية احتيال في التاريخ") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                viewModel.setPlatform(Platform.YOUTUBE)
                                viewModel.generateContent(localTopic, Platform.YOUTUBE)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_yt_generate"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = YouTubeRed)
                        ) {
                            Icon(imageVector = Icons.Default.RocketLaunch, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("توليد حزمة يوتيوب كاملة 🚀", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // If content available
                if (currentPackage != null) {
                    Spacer(modifier = Modifier.height(20.dp))

                    // 1 & 2: Titles Card
                    ContentSectionCard(
                        title = "🎯 عناوين يوتيوب الـ 5 المقترحة",
                        iconEmoji = "🎯",
                        badgeText = "CTR Booster",
                        onCopy = { viewModel.copyToClipboard(context, currentPackage.titles.joinToString("\n"), "Titles") },
                        onRegenerate = { viewModel.regenerateSection("titles") },
                        onEdit = { viewModel.openEditSection("Titles", currentPackage.titles.joinToString("\n")) },
                        testTagPrefix = "yt_titles"
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            currentPackage.titles.forEachIndexed { i, title ->
                                val isBest = title == currentPackage.recommendedTitle
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isBest) YouTubeRed.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                                    border = BorderStroke(1.dp, if (isBest) YouTubeRed else Color.Transparent),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                        if (isBest) {
                                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = ViralGold, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                        }
                                        Text(text = "${i + 1}. $title", fontWeight = if (isBest) FontWeight.Bold else FontWeight.Normal)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 6: Hook
                    ContentSectionCard(
                        title = "🔥 Hook لأول 5–10 ثوانٍ لمنع الخروج",
                        iconEmoji = "🔥",
                        badgeText = "Retention 80%+",
                        onCopy = { viewModel.copyToClipboard(context, currentPackage.primaryHook, "Hook") },
                        onRegenerate = { viewModel.regenerateSection("hook") },
                        onEdit = { viewModel.openEditSection("Hook", currentPackage.primaryHook) },
                        testTagPrefix = "yt_hook"
                    ) {
                        Text(text = currentPackage.primaryHook, style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3: Long Description
                    ContentSectionCard(
                        title = "📝 وصف الفيديو الطويل مع الفصول والمصادر",
                        iconEmoji = "📝",
                        badgeText = "Timestamps Ready",
                        onCopy = { viewModel.copyToClipboard(context, currentPackage.description, "Description") },
                        onRegenerate = { viewModel.regenerateSection("description") },
                        onEdit = { viewModel.openEditSection("Description", currentPackage.description) },
                        testTagPrefix = "yt_desc"
                    ) {
                        Text(text = currentPackage.description, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 7: Script
                    ContentSectionCard(
                        title = "📜 Script كامل للفيديو مع توجيهات التصوير",
                        iconEmoji = "📜",
                        badgeText = "Full Script",
                        onCopy = { viewModel.copyToClipboard(context, currentPackage.script, "Script") },
                        onRegenerate = { viewModel.regenerateSection("script") },
                        onEdit = { viewModel.openEditSection("Script", currentPackage.script) },
                        testTagPrefix = "yt_script"
                    ) {
                        Text(text = currentPackage.script, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 9 & 10: Thumbnail text and 16:9 Prompt
                    ContentSectionCard(
                        title = "🖼️ Thumbnail Text & 16:9 AI Prompt",
                        iconEmoji = "🖼️",
                        badgeText = "16:9 High CTR",
                        onCopy = { viewModel.copyToClipboard(context, currentPackage.thumbnailPrompt, "Thumbnail Prompt") },
                        onRegenerate = { viewModel.regenerateSection("thumbnail") },
                        onEdit = { viewModel.openEditSection("Thumbnail", currentPackage.thumbnailPrompt) },
                        testTagPrefix = "yt_thumb"
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ViralGold.copy(alpha = 0.2f),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = "🔤 نص الصورة المصغرة (2-6 كلمات): ${currentPackage.thumbnailText}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = ViralGold),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Text(text = currentPackage.thumbnailPrompt, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4 & 5: SEO Keywords and Tags
                    ContentSectionCard(
                        title = "🏷️ YouTube Tags & SEO Keywords",
                        iconEmoji = "🏷️",
                        badgeText = "SEO Engine",
                        onCopy = { viewModel.copyToClipboard(context, currentPackage.tags.joinToString(", "), "Tags") },
                        onRegenerate = { viewModel.regenerateSection("tags") },
                        onEdit = { viewModel.openEditSection("Tags", currentPackage.tags.joinToString(", ")) },
                        testTagPrefix = "yt_tags"
                    ) {
                        Text(text = "العلامات (Tags): ${currentPackage.tags.joinToString(", ")}", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "كلمات البحث (SEO): ${currentPackage.keywords.joinToString(" • ")}", style = MaterialTheme.typography.bodySmall, color = NeonCyan)
                    }
                }
            }
        }
    }
}
