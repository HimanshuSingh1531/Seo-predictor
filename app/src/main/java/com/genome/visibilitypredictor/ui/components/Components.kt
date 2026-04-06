package com.genome.visibilitypredictor.ui.components

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
import com.genome.visibilitypredictor.data.*
import com.genome.visibilitypredictor.ui.theme.*

// =============================================
// TOP APP BAR
// =============================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenomeTopBar(
    title: String,
    subtitle: String? = null,
    showBack: Boolean = false,
    onBack: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(GenomePrimary, Color(0xFF3B82F6))
                )
            )
    ) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                    }
                }
            },
            navigationIcon = {
                if (showBack) {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            },
            actions = actions,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}

// =============================================
// GENOME LOGO BADGE
// =============================================
@Composable
fun GenomeBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(GenomePrimary, GenomeAccent)
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                Icons.Default.Analytics,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "GENOME v2",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

// =============================================
// METRIC SCORE RING
// =============================================
@Composable
fun ScoreRing(
    score: Float,           // 0..1
    label: String,
    size: Dp = 80.dp,
    strokeWidth: Dp = 8.dp,
    color: Color = GenomePrimary,
    modifier: Modifier = Modifier
) {
    val animatedScore by animateFloatAsState(
        targetValue = score,
        animationSpec = tween(durationMillis = 1000, easing = EaseOutCubic),
        label = "score"
    )
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = strokeWidth.toPx()
            val radius = (size.toPx() / 2f) - stroke / 2
            // Track
            drawCircle(
                color = color.copy(alpha = 0.12f),
                radius = radius,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke)
            )
            // Progress arc
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * animatedScore,
                useCenter = false,
                topLeft = Offset(stroke / 2, stroke / 2),
                size = androidx.compose.ui.geometry.Size(
                    size.toPx() - stroke,
                    size.toPx() - stroke
                ),
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = stroke,
                    cap = StrokeCap.Round
                )
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${(animatedScore * 100).toInt()}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                fontSize = 9.sp,
                lineHeight = 11.sp
            )
        }
    }
}

// =============================================
// FEATURE BAR ITEM
// =============================================
@Composable
fun FeatureBar(
    name: String,
    value: Float,
    color: Color = GenomePrimary,
    modifier: Modifier = Modifier
) {
    val animatedWidth by animateFloatAsState(
        targetValue = value,
        animationSpec = tween(800, easing = EaseOutCubic),
        label = "bar"
    )
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Text(
                text = "${(value * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge,
                color = color,
                fontWeight = FontWeight.SemiBold
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(7.dp)
                .clip(RoundedCornerShape(50))
                .background(color.copy(alpha = 0.12f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedWidth)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(color, color.copy(alpha = 0.75f))
                        )
                    )
            )
        }
    }
}

// =============================================
// VISIBILITY TIER CHIP
// =============================================
@Composable
fun VisibilityTierChip(tier: VisibilityTier, modifier: Modifier = Modifier) {
    val (label, bg, fg, icon) = when (tier) {
        VisibilityTier.HIGH   -> Triple("High Visibility", GenomeSuccess.copy(alpha = 0.15f), GenomeSuccess, Icons.Default.TrendingUp)
            .let { (a,b,c,d) -> listOf(a,b,c,d) }
        VisibilityTier.MEDIUM -> Triple("Medium Visibility", GenomeWarning.copy(alpha = 0.15f), GenomeWarning, Icons.Default.TrendingFlat)
            .let { (a,b,c,d) -> listOf(a,b,c,d) }
        VisibilityTier.LOW    -> Triple("Low Visibility", GenomeDanger.copy(alpha = 0.15f), GenomeDanger, Icons.Default.TrendingDown)
            .let { (a,b,c,d) -> listOf(a,b,c,d) }
    }
    @Suppress("UNCHECKED_CAST")
    val l = label as String
    val bg2 = bg as Color
    val fg2 = fg as Color
    val ic = icon as androidx.compose.ui.graphics.vector.ImageVector

    Surface(
        color = bg2,
        shape = RoundedCornerShape(50),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(ic, contentDescription = null, tint = fg2, modifier = Modifier.size(14.dp))
            Text(
                text = l,
                style = MaterialTheme.typography.labelLarge,
                color = fg2,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// =============================================
// STAT MINI CARD
// =============================================
@Composable
fun StatMiniCard(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color = GenomePrimary,
    modifier: Modifier = Modifier
) {
    Surface(
        color = BgSubtle,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(tint.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
            }
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

// =============================================
// GAP STATUS ICON
// =============================================
@Composable
fun GapStatusIcon(status: GapStatus, size: Dp = 16.dp) {
    val (icon, color) = when (status) {
        GapStatus.GOOD     -> Icons.Default.CheckCircle to GenomeSuccess
        GapStatus.WARNING  -> Icons.Default.Warning to GenomeWarning
        GapStatus.CRITICAL -> Icons.Default.Cancel to GenomeDanger
    }
    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(size))
}

// =============================================
// GENOME URL INPUT FIELD
// =============================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenomeUrlField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "https://example.com",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder, color = TextMuted) },
        leadingIcon = {
            Icon(Icons.Default.Link, contentDescription = null, tint = GenomePrimary)
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted)
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GenomePrimary,
            unfocusedBorderColor = BorderLight,
            focusedLabelColor = GenomePrimary,
            cursorColor = GenomePrimary
        ),
        modifier = modifier.fillMaxWidth(),
        singleLine = true
    )
}

// =============================================
// PULSE LOADING INDICATOR
// =============================================
@Composable
fun GenomeLoadingIndicator(message: String = "Analyzing...") {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer(alpha = alpha)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(GenomePrimary, GenomeAccent)
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}

// =============================================
// SECTION HEADER
// =============================================
@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    action: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }
        if (action != null) action()
    }
}

// =============================================
// GENOME PRIMARY BUTTON
// =============================================
@Composable
fun GenomePrimaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = GenomePrimary,
            contentColor = Color.White,
            disabledContainerColor = BorderLight,
            disabledContentColor = TextMuted
        ),
        modifier = modifier.height(52.dp),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )
    }
}

// =============================================
// DIVIDER WITH LABEL
// =============================================
@Composable
fun LabelDivider(label: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = BorderLight)
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = BorderLight)
    }
}