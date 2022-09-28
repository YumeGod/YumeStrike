package org.apache.xmlgraphics.ps.dsc.events;

import org.apache.xmlgraphics.ps.PSResource;

public class DSCCommentIncludeResource extends AbstractResourceDSCComment {
   public DSCCommentIncludeResource() {
   }

   public DSCCommentIncludeResource(PSResource resource) {
      super(resource);
   }

   public String getName() {
      return "IncludeResource";
   }
}
