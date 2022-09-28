package org.apache.xalan.xsltc.dom;

import java.text.Collator;
import java.util.Locale;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;

final class SortSettings {
   private AbstractTranslet _translet;
   private int[] _sortOrders;
   private int[] _types;
   private Locale[] _locales;
   private Collator[] _collators;
   private String[] _caseOrders;

   SortSettings(AbstractTranslet translet, int[] sortOrders, int[] types, Locale[] locales, Collator[] collators, String[] caseOrders) {
      this._translet = translet;
      this._sortOrders = sortOrders;
      this._types = types;
      this._locales = locales;
      this._collators = collators;
      this._caseOrders = caseOrders;
   }

   AbstractTranslet getTranslet() {
      return this._translet;
   }

   int[] getSortOrders() {
      return this._sortOrders;
   }

   int[] getTypes() {
      return this._types;
   }

   Locale[] getLocales() {
      return this._locales;
   }

   Collator[] getCollators() {
      return this._collators;
   }

   String[] getCaseOrders() {
      return this._caseOrders;
   }
}
