package org.apache.fop.fo.properties;

import java.awt.Color;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;
import org.apache.fop.util.ColorUtil;

public final class ColorProperty extends Property {
   private static final PropertyCache cache;
   protected final Color color;

   public static ColorProperty getInstance(FOUserAgent foUserAgent, String value) throws PropertyException {
      ColorProperty instance = new ColorProperty(ColorUtil.parseColorString(foUserAgent, value));
      return (ColorProperty)cache.fetch((Property)instance);
   }

   private ColorProperty(Color value) {
      this.color = value;
   }

   public Color getColor(FOUserAgent foUserAgent) {
      return this.color;
   }

   public String toString() {
      return ColorUtil.colorToString(this.color);
   }

   public ColorProperty getColorProperty() {
      return this;
   }

   public Object getObject() {
      return this;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else {
         return o instanceof ColorProperty ? ((ColorProperty)o).color.equals(this.color) : false;
      }
   }

   public int hashCode() {
      return this.color.hashCode();
   }

   // $FF: synthetic method
   ColorProperty(Color x0, Object x1) {
      this(x0);
   }

   static {
      cache = new PropertyCache(ColorProperty.class);
   }

   public static class Maker extends PropertyMaker {
      public Maker(int propId) {
         super(propId);
      }

      public Property convertProperty(Property p, PropertyList propertyList, FObj fo) throws PropertyException {
         if (p instanceof ColorProperty) {
            return p;
         } else {
            FObj fobj = fo == null ? propertyList.getFObj() : fo;
            FOUserAgent ua = fobj == null ? null : fobj.getUserAgent();
            Color val = p.getColor(ua);
            return (Property)(val != null ? new ColorProperty(val) : this.convertPropertyDatatype(p, propertyList, fo));
         }
      }
   }
}
