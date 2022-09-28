package org.apache.xpath;

import javax.xml.transform.SourceLocator;
import org.apache.xml.utils.PrefixResolver;

public interface XPathFactory {
   XPath create(String var1, SourceLocator var2, PrefixResolver var3, int var4);
}
