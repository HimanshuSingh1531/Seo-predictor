package com.genome.visibilitypredictor.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.sp

// ============================
// COLOR PALETTE — Light, Fresh, Premium
// ============================
val GenomePrimary     = Color(0xFF2563EB)   // Vivid Blue
val GenomePrimaryDark = Color(0xFF1D4ED8)   // Deep Blue
val GenomeAccent      = Color(0xFF06B6D4)   // Cyan
val GenomeSuccess     = Color(0xFF10B981)   // Emerald Green
val GenomeWarning     = Color(0xFFF59E0B)   // Amber
val GenomeDanger      = Color(0xFFEF4444)   // Red
val GenomePurple      = Color(0xFF8B5CF6)   // Violet

// Backgrounds
val BgSurface    = Color(0xFFF8FAFF)   // Near-white with blue tint
val BgCard       = Color(0xFFFFFFFF)   // Pure white cards
val BgSubtle     = Color(0xFFEFF6FF)   // Very light blue
val BgHighlight  = Color(0xFFDBEAFE)   // Blue highlight

// Text
val TextPrimary   = Color(0xFF0F172A)   // Near-black
val TextSecondary = Color(0xFF475569)   // Slate
val TextMuted     = Color(0xFF94A3B8)   // Light slate
val TextOnPrimary = Color(0xFFFFFFFF)   // White on colored backgrounds

// Borders
val BorderLight = Color(0xFFE2E8F0)
val BorderBlue  = Color(0xFFBFDBFE)

private val GenomeLightColorScheme = lightColorScheme(
    primary           = GenomePrimary,
    onPrimary         = TextOnPrimary,
    primaryContainer  = BgHighlight,
    onPrimaryContainer = GenomePrimaryDark,
    secondary         = GenomeAccent,
    onSecondary       = TextOnPrimary,
    secondaryContainer = Color(0xFFCFFAFE),
    onSecondaryContainer = Color(0xFF0E7490),
    tertiary          = GenomePurple,
    onTertiary        = TextOnPrimary,
    background        = BgSurface,
    onBackground      = TextPrimary,
    surface           = BgCard,
    onSurface         = TextPrimary,
    surfaceVariant    = BgSubtle,
    onSurfaceVariant  = TextSecondary,
    outline           = BorderLight,
    outlineVariant    = BorderBlue,
    error             = GenomeDanger,
    onError           = TextOnPrimary,
)

@Composable
fun GenomeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GenomeLightColorScheme,
        typography = GenomeTypography,
        content = content
    )
}

val GenomeTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.5).sp,
        color = TextPrimary
    ),
    displayMedium = TextStyle(
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.3).sp,
        color = TextPrimary
    ),
    headlineLarge = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary
    ),
    headlineMedium = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary
    ),
    headlineSmall = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary
    ),
    titleLarge = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = TextPrimary
    ),
    bodyLarge = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        color = TextPrimary
    ),
    bodyMedium = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = TextSecondary
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = TextMuted
    ),
    labelLarge = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.3.sp
    ),
    labelMedium = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.5.sp
    ),
)