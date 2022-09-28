package org.apache.fop.fo;

import java.util.HashMap;
import org.apache.fop.fo.flow.BasicLink;
import org.apache.fop.fo.flow.BidiOverride;
import org.apache.fop.fo.flow.Block;
import org.apache.fop.fo.flow.BlockContainer;
import org.apache.fop.fo.flow.Character;
import org.apache.fop.fo.flow.ExternalGraphic;
import org.apache.fop.fo.flow.Float;
import org.apache.fop.fo.flow.Footnote;
import org.apache.fop.fo.flow.FootnoteBody;
import org.apache.fop.fo.flow.InitialPropertySet;
import org.apache.fop.fo.flow.Inline;
import org.apache.fop.fo.flow.InlineContainer;
import org.apache.fop.fo.flow.InstreamForeignObject;
import org.apache.fop.fo.flow.Leader;
import org.apache.fop.fo.flow.ListBlock;
import org.apache.fop.fo.flow.ListItem;
import org.apache.fop.fo.flow.ListItemBody;
import org.apache.fop.fo.flow.ListItemLabel;
import org.apache.fop.fo.flow.Marker;
import org.apache.fop.fo.flow.MultiCase;
import org.apache.fop.fo.flow.MultiProperties;
import org.apache.fop.fo.flow.MultiPropertySet;
import org.apache.fop.fo.flow.MultiSwitch;
import org.apache.fop.fo.flow.MultiToggle;
import org.apache.fop.fo.flow.PageNumber;
import org.apache.fop.fo.flow.PageNumberCitation;
import org.apache.fop.fo.flow.PageNumberCitationLast;
import org.apache.fop.fo.flow.RetrieveMarker;
import org.apache.fop.fo.flow.RetrieveTableMarker;
import org.apache.fop.fo.flow.Wrapper;
import org.apache.fop.fo.flow.table.Table;
import org.apache.fop.fo.flow.table.TableAndCaption;
import org.apache.fop.fo.flow.table.TableBody;
import org.apache.fop.fo.flow.table.TableCaption;
import org.apache.fop.fo.flow.table.TableCell;
import org.apache.fop.fo.flow.table.TableColumn;
import org.apache.fop.fo.flow.table.TableFooter;
import org.apache.fop.fo.flow.table.TableHeader;
import org.apache.fop.fo.flow.table.TableRow;
import org.apache.fop.fo.pagination.ColorProfile;
import org.apache.fop.fo.pagination.ConditionalPageMasterReference;
import org.apache.fop.fo.pagination.Declarations;
import org.apache.fop.fo.pagination.Flow;
import org.apache.fop.fo.pagination.LayoutMasterSet;
import org.apache.fop.fo.pagination.PageSequence;
import org.apache.fop.fo.pagination.PageSequenceMaster;
import org.apache.fop.fo.pagination.PageSequenceWrapper;
import org.apache.fop.fo.pagination.RegionAfter;
import org.apache.fop.fo.pagination.RegionBefore;
import org.apache.fop.fo.pagination.RegionBody;
import org.apache.fop.fo.pagination.RegionEnd;
import org.apache.fop.fo.pagination.RegionStart;
import org.apache.fop.fo.pagination.RepeatablePageMasterAlternatives;
import org.apache.fop.fo.pagination.RepeatablePageMasterReference;
import org.apache.fop.fo.pagination.Root;
import org.apache.fop.fo.pagination.SimplePageMaster;
import org.apache.fop.fo.pagination.SinglePageMasterReference;
import org.apache.fop.fo.pagination.StaticContent;
import org.apache.fop.fo.pagination.Title;
import org.apache.fop.fo.pagination.bookmarks.Bookmark;
import org.apache.fop.fo.pagination.bookmarks.BookmarkTitle;
import org.apache.fop.fo.pagination.bookmarks.BookmarkTree;
import org.apache.xmlgraphics.util.QName;

public class FOElementMapping extends ElementMapping {
   public static final String URI = "http://www.w3.org/1999/XSL/Format";

   public FOElementMapping() {
      this.namespaceURI = "http://www.w3.org/1999/XSL/Format";
   }

