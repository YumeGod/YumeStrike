package org.apache.batik.dom.svg;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.batik.anim.values.AnimatablePathDataValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.PathArrayProducer;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAnimatedPathData;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGPathSeg;
import org.w3c.dom.svg.SVGPathSegList;

public class SVGOMAnimatedPathData extends AbstractSVGAnimatedValue implements SVGAnimatedPathData {
   protected boolean changing;
   protected BaseSVGPathSegList pathSegs;
   protected NormalizedBaseSVGPathSegList normalizedPathSegs;
   protected AnimSVGPathSegList animPathSegs;
   protected String defaultValue;

   public SVGOMAnimatedPathData(AbstractElement var1, String var2, String var3, String var4) {
      super(var1, var2, var3);
      this.defaultValue = var4;
   }

   public SVGPathSegList getAnimatedNormalizedPathSegList() {
      throw new UnsupportedOperationException("SVGAnimatedPathData.getAnimatedNormalizedPathSegList is not implemented");
   }

   public SVGPathSegList getAnimatedPathSegList() {
      if (this.animPathSegs == null) {
         this.animPathSegs = new AnimSVGPathSegList();
      }

      return this.animPathSegs;
   }

   public SVGPathSegList getNormalizedPathSegList() {
      if (this.normalizedPathSegs == null) {
         this.normalizedPathSegs = new NormalizedBaseSVGPathSegList();
      }

      return this.normalizedPathSegs;
   }

   public SVGPathSegList getPathSegList() {
      if (this.pathSegs == null) {
         this.pathSegs = new BaseSVGPathSegList();
      }

      return this.pathSegs;
   }

   public void check() {
      if (!this.hasAnimVal) {
         if (this.pathSegs == null) {
            this.pathSegs = new BaseSVGPathSegList();
         }

         this.pathSegs.revalidate();
         if (this.pathSegs.missing) {
            throw new LiveAttributeException(this.element, this.localName, (short)0, (String)null);
         }

         if (this.pathSegs.malformed) {
            throw new LiveAttributeException(this.element, this.localName, (short)1, this.pathSegs.getValueAsString());
         }
      }

   }

   public AnimatableValue getUnderlyingValue(AnimationTarget var1) {
      SVGPathSegList var2 = this.getPathSegList();
      PathArrayProducer var3 = new PathArrayProducer();
      SVGAnimatedPathDataSupport.handlePathSegList(var2, var3);
      return new AnimatablePathDataValue(var1, var3.getPathCommands(), var3.getPathParameters());
   }

   protected void updateAnimatedValue(AnimatableValue var1) {
      if (var1 == null) {
         this.hasAnimVal = false;
      } else {
         this.hasAnimVal = true;
         AnimatablePathDataValue var2 = (AnimatablePathDataValue)var1;
         if (this.animPathSegs == null) {
            this.animPathSegs = new AnimSVGPathSegList();
         }

         this.animPathSegs.setAnimatedValue(var2.getCommands(), var2.getParameters());
      }

      this.fireAnimatedAttributeListeners();
   }

   public void attrAdded(Attr var1, String var2) {
      if (!this.changing) {
         if (this.pathSegs != null) {
            this.pathSegs.invalidate();
         }

         if (this.normalizedPathSegs != null) {
            this.normalizedPathSegs.invalidate();
         }
      }

      this.fireBaseAttributeListeners();
      if (!this.hasAnimVal) {
         this.fireAnimatedAttributeListeners();
      }

   }

   public void attrModified(Attr var1, String var2, String var3) {
      if (!this.changing) {
         if (this.pathSegs != null) {
            this.pathSegs.invalidate();
         }

         if (this.normalizedPathSegs != null) {
            this.normalizedPathSegs.invalidate();
         }
      }

      this.fireBaseAttributeListeners();
      if (!this.hasAnimVal) {
         this.fireAnimatedAttributeListeners();
      }

   }

   public void attrRemoved(Attr var1, String var2) {
      if (!this.changing) {
         if (this.pathSegs != null) {
            this.pathSegs.invalidate();
         }

         if (this.normalizedPathSegs != null) {
            this.normalizedPathSegs.invalidate();
         }
      }

      this.fireBaseAttributeListeners();
      if (!this.hasAnimVal) {
         this.fireAnimatedAttributeListeners();
      }

   }

