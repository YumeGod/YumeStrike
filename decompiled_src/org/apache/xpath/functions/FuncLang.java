package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;

public class FuncLang extends FunctionOneArg {
   static final long serialVersionUID = -7868705139354872185L;

   public XObject execute(XPathContext xctxt) throws TransformerException {
      String lang = super.m_arg0.execute(xctxt).str();
      int parent = xctxt.getCurrentNode();
      boolean isLang = false;

      for(DTM dtm = xctxt.getDTM(parent); -1 != parent; parent = dtm.getParent(parent)) {
         if (1 == dtm.getNodeType(parent)) {
            int langAttr = dtm.getAttributeNode(parent, "http://www.w3.org/XML/1998/namespace", "lang");
            if (-1 != langAttr) {
               String langVal = dtm.getNodeValue(langAttr);
               if (langVal.toLowerCase().startsWith(lang.toLowerCase())) {
                  int valLen = lang.length();
                  if (langVal.length() == valLen || langVal.charAt(valLen) == '-') {
                     isLang = true;
                  }
               }
               break;
            }
         }
      }

      return isLang ? XBoolean.S_TRUE : XBoolean.S_FALSE;
   }
}
