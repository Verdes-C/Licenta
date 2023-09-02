package com.facultate.licenta.model

data class Product(
    val name: String,
    val price: Double,
    val images: List<String> = emptyList(),
    val category: String,
    val rating: Double = 0.0,
    val description: String,
    val specifications: List<Pair<String, String>>? = emptyList(),
    val reviews: List<Review>,
    val discount: Double = 0.0,
    val id: String,
)

val produs = Product(
    name = "Pilot Iro-Utsushi Dip Pen",
    price = 11.00,
    images = listOf(
        "https://static.inkquill.com/images/020b48a6-5730-4eff-a1ba-20328ba8f6c3/1.jpg",
        "https://static.inkquill.com/images/020b48a6-5730-4eff-a1ba-20328ba8f6c3/2.jpg",
        "https://static.inkquill.com/images/020b48a6-5730-4eff-a1ba-20328ba8f6c3/3.jpg",
    ),
    category = "Calligraphy Dip Pen",
    rating = 4.5,
    description = "The Pilot Iro-Utsushi combines the nib of ...",
    specifications = listOf(
        "Model Number" to "PILOT FIR-70R-CLM",
        "Manufacturer" to "Pilot",
        "Body Color" to "Light Blue",
        "Body Material" to "Resin",
        "Clippable" to "No",
        // ...
    ),
    reviews = listOf(
        Review(
            date = "December 6 2022",
            rating = 5.0,
            reviewBody = "Lovely to draw with. Nice and lightweight.",
            title = "Lovely to draw with"
        ),
        Review(
            date = "January 16 2023",
            rating = 4.5,
            reviewBody = "Perfect for testing inks. The nib appears ...",
            title = "Perfect for testing inks."
        )
    ),
    discount = 0.1, /* Echivalentul a 10% */
    id = "020b48a6-5730-4eff-a1ba-20328ba8f6c3"
)
