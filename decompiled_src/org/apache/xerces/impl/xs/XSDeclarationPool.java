package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;

public final class XSDeclarationPool {
   private static final int CHUNK_SHIFT = 8;
   private static final int CHUNK_SIZE = 256;
   private static final int CHUNK_MASK = 255;
   private static final int INITIAL_CHUNK_COUNT = 4;
   private XSElementDecl[][] fElementDecl = new XSElementDecl[4][];
   private int fElementDeclIndex = 0;
   private XSParticleDecl[][] fParticleDecl = new XSParticleDecl[4][];
   private int fParticleDeclIndex = 0;
   private XSModelGroupImpl[][] fModelGroup = new XSModelGroupImpl[4][];
   private int fModelGroupIndex = 0;
   private XSAttributeDecl[][] fAttrDecl = new XSAttributeDecl[4][];
   private int fAttrDeclIndex = 0;
   private XSComplexTypeDecl[][] fCTDecl = new XSComplexTypeDecl[4][];
   private int fCTDeclIndex = 0;
   private XSSimpleTypeDecl[][] fSTDecl = new XSSimpleTypeDecl[4][];
   private int fSTDeclIndex = 0;
   private XSAttributeUseImpl[][] fAttributeUse = new XSAttributeUseImpl[4][];
   private int fAttributeUseIndex = 0;

   public final XSElementDecl getElementDecl() {
      int var1 = this.fElementDeclIndex >> 8;
      int var2 = this.fElementDeclIndex & 255;
      this.ensureElementDeclCapacity(var1);
      if (this.fElementDecl[var1][var2] == null) {
         this.fElementDecl[var1][var2] = new XSElementDecl();
      } else {
         this.fElementDecl[var1][var2].reset();
      }

      ++this.fElementDeclIndex;
      return this.fElementDecl[var1][var2];
   }

   public final XSAttributeDecl getAttributeDecl() {
      int var1 = this.fAttrDeclIndex >> 8;
      int var2 = this.fAttrDeclIndex & 255;
      this.ensureAttrDeclCapacity(var1);
      if (this.fAttrDecl[var1][var2] == null) {
         this.fAttrDecl[var1][var2] = new XSAttributeDecl();
      } else {
         this.fAttrDecl[var1][var2].reset();
      }

      ++this.fAttrDeclIndex;
      return this.fAttrDecl[var1][var2];
   }

   public final XSAttributeUseImpl getAttributeUse() {
      int var1 = this.fAttributeUseIndex >> 8;
      int var2 = this.fAttributeUseIndex & 255;
      this.ensureAttributeUseCapacity(var1);
      if (this.fAttributeUse[var1][var2] == null) {
         this.fAttributeUse[var1][var2] = new XSAttributeUseImpl();
      } else {
         this.fAttributeUse[var1][var2].reset();
      }

      ++this.fAttributeUseIndex;
      return this.fAttributeUse[var1][var2];
   }

   public final XSComplexTypeDecl getComplexTypeDecl() {
      int var1 = this.fCTDeclIndex >> 8;
      int var2 = this.fCTDeclIndex & 255;
      this.ensureCTDeclCapacity(var1);
      if (this.fCTDecl[var1][var2] == null) {
         this.fCTDecl[var1][var2] = new XSComplexTypeDecl();
      } else {
         this.fCTDecl[var1][var2].reset();
      }

      ++this.fCTDeclIndex;
      return this.fCTDecl[var1][var2];
   }

   public final XSSimpleTypeDecl getSimpleTypeDecl() {
      int var1 = this.fSTDeclIndex >> 8;
      int var2 = this.fSTDeclIndex & 255;
      this.ensureSTDeclCapacity(var1);
      if (this.fSTDecl[var1][var2] == null) {
         this.fSTDecl[var1][var2] = new XSSimpleTypeDecl();
      } else {
         this.fSTDecl[var1][var2].reset();
      }

      ++this.fSTDeclIndex;
      return this.fSTDecl[var1][var2];
   }

   public final XSParticleDecl getParticleDecl() {
      int var1 = this.fParticleDeclIndex >> 8;
      int var2 = this.fParticleDeclIndex & 255;
      this.ensureParticleDeclCapacity(var1);
      if (this.fParticleDecl[var1][var2] == null) {
         this.fParticleDecl[var1][var2] = new XSParticleDecl();
      } else {
         this.fParticleDecl[var1][var2].reset();
      }

      ++this.fParticleDeclIndex;
      return this.fParticleDecl[var1][var2];
   }

