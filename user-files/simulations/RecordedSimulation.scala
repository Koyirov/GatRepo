
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulationKakuro extends Simulation {

	object Kakuro {

		val kakuro = exec(http("kakuro")
			.get("/kakuro")
			.headers(headers_0))
			.pause(2)
	}
  
	object Game {
	
		val set = repeat(15, "n"){
			exec(http("set")
			.get("/kakuro/set/${row} ${column} ${value}")
			.headers(headers_0))
			.pause(10)
		}
	}
	
	object Clear {
	
		val clear = repeat(2, "n"){
			exec(http("clear")
			.get("/kakuro/clear/${row} ${column}")
			.headers(headers_0))
			.pause(10)
		}
	}
  
	object Save {

		val save = exec(http("save")
			.get("/kakuro/save")
			.headers(headers_0))
			.pause(2)
		
	}

	object Load {

		val load = exec(http("load")
			.get("/kakuro/load")
			.headers(headers_0))
			.pause(31)
	}

	object Undo {

		val undo = repeat(2, "n"){ 
			exec(http("undo")
			.get("/kakuro/undo")
			.headers(headers_0))
			.pause(2)
		}
	}

	object Redo {

		val redo = repeat(2, "n"){
			exec(http("redo")
			.get("/kakuro/redo")
			.headers(headers_0))
			.pause(2)
		}
	}

	val httpProtocol = http
		.baseUrl("http://0.0.0.0:8090")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("de,en-US;q=0.7,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0")


	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val headers_18 = Map(
		"DNT" -> "1",
		"Pragma" -> "no-cache",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_19 = Map(
		"Accept" -> "image/webp,*/*",
		"DNT" -> "1",
		"Pragma" -> "no-cache")


	val feeder = csv("set.csv").random
	val player = scenario("RecordedSimulationKakuro").feed(feeder).exec(Kakuro.kakuro, Game.set, Clear.clear, Save.save, Load.load, Undo.undo, Redo.redo)
		
	setUp(player.inject(rampUsers(500) during (10 seconds))).protocols(httpProtocol)
	//setUp(player.inject(atOnceUsers(100))).protocols(httpProtocol)
	
}