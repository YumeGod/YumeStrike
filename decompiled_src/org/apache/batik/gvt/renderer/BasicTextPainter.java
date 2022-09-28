package org.apache.batik.gvt.renderer;

import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.TextPainter;
import org.apache.batik.gvt.text.ConcreteTextLayoutFactory;
import org.apache.batik.gvt.text.Mark;
import org.apache.batik.gvt.text.TextHit;
import org.apache.batik.gvt.text.TextLayoutFactory;

public abstract class BasicTextPainter implements TextPainter {
   private static TextLayoutFactory textLayoutFactory = new ConcreteTextLayoutFactory();
   protected FontRenderContext fontRenderContext = new FontRenderContext(new AffineTransform(), true, true);
   protected FontRenderContext aaOffFontRenderContext = new FontRenderContext(new AffineTransform(), false, true);

   protected TextLayoutFactory getTextLayoutFactory() {
      return textLayoutFactory;
   }

   public Mark selectAt(double var1, double var3, TextNode var5) {
      return this.hitTest(var1, var3, var5);
   }

   public Mark selectTo(double var1, double var3, Mark var5) {
      return var5 == null ? null : this.hitTest(var1, var3, var5.getTextNode());
   }

   public Rectangle2D getGeometryBounds(TextNode var1) {
      return this.getOutline(var1).getBounds2D();
   }

   protected abstract Mark hitTest(double var1, double var3, TextNode var5);

   protected static class BasicMark implements Mark {
      private TextNode node;
      private TextHit hit;

      protected BasicMark(TextNode var1, TextHit var2) {
         this.hit = var2;
         this.node = var1;
      }

      public TextHit getHit() {
         return this.hit;
      }

      public TextNode getTextNode() {
         return this.node;
      }

      public int getCharIndex() {
         return this.hit.getCharIndex();
      }
   }
}
