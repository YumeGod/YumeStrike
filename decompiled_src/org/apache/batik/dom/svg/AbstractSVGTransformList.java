package org.apache.batik.dom.svg;

import java.awt.geom.AffineTransform;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.TransformListHandler;
import org.apache.batik.parser.TransformListParser;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGMatrix;
import org.w3c.dom.svg.SVGTransform;
import org.w3c.dom.svg.SVGTransformList;

public abstract class AbstractSVGTransformList extends AbstractSVGList implements SVGTransformList {
   public static final String SVG_TRANSFORMATION_LIST_SEPARATOR = "";

   protected String getItemSeparator() {
      return "";
   }

   protected abstract SVGException createSVGException(short var1, String var2, Object[] var3);

   public SVGTransform initialize(SVGTransform var1) throws DOMException, SVGException {
      return (SVGTransform)this.initializeImpl(var1);
   }

   public SVGTransform getItem(int var1) throws DOMException {
      return (SVGTransform)this.getItemImpl(var1);
   }

   public SVGTransform insertItemBefore(SVGTransform var1, int var2) throws DOMException, SVGException {
      return (SVGTransform)this.insertItemBeforeImpl(var1, var2);
   }

   public SVGTransform replaceItem(SVGTransform var1, int var2) throws DOMException, SVGException {
      return (SVGTransform)this.replaceItemImpl(var1, var2);
   }

   public SVGTransform removeItem(int var1) throws DOMException {
      return (SVGTransform)this.removeItemImpl(var1);
   }

   public SVGTransform appendItem(SVGTransform var1) throws DOMException, SVGException {
      return (SVGTransform)this.appendItemImpl(var1);
   }

   public SVGTransform createSVGTransformFromMatrix(SVGMatrix var1) {
      SVGOMTransform var2 = new SVGOMTransform();
      var2.setMatrix(var1);
      return var2;
   }

   public SVGTransform consolidate() {
      this.revalidate();
      int var1 = this.itemList.size();
      if (var1 == 0) {
         return null;
      } else if (var1 == 1) {
         return this.getItem(0);
      } else {
         SVGTransformItem var2 = (SVGTransformItem)this.getItemImpl(0);
         AffineTransform var3 = (AffineTransform)var2.affineTransform.clone();

         for(int var4 = 1; var4 < var1; ++var4) {
            var2 = (SVGTransformItem)this.getItemImpl(var4);
            var3.concatenate(var2.affineTransform);
         }

         SVGOMMatrix var5 = new SVGOMMatrix(var3);
         return this.initialize(this.createSVGTransformFromMatrix(var5));
      }
   }

   public AffineTransform getAffineTransform() {
      AffineTransform var1 = new AffineTransform();

      for(int var2 = 0; var2 < this.getNumberOfItems(); ++var2) {
         SVGTransformItem var3 = (SVGTransformItem)this.getItem(var2);
         var1.concatenate(var3.affineTransform);
      }

      return var1;
   }

   protected SVGItem createSVGItem(Object var1) {
      return new SVGTransformItem((SVGTransform)var1);
   }

   protected void doParse(String var1, ListHandler var2) throws ParseException {
      TransformListParser var3 = new TransformListParser();
      TransformListBuilder var4 = new TransformListBuilder(var2);
      var3.setTransformListHandler(var4);
      var3.parse(var1);
   }

   protected void checkItemType(Object var1) {
      if (!(var1 instanceof SVGTransform)) {
         this.createSVGException((short)0, "expected.transform", (Object[])null);
      }

   }

   protected class TransformListBuilder implements TransformListHandler {
      protected ListHandler listHandler;

      public TransformListBuilder(ListHandler var2) {
         this.listHandler = var2;
      }

      public void startTransformList() throws ParseException {
         this.listHandler.startList();
      }

      public void matrix(float var1, float var2, float var3, float var4, float var5, float var6) throws ParseException {
         SVGTransformItem var7 = AbstractSVGTransformList.this.new SVGTransformItem();
         var7.matrix(var1, var2, var3, var4, var5, var6);
         this.listHandler.item(var7);
      }

