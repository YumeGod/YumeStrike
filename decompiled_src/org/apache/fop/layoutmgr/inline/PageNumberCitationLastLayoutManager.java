package org.apache.fop.layoutmgr.inline;

import org.apache.fop.area.PageViewport;
import org.apache.fop.area.Resolvable;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.area.inline.TextArea;
import org.apache.fop.area.inline.UnresolvedPageNumber;
import org.apache.fop.fo.flow.PageNumberCitationLast;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.LayoutManager;

public class PageNumberCitationLastLayoutManager extends AbstractPageNumberCitationLayoutManager {
   public PageNumberCitationLastLayoutManager(PageNumberCitationLast node) {
      super(node);
      this.fobj = node;
   }

   public InlineArea get(LayoutContext context) {
      this.curArea = this.getPageNumberCitationLastInlineArea(this.parentLayoutManager);
      return this.curArea;
   }

   private InlineArea getPageNumberCitationLastInlineArea(LayoutManager parentLM) {
      TextArea text = null;
      this.resolved = false;
      if (!this.getPSLM().associateLayoutManagerID(this.fobj.getRefId())) {
         text = new UnresolvedPageNumber(this.fobj.getRefId(), this.font, false);
         this.getPSLM().addUnresolvedArea(this.fobj.getRefId(), (Resolvable)text);
         String str = "MMM";
         int width = this.getStringWidth(str);
         ((TextArea)text).setIPD(width);
      } else {
         PageViewport page = this.getPSLM().getLastPVWithID(this.fobj.getRefId());
         String str = page.getPageNumberString();
         text = new TextArea();
         int width = this.getStringWidth(str);
         ((TextArea)text).addWord(str, 0);
         ((TextArea)text).setIPD(width);
         this.resolved = true;
      }

      this.updateTextAreaTraits((TextArea)text);
      return (InlineArea)text;
   }
}
