package com.lucwaw.domain

data class Activity(
    override val name: String,
    val address: Address,
    val description: String
) : Named