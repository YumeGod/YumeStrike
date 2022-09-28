package org.apache.batik.svggen.font.table;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GlyfCompositeDescript extends GlyfDescript {
   private List components = new ArrayList();
   protected boolean beingResolved = false;
   protected boolean resolved = false;

   public GlyfCompositeDescript(GlyfTable var1, ByteArrayInputStream var2) {
      super(var1, (short)-1, var2);

      GlyfCompositeComp var3;
      do {
         var3 = new GlyfCompositeComp(var2);
         this.components.add(var3);
      } while((var3.getFlags() & 32) != 0);

      if ((var3.getFlags() & 256) != 0) {
         this.readInstructions(var2, var2.read() << 8 | var2.read());
      }

   }

   public void resolve() {
      if (!this.resolved) {
         if (this.beingResolved) {
            System.err.println("Circular reference in GlyfCompositeDesc");
         } else {
            this.beingResolved = true;
            int var1 = 0;
            int var2 = 0;
            Iterator var3 = this.components.iterator();

            while(var3.hasNext()) {
               GlyfCompositeComp var4 = (GlyfCompositeComp)var3.next();
               var4.setFirstIndex(var1);
               var4.setFirstContour(var2);
               GlyfDescript var5 = this.parentTable.getDescription(var4.getGlyphIndex());
               if (var5 != null) {
                  var5.resolve();
                  var1 += var5.getPointCount();
                  var2 += var5.getContourCount();
               }
            }

            this.resolved = true;
            this.beingResolved = false;
         }
      }
   }

   public int getEndPtOfContours(int var1) {
      GlyfCompositeComp var2 = this.getCompositeCompEndPt(var1);
      if (var2 != null) {
         GlyfDescript var3 = this.parentTable.getDescription(var2.getGlyphIndex());
         return var3.getEndPtOfContours(var1 - var2.getFirstContour()) + var2.getFirstIndex();
      } else {
         return 0;
      }
   }

   public byte getFlags(int var1) {
      GlyfCompositeComp var2 = this.getCompositeComp(var1);
      if (var2 != null) {
         GlyfDescript var3 = this.parentTable.getDescription(var2.getGlyphIndex());
         return var3.getFlags(var1 - var2.getFirstIndex());
      } else {
         return 0;
      }
   }

   public short getXCoordinate(int var1) {
      GlyfCompositeComp var2 = this.getCompositeComp(var1);
      if (var2 != null) {
         GlyfDescript var3 = this.parentTable.getDescription(var2.getGlyphIndex());
         int var4 = var1 - var2.getFirstIndex();
         short var5 = var3.getXCoordinate(var4);
         short var6 = var3.getYCoordinate(var4);
         short var7 = (short)var2.scaleX(var5, var6);
         var7 = (short)(var7 + var2.getXTranslate());
         return var7;
      } else {
         return 0;
      }
   }

   public short getYCoordinate(int var1) {
      GlyfCompositeComp var2 = this.getCompositeComp(var1);
      if (var2 != null) {
         GlyfDescript var3 = this.parentTable.getDescription(var2.getGlyphIndex());
         int var4 = var1 - var2.getFirstIndex();
         short var5 = var3.getXCoordinate(var4);
         short var6 = var3.getYCoordinate(var4);
         short var7 = (short)var2.scaleY(var5, var6);
         var7 = (short)(var7 + var2.getYTranslate());
         return var7;
      } else {
         return 0;
      }
   }

   public boolean isComposite() {
      return true;
   }

   public int getPointCount() {
      if (!this.resolved) {
         System.err.println("getPointCount called on unresolved GlyfCompositeDescript");
      }

      GlyfCompositeComp var1 = (GlyfCompositeComp)this.components.get(this.components.size() - 1);
      return var1.getFirstIndex() + this.parentTable.getDescription(var1.getGlyphIndex()).getPointCount();
   }

   public int getContourCount() {
      if (!this.resolved) {
         System.err.println("getContourCount called on unresolved GlyfCompositeDescript");
      }

      GlyfCompositeComp var1 = (GlyfCompositeComp)this.components.get(this.components.size() - 1);
      return var1.getFirstContour() + this.parentTable.getDescription(var1.getGlyphIndex()).getContourCount();
   }

   public int getComponentIndex(int var1) {
      return ((GlyfCompositeComp)this.components.get(var1)).getFirstIndex();
   }

   public int getComponentCount() {
      return this.components.size();
   }

   protected GlyfCompositeComp getCompositeComp(int var1) {
      for(int var3 = 0; var3 < this.components.size(); ++var3) {
         GlyfCompositeComp var2 = (GlyfCompositeComp)this.components.get(var3);
         GlyfDescript var4 = this.parentTable.getDescription(var2.getGlyphIndex());
         if (var2.getFirstIndex() <= var1 && var1 < var2.getFirstIndex() + var4.getPointCount()) {
            return var2;
         }
      }

      return null;
   }

   protected GlyfCompositeComp getCompositeCompEndPt(int var1) {
      for(int var3 = 0; var3 < this.components.size(); ++var3) {
         GlyfCompositeComp var2 = (GlyfCompositeComp)this.components.get(var3);
         GlyfDescript var4 = this.parentTable.getDescription(var2.getGlyphIndex());
         if (var2.getFirstContour() <= var1 && var1 < var2.getFirstContour() + var4.getContourCount()) {
            return var2;
         }
      }

      return null;
   }
}
