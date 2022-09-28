package javax.xml.namespace;

import java.util.Iterator;

public interface NamespaceContext {
   String getNamespaceURI(String var1);

   String getPrefix(String var1);

   Iterator getPrefixes(String var1);
}
