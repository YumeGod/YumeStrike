package org.apache.fop.area;

public class BlockViewport extends Block {
   private boolean clip;
   private CTM viewportCTM;

   public BlockViewport() {
      this(false);
   }

   public BlockViewport(boolean allowBPDUpdate) {
      this.clip = false;
      this.allowBPDUpdate = allowBPDUpdate;
   }

   public void setCTM(CTM ctm) {
      this.viewportCTM = ctm;
   }

   public CTM getCTM() {
      return this.viewportCTM;
   }

   public void setClip(boolean cl) {
      this.clip = cl;
   }

   public boolean getClip() {
      return this.clip;
   }
}
