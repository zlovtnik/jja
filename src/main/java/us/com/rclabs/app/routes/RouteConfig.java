package us.com.rclabs.app.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

/**
 * Mano, essa classe aqui é a RouteConfig Ela manja dos paranauê de configurar as rotas do Camel
 * pra pegar os XML e mandar umas req SOAP pros brother.
 */
@Component
public class RouteConfig extends RouteBuilder {

    /** Namespace padrão da API? Não mexe não */
    private static final String NAMESPACE = "http://xmlns.oracle.com/oxp/service/v2";

    /** Injeta as config do ambiente, mano. Deus é mais! */
    @Autowired
    private Environment environment;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RouteConfig(Environment environment, @Lazy @Qualifier("amqpTemplate") RabbitTemplate rabbitTemplate) {
        this.environment = environment;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Função responsa que configura a rota do Camel, tá ligado?
     *
     * @throws Exception se der alguma treta na hora de configurar
     */
    @Override
    public void configure() throws Exception {

        final String SOAP_ENDPOINT = environment.getProperty("oracle.url");

        from("timer:xmlPollingTimer?period=5000")
            .routeId("xmlPollingRoute")
            .log(LoggingLevel.INFO, "Processando arquivo XML da hora: ${header.CamelFileName}")
            .setHeader("SOAPAction", constant(NAMESPACE + "/getListOfSubjectArea"))
            .setHeader("Content-Type", constant("text/xml;charset=UTF-8"))
            .setBody(constant(createXmlPayload()))
            .to("spring-ws:" + SOAP_ENDPOINT + "?messageFactory=#messageFactory")
            .log(LoggingLevel.INFO, "Fechou! Requisição SOAP terminada pro arquivo ${header.CamelFileName}")
            .to("log:us.com.rclabs.app.routes?level=INFO");

        from("rest:post:/xml/process")
            .routeId("xmlProcessRoute")
            .log(LoggingLevel.INFO, "Received XML via REST: ${body}")
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    String xml = exchange.getIn().getBody(String.class);
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
                    Element root = doc.getDocumentElement();
                    String tag1 = getTagValue(root, "tag1");
                    String tag2 = getTagValue(root, "tag2");
                    String tag3 = getTagValue(root, "tag3");
                    String tag4 = getTagValue(root, "tag4");
                    // Send to RabbitMQ as JSON
                    String json = String.format("{\"tag1\":\"%s\",\"tag2\":\"%s\",\"tag3\":\"%s\",\"tag4\":\"%s\"}", tag1, tag2, tag3, tag4);
                    rabbitTemplate.convertAndSend(
                        com.example.jja.config.RabbitMQConfig.EXCHANGE_NAME,
                        "report.request",
                        json
                    );
                }
                private String getTagValue(Element root, String tag) {
                    try {
                        return root.getElementsByTagName(tag).item(0).getTextContent();
                    } catch (Exception e) {
                        return null;
                    }
                }
            })
            .setBody(constant("<response>OK</response>"))
            .setHeader(Exchange.CONTENT_TYPE, constant("application/xml"));
    }

    /**
     * Cria o XML maroto pra mandar na req SOAP, tá ligado?
     * Detalhe no hardcode :sunglasses:
     * @return XML montado em String pros parça
     */
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