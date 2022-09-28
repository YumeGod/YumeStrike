package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEnvironmentGroup extends AbstractNamedAFPObject {
   protected final List mapDataResources = null;
   protected List mapPageOverlays = null;

   public AbstractEnvironmentGroup(String name) {
      super(name);
   }

   private List getMapPageOverlays() {
      if (this.mapPageOverlays == null) {
         this.mapPageOverlays = new ArrayList();
      }

      return this.mapPageOverlays;
   }

   public void createOverlay(String name) {
      MapPageOverlay mpo = this.getCurrentMapPageOverlay();
      if (mpo == null) {
         mpo = new MapPageOverlay();
         this.getMapPageOverlays().add(mpo);
      }

      try {
         mpo.addOverlay(name);
      } catch (MaximumSizeExceededException var6) {
         mpo = new MapPageOverlay();
         this.getMapPageOverlays().add(mpo);

         try {
            mpo.addOverlay(name);
         } catch (MaximumSizeExceededException var5) {
            log.error("createOverlay():: resulted in a MaximumSizeExceededException");
         }
      }

   }

   private MapPageOverlay getCurrentMapPageOverlay() {
      return (MapPageOverlay)this.getLastElement(this.mapPageOverlays);
   }

   protected Object getLastElement(List list) {
      return list != null && list.size() > 0 ? list.get(list.size() - 1) : null;
   }

   protected void writeContent(OutputStream os) throws IOException {
      super.writeContent(os);
   }
}
