package org.apache.batik.dom;

public interface DomExtension {
   float getPriority();

   String getAuthor();

   String getContactAddress();

   String getURL();

   String getDescription();

   void registerTags(ExtensibleDOMImplementation var1);
}
