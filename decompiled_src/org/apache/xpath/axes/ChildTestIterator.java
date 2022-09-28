package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.compiler.Compiler;

public class ChildTestIterator extends BasicTestIterator {
   static final long serialVersionUID = -7936835957960705722L;
   protected transient DTMAxisTraverser m_traverser;

   ChildTestIterator(Compiler compiler, int opPos, int analysis) throws TransformerException {
      super(compiler, opPos, analysis);
   }

   public ChildTestIterator(DTMAxisTraverser traverser) {
      super((PrefixResolver)null);
      this.m_traverser = traverser;
   }

   protected int getNextNode() {
      super.m_lastFetched = -1 == super.m_lastFetched ? this.m_traverser.first(super.m_context) : this.m_traverser.next(super.m_context, super.m_lastFetched);
      return super.m_lastFetched;
   }

   public DTMIterator cloneWithReset() throws CloneNotSupportedException {
      ChildTestIterator clone = (ChildTestIterator)super.cloneWithReset();
      clone.m_traverser = this.m_traverser;
      return clone;
   }

   public void setRoot(int context, Object environment) {
      super.setRoot(context, environment);
      this.m_traverser = super.m_cdtm.getAxisTraverser(3);
   }

   public int getAxis() {
      return 3;
   }

   public void detach() {
      if (super.m_allowDetach) {
         this.m_traverser = null;
         super.detach();
      }

   }
}
