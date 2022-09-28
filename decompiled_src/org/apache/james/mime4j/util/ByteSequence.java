package org.apache.james.mime4j.util;

public interface ByteSequence {
   ByteSequence EMPTY = new EmptyByteSequence();

   int length();

   byte byteAt(int var1);

   byte[] toByteArray();
}
