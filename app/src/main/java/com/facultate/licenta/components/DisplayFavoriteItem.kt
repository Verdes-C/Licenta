package com.facultate.licenta.components

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.facultate.licenta.R
import com.facultate.licenta.model.Product
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables


@Composable
fun DisplayFavoriteItem(
    modifier: Modifier = Modifier,
    favoritesItem: Product,
    removeFromFavorite: () -> Unit,
    addToCart: () -> Unit,
    navigate: () -> Unit
) {

    val context = LocalContext.current

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
            .heightIn(min = 130.dp, max = 160.dp)
            .clip(shape = RoundedCornerShape(Variables.cornerRadius))
            .background(color = Color.White)
            .padding(all = Variables.innerItemGapLow)
            .clickable(
                //_ set the onClick animation to null
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { menuIsVisible = false }
    ) {
        AsyncImage(
            model = favoritesItem.images.first(),
            contentDescription = favoritesItem.description,
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
                .clickable {
                    navigate.invoke()
                }
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = favoritesItem.name,
                        color = Variables.blue3,
                        style = Typography.p,
                        fontWeight = FontWeight.Bold,
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
                            .clickable { menuIsVisible = !menuIsVisible }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "${favoritesItem.reviews.size} reviews",
                        style = Typography.p,
                        color = Variables.blue3
                    )
                }

                DisplayRating(rating = favoritesItem.rating)

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${favoritesItem.price - favoritesItem.price * favoritesItem.discount}",
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
                        Icon(
                            painter = painterResource(id = R.drawable.icon_cart),
                            contentDescription = "Add to cart",
                            tint = Variables.blue3,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null
                                ) {

                                    Toast
                                        .makeText(
                                            context,
                                            "Product added to the cart",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    addToCart.invoke()
                                })
                    }
                }
            }

            if (menuIsVisible) {
                Log.d("TESTING", "menu is visible")
                DisplayFavoritesOptionMenu(
                    modifier = Modifier.align(Alignment.TopEnd),
                    removeFromFavorite = removeFromFavorite
                ) {
                    menuIsVisible = false
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStar1(rating: Double = 0.4) {
    DisplayRating(rating = rating)
}

@Preview(showBackground = true)
@Composable
fun PreviewStar2(rating: Double = 0.6) {
    DisplayRating(rating = rating)
}

@Preview(showBackground = true)
@Composable
fun PreviewStar3(rating: Double = 3.4) {
    DisplayRating(rating = rating)
}

@Preview(showBackground = true)
@Composable
fun PreviewStar4(rating: Double = 4.4) {
    DisplayRating(rating = rating)
}

@Preview(showBackground = true)
@Composable
fun PreviewStar5(rating: Double = 4.6) {
    DisplayRating(rating = rating)
}

@Preview(showBackground = true)
@Composable
fun PreviewStar6(rating: Double = 5.0) {
    DisplayRating(rating = rating)
}




