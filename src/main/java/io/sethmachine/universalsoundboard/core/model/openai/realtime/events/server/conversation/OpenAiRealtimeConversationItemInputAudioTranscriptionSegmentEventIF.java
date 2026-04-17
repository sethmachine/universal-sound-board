package io.sethmachine.universalsoundboard.core.model.openai.realtime.events.server.conversation;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEventCore;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeConversationItemInputAudioTranscriptionSegmentEventIF
  extends OpenAiRealtimeEventCore {
  String getId();

  String getItemId();

  int getContentIndex();

  String getText();

  String getSpeaker();

  double getStart();

  double getEnd();

  @Derived
  default String getType() {
    return "conversation.item.input_audio_transcription.segment";
  }
}
