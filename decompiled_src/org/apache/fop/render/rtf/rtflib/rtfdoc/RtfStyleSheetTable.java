package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class RtfStyleSheetTable {
   private static int startIndex = 15;
   public static final int STATUS_OK = 0;
   public static final int STATUS_DEFAULT = 1;
   private static final String STANDARD_STYLE = "Standard";
   private static RtfStyleSheetTable instance = null;
   private Hashtable styles = null;
   private Hashtable attrTable = null;
   private Vector nameTable = null;
   private String defaultStyleName = "Standard";

   private RtfStyleSheetTable() {
      this.styles = new Hashtable();
      this.attrTable = new Hashtable();
      this.nameTable = new Vector();
   }

   public static RtfStyleSheetTable getInstance() {
      if (instance == null) {
         instance = new RtfStyleSheetTable();
      }

      return instance;
   }

   public void setDefaultStyle(String styleName) {
      this.defaultStyleName = styleName;
   }

   public String getDefaultStyleName() {
      if (this.attrTable.get(this.defaultStyleName) != null) {
         return this.defaultStyleName;
      } else if (this.attrTable.get("Standard") != null) {
         this.defaultStyleName = "Standard";
         return this.defaultStyleName;
      } else {
         return null;
      }
   }

   public void addStyle(String name, RtfAttributes attrs) {
      this.nameTable.addElement(name);
      if (attrs != null) {
         this.attrTable.put(name, attrs);
      }

      this.styles.put(name, new Integer(this.nameTable.size() - 1 + startIndex));
   }

   public int addStyleToAttributes(String name, RtfAttributes attr) {
      int status = 0;
      Integer style = (Integer)this.styles.get(name);
      if (style == null && !name.equals(this.defaultStyleName)) {
         name = this.defaultStyleName;
         style = (Integer)this.styles.get(name);
         status = 1;
      }

      if (style == null) {
         return status;
      } else {
         attr.set("cs", style);
         Object o = this.attrTable.get(name);
         if (o != null) {
            RtfAttributes rtfAttr = (RtfAttributes)o;
            Iterator names = rtfAttr.nameIterator();

            while(names.hasNext()) {
               String attrName = (String)names.next();
               if (!attr.isSet(attrName)) {
                  Integer i = (Integer)rtfAttr.getValue(attrName);
                  if (i == null) {
                     attr.set(attrName);
                  } else {
                     attr.set(attrName, i);
                  }
               }
            }
         }

         return status;
      }
   }

   public void writeStyleSheet(RtfHeader header) throws IOException {
      if (this.styles != null && this.styles.size() != 0) {
         header.writeGroupMark(true);
         header.writeControlWord("stylesheet");
         int number = this.nameTable.size();

         for(int i = 0; i < number; ++i) {
            String name = (String)this.nameTable.elementAt(i);
            header.writeGroupMark(true);
            header.writeControlWord("*\\" + this.getRtfStyleReference(name));
            Object o = this.attrTable.get(name);
            if (o != null) {
               header.writeAttributes((RtfAttributes)o, RtfText.ATTR_NAMES);
               header.writeAttributes((RtfAttributes)o, RtfText.ALIGNMENT);
            }

            header.write(name + ";");
            header.writeGroupMark(false);
         }

         header.writeGroupMark(false);
      }
   }

   private String getRtfStyleReference(String name) {
      return "cs" + this.styles.get(name).toString();
   }
}
