package org.apache.fop.fonts;

import java.io.Serializable;

public class FontTriplet implements Comparable, Serializable {
   private static final long serialVersionUID = 1168991106658033508L;
   private String name;
   private String style;
   private int weight;
   private int priority;
   private transient String key;

   public FontTriplet(String name) {
      this.name = name;
   }

   public FontTriplet(String name, String style, int weight) {
      this(name, style, weight, 0);
   }

   public FontTriplet(String name, String style, int weight, int priority) {
      this(name);
      this.style = style;
      this.weight = weight;
      this.priority = priority;
   }

   public String getName() {
      return this.name;
   }

   public String getStyle() {
      return this.style;
   }

   public int getWeight() {
      return this.weight;
   }

   public int getPriority() {
      return this.priority;
   }

   private String getKey() {
      if (this.key == null) {
         this.key = this.getName() + "," + this.getStyle() + "," + this.getWeight();
      }

      return this.key;
   }

   public int compareTo(Object o) {
      return this.getKey().compareTo(((FontTriplet)o).getKey());
   }

   public int hashCode() {
      return this.toString().hashCode();
   }

   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else if (obj == this) {
         return true;
      } else if (!(obj instanceof FontTriplet)) {
         return false;
      } else {
         FontTriplet other = (FontTriplet)obj;
         return this.getName().equals(other.getName()) && this.getStyle().equals(other.getStyle()) && this.getWeight() == other.getWeight();
      }
   }

   public String toString() {
      return this.getKey();
   }

   public interface Matcher {
      boolean matches(FontTriplet var1);
   }
}
