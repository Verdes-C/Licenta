package com.facultate.licenta.model


data class CollectionEntry(
    val id: String,
    val link: String,
    val tagData: TagData,
    val reviews: List<Review>,
)
