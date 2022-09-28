package org.apache.xpath.objects;

import java.io.Serializable;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathException;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.res.XPATHMessages;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class XObject extends Expression implements Serializable, Cloneable {
   static final long serialVersionUID = -821887098985662951L;
   protected Object m_obj;
   public static final int CLASS_NULL = -1;
   public static final int CLASS_UNKNOWN = 0;
   public static final int CLASS_BOOLEAN = 1;
   public static final int CLASS_NUMBER = 2;
   public static final int CLASS_STRING = 3;
   public static final int CLASS_NODESET = 4;
   public static final int CLASS_RTREEFRAG = 5;
   public static final int CLASS_UNRESOLVEDVARIABLE = 600;

   public XObject() {
   }

   public XObject(Object obj) {
      this.m_obj = obj;
   }

   public XObject execute(XPathContext xctxt) throws TransformerException {
      return this;
   }

   public void allowDetachToRelease(boolean allowRelease) {
   }

   public void detach() {
   }

   public void destruct() {
      if (null != this.m_obj) {
         this.allowDetachToRelease(true);
         this.detach();
         this.m_obj = null;
      }

   }

   public void reset() {
   }

   public void dispatchCharactersEvents(ContentHandler ch) throws SAXException {
      this.xstr().dispatchCharactersEvents(ch);
   }

   public static XObject create(Object val) {
      return XObjectFactory.create(val);
   }

   public static XObject create(Object val, XPathContext xctxt) {
      return XObjectFactory.create(val, xctxt);
   }

   public int getType() {
      return 0;
   }

   public String getTypeString() {
      return "#UNKNOWN (" + this.object().getClass().getName() + ")";
   }

   public double num() throws TransformerException {
      this.error("ER_CANT_CONVERT_TO_NUMBER", new Object[]{this.getTypeString()});
      return 0.0;
   }

   public double numWithSideEffects() throws TransformerException {
      return this.num();
   }

   public boolean bool() throws TransformerException {
      this.error("ER_CANT_CONVERT_TO_NUMBER", new Object[]{this.getTypeString()});
      return false;
   }

   public boolean boolWithSideEffects() throws TransformerException {
      return this.bool();
   }

   public XMLString xstr() {
      return XMLStringFactoryImpl.getFactory().newstr(this.str());
   }

   public String str() {
      return this.m_obj != null ? this.m_obj.toString() : "";
   }

   public String toString() {
      return this.str();
   }

   public int rtf(XPathContext support) {
      int result = this.rtf();
      if (-1 == result) {
         DTM frag = support.createDocumentFragment();
         frag.appendTextChild(this.str());
         result = frag.getDocument();
      }

      return result;
   }

   public DocumentFragment rtree(XPathContext support) {
      DocumentFragment docFrag = null;
      int result = this.rtf();
      DTM frag;
      if (-1 == result) {
         frag = support.createDocumentFragment();
         frag.appendTextChild(this.str());
         docFrag = (DocumentFragment)frag.getNode(frag.getDocument());
      } else {
         frag = support.getDTM(result);
         docFrag = (DocumentFragment)frag.getNode(frag.getDocument());
      }

      return docFrag;
   }

   public DocumentFragment rtree() {
      return null;
   }

   public int rtf() {
      return -1;
   }

   public Object object() {
      return this.m_obj;
   }

   public DTMIterator iter() throws TransformerException {
      this.error("ER_CANT_CONVERT_TO_NODELIST", new Object[]{this.getTypeString()});
      return null;
   }

   public XObject getFresh() {
      return this;
   }

   public NodeIterator nodeset() throws TransformerException {
      this.error("ER_CANT_CONVERT_TO_NODELIST", new Object[]{this.getTypeString()});
      return null;
   }

   public NodeList nodelist() throws TransformerException {
      this.error("ER_CANT_CONVERT_TO_NODELIST", new Object[]{this.getTypeString()});
      return null;
   }

   public NodeSetDTM mutableNodeset() throws TransformerException {
      this.error("ER_CANT_CONVERT_TO_MUTABLENODELIST", new Object[]{this.getTypeString()});
      return (NodeSetDTM)this.m_obj;
   }

   public Object castToType(int t, XPathContext support) throws TransformerException {
      Object result;
      switch (t) {
         case 0:
            result = this.m_obj;
            break;
         case 1:
            result = new Boolean(this.bool());
            break;
         case 2:
            result = new Double(this.num());
            break;
         case 3:
            result = this.str();
            break;
         case 4:
            result = this.iter();
            break;
         default:
            this.error("ER_CANT_CONVERT_TO_TYPE", new Object[]{this.getTypeString(), Integer.toString(t)});
            result = null;
      }

      return result;
   }

   public boolean lessThan(XObject obj2) throws TransformerException {
      if (obj2.getType() == 4) {
         return obj2.greaterThan(this);
      } else {
         return this.num() < obj2.num();
      }
   }

   public boolean lessThanOrEqual(XObject obj2) throws TransformerException {
      if (obj2.getType() == 4) {
         return obj2.greaterThanOrEqual(this);
      } else {
         return this.num() <= obj2.num();
      }
   }

   public boolean greaterThan(XObject obj2) throws TransformerException {
      if (obj2.getType() == 4) {
         return obj2.lessThan(this);
      } else {
         return this.num() > obj2.num();
      }
   }

   public boolean greaterThanOrEqual(XObject obj2) throws TransformerException {
      if (obj2.getType() == 4) {
         return obj2.lessThanOrEqual(this);
      } else {
         return this.num() >= obj2.num();
      }
   }

   public boolean equals(XObject obj2) {
      if (obj2.getType() == 4) {
         return obj2.equals(this);
      } else if (null != this.m_obj) {
         return this.m_obj.equals(obj2.m_obj);
      } else {
         return obj2.m_obj == null;
      }
   }

   public boolean notEquals(XObject obj2) throws TransformerException {
      if (obj2.getType() == 4) {
         return obj2.notEquals(this);
      } else {
         return !this.equals(obj2);
      }
   }

   protected void error(String msg) throws TransformerException {
      this.error(msg, (Object[])null);
   }

   protected void error(String msg, Object[] args) throws TransformerException {
      String fmsg = XPATHMessages.createXPATHMessage(msg, args);
      throw new XPathException(fmsg, this);
   }

   public void fixupVariables(Vector vars, int globalsSize) {
   }

   public void appendToFsb(FastStringBuffer fsb) {
      fsb.append(this.str());
   }

   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
      this.assertion(false, "callVisitors should not be called for this object!!!");
   }

   public boolean deepEquals(Expression expr) {
      if (!this.isSameClass(expr)) {
         return false;
      } else {
         return this.equals((XObject)expr);
      }
   }
}
