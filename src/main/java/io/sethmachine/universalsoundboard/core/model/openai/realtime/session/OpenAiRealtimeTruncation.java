package io.sethmachine.universalsoundboard.core.model.openai.realtime.session;

/**
 * Controls how the conversation is truncated prior to model inference. Can be:
 * - String: "auto" (default) - OpenAiRealtimeTruncationAuto
 * - String: "disabled" - OpenAiRealtimeTruncationDisabled
 * - Object: OpenAiRealtimeTruncationRetentionRatio with retention_ratio (0.0-1.0)
 */
public interface OpenAiRealtimeTruncation {}
