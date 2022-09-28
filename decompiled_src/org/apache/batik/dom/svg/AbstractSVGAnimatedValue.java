package org.apache.batik.dom.svg;

import java.util.Iterator;
import java.util.LinkedList;
import org.apache.batik.anim.values.AnimatableValue;

public abstract class AbstractSVGAnimatedValue implements AnimatedLiveAttributeValue {
   protected AbstractElement element;
   protected String namespaceURI;
   protected String localName;
   protected boolean hasAnimVal;
   protected LinkedList listeners = new LinkedList();

   public AbstractSVGAnimatedValue(AbstractElement var1, String var2, String var3) {
      this.element = var1;
      this.namespaceURI = var2;
      this.localName = var3;
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public String getLocalName() {
      return this.localName;
   }

   public boolean isSpecified() {
      return this.hasAnimVal || this.element.hasAttributeNS(this.namespaceURI, this.localName);
   }

   protected abstract void updateAnimatedValue(AnimatableValue var1);

   public void addAnimatedAttributeListener(AnimatedAttributeListener var1) {
      if (!this.listeners.contains(var1)) {
         this.listeners.add(var1);
      }

   }

   public void removeAnimatedAttributeListener(AnimatedAttributeListener var1) {
      this.listeners.remove(var1);
   }

   protected void fireBaseAttributeListeners() {
      if (this.element instanceof SVGOMElement) {
         ((SVGOMElement)this.element).fireBaseAttributeListeners(this.namespaceURI, this.localName);
      }

   }

   protected void fireAnimatedAttributeListeners() {
      Iterator var1 = this.listeners.iterator();

      while(var1.hasNext()) {
         AnimatedAttributeListener var2 = (AnimatedAttributeListener)var1.next();
         var2.animatedAttributeChanged(this.element, this);
      }

   }
}
