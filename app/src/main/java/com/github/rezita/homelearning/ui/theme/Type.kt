package com.github.rezita.homelearning.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.github.rezita.homelearning.R

val amarant = FontFamily(
    Font(R.font.amaranth)
)

val typography = Typography(
    //Text for reading
    displayLarge = TextStyle(
        fontFamily = amarant,
        fontWeight = FontWeight.Normal,
        fontSize = 96.sp
    ),
    labelSmall = TextStyle(
        fontFamily = amarant,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
)