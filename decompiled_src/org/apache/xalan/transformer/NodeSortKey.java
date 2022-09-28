package org.apache.xalan.transformer;

import java.text.Collator;
import java.util.Locale;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPath;

class NodeSortKey {
   XPath m_selectPat;
   boolean m_treatAsNumbers;
   boolean m_descending;
   boolean m_caseOrderUpper;
   Collator m_col;
   Locale m_locale;
   PrefixResolver m_namespaceContext;
   TransformerImpl m_processor;

   NodeSortKey(TransformerImpl transformer, XPath selectPat, boolean treatAsNumbers, boolean descending, String langValue, boolean caseOrderUpper, PrefixResolver namespaceContext) throws TransformerException {
      this.m_processor = transformer;
      this.m_namespaceContext = namespaceContext;
      this.m_selectPat = selectPat;
      this.m_treatAsNumbers = treatAsNumbers;
      this.m_descending = descending;
      this.m_caseOrderUpper = caseOrderUpper;
      if (null != langValue && !this.m_treatAsNumbers) {
         this.m_locale = new Locale(langValue.toLowerCase(), Locale.getDefault().getCountry());
         if (null == this.m_locale) {
            this.m_locale = Locale.getDefault();
         }
      } else {
         this.m_locale = Locale.getDefault();
      }

      this.m_col = Collator.getInstance(this.m_locale);
      if (null == this.m_col) {
         this.m_processor.getMsgMgr().warn((SourceLocator)null, "WG_CANNOT_FIND_COLLATOR", new Object[]{langValue});
         this.m_col = Collator.getInstance();
      }

   }
}
