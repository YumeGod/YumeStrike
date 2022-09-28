package org.apache.fop.render.rtf.rtflib.tools;

import java.util.Stack;
import org.apache.fop.render.rtf.rtflib.exceptions.RtfException;
import org.apache.fop.render.rtf.rtflib.rtfdoc.IRtfOptions;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfContainer;

public class BuilderContext {
   private final Stack containers = new Stack();
   private final Stack tableContexts = new Stack();
   private final Stack builders = new Stack();
   private IRtfOptions options;

   public BuilderContext(IRtfOptions rtfOptions) {
      this.options = rtfOptions;
   }

   private Object getObjectFromStack(Stack s, Class desiredClass) {
      Object result = null;
      Stack copy = (Stack)s.clone();

      while(!copy.isEmpty()) {
         Object o = copy.pop();
         if (desiredClass.isAssignableFrom(o.getClass())) {
            result = o;
            break;
         }
      }

      return result;
   }

   public RtfContainer getContainer(Class containerClass, boolean required, Object forWhichBuilder) throws RtfException {
      RtfContainer result = (RtfContainer)this.getObjectFromStack(this.containers, containerClass);
      if (result == null && required) {
         throw new RtfException("No RtfContainer of class '" + containerClass.getName() + "' available for '" + forWhichBuilder.getClass().getName() + "' builder");
      } else {
         return result;
      }
   }

   public void pushContainer(RtfContainer c) {
      this.containers.push(c);
   }

   public void replaceContainer(RtfContainer oldC, RtfContainer newC) throws Exception {
      int index = this.containers.indexOf(oldC);
      if (index < 0) {
         throw new Exception("container to replace not found:" + oldC);
      } else {
         this.containers.setElementAt(newC, index);
      }
   }

   public void popContainer() {
      this.containers.pop();
   }

   public TableContext getTableContext() {
      return (TableContext)this.tableContexts.peek();
   }

   public void pushTableContext(TableContext tc) {
      this.tableContexts.push(tc);
   }

   public void popTableContext() {
      this.tableContexts.pop();
   }
}
