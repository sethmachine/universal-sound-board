package io.sethmachine.universalsoundboard.db.liquibase;

import java.sql.Connection;
import java.sql.SQLException;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

public class LiquibaseMigrator {

  private static final String MIGRATIONS_FILE = "migrations.xml";

  public static void doMigrations(Connection connection)
    throws LiquibaseException, SQLException {
    try {
      Database database = DatabaseFactory
        .getInstance()
        .findCorrectDatabaseImplementation(new JdbcConnection(connection));
      Liquibase liquibase = new Liquibase(
        MIGRATIONS_FILE,
        new ClassLoaderResourceAccessor(),
        database
      );
      liquibase.update(new Contexts());
    } finally {
      if (connection != null) {
        connection.rollback();
        connection.close();
      }
    }
  }
}
