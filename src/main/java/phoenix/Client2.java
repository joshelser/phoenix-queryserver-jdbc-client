package phoenix;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Client2 extends BaseClient {
  
  public static void main(String[] args) throws Exception {
    Client2 client = new Client2();
    client.run(args);
  }

  @Override
  public String getSqlQuery() {
    return "SELECT Column1 FROM PQS.INTEGER_TABLE WHERE id = ?";
  }

  @Override
  public void updateStatement(PreparedStatement stmt) throws SQLException {
    stmt.setInt(1, 5000);
  }

  @Override
  public void verifyResults(ResultSet rs) throws SQLException {
    int column1 = rs.getInt("column1");
    if (column1 != 5000) {
      throw new RuntimeException("Expected column1 to be 5000 but was " + column1);
    }
  }  
}
