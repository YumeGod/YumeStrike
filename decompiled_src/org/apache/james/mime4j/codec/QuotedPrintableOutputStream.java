package org.apache.james.mime4j.codec;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class QuotedPrintableOutputStream extends FilterOutputStream {
   private static final int DEFAULT_BUFFER_SIZE = 3072;
   private static final byte TB = 9;
   private static final byte SP = 32;
   private static final byte EQ = 61;
   private static final byte DOT = 46;
   private static final byte CR = 13;
   private static final byte LF = 10;
   private static final byte QUOTED_PRINTABLE_LAST_PLAIN = 126;
   private static final int QUOTED_PRINTABLE_MAX_LINE_LENGTH = 76;
   private static final int QUOTED_PRINTABLE_OCTETS_PER_ESCAPE = 3;
   private static final byte[] HEX_DIGITS = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70};
   private final byte[] outBuffer;
   private final boolean binary;
   private boolean pendingSpace;
   private boolean pendingTab;
   private boolean pendingCR;
   private int nextSoftBreak;
   private int outputIndex;
   private boolean closed;
   private byte[] singleByte;

   public QuotedPrintableOutputStream(int bufsize, OutputStream out, boolean binary) {
      super(out);
      this.closed = false;
      this.singleByte = new byte[1];
      this.outBuffer = new byte[bufsize];
      this.binary = binary;
      this.pendingSpace = false;
      this.pendingTab = false;
      this.pendingCR = false;
      this.outputIndex = 0;
      this.nextSoftBreak = 77;
   }

   public QuotedPrintableOutputStream(OutputStream out, boolean binary) {
      this(3072, out, binary);
   }

   private void encodeChunk(byte[] buffer, int off, int len) throws IOException {
      for(int inputIndex = off; inputIndex < len + off; ++inputIndex) {
         this.encode(buffer[inputIndex]);
      }

   }

   private void completeEncoding() throws IOException {
      this.writePending();
      this.flushOutput();
   }

   private void writePending() throws IOException {
      if (this.pendingSpace) {
         this.plain((byte)32);
      } else if (this.pendingTab) {
         this.plain((byte)9);
      } else if (this.pendingCR) {
         this.plain((byte)13);
      }

      this.clearPending();
   }

   private void clearPending() throws IOException {
      this.pendingSpace = false;
      this.pendingTab = false;
      this.pendingCR = false;
   }

   private void encode(byte next) throws IOException {
      if (next == 10) {
         if (this.binary) {
            this.writePending();
            this.escape(next);
         } else if (this.pendingCR) {
            if (this.pendingSpace) {
               this.escape((byte)32);
            } else if (this.pendingTab) {
               this.escape((byte)9);
            }

            this.lineBreak();
            this.clearPending();
         } else {
            this.writePending();
            this.plain(next);
         }
      } else if (next == 13) {
         if (this.binary) {
            this.escape(next);
         } else {
            this.pendingCR = true;
         }
      } else {
         this.writePending();
         if (next == 32) {
            if (this.binary) {
               this.escape(next);
            } else {
               this.pendingSpace = true;
            }
         } else if (next == 9) {
            if (this.binary) {
               this.escape(next);
            } else {
               this.pendingTab = true;
            }
         } else if (next < 32) {
            this.escape(next);
         } else if (next > 126) {
            this.escape(next);
         } else if (next != 61 && next != 46) {
            this.plain(next);
         } else {
            this.escape(next);
         }
      }

   }

   private void plain(byte next) throws IOException {
      if (--this.nextSoftBreak <= 1) {
         this.softBreak();
      }

      this.write(next);
   }

   private void escape(byte next) throws IOException {
      if (--this.nextSoftBreak <= 3) {
         this.softBreak();
      }

      int nextUnsigned = next & 255;
      this.write((byte)61);
      --this.nextSoftBreak;
      this.write(HEX_DIGITS[nextUnsigned >> 4]);
      --this.nextSoftBreak;
      this.write(HEX_DIGITS[nextUnsigned % 16]);
   }

   private void write(byte next) throws IOException {
      this.outBuffer[this.outputIndex++] = next;
      if (this.outputIndex >= this.outBuffer.length) {
         this.flushOutput();
      }

   }

   private void softBreak() throws IOException {
      this.write((byte)61);
      this.lineBreak();
   }

   private void lineBreak() throws IOException {
      this.write((byte)13);
      this.write((byte)10);
      this.nextSoftBreak = 76;
   }

   void flushOutput() throws IOException {
      if (this.outputIndex < this.outBuffer.length) {
         this.out.write(this.outBuffer, 0, this.outputIndex);
      } else {
         this.out.write(this.outBuffer);
      }

      this.outputIndex = 0;
   }

   public void close() throws IOException {
      if (!this.closed) {
         try {
            this.completeEncoding();
         } finally {
            this.closed = true;
         }

      }
   }

   public void flush() throws IOException {
      this.flushOutput();
   }

   public void write(int b) throws IOException {
      this.singleByte[0] = (byte)b;
      this.write(this.singleByte, 0, 1);
   }

   public void write(byte[] b, int off, int len) throws IOException {
      if (this.closed) {
         throw new IOException("Stream has been closed");
      } else {
         this.encodeChunk(b, off, len);
      }
   }
}
