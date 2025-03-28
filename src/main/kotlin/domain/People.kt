package com.lucwaw.domain

data class People(
    override val name: String,
    val id: String,
    val firstName: String,
) : Named