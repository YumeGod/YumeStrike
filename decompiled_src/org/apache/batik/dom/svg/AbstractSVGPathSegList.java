package org.apache.batik.dom.svg;

import org.apache.batik.parser.DefaultPathHandler;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.PathParser;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGPathSeg;
import org.w3c.dom.svg.SVGPathSegArcAbs;
import org.w3c.dom.svg.SVGPathSegArcRel;
import org.w3c.dom.svg.SVGPathSegClosePath;
import org.w3c.dom.svg.SVGPathSegCurvetoCubicAbs;
import org.w3c.dom.svg.SVGPathSegCurvetoCubicRel;
import org.w3c.dom.svg.SVGPathSegCurvetoCubicSmoothAbs;
import org.w3c.dom.svg.SVGPathSegCurvetoCubicSmoothRel;
import org.w3c.dom.svg.SVGPathSegCurvetoQuadraticAbs;
import org.w3c.dom.svg.SVGPathSegCurvetoQuadraticRel;
import org.w3c.dom.svg.SVGPathSegCurvetoQuadraticSmoothAbs;
import org.w3c.dom.svg.SVGPathSegCurvetoQuadraticSmoothRel;
import org.w3c.dom.svg.SVGPathSegLinetoAbs;
import org.w3c.dom.svg.SVGPathSegLinetoHorizontalAbs;
import org.w3c.dom.svg.SVGPathSegLinetoHorizontalRel;
import org.w3c.dom.svg.SVGPathSegLinetoRel;
import org.w3c.dom.svg.SVGPathSegLinetoVerticalAbs;
import org.w3c.dom.svg.SVGPathSegLinetoVerticalRel;
import org.w3c.dom.svg.SVGPathSegList;
import org.w3c.dom.svg.SVGPathSegMovetoAbs;
import org.w3c.dom.svg.SVGPathSegMovetoRel;

public abstract class AbstractSVGPathSegList extends AbstractSVGList implements SVGPathSegList, SVGPathSegConstants {
   public static final String SVG_PATHSEG_LIST_SEPARATOR = " ";

   protected AbstractSVGPathSegList() {
   }

   protected String getItemSeparator() {
      return " ";
   }

   protected abstract SVGException createSVGException(short var1, String var2, Object[] var3);

   public SVGPathSeg initialize(SVGPathSeg var1) throws DOMException, SVGException {
      return (SVGPathSeg)this.initializeImpl(var1);
   }

   public SVGPathSeg getItem(int var1) throws DOMException {
      return (SVGPathSeg)this.getItemImpl(var1);
   }

   public SVGPathSeg insertItemBefore(SVGPathSeg var1, int var2) throws DOMException, SVGException {
      return (SVGPathSeg)this.insertItemBeforeImpl(var1, var2);
   }

   public SVGPathSeg replaceItem(SVGPathSeg var1, int var2) throws DOMException, SVGException {
      return (SVGPathSeg)this.replaceItemImpl(var1, var2);
   }

   public SVGPathSeg removeItem(int var1) throws DOMException {
      return (SVGPathSeg)this.removeItemImpl(var1);
   }

   public SVGPathSeg appendItem(SVGPathSeg var1) throws DOMException, SVGException {
      return (SVGPathSeg)this.appendItemImpl(var1);
   }

   protected SVGItem createSVGItem(Object var1) {
      SVGPathSeg var2 = (SVGPathSeg)var1;
      return this.createPathSegItem(var2);
   }

   protected void doParse(String var1, ListHandler var2) throws ParseException {
      PathParser var3 = new PathParser();
      PathSegListBuilder var4 = new PathSegListBuilder(var2);
      var3.setPathHandler(var4);
      var3.parse(var1);
   }

   protected void checkItemType(Object var1) {
      if (!(var1 instanceof SVGPathSeg)) {
         this.createSVGException((short)0, "expected SVGPathSeg", (Object[])null);
      }

   }

