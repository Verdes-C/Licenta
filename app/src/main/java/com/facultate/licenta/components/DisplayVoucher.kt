package com.facultate.licenta.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.facultate.licenta.ui.theme.Typography


@Composable
fun DisplayVoucher(modifier: Modifier = Modifier, voucher: Voucher = Voucher()) {
    var isExpanded by remember{
        mutableStateOf(false)
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .requiredWidth(width = 100.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = Color(0xffebecef))
            .border(
                border = BorderStroke(2.dp, Color(0xff163688)),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                horizontal = 4.dp,
                vertical = 10.dp
            )
            .clickable { isExpanded = !isExpanded }
    ) {
        Text(
            text = voucher.category,
            textAlign = TextAlign.Center,
            style = Typography.p,
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(height = 40.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))
        Text(
            text = "${(voucher.percentage*100).toInt()}%",
            color = Color(0xffda763f),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold),
            modifier = Modifier
                .wrapContentHeight(align = Alignment.CenterVertically))

        if(isExpanded){
            Buttons.PrimaryActive(text = "Apply") {
                //TODO USE
            }
        }
    }
}

@Preview(widthDp = 100, heightDp = 99)
@Composable
private fun CardsVouchersPreview() {
    DisplayVoucher(Modifier)
}

data class Voucher(
    val category: String = "Calligraphy Fountain Pens",
    val percentage: Float = 0.5f
)