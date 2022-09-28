package org.apache.batik.ext.awt.geom;

import java.awt.Shape;

public interface ExtendedShape extends Shape {
   ExtendedPathIterator getExtendedPathIterator();
}
