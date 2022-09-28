package javax.xml.xpath;

public abstract class XPathFactory {
   public static final String DEFAULT_PROPERTY_NAME = "javax.xml.xpath.XPathFactory";
   public static final String DEFAULT_OBJECT_MODEL_URI = "http://java.sun.com/jaxp/xpath/dom";
   // $FF: synthetic field
   static Class class$javax$xml$xpath$XPathFactory;

   protected XPathFactory() {
   }

   public static final XPathFactory newInstance() {
      try {
         return newInstance("http://java.sun.com/jaxp/xpath/dom");
      } catch (XPathFactoryConfigurationException var1) {
         throw new RuntimeException("XPathFactory#newInstance() failed to create an XPathFactory for the default object model: http://java.sun.com/jaxp/xpath/dom with the XPathFactoryConfigurationException: " + var1.toString());
      }
   }

   public static final XPathFactory newInstance(String var0) throws XPathFactoryConfigurationException {
      if (var0 == null) {
         throw new NullPointerException("XPathFactory#newInstance(String uri) cannot be called with uri == null");
      } else if (var0.length() == 0) {
         throw new IllegalArgumentException("XPathFactory#newInstance(String uri) cannot be called with uri == \"\"");
      } else {
         ClassLoader var1 = SecuritySupport.getContextClassLoader();
         if (var1 == null) {
            var1 = (class$javax$xml$xpath$XPathFactory == null ? (class$javax$xml$xpath$XPathFactory = class$("javax.xml.xpath.XPathFactory")) : class$javax$xml$xpath$XPathFactory).getClassLoader();
         }

         XPathFactory var2 = (new XPathFactoryFinder(var1)).newFactory(var0);
         if (var2 == null) {
            throw new XPathFactoryConfigurationException("No XPathFctory implementation found for the object model: " + var0);
         } else {
            return var2;
         }
      }
   }

   public abstract boolean isObjectModelSupported(String var1);

   public abstract void setFeature(String var1, boolean var2) throws XPathFactoryConfigurationException;

   public abstract boolean getFeature(String var1) throws XPathFactoryConfigurationException;

   public abstract void setXPathVariableResolver(XPathVariableResolver var1);

   public abstract void setXPathFunctionResolver(XPathFunctionResolver var1);

   public abstract XPath newXPath();

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
