package aggressor;

import common.AdjustData;
import common.Callback;
import common.ChangeLog;
import common.CommonUtils;
import common.Keys;
import common.PlaybackStatus;
import common.ScriptUtils;
import common.Scriptable;
import common.Transcript;
import common.TranscriptReset;
import cortana.Cortana;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class DataManager implements Callback, GenericDataManager {
   protected Cortana engine;
   protected Map store = new HashMap();
   protected Map subs = new HashMap();
   protected Map transcripts = new HashMap();
   protected boolean syncing = true;
   protected boolean alive = true;

   public void dead() {
      this.alive = false;
   }

   public boolean isAlive() {
      return this.alive;
   }

   private LinkedList getTranscript(String var1) {
      synchronized(this) {
         LinkedList var3 = (LinkedList)this.transcripts.get(var1);
         if (var3 == null) {
            var3 = new LinkedList();
            this.transcripts.put(var1, var3);
         }

         return var3;
      }
   }

   public List getDataKeys() {
      synchronized(this) {
         HashSet var2 = new HashSet();
         var2.addAll(this.store.keySet());
         var2.addAll(this.transcripts.keySet());
         return new LinkedList(var2);
      }
   }

   public Object getDataSafe(String var1) {
      synchronized(this) {
         Object var3 = this.get(var1, (Object)null);
         if (var3 == null) {
            return null;
         } else if (var3 instanceof Map) {
            return new HashMap((Map)var3);
         } else if (var3 instanceof List) {
            return new LinkedList((List)var3);
         } else {
            return var3 instanceof Collection ? new HashSet((Collection)var3) : var3;
         }
      }
   }

   public Map getMapSafe(String var1) {
      synchronized(this) {
         Map var3 = (Map)this.get(var1, Collections.emptyMap());
         return new HashMap(var3);
      }
   }

   public Collection getSetSafe(String var1) {
      synchronized(this) {
         Collection var3 = (Collection)this.get(var1, Collections.emptySet());
         return new HashSet(var3);
      }
   }

   public List getListSafe(String var1) {
      synchronized(this) {
         List var3 = (List)this.get(var1, Collections.emptyList());
         return new LinkedList(var3);
      }
   }

   public LinkedList populateListAndSubscribe(String var1, AdjustData var2) {
      synchronized(this) {
         if (this.isTranscript(var1)) {
            CommonUtils.print_warn("populateListAndSubscribe: " + var1 + ", " + var2 + ": wrong function");
         }

         List var4 = (List)this.get(var1, Collections.emptyList());
         LinkedList var5 = CommonUtils.apply(var1, var4, var2);
         this.subscribe(var1, var2);
         return var5;
      }
   }

   public LinkedList populateAndSubscribe(String var1, AdjustData var2) {
      synchronized(this) {
         if (this.isStore(var1)) {
            CommonUtils.print_warn("populateAndSubscribe: " + var1 + ", " + var2 + ": wrong function");
         }

         LinkedList var4 = this.getTranscriptSafe(var1);
         LinkedList var5 = CommonUtils.apply(var1, var4, var2);
         this.subscribe(var1, var2);
         return var5;
      }
   }

   public LinkedList getTranscriptAndSubscribeSafe(String var1, Callback var2) {
      synchronized(this) {
         LinkedList var4 = this.getTranscriptSafe(var1);
         this.subscribe(var1, var2);
         return var4;
      }
   }

   public LinkedList getTranscriptSafe(String var1) {
      synchronized(this) {
         return new LinkedList(this.getTranscript(var1));
      }
   }

   protected boolean isTranscript(String var1) {
      synchronized(this) {
         return this.transcripts.containsKey(var1);
      }
   }

   protected boolean isStore(String var1) {
      synchronized(this) {
         return this.store.containsKey(var1);
      }
   }

   public WindowCleanup unsubOnClose(String var1, Callback var2) {
      return new WindowCleanup(this, var1, var2);
   }

   public DataManager(Cortana var1) {
      this.engine = var1;
   }

   public void unsub(String var1, Callback var2) {
      synchronized(this) {
         List var4 = (List)this.subs.get(var1);
         var4.remove(var2);
      }
   }

   public String key() {
      return this.hashCode() + "";
   }

   protected List getSubs(String var1) {
      synchronized(this) {
         Object var3 = (List)this.subs.get(var1);
         if (var3 == null) {
            var3 = new LinkedList();
            this.subs.put(var1, var3);
         }

         return (List)var3;
      }
   }

   protected List getSubsSafe(String var1) {
      synchronized(this) {
         return new LinkedList(this.getSubs(var1));
      }
   }

   public void subscribe(String var1, Callback var2) {
      synchronized(this) {
         this.getSubs(var1).add(var2);
      }
   }

   public Map getModelDirect(String var1, String var2) {
      synchronized(this) {
         if (this.isDataModel(var1)) {
            Map var4 = (Map)this.store.get(var1);
            return (Map)(var4 == null ? new HashMap() : (Map)var4.get(var2));
         } else {
            throw new RuntimeException("'" + var1 + "' is not a data model!");
         }
      }
   }

   public Object get(String var1, Object var2) {
      synchronized(this) {
         if (this.isTranscript(var1)) {
            return this.getTranscript(var1);
         } else if (this.isStore(var1)) {
            return this.isDataModel(var1) ? new LinkedList(((Map)this.store.get(var1)).values()) : this.store.get(var1);
         } else {
            return var2;
         }
      }
   }

   public Map getDataModel(String var1) {
      synchronized(this) {
         return this.isStore(var1) && this.isDataModel(var1) ? new HashMap((Map)this.store.get(var1)) : new HashMap();
      }
   }

   public void put(String var1, Object var2) {
      synchronized(this) {
         this.store.put(var1, var2);
      }
   }

   public void put(String var1, String var2, Object var3) {
      synchronized(this) {
         if (!this.store.containsKey(var1)) {
            this.store.put(var1, new HashMap());
         }

         Object var5 = this.store.get(var1);
         if (var5 instanceof Map) {
            Map var6 = (Map)this.store.get(var1);
            var6.put(var2, var3);
         } else {
            CommonUtils.print_error("DataManager.put: " + var1 + " -> " + var2 + " -> " + var3 + " applied to a non-Map incumbent (ignoring)");
         }

      }
   }

   public boolean isDataModel(String var1) {
      return Keys.isDataModel(var1);
   }

   public void result(String var1, Object var2) {
      synchronized(this) {
         if (var2 instanceof Transcript) {
            LinkedList var10 = this.getTranscript(var1);
            var10.add(var2);

            while(var10.size() >= CommonUtils.limit(var1)) {
               var10.removeFirst();
            }
         } else if (this.isDataModel(var1)) {
            if (var2 instanceof ChangeLog) {
               Map var4 = (Map)this.store.get(var1);
               ChangeLog var5;
               if (var4 == null) {
                  CommonUtils.print_error("data manager does not have: " + var1 + " [will apply summary to empty model]");
                  HashMap var8 = new HashMap();
                  var5 = (ChangeLog)var2;
                  var5.applyForce(var8);
                  this.store.put(var1, var8);
               } else {
                  var5 = (ChangeLog)var2;
                  var5.applyForce(var4);
               }
            } else {
               this.store.put(var1, var2);
            }

            var2 = this.get(var1, (Object)null);
         } else if (var2 instanceof PlaybackStatus) {
            PlaybackStatus var9 = (PlaybackStatus)var2;
            if (var9.isDone()) {
               this.syncing = false;
            }
         } else if (var2 instanceof TranscriptReset) {
            this.transcripts = new HashMap();
         } else {
            this.store.put(var1, var2);
         }
      }

      Iterator var3 = this.getSubsSafe(var1).iterator();

      while(var3.hasNext()) {
         Callback var11 = (Callback)var3.next();
         var11.result(var1, var2);
      }

      if (!this.syncing) {
         if (var2 instanceof Scriptable) {
            Scriptable var12 = (Scriptable)var2;
            Stack var13 = var12.eventArguments();
            String var6 = var12.eventName();
            this.engine.getEventManager().fireEvent(var6, var13);
         }

         if (this.engine.getEventManager().isLiveEvent(var1)) {
            Stack var14 = new Stack();
            var14.push(ScriptUtils.convertAll(var2));
            this.engine.getEventManager().fireEvent(var1, var14);
         }
      }

      if (GlobalDataManager.getGlobalDataManager().isGlobal(var1)) {
         GlobalDataManager.getGlobalDataManager().report(this, var1, var2);
      }

   }
}