   protected void initialize() {
      if (this.foObjs == null) {
         this.foObjs = new HashMap();
         this.foObjs.put("root", new RootMaker());
         this.foObjs.put("declarations", new DeclarationsMaker());
         this.foObjs.put("color-profile", new ColorProfileMaker());
         this.foObjs.put("bookmark-tree", new BookmarkTreeMaker());
         this.foObjs.put("bookmark", new BookmarkMaker());
         this.foObjs.put("bookmark-title", new BookmarkTitleMaker());
         this.foObjs.put("page-sequence", new PageSequenceMaker());
         this.foObjs.put("layout-master-set", new LayoutMasterSetMaker());
         this.foObjs.put("page-sequence-master", new PageSequenceMasterMaker());
         this.foObjs.put("single-page-master-reference", new SinglePageMasterReferenceMaker());
         this.foObjs.put("repeatable-page-master-reference", new RepeatablePageMasterReferenceMaker());
         this.foObjs.put("repeatable-page-master-alternatives", new RepeatablePageMasterAlternativesMaker());
         this.foObjs.put("conditional-page-master-reference", new ConditionalPageMasterReferenceMaker());
         this.foObjs.put("simple-page-master", new SimplePageMasterMaker());
         this.foObjs.put("region-body", new RegionBodyMaker());
         this.foObjs.put("region-before", new RegionBeforeMaker());
         this.foObjs.put("region-after", new RegionAfterMaker());
         this.foObjs.put("region-start", new RegionStartMaker());
         this.foObjs.put("region-end", new RegionEndMaker());
         this.foObjs.put("flow", new FlowMaker());
         this.foObjs.put("static-content", new StaticContentMaker());
         this.foObjs.put("title", new TitleMaker());
         this.foObjs.put("block", new BlockMaker());
         this.foObjs.put("block-container", new BlockContainerMaker());
         this.foObjs.put("bidi-override", new BidiOverrideMaker());
         this.foObjs.put("character", new CharacterMaker());
         this.foObjs.put("initial-property-set", new InitialPropertySetMaker());
         this.foObjs.put("external-graphic", new ExternalGraphicMaker());
         this.foObjs.put("instream-foreign-object", new InstreamForeignObjectMaker());
         this.foObjs.put("inline", new InlineMaker());
         this.foObjs.put("inline-container", new InlineContainerMaker());
         this.foObjs.put("leader", new LeaderMaker());
         this.foObjs.put("page-number", new PageNumberMaker());
         this.foObjs.put("page-number-citation", new PageNumberCitationMaker());
         this.foObjs.put("page-number-citation-last", new PageNumberCitationLastMaker());
         this.foObjs.put("table-and-caption", new TableAndCaptionMaker());
         this.foObjs.put("table", new TableMaker());
         this.foObjs.put("table-column", new TableColumnMaker());
         this.foObjs.put("table-caption", new TableCaptionMaker());
         this.foObjs.put("table-header", new TableHeaderMaker());
         this.foObjs.put("table-footer", new TableFooterMaker());
         this.foObjs.put("table-body", new TableBodyMaker());
         this.foObjs.put("table-row", new TableRowMaker());
         this.foObjs.put("table-cell", new TableCellMaker());
         this.foObjs.put("list-block", new ListBlockMaker());
         this.foObjs.put("list-item", new ListItemMaker());
         this.foObjs.put("list-item-body", new ListItemBodyMaker());
         this.foObjs.put("list-item-label", new ListItemLabelMaker());
         this.foObjs.put("basic-link", new BasicLinkMaker());
         this.foObjs.put("multi-switch", new MultiSwitchMaker());
         this.foObjs.put("multi-case", new MultiCaseMaker());
         this.foObjs.put("multi-toggle", new MultiToggleMaker());
         this.foObjs.put("multi-properties", new MultiPropertiesMaker());
         this.foObjs.put("multi-property-set", new MultiPropertySetMaker());
         this.foObjs.put("float", new FloatMaker());
         this.foObjs.put("footnote", new FootnoteMaker());
         this.foObjs.put("footnote-body", new FootnoteBodyMaker());
         this.foObjs.put("wrapper", new WrapperMaker());
         this.foObjs.put("marker", new MarkerMaker());
         this.foObjs.put("retrieve-marker", new RetrieveMarkerMaker());
         this.foObjs.put("retrieve-table-marker", new RetrieveTableMarkerMaker());
      }

   }

   public String getStandardPrefix() {
      return "fo";
   }

   public boolean isAttributeProperty(QName attributeName) {
      return true;
   }

