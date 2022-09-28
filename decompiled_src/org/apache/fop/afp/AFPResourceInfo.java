package org.apache.fop.afp;

import java.awt.Dimension;

public class AFPResourceInfo {
   public static final AFPResourceLevel DEFAULT_LEVEL = new AFPResourceLevel(4);
   private String uri = null;
   private Dimension imageDimension = null;
   private String name = null;
   private AFPResourceLevel level;
   private boolean levelChanged;

   public AFPResourceInfo() {
      this.level = DEFAULT_LEVEL;
      this.levelChanged = false;
   }

   public void setUri(String uri) {
      this.uri = uri;
   }

   public String getUri() {
      return this.uri;
   }

   public void setImageDimension(Dimension dim) {
      this.imageDimension = dim;
   }

   public Dimension getImageDimension() {
      return this.imageDimension;
   }

   public void setName(String resourceName) {
      this.name = resourceName;
   }

   public String getName() {
      return this.name;
   }

   public AFPResourceLevel getLevel() {
      return this.level == null ? DEFAULT_LEVEL : this.level;
   }

   public void setLevel(AFPResourceLevel resourceLevel) {
      this.level = resourceLevel;
      this.levelChanged = true;
   }

   public boolean levelChanged() {
      return this.levelChanged;
   }

   public String toString() {
      return "AFPResourceInfo{uri=" + this.uri + (this.imageDimension != null ? ", " + this.imageDimension.width + "x" + this.imageDimension.height : "") + (this.name != null ? ", name=" + this.name : "") + (this.level != null ? ", level=" + this.level : "") + "}";
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && obj instanceof AFPResourceInfo) {
         AFPResourceInfo ri = (AFPResourceInfo)obj;
         return (this.uri == ri.uri || this.uri != null && this.uri.equals(ri.uri)) && (this.imageDimension == ri.imageDimension || this.imageDimension != null && this.imageDimension.equals(ri.imageDimension)) && (this.name == ri.name || this.name != null && this.name.equals(ri.name)) && (this.level == ri.level || this.level != null && this.level.equals(ri.level));
      } else {
         return false;
      }
   }

   public int hashCode() {
      int hash = 7;
      hash = 31 * hash + (null == this.uri ? 0 : this.uri.hashCode());
      hash = 31 * hash + (null == this.imageDimension ? 0 : this.imageDimension.hashCode());
      hash = 31 * hash + (null == this.name ? 0 : this.name.hashCode());
      hash = 31 * hash + (null == this.level ? 0 : this.level.hashCode());
      return hash;
   }
}
