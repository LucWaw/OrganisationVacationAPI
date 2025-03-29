package com.lucwaw.domain

import kotlinx.serialization.Serializable

@Serializable
data class Activity(
    override val name: String,
    val address: Address,
    val description: String
) : Named