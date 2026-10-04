package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.ThumbnailStyle
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.ViralGold

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ThumbnailPromptStudioModal(
    initialTopic: String,
    onCopyPrompt: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var subjectTopic by remember { mutableStateOf(initialTopic.ifBlank { "أفضل تطبيقات الذكاء الاصطناعي 2026" }) }
    var selectedStyle by remember { mutableStateOf(ThumbnailStyle.VIRAL_YOUTUBE) }
    var customHookText by remember { mutableStateOf("صدمة لا تصدق!") }

    fun generateStudioPrompt(): String {
        val topic = subjectTopic.trim()
        return """
            Subject: Dynamic and expressive content creator interacting with a massive glowing holographic 3D symbol of ($topic), mouth open in excitement and shock.
            Background: Ultra-detailed dark background matching ($topic) theme, high-end studio depth of field blur, cinematic bokeh.
            Lighting: Sharp neon rim lighting, dual-tone key light, glowing edge highlights separating subject clearly from background.
            Composition: Asymmetrical rule of thirds composition, subject positioned on one side (occupying 40% frame) leaving the opposite side clean for high-contrast thumbnail text.
            Camera Angle: Low heroic angle, 35mm wide lens, intense direct eye contact with the viewer.
            Depth of Field: Extremely shallow f/1.8 aperture, razor-sharp focus on subject eyes and face.
            Typography Placement: Bold empty quadrant designated for punchy 2-4 word text overlay: "$customHookText".
            Color Direction: Saturated complementary color grading, high contrast, radiant neon accents.
            Visual Hook: Extreme facial reaction, glowing floating elements, viral 1M views energy.
            Visual Style: ${selectedStyle.keywords}.
            Aspect Ratio: 16:9
            Resolution: 8K UHD, masterpiece, hyper-realistic, pristine commercial YouTube thumbnail quality.
        """.trimIndent()
    }

    var generatedPrompt by remember { mutableStateOf(generateStudioPrompt()) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 10.dp,
            border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .testTag("dialog_thumbnail_studio")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "استوديو برومبت الصور المصغرة 16:9",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Topic Input
                Text(
                    text = "موضوع أو عنصر الصورة:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextField(
                    value = subjectTopic,
                    onValueChange = {
                        subjectTopic = it
                        generatedPrompt = generateStudioPrompt()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 12.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                // Style Selector Chips
                Text(
                    text = "النمط البصري (Style):",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = ViralGold
                    ),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ThumbnailStyle.entries.forEach { style ->
                        val isSelected = style == selectedStyle
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedStyle = style
                                generatedPrompt = generateStudioPrompt()
                            },
                            label = {
                                Text(
                                    text = style.labelAr,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonCyan.copy(alpha = 0.2f),
                                selectedLabelColor = NeonCyan
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Generated Prompt Box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "16:9 AI Prompt (Midjourney / Flux / Imagen):",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = NeonCyan
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = ViralGold.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "16:9 Widescreen",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = ViralGold,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = generatedPrompt,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 18.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            generatedPrompt = generateStudioPrompt()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("تجديد")
                    }

                    Button(
                        onClick = {
                            onCopyPrompt(generatedPrompt)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1.5f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            tint = androidx.compose.ui.graphics.Color(0xFF00363D),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("نسخ البرومبت", color = androidx.compose.ui.graphics.Color(0xFF00363D), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