   protected SVGPathSegItem createPathSegItem(SVGPathSeg var1) {
      Object var2 = null;
      short var3 = var1.getPathSegType();
      switch (var3) {
         case 1:
            var2 = new SVGPathSegItem(var1);
            break;
         case 2:
         case 3:
         case 4:
         case 5:
            var2 = new SVGPathSegMovetoLinetoItem(var1);
            break;
         case 6:
         case 7:
            var2 = new SVGPathSegCurvetoCubicItem(var1);
            break;
         case 8:
         case 9:
            var2 = new SVGPathSegCurvetoQuadraticItem(var1);
            break;
         case 10:
         case 11:
            var2 = new SVGPathSegArcItem(var1);
            break;
         case 12:
         case 13:
            var2 = new SVGPathSegLinetoHorizontalItem(var1);
            break;
         case 14:
         case 15:
            var2 = new SVGPathSegLinetoVerticalItem(var1);
            break;
         case 16:
         case 17:
            var2 = new SVGPathSegCurvetoCubicSmoothItem(var1);
            break;
         case 18:
         case 19:
            var2 = new SVGPathSegCurvetoQuadraticSmoothItem(var1);
      }

      return (SVGPathSegItem)var2;
   }

   protected class PathSegListBuilder extends DefaultPathHandler {
      protected ListHandler listHandler;

      public PathSegListBuilder(ListHandler var2) {
         this.listHandler = var2;
      }

      public void startPath() throws ParseException {
         this.listHandler.startList();
      }

      public void endPath() throws ParseException {
         this.listHandler.endList();
      }

