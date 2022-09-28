package org.apache.xerces.impl.dtd.models;

public class CMAny extends CMNode {
   private int fType;
   private String fURI;
   private int fPosition = -1;

   public CMAny(int var1, String var2, int var3) {
      super(var1);
      this.fType = var1;
      this.fURI = var2;
      this.fPosition = var3;
   }

   final int getType() {
      return this.fType;
   }

   final String getURI() {
      return this.fURI;
   }

   final int getPosition() {
      return this.fPosition;
   }

   final void setPosition(int var1) {
      this.fPosition = var1;
   }

   public boolean isNullable() {
      return this.fPosition == -1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("(");
      var1.append("##any:uri=");
      var1.append(this.fURI);
      var1.append(')');
      if (this.fPosition >= 0) {
         var1.append(" (Pos:" + Integer.toString(this.fPosition) + ")");
      }

      return var1.toString();
   }

   protected void calcFirstPos(CMStateSet var1) {
      if (this.fPosition == -1) {
         var1.zeroBits();
      } else {
         var1.setBit(this.fPosition);
      }

   }

   protected void calcLastPos(CMStateSet var1) {
      if (this.fPosition == -1) {
         var1.zeroBits();
      } else {
         var1.setBit(this.fPosition);
      }

   }
}
