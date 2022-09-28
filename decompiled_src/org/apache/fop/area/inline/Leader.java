package org.apache.fop.area.inline;

public class Leader extends InlineArea {
   private int ruleStyle = 133;
   private int ruleThickness = 1000;

   public void setRuleStyle(int style) {
      this.ruleStyle = style;
   }

   public void setRuleStyle(String style) {
      if ("dotted".equalsIgnoreCase(style)) {
         this.setRuleStyle(36);
      } else if ("dashed".equalsIgnoreCase(style)) {
         this.setRuleStyle(31);
      } else if ("solid".equalsIgnoreCase(style)) {
         this.setRuleStyle(133);
      } else if ("double".equalsIgnoreCase(style)) {
         this.setRuleStyle(37);
      } else if ("groove".equalsIgnoreCase(style)) {
         this.setRuleStyle(55);
      } else if ("ridge".equalsIgnoreCase(style)) {
         this.setRuleStyle(119);
      } else if ("none".equalsIgnoreCase(style)) {
         this.setRuleStyle(95);
      }

   }

   public void setRuleThickness(int rt) {
      this.ruleThickness = rt;
   }

   public int getRuleStyle() {
      return this.ruleStyle;
   }

   public String getRuleStyleAsString() {
      switch (this.getRuleStyle()) {
         case 31:
            return "dashed";
         case 36:
            return "dotted";
         case 37:
            return "double";
         case 55:
            return "groove";
         case 95:
            return "none";
         case 119:
            return "ridge";
         case 133:
            return "solid";
         default:
            throw new IllegalStateException("Unsupported rule style: " + this.getRuleStyle());
      }
   }

   public int getRuleThickness() {
      return this.ruleThickness;
   }
}
