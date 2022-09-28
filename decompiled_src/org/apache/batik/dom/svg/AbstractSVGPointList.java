package org.apache.batik.dom.svg;

import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.PointsHandler;
import org.apache.batik.parser.PointsParser;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGMatrix;
import org.w3c.dom.svg.SVGPoint;
import org.w3c.dom.svg.SVGPointList;

public abstract class AbstractSVGPointList extends AbstractSVGList implements SVGPointList {
   public static final String SVG_POINT_LIST_SEPARATOR = " ";

   protected String getItemSeparator() {
      return " ";
   }

   protected abstract SVGException createSVGException(short var1, String var2, Object[] var3);

   public SVGPoint initialize(SVGPoint var1) throws DOMException, SVGException {
      return (SVGPoint)this.initializeImpl(var1);
   }

   public SVGPoint getItem(int var1) throws DOMException {
      return (SVGPoint)this.getItemImpl(var1);
   }

   public SVGPoint insertItemBefore(SVGPoint var1, int var2) throws DOMException, SVGException {
      return (SVGPoint)this.insertItemBeforeImpl(var1, var2);
   }

   public SVGPoint replaceItem(SVGPoint var1, int var2) throws DOMException, SVGException {
      return (SVGPoint)this.replaceItemImpl(var1, var2);
   }

   public SVGPoint removeItem(int var1) throws DOMException {
      return (SVGPoint)this.removeItemImpl(var1);
   }

   public SVGPoint appendItem(SVGPoint var1) throws DOMException, SVGException {
      return (SVGPoint)this.appendItemImpl(var1);
   }

   protected SVGItem createSVGItem(Object var1) {
      SVGPoint var2 = (SVGPoint)var1;
      return new SVGPointItem(var2.getX(), var2.getY());
   }

   protected void doParse(String var1, ListHandler var2) throws ParseException {
      PointsParser var3 = new PointsParser();
      PointsListBuilder var4 = new PointsListBuilder(var2);
      var3.setPointsHandler(var4);
      var3.parse(var1);
   }

   protected void checkItemType(Object var1) throws SVGException {
      if (!(var1 instanceof SVGPoint)) {
         this.createSVGException((short)0, "expected.point", (Object[])null);
      }

   }

   protected class PointsListBuilder implements PointsHandler {
      protected ListHandler listHandler;

      public PointsListBuilder(ListHandler var2) {
         this.listHandler = var2;
      }

      public void startPoints() throws ParseException {
         this.listHandler.startList();
      }

      public void point(float var1, float var2) throws ParseException {
         this.listHandler.item(AbstractSVGPointList.this.new SVGPointItem(var1, var2));
      }

      public void endPoints() throws ParseException {
         this.listHandler.endList();
      }
   }

   protected class SVGPointItem extends AbstractSVGItem implements SVGPoint {
      protected float x;
      protected float y;

      public SVGPointItem(float var2, float var3) {
         this.x = var2;
         this.y = var3;
      }

      protected String getStringValue() {
         return Float.toString(this.x) + ',' + Float.toString(this.y);
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

      public SVGPoint matrixTransform(SVGMatrix var1) {
         return SVGOMPoint.matrixTransform(this, var1);
      }
   }
}