   static class RetrieveTableMarkerMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new RetrieveTableMarker(parent);
      }
   }

   static class RetrieveMarkerMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new RetrieveMarker(parent);
      }
   }

   static class MarkerMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new Marker(parent);
      }
   }

   static class WrapperMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new Wrapper(parent);
      }
   }

   static class FootnoteBodyMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new FootnoteBody(parent);
      }
   }

   static class FootnoteMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new Footnote(parent);
      }
   }

   static class FloatMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new Float(parent);
      }
   }

   static class MultiPropertySetMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new MultiPropertySet(parent);
      }
   }

   static class MultiPropertiesMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new MultiProperties(parent);
      }
   }

   static class MultiToggleMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new MultiToggle(parent);
      }
   }

   static class MultiCaseMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new MultiCase(parent);
      }
   }

   static class MultiSwitchMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new MultiSwitch(parent);
      }
   }

   static class BasicLinkMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new BasicLink(parent);
      }
   }

   static class ListItemLabelMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new ListItemLabel(parent);
      }
   }

   static class ListItemBodyMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new ListItemBody(parent);
      }
   }

   static class ListItemMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new ListItem(parent);
      }
   }

   static class ListBlockMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new ListBlock(parent);
      }
   }

   static class TableCellMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new TableCell(parent);
      }
   }

   static class TableRowMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new TableRow(parent);
      }
   }

   static class TableFooterMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new TableFooter(parent);
      }
   }

   static class TableHeaderMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new TableHeader(parent);
      }
   }

   static class TableBodyMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new TableBody(parent);
      }
   }

   static class TableCaptionMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new TableCaption(parent);
      }
   }

   static class TableColumnMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new TableColumn(parent);
      }
   }

   static class TableMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new Table(parent);
      }
   }

   static class TableAndCaptionMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new TableAndCaption(parent);
      }
   }

   static class PageNumberCitationLastMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new PageNumberCitationLast(parent);
      }
   }

   static class PageNumberCitationMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new PageNumberCitation(parent);
      }
   }

   static class PageNumberMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new PageNumber(parent);
      }
   }

   static class LeaderMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new Leader(parent);
      }
   }

   static class InlineContainerMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new InlineContainer(parent);
      }
   }

   static class InlineMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new Inline(parent);
      }
   }

   static class InstreamForeignObjectMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new InstreamForeignObject(parent);
      }
   }

   static class ExternalGraphicMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new ExternalGraphic(parent);
      }
   }

   static class InitialPropertySetMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new InitialPropertySet(parent);
      }
   }

   static class CharacterMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new Character(parent);
      }
   }

   static class BidiOverrideMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new BidiOverride(parent);
      }
   }

   static class BlockContainerMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new BlockContainer(parent);
      }
   }

   static class BlockMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new Block(parent);
      }
   }

   static class TitleMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new Title(parent);
      }
   }

   static class StaticContentMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new StaticContent(parent);
      }
   }

   static class FlowMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new Flow(parent);
      }
   }

   static class RegionEndMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new RegionEnd(parent);
      }
   }

   static class RegionStartMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new RegionStart(parent);
      }
   }

   static class RegionAfterMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new RegionAfter(parent);
      }
   }

   static class RegionBeforeMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new RegionBefore(parent);
      }
   }

   static class RegionBodyMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new RegionBody(parent);
      }
   }

   static class SimplePageMasterMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new SimplePageMaster(parent);
      }
   }

   static class ConditionalPageMasterReferenceMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new ConditionalPageMasterReference(parent);
      }
   }

   static class RepeatablePageMasterAlternativesMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new RepeatablePageMasterAlternatives(parent);
      }
   }

   static class RepeatablePageMasterReferenceMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new RepeatablePageMasterReference(parent);
      }
   }

   static class SinglePageMasterReferenceMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new SinglePageMasterReference(parent);
      }
   }

   static class PageSequenceMasterMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new PageSequenceMaster(parent);
      }
   }

   static class LayoutMasterSetMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new LayoutMasterSet(parent);
      }
   }

   static class PageSequenceMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new PageSequence(parent);
      }
   }

   static class PageSequenceWrapperMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new PageSequenceWrapper(parent);
      }
   }

   static class BookmarkTitleMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new BookmarkTitle(parent);
      }
   }

   static class BookmarkMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new Bookmark(parent);
      }
   }

   static class BookmarkTreeMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new BookmarkTree(parent);
      }
   }

   static class ColorProfileMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new ColorProfile(parent);
      }
   }

   static class DeclarationsMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new Declarations(parent);
      }
   }

   static class RootMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new Root(parent);
      }
   }
}
