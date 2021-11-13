package io.sethmachine.universalsoundboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jdbi.v3.core.Jdbi;

import com.hubspot.rosetta.jdbi3.RosettaRowMapperFactory;

import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.sethmachine.universalsoundboard.db.daos.FoobarDAO;
import io.sethmachine.universalsoundboard.health.TemplateHealthCheck;
import io.sethmachine.universalsoundboard.resources.HelloWorldResource;


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
                    final Environment environment) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

//        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
//        DriverManager.getConnection(configuration.getDataSourceFactory().getUrl());
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "derby");
        jdbi.registerRowMapper(new RosettaRowMapperFactory());
        final FoobarDAO foobarDao = jdbi.onDemand(FoobarDAO.class);
//        foobarDao.createSomethingTable();

        final HelloWorldResource resource = new HelloWorldResource(
            configuration.getTemplate(),
            configuration.getDefaultName()
        );
        final TemplateHealthCheck healthCheck =
            new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
    }

}
