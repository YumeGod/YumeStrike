package org.apache.fop.layoutmgr.inline;

import org.apache.fop.area.LinkResolver;
import org.apache.fop.area.Trait;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.datatypes.URISpecification;
import org.apache.fop.fo.flow.BasicLink;
import org.apache.fop.layoutmgr.PageSequenceLayoutManager;
import org.apache.fop.layoutmgr.TraitSetter;

public class BasicLinkLayoutManager extends InlineLayoutManager {
   public BasicLinkLayoutManager(BasicLink node) {
      super(node);
   }

   protected InlineArea createArea(boolean bInlineParent) {
      InlineArea area = super.createArea(bInlineParent);
      this.setupBasicLinkArea(area);
      return area;
   }

   private void setupBasicLinkArea(InlineArea area) {
      BasicLink fobj = (BasicLink)this.fobj;
      TraitSetter.addPtr(area, fobj.getPtr());
      String url;
      if (fobj.hasInternalDestination()) {
         url = fobj.getInternalDestination();
         PageSequenceLayoutManager pslm = this.getPSLM();
         LinkResolver res = new LinkResolver(url, area);
         res.resolveIDRef(url, pslm.getFirstPVWithID(url));
         if (!res.isResolved()) {
            pslm.addUnresolvedArea(url, res);
         }
      } else if (fobj.hasExternalDestination()) {
         url = URISpecification.getURL(fobj.getExternalDestination());
         boolean newWindow = fobj.getShowDestination() == 190;
         if (url.length() > 0) {
            area.addTrait(Trait.EXTERNAL_LINK, new Trait.ExternalLink(url, newWindow));
         }
      }

   }
}
