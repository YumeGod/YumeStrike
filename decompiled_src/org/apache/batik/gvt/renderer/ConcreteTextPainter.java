package org.apache.batik.gvt.renderer;

import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import org.apache.batik.gvt.TextNode;

public abstract class ConcreteTextPainter extends BasicTextPainter {
   public void paint(AttributedCharacterIterator var1, Point2D var2, TextNode.Anchor var3, Graphics2D var4) {
      TextLayout var5 = new TextLayout(var1, this.fontRenderContext);
      float var6 = var5.getAdvance();
      float var7 = 0.0F;
      switch (var3.getType()) {
         case 1:
            var7 = -var6 / 2.0F;
            break;
         case 2:
            var7 = -var6;
      }

      var5.draw(var4, (float)(var2.getX() + (double)var7), (float)var2.getY());
   }
}
