package com.lucwaw.model

import kotlinx.serialization.Serializable


enum class Region {
    AuvergneRhoneAlpes,
    BourgogneFrancheComte,
    Bretagne,
    CentreValDeLoire,
    GrandEst,
    HautsDeFrance,
    IleDeFrance,
    Normandie,
    NouvelleAquitaine,
    Occitanie,
    PaysDeLaLoire,
    ProvenceAlpesCoteDAzur
}


@Serializable
data class Destination(
    val name: String,
    val region: Region,
    val description: String
)