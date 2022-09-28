package com.xmlmind.fo.converter;

import com.xmlmind.fo.graphic.Graphic;
import com.xmlmind.fo.graphic.GraphicEnv;
import com.xmlmind.fo.objects.Flow;
import com.xmlmind.fo.objects.PageSequence;
import com.xmlmind.fo.properties.Value;

public interface EventHandler {
   void startDocument() throws Exception;

   void endDocument() throws Exception;

   void characters(String var1, Context var2) throws Exception;

   void startRoot(Value[] var1, Context var2) throws Exception;

   void endRoot(Context var1) throws Exception;

   void startDeclarations(Value[] var1, Context var2) throws Exception;

   void endDeclarations(Context var1) throws Exception;

   void startColorProfile(Value[] var1, Context var2) throws Exception;

   void endColorProfile(Context var1) throws Exception;

   void startPageSequence(PageSequence var1, Value[] var2, Context var3) throws Exception;

   void endPageSequence(Context var1) throws Exception;

   void startLayoutMasterSet(Value[] var1, Context var2) throws Exception;

   void endLayoutMasterSet(Context var1) throws Exception;

   void startPageSequenceMaster(Value[] var1, Context var2) throws Exception;

   void endPageSequenceMaster(Context var1) throws Exception;

   void startSinglePageMasterReference(Value[] var1, Context var2) throws Exception;

   void endSinglePageMasterReference(Context var1) throws Exception;

   void startRepeatablePageMasterReference(Value[] var1, Context var2) throws Exception;

   void endRepeatablePageMasterReference(Context var1) throws Exception;

   void startRepeatablePageMasterAlternatives(Value[] var1, Context var2) throws Exception;

   void endRepeatablePageMasterAlternatives(Context var1) throws Exception;

   void startConditionalPageMasterReference(Value[] var1, Context var2) throws Exception;

   void endConditionalPageMasterReference(Context var1) throws Exception;

   void startSimplePageMaster(Value[] var1, Context var2) throws Exception;

   void endSimplePageMaster(Context var1) throws Exception;

   void startRegionBody(Value[] var1, Context var2) throws Exception;

   void endRegionBody(Context var1) throws Exception;

   void startRegionBefore(Value[] var1, Context var2) throws Exception;

   void endRegionBefore(Context var1) throws Exception;

   void startRegionAfter(Value[] var1, Context var2) throws Exception;

   void endRegionAfter(Context var1) throws Exception;

   void startRegionStart(Value[] var1, Context var2) throws Exception;

   void endRegionStart(Context var1) throws Exception;

   void startRegionEnd(Value[] var1, Context var2) throws Exception;

   void endRegionEnd(Context var1) throws Exception;

   void startFlow(Flow var1, Value[] var2, Context var3) throws Exception;

   void endFlow(Context var1) throws Exception;

   void startStaticContent(Flow var1, Value[] var2, Context var3) throws Exception;

   void endStaticContent(Context var1) throws Exception;

   void startTitle(Value[] var1, Context var2) throws Exception;

   void endTitle(Context var1) throws Exception;

   void startBlock(Value[] var1, Context var2) throws Exception;

   void endBlock(Context var1) throws Exception;

   void startBlockContainer(Value[] var1, Context var2) throws Exception;

   void endBlockContainer(Context var1) throws Exception;

   void startBidiOverride(Value[] var1, Context var2) throws Exception;

   void endBidiOverride(Context var1) throws Exception;

   void startCharacter(Value[] var1, Context var2) throws Exception;

   void endCharacter(Context var1) throws Exception;

   void startInitialPropertySet(Value[] var1, Context var2) throws Exception;

   void endInitialPropertySet(Context var1) throws Exception;

   void startExternalGraphic(Graphic var1, GraphicEnv var2, Value[] var3, Context var4) throws Exception;

   void endExternalGraphic(Context var1) throws Exception;

