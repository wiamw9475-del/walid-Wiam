package com.example.model

data class ContentPackage(
    val topic: String,
    val platform: Platform,
    val language: ContentLanguage = ContentLanguage.ARABIC,
    val tone: Tone = Tone.VIRAL,
    val audience: Audience = Audience.AUTO,
    val timestamp: Long = System.currentTimeMillis(),

    // Titles
    val titles: List<String> = emptyList(),
    val recommendedTitle: String = "",

    // Hooks
    val hooks: List<String> = emptyList(),
    val primaryHook: String = "",

    // Descriptions & Scripts
    val description: String = "",
    val script: String = "",
    val cta: String = "",

    // SEO & Metadata
    val tags: List<String> = emptyList(),
    val hashtags: List<String> = emptyList(),
    val keywords: List<String> = emptyList(),

    // Thumbnail / Visual prompt
    val thumbnailPrompt: String = "",
    val thumbnailText: String = "",
    val thumbnailStyle: ThumbnailStyle = ThumbnailStyle.VIRAL_YOUTUBE,

    // Platform-specific extras
    val extraAngleIdeas: List<String> = emptyList(), // For TikTok 3 extra angles
    val captionVariations: List<String> = emptyList(), // For Instagram 3 variations
    val commentTriggerQuestion: String = "", // For Facebook engagement
    val videoDurationAdvice: String = "", // Recommended video length
    val coverIdea: String = "" // For Reels & TikTok cover
) {
    fun toMarkdownSummary(): String {
        return buildString {
            appendLine("# 🚀 حزمة المحتوى الفيروسي: $topic")
            appendLine("**المنصة:** ${platform.titleEn} | **النبرة:** ${tone.labelAr} | **الجمهور:** ${audience.labelAr}")
            appendLine("---")
            appendLine("## 🎯 العناوين المقترحة:")
            titles.forEachIndexed { index, title ->
                val star = if (title == recommendedTitle) " ⭐ [الموصى به]" else ""
                appendLine("${index + 1}. $title$star")
            }
            appendLine()
            if (hooks.isNotEmpty()) {
                appendLine("## 🔥 هوكات البداية (Hooks):")
                hooks.forEachIndexed { index, hook ->
                    appendLine("- خطاف ${index + 1}: $hook")
                }
                appendLine()
            }
            if (primaryHook.isNotEmpty() && hooks.isEmpty()) {
                appendLine("## 🔥 هوك الفيديو (Hook):")
                appendLine(primaryHook)
                appendLine()
            }
            if (description.isNotEmpty()) {
                appendLine("## 📝 الوصف (Description):")
                appendLine(description)
                appendLine()
            }
            if (script.isNotEmpty()) {
                appendLine("## 📜 السكربت (Script):")
                appendLine(script)
                appendLine()
            }
            if (cta.isNotEmpty()) {
                appendLine("## 📣 الدعوة للإجراء (CTA):")
                appendLine(cta)
                appendLine()
            }
            if (tags.isNotEmpty()) {
                appendLine("## 🏷️ الكلمات الدلالية (Tags):")
                appendLine(tags.joinToString(", "))
                appendLine()
            }
            if (hashtags.isNotEmpty()) {
                appendLine("## #️⃣ الهاشتاقات (Hashtags):")
                appendLine(hashtags.joinToString(" "))
                appendLine()
            }
            if (keywords.isNotEmpty()) {
                appendLine("## 🔎 كلمات البحث المفتاحية (SEO Keywords):")
                appendLine(keywords.joinToString(" • "))
                appendLine()
            }
            if (thumbnailText.isNotEmpty()) {
                appendLine("## 🔤 نص الصورة المصغرة (Thumbnail Text):")
                appendLine(thumbnailText)
                appendLine()
            }
            if (thumbnailPrompt.isNotEmpty()) {
                appendLine("## 🖼️ برومبت الذكاء الاصطناعي (Thumbnail AI Prompt 16:9):")
                appendLine(thumbnailPrompt)
                appendLine()
            }
            if (commentTriggerQuestion.isNotEmpty()) {
                appendLine("## 💬 سؤال تحفيز التعليقات (Facebook Question):")
                appendLine(commentTriggerQuestion)
                appendLine()
            }
            if (extraAngleIdeas.isNotEmpty()) {
                appendLine("## 💡 أفكار زوايا إضافية:")
                extraAngleIdeas.forEachIndexed { idx, idea ->
                    appendLine("${idx + 1}. $idea")
                }
                appendLine()
            }
            if (captionVariations.isNotEmpty()) {
                appendLine("## 📑 نسخ الكابشن البديلة:")
                captionVariations.forEachIndexed { idx, cap ->
                    appendLine("--- نسخة ${idx + 1} ---")
                    appendLine(cap)
                }
                appendLine()
            }
            appendLine("---")
            appendLine("تم التوليد عبر تطبيق: Get 1M Views - Turn One Idea Into Viral Content")
        }
    }
}
