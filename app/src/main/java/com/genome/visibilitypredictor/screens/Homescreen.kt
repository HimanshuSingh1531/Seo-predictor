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
import com.genome.visibilitypredictor.ui.components.*
import com.genome.visibilitypredictor.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    val scrollState = rememberScrollState()
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Scaffold(
        containerColor = BgSurface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
        ) {
            // ── HERO HEADER ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF1E3A8A),
                                Color(0xFF2563EB),
                                Color(0xFF0EA5E9)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
            ) {
                // Decorative circles
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.04f),
                        radius = 200f,
                        center = Offset(size.width - 80f, -60f)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.06f),
                        radius = 120f,
                        center = Offset(-40f, size.height * 0.6f)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.03f),
                        radius = 160f,
                        center = Offset(size.width * 0.5f, size.height + 40f)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 56.dp, bottom = 36.dp)
                ) {
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(600)) + slideInVertically(tween(600)) { it / 2 }
                    ) {
                        Column {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color.White.copy(alpha = 0.2f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Analytics,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "GENOME",
                                        style = MaterialTheme.typography.displayMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 2.sp
                                    )
                                    Text(
                                        text = "Visibility Predictor v2",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.7f),
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }

                            Spacer(Modifier.height(20.dp))

                            Text(
                                text = "LLM + Google SEO\nIntelligence in your pocket",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                lineHeight = 32.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(12.dp))

                            Text(
                                text = "Analyze visibility, predict rankings, and benchmark your content against competitors — powered by machine learning.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.75f),
                                lineHeight = 22.sp
                            )

                            Spacer(Modifier.height(28.dp))

                            // Quick Stats Row
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                QuickStatPill("PAWC Score", Icons.Default.Star)
                                QuickStatPill("Competitor", Icons.Default.CompareArrows)
                                QuickStatPill("SEO Grade", Icons.Default.Grade)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── MAIN ACTION CARDS ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(600, delayMillis = 200)) + slideInVertically(tween(600, delayMillis = 200)) { 60 }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        // Primary Action
                        PrimaryActionCard(
                            title = "Visibility Predictor",
                            subtitle = "Analyze your page vs competitors. Get PAWC score, rank prediction & gap analysis.",
                            icon = Icons.Default.Search,
                            gradient = listOf(GenomePrimary, Color(0xFF3B82F6)),
                            onClick = { navController.navigate("analyze") }
                        )

                        // Secondary cards
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            SecondaryActionCard(
                                title = "SEO\nAnalyzer",
                                icon = Icons.Default.Tune,
                                color = GenomePurple,
                                modifier = Modifier.weight(1f),
                                onClick = { navController.navigate("seo_analyze") }
                            )
                            SecondaryActionCard(
                                title = "Analysis\nHistory",
                                icon = Icons.Default.History,
                                color = GenomeAccent,
                                modifier = Modifier.weight(1f),
                                onClick = { navController.navigate("history") }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── HOW IT WORKS ──
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(600, delayMillis = 400))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    SectionHeader(
                        title = "How It Works",
                        subtitle = "3 simple steps to insights"
                    )
                    Spacer(Modifier.height(14.dp))
                    HowItWorksCard()
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── FEATURES ──
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(600, delayMillis = 500))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    SectionHeader(title = "Key Features")
                    Spacer(Modifier.height(12.dp))
                    FeatureHighlights()
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun QuickStatPill(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Surface(
        color = Color.White.copy(alpha = 0.15f),
        shape = RoundedCornerShape(50)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color.White, fontSize = 11.sp)
        }
    }
}

@Composable
private fun PrimaryActionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = gradient,
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
        ) {
            // Decorative element
            Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.06f),
                    radius = 80f,
                    center = Offset(size.width - 40f, 40f)
                )
            }
            Row(
                modifier = Modifier.padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f),
                        lineHeight = 18.sp
                    )
                }
                Icon(
                    Icons.Default.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun SecondaryActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = BgCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(color.copy(alpha = 0.12f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                lineHeight = 20.sp
            )
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun HowItWorksCard() {
    Surface(
        color = BgCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, BorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HowStep(
                number = "1",
                title = "Enter Your URL",
                desc = "Paste your page URL and the search query you're targeting.",
                color = GenomePrimary
            )
            HorizontalDivider(color = BorderLight)
            HowStep(
                number = "2",
                title = "Add Competitors",
                desc = "Optionally add competitor URLs for benchmark comparison.",
                color = GenomeAccent
            )
            HorizontalDivider(color = BorderLight)
            HowStep(
                number = "3",
                title = "Get AI Insights",
                desc = "Receive PAWC score, visibility tier, rank, and gap analysis.",
                color = GenomePurple
            )
        }
    }
}

@Composable
private fun HowStep(number: String, title: String, desc: String, color: Color) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun FeatureHighlights() {
    val features = listOf(
        Triple(Icons.Default.Psychology, "PAWC Score", GenomePrimary),
        Triple(Icons.Default.CompareArrows, "Competitor Analysis", GenomeAccent),
        Triple(Icons.Default.AutoGraph, "2-Stage ML Model", GenomePurple),
        Triple(Icons.Default.TipsAndUpdates, "Gap Analysis", GenomeWarning),
        Triple(Icons.Default.Language, "LLM + Google SEO", GenomeSuccess),
        Triple(Icons.Default.Speed, "Real-Time Fetch", GenomeDanger)
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        features.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { (icon, label, color) ->
                    Surface(
                        color = color.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodySmall,
                                color = color,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}