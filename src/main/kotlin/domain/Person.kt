package com.lucwaw.domain

import kotlinx.serialization.Serializable

@Serializable
data class Person(
    override val name: String,
    val id: String,
    val firstName: String,
) : Named