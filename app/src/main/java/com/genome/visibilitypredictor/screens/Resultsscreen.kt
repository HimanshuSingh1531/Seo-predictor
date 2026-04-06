package com.genome.visibilitypredictor.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.genome.visibilitypredictor.data.*
import com.genome.visibilitypredictor.ui.components.*
import com.genome.visibilitypredictor.ui.theme.*

// ── SAMPLE DATA (mirrors Python backend output) ──
private val sampleResult = PageResult(
    url = "https://myportfolio.dev/ml-projects",
    domain = "myportfolio.dev",
    title = "Top Machine Learning Projects | My Portfolio",
    isUserPage = true,
    isVisible = true,
    visibilityProbability = 0.78f,
    predictedPawc = 34.6f,
    finalRank = 2,
    visibilityTier = VisibilityTier.MEDIUM,
    baseFeatures = BaseFeatures(
        relevance = 0.71f,
        influence = 0.62f,
        uniqueness = 0.58f,
        clickProbability = 0.74f,
        diversity = 0.66f,
        wordCount = 1240,
        queryLength = 35
    ),
    competitiveFeatures = CompetitiveFeatures(
        subjectivePosition = 2,
        subjectiveCount = 4,
        wcRel = 0.91f,
        qualityScore = 0.65f,
        pawcRank = 2,
        pawcPct = 0.75f
    ),
    diagnostics = PageDiagnostics(
        url = "https://myportfolio.dev/ml-projects",
        domain = "myportfolio.dev",
        title = "Top Machine Learning Projects | My Portfolio",
        metaDescription = "Explore real ML projects with code, benchmarks, and results.",
        headingsFound = 8,
        bodyWordCount = 1240,
        structureStats = StructureStats(
            headingCount = 8, h1Count = 1, h2Count = 5, h3Count = 2,
            paragraphCount = 12, listCount = 4, imageCount = 3,
            tableCount = 1, codeBlockCount = 2
        ),
        contentFetched = true,
        fallbackUsed = false
    ),
    featureGaps = listOf(
        FeatureGap("Relevance", 0.71f, 0.82f, 13.4f, GapStatus.WARNING),
        FeatureGap("Uniqueness", 0.58f, 0.75f, 22.7f, GapStatus.WARNING),
        FeatureGap("Influence", 0.62f, 0.85f, 27.1f, GapStatus.CRITICAL),
        FeatureGap("Diversity", 0.66f, 0.70f, 5.7f, GapStatus.GOOD),
        FeatureGap("Click Probability", 0.74f, 0.79f, 6.3f, GapStatus.GOOD)
    ),
    interpretation = "The page is likely to be considered visible but its competitive strength is moderate. Ranked 2nd out of 4 analyzed pages."
)

