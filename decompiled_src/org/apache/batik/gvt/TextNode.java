package org.apache.batik.gvt;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.text.AttributedCharacterIterator;
import java.util.List;
import org.apache.batik.gvt.renderer.StrokingTextPainter;
import org.apache.batik.gvt.text.AttributedCharacterSpanIterator;
import org.apache.batik.gvt.text.GVTAttributedCharacterIterator;
import org.apache.batik.gvt.text.Mark;
import org.apache.batik.gvt.text.TextHit;
import org.apache.batik.gvt.text.TextPaintInfo;
import org.apache.batik.gvt.text.TextSpanLayout;

public class TextNode extends AbstractGraphicsNode implements Selectable {
   public static final AttributedCharacterIterator.Attribute PAINT_INFO;
   protected Point2D location = new Point2D.Float(0.0F, 0.0F);
   protected AttributedCharacterIterator aci;
   protected String text;
   protected Mark beginMark = null;
   protected Mark endMark = null;
   protected List textRuns;
   protected TextPainter textPainter = StrokingTextPainter.getInstance();
   private Rectangle2D geometryBounds;
   private Rectangle2D primitiveBounds;
   private Shape outline;

   public void setTextPainter(TextPainter var1) {
      if (var1 == null) {
         this.textPainter = StrokingTextPainter.getInstance();
      } else {
         this.textPainter = var1;
      }

   }

   public TextPainter getTextPainter() {
      return this.textPainter;
   }

   public List getTextRuns() {
      return this.textRuns;
   }

   public void setTextRuns(List var1) {
      this.textRuns = var1;
   }

   public String getText() {
      if (this.text != null) {
         return this.text;
      } else {
         if (this.aci == null) {
            this.text = "";
         } else {
            StringBuffer var1 = new StringBuffer(this.aci.getEndIndex());

            for(char var2 = this.aci.first(); var2 != '\uffff'; var2 = this.aci.next()) {
               var1.append(var2);
            }

            this.text = var1.toString();
         }

         return this.text;
      }
   }

   public void setLocation(Point2D var1) {
      this.fireGraphicsNodeChangeStarted();
      this.invalidateGeometryCache();
      this.location = var1;
      this.fireGraphicsNodeChangeCompleted();
   }

   public Point2D getLocation() {
      return this.location;
   }

   public void swapTextPaintInfo(TextPaintInfo var1, TextPaintInfo var2) {
      this.fireGraphicsNodeChangeStarted();
      this.invalidateGeometryCache();
      var2.set(var1);
      this.fireGraphicsNodeChangeCompleted();
   }

   public void setAttributedCharacterIterator(AttributedCharacterIterator var1) {
      this.fireGraphicsNodeChangeStarted();
      this.invalidateGeometryCache();
      this.aci = var1;
      this.text = null;
      this.textRuns = null;
      this.fireGraphicsNodeChangeCompleted();
   }

   public AttributedCharacterIterator getAttributedCharacterIterator() {
      return this.aci;
   }

   protected void invalidateGeometryCache() {
      super.invalidateGeometryCache();
      this.primitiveBounds = null;
      this.geometryBounds = null;
      this.outline = null;
   }

   public Rectangle2D getPrimitiveBounds() {
      if (this.primitiveBounds == null && this.aci != null) {
         this.primitiveBounds = this.textPainter.getBounds2D(this);
      }

      return this.primitiveBounds;
   }

   public Rectangle2D getGeometryBounds() {
      if (this.geometryBounds == null && this.aci != null) {
         this.geometryBounds = this.textPainter.getGeometryBounds(this);
      }

      return this.geometryBounds;
   }

   public Rectangle2D getSensitiveBounds() {
      return this.getGeometryBounds();
   }

   public Shape getOutline() {
      if (this.outline == null && this.aci != null) {
         this.outline = this.textPainter.getOutline(this);
      }

      return this.outline;
   }

