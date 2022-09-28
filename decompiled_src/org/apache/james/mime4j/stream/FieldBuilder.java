package org.apache.james.mime4j.stream;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.util.ByteArrayBuffer;

public interface FieldBuilder {
   void reset();

   void append(ByteArrayBuffer var1) throws MimeException;

   RawField build() throws MimeException;

   ByteArrayBuffer getRaw();
}
