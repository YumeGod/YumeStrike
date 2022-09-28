package de.javasoft.plaf.synthetica;

import de.javasoft.plaf.synthetica.painter.ScrollPanePainter;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class SyntheticaScrollPaneCorner extends JComponent {
   private String cornerID;

   public SyntheticaScrollPaneCorner(String var1) {
      this.cornerID = var1;
      this.setOpaque(false);
   }

   protected void paintComponent(Graphics var1) {
      JScrollPane var2 = (JScrollPane)this.getParent();
      ScrollPanePainter.getInstance().paintScrollPaneCorner(var2, var1, 0, 0, this.getWidth(), this.getHeight(), this.cornerID);
   }

   public static class TableUpperTrailingCorner extends SyntheticaScrollPaneCorner {
      public TableUpperTrailingCorner() {
         super("UPPER_TRAILING_CORNER");
      }
   }

   public static class UIResource extends SyntheticaScrollPaneCorner implements javax.swing.plaf.UIResource {
      public UIResource(String var1) {
         super(var1);
      }
   }
}
