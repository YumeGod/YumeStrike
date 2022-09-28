package org.apache.xml.utils;

import org.w3c.dom.Node;

public interface PrefixResolver {
   String getNamespaceForPrefix(String var1);

   String getNamespaceForPrefix(String var1, Node var2);

   String getBaseIdentifier();

   boolean handlesNullPrefixes();
}
