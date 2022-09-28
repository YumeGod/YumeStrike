package org.apache.fop.area.inline;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import org.apache.fop.area.Area;

public class Viewport extends InlineArea {
   private Area content;
   private boolean clip = false;
   private Rectangle2D contentPosition;

   public Viewport(Area child) {
      this.content = child;
   }

   public void setClip(boolean c) {
      this.clip = c;
   }

   public boolean getClip() {
      return this.clip;
   }

   public void setContentPosition(Rectangle2D cp) {
      this.contentPosition = cp;
   }

   public Rectangle2D getContentPosition() {
      return this.contentPosition;
   }

   public void setContent(Area content) {
      this.content = content;
   }

   public Area getContent() {
      return this.content;
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.writeBoolean(this.contentPosition != null);
      if (this.contentPosition != null) {
         out.writeFloat((float)this.contentPosition.getX());
         out.writeFloat((float)this.contentPosition.getY());
         out.writeFloat((float)this.contentPosition.getWidth());
         out.writeFloat((float)this.contentPosition.getHeight());
      }

      out.writeBoolean(this.clip);
      out.writeObject(this.props);
      out.writeObject(this.content);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      if (in.readBoolean()) {
         this.contentPosition = new Rectangle2D.Float(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat());
      }

      this.clip = in.readBoolean();
      this.props = (HashMap)in.readObject();
      this.content = (Area)in.readObject();
   }
}
