package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedProjectDao {

    @Query("SELECT * FROM saved_projects ORDER BY createdAt DESC")
    fun getAllProjects(): Flow<List<SavedProjectEntity>>

    @Query("SELECT * FROM saved_projects WHERE platformId = :platformId ORDER BY createdAt DESC")
    fun getProjectsByPlatform(platformId: String): Flow<List<SavedProjectEntity>>

    @Query("SELECT * FROM saved_projects WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteProjects(): Flow<List<SavedProjectEntity>>

    @Query("SELECT * FROM saved_projects WHERE topic LIKE '%' || :query || '%' OR recommendedTitle LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchProjects(query: String): Flow<List<SavedProjectEntity>>

    @Query("SELECT * FROM saved_projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: Long): SavedProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: SavedProjectEntity): Long

    @Update
    suspend fun updateProject(project: SavedProjectEntity)

    @Query("UPDATE saved_projects SET isFavorite = :isFav WHERE id = :id")
    suspend fun toggleFavorite(id: Long, isFav: Boolean)

    @Query("DELETE FROM saved_projects WHERE id = :id")
    suspend fun deleteProjectById(id: Long)

    @Query("DELETE FROM saved_projects")
    suspend fun clearAll()
}
