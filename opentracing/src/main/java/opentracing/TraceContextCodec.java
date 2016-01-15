/**
 * Copyright 2016 The OpenTracing Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package opentracing;

import java.util.Map;

/**
 * Encodes or Decodes a {@linkplain TraceContext trace context} in binary or text formats.
 *
 * <p>The toXXX methods are expected to serialize trace contexts into a pair of values representing
 * separately the trace context / span identity, and the trace attributes. This is done specifically
 * for binary protocols that may represent tracing identity in a dedicated fixed-length slot in the
 * binary message format, so that it can be inspected efficiently by the middleware / routing layers
 * without parsing the whole message.
 */
public interface TraceContextCodec {

  /**
   * Implementation-specific format of a span's identity along with any trace attributes.
   *
   * @param <E> encoding, for example {@code byte[]} for binary, or {@code Map<String, String>} for
   * text.
   */
  // Can instead explicitly create BinaryEncodedTraceContext, TextEncodedTraceContext, just.. cluttery
  interface EncodedTraceContext<E> {
    /** Encoded span identifier. */
    E spanIdentity();

    /** Encoded trace attributes, or null if none were encoded. */
    E traceAttributes();
  }

  /**
   * Encodes the trace context into a binary representation of the span's identity and trace
   * attributes.
   */
  EncodedTraceContext<byte[]> toBinary(TraceContext tc);

  /**
   * Decodes a trace context from a binary representation of the span's identity and trace
   * attributes.
   *
   * @throws IllegalArgumentException if the encoded data is malformed.
   */
  TraceContext fromBinary(EncodedTraceContext<byte[]> encoded);

  /**
   * Encodes the trace context into a text representation of the span's identity and trace
   * attributes.
   */
  EncodedTraceContext<Map<String, String>> toText(TraceContext tc);

  /**
   * Decodes a trace context from a text representation of the span's identity and trace
   * attributes.
   *
   * @throws IllegalArgumentException if the encoded data is malformed.
   */
  TraceContext fromText(EncodedTraceContext<Map<String, String>> encoded);
}