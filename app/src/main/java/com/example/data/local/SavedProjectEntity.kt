package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Audience
import com.example.model.ContentLanguage
import com.example.model.ContentPackage
import com.example.model.Platform
import com.example.model.ThumbnailStyle
import com.example.model.Tone

@Entity(tableName = "saved_projects")
data class SavedProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val topic: String,
    val platformId: String,
    val languageId: String,
    val toneId: String,
    val audienceId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,

    val titlesRaw: String = "",
    val recommendedTitle: String = "",
    val hooksRaw: String = "",
    val primaryHook: String = "",
    val description: String = "",
    val script: String = "",
    val cta: String = "",
    val tagsRaw: String = "",
    val hashtagsRaw: String = "",
    val keywordsRaw: String = "",
    val thumbnailPrompt: String = "",
    val thumbnailText: String = "",
    val thumbnailStyleId: String = "",
    val extraAnglesRaw: String = "",
    val captionsRaw: String = "",
    val commentTriggerQuestion: String = "",
    val videoDurationAdvice: String = "",
    val coverIdea: String = ""
) {
    fun toContentPackage(): ContentPackage {
        val platform = Platform.fromId(platformId)
        val language = ContentLanguage.fromId(languageId)
        val tone = Tone.fromId(toneId)
        val audience = Audience.fromId(audienceId)
        val thumbnailStyle = ThumbnailStyle.fromId(thumbnailStyleId)

        return ContentPackage(
            topic = topic,
            platform = platform,
            language = language,
            tone = tone,
            audience = audience,
            timestamp = createdAt,
            titles = titlesRaw.split("\n---\n").filter { it.isNotBlank() },
            recommendedTitle = recommendedTitle,
            hooks = hooksRaw.split("\n---\n").filter { it.isNotBlank() },
            primaryHook = primaryHook,
            description = description,
            script = script,
            cta = cta,
            tags = tagsRaw.split(",").map { it.trim() }.filter { it.isNotEmpty() },
            hashtags = hashtagsRaw.split(" ").map { it.trim() }.filter { it.isNotEmpty() },
            keywords = keywordsRaw.split(",").map { it.trim() }.filter { it.isNotEmpty() },
            thumbnailPrompt = thumbnailPrompt,
            thumbnailText = thumbnailText,
            thumbnailStyle = thumbnailStyle,
            extraAngleIdeas = extraAnglesRaw.split("\n---\n").filter { it.isNotBlank() },
            captionVariations = captionsRaw.split("\n===\n").filter { it.isNotBlank() },
            commentTriggerQuestion = commentTriggerQuestion,
            videoDurationAdvice = videoDurationAdvice,
            coverIdea = coverIdea
        )
    }

    companion object {
        fun fromContentPackage(pkg: ContentPackage, isFavorite: Boolean = false): SavedProjectEntity {
            return SavedProjectEntity(
                topic = pkg.topic,
                platformId = pkg.platform.id,
                languageId = pkg.language.id,
                toneId = pkg.tone.id,
                audienceId = pkg.audience.id,
                createdAt = pkg.timestamp,
                isFavorite = isFavorite,
                titlesRaw = pkg.titles.joinToString("\n---\n"),
                recommendedTitle = pkg.recommendedTitle,
                hooksRaw = pkg.hooks.joinToString("\n---\n"),
                primaryHook = pkg.primaryHook,
                description = pkg.description,
                script = pkg.script,
                cta = pkg.cta,
                tagsRaw = pkg.tags.joinToString(","),
                hashtagsRaw = pkg.hashtags.joinToString(" "),
                keywordsRaw = pkg.keywords.joinToString(","),
                thumbnailPrompt = pkg.thumbnailPrompt,
                thumbnailText = pkg.thumbnailText,
                thumbnailStyleId = pkg.thumbnailStyle.id,
                extraAnglesRaw = pkg.extraAngleIdeas.joinToString("\n---\n"),
                captionsRaw = pkg.captionVariations.joinToString("\n===\n"),
                commentTriggerQuestion = pkg.commentTriggerQuestion,
                videoDurationAdvice = pkg.videoDurationAdvice,
                coverIdea = pkg.coverIdea
            )
        }
    }
}
