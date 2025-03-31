package com.lucwaw.model.repository

import com.lucwaw.domain.Person
import com.lucwaw.model.Vacation

class PeopleRepository(val contextVacation: Vacation) : Repository<Person>() {

}

