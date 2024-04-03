package com.github.rezita.homelearning.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.github.rezita.homelearning.R

val balsamiq = FontFamily(
    Font(R.font.balsamiq_sans_regular)
)

val typography = Typography(
    //Text for reading
    displayLarge = TextStyle(
        fontFamily = balsamiq,
        fontWeight = FontWeight.Normal,
        fontSize = 96.sp
    ),
    labelSmall = TextStyle(
        fontFamily = balsamiq,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
)