   public class AnimSVGPathSegList extends AbstractSVGPathSegList {
      private int[] parameterIndex = new int[1];

      public AnimSVGPathSegList() {
         this.itemList = new ArrayList(1);
      }

      protected DOMException createDOMException(short var1, String var2, Object[] var3) {
         return SVGOMAnimatedPathData.this.element.createDOMException(var1, var2, var3);
      }

      protected SVGException createSVGException(short var1, String var2, Object[] var3) {
         return ((SVGOMElement)SVGOMAnimatedPathData.this.element).createSVGException(var1, var2, var3);
      }

      public int getNumberOfItems() {
         return SVGOMAnimatedPathData.this.hasAnimVal ? super.getNumberOfItems() : SVGOMAnimatedPathData.this.getPathSegList().getNumberOfItems();
      }

      public SVGPathSeg getItem(int var1) throws DOMException {
         return SVGOMAnimatedPathData.this.hasAnimVal ? super.getItem(var1) : SVGOMAnimatedPathData.this.getPathSegList().getItem(var1);
      }

      protected String getValueAsString() {
         if (this.itemList.size() == 0) {
            return "";
         } else {
            StringBuffer var1 = new StringBuffer(this.itemList.size() * 8);
            Iterator var2 = this.itemList.iterator();
            if (var2.hasNext()) {
               var1.append(((SVGItem)var2.next()).getValueAsString());
            }

            while(var2.hasNext()) {
               var1.append(this.getItemSeparator());
               var1.append(((SVGItem)var2.next()).getValueAsString());
            }

            return var1.toString();
         }
      }

      protected void setAttributeValue(String var1) {
      }

      public void clear() throws DOMException {
         throw SVGOMAnimatedPathData.this.element.createDOMException((short)7, "readonly.pathseg.list", (Object[])null);
      }

      public SVGPathSeg initialize(SVGPathSeg var1) throws DOMException, SVGException {
         throw SVGOMAnimatedPathData.this.element.createDOMException((short)7, "readonly.pathseg.list", (Object[])null);
      }

      public SVGPathSeg insertItemBefore(SVGPathSeg var1, int var2) throws DOMException, SVGException {
         throw SVGOMAnimatedPathData.this.element.createDOMException((short)7, "readonly.pathseg.list", (Object[])null);
      }

      public SVGPathSeg replaceItem(SVGPathSeg var1, int var2) throws DOMException, SVGException {
         throw SVGOMAnimatedPathData.this.element.createDOMException((short)7, "readonly.pathseg.list", (Object[])null);
      }

      public SVGPathSeg removeItem(int var1) throws DOMException {
         throw SVGOMAnimatedPathData.this.element.createDOMException((short)7, "readonly.pathseg.list", (Object[])null);
      }

      public SVGPathSeg appendItem(SVGPathSeg var1) throws DOMException {
         throw SVGOMAnimatedPathData.this.element.createDOMException((short)7, "readonly.pathseg.list", (Object[])null);
      }