      public void rotate(float var1) throws ParseException {
         SVGTransformItem var2 = AbstractSVGTransformList.this.new SVGTransformItem();
         var2.rotate(var1);
         this.listHandler.item(var2);
      }

      public void rotate(float var1, float var2, float var3) throws ParseException {
         SVGTransformItem var4 = AbstractSVGTransformList.this.new SVGTransformItem();
         var4.setRotate(var1, var2, var3);
         this.listHandler.item(var4);
      }

      public void translate(float var1) throws ParseException {
         SVGTransformItem var2 = AbstractSVGTransformList.this.new SVGTransformItem();
         var2.translate(var1);
         this.listHandler.item(var2);
      }

      public void translate(float var1, float var2) throws ParseException {
         SVGTransformItem var3 = AbstractSVGTransformList.this.new SVGTransformItem();
         var3.setTranslate(var1, var2);
         this.listHandler.item(var3);
      }

      public void scale(float var1) throws ParseException {
         SVGTransformItem var2 = AbstractSVGTransformList.this.new SVGTransformItem();
         var2.scale(var1);
         this.listHandler.item(var2);
      }

      public void scale(float var1, float var2) throws ParseException {
         SVGTransformItem var3 = AbstractSVGTransformList.this.new SVGTransformItem();
         var3.setScale(var1, var2);
         this.listHandler.item(var3);
      }

      public void skewX(float var1) throws ParseException {
         SVGTransformItem var2 = AbstractSVGTransformList.this.new SVGTransformItem();
         var2.setSkewX(var1);
         this.listHandler.item(var2);
      }

      public void skewY(float var1) throws ParseException {
         SVGTransformItem var2 = AbstractSVGTransformList.this.new SVGTransformItem();
         var2.setSkewY(var1);
         this.listHandler.item(var2);
      }

      public void endTransformList() throws ParseException {
         this.listHandler.endList();
      }
   }

   protected class SVGTransformItem extends AbstractSVGTransform implements SVGItem {
      protected boolean xOnly;
      protected boolean angleOnly;
      protected AbstractSVGList parent;
      protected String itemStringValue;

      protected SVGTransformItem() {
      }

      protected SVGTransformItem(SVGTransform var2) {
         this.assign(var2);
      }

      protected void resetAttribute() {
         if (this.parent != null) {
            this.itemStringValue = null;
            this.parent.itemChanged();
         }

      }

      public void setParent(AbstractSVGList var1) {
         this.parent = var1;
      }

      public AbstractSVGList getParent() {
         return this.parent;
      }

      public String getValueAsString() {
         if (this.itemStringValue == null) {
            this.itemStringValue = this.getStringValue();
         }

         return this.itemStringValue;
      }

      public void assign(SVGTransform var1) {
         this.type = var1.getType();
         SVGMatrix var2 = var1.getMatrix();
         switch (this.type) {
            case 1:
               this.setMatrix(var2);
               break;
            case 2:
               this.setTranslate(var2.getE(), var2.getF());
               break;
            case 3:
               this.setScale(var2.getA(), var2.getD());
               break;
            case 4:
               if (var2.getE() == 0.0F) {
                  this.rotate(var1.getAngle());
               } else {
                  this.angleOnly = false;
                  if (var2.getA() == 1.0F) {
                     this.setRotate(var1.getAngle(), var2.getE(), var2.getF());
                  } else if (var1 instanceof AbstractSVGTransform) {
                     AbstractSVGTransform var3 = (AbstractSVGTransform)var1;
                     this.setRotate(var3.getAngle(), var3.getX(), var3.getY());
                  }
               }
               break;
            case 5:
               this.setSkewX(var1.getAngle());
               break;
            case 6:
               this.setSkewY(var1.getAngle());
         }

      }

      protected void translate(float var1) {
         this.xOnly = true;
         this.setTranslate(var1, 0.0F);
      }

