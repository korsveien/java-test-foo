import io.javalin.Javalin;
import io.restassured.RestAssured;
import lombok.SneakyThrows;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.ServerSocket;

import static io.restassured.RestAssured.when;
import static org.hamcrest.core.IsEqual.equalTo;

public class RestRestApiTest {


    @SneakyThrows
    static int getRandomPort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    @BeforeClass
    public static void setUp() {
        int port = getRandomPort();
        Javalin app = Javalin.create().start(port);
        RestAssured.port = port;
        RestApi.start(app);
    }

    @Test
    public void skal_returnere_id() {
        when().get("/foo/{id}", 5)
                .then()
                .statusCode(200)
                .body("id", equalTo(5));
    }

}
