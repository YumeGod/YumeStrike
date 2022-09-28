package org.apache.fop.fo.properties;

import java.util.Iterator;
import java.util.List;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.NCnameProperty;
import org.apache.fop.fo.expr.PropertyException;

public class TextDecorationProperty extends ListProperty {
   public TextDecorationProperty(ListProperty listProp) throws PropertyException {
      List lst = listProp.getList();
      boolean none = false;
      boolean under = false;
      boolean over = false;
      boolean through = false;
      boolean blink = false;

      Property prop;
      for(Iterator i = lst.iterator(); i.hasNext(); this.addProperty(prop)) {
         prop = (Property)i.next();
         switch (prop.getEnum()) {
            case 17:
            case 86:
               if (none) {
                  throw new PropertyException("'none' specified, no additional values allowed");
               }

               if (blink) {
                  throw new PropertyException("Invalid combination of values");
               }

               blink = true;
               break;
            case 77:
            case 90:
               if (none) {
                  throw new PropertyException("'none' specified, no additional values allowed");
               }

               if (through) {
                  throw new PropertyException("Invalid combination of values");
               }

               through = true;
               break;
            case 91:
            case 103:
               if (none) {
                  throw new PropertyException("'none' specified, no additional values allowed");
               }

               if (over) {
                  throw new PropertyException("Invalid combination of values");
               }

               over = true;
               break;
            case 92:
            case 153:
               if (none) {
                  throw new PropertyException("'none' specified, no additional values allowed");
               }

               if (under) {
                  throw new PropertyException("Invalid combination of values");
               }

               under = true;
               break;
            case 95:
               if (under | over | through | blink) {
                  throw new PropertyException("Invalid combination of values");
               }

               none = true;
               break;
            default:
               throw new PropertyException("Invalid value specified: " + prop);
         }
      }

   }

   public static class Maker extends PropertyMaker {
      public Maker(int propId) {
         super(propId);
      }

      public Property convertProperty(Property p, PropertyList propertyList, FObj fo) throws PropertyException {
         if (p instanceof TextDecorationProperty) {
            return p;
         } else {
            ListProperty lst;
            if (p instanceof ListProperty) {
               lst = (ListProperty)p;
               this.checkEnums(lst);
               return new TextDecorationProperty((ListProperty)p);
            } else if (p instanceof EnumProperty) {
               lst = new ListProperty(p);
               return new TextDecorationProperty(lst);
            } else {
               throw new PropertyException("Cannot convert anything other than a list property, got a " + p.getClass().getName());
            }
         }
      }

      private ListProperty checkEnums(ListProperty lst) throws PropertyException {
         List l = lst.getList();

         for(int i = 0; i < l.size(); ++i) {
            Property prop = (Property)l.get(i);
            if (!(prop instanceof EnumProperty)) {
               if (!(prop instanceof NCnameProperty)) {
                  throw new PropertyException("Invalid content for text-decoration property: " + prop);
               }

               Property prop_enum = this.checkEnumValues(((NCnameProperty)prop).getString());
               if (prop_enum == null) {
                  throw new PropertyException("Illegal enum value: " + prop.getString());
               }

               l.set(i, prop_enum);
            }
         }

         return lst;
      }
   }
}