private val sampleCompetitors = listOf(
    PageResult(
        url = "https://towardsdatascience.com/ml-projects-2024",
        domain = "towardsdatascience.com",
        title = "50 Machine Learning Projects for Beginners",
        isUserPage = false,
        isVisible = true,
        visibilityProbability = 0.94f,
        predictedPawc = 67.2f,
        finalRank = 1,
        visibilityTier = VisibilityTier.HIGH,
        baseFeatures = BaseFeatures(relevance = 0.87f, influence = 0.88f, uniqueness = 0.79f, clickProbability = 0.91f, diversity = 0.82f, wordCount = 2840),
        competitiveFeatures = CompetitiveFeatures(),
        diagnostics = PageDiagnostics("", "towardsdatascience.com", "", "", 0, 0, StructureStats(), true, false)
    ),
    PageResult(
        url = "https://github.com/ml-projects-list",
        domain = "github.com",
        title = "Awesome Machine Learning Projects List",
        isUserPage = false,
        isVisible = true,
        visibilityProbability = 0.69f,
        predictedPawc = 22.1f,
        finalRank = 3,
        visibilityTier = VisibilityTier.MEDIUM,
        baseFeatures = BaseFeatures(relevance = 0.64f, influence = 0.80f, uniqueness = 0.52f, clickProbability = 0.66f, diversity = 0.45f, wordCount = 680),
        competitiveFeatures = CompetitiveFeatures(),
        diagnostics = PageDiagnostics("", "github.com", "", "", 0, 0, StructureStats(), true, false)
    ),
    PageResult(
        url = "https://randomsite.io/ml",
        domain = "randomsite.io",
        title = "ML Projects",
        isUserPage = false,
        isVisible = false,
        visibilityProbability = 0.31f,
        predictedPawc = 4.3f,
        finalRank = 4,
        visibilityTier = VisibilityTier.LOW,
        baseFeatures = BaseFeatures(relevance = 0.38f, influence = 0.44f, uniqueness = 0.40f, clickProbability = 0.35f, diversity = 0.28f, wordCount = 310),
        competitiveFeatures = CompetitiveFeatures(),
        diagnostics = PageDiagnostics("", "randomsite.io", "", "", 0, 0, StructureStats(), true, false)
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = BgSurface,
        topBar = {
            GenomeTopBar(
                title = "Analysis Results",
                subtitle = "best machine learning projects",
                showBack = true,
                onBack = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Download, contentDescription = "Export", tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // ── HERO RESULT CARD ──
            ResultHeroCard(result = sampleResult)

            // ── TABS ──
            val tabs = listOf("Overview", "Features", "Competitors", "Gaps")
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = BgCard,
                contentColor = GenomePrimary,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = GenomePrimary,
                            height = 3.dp
                        )
                    }
                },
                divider = { HorizontalDivider(color = BorderLight) }
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = tab,
                                fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }

            // ── TAB CONTENT ──
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (selectedTab) {
                    0 -> OverviewTab(result = sampleResult, competitors = sampleCompetitors)
                    1 -> FeaturesTab(result = sampleResult)
                    2 -> CompetitorsTab(allResults = listOf(sampleResult) + sampleCompetitors)
                    3 -> GapsTab(result = sampleResult)
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

// =============================================
// HERO CARD
// =============================================
@Composable
private fun ResultHeroCard(result: PageResult) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = when (result.visibilityTier) {
                        VisibilityTier.HIGH   -> listOf(Color(0xFF065F46), Color(0xFF10B981))
                        VisibilityTier.MEDIUM -> listOf(Color(0xFF92400E), Color(0xFFF59E0B))
                        VisibilityTier.LOW    -> listOf(Color(0xFF7F1D1D), Color(0xFFEF4444))
                    }
                )
            )
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(Color.White.copy(alpha = 0.05f), 120f, Offset(size.width + 30f, -30f))
            drawCircle(Color.White.copy(alpha = 0.03f), 80f, Offset(-20f, size.height * 0.8f))
        }
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = result.domain,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Text(
                        text = result.title.take(50) + if (result.title.length > 50) "..." else "",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 22.sp
                    )
                }
                Spacer(Modifier.width(12.dp))
                // PAWC Score circle
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = String.format("%.1f", result.predictedPawc),
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "PAWC",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 9.sp
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VisibilityTierChip(result.visibilityTier)
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Rank #${result.finalRank} of 4",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "${(result.visibilityProbability * 100).toInt()}% visible",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// =============================================
// OVERVIEW TAB
// =============================================
@Composable
private fun OverviewTab(result: PageResult, competitors: List<PageResult>) {
    // Mini stat cards
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        StatMiniCard(
            value = "#${result.finalRank}",
            label = "Rank",
            icon = Icons.Default.Leaderboard,
            tint = GenomePrimary,
            modifier = Modifier.weight(1f)
        )
        StatMiniCard(
            value = "${result.diagnostics.bodyWordCount}",
            label = "Word Count",
            icon = Icons.Default.TextFields,
            tint = GenomeAccent,
            modifier = Modifier.weight(1f)
        )
        StatMiniCard(
            value = "${result.diagnostics.headingsFound}",
            label = "Headings",
            icon = Icons.Default.FormatListBulleted,
            tint = GenomePurple,
            modifier = Modifier.weight(1f)
        )
    }

    // Interpretation
    Surface(
        color = GenomePrimary.copy(alpha = 0.06f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(GenomePrimary.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Psychology, contentDescription = null, tint = GenomePrimary, modifier = Modifier.size(18.dp))
            }
            Column {
                Text(
                    text = "AI Interpretation",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = GenomePrimary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = result.interpretation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    lineHeight = 22.sp
                )
            }
        }
    }

    // Quality Score visual
    SectionHeader(title = "Quality Score Breakdown")
    Surface(
        color = BgCard,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ScoreRing(
                    score = result.baseFeatures.relevance,
                    label = "Relevance",
                    color = GenomePrimary,
                    size = 80.dp
                )
                ScoreRing(
                    score = result.baseFeatures.influence,
                    label = "Influence",
                    color = GenomePurple,
                    size = 80.dp
                )
                ScoreRing(
                    score = result.baseFeatures.uniqueness,
                    label = "Uniqueness",
                    color = GenomeAccent,
                    size = 80.dp
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ScoreRing(
                    score = result.baseFeatures.diversity,
                    label = "Diversity",
                    color = GenomeWarning,
                    size = 80.dp
                )
                ScoreRing(
                    score = result.baseFeatures.clickProbability,
                    label = "Click Prob.",
                    color = GenomeSuccess,
                    size = 80.dp
                )
                ScoreRing(
                    score = result.competitiveFeatures.qualityScore,
                    label = "Quality",
                    color = GenomeDanger,
                    size = 80.dp
                )
            }
        }
    }

    // Page structure info
    SectionHeader(title = "Page Diagnostics")
    val d = result.diagnostics
    val s = d.structureStats
    Surface(
        color = BgCard,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            DiagRow("Title", d.title.ifEmpty { "Not found" })
            HorizontalDivider(color = BorderLight, modifier = Modifier.padding(vertical = 8.dp))
            DiagRow("Meta Description", if (d.metaDescription.isNotEmpty()) d.metaDescription.take(80) + "…" else "Not found")
            HorizontalDivider(color = BorderLight, modifier = Modifier.padding(vertical = 8.dp))
            DiagRow("Content Fetched", if (d.contentFetched) "✓ Yes" else "✗ No (fallback used)")
            HorizontalDivider(color = BorderLight, modifier = Modifier.padding(vertical = 8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    "H1: ${s.h1Count}" to GenomePrimary,
                    "H2: ${s.h2Count}" to GenomeAccent,
                    "H3: ${s.h3Count}" to GenomePurple,
                    "Lists: ${s.listCount}" to GenomeWarning,
                    "Images: ${s.imageCount}" to GenomeSuccess,
                    "Code: ${s.codeBlockCount}" to GenomeDanger
                ).forEach { (text, color) ->
                    Surface(
                        color = color.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodySmall,
                            color = color,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DiagRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
    }
}

// =============================================
// FEATURES TAB
// =============================================
@Composable
private fun FeaturesTab(result: PageResult) {
    SectionHeader(
        title = "Feature Scores",
        subtitle = "Extracted from your page content"
    )

    Surface(
        color = BgCard,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FeatureBar("Relevance", result.baseFeatures.relevance, GenomePrimary)
            FeatureBar("Influence", result.baseFeatures.influence, GenomePurple)
            FeatureBar("Uniqueness", result.baseFeatures.uniqueness, GenomeAccent)
            FeatureBar("Click Probability", result.baseFeatures.clickProbability, GenomeSuccess)
            FeatureBar("Diversity", result.baseFeatures.diversity, GenomeWarning)
        }
    }

    SectionHeader(title = "Competitive Features", subtitle = "Computed relative to analyzed group")
    Surface(
        color = BgCard,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            val cf = result.competitiveFeatures
            CompFeatureRow("Position in Group", "#${cf.subjectivePosition} / ${cf.subjectiveCount}")
            CompFeatureRow("WC Relative to Avg", String.format("%.2fx", cf.wcRel))
            CompFeatureRow("PAWC Percentile", "${(cf.pawcPct * 100).toInt()}th")
            CompFeatureRow("Quality Score", String.format("%.2f", cf.qualityScore))
            CompFeatureRow("Relevance Rank", "#${cf.relevanceRank}")
            CompFeatureRow("Influence Rank", "#${cf.influenceRank}")
        }
    }

    SectionHeader(title = "Feature Provenance", subtitle = "How each score is computed")
    val provenance = listOf(
        "Relevance" to "Query overlap with title (30%), headings (18%), body (16%) + phrase match",
        "Influence" to "Domain pattern heuristic + HTTPS + structure signals",
        "Uniqueness" to "Lexical diversity, originality markers, number density",
        "Click Prob." to "Title/meta alignment + query intent + length optimization",
        "Diversity" to "Headings, lists, images, tables, code blocks, FAQ count"
    )
    Surface(
        color = BgSubtle,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            provenance.forEach { (feature, desc) ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
                    Text(
                        text = feature,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = GenomePrimary,
                        modifier = Modifier.width(80.dp)
                    )
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (feature != "Diversity") HorizontalDivider(color = BorderLight)
            }
        }
    }
}

