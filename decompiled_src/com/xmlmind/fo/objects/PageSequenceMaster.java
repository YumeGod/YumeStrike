package com.xmlmind.fo.objects;

import com.xmlmind.fo.properties.Value;
import java.util.Vector;

public class PageSequenceMaster {
   public String masterName;
   public Vector subSequences = new Vector();

   public PageSequenceMaster(Value[] var1) {
      Value var2 = var1[178];
      if (var2 != null) {
         this.masterName = var2.name();
      }

   }

   public PageMasterReference subSequence(int var1) {
      return (PageMasterReference)this.subSequences.elementAt(var1);
   }
}
