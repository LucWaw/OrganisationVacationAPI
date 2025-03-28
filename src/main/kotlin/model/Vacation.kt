package com.lucwaw.model

import com.lucwaw.model.repository.ActivityRepository
import com.lucwaw.model.repository.PeopleRepository
import java.util.*

class Vacation (
    val startDate : Date,
    val endDate : Date
){
    val activityRepository: ActivityRepository = ActivityRepository(this)
    val peopleRepository: PeopleRepository = PeopleRepository(this)
}