      protected void rotate(float var1) {
         this.angleOnly = true;
         this.setRotate(var1, 0.0F, 0.0F);
      }

      protected void scale(float var1) {
         this.xOnly = true;
         this.setScale(var1, var1);
      }

      protected void matrix(float var1, float var2, float var3, float var4, float var5, float var6) {
         this.setMatrix(new SVGOMMatrix(new AffineTransform(var1, var2, var3, var4, var5, var6)));
      }

      public void setMatrix(SVGMatrix var1) {
         super.setMatrix(var1);
         this.resetAttribute();
      }

      public void setTranslate(float var1, float var2) {
         super.setTranslate(var1, var2);
         this.resetAttribute();
      }

      public void setScale(float var1, float var2) {
         super.setScale(var1, var2);
         this.resetAttribute();
      }

      public void setRotate(float var1, float var2, float var3) {
         super.setRotate(var1, var2, var3);
         this.resetAttribute();
      }

      public void setSkewX(float var1) {
         super.setSkewX(var1);
         this.resetAttribute();
      }

      public void setSkewY(float var1) {
         super.setSkewY(var1);
         this.resetAttribute();
      }

      protected SVGMatrix createMatrix() {
         return new AbstractSVGMatrix() {
            protected AffineTransform getAffineTransform() {
               return SVGTransformItem.this.affineTransform;
            }

            public void setA(float var1) throws DOMException {
               SVGTransformItem.this.type = 1;
               super.setA(var1);
               SVGTransformItem.this.resetAttribute();
            }

            public void setB(float var1) throws DOMException {
               SVGTransformItem.this.type = 1;
               super.setB(var1);
               SVGTransformItem.this.resetAttribute();
            }

            public void setC(float var1) throws DOMException {
               SVGTransformItem.this.type = 1;
               super.setC(var1);
               SVGTransformItem.this.resetAttribute();
            }

            public void setD(float var1) throws DOMException {
               SVGTransformItem.this.type = 1;
               super.setD(var1);
               SVGTransformItem.this.resetAttribute();
            }

            public void setE(float var1) throws DOMException {
               SVGTransformItem.this.type = 1;
               super.setE(var1);
               SVGTransformItem.this.resetAttribute();
            }

            public void setF(float var1) throws DOMException {
               SVGTransformItem.this.type = 1;
               super.setF(var1);
               SVGTransformItem.this.resetAttribute();
            }
         };
      }

      protected String getStringValue() {
         StringBuffer var1 = new StringBuffer();
         switch (this.type) {
            case 1:
               var1.append("matrix(");
               double[] var2 = new double[6];
               this.affineTransform.getMatrix(var2);

               for(int var3 = 0; var3 < 6; ++var3) {
                  if (var3 != 0) {
                     var1.append(' ');
                  }

                  var1.append((float)var2[var3]);
               }

               var1.append(')');
               break;
            case 2:
               var1.append("translate(");
               var1.append((float)this.affineTransform.getTranslateX());
               if (!this.xOnly) {
                  var1.append(' ');
                  var1.append((float)this.affineTransform.getTranslateY());
               }

               var1.append(')');
               break;
            case 3:
               var1.append("scale(");
               var1.append((float)this.affineTransform.getScaleX());
               if (!this.xOnly) {
                  var1.append(' ');
                  var1.append((float)this.affineTransform.getScaleY());
               }

               var1.append(')');
               break;
            case 4:
               var1.append("rotate(");
               var1.append(this.angle);
               if (!this.angleOnly) {
                  var1.append(' ');
                  var1.append(this.x);
                  var1.append(' ');
                  var1.append(this.y);
               }

               var1.append(')');
               break;
            case 5:
               var1.append("skewX(");
               var1.append(this.angle);
               var1.append(')');
               break;
            case 6:
               var1.append("skewY(");
               var1.append(this.angle);
               var1.append(')');
         }

         return var1.toString();
      }
   }
}
