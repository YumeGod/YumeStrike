package org.apache.batik.ext.awt.image.spi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractRegistryEntry implements RegistryEntry, ErrorConstants {
   String name;
   float priority;
   List exts;
   List mimeTypes;

   public AbstractRegistryEntry(String var1, float var2, String[] var3, String[] var4) {
      this.name = var1;
      this.priority = var2;
      this.exts = new ArrayList(var3.length);

      int var5;
      for(var5 = 0; var5 < var3.length; ++var5) {
         this.exts.add(var3[var5]);
      }

      this.exts = Collections.unmodifiableList(this.exts);
      this.mimeTypes = new ArrayList(var4.length);

      for(var5 = 0; var5 < var4.length; ++var5) {
         this.mimeTypes.add(var4[var5]);
      }

      this.mimeTypes = Collections.unmodifiableList(this.mimeTypes);
   }

   public AbstractRegistryEntry(String var1, float var2, String var3, String var4) {
      this.name = var1;
      this.priority = var2;
      this.exts = new ArrayList(1);
      this.exts.add(var3);
      this.exts = Collections.unmodifiableList(this.exts);
      this.mimeTypes = new ArrayList(1);
      this.mimeTypes.add(var4);
      this.mimeTypes = Collections.unmodifiableList(this.mimeTypes);
   }

   public String getFormatName() {
      return this.name;
   }

   public List getStandardExtensions() {
      return this.exts;
   }

   public List getMimeTypes() {
      return this.mimeTypes;
   }

   public float getPriority() {
      return this.priority;
   }
}
