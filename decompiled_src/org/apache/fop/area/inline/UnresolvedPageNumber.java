package org.apache.fop.area.inline;

import java.util.List;
import org.apache.fop.area.PageViewport;
import org.apache.fop.area.Resolvable;
import org.apache.fop.fonts.Font;

public class UnresolvedPageNumber extends TextArea implements Resolvable {
   private boolean resolved;
   private String pageIDRef;
   private String text;
   private boolean pageType;
   public static final boolean FIRST = true;
   public static final boolean LAST = false;
   private transient Font font;

   public UnresolvedPageNumber(String id, Font f) {
      this(id, f, true);
   }

   public UnresolvedPageNumber(String id, Font f, boolean type) {
      this.resolved = false;
      this.pageIDRef = id;
      this.font = f;
      this.text = "?";
      this.pageType = type;
   }

   public String[] getIDRefs() {
      return new String[]{this.pageIDRef};
   }

   public void resolveIDRef(String id, List pages) {
      if (!this.resolved && this.pageIDRef.equals(id) && pages != null) {
         if (log.isDebugEnabled()) {
            log.debug("Resolving pageNumber: " + id);
         }

         this.resolved = true;
         PageViewport page;
         if (this.pageType) {
            page = (PageViewport)pages.get(0);
         } else {
            page = (PageViewport)pages.get(pages.size() - 1);
         }

         this.removeText();
         this.text = page.getPageNumberString();
         this.addWord(this.text, 0);
         if (this.font != null) {
            this.handleIPDVariation(this.font.getWordWidth(this.text) - this.getIPD());
            this.font = null;
         } else {
            log.warn("Cannot update the IPD of an unresolved page number. No font information available.");
         }
      }

   }

   public boolean isResolved() {
      return this.resolved;
   }

   public boolean applyVariationFactor(double variationFactor, int lineStretch, int lineShrink) {
      return true;
   }
}
