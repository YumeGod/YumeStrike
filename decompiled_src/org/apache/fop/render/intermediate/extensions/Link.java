package org.apache.fop.render.intermediate.extensions;

import java.awt.Rectangle;

public class Link {
   private AbstractAction action;
   private Rectangle targetRect;

   public Link(AbstractAction action, Rectangle targetRect) {
      this.action = action;
      this.targetRect = targetRect;
   }

   public AbstractAction getAction() {
      return this.action;
   }

   public Rectangle getTargetRect() {
      return new Rectangle(this.targetRect);
   }

   public void setAction(AbstractAction action) {
      this.action = action;
   }
}
