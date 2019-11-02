import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.util.function.Function;

class Db {

    private static HikariDataSource ds;

    Db(String name) {

        String jdbcUrl = "jdbc:h2:file:./target/" + name;
        ds = new HikariDataSource();
        ds.setJdbcUrl(jdbcUrl);
        ds.setUsername("sa");
        ds.setPassword("");

        Flyway
                .configure()
                .dataSource(ds)
                .load()
                .migrate();
    }

    @SneakyThrows
    static <T> T useConnection(Function<Connection, T> function) {
        try (Connection conn = ds.getConnection()) {
            return function.apply(conn);
        }
    }

}
