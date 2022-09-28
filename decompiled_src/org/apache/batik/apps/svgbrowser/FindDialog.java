package org.apache.batik.apps.svgbrowser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.apache.batik.gvt.GVTTreeWalker;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.text.Mark;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.util.gui.ExtendedGridBagConstraints;
import org.apache.batik.util.gui.resource.ActionMap;
import org.apache.batik.util.gui.resource.ButtonFactory;
import org.apache.batik.util.gui.resource.MissingListenerException;
import org.apache.batik.util.resources.ResourceManager;

public class FindDialog extends JDialog implements ActionMap {
   protected static final String RESOURCES = "org.apache.batik.apps.svgbrowser.resources.FindDialog";
   public static final String FIND_ACTION = "FindButtonAction";
   public static final String CLEAR_ACTION = "ClearButtonAction";
   public static final String CLOSE_ACTION = "CloseButtonAction";
   protected static ResourceBundle bundle = ResourceBundle.getBundle("org.apache.batik.apps.svgbrowser.resources.FindDialog", Locale.getDefault());
   protected static ResourceManager resources;
   protected ButtonFactory buttonFactory;
   protected GraphicsNode gvtRoot;
   protected GVTTreeWalker walker;
   protected int currentIndex;
   protected JTextField search;
   protected JButton findButton;
   protected JButton clearButton;
   protected JButton closeButton;
   protected JCheckBox caseSensitive;
   protected JSVGCanvas svgCanvas;
   protected JRadioButton highlightButton;
   protected JRadioButton highlightCenterButton;
   protected JRadioButton highlightCenterZoomButton;
   protected Map listeners;

   public FindDialog(JSVGCanvas var1) {
      this((Frame)null, var1);
   }

   public FindDialog(Frame var1, JSVGCanvas var2) {
      super(var1, resources.getString("Dialog.title"));
      this.listeners = new HashMap(10);
      this.svgCanvas = var2;
      this.buttonFactory = new ButtonFactory(bundle, this);
      this.listeners.put("FindButtonAction", new FindButtonAction());
      this.listeners.put("ClearButtonAction", new ClearButtonAction());
      this.listeners.put("CloseButtonAction", new CloseButtonAction());
      JPanel var3 = new JPanel(new BorderLayout());
      var3.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
      var3.add(this.createFindPanel(), "Center");
      var3.add(this.createShowResultPanel(), "South");
      this.getContentPane().add(var3, "Center");
      this.getContentPane().add(this.createButtonsPanel(), "South");
   }

