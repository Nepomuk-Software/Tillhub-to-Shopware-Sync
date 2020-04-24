package nepomuk.software.tillhubToShopwareSnyc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class Wolke7ConnectorApplication

fun main(args: Array<String>) {
	runApplication<Wolke7ConnectorApplication>(*args)
}
