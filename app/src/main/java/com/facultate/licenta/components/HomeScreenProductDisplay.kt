package com.facultate.licenta.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.facultate.licenta.R
import com.facultate.licenta.model.CartItemShort
import com.facultate.licenta.screens.home.HomePageViewModel
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import kotlinx.coroutines.launch


@Composable
fun HomeScreenProductDisplay(
    productImageDescription: String = "Product Image",
    productName: String = "Ink Pen Second In Line",
    productId: String,
    productCategory: String = "Category",
    productImage: String? = null,
    productPrice: Double = 123.24,
    discount: Double = 0.0,
    viewModel: HomePageViewModel,
    navigateToProduct: () -> Unit
) {

    Box(
        modifier = Modifier
            .shadow(
                elevation = Variables.elevation,
                spotColor = Variables.grey6,
                ambientColor = Variables.grey6,
                shape = RoundedCornerShape(size = Variables.cornerRadius)
            )
            .border(
                width = 1.dp,
                color = Variables.blue3,
                shape = RoundedCornerShape(size = Variables.cornerRadius)
            )
            .width(224.dp)
            .height(152.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(size = Variables.cornerRadius)
            )
            .padding(all = Variables.innerItemGap)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                navigateToProduct.invoke()
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                8.dp,
                Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(productImage)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.image_placeholder),
                contentDescription = productImageDescription,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .width(84.dp)
                    .height(128.dp)
                    .background(
                        color = Variables.grey1,
                        shape = RoundedCornerShape(size = Variables.cornerRadius)
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .width(108.dp)
                        .height(36.dp),
                    text = productName,
                    maxLines = 2,
                    overflow = TextOverflow.Clip,
                    style = Typography.buttonBold,
                    color = Variables.blue3
                )
                Text(
                    text = productCategory,
                    style = Typography.caption,
                    color = Variables.grey3
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (discount != 0.0) {
                        Text(
                            text = "$${productPrice - productPrice * discount}",
                            style = Typography.buttonBold,
                            color = Variables.red,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "$${productPrice}",
                            style = Typography.p.copy(
                                textDecoration = TextDecoration.LineThrough,
                            ),
                            color = Variables.grey3
                        )
                    } else {
                        Text(
                            text = "$${productPrice}",
                            style = Typography.buttonBold,
                            color = Variables.grey6,
                            modifier = Modifier.weight(1f)
                        )
                    }

                }
                Button(
                    onClick = {
                        viewModel.viewModelScope.launch{
                            viewModel.addToCart(
                                quantity = 1,
                                discount = discount,
                                cartItem = CartItemShort(
                                    productId = productId,
                                    category = productCategory
                                )
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(
                            0xff172145
                        )
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    modifier = Modifier
                        .requiredWidth(width = 107.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            12.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .requiredWidth(width = 107.dp)
                    ) {
                        Text(
                            text = "Add to cart",
                            color = Color(0xffebecef),
                            textAlign = TextAlign.Center,
                            lineHeight = 9.29.em,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}
