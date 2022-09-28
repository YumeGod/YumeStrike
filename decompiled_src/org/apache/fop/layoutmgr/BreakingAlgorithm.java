package org.apache.fop.layoutmgr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class BreakingAlgorithm {
   protected static Log log;
   protected static final int INFINITE_RATIO = 1000;
   private static final int MAX_RECOVERY_ATTEMPTS = 5;
   public static final int ALL_BREAKS = 0;
   public static final int NO_FLAGGED_PENALTIES = 1;
   public static final int ONLY_FORCED_BREAKS = 2;
   protected int repeatedFlaggedDemerit = 50;
   protected int incompatibleFitnessDemerit = 50;
   protected int maxFlaggedPenaltiesCount;
   private double threshold;
   protected KnuthSequence par;
   protected int lineWidth = -1;
   private boolean force = false;
   protected boolean considerTooShort = false;
   private KnuthNode lastTooLong;
   private KnuthNode lastTooShort;
   private KnuthNode lastDeactivated;
   protected int alignment;
   protected int alignmentLast;
   protected boolean indentFirstPart;
   protected KnuthNode[] activeLines;
   protected int activeNodeCount;
   protected int startLine = 0;
   protected int endLine = 0;
   protected int totalWidth;
   protected int totalStretch = 0;
   protected int totalShrink = 0;
   protected BestRecords best;
   private boolean partOverflowRecoveryActivated = true;
   private KnuthNode lastRecovered;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public BreakingAlgorithm(int align, int alignLast, boolean first, boolean partOverflowRecovery, int maxFlagCount) {
      this.alignment = align;
      this.alignmentLast = alignLast;
      this.indentFirstPart = first;
      this.partOverflowRecoveryActivated = partOverflowRecovery;
      this.best = new BestRecords();
      this.maxFlaggedPenaltiesCount = maxFlagCount;
   }

   protected int getMaxRecoveryAttempts() {
      return 5;
   }

   protected boolean isPartOverflowRecoveryActivated() {
      return this.partOverflowRecoveryActivated;
   }

   public abstract void updateData1(int var1, double var2);

   public abstract void updateData2(KnuthNode var1, KnuthSequence var2, int var3);

   public void setConstantLineWidth(int lineWidth) {
      this.lineWidth = lineWidth;
   }

   public int findBreakingPoints(KnuthSequence par, double threshold, boolean force, int allowedBreaks) {
      return this.findBreakingPoints(par, 0, threshold, force, allowedBreaks);
   }

   public int findBreakingPoints(KnuthSequence par, int startIndex, double threshold, boolean force, int allowedBreaks) {
      this.par = par;
      this.threshold = threshold;
      this.force = force;
      this.initialize();
      boolean previousIsBox = false;
      int firstBoxIndex = startIndex;
      if (this.alignment != 23) {
         firstBoxIndex = par.getFirstBoxIndex(startIndex);
      }

      firstBoxIndex = firstBoxIndex < 0 ? 0 : firstBoxIndex;
      this.addNode(0, this.createNode(firstBoxIndex, 0, 1, 0, 0, 0, 0.0, 0, 0, 0, 0.0, (KnuthNode)null));
      KnuthNode lastForced = this.getNode(0);
      if (log.isTraceEnabled()) {
         log.trace("Looping over " + (par.size() - startIndex) + " elements");
         log.trace(par);
      }

      int elementIndex;
      for(elementIndex = startIndex; elementIndex < par.size(); ++elementIndex) {
         previousIsBox = this.handleElementAt(elementIndex, previousIsBox, allowedBreaks).isBox();
         if (this.activeNodeCount == 0) {
            if (this.getIPDdifference() != 0) {
               return this.handleIpdChange();
            }

            if (!force) {
               log.debug("Could not find a set of breaking points " + threshold);
               return 0;
            }

            if (this.lastDeactivated != null && this.lastDeactivated != lastForced) {
               this.replaceLastDeactivated();
            }

            if (this.lastTooShort != null && lastForced.position != this.lastTooShort.position) {
               lastForced = this.lastTooShort;
               this.lastRecovered = null;
            } else {
               lastForced = this.recoverFromOverflow();
            }

            elementIndex = this.restartFrom(lastForced, elementIndex);
         }
      }

      this.finish();
      elementIndex = this.filterActiveNodes();

      for(int i = this.startLine; i < this.endLine; ++i) {
         for(KnuthNode node = this.getNode(i); node != null; node = node.next) {
            this.updateData1(node.line, node.totalDemerits);
            this.calculateBreakPoints(node, par, node.line);
         }
      }

      this.activeLines = null;
      return elementIndex;
   }

   protected int getIPDdifference() {
      return 0;
   }

   protected int handleIpdChange() {
      throw new IllegalStateException();
   }

   protected KnuthNode recoverFromTooLong(KnuthNode lastTooLong) {
      if (log.isDebugEnabled()) {
         log.debug("Recovering from too long: " + lastTooLong);
      }

      return this.createNode(lastTooLong.previous.position, lastTooLong.previous.line + 1, 1, 0, 0, 0, 0.0, 0, 0, 0, 0.0, lastTooLong.previous);
   }

   protected void initialize() {
      this.totalWidth = 0;
      this.totalStretch = 0;
      this.totalShrink = 0;
      this.lastTooShort = this.lastTooLong = null;
      this.startLine = this.endLine = 0;
      this.activeLines = new KnuthNode[20];
   }

   protected KnuthNode createNode(int position, int line, int fitness, int totalWidth, int totalStretch, int totalShrink, double adjustRatio, int availableShrink, int availableStretch, int difference, double totalDemerits, KnuthNode previous) {
      return new KnuthNode(position, line, fitness, totalWidth, totalStretch, totalShrink, adjustRatio, availableShrink, availableStretch, difference, totalDemerits, previous);
   }

   protected KnuthNode createNode(int position, int line, int fitness, int totalWidth, int totalStretch, int totalShrink) {
      return new KnuthNode(position, line, fitness, totalWidth, totalStretch, totalShrink, this.best.getAdjust(fitness), this.best.getAvailableShrink(fitness), this.best.getAvailableStretch(fitness), this.best.getDifference(fitness), this.best.getDemerits(fitness), this.best.getNode(fitness));
   }

   protected final KnuthNode getLastTooShort() {
      return this.lastTooShort;
   }

   protected final KnuthElement handleElementAt(int position, boolean previousIsBox, int allowedBreaks) {
      KnuthElement element = this.getElement(position);
      if (element.isBox()) {
         this.handleBox((KnuthBox)element);
      } else if (element.isGlue()) {
         this.handleGlueAt((KnuthGlue)element, position, previousIsBox, allowedBreaks);
      } else {
         if (!element.isPenalty()) {
            throw new IllegalArgumentException("Unknown KnuthElement type: expecting KnuthBox, KnuthGlue or KnuthPenalty");
         }

         this.handlePenaltyAt((KnuthPenalty)element, position, allowedBreaks);
      }

      return element;
   }

   protected void handleBox(KnuthBox box) {
      this.totalWidth += box.getWidth();
   }

   protected void handleGlueAt(KnuthGlue glue, int position, boolean previousIsBox, int allowedBreaks) {
      if (previousIsBox && allowedBreaks != 2) {
         this.considerLegalBreak(glue, position);
      }

      this.totalWidth += glue.getWidth();
      this.totalStretch += glue.getStretch();
      this.totalShrink += glue.getShrink();
   }

   protected void handlePenaltyAt(KnuthPenalty penalty, int position, int allowedBreaks) {
      if (penalty.getPenalty() < 1000 && (allowedBreaks != 1 || !penalty.isPenaltyFlagged()) && (allowedBreaks != 2 || penalty.isForcedBreak())) {
         this.considerLegalBreak(penalty, position);
      }

   }

   protected final void replaceLastDeactivated() {
      if (this.lastDeactivated.adjustRatio > 0.0) {
         this.lastTooShort = this.lastDeactivated;
      } else {
         this.lastTooLong = this.lastDeactivated;
      }

   }

   protected KnuthNode recoverFromOverflow() {
      KnuthNode lastForced;
      if (this.isPartOverflowRecoveryActivated()) {
         if (this.lastRecovered == null) {
            this.lastRecovered = this.lastTooLong;
            if (log.isDebugEnabled()) {
               log.debug("Recovery point: " + this.lastRecovered);
            }
         }

         KnuthNode node = this.recoverFromTooLong(this.lastTooLong);
         lastForced = node;
         node.fitRecoveryCounter = this.lastTooLong.previous.fitRecoveryCounter + 1;
         if (log.isDebugEnabled()) {
            log.debug("first part doesn't fit into line, recovering: " + node.fitRecoveryCounter);
         }

         if (node.fitRecoveryCounter > this.getMaxRecoveryAttempts()) {
            while(lastForced.fitRecoveryCounter > 0 && lastForced.previous != null) {
               lastForced = lastForced.previous;
               this.lastDeactivated = lastForced.previous;
            }

            lastForced = this.lastRecovered;
            this.lastRecovered = null;
            this.startLine = lastForced.line;
            this.endLine = lastForced.line;
            log.debug("rolled back...");
         }
      } else {
         lastForced = this.lastTooLong;
      }

      return lastForced;
   }

   protected int restartFrom(KnuthNode restartingNode, int currentIndex) {
      if (log.isDebugEnabled()) {
         log.debug("Restarting at node " + restartingNode);
      }

      restartingNode.totalDemerits = 0.0;
      this.addNode(restartingNode.line, restartingNode);
      this.startLine = restartingNode.line;
      this.endLine = this.startLine + 1;
      this.totalWidth = restartingNode.totalWidth;
      this.totalStretch = restartingNode.totalStretch;
      this.totalShrink = restartingNode.totalShrink;
      this.lastTooShort = null;
      this.lastTooLong = null;

      int restartingIndex;
      for(restartingIndex = restartingNode.position; restartingIndex + 1 < this.par.size() && !this.getElement(restartingIndex + 1).isBox(); ++restartingIndex) {
      }

      return restartingIndex;
   }

   protected void considerLegalBreak(KnuthElement element, int elementIdx) {
      if (log.isTraceEnabled()) {
         log.trace("considerLegalBreak() at " + elementIdx + " (" + this.totalWidth + "+" + this.totalStretch + "-" + this.totalShrink + "), parts/lines: " + this.startLine + "-" + this.endLine);
         log.trace("\tCurrent active node list: " + this.activeNodeCount + " " + this.toString("\t"));
      }

      this.lastDeactivated = null;
      this.lastTooLong = null;

      for(int line = this.startLine; line < this.endLine; ++line) {
         for(KnuthNode node = this.getNode(line); node != null; node = node.next) {
            if (node.position != elementIdx) {
               int difference = this.computeDifference(node, element, elementIdx);
               if (!this.elementCanEndLine(element, this.endLine, difference)) {
                  log.trace("Skipping legal break");
                  break;
               }

               double r = this.computeAdjustmentRatio(node, difference);
               int availableShrink = this.totalShrink - node.totalShrink;
               int availableStretch = this.totalStretch - node.totalStretch;
               if (log.isTraceEnabled()) {
                  log.trace("\tr=" + r + " difference=" + difference);
                  log.trace("\tline=" + line);
               }

               if (r < -1.0 || element.isForcedBreak()) {
                  this.deactivateNode(node, line);
               }

               int fitnessClass = BreakingAlgorithm.FitnessClasses.computeFitness(r);
               double demerits = this.computeDemerits(node, element, fitnessClass, r);
               if (r >= -1.0 && r <= this.threshold) {
                  this.activateNode(node, difference, r, demerits, fitnessClass, availableShrink, availableStretch);
               }

               if (this.force && (r <= -1.0 || r > this.threshold)) {
                  this.forceNode(node, line, elementIdx, difference, r, demerits, fitnessClass, availableShrink, availableStretch);
               }
            }
         }

         this.addBreaks(line, elementIdx);
      }

   }

   protected boolean elementCanEndLine(KnuthElement element, int line, int difference) {
      return !element.isPenalty() || element.getPenalty() < 1000;
   }

   protected void forceNode(KnuthNode node, int line, int elementIdx, int difference, double r, double demerits, int fitnessClass, int availableShrink, int availableStretch) {
      int newWidth = this.totalWidth;
      int newStretch = this.totalStretch;
      int newShrink = this.totalShrink;

      for(int i = elementIdx; i < this.par.size(); ++i) {
         KnuthElement tempElement = this.getElement(i);
         if (tempElement.isBox()) {
            break;
         }

         if (tempElement.isGlue()) {
            newWidth += tempElement.getWidth();
            newStretch += tempElement.getStretch();
            newShrink += tempElement.getShrink();
         } else if (tempElement.isForcedBreak() && i != elementIdx) {
            break;
         }
      }

      if (r <= -1.0) {
         log.debug("Considering tooLong, demerits=" + demerits);
         if (this.lastTooLong == null || demerits < this.lastTooLong.totalDemerits) {
            this.lastTooLong = this.createNode(elementIdx, line + 1, fitnessClass, newWidth, newStretch, newShrink, r, availableShrink, availableStretch, difference, demerits, node);
            if (log.isTraceEnabled()) {
               log.trace("Picking tooLong " + this.lastTooLong);
            }
         }
      } else if (this.lastTooShort == null || demerits <= this.lastTooShort.totalDemerits) {
         if (this.considerTooShort) {
            this.best.addRecord(demerits, node, r, availableShrink, availableStretch, difference, fitnessClass);
         }

         this.lastTooShort = this.createNode(elementIdx, line + 1, fitnessClass, newWidth, newStretch, newShrink, r, availableShrink, availableStretch, difference, demerits, node);
         if (log.isTraceEnabled()) {
            log.trace("Picking tooShort " + this.lastTooShort);
         }
      }

   }

   protected void activateNode(KnuthNode node, int difference, double r, double demerits, int fitnessClass, int availableShrink, int availableStretch) {
      if (log.isTraceEnabled()) {
         log.trace("\tDemerits=" + demerits);
         log.trace("\tFitness class=" + BreakingAlgorithm.FitnessClasses.NAMES[fitnessClass]);
      }

      if (demerits < this.best.getDemerits(fitnessClass)) {
         this.best.addRecord(demerits, node, r, availableShrink, availableStretch, difference, fitnessClass);
         this.lastTooShort = null;
      }

   }

   protected void deactivateNode(KnuthNode node, int line) {
      if (log.isTraceEnabled()) {
         log.trace("Removing " + node);
      }

      this.removeNode(line, node);
      this.lastDeactivated = this.compareNodes(this.lastDeactivated, node);
   }

   private void addBreaks(int line, int elementIdx) {
      if (this.best.hasRecords()) {
         int newWidth = this.totalWidth;
         int newStretch = this.totalStretch;
         int newShrink = this.totalShrink;

         for(int i = elementIdx; i < this.par.size(); ++i) {
            KnuthElement tempElement = this.getElement(i);
            if (tempElement.isBox()) {
               break;
            }

            if (tempElement.isGlue()) {
               newWidth += tempElement.getWidth();
               newStretch += tempElement.getStretch();
               newShrink += tempElement.getShrink();
            } else if (tempElement.isForcedBreak() && i != elementIdx) {
               break;
            }
         }

         double minimumDemerits = this.best.getMinDemerits() + (double)this.incompatibleFitnessDemerit;

         for(int i = 0; i <= 3; ++i) {
            if (this.best.notInfiniteDemerits(i) && this.best.getDemerits(i) <= minimumDemerits) {
               if (log.isTraceEnabled()) {
                  log.trace("\tInsert new break in list of " + this.activeNodeCount + " from fitness class " + BreakingAlgorithm.FitnessClasses.NAMES[i]);
               }

               KnuthNode newNode = this.createNode(elementIdx, line + 1, i, newWidth, newStretch, newShrink);
               this.addNode(line + 1, newNode);
            }
         }

         this.best.reset();
      }
   }

   protected int computeDifference(KnuthNode activeNode, KnuthElement element, int elementIndex) {
      int actualWidth = this.totalWidth - activeNode.totalWidth;
      if (element.isPenalty()) {
         actualWidth += element.getWidth();
      }

      return this.getLineWidth() - actualWidth;
   }

   protected double computeAdjustmentRatio(KnuthNode activeNode, int difference) {
      int maxAdjustment;
      if (difference > 0) {
         maxAdjustment = this.totalStretch - activeNode.totalStretch;
         return maxAdjustment > 0 ? (double)difference / (double)maxAdjustment : 1000.0;
      } else if (difference < 0) {
         maxAdjustment = this.totalShrink - activeNode.totalShrink;
         return maxAdjustment > 0 ? (double)difference / (double)maxAdjustment : -1000.0;
      } else {
         return 0.0;
      }
   }

   protected double computeDemerits(KnuthNode activeNode, KnuthElement element, int fitnessClass, double r) {
      double demerits = 0.0;
      double f = Math.abs(r);
      f = 1.0 + 100.0 * f * f * f;
      if (element.isPenalty()) {
         double penalty = (double)element.getPenalty();
         if (penalty >= 0.0) {
            f += penalty;
            demerits = f * f;
         } else if (!element.isForcedBreak()) {
            demerits = f * f - penalty * penalty;
         } else {
            demerits = f * f;
         }
      } else {
         demerits = f * f;
      }

      if (element.isPenalty() && ((KnuthPenalty)element).isPenaltyFlagged() && this.getElement(activeNode.position).isPenalty() && ((KnuthPenalty)this.getElement(activeNode.position)).isPenaltyFlagged()) {
         demerits += (double)this.repeatedFlaggedDemerit;
         int flaggedPenaltiesCount = 2;

         for(KnuthNode prevNode = activeNode.previous; prevNode != null && flaggedPenaltiesCount <= this.maxFlaggedPenaltiesCount; prevNode = prevNode.previous) {
            KnuthElement prevElement = this.getElement(prevNode.position);
            if (!prevElement.isPenalty() || !((KnuthPenalty)prevElement).isPenaltyFlagged()) {
               break;
            }

            ++flaggedPenaltiesCount;
         }

         if (this.maxFlaggedPenaltiesCount >= 1 && flaggedPenaltiesCount > this.maxFlaggedPenaltiesCount) {
            demerits += Double.POSITIVE_INFINITY;
         }
      }

      if (Math.abs(fitnessClass - activeNode.fitness) > 1) {
         demerits += (double)this.incompatibleFitnessDemerit;
      }

      demerits += activeNode.totalDemerits;
      return demerits;
   }

   protected void finish() {
      if (log.isTraceEnabled()) {
         log.trace("Main loop completed " + this.activeNodeCount);
         log.trace("Active nodes=" + this.toString(""));
      }

   }

   protected KnuthElement getElement(int idx) {
      return (KnuthElement)this.par.get(idx);
   }

   protected KnuthNode compareNodes(KnuthNode node1, KnuthNode node2) {
      if (node1 != null && node2.position <= node1.position) {
         return node2.position == node1.position && node2.totalDemerits < node1.totalDemerits ? node2 : node1;
      } else {
         return node2;
      }
   }

   protected void addNode(int line, KnuthNode node) {
      int headIdx = line * 2;
      if (headIdx >= this.activeLines.length) {
         KnuthNode[] oldList = this.activeLines;
         this.activeLines = new KnuthNode[headIdx + headIdx];
         System.arraycopy(oldList, 0, this.activeLines, 0, oldList.length);
      }

      node.next = null;
      if (this.activeLines[headIdx + 1] != null) {
         this.activeLines[headIdx + 1].next = node;
      } else {
         this.activeLines[headIdx] = node;
         this.endLine = line + 1;
      }

      this.activeLines[headIdx + 1] = node;
      ++this.activeNodeCount;
   }

   protected void removeNode(int line, KnuthNode node) {
      int headIdx = line * 2;
      KnuthNode n = this.getNode(line);
      if (n != node) {
         KnuthNode prevNode;
         for(prevNode = null; n != node; n = n.next) {
            prevNode = n;
         }

         prevNode.next = n.next;
         if (prevNode.next == null) {
            this.activeLines[headIdx + 1] = prevNode;
         }
      } else {
         this.activeLines[headIdx] = node.next;
         if (node.next == null) {
            this.activeLines[headIdx + 1] = null;
         }

         while(this.startLine < this.endLine && this.getNode(this.startLine) == null) {
            ++this.startLine;
         }
      }

      --this.activeNodeCount;
   }

   protected KnuthNode getNode(int line) {
      return this.activeLines[line * 2];
   }

   protected int getLineWidth(int line) {
      if (!$assertionsDisabled && this.lineWidth < 0) {
         throw new AssertionError();
      } else {
         return this.lineWidth;
      }
   }

   protected int getLineWidth() {
      return this.lineWidth;
   }

   public String toString(String prepend) {
      StringBuffer sb = new StringBuffer();
      sb.append("[\n");

      for(int i = this.startLine; i < this.endLine; ++i) {
         for(KnuthNode node = this.getNode(i); node != null; node = node.next) {
            sb.append(prepend).append('\t').append(node).append(",\n");
         }
      }

      sb.append(prepend).append("]");
      return sb.toString();
   }

   protected abstract int filterActiveNodes();

   protected void calculateBreakPoints(KnuthNode node, KnuthSequence par, int total) {
      KnuthNode bestActiveNode = node;

      for(int i = node.line; i > 0; --i) {
         this.updateData2(bestActiveNode, par, total);
         bestActiveNode = bestActiveNode.previous;
      }

   }

   public int getAlignment() {
      return this.alignment;
   }

   public int getAlignmentLast() {
      return this.alignmentLast;
   }

   static {
      $assertionsDisabled = !BreakingAlgorithm.class.desiredAssertionStatus();
      log = LogFactory.getLog(BreakingAlgorithm.class);
   }

   protected class BestRecords {
      private static final double INFINITE_DEMERITS = Double.POSITIVE_INFINITY;
      private double[] bestDemerits = new double[4];
      private KnuthNode[] bestNode = new KnuthNode[4];
      private double[] bestAdjust = new double[4];
      private int[] bestDifference = new int[4];
      private int[] bestAvailableShrink = new int[4];
      private int[] bestAvailableStretch = new int[4];
      private int bestIndex = -1;

      public BestRecords() {
         this.reset();
      }

      public void addRecord(double demerits, KnuthNode node, double adjust, int availableShrink, int availableStretch, int difference, int fitness) {
         if (demerits > this.bestDemerits[fitness]) {
            BreakingAlgorithm.log.error("New demerits value greater than the old one");
         }

         this.bestDemerits[fitness] = demerits;
         this.bestNode[fitness] = node;
         this.bestAdjust[fitness] = adjust;
         this.bestAvailableShrink[fitness] = availableShrink;
         this.bestAvailableStretch[fitness] = availableStretch;
         this.bestDifference[fitness] = difference;
         if (this.bestIndex == -1 || demerits < this.bestDemerits[this.bestIndex]) {
            this.bestIndex = fitness;
         }

      }

      public boolean hasRecords() {
         return this.bestIndex != -1;
      }

      public boolean notInfiniteDemerits(int fitness) {
         return this.bestDemerits[fitness] != Double.POSITIVE_INFINITY;
      }

      public double getDemerits(int fitness) {
         return this.bestDemerits[fitness];
      }

      public KnuthNode getNode(int fitness) {
         return this.bestNode[fitness];
      }

      public double getAdjust(int fitness) {
         return this.bestAdjust[fitness];
      }

      public int getAvailableShrink(int fitness) {
         return this.bestAvailableShrink[fitness];
      }

      public int getAvailableStretch(int fitness) {
         return this.bestAvailableStretch[fitness];
      }

      public int getDifference(int fitness) {
         return this.bestDifference[fitness];
      }

      public double getMinDemerits() {
         return this.bestIndex != -1 ? this.getDemerits(this.bestIndex) : Double.POSITIVE_INFINITY;
      }

      public void reset() {
         for(int i = 0; i < 4; ++i) {
            this.bestDemerits[i] = Double.POSITIVE_INFINITY;
         }

         this.bestIndex = -1;
      }
   }

   public class KnuthNode {
      public final int position;
      public final int line;
      public final int fitness;
      public final int totalWidth;
      public final int totalStretch;
      public final int totalShrink;
      public final double adjustRatio;
      public final int availableShrink;
      public final int availableStretch;
      public final int difference;
      public double totalDemerits;
      public KnuthNode previous;
      public KnuthNode next;
      public int fitRecoveryCounter = 0;

      public KnuthNode(int position, int line, int fitness, int totalWidth, int totalStretch, int totalShrink, double adjustRatio, int availableShrink, int availableStretch, int difference, double totalDemerits, KnuthNode previous) {
         this.position = position;
         this.line = line;
         this.fitness = fitness;
         this.totalWidth = totalWidth;
         this.totalStretch = totalStretch;
         this.totalShrink = totalShrink;
         this.adjustRatio = adjustRatio;
         this.availableShrink = availableShrink;
         this.availableStretch = availableStretch;
         this.difference = difference;
         this.totalDemerits = totalDemerits;
         this.previous = previous;
      }

      public String toString() {
         return "<KnuthNode at " + this.position + " " + this.totalWidth + "+" + this.totalStretch + "-" + this.totalShrink + " line:" + this.line + " prev:" + (this.previous != null ? this.previous.position : -1) + " dem:" + this.totalDemerits + " fitness:" + BreakingAlgorithm.FitnessClasses.NAMES[this.fitness] + ">";
      }
   }

   static final class FitnessClasses {
      static final int VERY_TIGHT = 0;
      static final int TIGHT = 1;
      static final int LOOSE = 2;
      static final int VERY_LOOSE = 3;
      static final String[] NAMES = new String[]{"VERY TIGHT", "TIGHT", "LOOSE", "VERY LOOSE"};

      static int computeFitness(double adjustRatio) {
         if (adjustRatio < -0.5) {
            return 0;
         } else if (adjustRatio <= 0.5) {
            return 1;
         } else {
            return adjustRatio <= 1.0 ? 2 : 3;
         }
      }
   }
}
