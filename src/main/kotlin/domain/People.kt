package com.lucwaw.domain

import kotlinx.serialization.Serializable

@Serializable
data class People(
    override val name: String,
    val id: String,
    val firstName: String,
) : Named