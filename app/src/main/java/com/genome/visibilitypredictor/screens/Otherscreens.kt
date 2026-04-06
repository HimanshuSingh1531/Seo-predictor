package com.genome.visibilitypredictor.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.genome.visibilitypredictor.data.*
import com.genome.visibilitypredictor.ui.components.*
import com.genome.visibilitypredictor.ui.theme.*

// =============================================
// SEO ANALYZER SCREEN
// =============================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeoAnalyzeScreen(navController: NavController) {
    var url by remember { mutableStateOf("") }
    var keyword by remember { mutableStateOf("") }
    var showResults by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Sample SEO result data
    val seoResult = remember {
        SeoAnalysisResult(
            url = "https://mysite.com/page",
            keyword = "machine learning projects",
            overallScore = 0.63f,
            metrics = listOf(
                SeoMetric("Title Tag", 0.80f, description = "Title contains keyword near front", status = GapStatus.GOOD, recommendation = "Good! Keep title under 60 chars."),
                SeoMetric("Meta Description", 0.45f, description = "Meta desc is present but lacks keyword", status = GapStatus.WARNING, recommendation = "Add target keyword naturally in meta description"),
                SeoMetric("H1 Usage", 1.0f, description = "Single H1 found with keyword", status = GapStatus.GOOD),
                SeoMetric("H2/H3 Structure", 0.60f, description = "Some subheadings found", status = GapStatus.WARNING, recommendation = "Add 3–5 more H2s covering query subtopics"),
                SeoMetric("Keyword Density", 0.72f, description = "Keyword appears 8 times", status = GapStatus.GOOD),
                SeoMetric("Word Count", 0.55f, description = "1240 words — below top competitor avg (2100)", status = GapStatus.WARNING, recommendation = "Aim for 1800–2500 words for this topic"),
                SeoMetric("Image Alt Text", 0.30f, description = "Only 1 of 3 images have alt text", status = GapStatus.CRITICAL, recommendation = "Add keyword-relevant alt text to all images"),
                SeoMetric("Internal Links", 0.40f, description = "2 internal links found", status = GapStatus.CRITICAL, recommendation = "Add 5–8 contextual internal links"),
                SeoMetric("Page Speed Signal", 0.70f, description = "Lightweight page — likely fast", status = GapStatus.GOOD),
                SeoMetric("Structured Data", 0.0f, description = "No schema markup found", status = GapStatus.CRITICAL, recommendation = "Add Article or HowTo schema for rich results"),
            ),
            topIssues = listOf(
                "No structured data / schema markup",
                "Missing alt text on 2 images",
                "Only 2 internal links — build more topic clusters",
                "Meta description doesn't include target keyword"
            ),
            quickWins = listOf(
                "Add alt text to images (15 min fix, high impact)",
                "Insert target keyword into meta description",
                "Add FAQ schema markup (Article schema is easy to implement)",
                "Link 3–5 related posts internally"
            )
        )
    }

    Scaffold(
        containerColor = BgSurface,
        topBar = {
            GenomeTopBar(
                title = "SEO Analyzer",
                subtitle = "Google + LLM Signal Analysis",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (!showResults) {
                // Input Form
                Surface(
                    color = BgCard,
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, BorderLight)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text("Analyze SEO Signals", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        HorizontalDivider(color = BorderLight)
                        GenomeUrlField(value = url, onValueChange = { url = it }, label = "Page URL *")
                        OutlinedTextField(
                            value = keyword,
                            onValueChange = { keyword = it },
                            label = { Text("Target Keyword *") },
                            placeholder = { Text("e.g. machine learning projects", color = TextMuted) },
                            leadingIcon = { Icon(Icons.Default.Key, contentDescription = null, tint = GenomePurple) },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GenomePurple,
                                unfocusedBorderColor = BorderLight,
                                focusedLabelColor = GenomePurple,
                                cursorColor = GenomePurple
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Button(
                            onClick = { showResults = true },
                            enabled = url.isNotBlank() && keyword.isNotBlank(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GenomePurple),
                            modifier = Modifier.fillMaxWidth().height(52.dp)
                        ) {
                            Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Run SEO Analysis", fontWeight = FontWeight.SemiBold)
                        }
                        // Demo
                        TextButton(onClick = { showResults = true }, modifier = Modifier.fillMaxWidth()) {
                            Text("View Demo Results", color = GenomePurple)
                        }
                    }
                }

                // What it checks
                Surface(color = BgSubtle, shape = RoundedCornerShape(14.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Checks performed", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        listOf(
                            "Title tag optimization",
                            "Meta description quality",
                            "H1/H2/H3 heading structure",
                            "Keyword density & placement",
                            "Word count vs competitors",
                            "Image alt text",
                            "Internal linking",
                            "Structured data / Schema",
                            "Page speed signals"
                        ).forEach { item ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GenomePurple, modifier = Modifier.size(14.dp))
                                Text(item, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                        }
                    }
                }
            } else {
                // SEO Results
                SeoScoreHero(result = seoResult)
                SeoIssues(issues = seoResult.topIssues)
                SeoQuickWins(wins = seoResult.quickWins)
                SeoMetricsList(metrics = seoResult.metrics)
                TextButton(onClick = { showResults = false }, modifier = Modifier.fillMaxWidth()) {
                    Text("← New Analysis", color = GenomePurple)
                }
            }
        }
    }
}

