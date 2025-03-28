package com.lucwaw.domain

data class Address(
    override val name: String,
    val description: String,
    val region: Region
) : Named