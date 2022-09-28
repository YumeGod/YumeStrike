package org.apache.xalan.xsltc.dom;

import java.text.Collator;
import java.util.Locale;
import org.apache.xalan.xsltc.CollatorFactory;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xml.utils.StringComparable;

public abstract class NodeSortRecord {
   public static final int COMPARE_STRING = 0;
   public static final int COMPARE_NUMERIC = 1;
   public static final int COMPARE_ASCENDING = 0;
   public static final int COMPARE_DESCENDING = 1;
   /** @deprecated */
   private static final Collator DEFAULT_COLLATOR = Collator.getInstance();
   /** @deprecated */
   protected Collator _collator;
   protected Collator[] _collators;
   /** @deprecated */
   protected Locale _locale;
   protected CollatorFactory _collatorFactory;
   protected SortSettings _settings;
   private DOM _dom;
   private int _node;
   private int _last;
   private int _scanned;
   private Object[] _values;

   public NodeSortRecord(int node) {
      this._collator = DEFAULT_COLLATOR;
      this._dom = null;
      this._last = 0;
      this._scanned = 0;
      this._node = node;
   }

   public NodeSortRecord() {
      this(0);
   }

   public final void initialize(int node, int last, DOM dom, SortSettings settings) throws TransletException {
      this._dom = dom;
      this._node = node;
      this._last = last;
      this._settings = settings;
      int levels = settings.getSortOrders().length;
      this._values = new Object[levels];
      String colFactClassname = System.getProperty("org.apache.xalan.xsltc.COLLATOR_FACTORY");
      if (colFactClassname != null) {
         try {
            Object candObj = ObjectFactory.findProviderClass(colFactClassname, ObjectFactory.findClassLoader(), true);
            this._collatorFactory = (CollatorFactory)candObj;
         } catch (ClassNotFoundException var9) {
            throw new TransletException(var9);
         }

         Locale[] locales = settings.getLocales();
         this._collators = new Collator[levels];

         for(int i = 0; i < levels; ++i) {
            this._collators[i] = this._collatorFactory.getCollator(locales[i]);
         }

         this._collator = this._collators[0];
      } else {
         this._collators = settings.getCollators();
         this._collator = this._collators[0];
      }

   }

   public final int getNode() {
      return this._node;
   }

   public final int compareDocOrder(NodeSortRecord other) {
      return this._node - other._node;
   }

   private final Comparable stringValue(int level) {
      if (this._scanned <= level) {
         AbstractTranslet translet = this._settings.getTranslet();
         Locale[] locales = this._settings.getLocales();
         String[] caseOrder = this._settings.getCaseOrders();
         String str = this.extractValueFromDOM(this._dom, this._node, level, translet, this._last);
         Comparable key = StringComparable.getComparator(str, locales[level], this._collators[level], caseOrder[level]);
         this._values[this._scanned++] = key;
         return key;
      } else {
         return (Comparable)this._values[level];
      }
   }

   private final Double numericValue(int level) {
      if (this._scanned <= level) {
         AbstractTranslet translet = this._settings.getTranslet();
         String str = this.extractValueFromDOM(this._dom, this._node, level, translet, this._last);

         Double num;
         try {
            num = new Double(str);
         } catch (NumberFormatException var6) {
            num = new Double(Double.NEGATIVE_INFINITY);
         }

         this._values[this._scanned++] = num;
         return num;
      } else {
         return (Double)this._values[level];
      }
   }

   public int compareTo(NodeSortRecord other) {
      int[] sortOrder = this._settings.getSortOrders();
      int levels = this._settings.getSortOrders().length;
      int[] compareTypes = this._settings.getTypes();

      for(int level = 0; level < levels; ++level) {
         int cmp;
         if (compareTypes[level] == 1) {
            Double our = this.numericValue(level);
            Double their = other.numericValue(level);
            cmp = our.compareTo(their);
         } else {
            Comparable our = this.stringValue(level);
            Comparable their = other.stringValue(level);
            cmp = our.compareTo(their);
         }

         if (cmp != 0) {
            return sortOrder[level] == 1 ? 0 - cmp : cmp;
         }
      }

      return this._node - other._node;
   }

   public Collator[] getCollator() {
      return this._collators;
   }

   public abstract String extractValueFromDOM(DOM var1, int var2, int var3, AbstractTranslet var4, int var5);
}
