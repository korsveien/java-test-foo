import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

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

    @SneakyThrows
    static <T> DbTransaction<T> useTransaction(Function<Connection, T> function) {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            requireNonNull(conn).rollback();
        }
        return DbTransaction.of(conn, function.apply(conn));
    }

    public static void main(String[] args) {
        Long foo = useTransaction(conn -> {
            try {
                conn.createStatement().executeQuery("SELECT 1 FROM DUAL");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return LocalDate.now();
        }).andThen((conn, value) -> {
            long l = value.toEpochDay();
            try {
                conn.createStatement().executeQuery("SELECT 1 FROM DUAL");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return DbTransaction.of(conn, l);
        }).commit();
    }
}
