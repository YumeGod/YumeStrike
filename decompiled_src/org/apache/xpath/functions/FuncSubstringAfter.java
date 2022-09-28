package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

public class FuncSubstringAfter extends Function2Args {
   static final long serialVersionUID = -8119731889862512194L;

   public XObject execute(XPathContext xctxt) throws TransformerException {
      XMLString s1 = super.m_arg0.execute(xctxt).xstr();
      XMLString s2 = super.m_arg1.execute(xctxt).xstr();
      int index = s1.indexOf(s2);
      return -1 == index ? XString.EMPTYSTRING : (XString)s1.substring(index + s2.length());
   }
}
