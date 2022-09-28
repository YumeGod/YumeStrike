package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;

public class MultiCase extends FObj {
   private int startingState;
   static boolean notImplementedWarningGiven = false;

   public MultiCase(FONode parent) {
      super(parent);
      if (!notImplementedWarningGiven) {
         this.getFOValidationEventProducer().unimplementedFeature(this, this.getName(), this.getName(), this.getLocator());
         notImplementedWarningGiven = true;
      }

   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.startingState = pList.get(234).getEnum();
   }

   public int getStartingState() {
      return this.startingState;
   }

   public String getLocalName() {
      return "multi-case";
   }

   public int getNameId() {
      return 45;
   }
}
