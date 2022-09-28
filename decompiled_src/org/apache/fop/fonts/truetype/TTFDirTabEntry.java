package org.apache.fop.fonts.truetype;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

class TTFDirTabEntry {
   private byte[] tag = new byte[4];
   private int checksum;
   private long offset;
   private long length;

   public String read(FontFileReader in) throws IOException {
      this.tag[0] = in.readTTFByte();
      this.tag[1] = in.readTTFByte();
      this.tag[2] = in.readTTFByte();
      this.tag[3] = in.readTTFByte();
      in.skip(4L);
      this.offset = in.readTTFULong();
      this.length = in.readTTFULong();
      String tagStr = new String(this.tag, "ISO-8859-1");
      return tagStr;
   }

   public String toString() {
      return "Read dir tab [" + this.tag[0] + " " + this.tag[1] + " " + this.tag[2] + " " + this.tag[3] + "]" + " offset: " + this.offset + " length: " + this.length + " name: " + this.tag;
   }

   public int getChecksum() {
      return this.checksum;
   }

   public long getLength() {
      return this.length;
   }

   public long getOffset() {
      return this.offset;
   }

   public byte[] getTag() {
      return this.tag;
   }

   public String getTagString() {
      try {
         return new String(this.tag, "ISO-8859-1");
      } catch (UnsupportedEncodingException var2) {
         return this.toString();
      }
   }
}
