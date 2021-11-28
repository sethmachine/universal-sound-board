package io.sethmachine.universalsoundboard.guice;

import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.hubspot.rosetta.jdbi3.RosettaObjectMapper;
import com.hubspot.rosetta.jdbi3.RosettaRowMapperFactory;
import io.dropwizard.Configuration;
import io.dropwizard.jdbi3.JdbiFactory;
import io.sethmachine.universalsoundboard.UniversalSoundBoardConfiguration;
import io.sethmachine.universalsoundboard.core.concurrent.SinkAudioMixerRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.SinkAudioMixerRunnableFactory;
import io.sethmachine.universalsoundboard.db.daos.AudioMixerDAO;
import io.sethmachine.universalsoundboard.db.daos.AudioMixerWiringDAO;
import io.sethmachine.universalsoundboard.db.daos.FoobarDAO;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.jdbi.v3.core.Jdbi;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;

public class UniversalSoundBoardModule extends DropwizardAwareModule<Configuration> {

  @Override
  protected void configure() {
    final JdbiFactory factory = new JdbiFactory();
    final Jdbi jdbi = factory.build(
      environment(),
      ((UniversalSoundBoardConfiguration) configuration()).getDataSourceFactory(),
      "derby"
    );
    jdbi.registerRowMapper(new RosettaRowMapperFactory());
    jdbi
      .getConfig()
      .get(RosettaObjectMapper.class)
      .setObjectMapper(bootstrap().getObjectMapper());
    bind(Jdbi.class).annotatedWith(Names.named("JDBI")).toInstance(jdbi);
    install(new FactoryModuleBuilder().build(SinkAudioMixerRunnableFactory.class));

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

  @Provides
  public AudioMixerWiringDAO provideAudioMixerWiringDAO(@Named("JDBI") Jdbi jdbi) {
    return jdbi.onDemand(AudioMixerWiringDAO.class);
  }

  @Provides
  @Named("SinkThreadPoolExecutor")
  public ThreadPoolExecutor provideThreadPoolExecutor() {
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(
      8,
      100,
      60,
      TimeUnit.SECONDS,
      new LinkedBlockingQueue()
    );
    //    tpe.execute(new SinkAudioMixerRunnable(5));
    return tpe;
  }
}
