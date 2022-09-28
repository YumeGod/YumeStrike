package org.apache.fop.layoutmgr.inline;

import org.apache.fop.area.PageViewport;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.area.inline.TextArea;
import org.apache.fop.area.inline.UnresolvedPageNumber;
import org.apache.fop.fo.flow.PageNumberCitation;
import org.apache.fop.layoutmgr.LayoutContext;

public class PageNumberCitationLayoutManager extends AbstractPageNumberCitationLayoutManager {
   public PageNumberCitationLayoutManager(PageNumberCitation node) {
      super(node);
   }

   public InlineArea get(LayoutContext context) {
      this.curArea = this.getPageNumberCitationInlineArea();
      return this.curArea;
   }

   private InlineArea getPageNumberCitationInlineArea() {
      PageViewport page = this.getPSLM().getFirstPVWithID(this.fobj.getRefId());
      TextArea text = null;
      String str;
      int width;
      if (page != null) {
         str = page.getPageNumberString();
         text = new TextArea();
         width = this.getStringWidth(str);
         ((TextArea)text).addWord(str, 0);
         ((TextArea)text).setIPD(width);
         this.resolved = true;
      } else {
         this.resolved = false;
         text = new UnresolvedPageNumber(this.fobj.getRefId(), this.font);
         str = "MMM";
         width = this.getStringWidth(str);
         ((TextArea)text).setIPD(width);
      }

      this.updateTextAreaTraits((TextArea)text);
      return (InlineArea)text;
   }
}
