package org.apache.xmlgraphics.ps.dsc.events;

import java.util.Collection;

public class DSCCommentDocumentNeededResources extends AbstractResourcesDSCComment {
   public DSCCommentDocumentNeededResources() {
   }

   public DSCCommentDocumentNeededResources(Collection resources) {
      super(resources);
   }

   public String getName() {
      return "DocumentNeededResources";
   }
}