@Composable
private fun SeoScoreHero(result: SeoAnalysisResult) {
    val score = (result.overallScore * 100).toInt()
    val (label, color) = when {
        score >= 80 -> "Strong" to GenomeSuccess
        score >= 60 -> "Moderate" to GenomeWarning
        else -> "Needs Work" to GenomeDanger
    }
    Surface(
        color = color.copy(alpha = 0.08f),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScoreRing(score = result.overallScore, label = "SEO\nScore", size = 90.dp, strokeWidth = 10.dp, color = color)
            Column {
                Text("Overall SEO Health", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(label, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = color)
                Text("$score / 100", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "${result.metrics.count { it.status == GapStatus.GOOD }} checks passed  •  ${result.metrics.count { it.status == GapStatus.CRITICAL }} critical",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }
    }
}

@Composable
private fun SeoIssues(issues: List<String>) {
    SectionHeader(title = "Top Issues", subtitle = "Fix these first")
    Surface(color = GenomeDanger.copy(alpha = 0.05f), shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, GenomeDanger.copy(alpha = 0.15f))) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            issues.forEachIndexed { i, issue ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = GenomeDanger, modifier = Modifier.size(16.dp))
                    Text(issue, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, lineHeight = 20.sp, modifier = Modifier.weight(1f))
                }
                if (i < issues.lastIndex) HorizontalDivider(color = GenomeDanger.copy(alpha = 0.1f))
            }
        }
    }
}

@Composable
private fun SeoQuickWins(wins: List<String>) {
    SectionHeader(title = "Quick Wins", subtitle = "Easy improvements with big impact")
    Surface(color = GenomeSuccess.copy(alpha = 0.05f), shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, GenomeSuccess.copy(alpha = 0.15f))) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            wins.forEachIndexed { i, win ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.TipsAndUpdates, contentDescription = null, tint = GenomeSuccess, modifier = Modifier.size(16.dp))
                    Text(win, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, lineHeight = 20.sp, modifier = Modifier.weight(1f))
                }
                if (i < wins.lastIndex) HorizontalDivider(color = GenomeSuccess.copy(alpha = 0.1f))
            }
        }
    }
}

