package org.apache.xalan.transformer;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.KeyDeclaration;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.OneStepIteratorForward;

public class KeyIterator extends OneStepIteratorForward {
   static final long serialVersionUID = -1349109910100249661L;
   private QName m_name;
   private Vector m_keyDeclarations;

   public QName getName() {
      return this.m_name;
   }

   public Vector getKeyDeclarations() {
      return this.m_keyDeclarations;
   }

   KeyIterator(QName name, Vector keyDeclarations) {
      super(16);
      this.m_keyDeclarations = keyDeclarations;
      this.m_name = name;
   }

   public short acceptNode(int testNode) {
      boolean foundKey = false;
      KeyIterator ki = (KeyIterator)super.m_lpi;
      XPathContext xctxt = ki.getXPathContext();
      Vector keys = ki.getKeyDeclarations();
      QName name = ki.getName();

      try {
         int nDeclarations = keys.size();

         for(int i = 0; i < nDeclarations; ++i) {
            KeyDeclaration kd = (KeyDeclaration)keys.elementAt(i);
            if (kd.getName().equals(name)) {
               foundKey = true;
               XPath matchExpr = kd.getMatch();
               double score = matchExpr.getMatchScore(xctxt, testNode);
               kd.getMatch();
               if (score != Double.NEGATIVE_INFINITY) {
                  return 1;
               }
            }
         }
      } catch (TransformerException var13) {
      }

      if (!foundKey) {
         throw new RuntimeException(XSLMessages.createMessage("ER_NO_XSLKEY_DECLARATION", new Object[]{name.getLocalName()}));
      } else {
         return 2;
      }
   }
}
