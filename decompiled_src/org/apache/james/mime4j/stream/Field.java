package org.apache.james.mime4j.stream;

import org.apache.james.mime4j.util.ByteSequence;

public interface Field {
   String getName();

   String getBody();

   ByteSequence getRaw();
}
