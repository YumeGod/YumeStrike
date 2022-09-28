package org.apache.batik.swing.gvt;

import java.awt.event.InputEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public interface Interactor extends KeyListener, MouseListener, MouseMotionListener {
   boolean startInteraction(InputEvent var1);

   boolean endInteraction();
}
