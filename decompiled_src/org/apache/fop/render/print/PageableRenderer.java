package org.apache.fop.render.print;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.io.IOException;
import java.util.Map;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.area.PageViewport;
import org.apache.fop.render.java2d.Java2DRenderer;

public class PageableRenderer extends Java2DRenderer implements Pageable {
   public static final String PAGES_MODE = "even-odd";
   public static final String START_PAGE = "start-page";
   public static final String END_PAGE = "end-page";
   protected int startNumber = 0;
   protected int endNumber = -1;
   protected PagesMode mode;
   private PageFilter pageFilter;

   public PageableRenderer() {
      this.mode = PagesMode.ALL;
   }

   public String getMimeType() {
      return "application/X-fop-print";
   }

   public void setUserAgent(FOUserAgent agent) {
      super.setUserAgent(agent);
      Map rendererOptions = agent.getRendererOptions();
      this.processOptions(rendererOptions);
      this.pageFilter = new DefaultPageFilter();
   }

   private void processOptions(Map rendererOptions) {
      Object o = rendererOptions.get("even-odd");
      if (o != null) {
         if (o instanceof PagesMode) {
            this.mode = (PagesMode)o;
         } else {
            if (!(o instanceof String)) {
               throw new IllegalArgumentException("Renderer option even-odd must be an 'all', 'even', 'odd' or a PagesMode instance.");
            }

            this.mode = PagesMode.byName((String)o);
         }
      }

      o = rendererOptions.get("start-page");
      if (o != null) {
         this.startNumber = this.getPositiveInteger(o);
      }

      o = rendererOptions.get("end-page");
      if (o != null) {
         this.endNumber = this.getPositiveInteger(o);
      }

      if (this.endNumber >= 0 && this.endNumber < this.endNumber) {
         this.endNumber = this.startNumber;
      }

   }

   protected int getPositiveInteger(Object o) {
      if (o instanceof Integer) {
         Integer i = (Integer)o;
         if (i < 1) {
            throw new IllegalArgumentException("Value must be a positive Integer");
         } else {
            return i;
         }
      } else if (o instanceof String) {
         return Integer.parseInt((String)o);
      } else {
         throw new IllegalArgumentException("Value must be a positive integer");
      }
   }

   public void stopRenderer() throws IOException {
      super.stopRenderer();
      if (this.endNumber == -1) {
         this.endNumber = this.getNumberOfPages();
      }

   }

   protected void rememberPage(PageViewport pageViewport) {
      if (this.pageFilter.isValid(pageViewport)) {
         super.rememberPage(pageViewport);
      }

   }

   public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
      try {
         if (pageIndex >= this.getNumberOfPages()) {
            return null;
         } else {
            PageFormat pageFormat = new PageFormat();
            Paper paper = new Paper();
            Rectangle2D dim = this.getPageViewport(pageIndex).getViewArea();
            double width = dim.getWidth();
            double height = dim.getHeight();
            if (width > height) {
               paper.setImageableArea(0.0, 0.0, height / 1000.0, width / 1000.0);
               paper.setSize(height / 1000.0, width / 1000.0);
               pageFormat.setOrientation(0);
            } else {
               paper.setImageableArea(0.0, 0.0, width / 1000.0, height / 1000.0);
               paper.setSize(width / 1000.0, height / 1000.0);
               pageFormat.setOrientation(1);
            }

            pageFormat.setPaper(paper);
            return pageFormat;
         }
      } catch (FOPException var9) {
         throw new IndexOutOfBoundsException(var9.getMessage());
      }
   }

   public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
      return this;
   }

   private class DefaultPageFilter implements PageFilter {
      private DefaultPageFilter() {
      }

      public boolean isValid(PageViewport page) {
         int pageNum = page.getPageIndex() + 1;

         assert pageNum >= 0;

         if (pageNum >= PageableRenderer.this.startNumber && (PageableRenderer.this.endNumber < 0 || pageNum <= PageableRenderer.this.endNumber)) {
            if (PageableRenderer.this.mode != PagesMode.ALL) {
               if (PageableRenderer.this.mode == PagesMode.EVEN && pageNum % 2 != 0) {
                  return false;
               }

               if (PageableRenderer.this.mode == PagesMode.ODD && pageNum % 2 == 0) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      DefaultPageFilter(Object x1) {
         this();
      }
   }

   private interface PageFilter {
      boolean isValid(PageViewport var1);
   }
}
