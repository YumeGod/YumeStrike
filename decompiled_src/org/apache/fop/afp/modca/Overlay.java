package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.Factory;

public class Overlay extends PageObject {
   public Overlay(Factory factory, String name, int width, int height, int rotation, int widthResolution, int heightResolution) {
      super(factory, name, width, height, rotation, widthResolution, heightResolution);
   }

   protected void writeStart(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-88, (byte)-33);
      os.write(data);
   }

   protected void writeContent(OutputStream os) throws IOException {
      super.writeContent(os);
      this.getActiveEnvironmentGroup().writeToStream(os);
      this.writeObjects(this.tagLogicalElements, os);
      this.writeObjects(this.objects, os);
   }

   protected void writeEnd(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-87, (byte)-33);
      os.write(data);
   }
}
