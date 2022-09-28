package org.apache.avalon.framework.component;

public interface ComponentSelector extends Component {
   Component select(Object var1) throws ComponentException;

   boolean hasComponent(Object var1);

   void release(Component var1);
}
