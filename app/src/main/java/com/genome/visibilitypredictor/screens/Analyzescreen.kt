package com.genome.visibilitypredictor.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.genome.visibilitypredictor.ui.components.*
import com.genome.visibilitypredictor.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyzeScreen(navController: NavController) {
    var userUrl by remember { mutableStateOf("") }
    var query by remember { mutableStateOf("") }
    var competitorText by remember { mutableStateOf("") }
    var showCompetitors by remember { mutableStateOf(false) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    // Simulated: detect query type
    val queryType = when {
        listOf("best", "top", "list").any { query.lowercase().contains(it) } -> "List Query"
        listOf("vs", "compare", "review").any { query.lowercase().contains(it) } -> "Opinion Query"
        query.isNotBlank() -> "Informational Query"
        else -> null
    }

    Scaffold(
        containerColor = BgSurface,
        topBar = {
            GenomeTopBar(
                title = "Visibility Predictor",
                subtitle = "GENOME v2 Analysis",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->

        if (isAnalyzing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    GenomeLoadingIndicator("Fetching & analyzing pages...")
                    Text(
                        text = "This may take 20–30 seconds",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                    // Simulated progress
                    LinearProgressIndicator(
                        color = GenomePrimary,
                        trackColor = BgHighlight,
                        modifier = Modifier
                            .width(220.dp)
                            .clip(RoundedCornerShape(50))
                    )
                    OutlinedButton(
                        onClick = { isAnalyzing = false },
                        border = BorderStroke(1.dp, BorderLight)
                    ) {
                        Text("Cancel", color = TextSecondary)
                    }
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ── YOUR PAGE SECTION ──
            AnalysisSection(title = "Your Page", subtitle = "The page you want to predict visibility for") {
                GenomeUrlField(
                    value = userUrl,
                    onValueChange = { userUrl = it; errorMsg = null },
                    label = "Your Page URL *"
                )
            }

            // ── SEARCH QUERY SECTION ──
            AnalysisSection(title = "Target Query", subtitle = "The keyword / question you're targeting") {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it; errorMsg = null },
                    label = { Text("Search Query *") },
                    placeholder = { Text("e.g. best machine learning projects", color = TextMuted) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = GenomePrimary)
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { query = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted)
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GenomePrimary,
                        unfocusedBorderColor = BorderLight,
                        focusedLabelColor = GenomePrimary,
                        cursorColor = GenomePrimary
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                // Query type badge
                AnimatedVisibility(visible = queryType != null) {
                    queryType?.let {
                        Surface(
                            color = GenomePrimary.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Label,
                                    contentDescription = null,
                                    tint = GenomePrimary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "Detected: $it",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GenomePrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // ── COMPETITOR URLS SECTION ──
            AnalysisSection(
                title = "Competitor Pages",
                subtitle = "Optional — add for competitive benchmarking",
                headerExtra = {
                    Switch(
                        checked = showCompetitors,
                        onCheckedChange = { showCompetitors = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = GenomePrimary
                        )
                    )
                }
            ) {
                AnimatedVisibility(visible = showCompetitors) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Enter one URL per line (max 5 competitors)",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                        OutlinedTextField(
                            value = competitorText,
                            onValueChange = { competitorText = it },
                            label = { Text("Competitor URLs") },
                            placeholder = {
                                Text(
                                    "https://competitor1.com\nhttps://competitor2.com",
                                    color = TextMuted
                                )
                            },
                            leadingIcon = {
                                Icon(Icons.Default.CompareArrows, contentDescription = null, tint = GenomeAccent)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GenomeAccent,
                                unfocusedBorderColor = BorderLight,
                                focusedLabelColor = GenomeAccent,
                                cursorColor = GenomeAccent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 120.dp),
                            minLines = 4
                        )
                        // Count chips
                        val count = competitorText
                            .lines()
                            .filter { it.trim().isNotEmpty() }
                            .size
                        if (count > 0) {
                            Surface(
                                color = if (count <= 5) GenomeSuccess.copy(alpha = 0.1f) else GenomeDanger.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        if (count <= 5) Icons.Default.CheckCircle else Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = if (count <= 5) GenomeSuccess else GenomeDanger,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = if (count <= 5) "$count competitor(s) entered" else "Max 5 competitors allowed (currently $count)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (count <= 5) GenomeSuccess else GenomeDanger,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
                if (!showCompetitors) {
                    Surface(
                        color = BgSubtle,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = GenomePrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Enable to analyze relative to competitors for more accurate ranking predictions.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // ── ERROR MESSAGE ──
            AnimatedVisibility(visible = errorMsg != null) {
                errorMsg?.let {
                    Surface(
                        color = GenomeDanger.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = GenomeDanger, modifier = Modifier.size(18.dp))
                            Text(it, style = MaterialTheme.typography.bodyMedium, color = GenomeDanger)
                        }
                    }
                }
            }

            // ── ANALYZE BUTTON ──
            val canAnalyze = userUrl.isNotBlank() && query.isNotBlank()
            GenomePrimaryButton(
                text = if (showCompetitors && competitorText.isNotBlank()) "Analyze with Competitors" else "Analyze My Page",
                onClick = {
                    when {
                        userUrl.isBlank() -> errorMsg = "Please enter your page URL"
                        query.isBlank()   -> errorMsg = "Please enter a search query"
                        else -> {
                            isAnalyzing = true
                            // Simulate analysis delay then navigate
                            // In real app, use ViewModel + coroutines
                        }
                    }
                },
                enabled = canAnalyze,
                icon = Icons.Default.PlayArrow,
                modifier = Modifier.fillMaxWidth()
            )

            // Demo mode
            TextButton(
                onClick = { navController.navigate("results") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("View Demo Results", color = GenomePrimary)
            }

            // ── ANALYSIS INFO ──
            Surface(
                color = BgSubtle,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "What gets analyzed",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    val items = listOf(
                        "Relevance — title, heading & body overlap with query",
                        "Influence — domain authority & structure signals",
                        "Uniqueness — lexical diversity & originality markers",
                        "Diversity — headings, lists, images, tables, code",
                        "Click Probability — title/meta quality & intent match",
                        "PAWC Score — predicted AI visibility strength"
                    )
                    items.forEach { item ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .size(6.dp)
                                    .background(GenomePrimary, CircleShape)
                            )
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun AnalysisSection(
    title: String,
    subtitle: String,
    headerExtra: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        color = BgCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, BorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
                if (headerExtra != null) headerExtra()
            }
            HorizontalDivider(color = BorderLight)
            content()
        }
    }
}