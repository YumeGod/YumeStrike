package org.apache.xalan.transformer;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.KeyDeclaration;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.ChildTestIterator;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;

public class KeyRefIterator extends ChildTestIterator {
   static final long serialVersionUID = 3837456451659435102L;
   DTMIterator m_keysNodes;
   protected XMLString m_ref;
   protected QName m_name;
   protected Vector m_keyDeclarations;

   public KeyRefIterator(QName name, XMLString ref, Vector keyDecls, DTMIterator ki) {
      super((DTMAxisTraverser)null);
      this.m_name = name;
      this.m_ref = ref;
      this.m_keyDeclarations = keyDecls;
      this.m_keysNodes = ki;
      this.setWhatToShow(-1);
   }

   protected int getNextNode() {
      int next;
      while(-1 != (next = this.m_keysNodes.nextNode()) && 1 != this.filterNode(next)) {
      }

      super.m_lastFetched = next;
      return next;
   }

   public short filterNode(int testNode) {
      boolean foundKey = false;
      Vector keys = this.m_keyDeclarations;
      QName name = this.m_name;
      KeyIterator ki = (KeyIterator)((XNodeSet)this.m_keysNodes).getContainedIter();
      XPathContext xctxt = ki.getXPathContext();
      if (null == xctxt) {
         this.assertion(false, "xctxt can not be null here!");
      }

      try {
         XMLString lookupKey = this.m_ref;
         int nDeclarations = keys.size();

         for(int i = 0; i < nDeclarations; ++i) {
            KeyDeclaration kd = (KeyDeclaration)keys.elementAt(i);
            if (kd.getName().equals(name)) {
               foundKey = true;
               XObject xuse = kd.getUse().execute(xctxt, testNode, ki.getPrefixResolver());
               if (xuse.getType() != 4) {
                  XMLString exprResult = xuse.xstr();
                  if (lookupKey.equals(exprResult)) {
                     return 1;
                  }
               } else {
                  DTMIterator nl = ((XNodeSet)xuse).iterRaw();

                  int useNode;
                  while(-1 != (useNode = nl.nextNode())) {
                     DTM dtm = this.getDTM(useNode);
                     XMLString exprResult = dtm.getStringValue(useNode);
                     if (null != exprResult && lookupKey.equals(exprResult)) {
                        return 1;
                     }
                  }
               }
            }
         }
      } catch (TransformerException var16) {
         throw new WrappedRuntimeException(var16);
      }

      if (!foundKey) {
         throw new RuntimeException(XSLMessages.createMessage("ER_NO_XSLKEY_DECLARATION", new Object[]{name.getLocalName()}));
      } else {
         return 2;
      }
   }
}
