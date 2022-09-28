package org.apache.fop.area;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fo.pagination.SimplePageMaster;

public class PageViewport extends AreaTreeObject implements Resolvable, Cloneable {
   private Page page;
   private Rectangle viewArea;
   private String simplePageMasterName;
   private String pageKey;
   private int pageNumber = -1;
   private String pageNumberString = null;
   private int pageIndex = -1;
   private boolean blank;
   private transient PageSequence pageSequence;
   private Set idFirsts = new HashSet();
   private Map unresolvedIDRefs = new HashMap();
   private Map pendingResolved = null;
   private Map markerFirstStart = null;
   private Map markerLastStart = null;
   private Map markerFirstAny = null;
   private Map markerLastEnd = null;
   private Map markerLastAny = null;
   protected static Log log;

   public PageViewport(SimplePageMaster spm, int pageNumber, String pageStr, boolean blank) {
      this.simplePageMasterName = spm.getMasterName();
      this.setExtensionAttachments(spm.getExtensionAttachments());
      this.setForeignAttributes(spm.getForeignAttributes());
      this.blank = blank;
      int pageWidth = spm.getPageWidth().getValue();
      int pageHeight = spm.getPageHeight().getValue();
      this.pageNumber = pageNumber;
      this.pageNumberString = pageStr;
      this.viewArea = new Rectangle(0, 0, pageWidth, pageHeight);
      this.page = new Page(spm);
      this.createSpan(false);
   }

   public PageViewport(PageViewport original) {
      if (original.extensionAttachments != null) {
         this.setExtensionAttachments(original.extensionAttachments);
      }

      if (original.foreignAttributes != null) {
         this.setForeignAttributes(original.foreignAttributes);
      }

      this.pageIndex = original.pageIndex;
      this.pageNumber = original.pageNumber;
      this.pageNumberString = original.pageNumberString;
      this.page = (Page)original.page.clone();
      this.viewArea = new Rectangle(original.viewArea);
      this.simplePageMasterName = original.simplePageMasterName;
      this.blank = original.blank;
   }

   public PageViewport(Rectangle viewArea, int pageNumber, String pageStr, String simplePageMasterName, boolean blank) {
      this.viewArea = viewArea;
      this.pageNumber = pageNumber;
      this.pageNumberString = pageStr;
      this.simplePageMasterName = simplePageMasterName;
      this.blank = blank;
   }

   public void setPageSequence(PageSequence seq) {
      this.pageSequence = seq;
   }

   public PageSequence getPageSequence() {
      return this.pageSequence;
   }

   public Rectangle getViewArea() {
      return this.viewArea;
   }

   public Page getPage() {
      return this.page;
   }

   public void setPage(Page page) {
      this.page = page;
   }

   public int getPageNumber() {
      return this.pageNumber;
   }

   public String getPageNumberString() {
      return this.pageNumberString;
   }

   public void setPageIndex(int index) {
      this.pageIndex = index;
   }

   public int getPageIndex() {
      return this.pageIndex;
   }

   public void setKey(String key) {
      this.pageKey = key;
   }

   public String getKey() {
      if (this.pageKey == null) {
         throw new IllegalStateException("No page key set on the PageViewport: " + this.toString());
      } else {
         return this.pageKey;
      }
   }

   public void setFirstWithID(String id) {
      if (id != null) {
         this.idFirsts.add(id);
      }

   }

   public boolean isFirstWithID(String id) {
      return this.idFirsts.contains(id);
   }

   public void addUnresolvedIDRef(String idref, Resolvable res) {
      if (this.unresolvedIDRefs == null) {
         this.unresolvedIDRefs = new HashMap();
      }

      List list = (List)this.unresolvedIDRefs.get(idref);
      if (list == null) {
         list = new ArrayList();
         this.unresolvedIDRefs.put(idref, list);
      }

      ((List)list).add(res);
   }

   public boolean isResolved() {
      return this.unresolvedIDRefs == null || this.unresolvedIDRefs.size() == 0;
   }

   public String[] getIDRefs() {
      return this.unresolvedIDRefs == null ? null : (String[])this.unresolvedIDRefs.keySet().toArray(new String[0]);
   }

   public void resolveIDRef(String id, List pages) {
      if (this.page == null) {
         if (this.pendingResolved == null) {
            this.pendingResolved = new HashMap();
         }

         this.pendingResolved.put(id, pages);
      } else if (this.unresolvedIDRefs != null) {
         List todo = (List)this.unresolvedIDRefs.get(id);
         if (todo != null) {
            for(int count = 0; count < todo.size(); ++count) {
               Resolvable res = (Resolvable)todo.get(count);
               res.resolveIDRef(id, pages);
            }
         }
      }

      if (this.unresolvedIDRefs != null && pages != null) {
         this.unresolvedIDRefs.remove(id);
         if (this.unresolvedIDRefs.isEmpty()) {
            this.unresolvedIDRefs = null;
         }
      }

   }

