package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PageSegment extends AbstractNamedAFPObject {
   private List objects = null;

   public PageSegment(String name) {
      super(name);
   }

   public List getObjects() {
      if (this.objects == null) {
         this.objects = new ArrayList();
      }

      return this.objects;
   }

   public void addObject(AbstractAFPObject object) {
      this.getObjects().add(object);
   }

   protected void writeStart(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-88, (byte)95);
      os.write(data);
   }

   protected void writeContent(OutputStream os) throws IOException {
      super.writeContent(os);
      this.writeObjects(this.objects, os);
   }

   protected void writeEnd(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-87, (byte)95);
      os.write(data);
   }

   public String toString() {
      return this.name;
   }
}
