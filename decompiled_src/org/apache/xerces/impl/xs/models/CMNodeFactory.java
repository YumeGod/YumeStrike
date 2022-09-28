package org.apache.xerces.impl.xs.models;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.util.SecurityManager;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;

public class CMNodeFactory {
   private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
   private static final boolean DEBUG = false;
   private static final int MULTIPLICITY = 1;
   private int nodeCount = 0;
   private int maxNodeLimit;
   private XMLErrorReporter fErrorReporter;
   private SecurityManager fSecurityManager = null;

   public void reset(XMLComponentManager var1) {
      this.fErrorReporter = (XMLErrorReporter)var1.getProperty("http://apache.org/xml/properties/internal/error-reporter");

      try {
         this.fSecurityManager = (SecurityManager)var1.getProperty("http://apache.org/xml/properties/security-manager");
         if (this.fSecurityManager != null) {
            this.maxNodeLimit = this.fSecurityManager.getMaxOccurNodeLimit() * 1;
         }
      } catch (XMLConfigurationException var3) {
         this.fSecurityManager = null;
      }

   }

   public CMNode getCMLeafNode(int var1, Object var2, int var3, int var4) {
      this.nodeCountCheck();
      return new XSCMLeaf(var1, var2, var3, var4);
   }

   public CMNode getCMUniOpNode(int var1, CMNode var2) {
      this.nodeCountCheck();
      return new XSCMUniOp(var1, var2);
   }

   public CMNode getCMBinOpNode(int var1, CMNode var2, CMNode var3) {
      this.nodeCountCheck();
      return new XSCMBinOp(var1, var2, var3);
   }

   public void nodeCountCheck() {
      if (this.fSecurityManager != null && this.nodeCount++ > this.maxNodeLimit) {
         this.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "maxOccurLimit", new Object[]{new Integer(this.maxNodeLimit)}, (short)2);
         this.nodeCount = 0;
      }

   }

   public void resetNodeCount() {
      this.nodeCount = 0;
   }

   public void setProperty(String var1, Object var2) throws XMLConfigurationException {
      if (var1.startsWith("http://apache.org/xml/properties/")) {
         int var3 = var1.length() - "http://apache.org/xml/properties/".length();
         if (var3 == "security-manager".length() && var1.endsWith("security-manager")) {
            this.fSecurityManager = (SecurityManager)var2;
            this.maxNodeLimit = this.fSecurityManager != null ? this.fSecurityManager.getMaxOccurNodeLimit() * 1 : 0;
            return;
         }

         if (var3 == "internal/error-reporter".length() && var1.endsWith("internal/error-reporter")) {
            this.fErrorReporter = (XMLErrorReporter)var2;
            return;
         }
      }

   }
}
