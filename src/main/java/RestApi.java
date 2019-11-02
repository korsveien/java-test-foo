import io.javalin.Javalin;

public class RestApi {

    public static void start(Javalin app) {
        app.get("/foo/:id", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.contentType("application/json");
            ctx.json(new DTO(id));
        });
    }
}
