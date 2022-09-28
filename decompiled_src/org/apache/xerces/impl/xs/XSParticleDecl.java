package org.apache.xerces.impl.xs;

import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;

public class XSParticleDecl implements XSParticle {
   public static final short PARTICLE_EMPTY = 0;
   public static final short PARTICLE_ELEMENT = 1;
   public static final short PARTICLE_WILDCARD = 2;
   public static final short PARTICLE_MODELGROUP = 3;
   public static final short PARTICLE_ZERO_OR_MORE = 4;
   public static final short PARTICLE_ZERO_OR_ONE = 5;
   public static final short PARTICLE_ONE_OR_MORE = 6;
   public short fType = 0;
   public XSTerm fValue = null;
   public int fMinOccurs = 1;
   public int fMaxOccurs = 1;
   private String fDescription = null;

   public XSParticleDecl makeClone() {
      XSParticleDecl var1 = new XSParticleDecl();
      var1.fType = this.fType;
      var1.fMinOccurs = this.fMinOccurs;
      var1.fMaxOccurs = this.fMaxOccurs;
      var1.fDescription = this.fDescription;
      var1.fValue = this.fValue;
      return var1;
   }

   public boolean emptiable() {
      return this.minEffectiveTotalRange() == 0;
   }

   public boolean isEmpty() {
      if (this.fType == 0) {
         return true;
      } else {
         return this.fType != 1 && this.fType != 2 ? ((XSModelGroupImpl)this.fValue).isEmpty() : false;
      }
   }

   public int minEffectiveTotalRange() {
      if (this.fType == 0) {
         return 0;
      } else {
         return this.fType == 3 ? ((XSModelGroupImpl)this.fValue).minEffectiveTotalRange() * this.fMinOccurs : this.fMinOccurs;
      }
   }

   public int maxEffectiveTotalRange() {
      if (this.fType == 0) {
         return 0;
      } else if (this.fType == 3) {
         int var1 = ((XSModelGroupImpl)this.fValue).maxEffectiveTotalRange();
         if (var1 == -1) {
            return -1;
         } else {
            return var1 != 0 && this.fMaxOccurs == -1 ? -1 : var1 * this.fMaxOccurs;
         }
      } else {
         return this.fMaxOccurs;
      }
   }

   public String toString() {
      if (this.fDescription == null) {
         StringBuffer var1 = new StringBuffer();
         this.appendParticle(var1);
         if ((this.fMinOccurs != 0 || this.fMaxOccurs != 0) && (this.fMinOccurs != 1 || this.fMaxOccurs != 1)) {
            var1.append("{" + this.fMinOccurs);
            if (this.fMaxOccurs == -1) {
               var1.append("-UNBOUNDED");
            } else if (this.fMinOccurs != this.fMaxOccurs) {
               var1.append("-" + this.fMaxOccurs);
            }

            var1.append("}");
         }

         this.fDescription = var1.toString();
      }

      return this.fDescription;
   }

   void appendParticle(StringBuffer var1) {
      switch (this.fType) {
         case 0:
            var1.append("EMPTY");
            break;
         case 1:
            var1.append(this.fValue.toString());
            break;
         case 2:
            var1.append('(');
            var1.append(this.fValue.toString());
            var1.append(')');
            break;
         case 3:
            var1.append(this.fValue.toString());
      }

   }

   public void reset() {
      this.fType = 0;
      this.fValue = null;
      this.fMinOccurs = 1;
      this.fMaxOccurs = 1;
      this.fDescription = null;
   }

   public short getType() {
      return 8;
   }

   public String getName() {
      return null;
   }

   public String getNamespace() {
      return null;
   }

   public int getMinOccurs() {
      return this.fMinOccurs;
   }

   public boolean getMaxOccursUnbounded() {
      return this.fMaxOccurs == -1;
   }

   public int getMaxOccurs() {
      return this.fMaxOccurs;
   }

   public XSTerm getTerm() {
      return this.fValue;
   }

   public XSNamespaceItem getNamespaceItem() {
      return null;
   }
}