@Composable
private fun SeoMetricsList(metrics: List<SeoMetric>) {
    SectionHeader(title = "All Metrics")
    metrics.forEach { metric ->
        val color = when (metric.status) {
            GapStatus.GOOD -> GenomeSuccess
            GapStatus.WARNING -> GenomeWarning
            GapStatus.CRITICAL -> GenomeDanger
        }
        Surface(
            color = BgCard,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, BorderLight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        GapStatusIcon(metric.status)
                        Text(metric.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    }
                    Text("${(metric.score * 100).toInt()}%", style = MaterialTheme.typography.labelLarge, color = color, fontWeight = FontWeight.Bold)
                }
                FeatureBar(name = "", value = metric.score, color = color)
                Text(metric.description, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                if (metric.recommendation.isNotEmpty()) {
                    Surface(color = color.copy(alpha = 0.08f), shape = RoundedCornerShape(8.dp)) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = color, modifier = Modifier.size(13.dp))
                            Text(metric.recommendation, style = MaterialTheme.typography.bodySmall, color = color, lineHeight = 17.sp)
                        }
                    }
                }
            }
        }
    }
}

// =============================================
// HISTORY SCREEN
// =============================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    val history = remember {
        listOf(
            AnalysisHistoryItem("1", "best machine learning projects", "myportfolio.dev/ml", VisibilityTier.MEDIUM, 34.6f, 2, 4, System.currentTimeMillis() - 3600000),
            AnalysisHistoryItem("2", "how to build a portfolio website", "myportfolio.dev", VisibilityTier.HIGH, 68.2f, 1, 3, System.currentTimeMillis() - 86400000),
            AnalysisHistoryItem("3", "python data science tutorial", "myblog.dev/ds", VisibilityTier.LOW, 7.4f, 5, 5, System.currentTimeMillis() - 172800000),
            AnalysisHistoryItem("4", "AI projects for beginners", "myportfolio.dev/ai", VisibilityTier.MEDIUM, 28.9f, 3, 4, System.currentTimeMillis() - 259200000),
        )
    }

    Scaffold(
        containerColor = BgSurface,
        topBar = {
            GenomeTopBar(
                title = "Analysis History",
                subtitle = "${history.size} past analyses",
                showBack = true,
                onBack = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Summary stats
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatMiniCard(
                    value = history.size.toString(),
                    label = "Total Analyses",
                    icon = Icons.Default.Analytics,
                    tint = GenomePrimary,
                    modifier = Modifier.weight(1f)
                )
                StatMiniCard(
                    value = history.count { it.visibilityTier == VisibilityTier.HIGH }.toString(),
                    label = "High Visibility",
                    icon = Icons.Default.TrendingUp,
                    tint = GenomeSuccess,
                    modifier = Modifier.weight(1f)
                )
                StatMiniCard(
                    value = String.format("%.0f", history.map { it.pawcScore }.average()),
                    label = "Avg PAWC",
                    icon = Icons.Default.Star,
                    tint = GenomeWarning,
                    modifier = Modifier.weight(1f)
                )
            }

            SectionHeader(title = "Recent Analyses")

            history.forEach { item ->
                HistoryItemCard(item = item, onClick = { navController.navigate("results") })
            }
        }
    }
}