   public final XSModelGroupImpl getModelGroup() {
      int var1 = this.fModelGroupIndex >> 8;
      int var2 = this.fModelGroupIndex & 255;
      this.ensureModelGroupCapacity(var1);
      if (this.fModelGroup[var1][var2] == null) {
         this.fModelGroup[var1][var2] = new XSModelGroupImpl();
      } else {
         this.fModelGroup[var1][var2].reset();
      }

      ++this.fModelGroupIndex;
      return this.fModelGroup[var1][var2];
   }

   private boolean ensureElementDeclCapacity(int var1) {
      if (var1 >= this.fElementDecl.length) {
         this.fElementDecl = resize(this.fElementDecl, this.fElementDecl.length * 2);
      } else if (this.fElementDecl[var1] != null) {
         return false;
      }

      this.fElementDecl[var1] = new XSElementDecl[256];
      return true;
   }

   private static XSElementDecl[][] resize(XSElementDecl[][] var0, int var1) {
      XSElementDecl[][] var2 = new XSElementDecl[var1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   private boolean ensureParticleDeclCapacity(int var1) {
      if (var1 >= this.fParticleDecl.length) {
         this.fParticleDecl = resize(this.fParticleDecl, this.fParticleDecl.length * 2);
      } else if (this.fParticleDecl[var1] != null) {
         return false;
      }

      this.fParticleDecl[var1] = new XSParticleDecl[256];
      return true;
   }

   private boolean ensureModelGroupCapacity(int var1) {
      if (var1 >= this.fModelGroup.length) {
         this.fModelGroup = resize(this.fModelGroup, this.fModelGroup.length * 2);
      } else if (this.fModelGroup[var1] != null) {
         return false;
      }

      this.fModelGroup[var1] = new XSModelGroupImpl[256];
      return true;
   }

   private static XSParticleDecl[][] resize(XSParticleDecl[][] var0, int var1) {
      XSParticleDecl[][] var2 = new XSParticleDecl[var1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   private static XSModelGroupImpl[][] resize(XSModelGroupImpl[][] var0, int var1) {
      XSModelGroupImpl[][] var2 = new XSModelGroupImpl[var1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   private boolean ensureAttrDeclCapacity(int var1) {
      if (var1 >= this.fAttrDecl.length) {
         this.fAttrDecl = resize(this.fAttrDecl, this.fAttrDecl.length * 2);
      } else if (this.fAttrDecl[var1] != null) {
         return false;
      }

      this.fAttrDecl[var1] = new XSAttributeDecl[256];
      return true;
   }

   private static XSAttributeDecl[][] resize(XSAttributeDecl[][] var0, int var1) {
      XSAttributeDecl[][] var2 = new XSAttributeDecl[var1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   private boolean ensureAttributeUseCapacity(int var1) {
      if (var1 >= this.fAttributeUse.length) {
         this.fAttributeUse = resize(this.fAttributeUse, this.fAttributeUse.length * 2);
      } else if (this.fAttributeUse[var1] != null) {
         return false;
      }

      this.fAttributeUse[var1] = new XSAttributeUseImpl[256];
      return true;
   }

   private static XSAttributeUseImpl[][] resize(XSAttributeUseImpl[][] var0, int var1) {
      XSAttributeUseImpl[][] var2 = new XSAttributeUseImpl[var1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   private boolean ensureSTDeclCapacity(int var1) {
      if (var1 >= this.fSTDecl.length) {
         this.fSTDecl = resize(this.fSTDecl, this.fSTDecl.length * 2);
      } else if (this.fSTDecl[var1] != null) {
         return false;
      }

      this.fSTDecl[var1] = new XSSimpleTypeDecl[256];
      return true;
   }

   private static XSSimpleTypeDecl[][] resize(XSSimpleTypeDecl[][] var0, int var1) {
      XSSimpleTypeDecl[][] var2 = new XSSimpleTypeDecl[var1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   private boolean ensureCTDeclCapacity(int var1) {
      if (var1 >= this.fCTDecl.length) {
         this.fCTDecl = resize(this.fCTDecl, this.fCTDecl.length * 2);
      } else if (this.fCTDecl[var1] != null) {
         return false;
      }

      this.fCTDecl[var1] = new XSComplexTypeDecl[256];
      return true;
   }

   private static XSComplexTypeDecl[][] resize(XSComplexTypeDecl[][] var0, int var1) {
      XSComplexTypeDecl[][] var2 = new XSComplexTypeDecl[var1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   public void reset() {
      this.fElementDeclIndex = 0;
      this.fParticleDeclIndex = 0;
      this.fModelGroupIndex = 0;
      this.fSTDeclIndex = 0;
      this.fCTDeclIndex = 0;
      this.fAttrDeclIndex = 0;
      this.fAttributeUseIndex = 0;
   }
}
