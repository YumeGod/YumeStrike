package org.apache.fop.layoutmgr;

import java.util.List;
import java.util.Stack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.FObj;

public abstract class AbstractBaseLayoutManager implements LayoutManager, PercentBaseContext {
   protected boolean generatesReferenceArea = false;
   protected boolean generatesBlockArea = false;
   protected final FObj fobj;
   private static final Log LOG;

   public AbstractBaseLayoutManager() {
      this.fobj = null;
   }

   public AbstractBaseLayoutManager(FObj fo) {
      this.fobj = fo;
      this.setGeneratesReferenceArea(fo.generatesReferenceAreas());
      if (this.getGeneratesReferenceArea()) {
         this.setGeneratesBlockArea(true);
      }

   }

   public int getBaseLength(int lengthBase, FObj fobjx) {
      if (fobjx == this.fobj) {
         switch (lengthBase) {
            case 3:
               return this.getParentAreaIPD();
            case 4:
               return this.getReferenceAreaIPD();
            case 5:
               return this.getAncestorBlockAreaIPD();
            case 6:
               return this.getAncestorBlockAreaBPD();
            default:
               LOG.error("Unknown base type for LengthBase:" + lengthBase);
               return 0;
         }
      } else {
         LayoutManager lm;
         for(lm = this.getParent(); lm != null && fobjx != lm.getFObj(); lm = lm.getParent()) {
         }

         if (lm != null) {
            return lm.getBaseLength(lengthBase, fobjx);
         } else {
            LOG.error("Cannot find LM to handle given FO for LengthBase. (" + fobjx.getContextInfo() + ")");
            return 0;
         }
      }
   }

   protected int getAncestorBlockAreaIPD() {
      for(LayoutManager lm = this.getParent(); lm != null; lm = lm.getParent()) {
         if (lm.getGeneratesBlockArea() && !lm.getGeneratesLineArea()) {
            return lm.getContentAreaIPD();
         }
      }

      LOG.error("No parent LM found");
      return 0;
   }

   protected int getAncestorBlockAreaBPD() {
      for(LayoutManager lm = this.getParent(); lm != null; lm = lm.getParent()) {
         if (lm.getGeneratesBlockArea() && !lm.getGeneratesLineArea()) {
            return lm.getContentAreaBPD();
         }
      }

      LOG.error("No parent LM found");
      return 0;
   }

   protected int getParentAreaIPD() {
      LayoutManager lm = this.getParent();
      if (lm != null) {
         return lm.getContentAreaIPD();
      } else {
         LOG.error("No parent LM found");
         return 0;
      }
   }

   protected int getParentAreaBPD() {
      LayoutManager lm = this.getParent();
      if (lm != null) {
         return lm.getContentAreaBPD();
      } else {
         LOG.error("No parent LM found");
         return 0;
      }
   }

   public int getReferenceAreaIPD() {
      for(LayoutManager lm = this.getParent(); lm != null; lm = lm.getParent()) {
         if (lm.getGeneratesReferenceArea()) {
            return lm.getContentAreaIPD();
         }
      }

      LOG.error("No parent LM found");
      return 0;
   }

   protected int getReferenceAreaBPD() {
      for(LayoutManager lm = this.getParent(); lm != null; lm = lm.getParent()) {
         if (lm.getGeneratesReferenceArea()) {
            return lm.getContentAreaBPD();
         }
      }

      LOG.error("No parent LM found");
      return 0;
   }

   public int getContentAreaIPD() {
      throw new UnsupportedOperationException("getContentAreaIPD() called when it should have been overridden");
   }

   public int getContentAreaBPD() {
      throw new UnsupportedOperationException("getContentAreaBPD() called when it should have been overridden");
   }

   public boolean getGeneratesReferenceArea() {
      return this.generatesReferenceArea;
   }

   protected void setGeneratesReferenceArea(boolean generatesReferenceArea) {
      this.generatesReferenceArea = generatesReferenceArea;
   }

   public boolean getGeneratesBlockArea() {
      return this.generatesBlockArea;
   }

   protected void setGeneratesBlockArea(boolean generatesBlockArea) {
      this.generatesBlockArea = generatesBlockArea;
   }

   public boolean getGeneratesLineArea() {
      return false;
   }

   public FObj getFObj() {
      return this.fobj;
   }

   public void reset() {
      throw new UnsupportedOperationException("Not implemented");
   }

   public boolean isRestartable() {
      return false;
   }

   public List getNextKnuthElements(LayoutContext context, int alignment, Stack lmStack, Position positionAtIPDChange, LayoutManager restartAtLM) {
      throw new UnsupportedOperationException("Not implemented");
   }

   static {
      LOG = LogFactory.getLog(AbstractBaseLayoutManager.class);
   }
}
