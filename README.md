## Phoenix QueryServer JDBC Example

This codebase is meant to create some tables using the Phoenix QueryServer (PQS)
and verify that results are not seen across separate clients.

### Build the code

`mvn package`

### Create the tables

`java -cp ${PHOENIX_HOME}/phoenix-${PHOENIX_VERSION}-thin-client.jar:target/pqs-jdbc-example-0.0.1-SNAPSHOT.jar phoenix.CreateTables 'http://localhost:8765;serialization=PROTOBUF'`

### Run the queries concurrently

`java -cp ${PHOENIX_HOME}/phoenix-${PHOENIX_VERSION}-thin-client.jar:target/pqs-jdbc-example-0.0.1-SNAPSHOT.jar phoenix.Client1 'http://localhost:8765;serialization=PROTOBUF'`

`java -cp ${PHOENIX_HOME}/phoenix-${PHOENIX_VERSION}-thin-client.jar:target/pqs-jdbc-example-0.0.1-SNAPSHOT.jar phoenix.Client2 'http://localhost:8765;serialization=PROTOBUF'`
