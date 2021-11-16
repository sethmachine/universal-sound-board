package io.sethmachine.universalsoundboard.guice;

import org.jdbi.v3.core.Jdbi;

import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.hubspot.rosetta.jdbi3.RosettaRowMapperFactory;

import io.dropwizard.Configuration;
import io.dropwizard.jdbi3.JdbiFactory;
import io.sethmachine.universalsoundboard.UniversalSoundBoardConfiguration;
import io.sethmachine.universalsoundboard.db.daos.AudioMixerDAO;
import io.sethmachine.universalsoundboard.db.daos.FoobarDAO;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;


public class UniversalSoundBoardModule extends DropwizardAwareModule<Configuration> {

  @Override
  protected void configure() {
    final JdbiFactory factory = new JdbiFactory();
    final Jdbi jdbi = factory.build(environment(), ((UniversalSoundBoardConfiguration) configuration()).getDataSourceFactory(), "derby");
    jdbi.registerRowMapper(new RosettaRowMapperFactory());
    bind(Jdbi.class).annotatedWith(Names.named("JDBI")).toInstance(jdbi);

    configuration();
    environment();
    bootstrap();
  }

  @Provides
  public FoobarDAO provideFoobarDAO(@Named("JDBI") Jdbi jdbi) {
    return jdbi.onDemand(FoobarDAO.class);
  }

  @Provides
  public AudioMixerDAO provideAudioMixerDAO(@Named("JDBI") Jdbi jdbi) {
    return jdbi.onDemand(AudioMixerDAO.class);
  }
}