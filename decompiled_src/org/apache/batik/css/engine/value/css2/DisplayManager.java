package org.apache.batik.css.engine.value.css2;

import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;

public class DisplayManager extends IdentifierManager {
   protected static final StringMap values = new StringMap();

   public boolean isInheritedProperty() {
      return false;
   }

   public boolean isAnimatableProperty() {
      return true;
   }

   public boolean isAdditiveProperty() {
      return false;
   }

   public int getPropertyType() {
      return 15;
   }

   public String getPropertyName() {
      return "display";
   }

   public Value getDefaultValue() {
      return ValueConstants.INLINE_VALUE;
   }

   public StringMap getIdentifiers() {
      return values;
   }

   static {
      values.put("block", ValueConstants.BLOCK_VALUE);
      values.put("compact", ValueConstants.COMPACT_VALUE);
      values.put("inline", ValueConstants.INLINE_VALUE);
      values.put("inline-table", ValueConstants.INLINE_TABLE_VALUE);
      values.put("list-item", ValueConstants.LIST_ITEM_VALUE);
      values.put("marker", ValueConstants.MARKER_VALUE);
      values.put("none", ValueConstants.NONE_VALUE);
      values.put("run-in", ValueConstants.RUN_IN_VALUE);
      values.put("table", ValueConstants.TABLE_VALUE);
      values.put("table-caption", ValueConstants.TABLE_CAPTION_VALUE);
      values.put("table-cell", ValueConstants.TABLE_CELL_VALUE);
      values.put("table-column", ValueConstants.TABLE_COLUMN_VALUE);
      values.put("table-column-group", ValueConstants.TABLE_COLUMN_GROUP_VALUE);
      values.put("table-footer-group", ValueConstants.TABLE_FOOTER_GROUP_VALUE);
      values.put("table-header-group", ValueConstants.TABLE_HEADER_GROUP_VALUE);
      values.put("table-row", ValueConstants.TABLE_ROW_VALUE);
      values.put("table-row-group", ValueConstants.TABLE_ROW_GROUP_VALUE);
   }
}
