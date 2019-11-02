import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Db db = new Db("casemaker");
        Javalin app = Javalin.create().start(8080);
        RestApi.start(app);

    }
}
