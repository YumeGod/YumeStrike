package org.apache.fop.area;

import java.io.Serializable;
import java.util.List;

public class LinkResolver implements Resolvable, Serializable {
   private boolean resolved = false;
   private String idRef;
   private Area area;

   public LinkResolver(String id, Area a) {
      this.idRef = id;
      this.area = a;
   }

   public boolean isResolved() {
      return this.resolved;
   }

   public String[] getIDRefs() {
      return new String[]{this.idRef};
   }

   public void resolveIDRef(String id, List pages) {
      this.resolveIDRef(id, (PageViewport)pages.get(0));
   }

   public void resolveIDRef(String id, PageViewport pv) {
      if (this.idRef.equals(id) && pv != null) {
         this.resolved = true;
         Trait.InternalLink iLink = new Trait.InternalLink(pv.getKey(), this.idRef);
         this.area.addTrait(Trait.INTERNAL_LINK, iLink);
      }

   }
}
