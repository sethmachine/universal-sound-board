package io.sethmachine.universalsoundboard.db.audio.mixer;

import com.hubspot.rosetta.annotations.RosettaProperty;

public class AudioMixerRow {

  public AudioMixerRow(int id) {
    this.id = id;
  }

  private int id;
  private String name;

  public AudioMixerRow() {}

  @RosettaProperty("NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @RosettaProperty("ID")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
