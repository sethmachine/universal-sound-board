package io.sethmachine.universalsoundboard.health;

import com.codahale.metrics.health.HealthCheck;
import java.sql.DriverManager;
import java.sql.SQLException;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;

public class DatabaseHealthCheck extends HealthCheck {

  private final String databaseConnectingString;

  public DatabaseHealthCheck(String databaseConnectionString) {
    this.databaseConnectingString = databaseConnectionString;
  }

  @Override
  protected Result check() throws Exception {
    try {
      final Database database = DatabaseFactory
        .getInstance()
        .findCorrectDatabaseImplementation(
          new JdbcConnection(DriverManager.getConnection(databaseConnectingString))
        );
      database.close();
      return Result.healthy();
    } catch (DatabaseException | SQLException e) {
      e.printStackTrace();
      return Result.unhealthy(
        "Unable to connect to the database.  Connection string: " +
        databaseConnectingString
      );
    }
  }
}
