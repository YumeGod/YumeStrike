package org.apache.xml.dtm;

import javax.xml.transform.Source;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Node;

public abstract class DTMManager {
   private static final String defaultPropName = "org.apache.xml.dtm.DTMManager";
   private static String defaultClassName = "org.apache.xml.dtm.ref.DTMManagerDefault";
   protected XMLStringFactory m_xsf = null;
   public boolean m_incremental = false;
   public boolean m_source_location = false;
   private static boolean debug;
   public static final int IDENT_DTM_NODE_BITS = 16;
   public static final int IDENT_NODE_DEFAULT = 65535;
   public static final int IDENT_DTM_DEFAULT = -65536;
   public static final int IDENT_MAX_DTMS = 65536;

   protected DTMManager() {
   }

   public XMLStringFactory getXMLStringFactory() {
      return this.m_xsf;
   }

   public void setXMLStringFactory(XMLStringFactory xsf) {
      this.m_xsf = xsf;
   }

   public static DTMManager newInstance(XMLStringFactory xsf) throws DTMConfigurationException {
      DTMManager factoryImpl = null;

      try {
         factoryImpl = (DTMManager)ObjectFactory.createObject("org.apache.xml.dtm.DTMManager", defaultClassName);
      } catch (ObjectFactory.ConfigurationError var3) {
         throw new DTMConfigurationException(XMLMessages.createXMLMessage("ER_NO_DEFAULT_IMPL", (Object[])null), var3.getException());
      }

      if (factoryImpl == null) {
         throw new DTMConfigurationException(XMLMessages.createXMLMessage("ER_NO_DEFAULT_IMPL", (Object[])null));
      } else {
         factoryImpl.setXMLStringFactory(xsf);
         return factoryImpl;
      }
   }

   public abstract DTM getDTM(Source var1, boolean var2, DTMWSFilter var3, boolean var4, boolean var5);

   public abstract DTM getDTM(int var1);

   public abstract int getDTMHandleFromNode(Node var1);

   public abstract DTM createDocumentFragment();

   public abstract boolean release(DTM var1, boolean var2);

   public abstract DTMIterator createDTMIterator(Object var1, int var2);

   public abstract DTMIterator createDTMIterator(String var1, PrefixResolver var2);

   public abstract DTMIterator createDTMIterator(int var1, DTMFilter var2, boolean var3);

   public abstract DTMIterator createDTMIterator(int var1);

   public boolean getIncremental() {
      return this.m_incremental;
   }

   public void setIncremental(boolean incremental) {
      this.m_incremental = incremental;
   }

   public boolean getSource_location() {
      return this.m_source_location;
   }

   public void setSource_location(boolean sourceLocation) {
      this.m_source_location = sourceLocation;
   }

   public abstract int getDTMIdentity(DTM var1);

   public int getDTMIdentityMask() {
      return -65536;
   }

   public int getNodeIdentityMask() {
      return 65535;
   }

   static {
      try {
         debug = System.getProperty("dtm.debug") != null;
      } catch (SecurityException var1) {
      }

   }
}
