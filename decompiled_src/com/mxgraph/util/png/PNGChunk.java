package com.mxgraph.util.png;

class PNGChunk {
   int length;
   int type;
   byte[] data;
   int crc;
   final String typeString;

   PNGChunk(int var1, int var2, byte[] var3, int var4) {
      this.length = var1;
      this.type = var2;
      this.data = var3;
      this.crc = var4;
      this.typeString = "" + (char)(var2 >>> 24 & 255) + (char)(var2 >>> 16 & 255) + (char)(var2 >>> 8 & 255) + (char)(var2 & 255);
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

   public byte getByte(int var1) {
      return this.data[var1];
   }

   public int getInt1(int var1) {
      return this.data[var1] & 255;
   }

   public int getInt2(int var1) {
      return (this.data[var1] & 255) << 8 | this.data[var1 + 1] & 255;
   }

   public int getInt4(int var1) {
      return (this.data[var1] & 255) << 24 | (this.data[var1 + 1] & 255) << 16 | (this.data[var1 + 2] & 255) << 8 | this.data[var1 + 3] & 255;
   }

   public String getString4(int var1) {
      return "" + (char)this.data[var1] + (char)this.data[var1 + 1] + (char)this.data[var1 + 2] + (char)this.data[var1 + 3];
   }

   public boolean isType(String var1) {
      return this.typeString.equals(var1);
   }
}
