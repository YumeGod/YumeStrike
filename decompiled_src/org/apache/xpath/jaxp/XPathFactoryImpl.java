package org.apache.xpath.jaxp;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathVariableResolver;
import org.apache.xpath.res.XPATHMessages;

public class XPathFactoryImpl extends XPathFactory {
   private static final String CLASS_NAME = "XPathFactoryImpl";
   private XPathFunctionResolver xPathFunctionResolver = null;
   private XPathVariableResolver xPathVariableResolver = null;
   private boolean featureSecureProcessing = false;

   public boolean isObjectModelSupported(String objectModel) {
      String fmsg;
      if (objectModel == null) {
         fmsg = XPATHMessages.createXPATHMessage("ER_OBJECT_MODEL_NULL", new Object[]{this.getClass().getName()});
         throw new NullPointerException(fmsg);
      } else if (objectModel.length() == 0) {
         fmsg = XPATHMessages.createXPATHMessage("ER_OBJECT_MODEL_EMPTY", new Object[]{this.getClass().getName()});
         throw new IllegalArgumentException(fmsg);
      } else {
         return objectModel.equals("http://java.sun.com/jaxp/xpath/dom");
      }
   }

   public XPath newXPath() {
      return new XPathImpl(this.xPathVariableResolver, this.xPathFunctionResolver, this.featureSecureProcessing);
   }

   public void setFeature(String name, boolean value) throws XPathFactoryConfigurationException {
      String fmsg;
      if (name == null) {
         fmsg = XPATHMessages.createXPATHMessage("ER_FEATURE_NAME_NULL", new Object[]{"XPathFactoryImpl", new Boolean(value)});
         throw new NullPointerException(fmsg);
      } else if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
         this.featureSecureProcessing = value;
      } else {
         fmsg = XPATHMessages.createXPATHMessage("ER_FEATURE_UNKNOWN", new Object[]{name, "XPathFactoryImpl", new Boolean(value)});
         throw new XPathFactoryConfigurationException(fmsg);
      }
   }

   public boolean getFeature(String name) throws XPathFactoryConfigurationException {
      String fmsg;
      if (name == null) {
         fmsg = XPATHMessages.createXPATHMessage("ER_GETTING_NULL_FEATURE", new Object[]{"XPathFactoryImpl"});
         throw new NullPointerException(fmsg);
      } else if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
         return this.featureSecureProcessing;
      } else {
         fmsg = XPATHMessages.createXPATHMessage("ER_GETTING_UNKNOWN_FEATURE", new Object[]{name, "XPathFactoryImpl"});
         throw new XPathFactoryConfigurationException(fmsg);
      }
   }

   public void setXPathFunctionResolver(XPathFunctionResolver resolver) {
      if (resolver == null) {
         String fmsg = XPATHMessages.createXPATHMessage("ER_NULL_XPATH_FUNCTION_RESOLVER", new Object[]{"XPathFactoryImpl"});
         throw new NullPointerException(fmsg);
      } else {
         this.xPathFunctionResolver = resolver;
      }
   }

   public void setXPathVariableResolver(XPathVariableResolver resolver) {
      if (resolver == null) {
         String fmsg = XPATHMessages.createXPATHMessage("ER_NULL_XPATH_VARIABLE_RESOLVER", new Object[]{"XPathFactoryImpl"});
         throw new NullPointerException(fmsg);
      } else {
         this.xPathVariableResolver = resolver;
      }
   }
}
