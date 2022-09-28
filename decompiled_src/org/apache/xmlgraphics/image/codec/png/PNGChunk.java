package org.apache.xmlgraphics.image.codec.png;

class PNGChunk {
   int length;
   int type;
   byte[] data;
   int crc;
   String typeString;

   public PNGChunk(int length, int type, byte[] data, int crc) {
      this.length = length;
      this.type = type;
      this.data = data;
      this.crc = crc;
      this.typeString = "";
      this.typeString = this.typeString + (char)(type >> 24);
      this.typeString = this.typeString + (char)(type >> 16 & 255);
      this.typeString = this.typeString + (char)(type >> 8 & 255);
      this.typeString = this.typeString + (char)(type & 255);
   }

   public int getLength() {
      return this.length;
   }

   public int getType() {
      return this.type;
   }

   public String getTypeString() {
      return this.typeString;
   }

   public byte[] getData() {
      return this.data;
   }

   public byte getByte(int offset) {
      return this.data[offset];
   }

   public int getInt1(int offset) {
      return this.data[offset] & 255;
   }

   public int getInt2(int offset) {
      return (this.data[offset] & 255) << 8 | this.data[offset + 1] & 255;
   }

   public int getInt4(int offset) {
      return (this.data[offset] & 255) << 24 | (this.data[offset + 1] & 255) << 16 | (this.data[offset + 2] & 255) << 8 | this.data[offset + 3] & 255;
   }

   public String getString4(int offset) {
      return "" + (char)this.data[offset] + (char)this.data[offset + 1] + (char)this.data[offset + 2] + (char)this.data[offset + 3];
   }

   public boolean isType(String typeName) {
      return this.typeString.equals(typeName);
   }
}
