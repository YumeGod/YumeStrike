package org.apache.xmlgraphics.ps.dsc.events;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.PSProcSet;
import org.apache.xmlgraphics.ps.PSResource;

public abstract class AbstractResourceDSCComment extends AbstractDSCComment {
   private PSResource resource;

   public AbstractResourceDSCComment() {
   }

   public AbstractResourceDSCComment(PSResource resource) {
      this.resource = resource;
   }

   public PSResource getResource() {
      return this.resource;
   }

   public boolean hasValues() {
      return true;
   }

   public void parseValue(String value) {
      List params = this.splitParams(value);
      Iterator iter = params.iterator();
      String name = (String)iter.next();
      String encodingname;
      if ("font".equals(name)) {
         encodingname = (String)iter.next();
         this.resource = new PSResource(name, encodingname);
      } else if ("procset".equals(name)) {
         encodingname = (String)iter.next();
         String version = (String)iter.next();
         String revision = (String)iter.next();
         this.resource = new PSProcSet(encodingname, Float.parseFloat(version), Integer.parseInt(revision));
      } else if ("file".equals(name)) {
         encodingname = (String)iter.next();
         this.resource = new PSResource(name, encodingname);
      } else if ("form".equals(name)) {
         encodingname = (String)iter.next();
         this.resource = new PSResource(name, encodingname);
      } else if ("pattern".equals(name)) {
         encodingname = (String)iter.next();
         this.resource = new PSResource(name, encodingname);
      } else {
         if (!"encoding".equals(name)) {
            throw new IllegalArgumentException("Invalid resource type: " + name);
         }

         encodingname = (String)iter.next();
         this.resource = new PSResource(name, encodingname);
      }

   }

   public void generate(PSGenerator gen) throws IOException {
      gen.writeDSCComment(this.getName(), (Object)this.getResource());
   }
}
