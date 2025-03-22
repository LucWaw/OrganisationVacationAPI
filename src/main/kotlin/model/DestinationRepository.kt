package com.lucwaw.model

object DestinationRepository
{
    private val destinations = mutableListOf(
        Destination("Paris", Region.IleDeFrance, "The city of love"),
        Destination("Lyon", Region.AuvergneRhoneAlpes, "The city of lights"),
        Destination("Marseille", Region.ProvenceAlpesCoteDAzur, "The city of the sun"),
        Destination("Lille", Region.HautsDeFrance, "The city of the north")
    )

    fun addUpdateDestination(destination: Destination) {
        val index = destinations.indexOfFirst { it.name == destination.name }
        if (index != -1) {
            destinations[index] = destination
        } else {
            destinations.add(destination)
        }
    }

    fun removeDestination(name: String) : Boolean {
        return destinations.removeIf { it.name == name }
    }

    fun getDestinations(): List<Destination> {
        return destinations
    }
}