@Composable
private fun HistoryItemCard(item: AnalysisHistoryItem, onClick: () -> Unit) {
    val (color, icon) = when (item.visibilityTier) {
        VisibilityTier.HIGH -> GenomeSuccess to Icons.Default.TrendingUp
        VisibilityTier.MEDIUM -> GenomeWarning to Icons.Default.TrendingFlat
        VisibilityTier.LOW -> GenomeDanger to Icons.Default.TrendingDown
    }
    val hoursAgo = ((System.currentTimeMillis() - item.timestamp) / 3600000).toInt()
    val timeLabel = when {
        hoursAgo < 1 -> "Just now"
        hoursAgo < 24 -> "${hoursAgo}h ago"
        else -> "${hoursAgo / 24}d ago"
    }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = BorderStroke(1.dp, BorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(color.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.query,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Text(
                    text = item.userUrl,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = color.copy(alpha = 0.1f), shape = RoundedCornerShape(6.dp)) {
                        Text(
                            text = "Rank #${item.rank}/${item.totalCompetitors}",
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = color,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text("• $timeLabel", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = String.format("%.1f", item.pawcScore),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text("PAWC", style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
        }
    }
}

// =============================================
// SETTINGS SCREEN
// =============================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var fetchTimeout by remember { mutableIntStateOf(12) }
    var maxCompetitors by remember { mutableIntStateOf(5) }
    var showProvenance by remember { mutableStateOf(true) }
    var autoDetectQueryType by remember { mutableStateOf(true) }
    var fallbackOnError by remember { mutableStateOf(true) }

    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = BgSurface,
        topBar = {
            GenomeTopBar(
                title = "Settings",
                subtitle = "Configure GENOME Predictor",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // App info
            Surface(
                color = GenomePrimary.copy(alpha = 0.06f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                brush = Brush.linearGradient(listOf(GenomePrimary, GenomeAccent)),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Analytics, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                    }
                    Column {
                        Text("GENOME Visibility Predictor", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Version 2.0.0  •  LLM + Google SEO", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    }
                }
            }

            SettingGroup(title = "Analysis Settings") {
                SettingSlider(
                    label = "Fetch Timeout",
                    value = fetchTimeout.toFloat(),
                    range = 5f..30f,
                    display = "${fetchTimeout}s",
                    onValueChange = { fetchTimeout = it.toInt() }
                )
                HorizontalDivider(color = BorderLight)
                SettingSlider(
                    label = "Max Competitors",
                    value = maxCompetitors.toFloat(),
                    range = 1f..10f,
                    display = "$maxCompetitors pages",
                    onValueChange = { maxCompetitors = it.toInt() }
                )
            }

            SettingGroup(title = "Display Options") {
                SettingToggle(
                    label = "Show Feature Provenance",
                    subtitle = "Show how each score is computed",
                    checked = showProvenance,
                    onCheckedChange = { showProvenance = it }
                )
                HorizontalDivider(color = BorderLight)
                SettingToggle(
                    label = "Auto-detect Query Type",
                    subtitle = "List / Opinion / Other detection",
                    checked = autoDetectQueryType,
                    onCheckedChange = { autoDetectQueryType = it }
                )
                HorizontalDivider(color = BorderLight)
                SettingToggle(
                    label = "Fallback on Fetch Error",
                    subtitle = "Use heuristic estimates if page fetch fails",
                    checked = fallbackOnError,
                    onCheckedChange = { fallbackOnError = it }
                )
            }

            SettingGroup(title = "About") {
                SettingInfoRow("Backend", "Python (predictor.py + seo_predictor.py)")
                HorizontalDivider(color = BorderLight)
                SettingInfoRow("ML Stage 1", "Binary Classifier — Is Visible?")
                HorizontalDivider(color = BorderLight)
                SettingInfoRow("ML Stage 2", "Regressor — PAWC Score (log-scale)")
                HorizontalDivider(color = BorderLight)
                SettingInfoRow("Features", "Relevance, Influence, Uniqueness, Diversity, Click Prob.")
                HorizontalDivider(color = BorderLight)
                SettingInfoRow("Approach", "2-pass competitor-aware prediction")
            }

            Surface(
                color = GenomeDanger.copy(alpha = 0.06f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GenomeDanger),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Clear Analysis History")
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SettingGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = TextMuted,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        Surface(
            color = BgCard,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, BorderLight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), content = content)
        }
    }
}

@Composable
private fun SettingToggle(label: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = GenomePrimary
            )
        )
    }
}

@Composable
private fun SettingSlider(label: String, value: Float, range: ClosedFloatingPointRange<Float>, display: String, onValueChange: (Float) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
            Text(display, style = MaterialTheme.typography.labelLarge, color = GenomePrimary, fontWeight = FontWeight.SemiBold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            steps = (range.endInclusive - range.start - 1).toInt(),
            colors = SliderDefaults.colors(
                thumbColor = GenomePrimary,
                activeTrackColor = GenomePrimary,
                inactiveTrackColor = BgHighlight
            )
        )
    }
}

@Composable
private fun SettingInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = TextSecondary, modifier = Modifier.weight(0.4f))
        Text(value, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, modifier = Modifier.weight(0.6f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
    }
}