   public Mark getMarkerForChar(int var1, boolean var2) {
      return this.textPainter.getMark(this, var1, var2);
   }

   public void setSelection(Mark var1, Mark var2) {
      if (var1.getTextNode() == this && var2.getTextNode() == this) {
         this.beginMark = var1;
         this.endMark = var2;
      } else {
         throw new Error("Markers not from this TextNode");
      }
   }

   public boolean selectAt(double var1, double var3) {
      this.beginMark = this.textPainter.selectAt(var1, var3, this);
      return true;
   }

   public boolean selectTo(double var1, double var3) {
      Mark var5 = this.textPainter.selectTo(var1, var3, this.beginMark);
      if (var5 == null) {
         return false;
      } else if (var5 != this.endMark) {
         this.endMark = var5;
         return true;
      } else {
         return false;
      }
   }

   public boolean selectAll(double var1, double var3) {
      this.beginMark = this.textPainter.selectFirst(this);
      this.endMark = this.textPainter.selectLast(this);
      return true;
   }

   public Object getSelection() {
      AttributedCharacterSpanIterator var1 = null;
      if (this.aci == null) {
         return var1;
      } else {
         int[] var2 = this.textPainter.getSelected(this.beginMark, this.endMark);
         if (var2 != null && var2.length > 1) {
            if (var2[0] > var2[1]) {
               int var3 = var2[1];
               var2[1] = var2[0];
               var2[0] = var3;
            }

            var1 = new AttributedCharacterSpanIterator(this.aci, var2[0], var2[1] + 1);
         }

         return var1;
      }
   }

   public Shape getHighlightShape() {
      Shape var1 = this.textPainter.getHighlightShape(this.beginMark, this.endMark);
      AffineTransform var2 = this.getGlobalTransform();
      var1 = var2.createTransformedShape(var1);
      return var1;
   }

   public void primitivePaint(Graphics2D var1) {
      Shape var2 = var1.getClip();
      if (var2 != null && !(var2 instanceof GeneralPath)) {
         var1.setClip(new GeneralPath(var2));
      }

      this.textPainter.paint(this, var1);
   }

   public boolean contains(Point2D var1) {
      if (!super.contains(var1)) {
         return false;
      } else {
         List var2 = this.getTextRuns();

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            StrokingTextPainter.TextRun var4 = (StrokingTextPainter.TextRun)var2.get(var3);
            TextSpanLayout var5 = var4.getLayout();
            float var6 = (float)var1.getX();
            float var7 = (float)var1.getY();
            TextHit var8 = var5.hitTestChar(var6, var7);
            if (var8 != null && this.contains(var1, var5.getBounds2D())) {
               return true;
            }
         }

         return false;
      }
   }

   protected boolean contains(Point2D var1, Rectangle2D var2) {
      if (var2 != null && var2.contains(var1)) {
         switch (this.pointerEventType) {
            case 0:
            case 1:
            case 2:
            case 3:
               return this.isVisible;
            case 4:
            case 5:
            case 6:
            case 7:
               return true;
            case 8:
               return false;
            default:
               return false;
         }
      } else {
         return false;
      }
   }

   static {
      PAINT_INFO = GVTAttributedCharacterIterator.TextAttribute.PAINT_INFO;
   }

   public static final class Anchor implements Serializable {
      public static final int ANCHOR_START = 0;
      public static final int ANCHOR_MIDDLE = 1;
      public static final int ANCHOR_END = 2;
      public static final Anchor START = new Anchor(0);
      public static final Anchor MIDDLE = new Anchor(1);
      public static final Anchor END = new Anchor(2);
      private int type;

      private Anchor(int var1) {
         this.type = var1;
      }

      public int getType() {
         return this.type;
      }

      private Object readResolve() throws ObjectStreamException {
         switch (this.type) {
            case 0:
               return START;
            case 1:
               return MIDDLE;
            case 2:
               return END;
            default:
               throw new Error("Unknown Anchor type");
         }
      }
   }
}
