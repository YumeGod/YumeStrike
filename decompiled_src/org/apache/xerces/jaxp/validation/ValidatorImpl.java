package org.apache.xerces.jaxp.validation;

import java.io.IOException;
import java.util.Locale;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import org.apache.xerces.util.SAXMessageFormatter;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.PSVIProvider;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

final class ValidatorImpl extends Validator implements PSVIProvider {
   private XMLSchemaValidatorComponentManager fComponentManager;
   private ValidatorHandlerImpl fSAXValidatorHelper;
   private DOMValidatorHelper fDOMValidatorHelper;
   private StreamValidatorHelper fStreamValidatorHelper;
   private boolean fConfigurationChanged = false;
   private boolean fErrorHandlerChanged = false;
   private boolean fResourceResolverChanged = false;

   public ValidatorImpl(XSGrammarPoolContainer var1) {
      this.fComponentManager = new XMLSchemaValidatorComponentManager(var1);
      this.setErrorHandler((ErrorHandler)null);
      this.setResourceResolver((LSResourceResolver)null);
   }

   public void validate(Source var1, Result var2) throws SAXException, IOException {
      if (var1 instanceof SAXSource) {
         if (this.fSAXValidatorHelper == null) {
            this.fSAXValidatorHelper = new ValidatorHandlerImpl(this.fComponentManager);
         }

         this.fSAXValidatorHelper.validate(var1, var2);
      } else if (var1 instanceof DOMSource) {
         if (this.fDOMValidatorHelper == null) {
            this.fDOMValidatorHelper = new DOMValidatorHelper(this.fComponentManager);
         }

         this.fDOMValidatorHelper.validate(var1, var2);
      } else {
         if (!(var1 instanceof StreamSource)) {
            if (var1 == null) {
               throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(Locale.getDefault(), "SourceParameterNull", (Object[])null));
            }

            throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(Locale.getDefault(), "SourceNotAccepted", new Object[]{var1.getClass().getName()}));
         }

         if (this.fStreamValidatorHelper == null) {
            this.fStreamValidatorHelper = new StreamValidatorHelper(this.fComponentManager);
         }

         this.fStreamValidatorHelper.validate(var1, var2);
      }

   }

   public void setErrorHandler(ErrorHandler var1) {
      this.fErrorHandlerChanged = var1 != null;
      this.fComponentManager.setErrorHandler(var1);
   }

   public ErrorHandler getErrorHandler() {
      return this.fComponentManager.getErrorHandler();
   }

   public void setResourceResolver(LSResourceResolver var1) {
      this.fResourceResolverChanged = var1 != null;
      this.fComponentManager.setResourceResolver(var1);
   }

   public LSResourceResolver getResourceResolver() {
      return this.fComponentManager.getResourceResolver();
   }

   public boolean getFeature(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         try {
            return this.fComponentManager.getFeature(var1);
         } catch (XMLConfigurationException var5) {
            String var3 = var5.getIdentifier();
            String var4 = var5.getType() == 0 ? "feature-not-recognized" : "feature-not-supported";
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(Locale.getDefault(), var4, new Object[]{var3}));
         }
      }
   }

   public void setFeature(String var1, boolean var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         try {
            this.fComponentManager.setFeature(var1, var2);
         } catch (XMLConfigurationException var6) {
            String var4 = var6.getIdentifier();
            String var5 = var6.getType() == 0 ? "feature-not-recognized" : "feature-not-supported";
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(Locale.getDefault(), var5, new Object[]{var4}));
         }

         this.fConfigurationChanged = true;
      }
   }

   public Object getProperty(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         try {
            return this.fComponentManager.getProperty(var1);
         } catch (XMLConfigurationException var5) {
            String var3 = var5.getIdentifier();
            String var4 = var5.getType() == 0 ? "property-not-recognized" : "property-not-supported";
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(Locale.getDefault(), var4, new Object[]{var3}));
         }
      }
   }

   public void setProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         try {
            this.fComponentManager.setProperty(var1, var2);
         } catch (XMLConfigurationException var6) {
            String var4 = var6.getIdentifier();
            String var5 = var6.getType() == 0 ? "property-not-recognized" : "property-not-supported";
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(Locale.getDefault(), var5, new Object[]{var4}));
         }

         this.fConfigurationChanged = true;
      }
   }

   public void reset() {
      if (this.fConfigurationChanged) {
         this.fComponentManager.restoreInitialState();
         this.setErrorHandler((ErrorHandler)null);
         this.setResourceResolver((LSResourceResolver)null);
         this.fConfigurationChanged = false;
         this.fErrorHandlerChanged = false;
         this.fResourceResolverChanged = false;
      } else {
         if (this.fErrorHandlerChanged) {
            this.setErrorHandler((ErrorHandler)null);
            this.fErrorHandlerChanged = false;
         }

         if (this.fResourceResolverChanged) {
            this.setResourceResolver((LSResourceResolver)null);
            this.fResourceResolverChanged = false;
         }
      }

   }

   public ElementPSVI getElementPSVI() {
      return this.fSAXValidatorHelper != null ? this.fSAXValidatorHelper.getElementPSVI() : null;
   }

   public AttributePSVI getAttributePSVI(int var1) {
      return this.fSAXValidatorHelper != null ? this.fSAXValidatorHelper.getAttributePSVI(var1) : null;
   }

   public AttributePSVI getAttributePSVIByName(String var1, String var2) {
      return this.fSAXValidatorHelper != null ? this.fSAXValidatorHelper.getAttributePSVIByName(var1, var2) : null;
   }
}
