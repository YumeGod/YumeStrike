package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class NameRecord {
   private short platformId;
   private short encodingId;
   private short languageId;
   private short nameId;
   private short stringLength;
   private short stringOffset;
   private String record;

   protected NameRecord(RandomAccessFile var1) throws IOException {
      this.platformId = var1.readShort();
      this.encodingId = var1.readShort();
      this.languageId = var1.readShort();
      this.nameId = var1.readShort();
      this.stringLength = var1.readShort();
      this.stringOffset = var1.readShort();
   }

   public short getEncodingId() {
      return this.encodingId;
   }

   public short getLanguageId() {
      return this.languageId;
   }

   public short getNameId() {
      return this.nameId;
   }

   public short getPlatformId() {
      return this.platformId;
   }

   public String getRecordString() {
      return this.record;
   }

   protected void loadString(RandomAccessFile var1, int var2) throws IOException {
      StringBuffer var3 = new StringBuffer();
      var1.seek((long)(var2 + this.stringOffset));
      int var4;
      if (this.platformId == 0) {
         for(var4 = 0; var4 < this.stringLength / 2; ++var4) {
            var3.append(var1.readChar());
         }
      } else if (this.platformId == 1) {
         for(var4 = 0; var4 < this.stringLength; ++var4) {
            var3.append((char)var1.readByte());
         }
      } else if (this.platformId == 2) {
         for(var4 = 0; var4 < this.stringLength; ++var4) {
            var3.append((char)var1.readByte());
         }
      } else if (this.platformId == 3) {
         for(int var5 = 0; var5 < this.stringLength / 2; ++var5) {
            char var6 = var1.readChar();
            var3.append(var6);
         }
      }

      this.record = var3.toString();
   }
}
