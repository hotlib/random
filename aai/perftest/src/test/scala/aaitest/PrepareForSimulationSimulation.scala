package aaitest

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class PrepareForSimulationSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("https://127.0.0.1:8443")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
		.acceptHeader("application/json")
		.contentTypeHeader("application/json")
		.userAgentHeader("curl/7.47.0")

	val headers_0 = Map(
		"X-FromAppId" -> "testapp",
		"X-TransactionId" -> "testtransaction")

	val uri1 = "https://127.0.0.1:8443/aai/v13"
	val scn = scenario("AaiSimulation")
		.exec(http("request_30")
			.delete("/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/1/1/tenants/tenant/1/vservers/vserver/1")
			.headers(headers_0)
			.basicAuth("AAI","AAI"))
		.pause(1)
		.exec(http("request_21")
			.delete("/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/1/1/tenants/tenant/1")
			.headers(headers_0)
			.basicAuth("AAI","AAI"))
		.pause(1)
		.exec(http("request_22")
			.delete("/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/1/1/tenants/tenant/2")
			.headers(headers_0)
			.basicAuth("AAI","AAI"))
		.pause(1)
		.exec(http("request_23")
			.delete("/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/1/1/tenants/tenant/3")
			.headers(headers_0)
			.basicAuth("AAI","AAI"))
		.pause(1)
		.exec(http("request_10")
			.put("/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/1/1")
			.headers(headers_0)
			.basicAuth("AAI","AAI"))
		.pause(1)
		// CREATE
		.exec(http("request_0")
		.put("/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/1/1")
		.headers(headers_0)
		.body(RawFileBody("AaiSimulation_0000_request.txt"))
		.basicAuth("AAI","AAI"))
		.pause(7)
		.exec(http("request_1")
			.put("/aai/v13/business/customers/customer/1")
			.headers(headers_0)
			.body(RawFileBody("AaiSimulation_0001_request.txt"))
			.basicAuth("AAI","AAI")
			.check(status.is(412)))
		.pause(16)
		.exec(http("request_2")
			.put("/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/1/1/tenants/tenant/1")
			.headers(headers_0)
			.body(RawFileBody("AaiSimulation_0002_request.txt"))
			.basicAuth("AAI","AAI"))
		.pause(7)
		.exec(http("request_3")
			.put("/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/1/1/tenants/tenant/1/vservers/vserver/1")
			.headers(headers_0)
			.body(RawFileBody("AaiSimulation_0003_request.txt"))
			.basicAuth("AAI","AAI"))
		.pause(36)


	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}