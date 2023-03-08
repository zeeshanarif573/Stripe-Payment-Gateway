package com.example.stripepayment.ui.model.empheral_keys

data class EphemeralKeysResponse(
    val associated_objects: List<AssociatedObject>,
    val created: Int,
    val expires: Int,
    val id: String,
    val livemode: Boolean,
    val `object`: String,
    val secret: String
)