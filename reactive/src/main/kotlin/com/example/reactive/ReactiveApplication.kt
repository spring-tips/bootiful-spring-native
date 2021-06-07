package com.example.reactive

import kotlinx.coroutines.reactive.asFlow
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.data.annotation.Id
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.coRouter

@SpringBootApplication
class ReactiveApplication

fun main(args: Array<String>) {
    runApplication<ReactiveApplication>(*args) {

        addInitializers(beans {
            bean {
                coRouter {
                    GET("/customers") {
                        ServerResponse.ok().bodyAndAwait(
                            ref<CustomerRepository>().findAll().asFlow()
                        )
                    }
                }
            }
        })
    }
}

interface CustomerRepository : ReactiveCrudRepository<Customer, Int>
data class Customer(@Id val id: Int, val name: String)