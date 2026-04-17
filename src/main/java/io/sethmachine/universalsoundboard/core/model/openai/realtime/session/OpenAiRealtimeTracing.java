package io.sethmachine.universalsoundboard.core.model.openai.realtime.session;

/**
 * Session tracing configuration. Can be:
 * - String: "auto" to create a trace with default values (OpenAiRealtimeTracingAuto)
 * - Object: OpenAiRealtimeTracingConfig for custom workflow name, group ID, and metadata
 */
public interface OpenAiRealtimeTracing {}
