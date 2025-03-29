package com.lucwaw

import com.lucwaw.domain.Activity
import com.lucwaw.domain.Address
import com.lucwaw.domain.Region
import com.lucwaw.model.Vacation
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDate

fun Application.configureRouting() {
    routing {

        val vacation = Vacation(
            startDate = LocalDate.of(2025, 8, 2),
            endDate = LocalDate.of(2025, 8, 9),
            address = Address(
                name = "Paris",
                region = Region.IleDeFrance
            )
        )

        get("/") {
            call.respondText("Hello Vacation!")
        }

        route("/vacation") {
            route("/{vacationName}") {
                get {
                    val name = call.parameters["vacationName"]
                    if (name == null) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@get
                    }


                    call.respond(vacation)
                }
            }
            route("/{vacationName}/activities") {
                get {
                    val name = call.parameters["vacationName"]
                    if (name == null) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@get
                    }

                    val activities = vacation.activityRepository.items
                    call.respond(activities)
                }

                post {

                    val activity = call.runCatching { receive<Activity>() }.getOrNull()
                    if (activity == null) {
                        log.error("Invalid activity data")
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }

                    vacation.activityRepository.upsertItem(activity)
                    call.respond(HttpStatusCode.Created)
                }

            }

            post {



                /*val destination = call.runCatching { receive<Address>() }.getOrNull()
                if (destination == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Invalid destination data, Note : region should be in ${
                            Region.entries.toTypedArray().map { it.name }
                        }"
                    )
                    return@post
                }

                call.respond(HttpStatusCode.Created)*/

            }

            delete("/{destinationName}") {
                /*val name = call.parameters["destinationName"]
                if (name == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }

                if (PeopleRepository.removeDestination(name)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }*/
            }

            get {
                /*val destinations = PeopleRepository.getDestinations()
                call.respond(destinations)*/
            }

        }

        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")
    }
}
