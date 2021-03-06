package io.sethmachine.universalsoundboard;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.mixins.AudioFormatEncodingMixIn;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.mixins.AudioFormatMixIn;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.mixins.AudioFormatMixInWithoutProperties;
import io.sethmachine.universalsoundboard.db.liquibase.LiquibaseMigrator;
import io.sethmachine.universalsoundboard.guice.UniversalSoundBoardModule;
import io.sethmachine.universalsoundboard.health.DatabaseHealthCheck;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import liquibase.exception.LiquibaseException;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class UniversalSoundBoardApplication
  extends Application<UniversalSoundBoardConfiguration> {

  public static void main(final String[] args) throws Exception {
    new UniversalSoundBoardApplication().run(args);
  }

  @Override
  public String getName() {
    return "UniversalSoundBoard";
  }

  @Override
  public void initialize(final Bootstrap<UniversalSoundBoardConfiguration> bootstrap) {
    // required to access private fields in the Encoding class
    bootstrap
      .getObjectMapper()
      .setVisibility(
        VisibilityChecker.Std.defaultInstance().withFieldVisibility(Visibility.ANY)
      );
    bootstrap.getObjectMapper().addMixIn(AudioFormat.class, AudioFormatMixIn.class);
    bootstrap
      .getObjectMapper()
      .addMixIn(AudioFormat.class, AudioFormatMixInWithoutProperties.class);
    bootstrap.getObjectMapper().addMixIn(Encoding.class, AudioFormatEncodingMixIn.class);
    bootstrap.addBundle(
      GuiceBundle
        .builder()
        .enableAutoConfig(getClass().getPackage().getName())
        .modules(new UniversalSoundBoardModule())
        .build()
    );

    bootstrap.addBundle(
      new MigrationsBundle<UniversalSoundBoardConfiguration>() {
        @Override
        public DataSourceFactory getDataSourceFactory(
          UniversalSoundBoardConfiguration configuration
        ) {
          return configuration.getDataSourceFactory();
        }
      }
    );
    bootstrap.addBundle(new MultiPartBundle());
  }

  @Override
  public void run(
    final UniversalSoundBoardConfiguration configuration,
    final Environment environment
  ) throws SQLException, LiquibaseException {
    final DatabaseHealthCheck databaseHealthCheck = new DatabaseHealthCheck(
      configuration.getDataSourceFactory().getUrl()
    );

    environment.healthChecks().register("databaseHealthCheck", databaseHealthCheck);

    LiquibaseMigrator.doMigrations(
      DriverManager.getConnection(configuration.getDataSourceFactory().getUrl())
    );
  }
}
