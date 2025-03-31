package com.lucwaw

import com.lucwaw.domain.Activity
import com.lucwaw.domain.Address
import com.lucwaw.domain.Person
import com.lucwaw.domain.Region
import com.lucwaw.model.Vacation
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDate

fun Application.configureRouting() {
    val vacations = mapOf<String, Vacation>(
        ("Paris" to Vacation(
            startDate = LocalDate.of(2025, 8, 2),
            endDate = LocalDate.of(2025, 8, 9),
            address = Address(
                name = "Paris",
                region = Region.IleDeFrance
            )
        ))
    )

    suspend fun ApplicationCall.getVacationOrNull() : Vacation? {
        val vacationName = validateParameter<String>("vacationName") ?: return null

        return if (vacations[vacationName] != null) {
            vacations[vacationName]
        } else {
            respond(HttpStatusCode.NotFound, "Vacation not found")
            return null
        }
    }
    routing {



        get("/") {
            call.respondText("Hello Vacation!")
        }


        route("/vacation") {
            route("/{vacationName}") {
                get {
                    call.getVacationOrNull()?.let { vacation ->
                        call.respond(vacation)
                    }
                }

                route("/persons") {
                    get {
                        val vacation = call.getVacationOrNull() ?: return@get

                        val items = vacation.personRepository.items
                        call.respond(items)
                    }

                    post {
                        val vacation = call.getVacationOrNull() ?: return@post

                        val person = call.validateJsonParameter<Person>() ?: return@post


                        try {
                            vacation.personRepository.addItem(person)
                            call.respond(HttpStatusCode.Created)
                        } catch (
                            e: IllegalArgumentException
                        ) {
                            call.respond(HttpStatusCode.Conflict, e.message ?: "Item already exists")
                        }
                    }

                    delete("/{personName}") {
                        val vacation = call.getVacationOrNull() ?: return@delete

                        val name = call.validateParameter<String>("personName") ?: return@delete

                        if (vacation.personRepository.removeItem(name)) {
                            call.respond(HttpStatusCode.NoContent)
                        } else {
                            call.respond(HttpStatusCode.NotFound)
                        }
                    }

                    put {
                        val vacation = call.getVacationOrNull() ?: return@put

                        val person = call.validateJsonParameter<Person>() ?: return@put


                        if (!vacation.personRepository.updateItem(person)) {
                            call.respond(HttpStatusCode.NotFound)
                            return@put
                        }
                        call.respond(HttpStatusCode.OK)
                        return@put
                    }
                    get("/{personName}") {
                        val vacation = call.getVacationOrNull() ?: return@get

                        val name = call.validateParameter<String>("personName") ?: return@get


                        val person = vacation.personRepository.itemByName(name)
                        if (person == null) {
                            call.respond(HttpStatusCode.NotFound)
                            return@get
                        }
                        call.respond(person)
                    }
                }

                route("/activities") {
                    get {
                        val vacation = call.getVacationOrNull() ?: return@get

                        val items = vacation.activityRepository.items
                        call.respond(items)
                    }

                    post {
                        val vacation = call.getVacationOrNull() ?: return@post


                        val activity = call.validateJsonParameter<Activity>() ?: return@post

                        addItemWithoutDuplicate(vacation, activity)
                    }

                    delete("/{activityName}") {
                        val vacation = call.getVacationOrNull() ?: return@delete


                        val name = call.validateParameter<String>("activityName") ?: return@delete


                        if (!vacation.activityRepository.removeItem(name)) {
                            call.respond(HttpStatusCode.NotFound)
                            return@delete
                        }
                        call.respond(HttpStatusCode.NoContent)

                    }
                    put {
                        val vacation = call.getVacationOrNull() ?: return@put


                        val activity = call.validateJsonParameter<Activity>() ?: return@put


                        if (!vacation.activityRepository.updateItem(activity)) {
                            call.respond(HttpStatusCode.NotFound)
                            return@put
                        }
                        call.respond(HttpStatusCode.OK)
                        return@put
                    }

                    get("/{activityName}") {
                        val vacation = call.getVacationOrNull() ?: return@get

                        val name = call.validateParameter<String>("activityName") ?: return@get


                        val activity = vacation.activityRepository.itemByName(name)
                        if (activity == null) {
                            call.respond(HttpStatusCode.NotFound)
                            return@get
                        }
                        call.respond(activity)
                    }
                }


            }


        }

        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")
    }


}


private suspend fun RoutingContext.addItemWithoutDuplicate(
    vacation: Vacation,
    activity: Activity
) {
    try {
        vacation.activityRepository.addItem(activity)
        call.respond(HttpStatusCode.Created)
    } catch (
        e: IllegalArgumentException
    ) {
        call.respond(HttpStatusCode.Conflict, e.message ?: "Item already exists")
    }
}

suspend inline fun <reified T : Any> RoutingCall.validateJsonParameter(): T? {
    val parameter = runCatching { receive<T>() }.getOrNull()
    if (parameter == null) {
        respond(HttpStatusCode.BadRequest)
        return null
    }
    return parameter

}

suspend inline fun <reified T> ApplicationCall.validateParameter(keyName: String): T? {
    val parameter = parameters[keyName]

    if (parameter.isNullOrEmpty()) {
        respond(HttpStatusCode.BadRequest, "$keyName is required")
        return null
    }

    return when (T::class) {
        String::class -> parameter as T
        Int::class -> parameter.toIntOrNull() as? T
        Long::class -> parameter.toLongOrNull() as? T
        Double::class -> parameter.toDoubleOrNull() as? T
        Boolean::class -> parameter.toBooleanStrictOrNull() as? T
        else -> {
            respond(HttpStatusCode.BadRequest, "Invalid parameter type")
            null
        }
    }
}


