package org.apache.fop.fo;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fo.extensions.ExternalDocument;
import org.apache.fop.fo.flow.BasicLink;
import org.apache.fop.fo.flow.Block;
import org.apache.fop.fo.flow.BlockContainer;
import org.apache.fop.fo.flow.Character;
import org.apache.fop.fo.flow.ExternalGraphic;
import org.apache.fop.fo.flow.Footnote;
import org.apache.fop.fo.flow.FootnoteBody;
import org.apache.fop.fo.flow.Inline;
import org.apache.fop.fo.flow.InstreamForeignObject;
import org.apache.fop.fo.flow.Leader;
import org.apache.fop.fo.flow.ListBlock;
import org.apache.fop.fo.flow.ListItem;
import org.apache.fop.fo.flow.PageNumber;
import org.apache.fop.fo.flow.PageNumberCitation;
import org.apache.fop.fo.flow.PageNumberCitationLast;
import org.apache.fop.fo.flow.table.Table;
import org.apache.fop.fo.flow.table.TableBody;
import org.apache.fop.fo.flow.table.TableCell;
import org.apache.fop.fo.flow.table.TableColumn;
import org.apache.fop.fo.flow.table.TableFooter;
import org.apache.fop.fo.flow.table.TableHeader;
import org.apache.fop.fo.flow.table.TableRow;
import org.apache.fop.fo.pagination.Flow;
import org.apache.fop.fo.pagination.PageSequence;
import org.apache.fop.fonts.FontEventAdapter;
import org.apache.fop.fonts.FontInfo;
import org.xml.sax.SAXException;

public abstract class FOEventHandler {
   protected FOUserAgent foUserAgent;
   protected FontInfo fontInfo;

   public FOEventHandler(FOUserAgent foUserAgent) {
      this.foUserAgent = foUserAgent;
      this.fontInfo = new FontInfo();
      this.fontInfo.setEventListener(new FontEventAdapter(foUserAgent.getEventBroadcaster()));
   }

   public FOUserAgent getUserAgent() {
      return this.foUserAgent;
   }

   public FontInfo getFontInfo() {
      return this.fontInfo;
   }

   public void startDocument() throws SAXException {
   }

   public void endDocument() throws SAXException {
   }

   public void startPageSequence(PageSequence pageSeq) {
   }

   public void endPageSequence(PageSequence pageSeq) {
   }

   public void startPageNumber(PageNumber pagenum) {
   }

   public void endPageNumber(PageNumber pagenum) {
   }

   public void startPageNumberCitation(PageNumberCitation pageCite) {
   }

   public void endPageNumberCitation(PageNumberCitation pageCite) {
   }

   public void startPageNumberCitationLast(PageNumberCitationLast pageLast) {
   }

   public void endPageNumberCitationLast(PageNumberCitationLast pageLast) {
   }

   public void startFlow(Flow fl) {
   }

   public void endFlow(Flow fl) {
   }

   public void startBlock(Block bl) {
   }

   public void endBlock(Block bl) {
   }

   public void startBlockContainer(BlockContainer blc) {
   }

   public void endBlockContainer(BlockContainer blc) {
   }

   public void startInline(Inline inl) {
   }

   public void endInline(Inline inl) {
   }

   public void startTable(Table tbl) {
   }

   public void endTable(Table tbl) {
   }

   public void startColumn(TableColumn tc) {
   }

   public void endColumn(TableColumn tc) {
   }

   public void startHeader(TableHeader header) {
   }

   public void endHeader(TableHeader header) {
   }

   public void startFooter(TableFooter footer) {
   }

   public void endFooter(TableFooter footer) {
   }

   public void startBody(TableBody body) {
   }

   public void endBody(TableBody body) {
   }

   public void startRow(TableRow tr) {
   }

   public void endRow(TableRow tr) {
   }

   public void startCell(TableCell tc) {
   }

   public void endCell(TableCell tc) {
   }

   public void startList(ListBlock lb) {
   }

   public void endList(ListBlock lb) {
   }

   public void startListItem(ListItem li) {
   }

   public void endListItem(ListItem li) {
   }

   public void startListLabel() {
   }

   public void endListLabel() {
   }

   public void startListBody() {
   }

   public void endListBody() {
   }

   public void startStatic() {
   }

   public void endStatic() {
   }

   public void startMarkup() {
   }

   public void endMarkup() {
   }

   public void startLink(BasicLink basicLink) {
   }

   public void endLink() {
   }

   public void image(ExternalGraphic eg) {
   }

   public void pageRef() {
   }

   public void foreignObject(InstreamForeignObject ifo) {
   }

   public void startFootnote(Footnote footnote) {
   }

   public void endFootnote(Footnote footnote) {
   }

   public void startFootnoteBody(FootnoteBody body) {
   }

   public void endFootnoteBody(FootnoteBody body) {
   }

   public void leader(Leader l) {
   }

   public void character(Character c) {
   }

   public void characters(char[] data, int start, int length) {
   }

   public void startExternalDocument(ExternalDocument document) {
   }

   public void endExternalDocument(ExternalDocument document) {
   }
}
