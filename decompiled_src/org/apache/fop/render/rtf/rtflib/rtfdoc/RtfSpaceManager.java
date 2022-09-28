package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.util.Iterator;
import java.util.LinkedList;

public class RtfSpaceManager {
   private LinkedList blockAttributes = new LinkedList();
   private LinkedList inlineAttributes = new LinkedList();
   private int accumulatedSpace = 0;

   public void stopUpdatingSpaceBefore() {
      Iterator it = this.blockAttributes.iterator();

      while(it.hasNext()) {
         RtfSpaceSplitter splitter = (RtfSpaceSplitter)it.next();
         if (splitter.isBeforeCadidateSet()) {
            splitter.stopUpdatingSpaceBefore();
         }
      }

   }

   public void setCandidate(RtfAttributes attrs) {
      Iterator it = this.blockAttributes.iterator();

      while(it.hasNext()) {
         RtfSpaceSplitter splitter = (RtfSpaceSplitter)it.next();
         splitter.setSpaceBeforeCandidate(attrs);
         splitter.setSpaceAfterCandidate(attrs);
      }

   }

   public RtfSpaceSplitter pushRtfSpaceSplitter(RtfAttributes attrs) {
      RtfSpaceSplitter splitter = new RtfSpaceSplitter(attrs, this.accumulatedSpace);
      this.accumulatedSpace = 0;
      this.blockAttributes.addLast(splitter);
      return splitter;
   }

   public void popRtfSpaceSplitter() {
      if (!this.blockAttributes.isEmpty()) {
         RtfSpaceSplitter splitter = (RtfSpaceSplitter)this.blockAttributes.removeLast();
         this.accumulatedSpace += splitter.flush();
      }

   }

   public void pushInlineAttributes(RtfAttributes attrs) {
      this.inlineAttributes.addLast(attrs);
   }

   public void popInlineAttributes() {
      if (!this.inlineAttributes.isEmpty()) {
         this.inlineAttributes.removeLast();
      }

   }

   public RtfAttributes getLastInlineAttribute() {
      return (RtfAttributes)this.inlineAttributes.getLast();
   }
}
