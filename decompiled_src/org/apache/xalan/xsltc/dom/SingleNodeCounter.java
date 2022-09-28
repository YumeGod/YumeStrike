package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xml.dtm.DTMAxisIterator;

public abstract class SingleNodeCounter extends NodeCounter {
   private static final int[] EmptyArray = new int[0];
   DTMAxisIterator _countSiblings = null;

   public SingleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
      super(translet, document, iterator);
   }

   public NodeCounter setStartNode(int node) {
      super._node = node;
      super._nodeType = super._document.getExpandedTypeID(node);
      this._countSiblings = super._document.getAxisIterator(12);
      return this;
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
         if (!this.matchesCount(next)) {
            while((next = super._document.getParent(next)) > -1 && !this.matchesCount(next)) {
               if (this.matchesFrom(next)) {
                  next = -1;
                  break;
               }
            }
         }

         if (next == -1) {
            return this.formatNumbers(EmptyArray);
         }

         this._countSiblings.setStartNode(next);

         do {
            if (this.matchesCount(next)) {
               ++result;
            }
         } while((next = this._countSiblings.next()) != -1);
      }

      return this.formatNumbers(result);
   }

   public static NodeCounter getDefaultNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
      return new DefaultSingleNodeCounter(translet, document, iterator);
   }

   static class DefaultSingleNodeCounter extends SingleNodeCounter {
      public DefaultSingleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
         super(translet, document, iterator);
      }

      public NodeCounter setStartNode(int node) {
         super._node = node;
         super._nodeType = super._document.getExpandedTypeID(node);
         super._countSiblings = super._document.getTypedAxisIterator(12, super._document.getExpandedTypeID(node));
         return this;
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
            result = 1;
            super._countSiblings.setStartNode(super._node);

            while(super._countSiblings.next() != -1) {
               ++result;
            }
         }

         return this.formatNumbers(result);
      }
   }
}
