package org.apache.xpath.objects;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.OneStepIterator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;

public class XObjectFactory {
   public static XObject create(Object val) {
      Object result;
      if (val instanceof XObject) {
         result = (XObject)val;
      } else if (val instanceof String) {
         result = new XString((String)val);
      } else if (val instanceof Boolean) {
         result = new XBoolean((Boolean)val);
      } else if (val instanceof Double) {
         result = new XNumber((Double)val);
      } else {
         result = new XObject(val);
      }

      return (XObject)result;
   }

   public static XObject create(Object val, XPathContext xctxt) {
      Object result;
      if (val instanceof XObject) {
         result = (XObject)val;
      } else if (val instanceof String) {
         result = new XString((String)val);
      } else if (val instanceof Boolean) {
         result = new XBoolean((Boolean)val);
      } else if (val instanceof Number) {
         result = new XNumber((Number)val);
      } else if (val instanceof DTM) {
         DTM dtm = (DTM)val;

         try {
            int dtmRoot = dtm.getDocument();
            DTMAxisIterator iter = dtm.getAxisIterator(13);
            iter.setStartNode(dtmRoot);
            DTMIterator iterator = new OneStepIterator(iter, 13);
            iterator.setRoot(dtmRoot, xctxt);
            result = new XNodeSet(iterator);
         } catch (Exception var8) {
            throw new WrappedRuntimeException(var8);
         }
      } else if (val instanceof DTMAxisIterator) {
         DTMAxisIterator iter = (DTMAxisIterator)val;

         try {
            DTMIterator iterator = new OneStepIterator(iter, 13);
            iterator.setRoot(iter.getStartNode(), xctxt);
            result = new XNodeSet(iterator);
         } catch (Exception var7) {
            throw new WrappedRuntimeException(var7);
         }
      } else if (val instanceof DTMIterator) {
         result = new XNodeSet((DTMIterator)val);
      } else if (val instanceof Node) {
         result = new XNodeSetForDOM((Node)val, xctxt);
      } else if (val instanceof NodeList) {
         result = new XNodeSetForDOM((NodeList)val, xctxt);
      } else if (val instanceof NodeIterator) {
         result = new XNodeSetForDOM((NodeIterator)val, xctxt);
      } else {
         result = new XObject(val);
      }

      return (XObject)result;
   }
}
