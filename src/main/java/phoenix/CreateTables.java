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
import java.sql.Statement;

public class CreateTables {

  public static final String INTEGER_TABLE = "PQS.INTEGER_TABLE";
  public static final String VARCHAR_TABLE = "PQS.VARCHAR_TABLE";
  
  public static void main(String[] args) throws Exception {
    // Test that the Driver is loaded so DriverManager works (works around PHOENIX a bug)
    Class.forName("org.apache.phoenix.queryserver.client.Driver");

    if (args.length < 1) {
      throw new RuntimeException("Usage: CreateTables <pqs_url>");
    }

    final String url = args[0];

    try (Connection conn = DriverManager.getConnection("jdbc:phoenix:thin:url=" + url)) {
      conn.setAutoCommit(false);

      try (Statement stmt = conn.createStatement()) {
        stmt.execute("drop table if exists " + INTEGER_TABLE);
        stmt.execute("create table " + INTEGER_TABLE + " (id integer not null primary key, column1 integer)");
      }
      conn.commit();

      try (PreparedStatement stmt = conn.prepareStatement("UPSERT INTO " + INTEGER_TABLE + " VALUES(?, ?)")) {
        for (int i = 0; i < 1000; i ++) {
          stmt.setInt(1, i);
          stmt.setInt(2, i);
          stmt.execute();
        }
      }
      conn.commit();

      try (Statement stmt = conn.createStatement()) {
        stmt.execute("drop table if exists " + VARCHAR_TABLE);
        stmt.execute("create table " + VARCHAR_TABLE + " (id integer not null primary key, column1 varchar(50)  )");
      }
      conn.commit();

      try (PreparedStatement stmt = conn.prepareStatement("UPSERT INTO " + VARCHAR_TABLE + " VALUES(?, ?)")) {
        for (int i = 0; i < 1000; i ++) {
          stmt.setInt(1, i);
          stmt.setString(2, "str_" + i);
          stmt.execute();
        }
      }
      conn.commit();
    }
  }
}
