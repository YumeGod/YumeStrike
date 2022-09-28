package org.apache.fop.render.rtf;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.flow.ListBlock;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfAttributes;

public final class ListAttributesConverter {
   private ListAttributesConverter() {
   }

   static RtfAttributes convertAttributes(ListBlock fobj) throws FOPException {
      FOPRtfAttributes attrib = new FOPRtfAttributes();
      attrib.setTwips("levelindent", fobj.getCommonMarginBlock().startIndent);
      attrib.setTwips("li", fobj.getCommonMarginBlock().endIndent);
      attrib.set("list", "simple");
      attrib.set("levelfollow", 0);
      return attrib;
   }
}
