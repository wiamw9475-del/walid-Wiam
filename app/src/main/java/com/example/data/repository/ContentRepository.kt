package com.example.data.repository

import com.example.data.local.SavedProjectDao
import com.example.data.local.SavedProjectEntity
import com.example.data.remote.ViralContentGenerator
import com.example.model.Audience
import com.example.model.ContentLanguage
import com.example.model.ContentPackage
import com.example.model.Platform
import com.example.model.ThumbnailStyle
import com.example.model.Tone
import kotlinx.coroutines.flow.Flow

class ContentRepository(
    private val savedProjectDao: SavedProjectDao
) {
    val allProjects: Flow<List<SavedProjectEntity>> = savedProjectDao.getAllProjects()
    val favoriteProjects: Flow<List<SavedProjectEntity>> = savedProjectDao.getFavoriteProjects()

    fun searchProjects(query: String): Flow<List<SavedProjectEntity>> {
        return if (query.isBlank()) {
            savedProjectDao.getAllProjects()
        } else {
            savedProjectDao.searchProjects(query.trim())
        }
    }

    fun getProjectsByPlatform(platform: Platform): Flow<List<SavedProjectEntity>> {
        return savedProjectDao.getProjectsByPlatform(platform.id)
    }

    suspend fun saveProject(pkg: ContentPackage, isFavorite: Boolean = false): Long {
        val entity = SavedProjectEntity.fromContentPackage(pkg, isFavorite)
        return savedProjectDao.insertProject(entity)
    }

    suspend fun updateProject(entity: SavedProjectEntity) {
        savedProjectDao.updateProject(entity)
    }

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        savedProjectDao.toggleFavorite(id, isFavorite)
    }

    suspend fun deleteProject(id: Long) {
        savedProjectDao.deleteProjectById(id)
    }

    suspend fun clearAll() {
        savedProjectDao.clearAll()
    }

    suspend fun generateContent(
        topic: String,
        platform: Platform,
        language: ContentLanguage,
        tone: Tone,
        audience: Audience,
        thumbnailStyle: ThumbnailStyle = ThumbnailStyle.VIRAL_YOUTUBE
    ): ContentPackage {
        return ViralContentGenerator.generatePackage(topic, platform, language, tone, audience, thumbnailStyle)
    }

    fun regenerateSection(pkg: ContentPackage, sectionType: String): ContentPackage {
        return ViralContentGenerator.regenerateSection(pkg, sectionType)
    }
}
