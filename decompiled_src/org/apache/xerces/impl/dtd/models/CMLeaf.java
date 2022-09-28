package org.apache.xerces.impl.dtd.models;

import org.apache.xerces.xni.QName;

public class CMLeaf extends CMNode {
   private QName fElement = new QName();
   private int fPosition = -1;

   public CMLeaf(QName var1, int var2) {
      super(0);
      this.fElement.setValues(var1);
      this.fPosition = var2;
   }

   public CMLeaf(QName var1) {
      super(0);
      this.fElement.setValues(var1);
   }

   final QName getElement() {
      return this.fElement;
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
      StringBuffer var1 = new StringBuffer(this.fElement.toString());
      var1.append(" (");
      var1.append(this.fElement.uri);
      var1.append(',');
      var1.append(this.fElement.localpart);
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
