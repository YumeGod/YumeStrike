package org.w3c.css.sac;

public interface SiblingSelector extends Selector {
   short ANY_NODE = 201;

   short getNodeType();

   Selector getSelector();

   SimpleSelector getSiblingSelector();
}
