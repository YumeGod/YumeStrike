package org.apache.xalan.xsltc.dom;

import org.apache.xml.dtm.DTMAxisIterator;

public final class FilteredStepIterator extends StepIterator {
   private Filter _filter;

   public FilteredStepIterator(DTMAxisIterator source, DTMAxisIterator iterator, Filter filter) {
      super(source, iterator);
      this._filter = filter;
   }

   public int next() {
      int node;
      do {
         if ((node = super.next()) == -1) {
            return node;
         }
      } while(!this._filter.test(node));

      return this.returnNode(node);
   }
}