@Composable
private fun CompFeatureRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        Text(value, style = MaterialTheme.typography.labelLarge, color = TextPrimary, fontWeight = FontWeight.SemiBold)
    }
}

// =============================================
// COMPETITORS TAB
// =============================================
@Composable
private fun CompetitorsTab(allResults: List<PageResult>) {
    SectionHeader(
        title = "Ranking Table",
        subtitle = "${allResults.size} pages analyzed"
    )
    allResults.sortedBy { it.finalRank }.forEach { result ->
        CompetitorCard(result = result)
    }
}

@Composable
private fun CompetitorCard(result: PageResult) {
    val isUser = result.isUserPage
    Surface(
        color = if (isUser) GenomePrimary.copy(alpha = 0.05f) else BgCard,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = if (isUser) 2.dp else 1.dp,
            color = if (isUser) GenomePrimary.copy(alpha = 0.3f) else BorderLight
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank badge
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(
                        color = when (result.finalRank) {
                            1 -> Color(0xFFFEF3C7)
                            2 -> Color(0xFFE0E7FF)
                            else -> BgSubtle
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#${result.finalRank}",
                    style = MaterialTheme.typography.labelLarge,
                    color = when (result.finalRank) {
                        1 -> Color(0xFFD97706)
                        2 -> GenomePrimary
                        else -> TextSecondary
                    },
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = result.domain,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    if (isUser) {
                        Surface(
                            color = GenomePrimary,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "YOU",
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Text(
                    text = result.title.take(45) + if (result.title.length > 45) "…" else "",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    VisibilityTierChip(result.visibilityTier)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = String.format("%.1f", result.predictedPawc),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = when (result.visibilityTier) {
                        VisibilityTier.HIGH -> GenomeSuccess
                        VisibilityTier.MEDIUM -> GenomeWarning
                        VisibilityTier.LOW -> GenomeDanger
                    }
                )
                Text("PAWC", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                Text(
                    text = "${(result.visibilityProbability * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// =============================================
// GAPS TAB
// =============================================
@Composable
private fun GapsTab(result: PageResult) {
    SectionHeader(
        title = "Feature Gap Analysis",
        subtitle = "Where to improve to boost visibility"
    )

    val critical = result.featureGaps.filter { it.status == GapStatus.CRITICAL }
    val warnings = result.featureGaps.filter { it.status == GapStatus.WARNING }
    val good = result.featureGaps.filter { it.status == GapStatus.GOOD }

    if (critical.isNotEmpty()) {
        GapGroup("Critical Gaps", critical, GenomeDanger)
    }
    if (warnings.isNotEmpty()) {
        GapGroup("Needs Attention", warnings, GenomeWarning)
    }
    if (good.isNotEmpty()) {
        GapGroup("On Track", good, GenomeSuccess)
    }

    // Recommendations
    SectionHeader(title = "Quick Wins", subtitle = "High-impact actions to take")
    Surface(
        color = BgCard,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val wins = listOf(
                "Add more exact and phrase matches of 'best machine learning projects' in headings and first 200 words",
                "Build 2–3 quality backlinks from domain authority sites (.edu, .org, major tech blogs)",
                "Add unique data — benchmarks, accuracy tables, or experiment results to boost Uniqueness score",
                "Include structured FAQ section with at least 4–6 common questions",
                "Optimize meta description to 130–155 characters with keyword and CTA"
            )
            wins.forEachIndexed { idx, win ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(GenomePrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${idx + 1}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                    Text(
                        text = win,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        lineHeight = 20.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (idx < wins.lastIndex) HorizontalDivider(color = BorderLight)
            }
        }
    }
}

@Composable
private fun GapGroup(title: String, gaps: List<FeatureGap>, color: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color, CircleShape)
            )
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        }
        gaps.forEach { gap ->
            GapCard(gap = gap)
        }
    }
}

@Composable
private fun GapCard(gap: FeatureGap) {
    val color = when (gap.status) {
        GapStatus.GOOD -> GenomeSuccess
        GapStatus.WARNING -> GenomeWarning
        GapStatus.CRITICAL -> GenomeDanger
    }
    Surface(
        color = color.copy(alpha = 0.06f),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    GapStatusIcon(gap.status)
                    Text(gap.feature, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                }
                Text(
                    text = if (gap.status == GapStatus.GOOD) "✓ On Track" else "-${gap.gapPercentage.toInt()}%",
                    style = MaterialTheme.typography.labelLarge,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Current", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    Text("${(gap.actual * 100).toInt()}%", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                }
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = TextMuted)
                Column(horizontalAlignment = Alignment.End) {
                    Text("Target (75th pct)", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    Text("${(gap.target * 100).toInt()}%", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = color)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(color.copy(alpha = 0.15f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(gap.actual)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(50))
                        .background(color)
                )
            }
        }
    }
}