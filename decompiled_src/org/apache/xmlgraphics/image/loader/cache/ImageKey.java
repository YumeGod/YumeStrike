package org.apache.xmlgraphics.image.loader.cache;

import org.apache.xmlgraphics.image.loader.ImageFlavor;

public class ImageKey {
   private String uri;
   private ImageFlavor flavor;

   public ImageKey(String uri, ImageFlavor flavor) {
      if (uri == null) {
         throw new NullPointerException("URI must not be null");
      } else if (flavor == null) {
         throw new NullPointerException("flavor must not be null");
      } else {
         this.uri = uri;
         this.flavor = flavor;
      }
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      result = 31 * result + (this.flavor == null ? 0 : this.flavor.hashCode());
      result = 31 * result + (this.uri == null ? 0 : this.uri.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         ImageKey other = (ImageKey)obj;
         if (!this.uri.equals(other.uri)) {
            return false;
         } else {
            return this.flavor.equals(other.flavor);
         }
      }
   }

   public String toString() {
      return this.uri + " (" + this.flavor + ")";
   }
}
