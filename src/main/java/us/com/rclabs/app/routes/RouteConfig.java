package us.com.rclabs.app.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class RouteConfig extends RouteBuilder {

    private static final String NAMESPACE = "http://xmlns.oracle.com/oxp/service/v2";
    private static final String SOAP_ENDPOINT = "spring-ws:https://eksz-test.fa.la1.oraclecloud.com:443/xmlpserver/services/v2/ReportService";

    @Autowired
    private Environment environment;

    @Override
    public void configure() throws Exception {
        from("timer:xmlPollingTimer?period=5000")
            .routeId("xmlPollingRoute")
            .log(LoggingLevel.INFO, "Processing new XML file: ${header.CamelFileName}")
            .setHeader("SOAPAction", constant(NAMESPACE + "/getListOfSubjectArea"))
            .setHeader("Content-Type", constant("text/xml;charset=UTF-8"))
            .setBody(constant(createXmlPayload()))
            .to(SOAP_ENDPOINT + "?messageFactory=#messageFactory")
            .log(LoggingLevel.INFO, "SOAP request completed for ${header.CamelFileName}")
            .to("log:us.com.rclabs.app.routes?level=INFO");
    }

    private String createXmlPayload() {
        return String.format("""
            <getListOfSubjectArea xmlns="%s">
                <userID>%s</userID>
                <password>%s</password>
                <connectionName>wilCuzao</connectionName>
            </getListOfSubjectArea>
            """, NAMESPACE, environment.getProperty("oracle.username"), environment.getProperty("oracle.password"));
    }

}