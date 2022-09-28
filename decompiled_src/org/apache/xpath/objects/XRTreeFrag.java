package org.apache.xpath.objects;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMNodeIterator;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.RTFIterator;
import org.w3c.dom.NodeList;

public class XRTreeFrag extends XObject implements Cloneable {
   static final long serialVersionUID = -3201553822254911567L;
   private DTMXRTreeFrag m_DTMXRTreeFrag;
   private int m_dtmRoot = -1;
   protected boolean m_allowRelease = false;
   private XMLString m_xmlStr = null;

   public XRTreeFrag(int root, XPathContext xctxt, ExpressionNode parent) {
      super((Object)null);
      this.exprSetParent(parent);
      this.initDTM(root, xctxt);
   }

   public XRTreeFrag(int root, XPathContext xctxt) {
      super((Object)null);
      this.initDTM(root, xctxt);
   }

   private final void initDTM(int root, XPathContext xctxt) {
      this.m_dtmRoot = root;
      DTM dtm = xctxt.getDTM(root);
      if (dtm != null) {
         this.m_DTMXRTreeFrag = xctxt.getDTMXRTreeFrag(xctxt.getDTMIdentity(dtm));
      }

   }

   public Object object() {
      return this.m_DTMXRTreeFrag.getXPathContext() != null ? new DTMNodeIterator(new NodeSetDTM(this.m_dtmRoot, this.m_DTMXRTreeFrag.getXPathContext().getDTMManager())) : super.object();
   }

   public XRTreeFrag(Expression expr) {
      super(expr);
   }

   public void allowDetachToRelease(boolean allowRelease) {
      this.m_allowRelease = allowRelease;
   }

   public void detach() {
      if (this.m_allowRelease) {
         this.m_DTMXRTreeFrag.destruct();
         super.m_obj = null;
      }

   }

   public int getType() {
      return 5;
   }

   public String getTypeString() {
      return "#RTREEFRAG";
   }

   public double num() throws TransformerException {
      XMLString s = this.xstr();
      return s.toDouble();
   }

   public boolean bool() {
      return true;
   }

   public XMLString xstr() {
      if (null == this.m_xmlStr) {
         this.m_xmlStr = this.m_DTMXRTreeFrag.getDTM().getStringValue(this.m_dtmRoot);
      }

      return this.m_xmlStr;
   }

   public void appendToFsb(FastStringBuffer fsb) {
      XString xstring = (XString)this.xstr();
      xstring.appendToFsb(fsb);
   }

   public String str() {
      String str = this.m_DTMXRTreeFrag.getDTM().getStringValue(this.m_dtmRoot).toString();
      return null == str ? "" : str;
   }

   public int rtf() {
      return this.m_dtmRoot;
   }

   public DTMIterator asNodeIterator() {
      return new RTFIterator(this.m_dtmRoot, this.m_DTMXRTreeFrag.getXPathContext().getDTMManager());
   }

   public NodeList convertToNodeset() {
      return (NodeList)(super.m_obj instanceof NodeList ? (NodeList)super.m_obj : new DTMNodeList(this.asNodeIterator()));
   }

   public boolean equals(XObject obj2) {
      try {
         if (4 == obj2.getType()) {
            return obj2.equals(this);
         } else if (1 == obj2.getType()) {
            return this.bool() == obj2.bool();
         } else if (2 == obj2.getType()) {
            return this.num() == obj2.num();
         } else if (4 == obj2.getType()) {
            return this.xstr().equals(obj2.xstr());
         } else if (3 == obj2.getType()) {
            return this.xstr().equals(obj2.xstr());
         } else {
            return 5 == obj2.getType() ? this.xstr().equals(obj2.xstr()) : super.equals(obj2);
         }
      } catch (TransformerException var3) {
         throw new WrappedRuntimeException(var3);
      }
   }
}
