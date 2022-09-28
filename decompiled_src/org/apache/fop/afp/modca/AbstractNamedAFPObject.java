package org.apache.fop.afp.modca;

import java.io.UnsupportedEncodingException;

public abstract class AbstractNamedAFPObject extends AbstractTripletStructuredObject {
   private static final int DEFAULT_NAME_LENGTH = 8;
   protected String name = null;

   protected AbstractNamedAFPObject() {
   }

   protected AbstractNamedAFPObject(String name) {
      this.name = name;
   }

   protected int getNameLength() {
      return 8;
   }

   public byte[] getNameBytes() {
      int afpNameLen = this.getNameLength();
      int nameLen = this.name.length();
      String truncatedName;
      if (nameLen < afpNameLen) {
         this.name = (this.name + "       ").substring(0, afpNameLen);
      } else if (this.name.length() > afpNameLen) {
         truncatedName = this.name.substring(nameLen - afpNameLen, nameLen);
         log.warn("Constructor:: name '" + this.name + "'" + " truncated to " + afpNameLen + " chars" + " ('" + truncatedName + "')");
         this.name = truncatedName;
      }

      truncatedName = null;

      byte[] nameBytes;
      try {
         nameBytes = this.name.getBytes("Cp1146");
      } catch (UnsupportedEncodingException var5) {
         nameBytes = this.name.getBytes();
         log.warn("Constructor:: UnsupportedEncodingException translating the name " + this.name);
      }

      return nameBytes;
   }

   protected void copySF(byte[] data, byte type, byte category) {
      super.copySF(data, type, category);
      byte[] nameData = this.getNameBytes();
      System.arraycopy(nameData, 0, data, 9, nameData.length);
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String toString() {
      return this.getName();
   }
}
