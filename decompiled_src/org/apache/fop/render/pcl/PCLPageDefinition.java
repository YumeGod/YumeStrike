package org.apache.fop.render.pcl;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.xmlgraphics.util.UnitConv;

public class PCLPageDefinition {
   private static List pageDefinitions;
   private static PCLPageDefinition defaultPageDefinition;
   private final String name;
   private final int selector;
   private final Dimension physicalPageSize;
   private final Rectangle logicalPageRect;
   private final boolean landscape;

   public PCLPageDefinition(String name, int selector, Dimension physicalPageSize, Rectangle logicalPageRect, boolean landscape) {
      this.name = name;
      this.selector = selector;
      this.physicalPageSize = physicalPageSize;
      this.logicalPageRect = logicalPageRect;
      this.landscape = landscape;
   }

   public String getName() {
      return this.name;
   }

   public int getSelector() {
      return this.selector;
   }

   public boolean isLandscapeFormat() {
      return this.landscape;
   }

   public Dimension getPhysicalPageSize() {
      return this.physicalPageSize;
   }

   public Rectangle getLogicalPageRect() {
      return this.logicalPageRect;
   }

   private boolean matches(long width, long height, int errorMargin) {
      return Math.abs((long)this.physicalPageSize.width - width) < (long)errorMargin && Math.abs((long)this.physicalPageSize.height - height) < (long)errorMargin;
   }

   public String toString() {
      return this.getName();
   }

   public static PCLPageDefinition getPageDefinition(long width, long height, int errorMargin) {
      Iterator iter = pageDefinitions.iterator();

      PCLPageDefinition def;
      do {
         if (!iter.hasNext()) {
            return null;
         }

         def = (PCLPageDefinition)iter.next();
      } while(!def.matches(width, height, errorMargin));

      return def;
   }

   public static PCLPageDefinition getPageDefinition(String name) {
      Iterator iter = pageDefinitions.iterator();

      PCLPageDefinition def;
      do {
         if (!iter.hasNext()) {
            return null;
         }

         def = (PCLPageDefinition)iter.next();
      } while(!def.getName().equalsIgnoreCase(name));

      return def;
   }

   public static PCLPageDefinition getDefaultPageDefinition() {
      return defaultPageDefinition;
   }

   private static int convert300dpiDotsToMpt(int offset) {
      return (int)Math.round((double)offset * 72000.0 / 300.0);
   }

   private static Dimension createPhysicalPageSizeInch(float width, float height) {
      return new Dimension((int)Math.round(UnitConv.in2mpt((double)width)), (int)Math.round(UnitConv.in2mpt((double)height)));
   }

   private static Dimension createPhysicalPageSizeMm(float width, float height) {
      return new Dimension((int)Math.round(UnitConv.mm2mpt((double)width)), (int)Math.round(UnitConv.mm2mpt((double)height)));
   }

   private static Rectangle createLogicalPageRect(int x, int y, int width, int height) {
      return new Rectangle(convert300dpiDotsToMpt(x), convert300dpiDotsToMpt(y), convert300dpiDotsToMpt(width), convert300dpiDotsToMpt(height));
   }

   private static void createPageDefinitions() {
      pageDefinitions = new ArrayList();
      pageDefinitions.add(new PCLPageDefinition("Letter", 2, createPhysicalPageSizeInch(8.5F, 11.0F), createLogicalPageRect(75, 0, 2400, 3300), false));
      defaultPageDefinition = new PCLPageDefinition("Legal", 3, createPhysicalPageSizeInch(8.5F, 14.0F), createLogicalPageRect(75, 0, 2400, 4200), false);
      pageDefinitions.add(defaultPageDefinition);
      pageDefinitions.add(new PCLPageDefinition("Executive", 1, createPhysicalPageSizeInch(7.25F, 10.5F), createLogicalPageRect(75, 0, 2025, 3150), false));
      pageDefinitions.add(new PCLPageDefinition("Ledger", 6, createPhysicalPageSizeInch(11.0F, 17.0F), createLogicalPageRect(75, 0, 3150, 5100), false));
      pageDefinitions.add(new PCLPageDefinition("A5", 25, createPhysicalPageSizeMm(148.0F, 210.0F), createLogicalPageRect(71, 0, 1745, 2480), false));
      pageDefinitions.add(new PCLPageDefinition("A4", 26, createPhysicalPageSizeMm(210.0F, 297.0F), createLogicalPageRect(71, 0, 2338, 3507), false));
      pageDefinitions.add(new PCLPageDefinition("A3", 27, createPhysicalPageSizeMm(297.0F, 420.0F), createLogicalPageRect(71, 0, 3365, 4960), false));
      pageDefinitions.add(new PCLPageDefinition("LetterL", 2, createPhysicalPageSizeInch(11.0F, 8.5F), createLogicalPageRect(60, 0, 3180, 2550), true));
      pageDefinitions.add(new PCLPageDefinition("LegalL", 3, createPhysicalPageSizeInch(14.0F, 8.5F), createLogicalPageRect(60, 0, 4080, 2550), true));
      pageDefinitions.add(new PCLPageDefinition("ExecutiveL", 1, createPhysicalPageSizeInch(10.5F, 7.25F), createLogicalPageRect(60, 0, 3030, 2175), true));
      pageDefinitions.add(new PCLPageDefinition("LedgerL", 6, createPhysicalPageSizeInch(17.0F, 11.0F), createLogicalPageRect(60, 0, 4980, 3300), true));
      pageDefinitions.add(new PCLPageDefinition("A5L", 25, createPhysicalPageSizeMm(210.0F, 148.0F), createLogicalPageRect(59, 0, 2362, 1747), true));
      pageDefinitions.add(new PCLPageDefinition("A4L", 26, createPhysicalPageSizeMm(297.0F, 210.0F), createLogicalPageRect(59, 0, 3389, 2480), true));
      pageDefinitions.add(new PCLPageDefinition("A3L", 27, createPhysicalPageSizeMm(420.0F, 297.0F), createLogicalPageRect(59, 0, 4842, 3507), true));
   }

   static {
      createPageDefinitions();
   }
}
