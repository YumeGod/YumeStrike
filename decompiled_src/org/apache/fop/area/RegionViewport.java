package org.apache.fop.area;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class RegionViewport extends Area implements Cloneable {
   private RegionReference regionReference;
   private Rectangle2D viewArea;
   private boolean clip = false;

   public RegionViewport(Rectangle2D viewArea) {
      this.viewArea = viewArea;
      this.addTrait(Trait.IS_VIEWPORT_AREA, Boolean.TRUE);
   }

   public void setRegionReference(RegionReference reg) {
      this.regionReference = reg;
   }

   public RegionReference getRegionReference() {
      return this.regionReference;
   }

   public void setClip(boolean c) {
      this.clip = c;
   }

   public boolean isClip() {
      return this.clip;
   }

   public Rectangle2D getViewArea() {
      return this.viewArea;
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.writeFloat((float)this.viewArea.getX());
      out.writeFloat((float)this.viewArea.getY());
      out.writeFloat((float)this.viewArea.getWidth());
      out.writeFloat((float)this.viewArea.getHeight());
      out.writeBoolean(this.clip);
      out.writeObject(this.props);
      out.writeObject(this.regionReference);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      this.viewArea = new Rectangle2D.Float(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat());
      this.clip = in.readBoolean();
      this.props = (HashMap)in.readObject();
      this.setRegionReference((RegionReference)in.readObject());
   }

   public Object clone() {
      RegionViewport rv = new RegionViewport((Rectangle2D)this.viewArea.clone());
      rv.regionReference = (RegionReference)this.regionReference.clone();
      if (this.props != null) {
         rv.props = new HashMap(this.props);
      }

      if (this.foreignAttributes != null) {
         rv.foreignAttributes = new HashMap(this.foreignAttributes);
      }

      return rv;
   }
}
