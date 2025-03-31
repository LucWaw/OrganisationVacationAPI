package com.lucwaw.model

import com.lucwaw.domain.Address
import com.lucwaw.model.repository.ActivityRepository
import com.lucwaw.model.repository.PersonRepository
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDate

@Serializable
class Vacation(
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDate,
    val address: Address,
) {

    @Transient
    val activityRepository: ActivityRepository = ActivityRepository(this)

    @Transient
    val personRepository: PersonRepository = PersonRepository(this)
}