      protected AbstractSVGPathSegList.SVGPathSegItem newItem(short var1, float[] var2, int[] var3) {
         float var4;
         float var5;
         String var10004;
         float var10005;
         int var10006;
         int var10007;
         int var10008;
         int var10009;
         int var10010;
         int var10011;
         int var10012;
         int var10013;
         int var10014;
         switch (var1) {
            case 1:
               return new AbstractSVGPathSegList.SVGPathSegItem(var1, SVGPathSegConstants.PATHSEG_LETTERS[var1]);
            case 2:
            case 3:
            case 4:
            case 5:
               var10004 = SVGPathSegConstants.PATHSEG_LETTERS[var1];
               var10009 = var3[0];
               var10006 = var3[0];
               var3[0] = var10009 + 1;
               var10005 = var2[var10006];
               var10010 = var3[0];
               var10007 = var3[0];
               var3[0] = var10010 + 1;
               return new AbstractSVGPathSegList.SVGPathSegMovetoLinetoItem(var1, var10004, var10005, var2[var10007]);
            case 6:
            case 7:
               var10004 = SVGPathSegConstants.PATHSEG_LETTERS[var1];
               var10009 = var3[0];
               var10006 = var3[0];
               var3[0] = var10009 + 1;
               var10005 = var2[var10006];
               var10010 = var3[0];
               var10007 = var3[0];
               var3[0] = var10010 + 1;
               var4 = var2[var10007];
               var10011 = var3[0];
               var10008 = var3[0];
               var3[0] = var10011 + 1;
               var5 = var2[var10008];
               var10012 = var3[0];
               var10009 = var3[0];
               var3[0] = var10012 + 1;
               float var7 = var2[var10009];
               var10013 = var3[0];
               var10010 = var3[0];
               var3[0] = var10013 + 1;
               float var10 = var2[var10010];
               var10014 = var3[0];
               var10011 = var3[0];
               var3[0] = var10014 + 1;
               return new AbstractSVGPathSegList.SVGPathSegCurvetoCubicItem(var1, var10004, var10005, var4, var5, var7, var10, var2[var10011]);
            case 8:
            case 9:
               var10004 = SVGPathSegConstants.PATHSEG_LETTERS[var1];
               var10009 = var3[0];
               var10006 = var3[0];
               var3[0] = var10009 + 1;
               var10005 = var2[var10006];
               var10010 = var3[0];
               var10007 = var3[0];
               var3[0] = var10010 + 1;
               var4 = var2[var10007];
               var10011 = var3[0];
               var10008 = var3[0];
               var3[0] = var10011 + 1;
               var5 = var2[var10008];
               var10012 = var3[0];
               var10009 = var3[0];
               var3[0] = var10012 + 1;
               return new AbstractSVGPathSegList.SVGPathSegCurvetoQuadraticItem(var1, var10004, var10005, var4, var5, var2[var10009]);
            case 10:
            case 11:
               var10004 = SVGPathSegConstants.PATHSEG_LETTERS[var1];
               var10009 = var3[0];
               var10006 = var3[0];
               var3[0] = var10009 + 1;
               var10005 = var2[var10006];
               var10010 = var3[0];
               var10007 = var3[0];
               var3[0] = var10010 + 1;
               var4 = var2[var10007];
               var10011 = var3[0];
               var10008 = var3[0];
               var3[0] = var10011 + 1;
               var5 = var2[var10008];
               var10012 = var3[0];
               var10009 = var3[0];
               var3[0] = var10012 + 1;
               boolean var6 = var2[var10009] != 0.0F;
               var10013 = var3[0];
               var10010 = var3[0];
               var3[0] = var10013 + 1;
               boolean var8 = var2[var10010] != 0.0F;
               var10014 = var3[0];
               var10011 = var3[0];
               var3[0] = var10014 + 1;
               float var9 = var2[var10011];
               int var10015 = var3[0];
               var10012 = var3[0];
               var3[0] = var10015 + 1;
               return new AbstractSVGPathSegList.SVGPathSegArcItem(var1, var10004, var10005, var4, var5, var6, var8, var9, var2[var10012]);
            case 12:
            case 13:
               var10004 = SVGPathSegConstants.PATHSEG_LETTERS[var1];
               var10009 = var3[0];
               var10006 = var3[0];
               var3[0] = var10009 + 1;
               return new AbstractSVGPathSegList.SVGPathSegLinetoHorizontalItem(var1, var10004, var2[var10006]);
            case 14:
            case 15:
               var10004 = SVGPathSegConstants.PATHSEG_LETTERS[var1];
               var10009 = var3[0];
               var10006 = var3[0];
               var3[0] = var10009 + 1;
               return new AbstractSVGPathSegList.SVGPathSegLinetoVerticalItem(var1, var10004, var2[var10006]);
            case 16:
            case 17:
               var10004 = SVGPathSegConstants.PATHSEG_LETTERS[var1];
               var10009 = var3[0];
               var10006 = var3[0];
               var3[0] = var10009 + 1;
               var10005 = var2[var10006];
               var10010 = var3[0];
               var10007 = var3[0];
               var3[0] = var10010 + 1;
               var4 = var2[var10007];
               var10011 = var3[0];
               var10008 = var3[0];
               var3[0] = var10011 + 1;
               var5 = var2[var10008];
               var10012 = var3[0];
               var10009 = var3[0];
               var3[0] = var10012 + 1;
               return new AbstractSVGPathSegList.SVGPathSegCurvetoCubicSmoothItem(var1, var10004, var10005, var4, var5, var2[var10009]);
            case 18:
            case 19:
               var10004 = SVGPathSegConstants.PATHSEG_LETTERS[var1];
               var10009 = var3[0];
               var10006 = var3[0];
               var3[0] = var10009 + 1;
               var10005 = var2[var10006];
               var10010 = var3[0];
               var10007 = var3[0];
               var3[0] = var10010 + 1;
               return new AbstractSVGPathSegList.SVGPathSegCurvetoQuadraticSmoothItem(var1, var10004, var10005, var2[var10007]);
            default:
               return null;
         }
      }

