package com.genome.visibilitypredictor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genome.visibilitypredictor.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// ============================================================
// UI STATE
// ============================================================
data class AnalysisUiState(
    val isLoading: Boolean = false,
    val result: GroupAnalysisResult? = null,
    val error: String? = null
)

data class SeoUiState(
    val isLoading: Boolean = false,
    val result: SeoAnalysisResult? = null,
    val error: String? = null
)

// ============================================================
// MAIN VIEWMODEL
// ============================================================
class GenomeViewModel : ViewModel() {

    private val _analysisState = MutableStateFlow(AnalysisUiState())
    val analysisState: StateFlow<AnalysisUiState> = _analysisState.asStateFlow()

    private val _seoState = MutableStateFlow(SeoUiState())
    val seoState: StateFlow<SeoUiState> = _seoState.asStateFlow()

    private val _history = MutableStateFlow<List<AnalysisHistoryItem>>(emptyList())
    val history: StateFlow<List<AnalysisHistoryItem>> = _history.asStateFlow()

    /**
     * Runs full visibility prediction with competitor analysis.
     * In production: calls your Python Streamlit backend or a REST API wrapper.
     */
    fun analyzeVisibility(request: PageAnalysisRequest) {
        viewModelScope.launch {
            _analysisState.value = AnalysisUiState(isLoading = true)
            try {
                // TODO: Replace with actual API call
                // val result = apiService.analyzeVisibility(request)
                // Simulating network delay for demo:
                kotlinx.coroutines.delay(2500)

                val mockResult = buildMockResult(request)
                _analysisState.value = AnalysisUiState(result = mockResult)

                // Save to history
                saveToHistory(request, mockResult)
            } catch (e: Exception) {
                _analysisState.value = AnalysisUiState(
                    error = "Analysis failed: ${e.localizedMessage}"
                )
            }
        }
    }

    fun analyzeSeo(request: SeoAnalysisRequest) {
        viewModelScope.launch {
            _seoState.value = SeoUiState(isLoading = true)
            try {
                kotlinx.coroutines.delay(2000)
                // TODO: replace with actual API call
                _seoState.value = SeoUiState(result = null) // fill with real result
            } catch (e: Exception) {
                _seoState.value = SeoUiState(error = e.localizedMessage)
            }
        }
    }

    fun clearError() {
        _analysisState.value = _analysisState.value.copy(error = null)
    }

    fun clearHistory() {
        _history.value = emptyList()
    }

    private fun saveToHistory(request: PageAnalysisRequest, result: GroupAnalysisResult) {
        val item = AnalysisHistoryItem(
            id = System.currentTimeMillis().toString(),
            query = request.query,
            userUrl = request.userUrl,
            visibilityTier = result.userPageResult.visibilityTier,
            pawcScore = result.userPageResult.predictedPawc,
            rank = result.userPageResult.finalRank,
            totalCompetitors = result.allResults.size,
            timestamp = System.currentTimeMillis()
        )
        _history.value = listOf(item) + _history.value
    }

    private fun buildMockResult(request: PageAnalysisRequest): GroupAnalysisResult {
        // In real app, this comes from your backend
        val userPage = PageResult(
            url = request.userUrl,
            domain = request.userUrl.substringAfter("://").substringBefore("/"),
            title = "Your Page — ${request.query}",
            isUserPage = true,
            isVisible = true,
            visibilityProbability = 0.72f,
            predictedPawc = 31.4f,
            finalRank = 2,
            visibilityTier = VisibilityTier.MEDIUM,
            baseFeatures = BaseFeatures(
                relevance = 0.68f, influence = 0.60f, uniqueness = 0.55f,
                clickProbability = 0.70f, diversity = 0.62f, wordCount = 1150
            ),
            competitiveFeatures = CompetitiveFeatures(
                subjectivePosition = 2, subjectiveCount = 3,
                qualityScore = 0.63f, pawcRank = 2, pawcPct = 0.67f
            ),
            diagnostics = PageDiagnostics(
                url = request.userUrl, domain = "", title = "", metaDescription = "",
                headingsFound = 7, bodyWordCount = 1150, structureStats = StructureStats(),
                contentFetched = true, fallbackUsed = false
            ),
            featureGaps = listOf(
                FeatureGap("Relevance", 0.68f, 0.82f, 17f, GapStatus.WARNING),
                FeatureGap("Influence", 0.60f, 0.85f, 29f, GapStatus.CRITICAL),
                FeatureGap("Diversity", 0.62f, 0.68f, 8.8f, GapStatus.GOOD)
            ),
            interpretation = "The page passes visibility eligibility. Ranked 2nd in analyzed group. Focus on improving Influence and Relevance."
        )
        return GroupAnalysisResult(
            query = request.query,
            userPageResult = userPage,
            competitorResults = emptyList(),
            allResults = listOf(userPage)
        )
    }
}

// ============================================================
// RETROFIT API SERVICE (Connect to Python FastAPI wrapper)
// ============================================================
// If you wrap the Python Streamlit backend as a FastAPI:
//
// interface GenomeApiService {
//     @POST("api/v1/analyze")
//     suspend fun analyzeVisibility(@Body request: PageAnalysisRequest): GroupAnalysisResult
//
//     @POST("api/v1/seo")
//     suspend fun analyzeSeo(@Body request: SeoAnalysisRequest): SeoAnalysisResult
// }
//
// object RetrofitClient {
//     private const val BASE_URL = "http://YOUR_BACKEND_URL:8000/"
//
//     val apiService: GenomeApiService by lazy {
//         Retrofit.Builder()
//             .baseUrl(BASE_URL)
//             .addConverterFactory(GsonConverterFactory.create())
//             .client(
//                 OkHttpClient.Builder()
//                     .connectTimeout(30, TimeUnit.SECONDS)
//                     .readTimeout(60, TimeUnit.SECONDS)
//                     .build()
//             )
//             .build()
//             .create(GenomeApiService::class.java)
//     }
// }