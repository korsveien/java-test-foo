import lombok.SneakyThrows;

import java.sql.Connection;
import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

public class DbTransaction<T> {
    private Connection connection;
    private T value;

    private DbTransaction(Connection connection, T value) {
        this.connection = connection;
        this.value = value;
    }

    public Connection getConnection() {
        return connection;
    }

    public T getValue() {
        return value;
    }

    public <V> DbTransaction<V> andThen(BiFunction<Connection, T, DbTransaction<V>> function) {
        return function.apply(connection, value);
    }

    @SneakyThrows
    public T commit() {
        connection.close();
        return value;
    }

    public static <T> DbTransaction<T> of(Connection conn, T value) {
        return new DbTransaction<>(requireNonNull(conn), value);
    }
}

