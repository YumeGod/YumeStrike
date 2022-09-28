package org.apache.batik.dom.events;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.util.HashTable;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;

public class EventSupport {
   protected HashTable capturingListeners;
   protected HashTable bubblingListeners;
   protected AbstractNode node;

   public EventSupport(AbstractNode var1) {
      this.node = var1;
   }

   public void addEventListener(String var1, EventListener var2, boolean var3) {
      this.addEventListenerNS((String)null, var1, var2, var3, (Object)null);
   }

   public void addEventListenerNS(String var1, String var2, EventListener var3, boolean var4, Object var5) {
      HashTable var6;
      if (var4) {
         if (this.capturingListeners == null) {
            this.capturingListeners = new HashTable();
         }

         var6 = this.capturingListeners;
      } else {
         if (this.bubblingListeners == null) {
            this.bubblingListeners = new HashTable();
         }

         var6 = this.bubblingListeners;
      }

      EventListenerList var7 = (EventListenerList)var6.get(var2);
      if (var7 == null) {
         var7 = new EventListenerList();
         var6.put(var2, var7);
      }

      var7.addListener(var1, var5, var3);
   }

   public void removeEventListener(String var1, EventListener var2, boolean var3) {
      this.removeEventListenerNS((String)null, var1, var2, var3);
   }

   public void removeEventListenerNS(String var1, String var2, EventListener var3, boolean var4) {
      HashTable var5;
      if (var4) {
         var5 = this.capturingListeners;
      } else {
         var5 = this.bubblingListeners;
      }

      if (var5 != null) {
         EventListenerList var6 = (EventListenerList)var5.get(var2);
         if (var6 != null) {
            var6.removeListener(var1, var3);
            if (var6.size() == 0) {
               var5.remove(var2);
            }
         }

      }
   }

   public void moveEventListeners(EventSupport var1) {
      var1.capturingListeners = this.capturingListeners;
      var1.bubblingListeners = this.bubblingListeners;
      this.capturingListeners = null;
      this.bubblingListeners = null;
   }

   public boolean dispatchEvent(NodeEventTarget var1, Event var2) throws EventException {
      if (var2 == null) {
         return false;
      } else if (!(var2 instanceof AbstractEvent)) {
         throw this.createEventException((short)9, "unsupported.event", new Object[0]);
      } else {
         AbstractEvent var3 = (AbstractEvent)var2;
         String var4 = var3.getType();
         if (var4 != null && var4.length() != 0) {
            var3.setTarget(var1);
            var3.stopPropagation(false);
            var3.stopImmediatePropagation(false);
            var3.preventDefault(false);
            NodeEventTarget[] var5 = this.getAncestors(var1);
            var3.setEventPhase((short)1);
            HashSet var6 = new HashSet();
            HashSet var7 = new HashSet();

            int var8;
            NodeEventTarget var9;
            for(var8 = 0; var8 < var5.length; ++var8) {
               var9 = var5[var8];
               var3.setCurrentTarget(var9);
               this.fireEventListeners(var9, var3, true, var6, var7);
               var6.addAll(var7);
               var7.clear();
            }

            var3.setEventPhase((short)2);
            var3.setCurrentTarget(var1);
            this.fireEventListeners(var1, var3, false, var6, var7);
            var6.addAll(var7);
            var7.clear();
            if (var3.getBubbles()) {
               var3.setEventPhase((short)3);

               for(var8 = var5.length - 1; var8 >= 0; --var8) {
                  var9 = var5[var8];
                  var3.setCurrentTarget(var9);
                  this.fireEventListeners(var9, var3, false, var6, var7);
                  var6.addAll(var7);
                  var7.clear();
               }
            }

            if (!var3.getDefaultPrevented()) {
               this.runDefaultActions(var3);
            }

            return var3.getDefaultPrevented();
         } else {
            throw this.createEventException((short)0, "unspecified.event", new Object[0]);
         }
      }
   }

