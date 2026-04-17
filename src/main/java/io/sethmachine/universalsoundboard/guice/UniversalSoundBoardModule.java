package io.sethmachine.universalsoundboard.guice;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jdbi.v3.core.Jdbi;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.eventbus.EventBus;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.hubspot.rosetta.jdbi3.RosettaObjectMapper;
import com.hubspot.rosetta.jdbi3.RosettaRowMapperFactory;

import io.dropwizard.Configuration;
import io.dropwizard.jdbi3.JdbiFactory;
import io.sethmachine.universalsoundboard.UniversalSoundBoardConfiguration;
import io.sethmachine.universalsoundboard.core.concurrent.openai.OpenAiRealtimeClient;
import io.sethmachine.universalsoundboard.core.concurrent.openai.OutboundVoiceAgentRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.openai.VoiceAgentSinkRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.openai.VoiceAgentSourceRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.sink.SinkAudioMixerRunnableFactory;
import io.sethmachine.universalsoundboard.core.concurrent.source.PlayAudioToSourceRunnableFactory;
import io.sethmachine.universalsoundboard.core.concurrent.source.SourceAudioMixerRunnableFactory;
import io.sethmachine.universalsoundboard.db.daos.AudioMixerDAO;
import io.sethmachine.universalsoundboard.db.daos.AudioMixerWiringDAO;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;

public class UniversalSoundBoardModule extends DropwizardAwareModule<Configuration> {

  public static final String OPEN_AI_OBJECT_MAPPER = "OpenAiObjectMapper";

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
    install(new FactoryModuleBuilder().build(PlayAudioToSourceRunnableFactory.class));
    install(new FactoryModuleBuilder().build(SourceAudioMixerRunnableFactory.class));
    install(new FactoryModuleBuilder().build(OpenAiRealtimeClient.Factory.class));
    install(new FactoryModuleBuilder().build(OutboundVoiceAgentRunnable.Factory.class));
    install(new FactoryModuleBuilder().build(VoiceAgentSourceRunnable.Factory.class));
    install(new FactoryModuleBuilder().build(VoiceAgentSinkRunnable.Factory.class));

    configuration();
    environment();
    bootstrap();
  }

  @Provides
  @Singleton
  public AudioMixerDAO provideAudioMixerDAO(@Named("JDBI") Jdbi jdbi) {
    return jdbi.onDemand(AudioMixerDAO.class);
  }

  @Provides
  @Singleton
  public AudioMixerWiringDAO provideAudioMixerWiringDAO(@Named("JDBI") Jdbi jdbi) {
    return jdbi.onDemand(AudioMixerWiringDAO.class);
  }

  @Provides
  @Singleton
  @Named("SinkThreadPoolExecutor")
  public ThreadPoolExecutor provideThreadPoolExecutorForSinks() {
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(
      8,
      100,
      60,
      TimeUnit.SECONDS,
      new LinkedBlockingQueue()
    );
    return tpe;
  }

  @Provides
  @Singleton
  @Named("PlayAudioToSourceThreadPoolExecutor")
  public ThreadPoolExecutor provideThreadPoolExecutorForPlayingAudioToSource() {
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(
      8,
      100,
      60,
      TimeUnit.SECONDS,
      new LinkedBlockingQueue()
    );
    return tpe;
  }

  @Provides
  @Singleton
  @Named("SourceCaptureThreadPoolExecutor")
  public ThreadPoolExecutor provideThreadPoolExecutorForSourceCapture() {
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(
      8,
      100,
      60,
      TimeUnit.SECONDS,
      new LinkedBlockingQueue()
    );
    return tpe;
  }

  @Provides
  @Singleton
  @Named("SinkEventBus")
  public EventBus provideEventBusForSinks() {
    return new EventBus();
  }

  @Provides
  @Singleton
  @Named("SourceEventBus")
  public EventBus provideEventBusForSources() {
    return new EventBus();
  }

  @Provides
  @Singleton
  @Named(OPEN_AI_OBJECT_MAPPER)
  public ObjectMapper providesOpenAiObjectMapper() {
    return new ObjectMapper()
      .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
      .setSerializationInclusion(Include.NON_ABSENT)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .findAndRegisterModules();
  }
}
