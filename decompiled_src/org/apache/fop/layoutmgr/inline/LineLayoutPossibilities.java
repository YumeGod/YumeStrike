package org.apache.fop.layoutmgr.inline;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.layoutmgr.Position;

public class LineLayoutPossibilities {
   protected static Log log;
   private List possibilitiesList = new ArrayList();
   private List savedPossibilities = new ArrayList();
   private int minimumIndex;
   private int optimumIndex = -1;
   private int maximumIndex;
   private int chosenIndex;
   private int savedOptLineCount;

   public void addPossibility(int ln, double dem) {
      this.possibilitiesList.add(new Possibility(ln, dem));
      if (this.possibilitiesList.size() == 1) {
         this.minimumIndex = 0;
         this.optimumIndex = 0;
         this.maximumIndex = 0;
         this.chosenIndex = 0;
      } else {
         if (dem < ((Possibility)this.possibilitiesList.get(this.optimumIndex)).getDemerits()) {
            this.optimumIndex = this.possibilitiesList.size() - 1;
            this.chosenIndex = this.optimumIndex;
         }

         if (ln < ((Possibility)this.possibilitiesList.get(this.minimumIndex)).getLineCount()) {
            this.minimumIndex = this.possibilitiesList.size() - 1;
         }

         if (ln > ((Possibility)this.possibilitiesList.get(this.maximumIndex)).getLineCount()) {
            this.maximumIndex = this.possibilitiesList.size() - 1;
         }
      }

   }

   public void savePossibilities(boolean bSaveOptLineCount) {
      if (bSaveOptLineCount) {
         this.savedOptLineCount = this.getOptLineCount();
      } else {
         this.savedOptLineCount = 0;
      }

      this.savedPossibilities = this.possibilitiesList;
      this.possibilitiesList = new ArrayList();
   }

   public void restorePossibilities() {
      int index = 0;

      while(true) {
         Possibility restoredPossibility;
         do {
            if (this.savedPossibilities.size() <= 0) {
               return;
            }

            restoredPossibility = (Possibility)this.savedPossibilities.remove(0);
            if (restoredPossibility.getLineCount() < this.getMinLineCount()) {
               this.possibilitiesList.add(0, restoredPossibility);
               this.minimumIndex = 0;
               ++this.optimumIndex;
               ++this.maximumIndex;
               ++this.chosenIndex;
            } else if (restoredPossibility.getLineCount() > this.getMaxLineCount()) {
               this.possibilitiesList.add(this.possibilitiesList.size(), restoredPossibility);
               this.maximumIndex = this.possibilitiesList.size() - 1;
               index = this.maximumIndex;
            } else {
               while(index < this.maximumIndex && this.getLineCount(index) < restoredPossibility.getLineCount()) {
                  ++index;
               }

               if (this.getLineCount(index) != restoredPossibility.getLineCount()) {
                  log.error("LineLayoutPossibilities restorePossibilities(), min= " + this.getMinLineCount() + " max= " + this.getMaxLineCount() + " restored= " + restoredPossibility.getLineCount());
                  return;
               }

               this.possibilitiesList.set(index, restoredPossibility);
            }
         } while((this.savedOptLineCount != 0 || !(this.getDemerits(this.optimumIndex) > restoredPossibility.getDemerits())) && (this.savedOptLineCount == 0 || restoredPossibility.getLineCount() != this.savedOptLineCount));

         this.optimumIndex = index;
         this.chosenIndex = this.optimumIndex;
      }
   }

   public void addBreakPosition(Position pos, int i) {
      ((Possibility)this.possibilitiesList.get(i)).addBreakPosition(pos);
   }

   public boolean canUseMoreLines() {
      return this.getOptLineCount() < this.getMaxLineCount();
   }

   public boolean canUseLessLines() {
      return this.getMinLineCount() < this.getOptLineCount();
   }

   public int getMinLineCount() {
      return this.getLineCount(this.minimumIndex);
   }

   public int getOptLineCount() {
      return this.getLineCount(this.optimumIndex);
   }

   public int getMaxLineCount() {
      return this.getLineCount(this.maximumIndex);
   }

   public int getChosenLineCount() {
      return this.getLineCount(this.chosenIndex);
   }

   public int getLineCount(int i) {
      return ((Possibility)this.possibilitiesList.get(i)).getLineCount();
   }

   public double getChosenDemerits() {
      return this.getDemerits(this.chosenIndex);
   }

   public double getDemerits(int i) {
      return ((Possibility)this.possibilitiesList.get(i)).getDemerits();
   }

   public int getPossibilitiesNumber() {
      return this.possibilitiesList.size();
   }

   public Position getChosenPosition(int i) {
      return ((Possibility)this.possibilitiesList.get(this.chosenIndex)).getBreakPosition(i);
   }

   public int applyLineCountAdjustment(int adj) {
      if (adj >= this.getMinLineCount() - this.getChosenLineCount() && adj <= this.getMaxLineCount() - this.getChosenLineCount() && this.getLineCount(this.chosenIndex + adj) == this.getChosenLineCount() + adj) {
         this.chosenIndex += adj;
         log.debug("chosenLineCount= " + (this.getChosenLineCount() - adj) + " adjustment= " + adj + " => chosenLineCount= " + this.getLineCount(this.chosenIndex));
         return adj;
      } else {
         log.warn("Cannot apply the desired line count adjustment.");
         return 0;
      }
   }

   public void printAll() {
      System.out.println("++++++++++");
      System.out.println(" " + this.possibilitiesList.size() + " possibility':");

      for(int i = 0; i < this.possibilitiesList.size(); ++i) {
         System.out.println("   " + ((Possibility)this.possibilitiesList.get(i)).getLineCount() + (i == this.optimumIndex ? " *" : "") + (i == this.minimumIndex ? " -" : "") + (i == this.maximumIndex ? " +" : ""));
      }

      System.out.println("++++++++++");
   }

   static {
      log = LogFactory.getLog(LineLayoutPossibilities.class);
   }

   private class Possibility {
      private int lineCount;
      private double demerits;
      private List breakPositions;

      private Possibility(int lc, double dem) {
         this.lineCount = lc;
         this.demerits = dem;
         this.breakPositions = new ArrayList(lc);
      }

      private int getLineCount() {
         return this.lineCount;
      }

      private double getDemerits() {
         return this.demerits;
      }

      private void addBreakPosition(Position pos) {
         this.breakPositions.add(0, pos);
      }

      private Position getBreakPosition(int i) {
         return (Position)this.breakPositions.get(i);
      }

      // $FF: synthetic method
      Possibility(int x1, double x2, Object x3) {
         this(x1, x2);
      }
   }
}
