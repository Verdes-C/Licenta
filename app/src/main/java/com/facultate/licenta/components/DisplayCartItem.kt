package com.facultate.licenta.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.facultate.licenta.R
import com.facultate.licenta.Screens.cart.CartItem
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import kotlin.math.ceil


@Composable
fun DisplayCartItem(modifier: Modifier = Modifier, cartItem: CartItem = CartItem()) {

    var quantity by remember {
        mutableIntStateOf(cartItem.productQuantity)
    }

    var menuIsVisible by remember {
        mutableStateOf(false)
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(Variables.innerItemGapLow, Alignment.Start),
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(height = 140.dp)
            .clip(shape = RoundedCornerShape(Variables.cornerRadius))
            .background(color = Color.White)
            .padding(all = Variables.innerItemGapLow)
            .clickable { menuIsVisible = false }
    ) {
        Image(
            painter = painterResource(id = cartItem.productImage),
            contentDescription = cartItem.productImageDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .clip(
                    shape = RoundedCornerShape(
                        topStart = Variables.cornerRadius,
                        bottomStart = Variables.cornerRadius
                    )
                )
                .border(
                    border = BorderStroke(1.dp, Variables.orange1),
                    shape = RoundedCornerShape(
                        topStart = Variables.cornerRadius,
                        bottomStart = Variables.cornerRadius
                    )
                )
//                .padding(all = Variables.innerItemGapLow)
        )
        Box(modifier = Modifier) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        shape = RoundedCornerShape(
                            topEnd = Variables.cornerRadius,
                            bottomEnd = Variables.cornerRadius
                        )
                    )
                    .border(
                        border = BorderStroke(1.dp, Variables.orange1),
                        shape = RoundedCornerShape(
                            topEnd = Variables.cornerRadius,
                            bottomEnd = Variables.cornerRadius
                        )
                    )
                    .padding(all = Variables.innerItemGapLow),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(height = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = cartItem.productName,
                        color = Variables.blue3,
                        style = Typography.p,
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.icon_options),
                        contentDescription = "Options menu",
                        tint = Variables.blue3,
                        modifier = Modifier
                            .requiredSize(size = 30.dp)
                            .clickable { menuIsVisible = !menuIsVisible }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(height = 36.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${
                            String.format(
                                "%.2f",
                                ceil(cartItem.productPrice * quantity * 100) / 100
                            )
                        }",
                        color = Variables.blue3,
                        style = Typography.pBig,
                        modifier = Modifier
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .requiredHeight(height = 36.dp)
                    ) {
                        Text(
                            text = "-",
                            color = Color(0xff163688),
                            textAlign = TextAlign.Center,
                            style = Typography.pBig,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .requiredSize(24.dp)
                                .wrapContentHeight(align = Alignment.CenterVertically)
                                .clickable(
                                    //_ set the onClick animation to null
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    if (quantity > 1) quantity -= 1
                                }
                        )
                        Text(
                            text = "$quantity",
                            color = Color(0xff163688),
                            style = Typography.pBig,
                            modifier = Modifier
                                .wrapContentHeight(align = Alignment.CenterVertically)
                        )
                        Text(
                            text = "+",
                            color = Color(0xff163688),
                            textAlign = TextAlign.Center,
                            style = Typography.pBig,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .requiredSize(24.dp)
                                .wrapContentHeight(align = Alignment.CenterVertically)
                                .clickable(
                                    //_ set the onClick animation to null
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { quantity += 1 }
                        )
                    }
                }
            }
            if (menuIsVisible) {
                DisplayCartOptionMenu(
                    modifier = Modifier.align(Alignment.TopEnd),
                    cartItem = cartItem
                )
            }
        }
    }
}