      protected void setAnimatedValue(short[] var1, float[] var2) {
         int var3 = this.itemList.size();
         int var4 = 0;
         int[] var5 = this.parameterIndex;

         for(var5[0] = 0; var4 < var3 && var4 < var1.length; ++var4) {
            SVGPathSeg var6 = (SVGPathSeg)this.itemList.get(var4);
            if (var6.getPathSegType() != var1[var4]) {
               this.newItem(var1[var4], var2, var5);
            } else {
               int var10002;
               int var10005;
               switch (var1[var4]) {
                  case 1:
                  default:
                     break;
                  case 2:
                  case 3:
                  case 4:
                  case 5:
                     AbstractSVGPathSegList.SVGPathSegMovetoLinetoItem var14 = (AbstractSVGPathSegList.SVGPathSegMovetoLinetoItem)var6;
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var14.x = var2[var10002];
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var14.y = var2[var10002];
                     break;
                  case 6:
                  case 7:
                     AbstractSVGPathSegList.SVGPathSegCurvetoCubicItem var13 = (AbstractSVGPathSegList.SVGPathSegCurvetoCubicItem)var6;
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var13.x1 = var2[var10002];
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var13.y1 = var2[var10002];
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var13.x2 = var2[var10002];
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var13.y2 = var2[var10002];
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var13.x = var2[var10002];
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var13.y = var2[var10002];
                     break;
                  case 8:
                  case 9:
                     AbstractSVGPathSegList.SVGPathSegCurvetoQuadraticItem var12 = (AbstractSVGPathSegList.SVGPathSegCurvetoQuadraticItem)var6;
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var12.x1 = var2[var10002];
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var12.y1 = var2[var10002];
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var12.x = var2[var10002];
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var12.y = var2[var10002];
                     break;
                  case 10:
                  case 11:
                     AbstractSVGPathSegList.SVGPathSegArcItem var11 = (AbstractSVGPathSegList.SVGPathSegArcItem)var6;
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var11.r1 = var2[var10002];
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var11.r2 = var2[var10002];
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var11.angle = var2[var10002];
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var11.largeArcFlag = var2[var10002] != 0.0F;
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var11.sweepFlag = var2[var10002] != 0.0F;
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var11.x = var2[var10002];
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var11.y = var2[var10002];
                     break;
                  case 12:
                  case 13:
                     AbstractSVGPathSegList.SVGPathSegLinetoHorizontalItem var10 = (AbstractSVGPathSegList.SVGPathSegLinetoHorizontalItem)var6;
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var10.x = var2[var10002];
                     break;
                  case 14:
                  case 15:
                     AbstractSVGPathSegList.SVGPathSegLinetoVerticalItem var9 = (AbstractSVGPathSegList.SVGPathSegLinetoVerticalItem)var6;
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var9.y = var2[var10002];
                     break;
                  case 16:
                  case 17:
                     AbstractSVGPathSegList.SVGPathSegCurvetoCubicSmoothItem var8 = (AbstractSVGPathSegList.SVGPathSegCurvetoCubicSmoothItem)var6;
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var8.x2 = var2[var10002];
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var8.y2 = var2[var10002];
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var8.x = var2[var10002];
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var8.y = var2[var10002];
                     break;
                  case 18:
                  case 19:
                     AbstractSVGPathSegList.SVGPathSegCurvetoQuadraticSmoothItem var7 = (AbstractSVGPathSegList.SVGPathSegCurvetoQuadraticSmoothItem)var6;
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var7.x = var2[var10002];
                     var10005 = var5[0];
                     var10002 = var5[0];
                     var5[0] = var10005 + 1;
                     var7.y = var2[var10002];
               }
            }
         }

         while(var4 < var1.length) {
            this.appendItemImpl(this.newItem(var1[var4], var2, var5));
            ++var4;
         }

         while(var3 > var1.length) {
            --var3;
            this.removeItemImpl(var3);
         }

      }

