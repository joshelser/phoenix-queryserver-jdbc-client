/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package phoenix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 
 */
public abstract class BaseClient {

  public abstract String getSqlQuery();

  public abstract void updateStatement(PreparedStatement stmt) throws SQLException;

  public abstract void verifyResults(ResultSet rs) throws SQLException;

  public void run(String[] args) throws Exception {
    // Test that the Driver is loaded so DriverManager works (works around PHOENIX a bug)
    Class.forName("org.apache.phoenix.queryserver.client.Driver");

    if (args.length < 1) {
      throw new RuntimeException("Usage: " + getClass().getSimpleName() + " <pqs_url>");
    }

    final String url = args[0];

    long iterations = 0;
    try (Connection conn = DriverManager.getConnection("jdbc:phoenix:thin:url=" + url)) {
      conn.setAutoCommit(true);
      
      String sqlStmt = getSqlQuery();
      System.out.println("SQL Statement:\n\t" + sqlStmt);
      
      while (true) {
        if (iterations == 10000) {
          System.out.println(new Date() + " 10k iterations");
          iterations = 0;
        }
        PreparedStatement stmt = conn.prepareStatement(sqlStmt);
        updateStatement(stmt);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
          verifyResults(rs);
        }

        stmt.close();
        iterations++;
      }
    }
  }
}
