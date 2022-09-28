package org.apache.batik.dom.svg12;

import java.util.HashSet;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.events.AbstractEvent;
import org.apache.batik.dom.events.EventListenerList;
import org.apache.batik.dom.events.EventSupport;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.util.HashTable;
import org.apache.batik.dom.xbl.NodeXBL;
import org.apache.batik.dom.xbl.ShadowTreeEvent;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.MutationEvent;

public class XBLEventSupport extends EventSupport {
   protected HashTable capturingImplementationListeners;
   protected HashTable bubblingImplementationListeners;
   protected static HashTable eventTypeAliases = new HashTable();

   public XBLEventSupport(AbstractNode var1) {
      super(var1);
   }

   public void addEventListenerNS(String var1, String var2, EventListener var3, boolean var4, Object var5) {
      super.addEventListenerNS(var1, var2, var3, var4, var5);
      if (var1 == null || var1.equals("http://www.w3.org/2001/xml-events")) {
         String var6 = (String)eventTypeAliases.get(var2);
         if (var6 != null) {
            super.addEventListenerNS(var1, var6, var3, var4, var5);
         }
      }

   }

   public void removeEventListenerNS(String var1, String var2, EventListener var3, boolean var4) {
      super.removeEventListenerNS(var1, var2, var3, var4);
      if (var1 == null || var1.equals("http://www.w3.org/2001/xml-events")) {
         String var5 = (String)eventTypeAliases.get(var2);
         if (var5 != null) {
            super.removeEventListenerNS(var1, var5, var3, var4);
         }
      }

   }

   public void addImplementationEventListenerNS(String var1, String var2, EventListener var3, boolean var4) {
      HashTable var5;
      if (var4) {
         if (this.capturingImplementationListeners == null) {
            this.capturingImplementationListeners = new HashTable();
         }

         var5 = this.capturingImplementationListeners;
      } else {
         if (this.bubblingImplementationListeners == null) {
            this.bubblingImplementationListeners = new HashTable();
         }

         var5 = this.bubblingImplementationListeners;
      }

      EventListenerList var6 = (EventListenerList)var5.get(var2);
      if (var6 == null) {
         var6 = new EventListenerList();
         var5.put(var2, var6);
      }

      var6.addListener(var1, (Object)null, var3);
   }

