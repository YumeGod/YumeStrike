package org.apache.fop.area;

import java.util.List;
import org.apache.fop.fo.extensions.destination.Destination;

public class DestinationData extends AbstractOffDocumentItem implements Resolvable {
   private String idRef;
   private String[] idRefs;
   private PageViewport pageRef;

   public DestinationData(Destination destination) {
      this(destination.getInternalDestination());
   }

   public DestinationData(String idRef) {
      this.pageRef = null;
      this.idRef = idRef;
      this.idRefs = new String[]{idRef};
   }

   public String getIDRef() {
      return this.idRef;
   }

   public String[] getIDRefs() {
      return this.idRefs;
   }

   public PageViewport getPageViewport() {
      return this.pageRef;
   }

   public boolean isResolved() {
      return true;
   }

   public void resolveIDRef(String id, List pages) {
      this.pageRef = (PageViewport)pages.get(0);
   }

   public String getName() {
      return "Destination";
   }
}
