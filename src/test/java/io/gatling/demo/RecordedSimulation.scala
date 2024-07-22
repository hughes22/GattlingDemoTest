package io.gatling.demo

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

  private val httpProtocol = http
    .baseUrl("https://computer-database.gatling.io")
    .inferHtmlResources(AllowList(), DenyList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*\.svg""", """.*detectportal\.firefox\.com.*"""))
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
    .acceptEncodingHeader("gzip, deflate, br")
    .acceptLanguageHeader("en-US,en;q=0.9")
    .upgradeInsecureRequestsHeader("1")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
  
  private val headers_0 = Map(
  		"priority" -> "u=0, i",
  		"sec-ch-ua" -> """Not/A)Brand";v="8", "Chromium";v="126", "Google Chrome";v="126""",
  		"sec-ch-ua-mobile" -> "?0",
  		"sec-ch-ua-platform" -> "Windows",
  		"sec-fetch-dest" -> "document",
  		"sec-fetch-mode" -> "navigate",
  		"sec-fetch-site" -> "cross-site",
  		"sec-fetch-user" -> "?1"
  )
  
  private val headers_1 = Map(
  		"priority" -> "u=0, i",
  		"sec-ch-ua" -> """Not/A)Brand";v="8", "Chromium";v="126", "Google Chrome";v="126""",
  		"sec-ch-ua-mobile" -> "?0",
  		"sec-ch-ua-platform" -> "Windows",
  		"sec-fetch-dest" -> "document",
  		"sec-fetch-mode" -> "navigate",
  		"sec-fetch-site" -> "same-origin",
  		"sec-fetch-user" -> "?1"
  )
  
  private val headers_2 = Map(
  		"origin" -> "https://computer-database.gatling.io",
  		"priority" -> "u=0, i",
  		"sec-ch-ua" -> """Not/A)Brand";v="8", "Chromium";v="126", "Google Chrome";v="126""",
  		"sec-ch-ua-mobile" -> "?0",
  		"sec-ch-ua-platform" -> "Windows",
  		"sec-fetch-dest" -> "document",
  		"sec-fetch-mode" -> "navigate",
  		"sec-fetch-site" -> "same-origin",
  		"sec-fetch-user" -> "?1"
  )
	val CreateComp = exec(
	http("CreateComputer")
		.post("/computers")
		.formParam("name", "hp")
		.formParam("introduced", "")
		.formParam("discontinued", "")
		.formParam("company", "26"),
	pause(9),
	)
	val SearchComp = exec(
		http("SearchComputer")
			.get("/computers?f=hp")
		,
		pause(26),
	)
	val SelectComp = exec(
		http("SelectComputer")
			.get("/computers/472")
		,
		pause(5),
	)
	val DeleteComp = exec(
		http("DeleteComputer")
			.post("/computers/472/delete")
	)

  private val scn = scenario("RecordedSimulation")
    .exec(
      http("VisitHomePage")
        .get("/computers")
        ,
      pause(8),
      http("GotoCreateComputer")
        .get("/computers/new")
        ,
      pause(19),
    )
	val Users = scenario("Users").exec(SearchComp)
	val admin = scenario("Admins").exec(CreateComp,SearchComp,SelectComp,DeleteComp)
	//setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
	setUp(
		Users.inject(rampUsers(15).during(5)),
		admin.inject(rampUsers(20).during(10))
	).protocols(httpProtocol)
}
