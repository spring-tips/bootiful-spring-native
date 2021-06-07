package com.example.imperative

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.servlet.function.router
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@SpringBootApplication
class ImperativeApplication

fun main(args: Array<String>) {
    runApplication<ImperativeApplication>(*args) {
        addInitializers(beans {
            bean {
                router {
                    GET("/customers") {
                        ok().body(ref<CustomerRepository>().findAll())
                    }
                }
            }
        })
    }
}


interface CustomerRepository : JpaRepository<Customer, Int>

@Entity
@Table(name = "customer")
data class Customer(
    @Id @GeneratedValue var id: Int? = null,
    var name: String? = null
)