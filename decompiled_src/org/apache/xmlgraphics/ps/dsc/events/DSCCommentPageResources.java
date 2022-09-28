package org.apache.xmlgraphics.ps.dsc.events;

import java.util.Collection;

public class DSCCommentPageResources extends AbstractResourcesDSCComment {
   public DSCCommentPageResources() {
   }

   public DSCCommentPageResources(Collection resources) {
      super(resources);
   }

   public String getName() {
      return "PageResources";
   }
}
