package org.apache.james.mime4j.util;

public final class ByteArrayBuffer implements ByteSequence {
   private byte[] buffer;
   private int len;

   public ByteArrayBuffer(int capacity) {
      if (capacity < 0) {
         throw new IllegalArgumentException("Buffer capacity may not be negative");
      } else {
         this.buffer = new byte[capacity];
      }
   }

   public ByteArrayBuffer(byte[] bytes, boolean dontCopy) {
      this(bytes, bytes.length, dontCopy);
   }

   public ByteArrayBuffer(byte[] bytes, int len, boolean dontCopy) {
      if (bytes == null) {
         throw new IllegalArgumentException();
      } else if (len >= 0 && len <= bytes.length) {
         if (dontCopy) {
            this.buffer = bytes;
         } else {
            this.buffer = new byte[len];
            System.arraycopy(bytes, 0, this.buffer, 0, len);
         }

         this.len = len;
      } else {
         throw new IllegalArgumentException();
      }
   }

   private void expand(int newlen) {
      byte[] newbuffer = new byte[Math.max(this.buffer.length << 1, newlen)];
      System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
      this.buffer = newbuffer;
   }

   public void append(byte[] b, int off, int len) {
      if (b != null) {
         if (off >= 0 && off <= b.length && len >= 0 && off + len >= 0 && off + len <= b.length) {
            if (len != 0) {
               int newlen = this.len + len;
               if (newlen > this.buffer.length) {
                  this.expand(newlen);
               }

               System.arraycopy(b, off, this.buffer, this.len, len);
               this.len = newlen;
            }
         } else {
            throw new IndexOutOfBoundsException();
         }
      }
   }

   public void append(int b) {
      int newlen = this.len + 1;
      if (newlen > this.buffer.length) {
         this.expand(newlen);
      }

      this.buffer[this.len] = (byte)b;
      this.len = newlen;
   }

   public void clear() {
      this.len = 0;
   }

   public byte[] toByteArray() {
      byte[] b = new byte[this.len];
      if (this.len > 0) {
         System.arraycopy(this.buffer, 0, b, 0, this.len);
      }

      return b;
   }

   public byte byteAt(int i) {
      if (i >= 0 && i < this.len) {
         return this.buffer[i];
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public int capacity() {
      return this.buffer.length;
   }

   public int length() {
      return this.len;
   }

   public byte[] buffer() {
      return this.buffer;
   }

   public int indexOf(byte b) {
      return this.indexOf(b, 0, this.len);
   }

   public int indexOf(byte b, int beginIndex, int endIndex) {
      if (beginIndex < 0) {
         beginIndex = 0;
      }

      if (endIndex > this.len) {
         endIndex = this.len;
      }

      if (beginIndex > endIndex) {
         return -1;
      } else {
         for(int i = beginIndex; i < endIndex; ++i) {
            if (this.buffer[i] == b) {
               return i;
            }
         }

         return -1;
      }
   }

   public void setLength(int len) {
      if (len >= 0 && len <= this.buffer.length) {
         this.len = len;
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public void remove(int off, int len) {
      if (off >= 0 && off <= this.len && len >= 0 && off + len >= 0 && off + len <= this.len) {
         if (len != 0) {
            int remaining = this.len - off - len;
            if (remaining > 0) {
               System.arraycopy(this.buffer, off + len, this.buffer, off, remaining);
            }

            this.len -= len;
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public boolean isEmpty() {
      return this.len == 0;
   }

   public boolean isFull() {
      return this.len == this.buffer.length;
   }

   public String toString() {
      return new String(this.toByteArray());
   }
}
