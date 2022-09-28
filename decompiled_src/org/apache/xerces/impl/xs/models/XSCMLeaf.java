package org.apache.xerces.impl.xs.models;

import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.impl.dtd.models.CMStateSet;

public class XSCMLeaf extends CMNode {
   private Object fLeaf = null;
   private int fParticleId = -1;
   private int fPosition = -1;

   public XSCMLeaf(int var1, Object var2, int var3, int var4) {
      super(var1);
      this.fLeaf = var2;
      this.fParticleId = var3;
      this.fPosition = var4;
   }

   final Object getLeaf() {
      return this.fLeaf;
   }

   final int getParticleId() {
      return this.fParticleId;
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
      StringBuffer var1 = new StringBuffer(this.fLeaf.toString());
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
