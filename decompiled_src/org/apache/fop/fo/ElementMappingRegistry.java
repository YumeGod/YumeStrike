package org.apache.fop.fo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.Service;
import org.w3c.dom.DOMImplementation;
import org.xml.sax.Locator;

public class ElementMappingRegistry {
   protected Log log;
   protected Map fobjTable;
   protected Map namespaces;

   public ElementMappingRegistry(FopFactory factory) {
      this.log = LogFactory.getLog(ElementMappingRegistry.class);
      this.fobjTable = new HashMap();
      this.namespaces = new HashMap();
      this.setupDefaultMappings();
   }

   private void setupDefaultMappings() {
      Iterator providers = Service.providers(ElementMapping.class, false);
      if (providers != null) {
         while(providers.hasNext()) {
            String mapping = (String)providers.next();

            try {
               this.addElementMapping(mapping);
            } catch (IllegalArgumentException var4) {
               this.log.warn("Error while adding element mapping", var4);
            }
         }
      }

   }

   public void addElementMapping(String mappingClassName) throws IllegalArgumentException {
      try {
         ElementMapping mapping = (ElementMapping)Class.forName(mappingClassName).newInstance();
         this.addElementMapping(mapping);
      } catch (ClassNotFoundException var3) {
         throw new IllegalArgumentException("Could not find " + mappingClassName);
      } catch (InstantiationException var4) {
         throw new IllegalArgumentException("Could not instantiate " + mappingClassName);
      } catch (IllegalAccessException var5) {
         throw new IllegalArgumentException("Could not access " + mappingClassName);
      } catch (ClassCastException var6) {
         throw new IllegalArgumentException(mappingClassName + " is not an ElementMapping");
      }
   }

   public void addElementMapping(ElementMapping mapping) {
      this.fobjTable.put(mapping.getNamespaceURI(), mapping.getTable());
      this.namespaces.put(mapping.getNamespaceURI().intern(), mapping);
   }

   public ElementMapping.Maker findFOMaker(String namespaceURI, String localName, Locator locator) throws FOPException {
      Map table = (Map)this.fobjTable.get(namespaceURI);
      ElementMapping.Maker fobjMaker = null;
      if (table != null) {
         fobjMaker = (ElementMapping.Maker)table.get(localName);
         if (fobjMaker == null) {
            fobjMaker = (ElementMapping.Maker)table.get("<default>");
         }
      }

      if (fobjMaker == null) {
         if (this.namespaces.containsKey(namespaceURI.intern())) {
            throw new FOPException(FONode.errorText(locator) + "No element mapping definition found for " + FONode.getNodeString(namespaceURI, localName), locator);
         }

         fobjMaker = new UnknownXMLObj.Maker(namespaceURI);
      }

      return (ElementMapping.Maker)fobjMaker;
   }

   public DOMImplementation getDOMImplementationForNamespace(String namespaceURI) {
      ElementMapping mapping = (ElementMapping)this.namespaces.get(namespaceURI);
      return mapping == null ? null : mapping.getDOMImplementation();
   }

   public ElementMapping getElementMapping(String namespaceURI) {
      return (ElementMapping)this.namespaces.get(namespaceURI);
   }

   public boolean isKnownNamespace(String namespaceURI) {
      return this.namespaces.containsKey(namespaceURI);
   }
}
