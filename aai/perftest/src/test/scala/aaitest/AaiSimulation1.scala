package aaitest

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

class AaiSimulation1 extends Simulation {

	val username = "AAI"
	val password = "AAI"
	val tenantIdFeeder = csv("tenant_ids.csv").queue

	val httpCommon = http.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
		.acceptHeader("application/json")
		.contentTypeHeader("application/json")
		.userAgentHeader("curl/7.47.0")

	val httpServer1 = httpCommon.baseURL("https://127.0.0.1:8443")

	val httpServer2 = httpCommon.baseURL("https://127.0.0.1:8443")

	val headers_0 = Map(
	"X-FromAppId" -> "testapp",
	"X-TransactionId" -> "testtransaction")

	def printValue() = exec(session => {
		val maybeId = session.get("resourceversion").asOption[String]
		println("resourceversion =======" + maybeId.getOrElse("COULD NOT FIND ID"))
		session
	})

	def resVersion( resourceVersion: String) = if (isBlank(resourceVersion)) "" else ", \"resource-version:\"" + resourceVersion

	def isBlank( input : String) : Boolean = input != null && !input.trim.isEmpty

	def resourceQueryParam(resourceVersion: String) = if (isBlank(resourceVersion)) "" else "?resource-version=" + resourceVersion

	def deleteTenant(tenantId: Int) = exec(http("request_delete")
		.delete("/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/1/1/tenants/tenant/" + tenantId + "?resource-version=${resourceversion}")
		.headers(headers_0)
		.basicAuth(username, password))

	def getTenant() = exec(http("request_get_tenant")
		.get("/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/1/1/tenants/tenant/"  + "${tenantid}")
		.check(jsonPath("$.resource-version").find.saveAs("resourceversion")	)
		.headers(headers_0)
		.basicAuth(username, password))

	def getTenant(index: Int) = exec(http("request_get_tenant")
		.get("/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/1/1/tenants/tenant/"  + index)
		.check(jsonPath("$.resource-version").find.saveAs("resourceversion")	)
		.headers(headers_0)
		.basicAuth(username, password))

	def createTenant() : ChainBuilder = exec(http("request_create_tenant")
		.put("/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/1/1/tenants/tenant/" + "${tenantid}")
		.headers(headers_0)
		.body(StringBody("{" +
			"\"tenant-id\": ${tenantid}," +
			"\"tenant-name\": \"testtenant\"," +
			"\"tenant-context\": \"tenantcontext\"" +
			"}"))
		.basicAuth(username, password)
		.check(status.in(201, 412)))

	def modifyTenant() : ChainBuilder =
		exec(
		http("request_modify_tenant")
		.put("/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/1/1/tenants/tenant/" + "${tenantid}")
		.headers(headers_0)
		.body(ElFileBody("modifytenant.json"))
		.basicAuth(username, password)
		.check(status.is(200)))


	def removeTenant(index: Int): ChainBuilder = exec(getTenant(), deleteTenant(index))

	val scenario1Server1Actions: ChainBuilder = exec(
		createTenant(), getTenant(),
		modifyTenant(), getTenant(), getTenant(),
		modifyTenant(), getTenant(), getTenant(),
		modifyTenant(), getTenant(), getTenant(),
	)

	val scenario1Server2Actions: ChainBuilder = exec(
		createTenant(), getTenant(),
		modifyTenant(), getTenant(),
		modifyTenant(), getTenant(),
		modifyTenant(), getTenant(),
		modifyTenant(), getTenant(),
		modifyTenant(), getTenant(),
	)


	val scenario1Server1 = scenario("Server 1 Scenario 1")
	.feed(tenantIdFeeder)
	.exec(scenario1Server1Actions)
 	.pause(10)

	val scenario1Server2 = scenario("Server 2 Scenario 1")
	.feed(tenantIdFeeder)
	.exec(scenario1Server2Actions)
 	.pause(10)

	//setUp(scenario1Server1.inject(atOnceUsers(1)).protocols(httpServer1), scenario1Server2.inject(atOnceUsers(1)).protocols(httpServer2))
	setUp(scenario1Server1.inject(atOnceUsers(1)).protocols(httpServer1))

}

