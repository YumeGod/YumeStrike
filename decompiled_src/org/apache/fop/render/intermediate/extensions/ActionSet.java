package org.apache.fop.render.intermediate.extensions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ActionSet {
   private int lastGeneratedID = 0;
   private Map actionRegistry = new HashMap();

   public synchronized String generateNewID(AbstractAction action) {
      ++this.lastGeneratedID;
      String prefix = action.getIDPrefix();
      if (prefix == null) {
         throw new IllegalArgumentException("Action class is not compatible");
      } else {
         return prefix + this.lastGeneratedID;
      }
   }

   public AbstractAction get(String id) {
      return (AbstractAction)this.actionRegistry.get(id);
   }

   public AbstractAction put(AbstractAction action) {
      if (!action.hasID()) {
         action.setID(this.generateNewID(action));
      }

      AbstractAction effAction = this.normalize(action);
      if (effAction == action) {
         this.actionRegistry.put(action.getID(), action);
      }

      return effAction;
   }

   public void clear() {
      this.actionRegistry.clear();
   }

   private AbstractAction normalize(AbstractAction action) {
      Iterator iter = this.actionRegistry.values().iterator();

      AbstractAction a;
      do {
         if (!iter.hasNext()) {
            return action;
         }

         a = (AbstractAction)iter.next();
      } while(!a.isSame(action));

      return a;
   }
}