   protected void runDefaultActions(AbstractEvent var1) {
      List var2 = var1.getDefaultActions();
      if (var2 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Runnable var4 = (Runnable)var3.next();
            var4.run();
         }
      }

   }

   protected void fireEventListeners(NodeEventTarget var1, AbstractEvent var2, EventListenerList.Entry[] var3, HashSet var4, HashSet var5) {
      if (var3 != null) {
         String var6 = var2.getNamespaceURI();

         for(int var7 = 0; var7 < var3.length; ++var7) {
            try {
               String var8 = var3[var7].getNamespaceURI();
               if (var8 == null || var6 == null || var8.equals(var6)) {
                  Object var9 = var3[var7].getGroup();
                  if (var4 == null || !var4.contains(var9)) {
                     var3[var7].getListener().handleEvent(var2);
                     if (var2.getStopImmediatePropagation()) {
                        if (var4 != null) {
                           var4.add(var9);
                        }

                        var2.stopImmediatePropagation(false);
                     } else if (var2.getStopPropagation()) {
                        if (var5 != null) {
                           var5.add(var9);
                        }

                        var2.stopPropagation(false);
                     }
                  }
               }
            } catch (ThreadDeath var10) {
               throw var10;
            } catch (Throwable var11) {
               var11.printStackTrace();
            }
         }

      }
   }

   protected void fireEventListeners(NodeEventTarget var1, AbstractEvent var2, boolean var3, HashSet var4, HashSet var5) {
      String var6 = var2.getType();
      EventSupport var7 = var1.getEventSupport();
      if (var7 != null) {
         EventListenerList var8 = var7.getEventListeners(var6, var3);
         if (var8 != null) {
            EventListenerList.Entry[] var9 = var8.getEventListeners();
            this.fireEventListeners(var1, var2, var9, var4, var5);
         }
      }
   }

   protected NodeEventTarget[] getAncestors(NodeEventTarget var1) {
      var1 = var1.getParentNodeEventTarget();
      int var2 = 0;

      for(NodeEventTarget var3 = var1; var3 != null; ++var2) {
         var3 = var3.getParentNodeEventTarget();
      }

      NodeEventTarget[] var5 = new NodeEventTarget[var2];

      for(int var4 = var2 - 1; var4 >= 0; var1 = var1.getParentNodeEventTarget()) {
         var5[var4] = var1;
         --var4;
      }

      return var5;
   }

   public boolean hasEventListenerNS(String var1, String var2) {
      EventListenerList var3;
      if (this.capturingListeners != null) {
         var3 = (EventListenerList)this.capturingListeners.get(var2);
         if (var3 != null && var3.hasEventListener(var1)) {
            return true;
         }
      }

      if (this.bubblingListeners != null) {
         var3 = (EventListenerList)this.capturingListeners.get(var2);
         if (var3 != null) {
            return var3.hasEventListener(var1);
         }
      }

      return false;
   }

   public EventListenerList getEventListeners(String var1, boolean var2) {
      HashTable var3 = var2 ? this.capturingListeners : this.bubblingListeners;
      return var3 == null ? null : (EventListenerList)var3.get(var1);
   }

   protected EventException createEventException(short var1, String var2, Object[] var3) {
      try {
         AbstractDocument var4 = (AbstractDocument)this.node.getOwnerDocument();
         return new EventException(var1, var4.formatMessage(var2, var3));
      } catch (Exception var5) {
         return new EventException(var1, var2);
      }
   }

   protected void setTarget(AbstractEvent var1, NodeEventTarget var2) {
      var1.setTarget(var2);
   }

   protected void stopPropagation(AbstractEvent var1, boolean var2) {
      var1.stopPropagation(var2);
   }

   protected void stopImmediatePropagation(AbstractEvent var1, boolean var2) {
      var1.stopImmediatePropagation(var2);
   }

   protected void preventDefault(AbstractEvent var1, boolean var2) {
      var1.preventDefault(var2);
   }

   protected void setCurrentTarget(AbstractEvent var1, NodeEventTarget var2) {
      var1.setCurrentTarget(var2);
   }

   protected void setEventPhase(AbstractEvent var1, short var2) {
      var1.setEventPhase(var2);
   }

   public static Event getUltimateOriginalEvent(Event var0) {
      AbstractEvent var1 = (AbstractEvent)var0;

      while(true) {
         AbstractEvent var2 = (AbstractEvent)var1.getOriginalEvent();
         if (var2 == null) {
            return var1;
         }

         var1 = var2;
      }
   }
}
