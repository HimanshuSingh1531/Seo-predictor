package com.genome.visibilitypredictor.data

// =============================================
// DATA MODELS — mirrors Python backend logic
// =============================================

data class PageAnalysisRequest(
    val userUrl: String,
    val query: String,
    val competitorUrls: List<String> = emptyList()
)

data class BaseFeatures(
    val relevance: Float = 0f,
    val influence: Float = 0f,
    val uniqueness: Float = 0f,
    val clickProbability: Float = 0f,
    val diversity: Float = 0f,
    val wordCount: Int = 0,
    val queryLength: Int = 0,
    val queryTypeList: Boolean = false,
    val queryTypeOpinion: Boolean = false,
    val queryTypeOther: Boolean = true
)

data class StructureStats(
    val headingCount: Int = 0,
    val distinctHeadingTypes: Int = 0,
    val h1Count: Int = 0,
    val h2Count: Int = 0,
    val h3Count: Int = 0,
    val paragraphCount: Int = 0,
    val listCount: Int = 0,
    val imageCount: Int = 0,
    val tableCount: Int = 0,
    val codeBlockCount: Int = 0,
    val blockquoteCount: Int = 0,
    val faqLikeCount: Int = 0
)

data class PageDiagnostics(
    val url: String,
    val domain: String,
    val title: String,
    val metaDescription: String,
    val headingsFound: Int,
    val bodyWordCount: Int,
    val structureStats: StructureStats,
    val contentFetched: Boolean,
    val fallbackUsed: Boolean
)

data class CompetitiveFeatures(
    val subjectivePosition: Int = 0,
    val subjectiveCount: Int = 0,
    val wcRel: Float = 1f,
    val numSources: Int = 0,
    val domainFreq: Int = 1,
    val avgPawcSource: Float = 50f,
    val influenceRank: Int = 0,
    val relevanceRank: Int = 0,
    val uniquenessRank: Int = 0,
    val clickProbRank: Int = 0,
    val diversityRank: Int = 0,
    val qualityScore: Float = 0f,
    val pawcRank: Int = 0,
    val pawcPct: Float = 0f,
    val wcXRelevance: Float = 0f
)

data class PageResult(
    val url: String,
    val domain: String,
    val title: String,
    val isUserPage: Boolean,
    val isVisible: Boolean,
    val visibilityProbability: Float,
    val predictedPawc: Float,
    val finalRank: Int,
    val visibilityTier: VisibilityTier,
    val baseFeatures: BaseFeatures,
    val competitiveFeatures: CompetitiveFeatures,
    val diagnostics: PageDiagnostics,
    val featureGaps: List<FeatureGap> = emptyList(),
    val interpretation: String = ""
)

data class FeatureGap(
    val feature: String,
    val actual: Float,
    val target: Float,
    val gapPercentage: Float,
    val status: GapStatus
)

enum class GapStatus { GOOD, WARNING, CRITICAL }
enum class VisibilityTier { HIGH, MEDIUM, LOW }

data class GroupAnalysisResult(
    val query: String,
    val userPageResult: PageResult,
    val competitorResults: List<PageResult>,
    val allResults: List<PageResult>,
    val analysisTimestamp: Long = System.currentTimeMillis()
)

data class SeoAnalysisRequest(
    val url: String,
    val targetKeyword: String
)

data class SeoMetric(
    val name: String,
    val score: Float,
    val maxScore: Float = 1f,
    val description: String,
    val status: GapStatus,
    val recommendation: String = ""
)

data class SeoAnalysisResult(
    val url: String,
    val keyword: String,
    val overallScore: Float,
    val metrics: List<SeoMetric>,
    val topIssues: List<String>,
    val quickWins: List<String>
)

// =============================================
// HISTORY
// =============================================
data class AnalysisHistoryItem(
    val id: String,
    val query: String,
    val userUrl: String,
    val visibilityTier: VisibilityTier,
    val pawcScore: Float,
    val rank: Int,
    val totalCompetitors: Int,
    val timestamp: Long
)