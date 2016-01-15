package phoenix;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Client1 extends BaseClient {
  
  public static void main(String[] args) throws Exception {
    Client1 client = new Client1();
    client.run(args);
  }

  @Override
  public String getSqlQuery() {
    return "SELECT Column1 FROM PQS.VARCHAR_TABLE WHERE id = ?";
  }

  @Override
  public void updateStatement(PreparedStatement stmt) throws SQLException {
    stmt.setInt(1, 5000);
  }

  @Override
  public void verifyResults(ResultSet rs) throws SQLException {
    String column1 = rs.getString("column1");
    if (!column1.equals("str_5000")) {
      throw new RuntimeException("Expected column1 to be str_5000 but was " + column1);
    }
  }
  
}