   protected JPanel createFindPanel() {
      JPanel var1 = new JPanel(new GridBagLayout());
      var1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), resources.getString("Panel.title")));
      ExtendedGridBagConstraints var2 = new ExtendedGridBagConstraints();
      var2.insets = new Insets(2, 2, 2, 2);
      var2.anchor = 13;
      var2.fill = 0;
      var2.setWeight(0.0, 0.0);
      var2.setGridBounds(0, 0, 1, 1);
      var1.add(new JLabel(resources.getString("FindLabel.text")), var2);
      var2.fill = 2;
      var2.setWeight(1.0, 0.0);
      var2.setGridBounds(1, 0, 2, 1);
      var1.add(this.search = new JTextField(20), var2);
      var2.fill = 0;
      var2.anchor = 17;
      var2.setWeight(0.0, 0.0);
      var2.setGridBounds(1, 1, 1, 1);
      this.caseSensitive = this.buttonFactory.createJCheckBox("CaseSensitiveCheckBox");
      var1.add(this.caseSensitive, var2);
      return var1;
   }

   protected JPanel createShowResultPanel() {
      JPanel var1 = new JPanel(new GridBagLayout());
      var1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), resources.getString("ShowResultPanel.title")));
      ExtendedGridBagConstraints var2 = new ExtendedGridBagConstraints();
      var2.insets = new Insets(2, 2, 2, 2);
      var2.anchor = 17;
      var2.fill = 0;
      var2.setWeight(0.0, 0.0);
      ButtonGroup var3 = new ButtonGroup();
      this.highlightButton = this.buttonFactory.createJRadioButton("Highlight");
      this.highlightButton.setSelected(true);
      var3.add(this.highlightButton);
      var2.setGridBounds(0, 0, 1, 1);
      var1.add(this.highlightButton, var2);
      this.highlightCenterButton = this.buttonFactory.createJRadioButton("HighlightAndCenter");
      var3.add(this.highlightCenterButton);
      var2.setGridBounds(0, 1, 1, 1);
      var1.add(this.highlightCenterButton, var2);
      this.highlightCenterZoomButton = this.buttonFactory.createJRadioButton("HighlightCenterAndZoom");
      var3.add(this.highlightCenterZoomButton);
      var2.setGridBounds(0, 2, 1, 1);
      var1.add(this.highlightCenterZoomButton, var2);
      return var1;
   }

   protected JPanel createButtonsPanel() {
      JPanel var1 = new JPanel(new FlowLayout(2));
      var1.add(this.findButton = this.buttonFactory.createJButton("FindButton"));
      var1.add(this.clearButton = this.buttonFactory.createJButton("ClearButton"));
      var1.add(this.closeButton = this.buttonFactory.createJButton("CloseButton"));
      return var1;
   }

   public void setGraphicsNode(GraphicsNode var1) {
      this.gvtRoot = var1;
      if (var1 != null) {
         this.walker = new GVTTreeWalker(var1);
      } else {
         this.walker = null;
      }

   }

   protected GraphicsNode getNext(String var1) {
      if (this.walker == null && this.gvtRoot != null) {
         this.walker = new GVTTreeWalker(this.gvtRoot);
      }

      GraphicsNode var2 = this.walker.getCurrentGraphicsNode();
      int var3 = this.match(var2, var1, this.currentIndex + var1.length());
      if (var3 >= 0) {
         this.currentIndex = var3;
      } else {
         this.currentIndex = 0;

         for(var2 = this.walker.nextGraphicsNode(); var2 != null && (this.currentIndex = this.match(var2, var1, this.currentIndex)) < 0; var2 = this.walker.nextGraphicsNode()) {
            this.currentIndex = 0;
         }
      }

      return var2;
   }

   protected int match(GraphicsNode var1, String var2, int var3) {
      if (var1 instanceof TextNode && var1.isVisible() && var2 != null && var2.length() != 0) {
         String var4 = ((TextNode)var1).getText();
         if (!this.caseSensitive.isSelected()) {
            var4 = var4.toLowerCase();
            var2 = var2.toLowerCase();
         }

         return var4.indexOf(var2, var3);
      } else {
         return -1;
      }
   }

   protected void showSelectedGraphicsNode() {
      GraphicsNode var1 = this.walker.getCurrentGraphicsNode();
      if (var1 instanceof TextNode) {
         TextNode var2 = (TextNode)var1;
         String var3 = var2.getText();
         String var4 = this.search.getText();
         if (!this.caseSensitive.isSelected()) {
            var3 = var3.toLowerCase();
            var4 = var4.toLowerCase();
         }

         int var5 = var3.indexOf(var4, this.currentIndex);
         AttributedCharacterIterator var6 = var2.getAttributedCharacterIterator();
         var6.first();

         for(int var7 = 0; var7 < var5; ++var7) {
            var6.next();
         }

         Mark var20 = var2.getMarkerForChar(var6.getIndex(), true);

         for(int var8 = 0; var8 < var4.length() - 1; ++var8) {
            var6.next();
         }

         Mark var21 = var2.getMarkerForChar(var6.getIndex(), false);
         this.svgCanvas.select(var20, var21);
         if (!this.highlightButton.isSelected()) {
            Shape var9 = var2.getHighlightShape();
            AffineTransform var10;
            if (this.highlightCenterZoomButton.isSelected()) {
               var10 = this.svgCanvas.getInitialTransform();
            } else {
               var10 = this.svgCanvas.getRenderingTransform();
            }

            Rectangle var11 = var10.createTransformedShape(var9).getBounds();
            Dimension var12 = this.svgCanvas.getSize();
            AffineTransform var13 = AffineTransform.getTranslateInstance(-var11.getX() - var11.getWidth() / 2.0, -var11.getY() - var11.getHeight() / 2.0);
            if (this.highlightCenterZoomButton.isSelected()) {
               double var14 = (double)var12.width / var11.getWidth();
               double var16 = (double)var12.height / var11.getHeight();
               double var18 = Math.min(var14, var16) / 8.0;
               if (var18 > 1.0) {
                  var13.preConcatenate(AffineTransform.getScaleInstance(var18, var18));
               }
            }

            var13.preConcatenate(AffineTransform.getTranslateInstance((double)(var12.width / 2), (double)(var12.height / 2)));
            AffineTransform var22 = new AffineTransform(var10);
            var22.preConcatenate(var13);
            this.svgCanvas.setRenderingTransform(var22);
         }
      }
   }

   public Action getAction(String var1) throws MissingListenerException {
      return (Action)this.listeners.get(var1);
   }

   static {
      resources = new ResourceManager(bundle);
   }

   protected class CloseButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         FindDialog.this.dispose();
      }
   }

   protected class ClearButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         FindDialog.this.search.setText((String)null);
         FindDialog.this.walker = null;
      }
   }

   protected class FindButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         String var2 = FindDialog.this.search.getText();
         if (var2 != null && var2.length() != 0) {
            GraphicsNode var3 = FindDialog.this.getNext(var2);
            if (var3 != null) {
               FindDialog.this.showSelectedGraphicsNode();
            } else {
               FindDialog.this.walker = null;
               JOptionPane.showMessageDialog(FindDialog.this, FindDialog.resources.getString("End.text"), FindDialog.resources.getString("End.title"), 1);
            }

         }
      }
   }
}
