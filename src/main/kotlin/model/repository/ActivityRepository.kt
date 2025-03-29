package com.lucwaw.model.repository

import com.lucwaw.domain.Activity
import com.lucwaw.domain.Address
import com.lucwaw.domain.Region
import com.lucwaw.model.Vacation


class ActivityRepository(val contextVacation: Vacation) : Repository<Activity>() {
    //fill items
    override val items: MutableList<Activity> = mutableListOf(
        Activity(
            "Kanoe",
            Address("Château de Fontainebleau, 77300 Fontainebleau, France", Region.IleDeFrance),
            "Description of Kanoe"
        ),
        Activity(
            "Musée",
            Address("Château de Fontainebleau, 77300 Fontainebleau, France", Region.IleDeFrance),
            "Description of Musée"
        ),
        Activity(
            "Château",
            Address("Château de Fontainebleau, 77300 Fontainebleau, France", Region.IleDeFrance),
            "Description of Château"
        )
    )
}