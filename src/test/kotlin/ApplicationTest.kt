package com.lucwaw

import com.lucwaw.domain.Activity
import com.lucwaw.domain.Address
import com.lucwaw.domain.Person
import com.lucwaw.domain.Region
import com.lucwaw.model.Vacation
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlin.test.*

class VacationApiTest {

    @Test
    fun rootEndpointReturnsHelloVacation() = testApplication {
        application { module() }

        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello Vacation!", response.bodyAsText())
    }

    @Test
    fun vacationCanBeFetchedByName() = testApplication {
        application { module() }

        val response = client.get("/vacation/Paris")
        assertEquals(HttpStatusCode.OK, response.status)

        val vacation = Json.decodeFromString<Vacation>(response.bodyAsText())
        assertEquals("Paris", vacation.address.name)
    }

    @Test
    fun fetchingNonExistentVacationReturns404() = testApplication {
        application { module() }

        val response = client.get("/vacation/Tokyo")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun personsCanBeFetchedForVacation() = testApplication {
        application { module() }

        val response = client.get("/vacation/Paris/persons")
        assertEquals(HttpStatusCode.OK, response.status)

        val persons = Json.decodeFromString<List<Person>>(response.bodyAsText())
        assertNotNull(persons)
    }

    @Test
    fun fetchingSpecificPerson() = testApplication {
        application { module() }

        val client = createClient {
            install(ContentNegotiation) { json() }
        }

        val personToAdd = Person(name = "Alice Dupont", id = "456", firstName = "Alice")

        client.post("/vacation/Paris/persons") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(personToAdd)
        }

        val response = client.get("/vacation/Paris/persons/Alice Dupont")
        assertEquals(HttpStatusCode.OK, response.status)

        val person = Json.decodeFromString<Person>(response.bodyAsText())
        assertEquals("Alice Dupont", person.name)
    }

    @Test
    fun fetchingNonExistentPersonReturns404() = testApplication {
        application { module() }

        val response = client.get("/vacation/Paris/persons/UnknownPerson")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }


    @Test
    fun addingNewPersonToVacation() = testApplication {
        application { module() }

        val client = createClient {
            install(ContentNegotiation) { json() }
        }

        val person = Person(name = "John Doe", id = "123", firstName = "John")
        val response = client.post("/vacation/Paris/persons") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(person)
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun addingDuplicatePersonReturnsConflict() = testApplication {
        application { module() }

        val client = createClient {
            install(ContentNegotiation) { json() }
        }

        val person = Person(name = "John Doe", id = "123", firstName = "John")
        client.post("/vacation/Paris/persons") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(person)
        }

        val response = client.post("/vacation/Paris/persons") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(person)
        }

        assertEquals(HttpStatusCode.Conflict, response.status)
    }

    @Test
    fun deletingPersonFromVacation() = testApplication {
        application { module() }

        val client = createClient {
            install(ContentNegotiation) { json() }
        }

        val person = Person(name = "John Doe", id = "123", firstName = "John")

        client.post("/vacation/Paris/persons") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(person)
        }

        val response = client.delete("/vacation/Paris/persons/John Doe")
        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    @Test
    fun deletingNonExistentPersonReturns404() = testApplication {
        application { module() }

        val response = client.delete("/vacation/Paris/persons/UnknownPerson")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun updatingPersonDetails() = testApplication {
        application { module() }

        val client = createClient {
            install(ContentNegotiation) { json() }
        }

        val person = Person(name = "John Doe", id = "123", firstName = "John")

        client.post("/vacation/Paris/persons") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(person)
        }

        val updatedPerson = Person(name = "John Doe", id = "123", firstName = "Johnny")
        val response = client.put("/vacation/Paris/persons") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(updatedPerson)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun fetchingActivitiesForVacation() = testApplication {
        application { module() }

        val response = client.get("/vacation/Paris/activities")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun addingNewActivityToVacation() = testApplication {
        application { module() }

        val client = createClient {
            install(ContentNegotiation) { json() }
        }

        val activity = Activity(
            name = "Tour Eiffel",
            address = Address(name = "Champ de Mars", region = Region.IleDeFrance),
            description = "Visite de la Tour Eiffel"
        )

        val response = client.post("/vacation/Paris/activities") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(activity)
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun deletingExistingActivity() = testApplication {
        application { module() }

        val client = createClient {
            install(ContentNegotiation) { json() }
        }

        val activity = Activity(
            name = "Tour Eiffel",
            address = Address(name = "Champ de Mars", region = Region.IleDeFrance),
            description = "Visite de la Tour Eiffel"
        )

        client.post("/vacation/Paris/activities") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(activity)
        }

        val response = client.delete("/vacation/Paris/activities/Tour Eiffel")
        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    @Test
    fun deletingNonExistentActivityReturns404() = testApplication {
        application { module() }

        val response = client.delete("/vacation/Paris/activities/UnknownActivity")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun fetchingSpecificActivity() = testApplication {
        application { module() }

        val client = createClient {
            install(ContentNegotiation) { json() }
        }

        val activityToAdd = Activity(
            name = "Tour Eiffel",
            address = Address(name = "Champ de Mars", region = Region.IleDeFrance),
            description = "Visite de la Tour Eiffel"
        )

        client.post("/vacation/Paris/activities") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(activityToAdd)
        }

        val response = client.get("/vacation/Paris/activities/Tour Eiffel")
        assertEquals(HttpStatusCode.OK, response.status)

        val activity = Json.decodeFromString<Activity>(response.bodyAsText())
        assertEquals("Tour Eiffel", activity.name)
    }

    @Test
    fun fetchingNonExistentActivityReturns404() = testApplication {
        application { module() }

        val response = client.get("/vacation/Paris/activities/UnknownActivity")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun addingDuplicateActivityReturnsConflict() = testApplication {
        application { module() }

        val client = createClient {
            install(ContentNegotiation) { json() }
        }

        val activity = Activity(
            name = "Tour Eiffel",
            address = Address(name = "Champ de Mars", region = Region.IleDeFrance),
            description = "Visite de la Tour Eiffel"
        )

        // Première insertion
        client.post("/vacation/Paris/activities") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(activity)
        }

        // Deuxième insertion (duplicate)
        val response = client.post("/vacation/Paris/activities") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(activity)
        }

        assertEquals(HttpStatusCode.Conflict, response.status)
    }

    @Test
    fun updatingActivityDetails() = testApplication {
        application { module() }

        val client = createClient {
            install(ContentNegotiation) { json() }
        }

        val originalActivity = Activity(
            name = "Tour Eiffel",
            address = Address(name = "Champ de Mars", region = Region.IleDeFrance),
            description = "Visite de la Tour Eiffel"
        )

        client.post("/vacation/Paris/activities") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(originalActivity)
        }

        val updatedActivity = Activity(
            name = "Tour Eiffel",
            address = Address(name = "Champ de Mars", region = Region.IleDeFrance),
            description = "Visite guidée de la Tour Eiffel avec accès VIP"
        )

        val response = client.put("/vacation/Paris/activities") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(updatedActivity)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

}

