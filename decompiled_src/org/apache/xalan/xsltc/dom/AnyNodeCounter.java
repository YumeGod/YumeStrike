package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xml.dtm.DTMAxisIterator;

public abstract class AnyNodeCounter extends NodeCounter {
   public AnyNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
      super(translet, document, iterator);
   }

   public NodeCounter setStartNode(int node) {
      super._node = node;
      super._nodeType = super._document.getExpandedTypeID(node);
      return this;
   }

   public String getCounter() {
      if (super._value != -2.147483648E9) {
         if (super._value == 0.0) {
            return "0";
         } else if (Double.isNaN(super._value)) {
            return "NaN";
         } else if (super._value < 0.0 && Double.isInfinite(super._value)) {
            return "-Infinity";
         } else {
            return Double.isInfinite(super._value) ? "Infinity" : this.formatNumbers((int)super._value);
         }
      } else {
         int next = super._node;
         int root = super._document.getDocument();

         int result;
         for(result = 0; next >= root && !this.matchesFrom(next); --next) {
            if (this.matchesCount(next)) {
               ++result;
            }
         }

         return this.formatNumbers(result);
      }
   }

   public static NodeCounter getDefaultNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
      return new DefaultAnyNodeCounter(translet, document, iterator);
   }

   static class DefaultAnyNodeCounter extends AnyNodeCounter {
      public DefaultAnyNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
         super(translet, document, iterator);
      }

      public String getCounter() {
         int result;
         if (super._value != -2.147483648E9) {
            if (super._value == 0.0) {
               return "0";
            }

            if (Double.isNaN(super._value)) {
               return "NaN";
            }

            if (super._value < 0.0 && Double.isInfinite(super._value)) {
               return "-Infinity";
            }

            if (Double.isInfinite(super._value)) {
               return "Infinity";
            }

            result = (int)super._value;
         } else {
            int next = super._node;
            result = 0;
            int ntype = super._document.getExpandedTypeID(super._node);

            for(int root = super._document.getDocument(); next >= 0; --next) {
               if (ntype == super._document.getExpandedTypeID(next)) {
                  ++result;
               }

               if (next == root) {
                  break;
               }
            }
         }

         return this.formatNumbers(result);
      }
   }
}
