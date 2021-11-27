package io.sethmachine.universalsoundboard.service;

import io.sethmachine.universalsoundboard.db.daos.AudioMixerDAO;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioMixersService {

  private static final Logger LOG = LoggerFactory.getLogger(AudioMixersService.class);

  private final AudioMixerDAO audioMixerDAO;

  @Inject
  public AudioMixersService(AudioMixerDAO audioMixerDAO) {
    this.audioMixerDAO = audioMixerDAO;
  }
}
