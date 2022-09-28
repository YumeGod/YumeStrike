package org.apache.bcel.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public final class ByteSequence extends DataInputStream {
   private ByteArrayStream byte_stream;

   public ByteSequence(byte[] bytes) {
      super(new ByteArrayStream(bytes));
      this.byte_stream = (ByteArrayStream)super.in;
   }

   public final int getIndex() {
      return this.byte_stream.getPosition();
   }

   final void unreadByte() {
      this.byte_stream.unreadByte();
   }

   private static final class ByteArrayStream extends ByteArrayInputStream {
      ByteArrayStream(byte[] bytes) {
         super(bytes);
      }

      final int getPosition() {
         return super.pos;
      }

      final void unreadByte() {
         if (super.pos > 0) {
            --super.pos;
         }

      }
   }
}
