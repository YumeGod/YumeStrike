package org.apache.fop.fo.expr;

import java.util.Stack;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.PercentBase;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.properties.PropertyMaker;

public class PropertyInfo {
   private PropertyMaker maker;
   private PropertyList plist;
   private FObj fo;
   private Stack stkFunction;

   public PropertyInfo(PropertyMaker maker, PropertyList plist) {
      this.maker = maker;
      this.plist = plist;
      this.fo = plist.getParentFObj();
   }

   public PercentBase getPercentBase() throws PropertyException {
      PercentBase pcbase = this.getFunctionPercentBase();
      return pcbase != null ? pcbase : this.maker.getPercentBase(this.plist);
   }

   public Length currentFontSize() throws PropertyException {
      return this.plist.get(103).getLength();
   }

   public FObj getFO() {
      return this.fo;
   }

   public PropertyList getPropertyList() {
      return this.plist;
   }

   public PropertyMaker getPropertyMaker() {
      return this.maker;
   }

   public void pushFunction(Function func) {
      if (this.stkFunction == null) {
         this.stkFunction = new Stack();
      }

      this.stkFunction.push(func);
   }

   public void popFunction() {
      if (this.stkFunction != null) {
         this.stkFunction.pop();
      }

   }

   protected FOUserAgent getUserAgent() {
      return this.plist.getFObj() != null ? this.plist.getFObj().getUserAgent() : null;
   }

   private PercentBase getFunctionPercentBase() {
      if (this.stkFunction != null) {
         Function f = (Function)this.stkFunction.peek();
         if (f != null) {
            return f.getPercentBase();
         }
      }

      return null;
   }
}
