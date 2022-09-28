package org.apache.xmlgraphics.ps.dsc;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.PSResource;
import org.apache.xmlgraphics.ps.dsc.events.DSCCommentDocumentNeededResources;
import org.apache.xmlgraphics.ps.dsc.events.DSCCommentDocumentSuppliedResources;
import org.apache.xmlgraphics.ps.dsc.events.DSCCommentPageResources;

public class ResourceTracker {
   private Set documentSuppliedResources;
   private Set documentNeededResources;
   private Set usedResources;
   private Set pageResources;
   private Map resourceUsageCounts;

   public Set getDocumentSuppliedResources() {
      return this.documentSuppliedResources != null ? Collections.unmodifiableSet(this.documentSuppliedResources) : Collections.EMPTY_SET;
   }

   public Set getDocumentNeededResources() {
      return this.documentNeededResources != null ? Collections.unmodifiableSet(this.documentNeededResources) : Collections.EMPTY_SET;
   }

   public void notifyStartNewPage() {
      if (this.pageResources != null) {
         this.pageResources.clear();
      }

   }

   public void registerSuppliedResource(PSResource res) {
      if (this.documentSuppliedResources == null) {
         this.documentSuppliedResources = new HashSet();
      }

      this.documentSuppliedResources.add(res);
      if (this.documentNeededResources != null) {
         this.documentNeededResources.remove(res);
      }

   }

   public void registerNeededResource(PSResource res) {
      if (this.documentNeededResources == null) {
         this.documentNeededResources = new HashSet();
      }

      if (!this.documentSuppliedResources.contains(res)) {
         this.documentNeededResources.add(res);
      }

   }

   private void preparePageResources() {
      if (this.pageResources == null) {
         this.pageResources = new HashSet();
      }

   }

   private void prepareUsageCounts() {
      if (this.resourceUsageCounts == null) {
         this.resourceUsageCounts = new HashMap();
      }

   }

   public void notifyResourceUsageOnPage(PSResource res) {
      this.preparePageResources();
      this.pageResources.add(res);
      this.prepareUsageCounts();
      Counter counter = (Counter)this.resourceUsageCounts.get(res);
      if (counter == null) {
         this.resourceUsageCounts.put(res, new Counter());
      } else {
         counter.inc();
      }

   }

   public void notifyResourceUsageOnPage(Collection resources) {
      this.preparePageResources();
      Iterator iter = resources.iterator();

      while(iter.hasNext()) {
         PSResource res = (PSResource)iter.next();
         this.notifyResourceUsageOnPage(res);
      }

   }

   public boolean isResourceSupplied(PSResource res) {
      return this.documentSuppliedResources != null && this.documentSuppliedResources.contains(res);
   }

   public void writeResources(boolean pageLevel, PSGenerator gen) throws IOException {
      if (pageLevel) {
         this.writePageResources(gen);
      } else {
         this.writeDocumentResources(gen);
      }

   }

   public void writePageResources(PSGenerator gen) throws IOException {
      (new DSCCommentPageResources(this.pageResources)).generate(gen);
      if (this.usedResources == null) {
         this.usedResources = new HashSet();
      }

      this.usedResources.addAll(this.pageResources);
   }

   public void writeDocumentResources(PSGenerator gen) throws IOException {
      if (this.usedResources != null) {
         Iterator iter = this.usedResources.iterator();

         label21:
         while(true) {
            PSResource res;
            do {
               if (!iter.hasNext()) {
                  break label21;
               }

               res = (PSResource)iter.next();
            } while(this.documentSuppliedResources != null && this.documentSuppliedResources.contains(res));

            this.registerNeededResource(res);
         }
      }

      (new DSCCommentDocumentNeededResources(this.documentNeededResources)).generate(gen);
      (new DSCCommentDocumentSuppliedResources(this.documentSuppliedResources)).generate(gen);
   }

   public void declareInlined(PSResource res) {
      if (this.documentNeededResources != null) {
         this.documentNeededResources.remove(res);
      }

      if (this.documentSuppliedResources != null) {
         this.documentSuppliedResources.remove(res);
      }

      if (this.pageResources != null) {
         this.pageResources.remove(res);
      }

      if (this.usedResources != null) {
         this.usedResources.remove(res);
      }

   }

   public long getUsageCount(PSResource res) {
      Counter counter = (Counter)this.resourceUsageCounts.get(res);
      return counter != null ? counter.getCount() : 0L;
   }

   private static class Counter {
      private long count;

      private Counter() {
         this.count = 1L;
      }

      public void inc() {
         ++this.count;
      }

      public long getCount() {
         return this.count;
      }

      public String toString() {
         return Long.toString(this.count);
      }

      // $FF: synthetic method
      Counter(Object x0) {
         this();
      }
   }
}
