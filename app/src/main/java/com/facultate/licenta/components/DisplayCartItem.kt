package com.facultate.licenta.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.facultate.licenta.R
import com.facultate.licenta.screens.cart.CartItem
import com.facultate.licenta.screens.cart.CartPageViewModel
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import kotlinx.coroutines.launch
import kotlin.math.ceil


@Composable
fun DisplayCartItem(
    modifier: Modifier = Modifier,
    cartItem: CartItem = CartItem(),
    viewModel: CartPageViewModel,
    isFavorite: Boolean = false,
    navigateToProduct: () -> Unit
) {

    var menuIsVisible by remember {
        mutableStateOf(false)
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(Variables.innerItemGapLow, Alignment.Start),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = Variables.elevation,
                spotColor = Variables.shadowColor,
                ambientColor = Variables.shadowColor
            )
            .heightIn(min = 114.dp, max = 134.dp)
            .clip(shape = RoundedCornerShape(Variables.cornerRadius))
            .background(color = Color.White)
            .padding(all = Variables.innerItemGapLow)
            .clickable(
                //_ set the onClick animation to null
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (menuIsVisible == true) {
                    menuIsVisible = false
                } else {
                    navigateToProduct()
                }
            }
    ) {


        AsyncImage(
            model = cartItem.productImage, contentDescription = cartItem.productImageDescription,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxHeight()
                .width(88.dp)
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
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 30.dp, max = 60.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = cartItem.productName,
                        color = Variables.blue3,
                        style = Typography.p,
                        maxLines = 2,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.icon_options),
                        contentDescription = "Options menu",
                        tint = Variables.blue3,
                        modifier = Modifier
                            .requiredSize(size = 24.dp)
                            .clickable {
                                menuIsVisible = !menuIsVisible
                            }
                    )
                }
                DisplayRating(rating = cartItem.rating)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${
                            String.format(
                                "%.2f",
                                ceil(cartItem.productPrice * cartItem.productQuantity * 100) / 100
                            )
                        }",
                        color = Variables.blue3,
                        style = Typography.pBig,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .requiredHeight(height = 32.dp)
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
                                    if (cartItem.productQuantity > 1) {
                                        viewModel.viewModelScope.launch {
                                            viewModel.updateCartProduct(
                                                productId = cartItem.productId,
                                                quantity = cartItem.productQuantity - 1
                                            )
                                        }
                                    } else {
                                        viewModel.viewModelScope.launch {
                                            viewModel.removeFromCart(
                                                productId = cartItem.productId,
                                                productCategory = cartItem.productCategory
                                            )
                                        }
                                    }
                                }
                        )
                        Text(
                            text = "${cartItem.productQuantity}",
                            color = Color(0xff163688),
                            style = Typography.pBig,
                            fontWeight = FontWeight.Bold,
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
                                ) {
                                    viewModel.viewModelScope.launch {
                                        viewModel.updateCartProduct(
                                            productId = cartItem.productId,
                                            quantity = cartItem.productQuantity + 1
                                        )
                                    }
                                }
                        )
                    }
                }
            }
            if (menuIsVisible) {
                DisplayCartOptionMenu(
                    modifier = Modifier.align(Alignment.TopEnd),
                    isFavorite = isFavorite,
                    closeTheMenu = {
                        menuIsVisible = !menuIsVisible
                    },
                    addOrRemoveFromFavorites = {
                        viewModel.viewModelScope.launch {
                            viewModel.addOrRemoveFromFavorites(
                                productId = cartItem.productId,
                                productCategory = cartItem.productCategory
                            )
                        }
                    },
                    removeFromCart = {
                        viewModel.viewModelScope.launch {
                            viewModel.removeFromCart(
                                productId = cartItem.productId,
                                productCategory = cartItem.productCategory
                            )
                        }
                    }
                )
            }
        }
    }
}


