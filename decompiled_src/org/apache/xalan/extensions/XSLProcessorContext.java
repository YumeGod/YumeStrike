package org.apache.xalan.extensions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.xml.transform.TransformerException;
import org.apache.xalan.serialize.SerializerUtils;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.transformer.ClonerToResultTree;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.QName;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.DescendantIterator;
import org.apache.xpath.axes.OneStepIterator;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XRTreeFrag;
import org.apache.xpath.objects.XString;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

public class XSLProcessorContext {
   private TransformerImpl transformer;
   private Stylesheet stylesheetTree;
   private DTM sourceTree;
   private int sourceNode;
   private QName mode;

   public XSLProcessorContext(TransformerImpl transformer, Stylesheet stylesheetTree) {
      this.transformer = transformer;
      this.stylesheetTree = stylesheetTree;
      XPathContext xctxt = transformer.getXPathContext();
      this.mode = transformer.getMode();
      this.sourceNode = xctxt.getCurrentNode();
      this.sourceTree = xctxt.getDTM(this.sourceNode);
   }

   public TransformerImpl getTransformer() {
      return this.transformer;
   }

   public Stylesheet getStylesheet() {
      return this.stylesheetTree;
   }

   public Node getSourceTree() {
      return this.sourceTree.getNode(this.sourceTree.getDocumentRoot(this.sourceNode));
   }

   public Node getContextNode() {
      return this.sourceTree.getNode(this.sourceNode);
   }

   public QName getMode() {
      return this.mode;
   }

   public void outputToResultTree(Stylesheet stylesheetTree, Object obj) throws TransformerException, MalformedURLException, FileNotFoundException, IOException {
      try {
         SerializationHandler rtreeHandler = this.transformer.getResultTreeHandler();
         XPathContext xctxt = this.transformer.getXPathContext();
         Object value;
         int handle;
         if (obj instanceof XObject) {
            value = (XObject)obj;
         } else if (obj instanceof String) {
            value = new XString((String)obj);
         } else if (obj instanceof Boolean) {
            value = new XBoolean((Boolean)obj);
         } else if (obj instanceof Double) {
            value = new XNumber((Double)obj);
         } else if (obj instanceof DocumentFragment) {
            handle = xctxt.getDTMHandleFromNode((DocumentFragment)obj);
            value = new XRTreeFrag(handle, xctxt);
         } else if (obj instanceof DTM) {
            DTM dtm = (DTM)obj;
            DTMIterator iterator = new DescendantIterator();
            iterator.setRoot(dtm.getDocument(), xctxt);
            value = new XNodeSet(iterator);
         } else if (obj instanceof DTMAxisIterator) {
            DTMAxisIterator iter = (DTMAxisIterator)obj;
            DTMIterator iterator = new OneStepIterator(iter, -1);
            value = new XNodeSet(iterator);
         } else if (obj instanceof DTMIterator) {
            value = new XNodeSet((DTMIterator)obj);
         } else if (obj instanceof NodeIterator) {
            value = new XNodeSet(new NodeSetDTM((NodeIterator)obj, xctxt));
         } else if (obj instanceof Node) {
            value = new XNodeSet(xctxt.getDTMHandleFromNode((Node)obj), xctxt.getDTMManager());
         } else {
            value = new XString(obj.toString());
         }

         handle = ((XObject)value).getType();
         switch (handle) {
            case 1:
            case 2:
            case 3:
               String s = ((XObject)value).str();
               rtreeHandler.characters(s.toCharArray(), 0, s.length());
               break;
            case 4:
               DTMIterator nl = ((XObject)value).iter();

               int pos;
               while(-1 != (pos = nl.nextNode())) {
                  DTM dtm = nl.getDTM(pos);

                  int nextNode;
                  for(int top = pos; -1 != pos; pos = nextNode) {
                     rtreeHandler.flushPending();
                     ClonerToResultTree.cloneToResultTree(pos, dtm.getNodeType(pos), dtm, rtreeHandler, true);
                     nextNode = dtm.getFirstChild(pos);

                     while(-1 == nextNode) {
                        if (1 == dtm.getNodeType(pos)) {
                           rtreeHandler.endElement("", "", dtm.getNodeName(pos));
                        }

                        if (top == pos) {
                           break;
                        }

                        nextNode = dtm.getNextSibling(pos);
                        if (-1 == nextNode) {
                           pos = dtm.getParent(pos);
                           if (top == pos) {
                              if (1 == dtm.getNodeType(pos)) {
                                 rtreeHandler.endElement("", "", dtm.getNodeName(pos));
                              }

                              nextNode = -1;
                              break;
                           }
                        }
                     }
                  }
               }

               return;
            case 5:
               SerializerUtils.outputResultTreeFragment(rtreeHandler, (XObject)value, this.transformer.getXPathContext());
         }

      } catch (SAXException var13) {
         throw new TransformerException(var13);
      }
   }
}
