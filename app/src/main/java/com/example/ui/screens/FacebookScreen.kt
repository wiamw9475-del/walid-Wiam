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
import androidx.compose.material.icons.filled.Public
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
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.ViralGold

@Composable
fun FacebookScreen(
    uiState: MainUiState,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var localTopic by remember(uiState.inputTopic) { mutableStateOf(uiState.inputTopic) }
    val scrollState = rememberScrollState()

    val currentPackage = if (uiState.currentPackage?.platform == Platform.FACEBOOK) {
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
                    colors = CardDefaults.cardColors(containerColor = FacebookBlue.copy(alpha = 0.12f)),
                    border = BorderStroke(1.dp, FacebookBlue.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = FacebookBlue,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Facebook Viral Engine 📘",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "منشور قصصي تفاعلي + نسخة قصيرة + أسئلة تفجير التعليقات + برومبت صورة",
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
                            text = "موضوع منشور فيسبوك:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = localTopic,
                            onValueChange = {
                                localTopic = it
                                viewModel.setTopic(it)
                            },
                            placeholder = { Text("مثال: تجربة شخصية في التوقف عن العمل الروتيني وبدء مشروع حر") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                viewModel.setPlatform(Platform.FACEBOOK)
                                viewModel.generateContent(localTopic, Platform.FACEBOOK)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_facebook_generate"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FacebookBlue)
                        ) {
                            Icon(imageVector = Icons.Default.RocketLaunch, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("توليد حزمة فيسبوك التفاعلية 🚀", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                if (currentPackage != null) {
                    Spacer(modifier = Modifier.height(20.dp))

                    // Post Title
                    ContentSectionCard(
                        title = "🎯 عنوان المنشور الرئيسي",
                        iconEmoji = "🎯",
                        badgeText = "Headline",
                        onCopy = { viewModel.copyToClipboard(context, currentPackage.recommendedTitle, "Post Title") },
                        onRegenerate = { viewModel.regenerateSection("titles") },
                        onEdit = { viewModel.openEditSection("Post Title", currentPackage.recommendedTitle) },
                        testTagPrefix = "fb_title"
                    ) {
                        Text(text = currentPackage.recommendedTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Discussion Question to trigger 100+ comments
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = ViralGold.copy(alpha = 0.15f)),
                        border = BorderStroke(1.dp, ViralGold.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "💬 سؤال تحفيز التعليقات والنقاش (Comment Booster):",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = ViralGold)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = currentPackage.commentTriggerQuestion,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Long Post Text
                    ContentSectionCard(
                        title = "📝 نص المنشور السردي الكامل",
                        iconEmoji = "📝",
                        badgeText = "Storytelling Format",
                        onCopy = { viewModel.copyToClipboard(context, currentPackage.description, "Post Text") },
                        onRegenerate = { viewModel.regenerateSection("description") },
                        onEdit = { viewModel.openEditSection("Post Text", currentPackage.description) },
                        testTagPrefix = "fb_desc"
                    ) {
                        Text(text = currentPackage.description, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Short Version
                    ContentSectionCard(
                        title = "⚡ النسخة القصيرة والسريعة للمنشور",
                        iconEmoji = "⚡",
                        badgeText = "Short Punchy",
                        onCopy = { viewModel.copyToClipboard(context, currentPackage.script, "Short Version") },
                        onRegenerate = { viewModel.regenerateSection("script") },
                        onEdit = { viewModel.openEditSection("Short Version", currentPackage.script) },
                        testTagPrefix = "fb_short"
                    ) {
                        Text(text = currentPackage.script, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Image Idea & Prompt
                    ContentSectionCard(
                        title = "🖼️ فكرة الصورة وبرومبت التصميم بالذكاء الاصطناعي (Image Prompt)",
                        iconEmoji = "🖼️",
                        badgeText = "Feed Stopping",
                        onCopy = { viewModel.copyToClipboard(context, currentPackage.thumbnailPrompt, "Image Prompt") },
                        onRegenerate = { viewModel.regenerateSection("thumbnail") },
                        onEdit = { viewModel.openEditSection("Image Prompt", currentPackage.thumbnailPrompt) },
                        testTagPrefix = "fb_image"
                    ) {
                        Text(text = "💡 فكرة الصورة: ${currentPackage.coverIdea}", style = MaterialTheme.typography.bodySmall, color = ViralGold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = currentPackage.thumbnailPrompt, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // CTA & Hashtags
                    ContentSectionCard(
                        title = "📣 CTA & Hashtags",
                        iconEmoji = "📣",
                        badgeText = "Engagement",
                        onCopy = {
                            val txt = "${currentPackage.cta}\n\n${currentPackage.hashtags.joinToString(" ")}"
                            viewModel.copyToClipboard(context, txt, "CTA & Hashtags")
                        },
                        onRegenerate = { viewModel.regenerateSection("hashtags") },
                        onEdit = { viewModel.openEditSection("CTA", currentPackage.cta) },
                        testTagPrefix = "fb_cta"
                    ) {
                        Text(text = currentPackage.cta, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = currentPackage.hashtags.joinToString(" "), color = NeonCyan, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