   void startInstreamForeignObject(Value[] var1, Context var2) throws Exception;

   void endInstreamForeignObject(Graphic var1, GraphicEnv var2, Value[] var3, Context var4) throws Exception;

   void startInline(Value[] var1, Context var2) throws Exception;

   void endInline(Context var1) throws Exception;

   void startInlineContainer(Value[] var1, Context var2) throws Exception;

   void endInlineContainer(Context var1) throws Exception;

   void startLeader(Value[] var1, Context var2) throws Exception;

   void endLeader(Context var1) throws Exception;

   void startPageNumber(Value[] var1, Context var2) throws Exception;

   void endPageNumber(Context var1) throws Exception;

   void startPageNumberCitation(Value[] var1, Context var2) throws Exception;

   void endPageNumberCitation(Context var1) throws Exception;

   void startTableAndCaption(Value[] var1, Context var2) throws Exception;

   void endTableAndCaption(Context var1) throws Exception;

   void startTable(Value[] var1, Context var2) throws Exception;

   void endTable(Context var1) throws Exception;

   void startTableColumn(Value[] var1, Context var2) throws Exception;

   void endTableColumn(Context var1) throws Exception;

   void startTableCaption(Value[] var1, Context var2) throws Exception;

   void endTableCaption(Context var1) throws Exception;

   void startTableHeader(Value[] var1, Context var2) throws Exception;

   void endTableHeader(Context var1) throws Exception;

   void startTableFooter(Value[] var1, Context var2) throws Exception;

   void endTableFooter(Context var1) throws Exception;

   void startTableBody(Value[] var1, Context var2) throws Exception;

   void endTableBody(Context var1) throws Exception;

   void startTableRow(Value[] var1, Context var2) throws Exception;

   void endTableRow(Context var1) throws Exception;

   void startTableCell(Value[] var1, Context var2) throws Exception;

   void endTableCell(Context var1) throws Exception;

   void startListBlock(Value[] var1, Context var2) throws Exception;

   void endListBlock(Context var1) throws Exception;

   void startListItem(Value[] var1, Context var2) throws Exception;

   void endListItem(Context var1) throws Exception;

   void startListItemBody(Value[] var1, Context var2) throws Exception;

   void endListItemBody(Context var1) throws Exception;

   void startListItemLabel(Value[] var1, Context var2) throws Exception;

   void endListItemLabel(Context var1) throws Exception;

   void startBasicLink(Value[] var1, Context var2) throws Exception;

   void endBasicLink(Context var1) throws Exception;

   void startMultiSwitch(Value[] var1, Context var2) throws Exception;

   void endMultiSwitch(Context var1) throws Exception;

   void startMultiCase(Value[] var1, Context var2) throws Exception;

   void endMultiCase(Context var1) throws Exception;

   void startMultiToggle(Value[] var1, Context var2) throws Exception;

   void endMultiToggle(Context var1) throws Exception;

   void startMultiProperties(Value[] var1, Context var2) throws Exception;

   void endMultiProperties(Context var1) throws Exception;

   void startMultiPropertySet(Value[] var1, Context var2) throws Exception;

   void endMultiPropertySet(Context var1) throws Exception;

   void startFloat(Value[] var1, Context var2) throws Exception;

   void endFloat(Context var1) throws Exception;

   void startFootnote(Value[] var1, Context var2) throws Exception;

   void endFootnote(Context var1) throws Exception;

   void startFootnoteBody(Value[] var1, Context var2) throws Exception;

   void endFootnoteBody(Context var1) throws Exception;

   void startWrapper(Value[] var1, Context var2) throws Exception;

   void endWrapper(Context var1) throws Exception;

   void startMarker(Value[] var1, Context var2) throws Exception;

   void endMarker(Context var1) throws Exception;

   void startRetrieveMarker(Value[] var1, Context var2) throws Exception;

   void endRetrieveMarker(Context var1) throws Exception;
}
