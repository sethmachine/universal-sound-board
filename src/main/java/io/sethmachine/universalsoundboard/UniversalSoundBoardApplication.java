package io.sethmachine.universalsoundboard;

import java.sql.DriverManager;
import java.sql.SQLException;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.sethmachine.universalsoundboard.db.liquibase.LiquibaseMigrator;
import io.sethmachine.universalsoundboard.guice.UniversalSoundBoardModule;
import io.sethmachine.universalsoundboard.health.TemplateHealthCheck;
import liquibase.exception.LiquibaseException;
import ru.vyarus.dropwizard.guice.GuiceBundle;


public class UniversalSoundBoardApplication extends Application<UniversalSoundBoardConfiguration> {

    public static void main(final String[] args) throws Exception {
        new UniversalSoundBoardApplication().run(args);
    }

    @Override
    public String getName() {
        return "UniversalSoundBoard";
    }

    @Override
    public void initialize(final Bootstrap<UniversalSoundBoardConfiguration> bootstrap) {

        bootstrap.addBundle(GuiceBundle.builder()
            .enableAutoConfig(getClass().getPackage().getName())
            .modules(new UniversalSoundBoardModule())
            .build());

        bootstrap.addBundle(new MigrationsBundle<UniversalSoundBoardConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(UniversalSoundBoardConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        // TODO: application initialization
//        String dbUrl = "jdbc:derby:/Users/sdworman/foobardb;create=true";
//        try {
//            Connection conn = DriverManager.getConnection(dbUrl);
//            int foo = 5;
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        final DBIFactory factory = new DBIFactory();
//        final DataSourceFactory dataSourceFactory = configuration.getDataSourceFactory();
//        final DBI jdbi = factory.build(environment, dataSourceFactory, "derby");
    }

    @Override
    public void run(final UniversalSoundBoardConfiguration configuration,
                    final Environment environment) throws SQLException, LiquibaseException {
//        final JdbiFactory factory = new JdbiFactory();
//        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "derby");
//        jdbi.registerRowMapper(new RosettaRowMapperFactory());
//        final FoobarDAO foobarDao = jdbi.onDemand(FoobarDAO.class);
        LiquibaseMigrator.doMigrations(DriverManager.getConnection(configuration.getDataSourceFactory().getUrl()));

//        final HelloWorldResource resource = new HelloWorldResource(
//            configuration.getTemplate(),
//            configuration.getDefaultName()
//        );
        final TemplateHealthCheck healthCheck =
            new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
//        environment.jersey().register(resource);
    }
}
