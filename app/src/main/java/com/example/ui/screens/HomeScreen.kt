package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Audience
import com.example.model.ContentLanguage
import com.example.model.ContentPackage
import com.example.model.Platform
import com.example.model.ThumbnailStyle
import com.example.model.Tone
import com.example.ui.MainUiState
import com.example.ui.MainViewModel
import com.example.ui.components.ContentSectionCard
import com.example.ui.components.HeaderBanner
import com.example.ui.components.PlatformSelectorRow
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.ViralGold

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    uiState: MainUiState,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Brand & Tagline Header
                HeaderBanner(isDark = uiState.isDarkMode)

                Spacer(modifier = Modifier.height(16.dp))

                // Topic Input Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_topic_input"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Text(
                            text = "اكتب موضوع الفيديو هنا...",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = uiState.inputTopic,
                            onValueChange = { viewModel.setTopic(it) },
                            placeholder = {
                                Text(
                                    text = "مثال: أفضل 10 تطبيقات ذكاء اصطناعي مجانية في 2026",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .testTag("input_topic_field"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Platform Selector Cards
                        PlatformSelectorRow(
                            selectedPlatform = uiState.selectedPlatform,
                            onPlatformSelected = { viewModel.setPlatform(it) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Language Selector
                        Text(
                            text = "لغة المحتوى:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            ContentLanguage.entries.forEach { lang ->
                                val isSelected = lang == uiState.selectedLanguage
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.setLanguage(lang) },
                                    label = { Text(lang.labelAr, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = NeonCyan.copy(alpha = 0.2f),
                                        selectedLabelColor = NeonCyan
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Tone Selector
                        Text(
                            text = "النبرة والأسلوب:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Tone.entries.forEach { tone ->
                                val isSelected = tone == uiState.selectedTone
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.setTone(tone) },
                                    label = { Text("${tone.emoji} ${tone.labelAr}", fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = ViralGold.copy(alpha = 0.2f),
                                        selectedLabelColor = ViralGold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Audience Selector
                        Text(
                            text = "الجمهور المستهدف:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Audience.entries.forEach { aud ->
                                val isSelected = aud == uiState.selectedAudience
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.setAudience(aud) },
                                    label = { Text("${aud.emoji} ${aud.labelAr}", fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = NeonCyan.copy(alpha = 0.2f),
                                        selectedLabelColor = NeonCyan
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Primary Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Give me an idea button
                            OutlinedButton(
                                onClick = { viewModel.setShowIdeaModal(true) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .testTag("btn_give_me_idea"),
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, ViralGold.copy(alpha = 0.7f)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ViralGold)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Casino,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "🎲 Give Me an Idea",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            // Generate Content Button
                            Button(
                                onClick = { viewModel.generateContent() },
                                modifier = Modifier
                                    .weight(1.3f)
                                    .height(52.dp)
                                    .testTag("btn_generate_content"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NeonCyan,
                                    contentColor = Color(0xFF00363D)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RocketLaunch,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "🚀 Generate Content",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Dedicated Thumbnail Studio link button
                        FilledTonalButton(
                            onClick = { viewModel.setShowThumbnailStudioModal(true) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("btn_open_thumbnail_studio"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = NeonCyan
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "🎨 مولد برومبت الصور المصغرة 16:9 الاحترافي (Thumbnail AI Studio)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Error State Display
                AnimatedVisibility(visible = uiState.errorMessage != null) {
                    uiState.errorMessage?.let { error ->
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ErrorOutline,
                                        contentDescription = "Error",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = error,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = { viewModel.retryGeneration() },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("🔄 Try Again")
                                }
                            }
                        }
                    }
                }

                // Results Section (Cards)
                AnimatedVisibility(
                    visible = uiState.currentPackage != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    uiState.currentPackage?.let { pkg ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Results Global Bar
                            ResultsHeaderBar(
                                pkg = pkg,
                                onCopyAll = { viewModel.copyAllContent(context) },
                                onRegenerateAll = { viewModel.regenerateEverything() },
                                onSave = { viewModel.saveCurrentProject() },
                                onClear = { viewModel.clearResults() }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // 🎯 Titles Card
                            ContentSectionCard(
                                title = "🎯 Titles (العناوين الفيروسية)",
                                iconEmoji = "🎯",
                                badgeText = "5 مقترحات",
                                onCopy = {
                                    val text = pkg.titles.joinToString("\n") { "- $it" }
                                    viewModel.copyToClipboard(context, text, "Titles")
                                },
                                onRegenerate = { viewModel.regenerateSection("titles") },
                                onEdit = {
                                    viewModel.openEditSection("Titles", pkg.titles.joinToString("\n"))
                                },
                                testTagPrefix = "titles"
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    pkg.titles.forEachIndexed { idx, title ->
                                        val isRecommended = title == pkg.recommendedTitle
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (isRecommended) NeonCyan.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                            border = BorderStroke(
                                                1.dp,
                                                if (isRecommended) NeonCyan.copy(alpha = 0.6f) else Color.Transparent
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                if (isRecommended) {
                                                    Icon(
                                                        imageVector = Icons.Default.Star,
                                                        contentDescription = "Recommended",
                                                        tint = ViralGold,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                }
                                                Text(
                                                    text = "${idx + 1}. $title",
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = if (isRecommended) FontWeight.Bold else FontWeight.Normal
                                                    ),
                                                    color = if (isRecommended) NeonCyan else MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // 🔥 Hook Card
                            ContentSectionCard(
                                title = "🔥 Hook (خاطف الانتباه - أول 5 إلى 10 ثوانٍ)",
                                iconEmoji = "🔥",
                                badgeText = "حاسم للانتشار",
                                onCopy = { viewModel.copyToClipboard(context, pkg.primaryHook, "Hook") },
                                onRegenerate = { viewModel.regenerateSection("hook") },
                                onEdit = { viewModel.openEditSection("Hook", pkg.primaryHook) },
                                testTagPrefix = "hook"
                            ) {
                                Text(
                                    text = pkg.primaryHook,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 15.sp,
                                        lineHeight = 22.sp,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (pkg.hooks.size > 1) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "خطافات بديلة أخرى:",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = ViralGold
                                    )
                                    pkg.hooks.drop(1).forEach { altHook ->
                                        Text(
                                            text = "• $altHook",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // 📝 Description Card
                            ContentSectionCard(
                                title = "📝 Description (الوصف المحسّن)",
                                iconEmoji = "📝",
                                badgeText = "SEO Ready",
                                onCopy = { viewModel.copyToClipboard(context, pkg.description, "Description") },
                                onRegenerate = { viewModel.regenerateSection("description") },
                                onEdit = { viewModel.openEditSection("Description", pkg.description) },
                                testTagPrefix = "description"
                            ) {
                                Text(
                                    text = pkg.description,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 13.sp,
                                        lineHeight = 20.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // 📜 Script Card
                            ContentSectionCard(
                                title = "📜 Script (السكربت وتوجيهات التصوير)",
                                iconEmoji = "📜",
                                badgeText = "Visual Cues",
                                onCopy = { viewModel.copyToClipboard(context, pkg.script, "Script") },
                                onRegenerate = { viewModel.regenerateSection("script") },
                                onEdit = { viewModel.openEditSection("Script", pkg.script) },
                                testTagPrefix = "script"
                            ) {
                                Text(
                                    text = pkg.script,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 13.sp,
                                        lineHeight = 20.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // 🖼️ Thumbnail Prompt Card
                            ContentSectionCard(
                                title = "🖼️ Thumbnail AI Prompt (وصف الصورة المصغرة 16:9)",
                                iconEmoji = "🖼️",
                                badgeText = "Midjourney / Flux",
                                onCopy = { viewModel.copyToClipboard(context, pkg.thumbnailPrompt, "Thumbnail Prompt") },
                                onRegenerate = { viewModel.regenerateSection("thumbnail") },
                                onEdit = { viewModel.openEditSection("Thumbnail", pkg.thumbnailPrompt) },
                                testTagPrefix = "thumbnail"
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    if (pkg.thumbnailText.isNotEmpty()) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = ViralGold.copy(alpha = 0.15f),
                                            border = BorderStroke(1.dp, ViralGold.copy(alpha = 0.4f))
                                        ) {
                                            Text(
                                                text = "النص المقترح على الصورة: ${pkg.thumbnailText}",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = ViralGold
                                                ),
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                            )
                                        }
                                    }

                                    Text(
                                        text = pkg.thumbnailPrompt,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 12.sp,
                                            lineHeight = 18.sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // 🏷️ Tags Card
                            ContentSectionCard(
                                title = "🏷️ Tags (الكلمات الدلالية)",
                                iconEmoji = "🏷️",
                                badgeText = "${pkg.tags.size} Tags",
                                onCopy = { viewModel.copyToClipboard(context, pkg.tags.joinToString(", "), "Tags") },
                                onRegenerate = { viewModel.regenerateSection("tags") },
                                onEdit = { viewModel.openEditSection("Tags", pkg.tags.joinToString(", ")) },
                                testTagPrefix = "tags"
                            ) {
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    pkg.tags.forEach { tag ->
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                                        ) {
                                            Text(
                                                text = tag,
                                                style = MaterialTheme.typography.bodySmall,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // #️⃣ Hashtags Card
                            ContentSectionCard(
                                title = "#️⃣ Hashtags (الهاشتاقات)",
                                iconEmoji = "#️⃣",
                                badgeText = "Trending",
                                onCopy = { viewModel.copyToClipboard(context, pkg.hashtags.joinToString(" "), "Hashtags") },
                                onRegenerate = { viewModel.regenerateSection("hashtags") },
                                onEdit = { viewModel.openEditSection("Hashtags", pkg.hashtags.joinToString(" ")) },
                                testTagPrefix = "hashtags"
                            ) {
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    pkg.hashtags.forEach { ht ->
                                        Text(
                                            text = ht,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = NeonCyan
                                            )
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // 🔎 Keywords Card
                            ContentSectionCard(
                                title = "🔎 Keywords (كلمات البحث المستهدفة)",
                                iconEmoji = "🔎",
                                badgeText = "Search Volume",
                                onCopy = { viewModel.copyToClipboard(context, pkg.keywords.joinToString(" • "), "Keywords") },
                                onRegenerate = { viewModel.regenerateSection("keywords") },
                                onEdit = { viewModel.openEditSection("Keywords", pkg.keywords.joinToString(", ")) },
                                testTagPrefix = "keywords"
                            ) {
                                Text(
                                    text = pkg.keywords.joinToString(" • "),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // 📣 CTA Card
                            ContentSectionCard(
                                title = "📣 CTA (الدعوة للإجراء)",
                                iconEmoji = "📣",
                                badgeText = "Conversion",
                                onCopy = { viewModel.copyToClipboard(context, pkg.cta, "CTA") },
                                onRegenerate = { viewModel.regenerateSection("cta") },
                                onEdit = { viewModel.openEditSection("CTA", pkg.cta) },
                                testTagPrefix = "cta"
                            ) {
                                Text(
                                    text = pkg.cta,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResultsHeaderBar(
    pkg: ContentPackage,
    onCopyAll: () -> Unit,
    onRegenerateAll: () -> Unit,
    onSave: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("results_header_bar"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "🚀 حزمة المحتوى الجاهزة للنشر",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${pkg.platform.titleEn} • ${pkg.topic}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = pkg.platform.brandColor.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, pkg.platform.brandColor)
                ) {
                    Text(
                        text = pkg.platform.titleEn,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = pkg.platform.brandColor
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Row: Copy All, Regenerate Everything, Save, Clear
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onCopyAll,
                    modifier = Modifier
                        .weight(1.2f)
                        .testTag("btn_copy_all"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null,
                        tint = Color(0xFF00363D),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("📋 Copy All", color = Color(0xFF00363D), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                FilledTonalButton(
                    onClick = onRegenerateAll,
                    modifier = Modifier
                        .weight(1.3f)
                        .testTag("btn_regen_everything"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = ViralGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("🔄 Regenerate All", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = onSave,
                    modifier = Modifier
                        .weight(0.9f)
                        .testTag("btn_save_project"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("حفظ", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = onClear,
                    modifier = Modifier.testTag("btn_clear_results"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
