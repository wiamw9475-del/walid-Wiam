package com.example.ui

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.SavedProjectEntity
import com.example.data.repository.ContentRepository
import com.example.model.Audience
import com.example.model.ContentLanguage
import com.example.model.ContentPackage
import com.example.model.Platform
import com.example.model.ThumbnailStyle
import com.example.model.Tone
import com.example.ui.components.GENERATION_STEPS
import com.example.ui.navigation.Screen
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MainUiState(
    val currentScreen: Screen = Screen.HOME,
    val inputTopic: String = "",
    val selectedPlatform: Platform = Platform.YOUTUBE,
    val selectedLanguage: ContentLanguage = ContentLanguage.ARABIC,
    val selectedTone: Tone = Tone.VIRAL,
    val selectedAudience: Audience = Audience.AUTO,
    val selectedThumbnailStyle: ThumbnailStyle = ThumbnailStyle.VIRAL_YOUTUBE,

    val isGenerating: Boolean = false,
    val generationStepIndex: Int = 0,
    val currentPackage: ContentPackage? = null,
    val errorMessage: String? = null,

    val showIdeaModal: Boolean = false,
    val showThumbnailStudioModal: Boolean = false,
    val activeEditSection: Pair<String, String>? = null, // sectionName to currentValue

    val searchQuery: String = "",
    val savedFilterPlatform: Platform? = null,
    val onlyFavorites: Boolean = false,

    val isDarkMode: Boolean = true,
    val snackbarMessage: String? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContentRepository

    init {
        val db = AppDatabase.getInstance(application)
        repository = ContentRepository(db.savedProjectDao())
    }

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _searchFlow = MutableStateFlow("")
    private val _platformFilterFlow = MutableStateFlow<Platform?>(null)
    private val _onlyFavoritesFlow = MutableStateFlow(false)

    val savedProjects: StateFlow<List<SavedProjectEntity>> = combine(
        repository.allProjects,
        _searchFlow,
        _platformFilterFlow,
        _onlyFavoritesFlow
    ) { projects, query, platform, favoritesOnly ->
        projects.filter { entity ->
            val matchesQuery = query.isBlank() ||
                    entity.topic.contains(query, ignoreCase = true) ||
                    entity.recommendedTitle.contains(query, ignoreCase = true)
            val matchesPlatform = platform == null || entity.platformId.equals(platform.id, ignoreCase = true)
            val matchesFav = !favoritesOnly || entity.isFavorite

            matchesQuery && matchesPlatform && matchesFav
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var generationJob: Job? = null

    fun setScreen(screen: Screen) {
        _uiState.value = _uiState.value.copy(currentScreen = screen)
    }

    fun setTopic(topic: String) {
        _uiState.value = _uiState.value.copy(inputTopic = topic, errorMessage = null)
    }

    fun setPlatform(platform: Platform) {
        _uiState.value = _uiState.value.copy(selectedPlatform = platform)
    }

    fun setLanguage(language: ContentLanguage) {
        _uiState.value = _uiState.value.copy(selectedLanguage = language)
    }

    fun setTone(tone: Tone) {
        _uiState.value = _uiState.value.copy(selectedTone = tone)
    }

    fun setAudience(audience: Audience) {
        _uiState.value = _uiState.value.copy(selectedAudience = audience)
    }

    fun setThumbnailStyle(style: ThumbnailStyle) {
        _uiState.value = _uiState.value.copy(selectedThumbnailStyle = style)
    }

    fun toggleTheme() {
        _uiState.value = _uiState.value.copy(isDarkMode = !_uiState.value.isDarkMode)
    }

    fun setShowIdeaModal(show: Boolean) {
        _uiState.value = _uiState.value.copy(showIdeaModal = show)
    }

    fun setShowThumbnailStudioModal(show: Boolean) {
        _uiState.value = _uiState.value.copy(showThumbnailStudioModal = show)
    }

    fun openEditSection(sectionName: String, currentValue: String) {
        _uiState.value = _uiState.value.copy(activeEditSection = sectionName to currentValue)
    }

    fun closeEditSection() {
        _uiState.value = _uiState.value.copy(activeEditSection = null)
    }

    fun clearSnackbar() {
        _uiState.value = _uiState.value.copy(snackbarMessage = null)
    }

    fun showMessage(message: String) {
        _uiState.value = _uiState.value.copy(snackbarMessage = message)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        _searchFlow.value = query
    }

    fun setSavedFilterPlatform(platform: Platform?) {
        _uiState.value = _uiState.value.copy(savedFilterPlatform = platform)
        _platformFilterFlow.value = platform
    }

    fun setOnlyFavorites(onlyFav: Boolean) {
        _uiState.value = _uiState.value.copy(onlyFavorites = onlyFav)
        _onlyFavoritesFlow.value = onlyFav
    }

    fun generateContent(overrideTopic: String? = null, overridePlatform: Platform? = null) {
        val topic = overrideTopic ?: _uiState.value.inputTopic
        if (topic.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "يرجى كتابة موضوع الفيديو أولاً أو الضغط على 🎲 Give Me an Idea"
            )
            return
        }

        val platform = overridePlatform ?: _uiState.value.selectedPlatform
        val language = _uiState.value.selectedLanguage
        val tone = _uiState.value.selectedTone
        val audience = _uiState.value.selectedAudience
        val style = _uiState.value.selectedThumbnailStyle

        generationJob?.cancel()
        generationJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isGenerating = true,
                generationStepIndex = 0,
                errorMessage = null
            )

            // Step animation tracker
            val stepAnimationJob = launch {
                for (i in 0 until GENERATION_STEPS.size) {
                    _uiState.value = _uiState.value.copy(generationStepIndex = i)
                    delay(550)
                }
            }

            try {
                val pkg = repository.generateContent(
                    topic = topic,
                    platform = platform,
                    language = language,
                    tone = tone,
                    audience = audience,
                    thumbnailStyle = style
                )
                stepAnimationJob.join()
                _uiState.value = _uiState.value.copy(
                    isGenerating = false,
                    currentPackage = pkg,
                    errorMessage = null
                )
                showMessage("🚀 تم توليد حزمة ${platform.titleEn} الفيروسية بنجاح!")
            } catch (e: Exception) {
                stepAnimationJob.cancel()
                _uiState.value = _uiState.value.copy(
                    isGenerating = false,
                    errorMessage = "Something went wrong. Try again."
                )
            }
        }
    }

    fun retryGeneration() {
        generateContent()
    }

    fun regenerateSection(sectionType: String) {
        val current = _uiState.value.currentPackage ?: return
        val updated = repository.regenerateSection(current, sectionType)
        _uiState.value = _uiState.value.copy(currentPackage = updated)
        showMessage("🔄 تم إعادة توليد قسم $sectionType بنجاح")
    }

    fun regenerateEverything() {
        val current = _uiState.value.currentPackage
        if (current != null) {
            generateContent(current.topic, current.platform)
        } else {
            generateContent()
        }
    }

    fun clearResults() {
        _uiState.value = _uiState.value.copy(currentPackage = null)
        showMessage("تم مسح النتائج")
    }

    fun saveEditedSection(sectionName: String, newValue: String) {
        val current = _uiState.value.currentPackage ?: return
        val updated = when (sectionName.lowercase()) {
            "title", "عناوين", "titles" -> {
                val list = newValue.split("\n").filter { it.isNotBlank() }
                current.copy(titles = list, recommendedTitle = list.firstOrNull() ?: current.recommendedTitle)
            }
            "hook", "هوك" -> current.copy(primaryHook = newValue)
            "description", "وصف" -> current.copy(description = newValue)
            "script", "سكربت" -> current.copy(script = newValue)
            "tags", "كلمات دلالية" -> current.copy(tags = newValue.split(",").map { it.trim() })
            "hashtags", "هاشتاقات" -> current.copy(hashtags = newValue.split(" ").map { it.trim() })
            "keywords", "كلمات البحث" -> current.copy(keywords = newValue.split(",").map { it.trim() })
            "cta", "دعوة للإجراء" -> current.copy(cta = newValue)
            "thumbnail", "صورة مصغرة" -> current.copy(thumbnailPrompt = newValue)
            else -> current
        }
        _uiState.value = _uiState.value.copy(currentPackage = updated, activeEditSection = null)
        showMessage("تم حفظ التعديل بنجاح")
    }

    fun saveCurrentProject() {
        val current = _uiState.value.currentPackage ?: return
        viewModelScope.launch {
            repository.saveProject(current)
            showMessage("💾 تم حفظ المشروع في المفضلة والسجل!")
        }
    }

    fun toggleFavorite(id: Long, isFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(id, !isFav)
        }
    }

    fun deleteSavedProject(id: Long) {
        viewModelScope.launch {
            repository.deleteProject(id)
            showMessage("تم حذف المشروع من السجل")
        }
    }

    fun clearAllSaved() {
        viewModelScope.launch {
            repository.clearAll()
            showMessage("تم مسح جميع المشاريع المحفوظة")
        }
    }

    fun loadSavedProject(entity: SavedProjectEntity) {
        val pkg = entity.toContentPackage()
        _uiState.value = _uiState.value.copy(
            inputTopic = pkg.topic,
            selectedPlatform = pkg.platform,
            selectedLanguage = pkg.language,
            selectedTone = pkg.tone,
            selectedAudience = pkg.audience,
            selectedThumbnailStyle = pkg.thumbnailStyle,
            currentPackage = pkg,
            currentScreen = Screen.HOME
        )
        showMessage("تم تحميل مشروع ${pkg.topic}")
    }

    fun copyToClipboard(context: Context, text: String, label: String = "Get 1M Views") {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard?.setPrimaryClip(clip)
        showMessage("📋 تم نسخ المحتوى بنجاح!")
    }

    fun copyAllContent(context: Context) {
        val current = _uiState.value.currentPackage ?: return
        copyToClipboard(context, current.toMarkdownSummary(), "Get 1M Views Full Package")
    }
}
