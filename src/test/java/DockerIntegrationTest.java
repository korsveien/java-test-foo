import org.junit.*;
import org.testcontainers.containers.OracleContainer;

import java.sql.*;

import static org.assertj.core.api.Assertions.assertThat;

public class DockerIntegrationTest {

    @ClassRule
    public static OracleContainer oracle = new OracleContainer("wnameless/oracle-xe-11g-r2");

    private static Connection conn;

    @BeforeClass
    public static void setup() throws SQLException {
        conn = DriverManager.getConnection(oracle.getJdbcUrl(), oracle.getUsername(), oracle.getPassword());
        Statement stm = conn.createStatement();
        stm.executeUpdate("create table bruker (fnr varchar2(11), navn varchar2(255))");
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        conn.close();
    }

    @After
    public void afterEach() throws Exception {
        conn.prepareStatement("truncate table bruker").executeUpdate();
    }

    @Test
    public void skal_teste_db_1() throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("insert into bruker (fnr, navn) values (?, ?)");
        pstm.setString(1, "12345678901");
        pstm.setString(2, "Test Testesen");
        int i = pstm.executeUpdate();
        assertThat(i).isEqualTo(1);
        ResultSet rs = conn.createStatement().executeQuery("select * from bruker");

        boolean firstRow = rs.next();
        boolean secondRow = rs.next();
        assertThat(firstRow).isTrue();
        assertThat(secondRow).isFalse();

    }

    @Test
    public void skal_teste_db_2() throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("insert into bruker (fnr, navn) values (?, ?)");
        pstm.setString(1, "09876543211");
        pstm.setString(2, "Navn Navnesen");
        int i = pstm.executeUpdate();
        assertThat(i).isEqualTo(1);

        ResultSet rs = conn.createStatement().executeQuery("select * from bruker");
        boolean firstRow = rs.next();
        boolean secondRow = rs.next();
        assertThat(firstRow).isTrue();
        assertThat(secondRow).isFalse();
    }
}
