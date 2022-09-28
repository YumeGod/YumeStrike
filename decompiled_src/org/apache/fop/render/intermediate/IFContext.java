package org.apache.fop.render.intermediate;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import org.apache.fop.apps.FOUserAgent;
import org.apache.xmlgraphics.util.QName;

public class IFContext {
   private FOUserAgent userAgent;
   private Map foreignAttributes;
   private Locale language;
   private String structurePointer;

   public IFContext(FOUserAgent ua) {
      this.foreignAttributes = Collections.EMPTY_MAP;
      this.setUserAgent(ua);
   }

   public void setUserAgent(FOUserAgent ua) {
      if (this.userAgent != null) {
         throw new IllegalStateException("The user agent was already set");
      } else {
         this.userAgent = ua;
      }
   }

   public FOUserAgent getUserAgent() {
      return this.userAgent;
   }

   public Map getForeignAttributes() {
      return this.foreignAttributes;
   }

   public Object getForeignAttribute(QName qName) {
      return this.foreignAttributes.get(qName);
   }

   public void setForeignAttributes(Map foreignAttributes) {
      if (foreignAttributes != null) {
         this.foreignAttributes = foreignAttributes;
      } else {
         this.foreignAttributes = Collections.EMPTY_MAP;
      }

   }

   public void resetForeignAttributes() {
      this.setForeignAttributes((Map)null);
   }

   public void setLanguage(Locale lang) {
      this.language = lang;
   }

   public Locale getLanguage() {
      return this.language;
   }

   public void setStructurePointer(String ptr) {
      this.structurePointer = ptr;
   }

   public void resetStructurePointer() {
      this.setStructurePointer((String)null);
   }

   public String getStructurePointer() {
      return this.structurePointer;
   }
}
