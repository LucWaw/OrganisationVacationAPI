package com.lucwaw.model.repository

import com.lucwaw.domain.People
import com.lucwaw.model.Vacation

class PeopleRepository(val contextVacation: Vacation) : Repository<People>() {
    
}

