package com.facultate.licenta.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.facultate.licenta.R


object Typography{
    private val headingFont = FontFamily(
        Font( resId = R.font.merienda)
    )
    private val paragraphFont = FontFamily(
        Font(resId = R.font.ysabeau, weight =  FontWeight.Normal)
    )
    val h1 = TextStyle(
        fontSize = 34.sp,
        fontFamily = headingFont,
        letterSpacing = TextUnit.Unspecified,
        lineHeight = (34 * 1.3).sp,
        fontWeight = FontWeight.Bold
    )
    val h2 = TextStyle(
        fontSize = 28.sp,
        fontFamily = headingFont,
        letterSpacing = TextUnit.Unspecified,
        lineHeight = (28 * 1.3).sp,
        fontWeight = FontWeight.Bold
    )
    val h3 = TextStyle(
        fontSize = 23.sp,
        fontFamily = headingFont,
        letterSpacing = TextUnit.Unspecified,
        lineHeight = (23 * 1.3).sp,
        fontWeight = FontWeight.Bold
    )
    val h4 = TextStyle(
        fontSize = 19.sp,
        fontFamily = headingFont,
        letterSpacing = TextUnit.Unspecified,
        lineHeight = (19 * 1.3).sp,
        fontWeight = FontWeight.Bold
    )

    val pBig = TextStyle(
        fontSize = 18.sp,
        fontFamily = paragraphFont,
        letterSpacing = 0.sp,
        lineHeight = (18 * 1.5).sp,
    )

    val p = TextStyle(
        fontSize = 15.sp,
        fontFamily = paragraphFont,
        letterSpacing = 0.sp,
        lineHeight = (15 * 1.5).sp,
    )

    val caption = TextStyle(
        fontSize = 12.sp,
        fontFamily = paragraphFont,
        letterSpacing = 0.sp,
        lineHeight = (12 * 1.875).sp,
    )

    val buttonBold = TextStyle(
        fontSize = 14.sp,
        lineHeight = (14*1.3).sp,
        fontFamily = paragraphFont,
        fontWeight = FontWeight(700),
        color = Variables.grey6,
    )

}