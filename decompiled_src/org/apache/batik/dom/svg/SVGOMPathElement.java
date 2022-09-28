package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedNumber;
import org.w3c.dom.svg.SVGPathElement;
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
import org.w3c.dom.svg.SVGPoint;

public class SVGOMPathElement extends SVGGraphicsElement implements SVGPathElement, SVGPathSegConstants {
   protected static DoublyIndexedTable xmlTraitInformation;
   protected SVGOMAnimatedPathData d;

   protected SVGOMPathElement() {
   }

   public SVGOMPathElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.initializeLiveAttributes();
   }

   protected void initializeAllLiveAttributes() {
      super.initializeAllLiveAttributes();
      this.initializeLiveAttributes();
   }

   private void initializeLiveAttributes() {
      this.d = this.createLiveAnimatedPathData((String)null, "d", "");
   }

   public String getLocalName() {
      return "path";
   }

   public SVGAnimatedNumber getPathLength() {
      throw new UnsupportedOperationException("SVGPathElement.getPathLength is not implemented");
   }

   public float getTotalLength() {
      return SVGPathSupport.getTotalLength(this);
   }

   public SVGPoint getPointAtLength(float var1) {
      return SVGPathSupport.getPointAtLength(this, var1);
   }

   public int getPathSegAtLength(float var1) {
      return SVGPathSupport.getPathSegAtLength(this, var1);
   }

   public SVGOMAnimatedPathData getAnimatedPathData() {
      return this.d;
   }

   public SVGPathSegList getPathSegList() {
      return this.d.getPathSegList();
   }

   public SVGPathSegList getNormalizedPathSegList() {
      return this.d.getNormalizedPathSegList();
   }

   public SVGPathSegList getAnimatedPathSegList() {
      return this.d.getAnimatedPathSegList();
   }

   public SVGPathSegList getAnimatedNormalizedPathSegList() {
      return this.d.getAnimatedNormalizedPathSegList();
   }

   public SVGPathSegClosePath createSVGPathSegClosePath() {
      return new SVGPathSegClosePath() {
         public short getPathSegType() {
            return 1;
         }

         public String getPathSegTypeAsLetter() {
            return "z";
         }
      };
   }

   public SVGPathSegMovetoAbs createSVGPathSegMovetoAbs(final float var1, final float var2) {
      return new SVGPathSegMovetoAbs() {
         protected float x = var1;
         protected float y = var2;

         public short getPathSegType() {
            return 2;
         }

         public String getPathSegTypeAsLetter() {
            return "M";
         }

         public float getX() {
            return this.x;
         }

         public void setX(float var1x) {
            this.x = var1x;
         }

         public float getY() {
            return this.y;
         }

         public void setY(float var1x) {
            this.y = var1x;
         }
      };
   }

   public SVGPathSegMovetoRel createSVGPathSegMovetoRel(final float var1, final float var2) {
      return new SVGPathSegMovetoRel() {
         protected float x = var1;
         protected float y = var2;

         public short getPathSegType() {
            return 3;
         }

         public String getPathSegTypeAsLetter() {
            return "m";
         }

         public float getX() {
            return this.x;
         }

         public void setX(float var1x) {
            this.x = var1x;
         }

         public float getY() {
            return this.y;
         }

         public void setY(float var1x) {
            this.y = var1x;
         }
      };
   }

   public SVGPathSegLinetoAbs createSVGPathSegLinetoAbs(final float var1, final float var2) {
      return new SVGPathSegLinetoAbs() {
         protected float x = var1;
         protected float y = var2;

         public short getPathSegType() {
            return 4;
         }

         public String getPathSegTypeAsLetter() {
            return "L";
         }

         public float getX() {
            return this.x;
         }

         public void setX(float var1x) {
            this.x = var1x;
         }

         public float getY() {
            return this.y;
         }

         public void setY(float var1x) {
            this.y = var1x;
         }
      };
   }

   public SVGPathSegLinetoRel createSVGPathSegLinetoRel(final float var1, final float var2) {
      return new SVGPathSegLinetoRel() {
         protected float x = var1;
         protected float y = var2;

         public short getPathSegType() {
            return 5;
         }

         public String getPathSegTypeAsLetter() {
            return "l";
         }

         public float getX() {
            return this.x;
         }

         public void setX(float var1x) {
            this.x = var1x;
         }

         public float getY() {
            return this.y;
         }

         public void setY(float var1x) {
            this.y = var1x;
         }
      };
   }

   public SVGPathSegLinetoHorizontalAbs createSVGPathSegLinetoHorizontalAbs(final float var1) {
      return new SVGPathSegLinetoHorizontalAbs() {
         protected float x = var1;

         public short getPathSegType() {
            return 12;
         }

         public String getPathSegTypeAsLetter() {
            return "H";
         }

         public float getX() {
            return this.x;
         }

         public void setX(float var1x) {
            this.x = var1x;
         }
      };
   }

   public SVGPathSegLinetoHorizontalRel createSVGPathSegLinetoHorizontalRel(final float var1) {
      return new SVGPathSegLinetoHorizontalRel() {
         protected float x = var1;

         public short getPathSegType() {
            return 13;
         }

         public String getPathSegTypeAsLetter() {
            return "h";
         }

         public float getX() {
            return this.x;
         }

         public void setX(float var1x) {
            this.x = var1x;
         }
      };
   }

   public SVGPathSegLinetoVerticalAbs createSVGPathSegLinetoVerticalAbs(final float var1) {
      return new SVGPathSegLinetoVerticalAbs() {
         protected float y = var1;

         public short getPathSegType() {
            return 14;
         }

         public String getPathSegTypeAsLetter() {
            return "V";
         }

         public float getY() {
            return this.y;
         }

         public void setY(float var1x) {
            this.y = var1x;
         }
      };
   }

   public SVGPathSegLinetoVerticalRel createSVGPathSegLinetoVerticalRel(final float var1) {
      return new SVGPathSegLinetoVerticalRel() {
         protected float y = var1;

         public short getPathSegType() {
            return 15;
         }

         public String getPathSegTypeAsLetter() {
            return "v";
         }

         public float getY() {
            return this.y;
         }

         public void setY(float var1x) {
            this.y = var1x;
         }
      };
   }

   public SVGPathSegCurvetoCubicAbs createSVGPathSegCurvetoCubicAbs(final float var1, final float var2, final float var3, final float var4, final float var5, final float var6) {
      return new SVGPathSegCurvetoCubicAbs() {
         protected float x = var1;
         protected float y = var2;
         protected float x1 = var3;
         protected float y1 = var4;
         protected float x2 = var5;
         protected float y2 = var6;

         public short getPathSegType() {
            return 6;
         }

         public String getPathSegTypeAsLetter() {
            return "C";
         }

         public float getX() {
            return this.x;
         }

         public void setX(float var1x) {
            this.x = var1x;
         }

         public float getY() {
            return this.y;
         }

         public void setY(float var1x) {
            this.y = var1x;
         }

         public float getX1() {
            return this.x1;
         }

         public void setX1(float var1x) {
            this.x1 = var1x;
         }

         public float getY1() {
            return this.y1;
         }

         public void setY1(float var1x) {
            this.y1 = var1x;
         }

         public float getX2() {
            return this.x2;
         }

         public void setX2(float var1x) {
            this.x2 = var1x;
         }

         public float getY2() {
            return this.y2;
         }

         public void setY2(float var1x) {
            this.y2 = var1x;
         }
      };
   }

   public SVGPathSegCurvetoCubicRel createSVGPathSegCurvetoCubicRel(final float var1, final float var2, final float var3, final float var4, final float var5, final float var6) {
      return new SVGPathSegCurvetoCubicRel() {
         protected float x = var1;
         protected float y = var2;
         protected float x1 = var3;
         protected float y1 = var4;
         protected float x2 = var5;
         protected float y2 = var6;

         public short getPathSegType() {
            return 7;
         }

         public String getPathSegTypeAsLetter() {
            return "c";
         }

         public float getX() {
            return this.x;
         }

         public void setX(float var1x) {
            this.x = var1x;
         }

         public float getY() {
            return this.y;
         }

         public void setY(float var1x) {
            this.y = var1x;
         }

         public float getX1() {
            return this.x1;
         }

         public void setX1(float var1x) {
            this.x1 = var1x;
         }

         public float getY1() {
            return this.y1;
         }

         public void setY1(float var1x) {
            this.y1 = var1x;
         }

         public float getX2() {
            return this.x2;
         }

         public void setX2(float var1x) {
            this.x2 = var1x;
         }

         public float getY2() {
            return this.y2;
         }

         public void setY2(float var1x) {
            this.y2 = var1x;
         }
      };
   }

   public SVGPathSegCurvetoQuadraticAbs createSVGPathSegCurvetoQuadraticAbs(final float var1, final float var2, final float var3, final float var4) {
      return new SVGPathSegCurvetoQuadraticAbs() {
         protected float x = var1;
         protected float y = var2;
         protected float x1 = var3;
         protected float y1 = var4;

         public short getPathSegType() {
            return 8;
         }

         public String getPathSegTypeAsLetter() {
            return "Q";
         }

         public float getX() {
            return this.x;
         }

         public void setX(float var1x) {
            this.x = var1x;
         }

         public float getY() {
            return this.y;
         }

         public void setY(float var1x) {
            this.y = var1x;
         }

         public float getX1() {
            return this.x1;
         }

         public void setX1(float var1x) {
            this.x1 = var1x;
         }

         public float getY1() {
            return this.y1;
         }

         public void setY1(float var1x) {
            this.y1 = var1x;
         }
      };
   }

   public SVGPathSegCurvetoQuadraticRel createSVGPathSegCurvetoQuadraticRel(final float var1, final float var2, final float var3, final float var4) {
      return new SVGPathSegCurvetoQuadraticRel() {
         protected float x = var1;
         protected float y = var2;
         protected float x1 = var3;
         protected float y1 = var4;

         public short getPathSegType() {
            return 9;
         }

         public String getPathSegTypeAsLetter() {
            return "q";
         }

         public float getX() {
            return this.x;
         }

         public void setX(float var1x) {
            this.x = var1x;
         }

         public float getY() {
            return this.y;
         }

         public void setY(float var1x) {
            this.y = var1x;
         }

         public float getX1() {
            return this.x1;
         }

         public void setX1(float var1x) {
            this.x1 = var1x;
         }

         public float getY1() {
            return this.y1;
         }

         public void setY1(float var1x) {
            this.y1 = var1x;
         }
      };
   }

   public SVGPathSegCurvetoCubicSmoothAbs createSVGPathSegCurvetoCubicSmoothAbs(final float var1, final float var2, final float var3, final float var4) {
      return new SVGPathSegCurvetoCubicSmoothAbs() {
         protected float x = var1;
         protected float y = var2;
         protected float x2 = var3;
         protected float y2 = var4;

         public short getPathSegType() {
            return 16;
         }

         public String getPathSegTypeAsLetter() {
            return "S";
         }

         public float getX() {
            return this.x;
         }

         public void setX(float var1x) {
            this.x = var1x;
         }

         public float getY() {
            return this.y;
         }

         public void setY(float var1x) {
            this.y = var1x;
         }

         public float getX2() {
            return this.x2;
         }

         public void setX2(float var1x) {
            this.x2 = var1x;
         }

         public float getY2() {
            return this.y2;
         }

         public void setY2(float var1x) {
            this.y2 = var1x;
         }
      };
   }

   public SVGPathSegCurvetoCubicSmoothRel createSVGPathSegCurvetoCubicSmoothRel(final float var1, final float var2, final float var3, final float var4) {
      return new SVGPathSegCurvetoCubicSmoothRel() {
         protected float x = var1;
         protected float y = var2;
         protected float x2 = var3;
         protected float y2 = var4;

         public short getPathSegType() {
            return 17;
         }

         public String getPathSegTypeAsLetter() {
            return "s";
         }

         public float getX() {
            return this.x;
         }

         public void setX(float var1x) {
            this.x = var1x;
         }

         public float getY() {
            return this.y;
         }

         public void setY(float var1x) {
            this.y = var1x;
         }

         public float getX2() {
            return this.x2;
         }

         public void setX2(float var1x) {
            this.x2 = var1x;
         }

         public float getY2() {
            return this.y2;
         }

         public void setY2(float var1x) {
            this.y2 = var1x;
         }
      };
   }

   public SVGPathSegCurvetoQuadraticSmoothAbs createSVGPathSegCurvetoQuadraticSmoothAbs(final float var1, final float var2) {
      return new SVGPathSegCurvetoQuadraticSmoothAbs() {
         protected float x = var1;
         protected float y = var2;

         public short getPathSegType() {
            return 18;
         }

         public String getPathSegTypeAsLetter() {
            return "T";
         }

         public float getX() {
            return this.x;
         }

         public void setX(float var1x) {
            this.x = var1x;
         }

         public float getY() {
            return this.y;
         }

         public void setY(float var1x) {
            this.y = var1x;
         }
      };
   }

   public SVGPathSegCurvetoQuadraticSmoothRel createSVGPathSegCurvetoQuadraticSmoothRel(final float var1, final float var2) {
      return new SVGPathSegCurvetoQuadraticSmoothRel() {
         protected float x = var1;
         protected float y = var2;

         public short getPathSegType() {
            return 19;
         }

         public String getPathSegTypeAsLetter() {
            return "t";
         }

         public float getX() {
            return this.x;
         }

         public void setX(float var1x) {
            this.x = var1x;
         }

         public float getY() {
            return this.y;
         }

         public void setY(float var1x) {
            this.y = var1x;
         }
      };
   }

   public SVGPathSegArcAbs createSVGPathSegArcAbs(final float var1, final float var2, final float var3, final float var4, final float var5, final boolean var6, final boolean var7) {
      return new SVGPathSegArcAbs() {
         protected float x = var1;
         protected float y = var2;
         protected float r1 = var3;
         protected float r2 = var4;
         protected float angle = var5;
         protected boolean largeArcFlag = var6;
         protected boolean sweepFlag = var7;

         public short getPathSegType() {
            return 10;
         }

         public String getPathSegTypeAsLetter() {
            return "A";
         }

         public float getX() {
            return this.x;
         }

         public void setX(float var1x) {
            this.x = var1x;
         }

         public float getY() {
            return this.y;
         }

         public void setY(float var1x) {
            this.y = var1x;
         }

         public float getR1() {
            return this.r1;
         }

         public void setR1(float var1x) {
            this.r1 = var1x;
         }

         public float getR2() {
            return this.r2;
         }

         public void setR2(float var1x) {
            this.r2 = var1x;
         }

         public float getAngle() {
            return this.angle;
         }

         public void setAngle(float var1x) {
            this.angle = var1x;
         }

         public boolean getLargeArcFlag() {
            return this.largeArcFlag;
         }

         public void setLargeArcFlag(boolean var1x) {
            this.largeArcFlag = var1x;
         }

         public boolean getSweepFlag() {
            return this.sweepFlag;
         }

         public void setSweepFlag(boolean var1x) {
            this.sweepFlag = var1x;
         }
      };
   }

   public SVGPathSegArcRel createSVGPathSegArcRel(final float var1, final float var2, final float var3, final float var4, final float var5, final boolean var6, final boolean var7) {
      return new SVGPathSegArcRel() {
         protected float x = var1;
         protected float y = var2;
         protected float r1 = var3;
         protected float r2 = var4;
         protected float angle = var5;
         protected boolean largeArcFlag = var6;
         protected boolean sweepFlag = var7;

         public short getPathSegType() {
            return 11;
         }

         public String getPathSegTypeAsLetter() {
            return "a";
         }

         public float getX() {
            return this.x;
         }

         public void setX(float var1x) {
            this.x = var1x;
         }

         public float getY() {
            return this.y;
         }

         public void setY(float var1x) {
            this.y = var1x;
         }

         public float getR1() {
            return this.r1;
         }

         public void setR1(float var1x) {
            this.r1 = var1x;
         }

         public float getR2() {
            return this.r2;
         }

         public void setR2(float var1x) {
            this.r2 = var1x;
         }

         public float getAngle() {
            return this.angle;
         }

         public void setAngle(float var1x) {
            this.angle = var1x;
         }

         public boolean getLargeArcFlag() {
            return this.largeArcFlag;
         }

         public void setLargeArcFlag(boolean var1x) {
            this.largeArcFlag = var1x;
         }

         public boolean getSweepFlag() {
            return this.sweepFlag;
         }

         public void setSweepFlag(boolean var1x) {
            this.sweepFlag = var1x;
         }
      };
   }

   protected Node newNode() {
      return new SVGOMPathElement();
   }

   protected DoublyIndexedTable getTraitInformationTable() {
      return xmlTraitInformation;
   }

   static {
      DoublyIndexedTable var0 = new DoublyIndexedTable(SVGGraphicsElement.xmlTraitInformation);
      var0.put((Object)null, "d", new TraitInformation(true, 22));
      var0.put((Object)null, "pathLength", new TraitInformation(true, 2));
      xmlTraitInformation = var0;
   }
}
