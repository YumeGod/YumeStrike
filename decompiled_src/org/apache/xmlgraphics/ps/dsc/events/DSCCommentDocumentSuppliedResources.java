package org.apache.xmlgraphics.ps.dsc.events;

import java.util.Collection;

public class DSCCommentDocumentSuppliedResources extends AbstractResourcesDSCComment {
   public DSCCommentDocumentSuppliedResources() {
   }

   public DSCCommentDocumentSuppliedResources(Collection resources) {
      super(resources);
   }

   public String getName() {
      return "DocumentSuppliedResources";
   }
}
