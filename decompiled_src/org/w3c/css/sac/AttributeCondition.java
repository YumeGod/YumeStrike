package org.w3c.css.sac;

public interface AttributeCondition extends Condition {
   String getNamespaceURI();

   String getLocalName();

   boolean getSpecified();

   String getValue();
}