      protected void resetAttribute() {
      }

      protected void resetAttribute(SVGItem var1) {
      }

      protected void revalidate() {
         this.valid = true;
      }
   }

   public class NormalizedBaseSVGPathSegList extends AbstractSVGNormPathSegList {
      protected boolean missing;
      protected boolean malformed;

      protected DOMException createDOMException(short var1, String var2, Object[] var3) {
         return SVGOMAnimatedPathData.this.element.createDOMException(var1, var2, var3);
      }

      protected SVGException createSVGException(short var1, String var2, Object[] var3) {
         return ((SVGOMElement)SVGOMAnimatedPathData.this.element).createSVGException(var1, var2, var3);
      }

      protected String getValueAsString() throws SVGException {
         Attr var1 = SVGOMAnimatedPathData.this.element.getAttributeNodeNS(SVGOMAnimatedPathData.this.namespaceURI, SVGOMAnimatedPathData.this.localName);
         return var1 == null ? SVGOMAnimatedPathData.this.defaultValue : var1.getValue();
      }

      protected void setAttributeValue(String var1) {
         try {
            SVGOMAnimatedPathData.this.changing = true;
            SVGOMAnimatedPathData.this.element.setAttributeNS(SVGOMAnimatedPathData.this.namespaceURI, SVGOMAnimatedPathData.this.localName, var1);
         } finally {
            SVGOMAnimatedPathData.this.changing = false;
         }

      }

      protected void revalidate() {
         if (!this.valid) {
            this.valid = true;
            this.missing = false;
            this.malformed = false;
            String var1 = this.getValueAsString();
            if (var1 == null) {
               this.missing = true;
            } else {
               try {
                  AbstractSVGList.ListBuilder var2 = new AbstractSVGList.ListBuilder();
                  this.doParse(var1, var2);
                  if (var2.getList() != null) {
                     this.clear(this.itemList);
                  }

                  this.itemList = var2.getList();
               } catch (ParseException var3) {
                  this.itemList = new ArrayList(1);
                  this.malformed = true;
               }

            }
         }
      }
   }

   public class BaseSVGPathSegList extends AbstractSVGPathSegList {
      protected boolean missing;
      protected boolean malformed;

      protected DOMException createDOMException(short var1, String var2, Object[] var3) {
         return SVGOMAnimatedPathData.this.element.createDOMException(var1, var2, var3);
      }

      protected SVGException createSVGException(short var1, String var2, Object[] var3) {
         return ((SVGOMElement)SVGOMAnimatedPathData.this.element).createSVGException(var1, var2, var3);
      }

      protected String getValueAsString() {
         Attr var1 = SVGOMAnimatedPathData.this.element.getAttributeNodeNS(SVGOMAnimatedPathData.this.namespaceURI, SVGOMAnimatedPathData.this.localName);
         return var1 == null ? SVGOMAnimatedPathData.this.defaultValue : var1.getValue();
      }

      protected void setAttributeValue(String var1) {
         try {
            SVGOMAnimatedPathData.this.changing = true;
            SVGOMAnimatedPathData.this.element.setAttributeNS(SVGOMAnimatedPathData.this.namespaceURI, SVGOMAnimatedPathData.this.localName, var1);
         } finally {
            SVGOMAnimatedPathData.this.changing = false;
         }

      }

      protected void resetAttribute() {
         super.resetAttribute();
         this.missing = false;
         this.malformed = false;
      }

      protected void resetAttribute(SVGItem var1) {
         super.resetAttribute(var1);
         this.missing = false;
         this.malformed = false;
      }

      protected void revalidate() {
         if (!this.valid) {
            this.valid = true;
            this.missing = false;
            this.malformed = false;
            String var1 = this.getValueAsString();
            if (var1 == null) {
               this.missing = true;
            } else {
               try {
                  AbstractSVGList.ListBuilder var2 = new AbstractSVGList.ListBuilder();
                  this.doParse(var1, var2);
                  if (var2.getList() != null) {
                     this.clear(this.itemList);
                  }

                  this.itemList = var2.getList();
               } catch (ParseException var3) {
                  this.itemList = new ArrayList(1);
                  this.malformed = true;
               }

            }
         }
      }
   }
}
