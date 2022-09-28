package org.apache.fop.fo.flow.table;

import org.apache.fop.layoutmgr.table.CollapsingBorderModel;

public class ConditionalBorder {
   public static final int NORMAL = 0;
   public static final int LEADING_TRAILING = 1;
   public static final int REST = 2;
   BorderSpecification normal;
   BorderSpecification leadingTrailing;
   BorderSpecification rest;
   private CollapsingBorderModel collapsingBorderModel;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   private ConditionalBorder(BorderSpecification normal, BorderSpecification leadingTrailing, BorderSpecification rest, CollapsingBorderModel collapsingBorderModel) {
      this.normal = normal;
      this.leadingTrailing = leadingTrailing;
      this.rest = rest;
      this.collapsingBorderModel = collapsingBorderModel;
   }

   ConditionalBorder(BorderSpecification borderSpecification, CollapsingBorderModel collapsingBorderModel) {
      this.normal = borderSpecification;
      this.leadingTrailing = this.normal;
      if (borderSpecification.getBorderInfo().getWidth().isDiscard()) {
         this.rest = BorderSpecification.getDefaultBorder();
      } else {
         this.rest = this.leadingTrailing;
      }

      this.collapsingBorderModel = collapsingBorderModel;
   }

   void resolve(ConditionalBorder competitor, boolean withNormal, boolean withLeadingTrailing, boolean withRest) {
      BorderSpecification resolvedBorder;
      if (withNormal) {
         resolvedBorder = this.collapsingBorderModel.determineWinner(this.normal, competitor.normal);
         if (resolvedBorder != null) {
            this.normal = resolvedBorder;
            competitor.normal = resolvedBorder;
         }
      }

      if (withLeadingTrailing) {
         resolvedBorder = this.collapsingBorderModel.determineWinner(this.leadingTrailing, competitor.leadingTrailing);
         if (resolvedBorder != null) {
            this.leadingTrailing = resolvedBorder;
            competitor.leadingTrailing = resolvedBorder;
         }
      }

      if (withRest) {
         resolvedBorder = this.collapsingBorderModel.determineWinner(this.rest, competitor.rest);
         if (resolvedBorder != null) {
            this.rest = resolvedBorder;
            competitor.rest = resolvedBorder;
         }
      }

   }

   void integrateCompetingSegment(ConditionalBorder competitor, boolean withNormal, boolean withLeadingTrailing, boolean withRest) {
      BorderSpecification resolvedBorder;
      if (withNormal) {
         resolvedBorder = this.collapsingBorderModel.determineWinner(this.normal, competitor.normal);
         if (resolvedBorder != null) {
            this.normal = resolvedBorder;
         }
      }

      if (withLeadingTrailing) {
         resolvedBorder = this.collapsingBorderModel.determineWinner(this.leadingTrailing, competitor.leadingTrailing);
         if (resolvedBorder != null) {
            this.leadingTrailing = resolvedBorder;
         }
      }

      if (withRest) {
         resolvedBorder = this.collapsingBorderModel.determineWinner(this.rest, competitor.rest);
         if (resolvedBorder != null) {
            this.rest = resolvedBorder;
         }
      }

   }

   void integrateSegment(ConditionalBorder segment, boolean withNormal, boolean withLeadingTrailing, boolean withRest) {
      if (withNormal) {
         this.normal = this.collapsingBorderModel.determineWinner(this.normal, segment.normal);
         if (!$assertionsDisabled && this.normal == null) {
            throw new AssertionError();
         }
      }

      if (withLeadingTrailing) {
         this.leadingTrailing = this.collapsingBorderModel.determineWinner(this.leadingTrailing, segment.leadingTrailing);
         if (!$assertionsDisabled && this.leadingTrailing == null) {
            throw new AssertionError();
         }
      }

      if (withRest) {
         this.rest = this.collapsingBorderModel.determineWinner(this.rest, segment.rest);
         if (!$assertionsDisabled && this.rest == null) {
            throw new AssertionError();
         }
      }

   }

   ConditionalBorder copy() {
      return new ConditionalBorder(this.normal, this.leadingTrailing, this.rest, this.collapsingBorderModel);
   }

   public String toString() {
      return "{normal: " + this.normal + ", leading: " + this.leadingTrailing + ", rest: " + this.rest + "}";
   }

   static ConditionalBorder getDefaultBorder(CollapsingBorderModel collapsingBorderModel) {
      BorderSpecification defaultBorderSpec = BorderSpecification.getDefaultBorder();
      return new ConditionalBorder(defaultBorderSpec, defaultBorderSpec, defaultBorderSpec, collapsingBorderModel);
   }

   static {
      $assertionsDisabled = !ConditionalBorder.class.desiredAssertionStatus();
   }
}
