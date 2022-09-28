package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

final class WriterToUTF8Buffered extends Writer implements WriterChain {
   private static final int BYTES_MAX = 16384;
   private static final int CHARS_MAX = 5461;
   private final OutputStream m_os;
   private final byte[] m_outputBytes;
   private final char[] m_inputChars;
   private int count;

   public WriterToUTF8Buffered(OutputStream out) throws UnsupportedEncodingException {
      this.m_os = out;
      this.m_outputBytes = new byte[16387];
      this.m_inputChars = new char[5463];
      this.count = 0;
   }

   public void write(int c) throws IOException {
      if (this.count >= 16384) {
         this.flushBuffer();
      }

      if (c < 128) {
         this.m_outputBytes[this.count++] = (byte)c;
      } else if (c < 2048) {
         this.m_outputBytes[this.count++] = (byte)(192 + (c >> 6));
         this.m_outputBytes[this.count++] = (byte)(128 + (c & 63));
      } else if (c < 65536) {
         this.m_outputBytes[this.count++] = (byte)(224 + (c >> 12));
         this.m_outputBytes[this.count++] = (byte)(128 + (c >> 6 & 63));
         this.m_outputBytes[this.count++] = (byte)(128 + (c & 63));
      } else {
         this.m_outputBytes[this.count++] = (byte)(240 + (c >> 18));
         this.m_outputBytes[this.count++] = (byte)(128 + (c >> 12 & 63));
         this.m_outputBytes[this.count++] = (byte)(128 + (c >> 6 & 63));
         this.m_outputBytes[this.count++] = (byte)(128 + (c & 63));
      }

   }

   public void write(char[] chars, int start, int length) throws IOException {
      int lengthx3 = 3 * length;
      int n;
      int end_chunk;
      int i;
      int c;
      if (lengthx3 >= 16384 - this.count) {
         this.flushBuffer();
         if (lengthx3 > 16384) {
            n = length / 5461;
            int chunks;
            if (n > 1) {
               chunks = n;
            } else {
               chunks = 2;
            }

            end_chunk = start;

            for(i = 1; i <= chunks; ++i) {
               c = end_chunk;
               end_chunk = start + (int)((long)length * (long)i / (long)chunks);
               char c = chars[end_chunk - 1];
               char var10000 = chars[end_chunk - 1];
               if (c >= '\ud800' && c <= '\udbff') {
                  if (end_chunk < start + length) {
                     ++end_chunk;
                  } else {
                     --end_chunk;
                  }
               }

               int len_chunk = end_chunk - c;
               this.write(chars, c, len_chunk);
            }

            return;
         }
      }

      n = length + start;
      byte[] buf_loc = this.m_outputBytes;
      end_chunk = this.count;

      for(i = start; i < n && (c = chars[i]) < 128; ++i) {
         buf_loc[end_chunk++] = (byte)c;
      }

      for(; i < n; ++i) {
         c = chars[i];
         if (c < 128) {
            buf_loc[end_chunk++] = (byte)c;
         } else if (c < 2048) {
            buf_loc[end_chunk++] = (byte)(192 + (c >> 6));
            buf_loc[end_chunk++] = (byte)(128 + (c & 63));
         } else if (c >= 55296 && c <= 56319) {
            ++i;
            char low = chars[i];
            buf_loc[end_chunk++] = (byte)(240 | c + 64 >> 8 & 240);
            buf_loc[end_chunk++] = (byte)(128 | c + 64 >> 2 & 63);
            buf_loc[end_chunk++] = (byte)(128 | (low >> 6 & 15) + (c << 4 & 48));
            buf_loc[end_chunk++] = (byte)(128 | low & 63);
         } else {
            buf_loc[end_chunk++] = (byte)(224 + (c >> 12));
            buf_loc[end_chunk++] = (byte)(128 + (c >> 6 & 63));
            buf_loc[end_chunk++] = (byte)(128 + (c & 63));
         }
      }

      this.count = end_chunk;
   }

   public void write(String s) throws IOException {
      int length = s.length();
      int lengthx3 = 3 * length;
      int n;
      int end_chunk;
      int i;
      int c;
      char low;
      if (lengthx3 >= 16384 - this.count) {
         this.flushBuffer();
         if (lengthx3 > 16384) {
            int start = false;
            n = length / 5461;
            int chunks;
            if (n > 1) {
               chunks = n;
            } else {
               chunks = 2;
            }

            end_chunk = 0;

            for(i = 1; i <= chunks; ++i) {
               c = end_chunk;
               end_chunk = 0 + (int)((long)length * (long)i / (long)chunks);
               s.getChars(c, end_chunk, this.m_inputChars, 0);
               int len_chunk = end_chunk - c;
               low = this.m_inputChars[len_chunk - 1];
               if (low >= '\ud800' && low <= '\udbff') {
                  --end_chunk;
                  --len_chunk;
                  if (i == chunks) {
                  }
               }

               this.write(this.m_inputChars, 0, len_chunk);
            }

            return;
         }
      }

      s.getChars(0, length, this.m_inputChars, 0);
      char[] chars = this.m_inputChars;
      n = length;
      byte[] buf_loc = this.m_outputBytes;
      end_chunk = this.count;

      for(i = 0; i < n && (c = chars[i]) < 128; ++i) {
         buf_loc[end_chunk++] = (byte)c;
      }

      for(; i < n; ++i) {
         c = chars[i];
         if (c < 128) {
            buf_loc[end_chunk++] = (byte)c;
         } else if (c < 2048) {
            buf_loc[end_chunk++] = (byte)(192 + (c >> 6));
            buf_loc[end_chunk++] = (byte)(128 + (c & 63));
         } else if (c >= 55296 && c <= 56319) {
            ++i;
            low = chars[i];
            buf_loc[end_chunk++] = (byte)(240 | c + 64 >> 8 & 240);
            buf_loc[end_chunk++] = (byte)(128 | c + 64 >> 2 & 63);
            buf_loc[end_chunk++] = (byte)(128 | (low >> 6 & 15) + (c << 4 & 48));
            buf_loc[end_chunk++] = (byte)(128 | low & 63);
         } else {
            buf_loc[end_chunk++] = (byte)(224 + (c >> 12));
            buf_loc[end_chunk++] = (byte)(128 + (c >> 6 & 63));
            buf_loc[end_chunk++] = (byte)(128 + (c & 63));
         }
      }

      this.count = end_chunk;
   }

   public void flushBuffer() throws IOException {
      if (this.count > 0) {
         this.m_os.write(this.m_outputBytes, 0, this.count);
         this.count = 0;
      }

   }

   public void flush() throws IOException {
      this.flushBuffer();
      this.m_os.flush();
   }

   public void close() throws IOException {
      this.flushBuffer();
      this.m_os.close();
   }

   public OutputStream getOutputStream() {
      return this.m_os;
   }

   public Writer getWriter() {
      return null;
   }
}
