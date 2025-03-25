package us.com.rclabs.app.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

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