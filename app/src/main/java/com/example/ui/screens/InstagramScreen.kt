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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.RocketLaunch
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
import com.example.ui.theme.InstagramGradientPink
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.ViralGold

@Composable
fun InstagramScreen(
    uiState: MainUiState,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var localTopic by remember(uiState.inputTopic) { mutableStateOf(uiState.inputTopic) }
    val scrollState = rememberScrollState()

    val currentPackage = if (uiState.currentPackage?.platform == Platform.INSTAGRAM) {
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
                    colors = CardDefaults.cardColors(containerColor = InstagramGradientPink.copy(alpha = 0.12f)),
                    border = BorderStroke(1.dp, InstagramGradientPink.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = InstagramGradientPink,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Instagram Creator Hub 📸",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "ريلز أنيق + 3 نسخ كابشن + هوك بصري + غلاف استيتي احترافي",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Input Box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "موضوع محتوى انستقرام (ريلز أو كاروسيل):",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = localTopic,
                            onValueChange = {
                                localTopic = it
                                viewModel.setTopic(it)
                            },
                            placeholder = { Text("مثال: 5 عادات صباحية تصنع الفرق في يومك") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                viewModel.setPlatform(Platform.INSTAGRAM)
                                viewModel.generateContent(localTopic, Platform.INSTAGRAM)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_instagram_generate"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = InstagramGradientPink)
                        ) {
                            Icon(imageVector = Icons.Default.RocketLaunch, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("توليد حزمة انستقرام كاملة ✨", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                if (currentPackage != null) {
                    Spacer(modifier = Modifier.height(20.dp))

                    // Reel Concept & Hook
                    ContentSectionCard(
                        title = "🔥 فكرة الريل وهوك البداية",
                        iconEmoji = "🔥",
                        badgeText = "Aesthetic Reel",
                        onCopy = { viewModel.copyToClipboard(context, currentPackage.primaryHook, "Hook") },
                        onRegenerate = { viewModel.regenerateSection("hook") },
                        onEdit = { viewModel.openEditSection("Hook", currentPackage.primaryHook) },
                        testTagPrefix = "ig_hook"
                    ) {
                        Text(text = currentPackage.primaryHook, style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3 Caption Variations
                    ContentSectionCard(
                        title = "📑 3 نسخ مختلفة للكابشن (سريع، قصة، قيمة)",
                        iconEmoji = "📑",
                        badgeText = "3 Variations",
                        onCopy = { viewModel.copyToClipboard(context, currentPackage.captionVariations.joinToString("\n\n---\n\n"), "Captions") },
                        onRegenerate = { viewModel.regenerateSection("description") },
                        onEdit = { viewModel.openEditSection("Captions", currentPackage.captionVariations.joinToString("\n\n---\n\n")) },
                        testTagPrefix = "ig_captions"
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            currentPackage.captionVariations.forEachIndexed { i, cap ->
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "النسخة ${i + 1}:",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = InstagramGradientPink)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = cap, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Script
                    ContentSectionCard(
                        title = "📜 Script ريلز انستقرام القصير",
                        iconEmoji = "📜",
                        badgeText = "Reels Script",
                        onCopy = { viewModel.copyToClipboard(context, currentPackage.script, "Script") },
                        onRegenerate = { viewModel.regenerateSection("script") },
                        onEdit = { viewModel.openEditSection("Script", currentPackage.script) },
                        testTagPrefix = "ig_script"
                    ) {
                        Text(text = currentPackage.script, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Cover Text & Cover Prompt
                    ContentSectionCard(
                        title = "🖼️ Cover Text & AI Prompt (غلاف الريل 9:16)",
                        iconEmoji = "🖼️",
                        badgeText = "Editorial Look",
                        onCopy = { viewModel.copyToClipboard(context, currentPackage.thumbnailPrompt, "Cover Prompt") },
                        onRegenerate = { viewModel.regenerateSection("thumbnail") },
                        onEdit = { viewModel.openEditSection("Cover Prompt", currentPackage.thumbnailPrompt) },
                        testTagPrefix = "ig_cover"
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ViralGold.copy(alpha = 0.15f),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = "نص الغلاف (Cover Text): ${currentPackage.thumbnailText}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = ViralGold),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Text(text = currentPackage.thumbnailPrompt, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Hashtags & CTA
                    ContentSectionCard(
                        title = "#️⃣ هاشتاقات انستقرام المستهدفة والدعوة للإجراء (CTA)",
                        iconEmoji = "#️⃣",
                        badgeText = "Save & Share",
                        onCopy = {
                            val txt = "${currentPackage.cta}\n\n${currentPackage.hashtags.joinToString(" ")}"
                            viewModel.copyToClipboard(context, txt, "CTA & Hashtags")
                        },
                        onRegenerate = { viewModel.regenerateSection("hashtags") },
                        onEdit = { viewModel.openEditSection("CTA", currentPackage.cta) },
                        testTagPrefix = "ig_cta"
                    ) {
                        Text(text = "📣 الدعوة للإجراء: ${currentPackage.cta}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = currentPackage.hashtags.joinToString(" "), color = NeonCyan, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
