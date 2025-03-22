package com.lucwaw

import com.lucwaw.model.Destination
import com.lucwaw.model.DestinationRepository
import com.lucwaw.model.Region
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello Vacation!")
        }

        route("/destinations") {
            post {

                val destination = call.runCatching { receive<Destination>() }.getOrNull()
                if (destination == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Invalid destination data, Note : region should be in ${
                            Region.entries.toTypedArray().map { it.name }
                        }"
                    )
                    return@post
                }
                DestinationRepository.addUpdateDestination(destination)
                call.respond(HttpStatusCode.Created)

            }

            delete("/{destinationName}") {
                val name = call.parameters["destinationName"]
                if (name == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }

                if (DestinationRepository.removeDestination(name)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            get {
                val destinations = DestinationRepository.getDestinations()
                call.respond(destinations)
            }

        }

        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")
    }
}
