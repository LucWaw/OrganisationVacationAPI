package com.lucwaw.model.repository

import com.lucwaw.domain.Activity
import com.lucwaw.model.Vacation

class ActivityRepository(val contextVacation: Vacation) : Repository<Activity>()