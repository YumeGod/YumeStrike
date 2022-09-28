package org.apache.fop.layoutmgr.inline;

public interface ScaledBaselineTable {
   int getDominantBaselineIdentifier();

   int getWritingMode();

   int getBaseline(int var1);

   void setBeforeAndAfterBaselines(int var1, int var2);

   ScaledBaselineTable deriveScaledBaselineTable(int var1);
}
