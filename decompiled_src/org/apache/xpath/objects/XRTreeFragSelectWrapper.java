package org.apache.xpath.objects;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.res.XPATHMessages;

public class XRTreeFragSelectWrapper extends XRTreeFrag implements Cloneable {
   static final long serialVersionUID = -6526177905590461251L;

   public XRTreeFragSelectWrapper(Expression expr) {
      super(expr);
   }

   public void fixupVariables(Vector vars, int globalsSize) {
      ((Expression)super.m_obj).fixupVariables(vars, globalsSize);
   }

   public XObject execute(XPathContext xctxt) throws TransformerException {
      XObject m_selected = ((Expression)super.m_obj).execute(xctxt);
      m_selected.allowDetachToRelease(super.m_allowRelease);
      return (XObject)(m_selected.getType() == 3 ? m_selected : new XString(m_selected.str()));
   }

   public void detach() {
      throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_DETACH_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", (Object[])null));
   }

   public double num() throws TransformerException {
      throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NUM_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", (Object[])null));
   }

   public XMLString xstr() {
      throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_XSTR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", (Object[])null));
   }

   public String str() {
      throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_STR_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", (Object[])null));
   }

   public int getType() {
      return 3;
   }

   public int rtf() {
      throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", (Object[])null));
   }

   public DTMIterator asNodeIterator() {
      throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", (Object[])null));
   }
}
