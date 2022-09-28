package org.w3c.css.sac;

public interface DescendantSelector extends Selector {
   Selector getAncestorSelector();

   SimpleSelector getSimpleSelector();
}