      public void movetoRel(float var1, float var2) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegMovetoLinetoItem((short)3, "m", var1, var2));
      }

      public void movetoAbs(float var1, float var2) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegMovetoLinetoItem((short)2, "M", var1, var2));
      }

      public void closePath() throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegItem((short)1, "z"));
      }

      public void linetoRel(float var1, float var2) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegMovetoLinetoItem((short)5, "l", var1, var2));
      }

      public void linetoAbs(float var1, float var2) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegMovetoLinetoItem((short)4, "L", var1, var2));
      }

      public void linetoHorizontalRel(float var1) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegLinetoHorizontalItem((short)13, "h", var1));
      }

      public void linetoHorizontalAbs(float var1) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegLinetoHorizontalItem((short)12, "H", var1));
      }

      public void linetoVerticalRel(float var1) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegLinetoVerticalItem((short)15, "v", var1));
      }

      public void linetoVerticalAbs(float var1) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegLinetoVerticalItem((short)14, "V", var1));
      }

      public void curvetoCubicRel(float var1, float var2, float var3, float var4, float var5, float var6) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegCurvetoCubicItem((short)7, "c", var1, var2, var3, var4, var5, var6));
      }

      public void curvetoCubicAbs(float var1, float var2, float var3, float var4, float var5, float var6) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegCurvetoCubicItem((short)6, "C", var1, var2, var3, var4, var5, var6));
      }

      public void curvetoCubicSmoothRel(float var1, float var2, float var3, float var4) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegCurvetoCubicSmoothItem((short)17, "s", var1, var2, var3, var4));
      }

      public void curvetoCubicSmoothAbs(float var1, float var2, float var3, float var4) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegCurvetoCubicSmoothItem((short)16, "S", var1, var2, var3, var4));
      }

      public void curvetoQuadraticRel(float var1, float var2, float var3, float var4) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegCurvetoQuadraticItem((short)9, "q", var1, var2, var3, var4));
      }

      public void curvetoQuadraticAbs(float var1, float var2, float var3, float var4) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegCurvetoQuadraticItem((short)8, "Q", var1, var2, var3, var4));
      }

      public void curvetoQuadraticSmoothRel(float var1, float var2) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegCurvetoQuadraticSmoothItem((short)19, "t", var1, var2));
      }

      public void curvetoQuadraticSmoothAbs(float var1, float var2) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegCurvetoQuadraticSmoothItem((short)18, "T", var1, var2));
      }

      public void arcRel(float var1, float var2, float var3, boolean var4, boolean var5, float var6, float var7) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegArcItem((short)11, "a", var1, var2, var3, var4, var5, var6, var7));
      }

      public void arcAbs(float var1, float var2, float var3, boolean var4, boolean var5, float var6, float var7) throws ParseException {
         this.listHandler.item(AbstractSVGPathSegList.this.new SVGPathSegArcItem((short)10, "A", var1, var2, var3, var4, var5, var6, var7));
      }
   }

   public class SVGPathSegCurvetoQuadraticSmoothItem extends SVGPathSegItem implements SVGPathSegCurvetoQuadraticSmoothAbs, SVGPathSegCurvetoQuadraticSmoothRel {
      public SVGPathSegCurvetoQuadraticSmoothItem(short var2, String var3, float var4, float var5) {
         super(var2, var3);
         this.x = var4;
         this.y = var5;
      }

      public SVGPathSegCurvetoQuadraticSmoothItem(SVGPathSeg var2) {
         super();
         this.type = var2.getPathSegType();
         switch (this.type) {
            case 18:
               this.letter = "T";
               this.x = ((SVGPathSegCurvetoQuadraticSmoothAbs)var2).getX();
               this.y = ((SVGPathSegCurvetoQuadraticSmoothAbs)var2).getY();
               break;
            case 19:
               this.letter = "t";
               this.x = ((SVGPathSegCurvetoQuadraticSmoothRel)var2).getX();
               this.y = ((SVGPathSegCurvetoQuadraticSmoothRel)var2).getY();
         }

      }

      public float getX() {
         return this.x;
      }

      public float getY() {
         return this.y;
      }

      public void setX(float var1) {
         this.x = var1;
         this.resetAttribute();
      }

      public void setY(float var1) {
         this.y = var1;
         this.resetAttribute();
      }

      protected String getStringValue() {
         return this.letter + ' ' + Float.toString(this.x) + ' ' + Float.toString(this.y);
      }
   }

   public class SVGPathSegCurvetoCubicSmoothItem extends SVGPathSegItem implements SVGPathSegCurvetoCubicSmoothAbs, SVGPathSegCurvetoCubicSmoothRel {
      public SVGPathSegCurvetoCubicSmoothItem(short var2, String var3, float var4, float var5, float var6, float var7) {
         super(var2, var3);
         this.x = var6;
         this.y = var7;
         this.x2 = var4;
         this.y2 = var5;
      }

      public SVGPathSegCurvetoCubicSmoothItem(SVGPathSeg var2) {
         super();
         this.type = var2.getPathSegType();
         switch (this.type) {
            case 16:
               this.letter = "S";
               this.x = ((SVGPathSegCurvetoCubicSmoothAbs)var2).getX();
               this.y = ((SVGPathSegCurvetoCubicSmoothAbs)var2).getY();
               this.x2 = ((SVGPathSegCurvetoCubicSmoothAbs)var2).getX2();
               this.y2 = ((SVGPathSegCurvetoCubicSmoothAbs)var2).getY2();
               break;
            case 17:
               this.letter = "s";
               this.x = ((SVGPathSegCurvetoCubicSmoothRel)var2).getX();
               this.y = ((SVGPathSegCurvetoCubicSmoothRel)var2).getY();
               this.x2 = ((SVGPathSegCurvetoCubicSmoothRel)var2).getX2();
               this.y2 = ((SVGPathSegCurvetoCubicSmoothRel)var2).getY2();
         }

      }

      public float getX() {
         return this.x;
      }

      public float getY() {
         return this.y;
      }

      public void setX(float var1) {
         this.x = var1;
         this.resetAttribute();
      }

      public void setY(float var1) {
         this.y = var1;
         this.resetAttribute();
      }

      public float getX2() {
         return this.x2;
      }

      public float getY2() {
         return this.y2;
      }

      public void setX2(float var1) {
         this.x2 = var1;
         this.resetAttribute();
      }

      public void setY2(float var1) {
         this.y2 = var1;
         this.resetAttribute();
      }

      protected String getStringValue() {
         return this.letter + ' ' + Float.toString(this.x2) + ' ' + Float.toString(this.y2) + ' ' + Float.toString(this.x) + ' ' + Float.toString(this.y);
      }
   }

   public class SVGPathSegLinetoVerticalItem extends SVGPathSegItem implements SVGPathSegLinetoVerticalAbs, SVGPathSegLinetoVerticalRel {
      public SVGPathSegLinetoVerticalItem(short var2, String var3, float var4) {
         super(var2, var3);
         this.y = var4;
      }

      public SVGPathSegLinetoVerticalItem(SVGPathSeg var2) {
         super();
         this.type = var2.getPathSegType();
         switch (this.type) {
            case 14:
               this.letter = "V";
               this.y = ((SVGPathSegLinetoVerticalAbs)var2).getY();
               break;
            case 15:
               this.letter = "v";
               this.y = ((SVGPathSegLinetoVerticalRel)var2).getY();
         }

      }

      public float getY() {
         return this.y;
      }

      public void setY(float var1) {
         this.y = var1;
         this.resetAttribute();
      }

      protected String getStringValue() {
         return this.letter + ' ' + Float.toString(this.y);
      }
   }

   public class SVGPathSegLinetoHorizontalItem extends SVGPathSegItem implements SVGPathSegLinetoHorizontalAbs, SVGPathSegLinetoHorizontalRel {
      public SVGPathSegLinetoHorizontalItem(short var2, String var3, float var4) {
         super(var2, var3);
         this.x = var4;
      }

      public SVGPathSegLinetoHorizontalItem(SVGPathSeg var2) {
         super();
         this.type = var2.getPathSegType();
         switch (this.type) {
            case 12:
               this.letter = "H";
               this.x = ((SVGPathSegLinetoHorizontalAbs)var2).getX();
               break;
            case 13:
               this.letter = "h";
               this.x = ((SVGPathSegLinetoHorizontalRel)var2).getX();
         }

      }

      public float getX() {
         return this.x;
      }

      public void setX(float var1) {
         this.x = var1;
         this.resetAttribute();
      }

      protected String getStringValue() {
         return this.letter + ' ' + Float.toString(this.x);
      }
   }

   public class SVGPathSegArcItem extends SVGPathSegItem implements SVGPathSegArcAbs, SVGPathSegArcRel {
      public SVGPathSegArcItem(short var2, String var3, float var4, float var5, float var6, boolean var7, boolean var8, float var9, float var10) {
         super(var2, var3);
         this.x = var9;
         this.y = var10;
         this.r1 = var4;
         this.r2 = var5;
         this.angle = var6;
         this.largeArcFlag = var7;
         this.sweepFlag = var8;
      }

      public SVGPathSegArcItem(SVGPathSeg var2) {
         super();
         this.type = var2.getPathSegType();
         switch (this.type) {
            case 10:
               this.letter = "A";
               this.x = ((SVGPathSegArcAbs)var2).getX();
               this.y = ((SVGPathSegArcAbs)var2).getY();
               this.r1 = ((SVGPathSegArcAbs)var2).getR1();
               this.r2 = ((SVGPathSegArcAbs)var2).getR2();
               this.angle = ((SVGPathSegArcAbs)var2).getAngle();
               this.largeArcFlag = ((SVGPathSegArcAbs)var2).getLargeArcFlag();
               this.sweepFlag = ((SVGPathSegArcAbs)var2).getSweepFlag();
               break;
            case 11:
               this.letter = "a";
               this.x = ((SVGPathSegArcRel)var2).getX();
               this.y = ((SVGPathSegArcRel)var2).getY();
               this.r1 = ((SVGPathSegArcRel)var2).getR1();
               this.r2 = ((SVGPathSegArcRel)var2).getR2();
               this.angle = ((SVGPathSegArcRel)var2).getAngle();
               this.largeArcFlag = ((SVGPathSegArcRel)var2).getLargeArcFlag();
               this.sweepFlag = ((SVGPathSegArcRel)var2).getSweepFlag();
         }

      }

      public float getX() {
         return this.x;
      }

      public float getY() {
         return this.y;
      }

      public void setX(float var1) {
         this.x = var1;
         this.resetAttribute();
      }

      public void setY(float var1) {
         this.y = var1;
         this.resetAttribute();
      }

      public float getR1() {
         return this.r1;
      }

      public float getR2() {
         return this.r2;
      }

      public void setR1(float var1) {
         this.r1 = var1;
         this.resetAttribute();
      }

      public void setR2(float var1) {
         this.r2 = var1;
         this.resetAttribute();
      }

      public float getAngle() {
         return this.angle;
      }

      public void setAngle(float var1) {
         this.angle = var1;
         this.resetAttribute();
      }

      public boolean getSweepFlag() {
         return this.sweepFlag;
      }

      public void setSweepFlag(boolean var1) {
         this.sweepFlag = var1;
         this.resetAttribute();
      }

      public boolean getLargeArcFlag() {
         return this.largeArcFlag;
      }

      public void setLargeArcFlag(boolean var1) {
         this.largeArcFlag = var1;
         this.resetAttribute();
      }

      protected String getStringValue() {
         return this.letter + ' ' + Float.toString(this.r1) + ' ' + Float.toString(this.r2) + ' ' + Float.toString(this.angle) + ' ' + (this.largeArcFlag ? "1" : "0") + ' ' + (this.sweepFlag ? "1" : "0") + ' ' + Float.toString(this.x) + ' ' + Float.toString(this.y);
      }
   }

   public class SVGPathSegCurvetoQuadraticItem extends SVGPathSegItem implements SVGPathSegCurvetoQuadraticAbs, SVGPathSegCurvetoQuadraticRel {
      public SVGPathSegCurvetoQuadraticItem(short var2, String var3, float var4, float var5, float var6, float var7) {
         super(var2, var3);
         this.x = var6;
         this.y = var7;
         this.x1 = var4;
         this.y1 = var5;
      }

      public SVGPathSegCurvetoQuadraticItem(SVGPathSeg var2) {
         super();
         this.type = var2.getPathSegType();
         switch (this.type) {
            case 8:
               this.letter = "Q";
               this.x = ((SVGPathSegCurvetoQuadraticAbs)var2).getX();
               this.y = ((SVGPathSegCurvetoQuadraticAbs)var2).getY();
               this.x1 = ((SVGPathSegCurvetoQuadraticAbs)var2).getX1();
               this.y1 = ((SVGPathSegCurvetoQuadraticAbs)var2).getY1();
               break;
            case 9:
               this.letter = "q";
               this.x = ((SVGPathSegCurvetoQuadraticRel)var2).getX();
               this.y = ((SVGPathSegCurvetoQuadraticRel)var2).getY();
               this.x1 = ((SVGPathSegCurvetoQuadraticRel)var2).getX1();
               this.y1 = ((SVGPathSegCurvetoQuadraticRel)var2).getY1();
         }

      }

      public float getX() {
         return this.x;
      }

      public float getY() {
         return this.y;
      }

      public void setX(float var1) {
         this.x = var1;
         this.resetAttribute();
      }

      public void setY(float var1) {
         this.y = var1;
         this.resetAttribute();
      }

      public float getX1() {
         return this.x1;
      }

      public float getY1() {
         return this.y1;
      }

      public void setX1(float var1) {
         this.x1 = var1;
         this.resetAttribute();
      }

      public void setY1(float var1) {
         this.y1 = var1;
         this.resetAttribute();
      }

      protected String getStringValue() {
         return this.letter + ' ' + Float.toString(this.x1) + ' ' + Float.toString(this.y1) + ' ' + Float.toString(this.x) + ' ' + Float.toString(this.y);
      }
   }

   public class SVGPathSegCurvetoCubicItem extends SVGPathSegItem implements SVGPathSegCurvetoCubicAbs, SVGPathSegCurvetoCubicRel {
      public SVGPathSegCurvetoCubicItem(short var2, String var3, float var4, float var5, float var6, float var7, float var8, float var9) {
         super(var2, var3);
         this.x = var8;
         this.y = var9;
         this.x1 = var4;
         this.y1 = var5;
         this.x2 = var6;
         this.y2 = var7;
      }

      public SVGPathSegCurvetoCubicItem(SVGPathSeg var2) {
         super();
         this.type = var2.getPathSegType();
         switch (this.type) {
            case 6:
               this.letter = "C";
               this.x = ((SVGPathSegCurvetoCubicAbs)var2).getX();
               this.y = ((SVGPathSegCurvetoCubicAbs)var2).getY();
               this.x1 = ((SVGPathSegCurvetoCubicAbs)var2).getX1();
               this.y1 = ((SVGPathSegCurvetoCubicAbs)var2).getY1();
               this.x2 = ((SVGPathSegCurvetoCubicAbs)var2).getX2();
               this.y2 = ((SVGPathSegCurvetoCubicAbs)var2).getY2();
               break;
            case 7:
               this.letter = "c";
               this.x = ((SVGPathSegCurvetoCubicRel)var2).getX();
               this.y = ((SVGPathSegCurvetoCubicRel)var2).getY();
               this.x1 = ((SVGPathSegCurvetoCubicRel)var2).getX1();
               this.y1 = ((SVGPathSegCurvetoCubicRel)var2).getY1();
               this.x2 = ((SVGPathSegCurvetoCubicRel)var2).getX2();
               this.y2 = ((SVGPathSegCurvetoCubicRel)var2).getY2();
         }

      }

      public float getX() {
         return this.x;
      }

      public float getY() {
         return this.y;
      }

      public void setX(float var1) {
         this.x = var1;
         this.resetAttribute();
      }

      public void setY(float var1) {
         this.y = var1;
         this.resetAttribute();
      }

      public float getX1() {
         return this.x1;
      }

      public float getY1() {
         return this.y1;
      }

      public void setX1(float var1) {
         this.x1 = var1;
         this.resetAttribute();
      }

      public void setY1(float var1) {
         this.y1 = var1;
         this.resetAttribute();
      }

      public float getX2() {
         return this.x2;
      }

      public float getY2() {
         return this.y2;
      }

      public void setX2(float var1) {
         this.x2 = var1;
         this.resetAttribute();
      }

      public void setY2(float var1) {
         this.y2 = var1;
         this.resetAttribute();
      }

      protected String getStringValue() {
         return this.letter + ' ' + Float.toString(this.x1) + ' ' + Float.toString(this.y1) + ' ' + Float.toString(this.x2) + ' ' + Float.toString(this.y2) + ' ' + Float.toString(this.x) + ' ' + Float.toString(this.y);
      }
   }

   public class SVGPathSegMovetoLinetoItem extends SVGPathSegItem implements SVGPathSegMovetoAbs, SVGPathSegMovetoRel, SVGPathSegLinetoAbs, SVGPathSegLinetoRel {
      public SVGPathSegMovetoLinetoItem(short var2, String var3, float var4, float var5) {
         super(var2, var3);
         this.x = var4;
         this.y = var5;
      }

      public SVGPathSegMovetoLinetoItem(SVGPathSeg var2) {
         super();
         this.type = var2.getPathSegType();
         switch (this.type) {
            case 2:
               this.letter = "M";
               this.x = ((SVGPathSegMovetoAbs)var2).getX();
               this.y = ((SVGPathSegMovetoAbs)var2).getY();
               break;
            case 3:
               this.letter = "m";
               this.x = ((SVGPathSegMovetoRel)var2).getX();
               this.y = ((SVGPathSegMovetoRel)var2).getY();
               break;
            case 4:
               this.letter = "L";
               this.x = ((SVGPathSegLinetoAbs)var2).getX();
               this.y = ((SVGPathSegLinetoAbs)var2).getY();
               break;
            case 5:
               this.letter = "l";
               this.x = ((SVGPathSegLinetoRel)var2).getX();
               this.y = ((SVGPathSegLinetoRel)var2).getY();
         }

      }

      public float getX() {
         return this.x;
      }

      public float getY() {
         return this.y;
      }

      public void setX(float var1) {
         this.x = var1;
         this.resetAttribute();
      }

      public void setY(float var1) {
         this.y = var1;
         this.resetAttribute();
      }

      protected String getStringValue() {
         return this.letter + ' ' + Float.toString(this.x) + ' ' + Float.toString(this.y);
      }
   }

   protected class SVGPathSegItem extends AbstractSVGItem implements SVGPathSeg, SVGPathSegClosePath {
      protected short type;
      protected String letter;
      protected float x;
      protected float y;
      protected float x1;
      protected float y1;
      protected float x2;
      protected float y2;
      protected float r1;
      protected float r2;
      protected float angle;
      protected boolean largeArcFlag;
      protected boolean sweepFlag;

      protected SVGPathSegItem() {
      }

      public SVGPathSegItem(short var2, String var3) {
         this.type = var2;
         this.letter = var3;
      }

      public SVGPathSegItem(SVGPathSeg var2) {
         this.type = var2.getPathSegType();
         switch (this.type) {
            case 1:
               this.letter = "z";
            default:
         }
      }

      protected String getStringValue() {
         return this.letter;
      }

      public short getPathSegType() {
         return this.type;
      }

      public String getPathSegTypeAsLetter() {
         return this.letter;
      }
   }
}