   public void removeImplementationEventListenerNS(String var1, String var2, EventListener var3, boolean var4) {
      HashTable var5 = var4 ? this.capturingImplementationListeners : this.bubblingImplementationListeners;
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
      super.moveEventListeners(var1);
      XBLEventSupport var2 = (XBLEventSupport)var1;
      var2.capturingImplementationListeners = this.capturingImplementationListeners;
      var2.bubblingImplementationListeners = this.bubblingImplementationListeners;
      this.capturingImplementationListeners = null;
      this.bubblingImplementationListeners = null;
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
            this.setTarget(var3, var1);
            this.stopPropagation(var3, false);
            this.stopImmediatePropagation(var3, false);
            this.preventDefault(var3, false);
            NodeEventTarget[] var5 = this.getAncestors(var1);
            int var6 = var3.getBubbleLimit();
            int var7 = 0;
            if (this.isSingleScopeEvent(var3)) {
               AbstractNode var8 = (AbstractNode)var1;
               Element var9 = var8.getXblBoundElement();
               if (var9 != null) {
                  for(var7 = var5.length; var7 > 0; --var7) {
                     AbstractNode var10 = (AbstractNode)var5[var7 - 1];
                     if (var10.getXblBoundElement() != var9) {
                        break;
                     }
                  }
               }
            } else if (var6 != 0) {
               var7 = var5.length - var6 + 1;
               if (var7 < 0) {
                  var7 = 0;
               }
            }

            AbstractEvent[] var14 = this.getRetargettedEvents(var1, var5, var3);
            boolean var15 = false;
            HashSet var16 = new HashSet();
            HashSet var11 = new HashSet();

            int var12;
            NodeEventTarget var13;
            for(var12 = 0; var12 < var7; ++var12) {
               var13 = var5[var12];
               this.setCurrentTarget(var14[var12], var13);
               this.setEventPhase(var14[var12], (short)1);
               this.fireImplementationEventListeners(var13, var14[var12], true);
            }

            for(var12 = var7; var12 < var5.length; ++var12) {
               var13 = var5[var12];
               this.setCurrentTarget(var14[var12], var13);
               this.setEventPhase(var14[var12], (short)1);
               this.fireImplementationEventListeners(var13, var14[var12], true);
               this.fireEventListeners(var13, var14[var12], true, var16, var11);
               this.fireHandlerGroupEventListeners(var13, var14[var12], true, var16, var11);
               var15 = var15 || var14[var12].getDefaultPrevented();
               var16.addAll(var11);
               var11.clear();
            }

            this.setEventPhase(var3, (short)2);
            this.setCurrentTarget(var3, var1);
            this.fireImplementationEventListeners(var1, var3, false);
            this.fireEventListeners(var1, var3, false, var16, var11);
            this.fireHandlerGroupEventListeners(this.node, var3, false, var16, var11);
            var16.addAll(var11);
            var11.clear();
            var15 = var15 || var3.getDefaultPrevented();
            if (var3.getBubbles()) {
               for(var12 = var5.length - 1; var12 >= var7; --var12) {
                  var13 = var5[var12];
                  this.setCurrentTarget(var14[var12], var13);
                  this.setEventPhase(var14[var12], (short)3);
                  this.fireImplementationEventListeners(var13, var14[var12], false);
                  this.fireEventListeners(var13, var14[var12], false, var16, var11);
                  this.fireHandlerGroupEventListeners(var13, var14[var12], false, var16, var11);
                  var15 = var15 || var14[var12].getDefaultPrevented();
                  var16.addAll(var11);
                  var11.clear();
               }

               for(var12 = var7 - 1; var12 >= 0; --var12) {
                  var13 = var5[var12];
                  this.setCurrentTarget(var14[var12], var13);
                  this.setEventPhase(var14[var12], (short)3);
                  this.fireImplementationEventListeners(var13, var14[var12], false);
                  var15 = var15 || var14[var12].getDefaultPrevented();
               }
            }

            if (!var15) {
               this.runDefaultActions(var3);
            }

            return var15;
         } else {
            throw this.createEventException((short)0, "unspecified.event", new Object[0]);
         }
      }
   }

   protected void fireHandlerGroupEventListeners(NodeEventTarget var1, AbstractEvent var2, boolean var3, HashSet var4, HashSet var5) {
      NodeList var6 = ((NodeXBL)var1).getXblDefinitions();

      for(int var7 = 0; var7 < var6.getLength(); ++var7) {
         Node var8;
         for(var8 = var6.item(var7).getFirstChild(); var8 != null && !(var8 instanceof XBLOMHandlerGroupElement); var8 = var8.getNextSibling()) {
         }

         if (var8 != null) {
            var1 = (NodeEventTarget)var8;
            String var9 = var2.getType();
            EventSupport var10 = var1.getEventSupport();
            if (var10 != null) {
               EventListenerList var11 = var10.getEventListeners(var9, var3);
               if (var11 == null) {
                  return;
               }

               EventListenerList.Entry[] var12 = var11.getEventListeners();
               this.fireEventListeners(var1, var2, var12, var4, var5);
            }
         }
      }

   }

   protected boolean isSingleScopeEvent(Event var1) {
      return var1 instanceof MutationEvent || var1 instanceof ShadowTreeEvent;
   }

   protected AbstractEvent[] getRetargettedEvents(NodeEventTarget var1, NodeEventTarget[] var2, AbstractEvent var3) {
      boolean var4 = this.isSingleScopeEvent(var3);
      AbstractNode var5 = (AbstractNode)var1;
      AbstractEvent[] var6 = new AbstractEvent[var2.length];
      if (var2.length > 0) {
         int var7 = var2.length - 1;
         Element var8 = var5.getXblBoundElement();
         AbstractNode var9 = (AbstractNode)var2[var7];
         if (!var4 && var9.getXblBoundElement() != var8) {
            var6[var7] = this.retargetEvent(var3, var2[var7]);
         } else {
            var6[var7] = var3;
         }

         while(true) {
            while(true) {
               --var7;
               if (var7 < 0) {
                  return var6;
               }

               var9 = (AbstractNode)var2[var7 + 1];
               var8 = var9.getXblBoundElement();
               AbstractNode var10 = (AbstractNode)var2[var7];
               Element var11 = var10.getXblBoundElement();
               if (!var4 && var11 != var8) {
                  var6[var7] = this.retargetEvent(var6[var7 + 1], var2[var7]);
               } else {
                  var6[var7] = var6[var7 + 1];
               }
            }
         }
      } else {
         return var6;
      }
   }

   protected AbstractEvent retargetEvent(AbstractEvent var1, NodeEventTarget var2) {
      AbstractEvent var3 = var1.cloneEvent();
      this.setTarget(var3, var2);
      return var3;
   }

   public EventListenerList getImplementationEventListeners(String var1, boolean var2) {
      HashTable var3 = var2 ? this.capturingImplementationListeners : this.bubblingImplementationListeners;
      return var3 == null ? null : (EventListenerList)var3.get(var1);
   }

   protected void fireImplementationEventListeners(NodeEventTarget var1, AbstractEvent var2, boolean var3) {
      String var4 = var2.getType();
      XBLEventSupport var5 = (XBLEventSupport)var1.getEventSupport();
      if (var5 != null) {
         EventListenerList var6 = var5.getImplementationEventListeners(var4, var3);
         if (var6 != null) {
            EventListenerList.Entry[] var7 = var6.getEventListeners();
            this.fireEventListeners(var1, var2, var7, (HashSet)null, (HashSet)null);
         }
      }
   }

   static {
      eventTypeAliases.put("SVGLoad", "load");
      eventTypeAliases.put("SVGUnoad", "unload");
      eventTypeAliases.put("SVGAbort", "abort");
      eventTypeAliases.put("SVGError", "error");
      eventTypeAliases.put("SVGResize", "resize");
      eventTypeAliases.put("SVGScroll", "scroll");
      eventTypeAliases.put("SVGZoom", "zoom");
   }
}
