package org.apache.james.mime4j.dom;

public interface Body extends Disposable {
   Entity getParent();

   void setParent(Entity var1);
}
