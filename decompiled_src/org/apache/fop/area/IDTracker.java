package org.apache.fop.area;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IDTracker {
   private static final Log log;
   private Map idLocations = new HashMap();
   private Map unresolvedIDRefs = new HashMap();
   private Set unfinishedIDs = new HashSet();
   private Set alreadyResolvedIDs = new HashSet();

   public void associateIDWithPageViewport(String id, PageViewport pv) {
      if (log.isDebugEnabled()) {
         log.debug("associateIDWithPageViewport(" + id + ", " + pv + ")");
      }

      List pvList = (List)this.idLocations.get(id);
      if (pvList == null) {
         List pvList = new ArrayList();
         this.idLocations.put(id, pvList);
         pvList.add(pv);
         pv.setFirstWithID(id);
         if (!this.unfinishedIDs.contains(id)) {
            this.tryIDResolution(id, pv, pvList);
         }
      } else if (!pvList.contains(pv)) {
         pvList.add(pv);
      }

   }

   public void signalPendingID(String id) {
      if (log.isDebugEnabled()) {
         log.debug("signalPendingID(" + id + ")");
      }

      this.unfinishedIDs.add(id);
   }

   public void signalIDProcessed(String id) {
      if (log.isDebugEnabled()) {
         log.debug("signalIDProcessed(" + id + ")");
      }

      this.alreadyResolvedIDs.add(id);
      if (this.unfinishedIDs.contains(id)) {
         this.unfinishedIDs.remove(id);
         List pvList = (List)this.idLocations.get(id);
         Set todo = (Set)this.unresolvedIDRefs.get(id);
         if (todo != null) {
            Iterator iter = todo.iterator();

            while(iter.hasNext()) {
               Resolvable res = (Resolvable)iter.next();
               res.resolveIDRef(id, pvList);
            }

            this.unresolvedIDRefs.remove(id);
         }

      }
   }

   public boolean alreadyResolvedID(String id) {
      return this.alreadyResolvedIDs.contains(id);
   }

   private void tryIDResolution(String id, PageViewport pv, List pvList) {
      Set todo = (Set)this.unresolvedIDRefs.get(id);
      if (todo != null) {
         Iterator iter = todo.iterator();

         while(iter.hasNext()) {
            Resolvable res = (Resolvable)iter.next();
            if (this.unfinishedIDs.contains(id)) {
               return;
            }

            res.resolveIDRef(id, pvList);
         }

         this.alreadyResolvedIDs.add(id);
         this.unresolvedIDRefs.remove(id);
      }

   }

   public void tryIDResolution(PageViewport pv) {
      String[] ids = pv.getIDRefs();
      if (ids != null) {
         for(int i = 0; i < ids.length; ++i) {
            List pvList = (List)this.idLocations.get(ids[i]);
            if (pvList != null) {
               this.tryIDResolution(ids[i], pv, pvList);
            }
         }
      }

   }

   public List getPageViewportsContainingID(String id) {
      return (List)this.idLocations.get(id);
   }

   public void addUnresolvedIDRef(String idref, Resolvable res) {
      Set todo = (Set)this.unresolvedIDRefs.get(idref);
      if (todo == null) {
         todo = new HashSet();
         this.unresolvedIDRefs.put(idref, todo);
      }

      ((Set)todo).add(res);
   }

   static {
      log = LogFactory.getLog(IDTracker.class);
   }
}
