import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static org.assertj.core.api.Assertions.assertThat;

public class WiremockTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    private static Client client = ClientBuilder.newClient();

    @AfterClass
    public static void tearDown() {
        client.close();
    }

    @Test
    public void skal_foo() {

        givenThat(post(urlMatching("/my/resource/[a-z0-9]+"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader(CONTENT_TYPE, APPLICATION_XML)
                        .withBody("<message>1234</message>")
                ));

        Response response = client
                .target(String.format("http://localhost:%s", wireMockRule.port()))
                .path("/my/resource/1")
                .request(APPLICATION_XML)
                .post(Entity.entity("<message>1234</message>", APPLICATION_XML));

        assertThat(response.getStatus()).isEqualTo(201);

        verify(postRequestedFor(urlMatching("/my/resource/[a-z0-9]+"))
                .withRequestBody(matching(".*<message>1234</message>.*"))
                .withHeader("Content-Type", matching("application/xml")));
    }
}