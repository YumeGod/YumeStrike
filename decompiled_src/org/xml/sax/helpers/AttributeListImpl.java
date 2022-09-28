package org.xml.sax.helpers;

import java.util.Vector;
import org.xml.sax.AttributeList;

/** @deprecated */
public class AttributeListImpl implements AttributeList {
   Vector names = new Vector();
   Vector types = new Vector();
   Vector values = new Vector();

   public AttributeListImpl() {
   }

   public AttributeListImpl(AttributeList var1) {
      this.setAttributeList(var1);
   }

   public void setAttributeList(AttributeList var1) {
      int var2 = var1.getLength();
      this.clear();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.addAttribute(var1.getName(var3), var1.getType(var3), var1.getValue(var3));
      }

   }

   public void addAttribute(String var1, String var2, String var3) {
      this.names.addElement(var1);
      this.types.addElement(var2);
      this.values.addElement(var3);
   }

   public void removeAttribute(String var1) {
      int var2 = this.names.indexOf(var1);
      if (var2 >= 0) {
         this.names.removeElementAt(var2);
         this.types.removeElementAt(var2);
         this.values.removeElementAt(var2);
      }

   }

   public void clear() {
      this.names.removeAllElements();
      this.types.removeAllElements();
      this.values.removeAllElements();
   }

   public int getLength() {
      return this.names.size();
   }

   public String getName(int var1) {
      if (var1 < 0) {
         return null;
      } else {
         try {
            return (String)this.names.elementAt(var1);
         } catch (ArrayIndexOutOfBoundsException var3) {
            return null;
         }
      }
   }

   public String getType(int var1) {
      if (var1 < 0) {
         return null;
      } else {
         try {
            return (String)this.types.elementAt(var1);
         } catch (ArrayIndexOutOfBoundsException var3) {
            return null;
         }
      }
   }

   public String getValue(int var1) {
      if (var1 < 0) {
         return null;
      } else {
         try {
            return (String)this.values.elementAt(var1);
         } catch (ArrayIndexOutOfBoundsException var3) {
            return null;
         }
      }
   }

   public String getType(String var1) {
      return this.getType(this.names.indexOf(var1));
   }

   public String getValue(String var1) {
      return this.getValue(this.names.indexOf(var1));
   }
}
