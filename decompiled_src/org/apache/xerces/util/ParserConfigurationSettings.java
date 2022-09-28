package org.apache.xerces.util;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;

public class ParserConfigurationSettings implements XMLComponentManager {
   protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
   protected ArrayList fRecognizedProperties;
   protected HashMap fProperties;
   protected ArrayList fRecognizedFeatures;
   protected HashMap fFeatures;
   protected XMLComponentManager fParentSettings;

   public ParserConfigurationSettings() {
      this((XMLComponentManager)null);
   }

   public ParserConfigurationSettings(XMLComponentManager var1) {
      this.fRecognizedFeatures = new ArrayList();
      this.fRecognizedProperties = new ArrayList();
      this.fFeatures = new HashMap();
      this.fProperties = new HashMap();
      this.fParentSettings = var1;
   }

   public void addRecognizedFeatures(String[] var1) {
      int var2 = var1 != null ? var1.length : 0;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         if (!this.fRecognizedFeatures.contains(var4)) {
            this.fRecognizedFeatures.add(var4);
         }
      }

   }

   public void setFeature(String var1, boolean var2) throws XMLConfigurationException {
      this.checkFeature(var1);
      this.fFeatures.put(var1, var2 ? Boolean.TRUE : Boolean.FALSE);
   }

   public void addRecognizedProperties(String[] var1) {
      int var2 = var1 != null ? var1.length : 0;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         if (!this.fRecognizedProperties.contains(var4)) {
            this.fRecognizedProperties.add(var4);
         }
      }

   }

   public void setProperty(String var1, Object var2) throws XMLConfigurationException {
      this.checkProperty(var1);
      this.fProperties.put(var1, var2);
   }

   public boolean getFeature(String var1) throws XMLConfigurationException {
      Boolean var2 = (Boolean)this.fFeatures.get(var1);
      if (var2 == null) {
         this.checkFeature(var1);
         return false;
      } else {
         return var2;
      }
   }

   public Object getProperty(String var1) throws XMLConfigurationException {
      Object var2 = this.fProperties.get(var1);
      if (var2 == null) {
         this.checkProperty(var1);
      }

      return var2;
   }

   protected void checkFeature(String var1) throws XMLConfigurationException {
      if (!this.fRecognizedFeatures.contains(var1)) {
         if (this.fParentSettings == null) {
            byte var2 = 0;
            throw new XMLConfigurationException(var2, var1);
         }

         this.fParentSettings.getFeature(var1);
      }

   }

   protected void checkProperty(String var1) throws XMLConfigurationException {
      if (!this.fRecognizedProperties.contains(var1)) {
         if (this.fParentSettings == null) {
            byte var2 = 0;
            throw new XMLConfigurationException(var2, var1);
         }

         this.fParentSettings.getProperty(var1);
      }

   }
}
