package com.gft.gft

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<GftApplication>().with(TestcontainersConfiguration::class).run(*args)
}