   public void addMarkers(Map marks, boolean starting, boolean isfirst, boolean islast) {
      if (marks != null) {
         if (log.isDebugEnabled()) {
            log.debug("--" + marks.keySet() + ": " + (starting ? "starting" : "ending") + (isfirst ? ", first" : "") + (islast ? ", last" : ""));
         }

         if (starting) {
            Iterator iter;
            Object key;
            if (isfirst) {
               if (this.markerFirstStart == null) {
                  this.markerFirstStart = new HashMap();
               }

               if (this.markerFirstAny == null) {
                  this.markerFirstAny = new HashMap();
               }

               iter = marks.keySet().iterator();

               while(iter.hasNext()) {
                  key = iter.next();
                  if (!this.markerFirstStart.containsKey(key)) {
                     this.markerFirstStart.put(key, marks.get(key));
                     if (log.isTraceEnabled()) {
                        log.trace("page " + this.pageNumberString + ": " + "Adding marker " + key + " to FirstStart");
                     }
                  }

                  if (!this.markerFirstAny.containsKey(key)) {
                     this.markerFirstAny.put(key, marks.get(key));
                     if (log.isTraceEnabled()) {
                        log.trace("page " + this.pageNumberString + ": " + "Adding marker " + key + " to FirstAny");
                     }
                  }
               }

               if (this.markerLastStart == null) {
                  this.markerLastStart = new HashMap();
               }

               this.markerLastStart.putAll(marks);
               if (log.isTraceEnabled()) {
                  log.trace("page " + this.pageNumberString + ": " + "Adding all markers to LastStart");
               }
            } else {
               if (this.markerFirstAny == null) {
                  this.markerFirstAny = new HashMap();
               }

               iter = marks.keySet().iterator();

               while(iter.hasNext()) {
                  key = iter.next();
                  if (!this.markerFirstAny.containsKey(key)) {
                     this.markerFirstAny.put(key, marks.get(key));
                     if (log.isTraceEnabled()) {
                        log.trace("page " + this.pageNumberString + ": " + "Adding marker " + key + " to FirstAny");
                     }
                  }
               }
            }
         } else {
            if (islast) {
               if (this.markerLastEnd == null) {
                  this.markerLastEnd = new HashMap();
               }

               this.markerLastEnd.putAll(marks);
               if (log.isTraceEnabled()) {
                  log.trace("page " + this.pageNumberString + ": " + "Adding all markers to LastEnd");
               }
            }

            if (this.markerLastAny == null) {
               this.markerLastAny = new HashMap();
            }

            this.markerLastAny.putAll(marks);
            if (log.isTraceEnabled()) {
               log.trace("page " + this.pageNumberString + ": " + "Adding all markers to LastAny");
            }
         }

      }
   }

   public Object getMarker(String name, int pos) {
      Object mark = null;
      String posName = null;
      switch (pos) {
         case 49:
            if (this.markerFirstAny != null) {
               mark = this.markerFirstAny.get(name);
               posName = "FIC";
            }
            break;
         case 54:
            if (this.markerFirstStart != null) {
               mark = this.markerFirstStart.get(name);
               posName = "FSWP";
            }

            if (mark == null && this.markerFirstAny != null) {
               mark = this.markerFirstAny.get(name);
               posName = "FirstAny after " + posName;
            }
            break;
         case 74:
            if (this.markerLastEnd != null) {
               mark = this.markerLastEnd.get(name);
               posName = "LEWP";
            }

            if (mark == null && this.markerLastAny != null) {
               mark = this.markerLastAny.get(name);
               posName = "LastAny after " + posName;
            }
            break;
         case 81:
            if (this.markerLastStart != null) {
               mark = this.markerLastStart.get(name);
               posName = "LSWP";
            }

            if (mark == null && this.markerLastAny != null) {
               mark = this.markerLastAny.get(name);
               posName = "LastAny after " + posName;
            }
            break;
         default:
            throw new RuntimeException();
      }

      if (log.isTraceEnabled()) {
         log.trace("page " + this.pageNumberString + ": " + "Retrieving marker " + name + " at position " + posName);
      }

      return mark;
   }

   public void dumpMarkers() {
      if (log.isTraceEnabled()) {
         log.trace("FirstAny: " + this.markerFirstAny);
         log.trace("FirstStart: " + this.markerFirstStart);
         log.trace("LastAny: " + this.markerLastAny);
         log.trace("LastEnd: " + this.markerLastEnd);
         log.trace("LastStart: " + this.markerLastStart);
      }

   }

   public void savePage(ObjectOutputStream out) throws IOException {
      this.page.setUnresolvedReferences(this.unresolvedIDRefs);
      out.writeObject(this.page);
      this.page = null;
   }

   public void loadPage(ObjectInputStream in) throws IOException, ClassNotFoundException {
      this.page = (Page)in.readObject();
      this.unresolvedIDRefs = this.page.getUnresolvedReferences();
      if (this.unresolvedIDRefs != null && this.pendingResolved != null) {
         Iterator iter = this.pendingResolved.keySet().iterator();

         while(iter.hasNext()) {
            String id = (String)iter.next();
            this.resolveIDRef(id, (List)this.pendingResolved.get(id));
         }

         this.pendingResolved = null;
      }

   }

   public Object clone() {
      return new PageViewport(this);
   }

   public void clear() {
      this.page = null;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer(64);
      sb.append("PageViewport: page=");
      sb.append(this.getPageNumberString());
      return sb.toString();
   }

   public String getSimplePageMasterName() {
      return this.simplePageMasterName;
   }

   public boolean isBlank() {
      return this.blank;
   }

   public BodyRegion getBodyRegion() {
      return (BodyRegion)this.getPage().getRegionViewport(58).getRegionReference();
   }

   public Span createSpan(boolean spanAll) {
      return this.getBodyRegion().getMainReference().createSpan(spanAll);
   }

   public Span getCurrentSpan() {
      return this.getBodyRegion().getMainReference().getCurrentSpan();
   }

   public NormalFlow getCurrentFlow() {
      return this.getCurrentSpan().getCurrentFlow();
   }

   public NormalFlow moveToNextFlow() {
      return this.getCurrentSpan().moveToNextFlow();
   }

   public RegionReference getRegionReference(int id) {
      return this.getPage().getRegionViewport(id).getRegionReference();
   }

   static {
      log = LogFactory.getLog(PageViewport.class);
   }
}
