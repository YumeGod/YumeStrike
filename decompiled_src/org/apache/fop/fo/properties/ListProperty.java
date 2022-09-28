package org.apache.fop.fo.properties;

import java.util.List;
import java.util.Vector;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;

public class ListProperty extends Property {
   protected List list;

   protected ListProperty() {
      this.list = new Vector();
   }

   public ListProperty(Property prop) {
      this();
      this.addProperty(prop);
   }

   public void addProperty(Property prop) {
      this.list.add(prop);
   }

   public List getList() {
      return this.list;
   }

   public Object getObject() {
      return this.list;
   }

   public static class Maker extends PropertyMaker {
      public Maker(int propId) {
         super(propId);
      }

      public Property convertProperty(Property p, PropertyList propertyList, FObj fo) {
         return (Property)(p instanceof ListProperty ? p : new ListProperty(p));
      }
   }
}
