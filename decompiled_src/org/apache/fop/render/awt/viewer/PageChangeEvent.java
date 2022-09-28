package org.apache.fop.render.awt.viewer;

import java.util.EventObject;

public class PageChangeEvent extends EventObject {
   private int oldPage;
   private int newPage;

   public PageChangeEvent(PreviewPanel panel, int oldPage, int newPage) {
      super(panel);
      this.oldPage = oldPage;
      this.newPage = newPage;
   }

   public int getNewPage() {
      return this.newPage;
   }

   public int getOldPage() {
      return this.oldPage;
   }
}
