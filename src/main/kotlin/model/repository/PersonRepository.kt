package com.lucwaw.model.repository

import com.lucwaw.domain.Person
import com.lucwaw.model.Vacation

class PersonRepository(val contextVacation: Vacation) : Repository<Person>() {

}

