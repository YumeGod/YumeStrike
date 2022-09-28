package org.apache.xmlgraphics.ps.dsc.events;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.PSProcSet;
import org.apache.xmlgraphics.ps.PSResource;

public abstract class AbstractResourcesDSCComment extends AbstractDSCComment {
   private Set resources;
   protected static final Set RESOURCE_TYPES = new HashSet();

   public AbstractResourcesDSCComment() {
   }

   public AbstractResourcesDSCComment(Collection resources) {
      this.addResources(resources);
   }

   public boolean hasValues() {
      return true;
   }

   private void prepareResourceSet() {
      if (this.resources == null) {
         this.resources = new TreeSet();
      }

   }

   public void addResource(PSResource res) {
      this.prepareResourceSet();
      this.resources.add(res);
   }

   public void addResources(Collection resources) {
      if (resources != null) {
         this.prepareResourceSet();
         this.resources.addAll(resources);
      }

   }

   public Set getResources() {
      return Collections.unmodifiableSet(this.resources);
   }

   public void parseValue(String value) {
      List params = this.splitParams(value);
      String currentResourceType = null;
      Iterator iter = params.iterator();

      while(iter.hasNext()) {
         String name = (String)iter.next();
         if (RESOURCE_TYPES.contains(name)) {
            currentResourceType = name;
         }

         if (currentResourceType == null) {
            throw new IllegalArgumentException("<resources> must begin with a resource type. Found: " + name);
         }

         String filename;
         if ("font".equals(currentResourceType)) {
            filename = (String)iter.next();
            this.addResource(new PSResource(name, filename));
         } else if ("form".equals(currentResourceType)) {
            filename = (String)iter.next();
            this.addResource(new PSResource(name, filename));
         } else if ("procset".equals(currentResourceType)) {
            filename = (String)iter.next();
            String version = (String)iter.next();
            String revision = (String)iter.next();
            this.addResource(new PSProcSet(filename, Float.parseFloat(version), Integer.parseInt(revision)));
         } else {
            if (!"file".equals(currentResourceType)) {
               throw new IllegalArgumentException("Invalid resource type: " + currentResourceType);
            }

            filename = (String)iter.next();
            this.addResource(new PSResource(name, filename));
         }
      }

   }

   public void generate(PSGenerator gen) throws IOException {
      if (this.resources != null && this.resources.size() != 0) {
         StringBuffer sb = new StringBuffer();
         sb.append("%%").append(this.getName()).append(": ");
         boolean first = true;

         for(Iterator i = this.resources.iterator(); i.hasNext(); first = false) {
            if (!first) {
               gen.writeln(sb.toString());
               sb.setLength(0);
               sb.append("%%+ ");
            }

            PSResource res = (PSResource)i.next();
            sb.append(res.getResourceSpecification());
         }

         gen.writeln(sb.toString());
      }
   }

   static {
      RESOURCE_TYPES.add("font");
      RESOURCE_TYPES.add("procset");
      RESOURCE_TYPES.add("file");
      RESOURCE_TYPES.add("pattern");
      RESOURCE_TYPES.add("form");
      RESOURCE_TYPES.add("encoding");
   }
}
