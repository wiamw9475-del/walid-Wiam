package com.example.model

import androidx.compose.ui.graphics.Color

enum class Platform(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val subtitleAr: String,
    val brandColor: Color,
    val iconName: String
) {
    YOUTUBE(
        id = "youtube",
        titleAr = "YouTube",
        titleEn = "YouTube",
        subtitleAr = "عناوين جذابة + SEO + سكربت + Thumbnail Prompt 16:9",
        brandColor = Color(0xFFFF0033),
        iconName = "youtube"
    ),
    TIKTOK(
        id = "tiktok",
        titleAr = "TikTok",
        titleEn = "TikTok",
        subtitleAr = "5 هوكات نارية + سكربت قصير + هاشتاقات فيروسية",
        brandColor = Color(0xFF00F2FE),
        iconName = "tiktok"
    ),
    INSTAGRAM(
        id = "instagram",
        titleAr = "Instagram",
        titleEn = "Instagram",
        subtitleAr = "ريلز + 3 نسخ كابشن + هوك + غلاف احترافي",
        brandColor = Color(0xFFE1306C),
        iconName = "instagram"
    ),
    FACEBOOK(
        id = "facebook",
        titleAr = "Facebook",
        titleEn = "Facebook",
        subtitleAr = "منشور تفاعلي + نسخة قصيرة + سؤال لزيادة التعليقات",
        brandColor = Color(0xFF1877F2),
        iconName = "facebook"
    );

    companion object {
        fun fromId(id: String): Platform = entries.find { it.id.equals(id, ignoreCase = true) } ?: YOUTUBE
    }
}

enum class ContentLanguage(
    val id: String,
    val labelAr: String,
    val labelEn: String
) {
    ARABIC("ar", "العربية (فصحى)", "Arabic"),
    MOROCCAN_DARIJA("darija", "الدارجة المغربية", "Moroccan Darija"),
    ENGLISH("en", "English", "English"),
    FRENCH("fr", "Français", "French");

    companion object {
        fun fromId(id: String): ContentLanguage = entries.find { it.id.equals(id, ignoreCase = true) } ?: ARABIC
    }
}

enum class Tone(
    val id: String,
    val labelAr: String,
    val labelEn: String,
    val emoji: String
) {
    VIRAL("viral", "Viral فيروسية", "Viral 🔥", "🔥"),
    PROFESSIONAL("professional", "احترافية موثوقة", "Professional", "💼"),
    HIGH_ENERGY("high_energy", "حماسية وسريعة", "High Energy", "⚡"),
    SIMPLE("simple", "بسيطة ومباشرة", "Simple & Clear", "✨"),
    EDUCATIONAL("educational", "تعليمية وشاملة", "Educational", "🎓");

    companion object {
        fun fromId(id: String): Tone = entries.find { it.id.equals(id, ignoreCase = true) } ?: VIRAL
    }
}

enum class Audience(
    val id: String,
    val labelAr: String,
    val labelEn: String,
    val emoji: String
) {
    AUTO("auto", "تلقائي حسب الموضوع", "Auto-detect", "🎯"),
    GENERAL("general", "جمهور عام", "General Public", "👥"),
    YOUTH("youth", "شباب (Gen Z)", "Youth & Gen Z", "⚡"),
    KIDS("kids", "أطفال وعائلة", "Kids & Family", "🎈"),
    ATHLETES("athletes", "رياضيون ورشاقة", "Athletes & Fitness", "💪"),
    TECH("tech", "تقنيون ورواد أعمال", "Tech & Creators", "💻");

    companion object {
        fun fromId(id: String): Audience = entries.find { it.id.equals(id, ignoreCase = true) } ?: AUTO
    }
}

enum class ThumbnailStyle(
    val id: String,
    val labelAr: String,
    val labelEn: String,
    val keywords: String
) {
    VIRAL_YOUTUBE("viral_yt", "Viral YouTube (مستر بيست ستايل)", "Viral YouTube", "extreme expressions, high saturation, sharp edge lighting, high contrast visual hook, bold elements"),
    PHOTOREALISTIC("photorealistic", "واقعي فوتوغرافي (Photorealistic)", "Photorealistic", "8k resolution, raw photography, natural bokeh, intricate details, Hasselblad 50mm f/1.8 lens"),
    CINEMATIC("cinematic", "سينمائي درامي (Cinematic)", "Cinematic", "anamorphic lens flare, moody dramatic teal and orange rim lighting, IMAX 70mm, atmospheric haze"),
    THREE_D("3d_render", "رندر 3D عصري (3D Pixar/Octane)", "3D Render", "Pixar style, glossy clay render, Octane 3D, subsurface scattering, playful lighting"),
    GAMING("gaming", "جيمنج وحماس (Gaming RGB)", "Gaming", "vibrant neon cyberpunk backlight, dark background, sharp neon glows, aggressive esports posture"),
    SPORTS("sports", "رياضي درامي (Sports Action)", "Sports", "frozen motion droplets, athletic tension, intense stadium floodlights, dynamic action angle"),
    NEWS("news", "أخباري عاجل (Breaking News)", "News & Documentary", "broadcast studio graphics, clean lower thirds space, dramatic documentary key light"),
    TECHNOLOGY("tech", "تقني مستقبلي (Futuristic Tech)", "Tech & AI", "sleek dark minimalism, holographic UI elements, polished glass reflections, subtle cyan highlights"),
    LUXURY("luxury", "فخم وراقي (Luxury & Wealth)", "Luxury & Gold", "gold accents, velvet blacks, studio product photography, elegant warm spotlight, high status"),
    MINIMAL("minimal", "بسيط وجذاب (Clean Minimal)", "Minimalist", "clean negative space, high contrast typography area, Apple keynote aesthetic, bold focal point");

    companion object {
        fun fromId(id: String): ThumbnailStyle = entries.find { it.id.equals(id, ignoreCase = true) } ?: VIRAL_YOUTUBE
    }
}
