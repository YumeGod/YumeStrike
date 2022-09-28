package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.fop.afp.Streamable;

public class ResourceGroup extends AbstractNamedAFPObject {
   private final Set resourceSet = new HashSet();

   public ResourceGroup(String name) {
      super(name);
   }

   public void addObject(AbstractNamedAFPObject namedObject) throws IOException {
      this.resourceSet.add(namedObject);
   }

   public int getResourceCount() {
      return this.resourceSet.size();
   }

   public boolean resourceExists(String uri) {
      return this.resourceSet.contains(uri);
   }

   public void writeStart(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-88, (byte)-58);
      os.write(data);
   }

   public void writeContent(OutputStream os) throws IOException {
      Iterator it = this.resourceSet.iterator();

      while(it.hasNext()) {
         Object object = it.next();
         if (object instanceof Streamable) {
            Streamable streamableObject = (Streamable)object;
            streamableObject.writeToStream(os);
         }
      }

   }

   public void writeEnd(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-87, (byte)-58);
      os.write(data);
   }

   public String toString() {
      return this.name + " " + this.resourceSet;
   }
}
