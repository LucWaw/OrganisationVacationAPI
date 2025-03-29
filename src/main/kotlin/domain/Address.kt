package com.lucwaw.domain

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    override val name: String,
    val region: Region
) : Named