package org.apache.xerces.impl;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.parser.XMLDocumentFilter;

public interface RevalidationHandler extends XMLDocumentFilter {
   boolean characterData(String var1, Augmentations var2);
}
