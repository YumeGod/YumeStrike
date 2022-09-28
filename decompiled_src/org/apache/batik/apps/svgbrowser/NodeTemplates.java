package org.apache.batik.apps.svgbrowser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NodeTemplates {
   public static final String VALUE = "Value";
   public static final String NAME = "Name";
   public static final String TYPE = "Type";
   public static final String DESCRIPTION = "Description";
   public static final String CATEGORY = "Category";
   public static final String BASIC_SHAPES = "Basic Shapes";
   public static final String LINKING = "Linking";
   public static final String TEXT = "Text";
   public static final String ANIMATION = "Animation";
   public static final String CLIP_MASK_COMPOSITE = "Clipping, Masking and Compositing";
   public static final String COLOR = "Color";
   public static final String INTERACTIVITY = "Interactivity";
   public static final String FONTS = "Fonts";
   public static final String DOCUMENT_STRUCTURE = "Document Structure";
   public static final String FILTER_EFFECTS = "Filter Effects";
   public static final String EXTENSIBILITY = "Extensibility";
   public static final String GRADIENTS_AND_PATTERNS = "Gradients and Patterns";
   public static final String PAINTING = "Painting: Filling, Stroking and Marker Symbols";
   public static final String METADATA = "Metadata";
   public static final String PATHS = "Paths";
   public static final String SCRIPTING = "Scripting";
   public static final String STYLING = "Styling";
   private Map nodeTemplatesMap = new HashMap();
   private ArrayList categoriesList = new ArrayList();
   public static String rectMemberName = "rectElement";
   public static String rectElementValue = "<rect width=\"0\" height=\"0\"/>";
   public static String rectElementName = "rect";
   public static short rectElementType = 1;
   public static String rectElementCategory = "Basic Shapes";
   public static String rectElementDescription = "Rect";
   public static String circleMemberName = "circleElement";
   public static String circleElementValue = "<circle r=\"0\"/>";
   public static String circleElementName = "circle";
   public short circleElementType = 1;
   public static String circleElementCategory = "Basic Shapes";
   public static String circleElementDescription = "Circle";
   public static String lineElementMemberName = "lineElement";
   public static String lineElementName = "line";
   public static String lineElementValue = "<line x1=\"0\" y1=\"0\" x2=\"0\" y2=\"0\"/>";
   public static short lineElementType = 1;
   public static String lineElementCategory = "Basic Shapes";
   public static String lineElementDescription = "Text";
   public static String pathElementMemberName = "pathElement";
   public static String pathElementName = "path";
   public static String pathElementValue = "<path d=\"M0,0\"/>";
   public static short pathElementType = 1;
   public static String pathElementCategory = "Paths";
   public static String pathElementDescription = "Path";
   public static String groupElementMemberName = "groupElement";
   public static String groupElementName = "g";
   public static String groupElementValue = "<g/>";
   public static short groupElementType = 1;
   public static String groupElementCategory = "Document Structure";
   public static String groupElementDescription = "Group";
   public static String ellipseElementMemberName = "ellipseElement";
   public static String ellipseElementName = "ellipse";
   public static String ellipseElementValue = "<ellipse/>";
   public static short ellipseElementType = 1;
   public static String ellipseElementCategory = "Basic Shapes";
   public static String ellipseElementDescription = "Ellipse";
   public static String imageElementMemberName = "imageElement";
   public static String imageElementName = "image";
   public static String imageElementValue = "<image xlink:href=\"file/c://\"/>";
   public static short imageElementType = 1;
   public static String imageElementCategory = "Document Structure";
   public static String imageElementDescription = "Image";
   public static String polygonElementMemberName = "polygonElement";
   public static String polygonElementName = "polygon";
   public static String polygonElementValue = "<polygon/>";
   public static short polygonElementType = 1;
   public static String polygonElementCategory = "Basic Shapes";
   public static String polygonElementDescription = "Polygon";
   public static String polylineElementMemberName = "polylineElement";
   public static String polylineElementName = "polyline";
   public static String polylineElementValue = "<polyline/>";
   public static short polylineElementType = 1;
   public static String polylineElementCategory = "Basic Shapes";
   public static String polylineElementDescription = "Polyline";
   public static String textElementMemberName = "textElement";
   public static String textElementName = "text";
   public static String textElementValue = "<text> </text>";
   public static short textElementType = 1;
   public static String textElementCategory = "Text";
   public static String textElementDescription = "Text";
   public static String tRefElementMemberName = "tRefElement";
   public static String tRefElementName = "tref";
   public static String tRefElementValue = "<tref/>";
   public static short tRefElementType = 1;
   public static String tRefElementCategory = "Text";
   public static String tRefElementDescription = "TRef";
   public static String tspanElementMemberName = "tspanElement";
   public static String tspanElementName = "tspan";
   public static String tspanElementValue = "<tspan/>";
   public static short tspanElementType = 1;
   public static String tspanElementCategory = "Text";
   public static String tspanElementDescription = "TSpan";
   public static String textPathElementMemberName = "textPathElement";
   public static String textPathElementName = "textPath";
   public static String textPathElementValue = "<textPath/>";
   public static short textPathElementType = 1;
   public static String textPathElementCategory = "Text";
   public static String textPathElementDescription = "TextPath";
   public static String svgElementMemberName = "svgElement";
   public static String svgElementName = "svg";
   public static String svgElementValue = "<svg/>";
   public static short svgElementType = 1;
   public static String svgElementCategory = "Document Structure";
   public static String svgElementDescription = "svg";
   public static String feBlendElementMemberName = "feBlendElement";
   public static String feBlendElementName = "feBlend";
   public static String feBlendElementValue = "<feBlend/>";
   public static short feBlendElementType = 1;
   public static String feBlendElementCategory = "Filter Effects";
   public static String feBlendElementDescription = "FeBlend";
   public static String feColorMatrixElementMemberName = "feColorMatrixElement";
   public static String feColorMatrixElementName = "feColorMatrix";
   public static String feColorMatrixElementValue = "<feColorMatrix/>";
   public static short feColorMatrixElementType = 1;
   public static String feColorMatrixElementCategory = "Filter Effects";
   public static String feColorMatrixElementDescription = "FeColorMatrix";
   public static String feComponentTransferElementMemberName = "feComponentTransferElement";
   public static String feComponentTransferElementName = "feComponentTransfer";
   public static String feComponentTransferElementValue = "<feComponentTransfer/>";
   public static short feComponentTransferElementType = 1;
   public static String feComponentTransferElementCategory = "Filter Effects";
   public static String feComponentTransferElementDescription = "FeComponentTransfer";
   public static String feCompositeElementMemberName = "feCompositeElement";
   public static String feCompositeElementName = "feComposite";
   public static String feCompositeElementValue = "<feComposite/>";
   public static short feCompositeElementType = 1;
   public static String feCompositeElementCategory = "Filter Effects";
   public static String feCompositeElementDescription = "FeComposite";
   public static String feConvolveMatrixElementMemberName = "feConvolveMatrixElement";
   public static String feConvolveMatrixElementName = "feConvolveMatrix";
   public static String feConvolveMatrixElementValue = "<feConvolveMatrix/>";
   public static short feConvolveMatrixElementType = 1;
   public static String feConvolveMatrixElementCategory = "Filter Effects";
   public static String feConvolveMatrixElementDescription = "FeConvolveMatrix";
   public static String feDiffuseLightingElementMemberName = "feDiffuseLightingElement";
   public static String feDiffuseLightingElementName = "feDiffuseLighting";
   public static String feDiffuseLightingElementValue = "<feDiffuseLighting/>";
   public static short feDiffuseLightingElementType = 1;
   public static String feDiffuseLightingElementCategory = "Filter Effects";
   public static String feDiffuseLightingElementDescription = "FeDiffuseLighting";
   public static String feDisplacementMapElementMemberName = "feDisplacementMapElement";
   public static String feDisplacementMapElementName = "feDisplacementMap";
   public static String feDisplacementMapElementValue = "<feDisplacementMap/>";
   public static short feDisplacementMapElementType = 1;
   public static String feDisplacementMapElementCategory = "Filter Effects";
   public static String feDisplacementMapElementDescription = "FeDisplacementMap";
   public static String feDistantLightElementMemberName = "feDistantLightElement";
   public static String feDistantLightElementName = "feDistantLight";
   public static String feDistantLightElementValue = "<feDistantLight/>";
   public static short feDistantLightElementType = 1;
   public static String feDistantLightElementCategory = "Filter Effects";
   public static String feDistantLightElementDescription = "FeDistantLight";
   public static String feFloodElementMemberName = "feFloodElement";
   public static String feFloodElementName = "feFlood";
   public static String feFloodElementValue = "<feFlood/>";
   public static short feFloodElementType = 1;
   public static String feFloodElementCategory = "Filter Effects";
   public static String feFloodElementDescription = "FeFlood";
   public static String feFuncAElementMemberName = "feFuncAElement";
   public static String feFuncAElementName = "feFuncA";
   public static String feFuncAElementValue = "<feFuncA/>";
   public static short feFuncAElementType = 1;
   public static String feFuncAElementCategory = "Filter Effects";
   public static String feFuncAElementDescription = "FeFuncA";
   public static String feFuncBElementMemberName = "feFuncBElement";
   public static String feFuncBElementName = "feFuncB";
   public static String feFuncBElementValue = "<feFuncB/>";
   public static short feFuncBElementType = 1;
   public static String feFuncBElementCategory = "Filter Effects";
   public static String feFuncBElementDescription = "FeFuncB";
   public static String feFuncGElementMemberName = "feFuncGElement";
   public static String feFuncGElementName = "feFuncG";
   public static String feFuncGElementValue = "<feFuncG/>";
   public static short feFuncGElementType = 1;
   public static String feFuncGElementCategory = "Filter Effects";
   public static String feFuncGElementDescription = "FeFuncG";
   public static String feFuncRElementMemberName = "feFuncRElement";
   public static String feFuncRElementName = "feFuncR";
   public static String feFuncRElementValue = "<feFuncR/>";
   public static short feFuncRElementType = 1;
   public static String feFuncRElementCategory = "Filter Effects";
   public static String feFuncRElementDescription = "FeFuncR";
   public static String feGaussianBlurElementMemberName = "feGaussianBlurElement";
   public static String feGaussianBlurElementName = "feGaussianBlur";
   public static String feGaussianBlurElementValue = "<feGaussianBlur/>";
   public static short feGaussianBlurElementType = 1;
   public static String feGaussianBlurElementCategory = "Filter Effects";
   public static String feGaussianBlurElementDescription = "FeGaussianBlur";
   public static String feImageElementMemberName = "feImageElement";
   public static String feImageElementName = "feImage";
   public static String feImageElementValue = "<feImage/>";
   public static short feImageElementType = 1;
   public static String feImageElementCategory = "Filter Effects";
   public static String feImageElementDescription = "FeImage";
   public static String feMergeElementMemberName = "feImageElement";
   public static String feMergeElementName = "feMerge";
   public static String feMergeElementValue = "<feMerge/>";
   public static short feMergeElementType = 1;
   public static String feMergeElementCategory = "Filter Effects";
   public static String feMergeElementDescription = "FeMerge";
   public static String feMergeNodeElementMemberName = "feMergeNodeElement";
   public static String feMergeNodeElementName = "feMergeNode";
   public static String feMergeNodeElementValue = "<feMergeNode/>";
   public static short feMergeNodeElementType = 1;
   public static String feMergeNodeElementCategory = "Filter Effects";
   public static String feMergeNodeElementDescription = "FeMergeNode";
   public static String feMorphologyElementMemberName = "feMorphologyElement";
   public static String feMorphologyElementName = "feMorphology";
   public static String feMorphologyElementValue = "<feMorphology/>";
   public static short feMorphologyElementType = 1;
   public static String feMorphologyElementCategory = "Filter Effects";
   public static String feMorphologyElementDescription = "FeMorphology";
   public static String feOffsetElementMemberName = "feMorphologyElement";
   public static String feOffsetElementName = "feOffset";
   public static String feOffsetElementValue = "<feOffset/>";
   public static short feOffsetElementType = 1;
   public static String feOffsetElementCategory = "Filter Effects";
   public static String feOffsetElementDescription = "FeOffset";
   public static String fePointLightElementMemberName = "fePointLightElement";
   public static String fePointLightElementName = "fePointLight";
   public static String fePointLightElementValue = "<fePointLight/>";
   public static short fePointLightElementType = 1;
   public static String fePointLightElementCategory = "Filter Effects";
   public static String fePointLightElementDescription = "FePointLight";
   public static String feSpecularLightingElementMemberName = "fePointLightElement";
   public static String feSpecularLightingElementName = "feSpecularLighting";
   public static String feSpecularLightingElementValue = "<feSpecularLighting/>";
   public static short feSpecularLightingElementType = 1;
   public static String feSpecularLightingElementCategory = "Filter Effects";
   public static String feSpecularLightingElementDescription = "FeSpecularLighting";
   public static String feSpotLightElementMemberName = "feSpotLightElement";
   public static String feSpotLightElementName = "feSpotLight";
   public static String feSpotLightElementValue = "<feSpotLight/>";
   public static short feSpotLightElementType = 1;
   public static String feSpotLightElementCategory = "Filter Effects";
   public static String feSpotLightElementDescription = "FeSpotLight";
   public static String feTileElementMemberName = "feTileElement";
   public static String feTileElementName = "feTile";
   public static String feTileElementValue = "<feTile/>";
   public static short feTileElementType = 1;
   public static String feTileElementCategory = "Filter Effects";
   public static String feTileElementDescription = "FeTile";
   public static String feTurbulenceElementMemberName = "feTurbulenceElement";
   public static String feTurbulenceElementName = "feTurbulence";
   public static String feTurbulenceElementValue = "<feTurbulence/>";
   public static short feTurbulenceElementType = 1;
   public static String feTurbulenceElementCategory = "Filter Effects";
   public static String feTurbulenceElementDescription = "FeTurbulence";
   public static String filterElementMemberName = "filterElement";
   public static String filterElementName = "filter";
   public static String filterElementValue = "<filter/>";
   public static short filterElementType = 1;
   public static String filterElementCategory = "Filter Effects";
   public static String filterElementDescription = "Filter";
   public static String aElementMemberName = "aElement";
   public static String aElementName = "a";
   public static String aElementValue = "<a/>";
   public static short aElementType = 1;
   public static String aElementCategory = "Linking";
   public static String aElementDescription = "A";
   public static String altGlyphElementMemberName = "altGlyphElement";
   public static String altGlyphElementName = "altGlyph";
   public static String altGlyphElementValue = "<altGlyph/>";
   public static short altGlyphElementType = 1;
   public static String altGlyphElementCategory = "Text";
   public static String altGlyphElementDescription = "AltGlyph";
   public static String altGlyphDefElementMemberName = "altGlyphDefElement";
   public static String altGlyphDefElementName = "altGlyphDef";
   public static String altGlyphDefElementValue = "<altGlyphDef/>";
   public static short altGlyphDefElementType = 1;
   public static String altGlyphDefElementCategory = "Text";
   public static String altGlyphDefElementDescription = "AltGlyphDef";
   public static String altGlyphItemElementMemberName = "altGlyphItemElement";
   public static String altGlyphItemElementName = "altGlyphItem";
   public static String altGlyphItemElementValue = "<altGlyphItem/>";
   public static short altGlyphItemElementType = 1;
   public static String altGlyphItemElementCategory = "Text";
   public static String altGlyphItemElementDescription = "AltGlyphItem";
   public static String clipPathElementMemberName = "clipPathElement";
   public static String clipPathElementName = "clipPath";
   public static String clipPathElementValue = "<clipPath/>";
   public static short clipPathElementType = 1;
   public static String clipPathElementCategory = "Clipping, Masking and Compositing";
   public static String clipPathElementDescription = "ClipPath";
   public static String colorProfileElementMemberName = "colorProfileElement";
   public static String colorProfileElementName = "color-profile";
   public static String colorProfileElementValue = "<color-profile/>";
   public static short colorProfileElementType = 1;
   public static String colorProfileElementCategory = "Color";
   public static String colorProfileElementDescription = "ColorProfile";
   public static String cursorElementMemberName = "cursorElement";
   public static String cursorElementName = "cursor";
   public static String cursorElementValue = "<cursor/>";
   public static short cursorElementType = 1;
   public static String cursorElementCategory = "Interactivity";
   public static String cursorElementDescription = "Cursor";
   public static String definitionSrcElementMemberName = "definitionSrcElement";
   public static String definitionSrcElementName = "definition-src";
   public static String definitionSrcElementValue = "<definition-src/>";
   public static short definitionSrcElementType = 1;
   public static String definitionSrcElementCategory = "Fonts";
   public static String definitionSrcElementDescription = "DefinitionSrc";
   public static String defsElementMemberName = "defsElement";
   public static String defsElementName = "defs";
   public static String defsElementValue = "<defs/>";
   public static short defsElementType = 1;
   public static String defsElementCategory = "Document Structure";
   public static String defsElementDescription = "Defs";
   public static String descElementMemberName = "descElement";
   public static String descElementName = "desc";
   public static String descElementValue = "<desc/>";
   public static short descElementType = 1;
   public static String descElementCategory = "Document Structure";
   public static String descElementDescription = "Desc";
   public static String foreignObjectElementMemberName = "foreignObjectElement";
   public static String foreignObjectElementName = "foreignObject";
   public static String foreignObjectElementValue = "<foreignObject/>";
   public static short foreignObjectElementType = 1;
   public static String foreignObjectElementCategory = "Extensibility";
   public static String foreignObjectElementDescription = "ForeignObject";
   public static String glyphElementMemberName = "glyphElement";
   public static String glyphElementName = "glyph";
   public static String glyphElementValue = "<glyph/>";
   public static short glyphElementType = 1;
   public static String glyphElementCategory = "Fonts";
   public static String glyphElementDescription = "Glyph";
   public static String glyphRefElementMemberName = "glyphRefElement";
   public static String glyphRefElementName = "glyphRef";
   public static String glyphRefElementValue = "<glyphRef/>";
   public static short glyphRefElementType = 1;
   public static String glyphRefElementCategory = "Text";
   public static String glyphRefElementDescription = "GlyphRef";
   public static String hkernElementMemberName = "hkernElement";
   public static String hkernElementName = "hkern";
   public static String hkernElementValue = "<hkern/>";
   public static short hkernElementType = 1;
   public static String hkernElementCategory = "Fonts";
   public static String hkernElementDescription = "Hkern";
   public static String linearGradientElementMemberName = "linearGradientElement";
   public static String linearGradientElementName = "linearGradient";
   public static String linearGradientElementValue = "<linearGradient/>";
   public static short linearGradientElementType = 1;
   public static String linearGradientElementCategory = "Gradients and Patterns";
   public static String linearGradientElementDescription = "LinearGradient";
   public static String markerElementMemberName = "markerElement";
   public static String markerElementName = "marker";
   public static String markerElementValue = "<marker/>";
   public static short markerElementType = 1;
   public static String markerElementCategory = "Painting: Filling, Stroking and Marker Symbols";
   public static String markerElementDescription = "Marker";
   public static String maskElementMemberName = "maskElement";
   public static String maskElementName = "mask";
   public static String maskElementValue = "<mask/>";
   public static short maskElementType = 1;
   public static String maskElementCategory = "Clipping, Masking and Compositing";
   public static String maskElementDescription = "Mask";
   public static String metadataElementMemberName = "metadataElement";
   public static String metadataElementName = "metadata";
   public static String metadataElementValue = "<metadata/>";
   public static short metadataElementType = 1;
   public static String metadataElementCategory = "Metadata";
   public static String metadataElementDescription = "Metadata";
   public static String missingGlyphElementMemberName = "missingGlyphElement";
   public static String missingGlyphElementName = "missing-glyph";
   public static String missingGlyphElementValue = "<missing-glyph/>";
   public static short missingGlyphElementType = 1;
   public static String missingGlyphElementCategory = "Fonts";
   public static String missingGlyphElementDescription = "MissingGlyph";
   public static String mpathElementMemberName = "mpathElement";
   public static String mpathElementName = "mpath";
   public static String mpathElementValue = "<mpath/>";
   public static short mpathElementType = 1;
   public static String mpathElementCategory = "Animation";
   public static String mpathElementDescription = "Mpath";
   public static String patternElementMemberName = "patternElement";
   public static String patternElementName = "pattern";
   public static String patternElementValue = "<pattern/>";
   public static short patternElementType = 1;
   public static String patternElementCategory = "Gradients and Patterns";
   public static String patternElementDescription = "Pattern";
   public static String radialGradientElementMemberName = "radialGradientElement";
   public static String radialGradientElementName = "radialGradient";
   public static String radialGradientElementValue = "<radialGradient/>";
   public static short radialGradientElementType = 1;
   public static String radialGradientElementCategory = "Gradients and Patterns";
   public static String radialGradientElementDescription = "RadialGradient";
   public static String scriptElementMemberName = "scriptElement";
   public static String scriptElementName = "script";
   public static String scriptElementValue = "<script/>";
   public static short scriptElementType = 1;
   public static String scriptElementCategory = "Scripting";
   public static String scriptElementDescription = "script";
   public static String setElementMemberName = "setElement";
   public static String setElementName = "set";
   public static String setElementValue = "<set attributeName=\"fill\" from=\"white\" to=\"black\" dur=\"1s\"/>";
   public static short setElementType = 1;
   public static String setElementCategory = "Animation";
   public static String setElementDescription = "set";
   public static String stopElementMemberName = "stopElement";
   public static String stopElementName = "stop";
   public static String stopElementValue = "<stop/>";
   public static short stopElementType = 1;
   public static String stopElementCategory = "Gradients and Patterns";
   public static String stopElementDescription = "Stop";
   public static String styleElementMemberName = "styleElement";
   public static String styleElementName = "style";
   public static String styleElementValue = "<style/>";
   public static short styleElementType = 1;
   public static String styleElementCategory = "Styling";
   public static String styleElementDescription = "Style";
   public static String switchElementMemberName = "switchElement";
   public static String switchElementName = "switch";
   public static String switchElementValue = "<switch/>";
   public static short switchElementType = 1;
   public static String switchElementCategory = "Document Structure";
   public static String switchElementDescription = "Switch";
   public static String symbolElementMemberName = "symbolElement";
   public static String symbolElementName = "symbol";
   public static String symbolElementValue = "<symbol/>";
   public static short symbolElementType = 1;
   public static String symbolElementCategory = "Document Structure";
   public static String symbolElementDescription = "Symbol";
   public static String titleElementMemberName = "titleElement";
   public static String titleElementName = "title";
   public static String titleElementValue = "<title/>";
   public static short titleElementType = 1;
   public static String titleElementCategory = "Document Structure";
   public static String titleElementDescription = "Title";
   public static String useElementMemberName = "useElement";
   public static String useElementName = "use";
   public static String useElementValue = "<use/>";
   public static short useElementType = 1;
   public static String useElementCategory = "Document Structure";
   public static String useElementDescription = "Use";
   public static String viewElementMemberName = "viewElement";
   public static String viewElementName = "view";
   public static String viewElementValue = "<view/>";
   public static short viewElementType = 1;
   public static String viewElementCategory = "Linking";
   public static String viewElementDescription = "View";
   public static String vkernElementMemberName = "vkernElement";
   public static String vkernElementName = "vkern";
   public static String vkernElementValue = "<vkern/>";
   public static short vkernElementType = 1;
   public static String vkernElementCategory = "Fonts";
   public static String vkernElementDescription = "Vkern";
   public static String fontElementMemberName = "fontElement";
   public static String fontElementName = "font";
   public static String fontElementValue = "<font/>";
   public static short fontElementType = 1;
   public static String fontElementCategory = "Fonts";
   public static String fontElementDescription = "Font";
   public static String fontFaceElementMemberName = "fontFaceElement";
   public static String fontFaceElementName = "font-face";
   public static String fontFaceElementValue = "<font-face/>";
   public static short fontFaceElementType = 1;
   public static String fontFaceElementCategory = "Fonts";
   public static String fontFaceElementDescription = "FontFace";
   public static String fontFaceFormatElementMemberName = "fontFaceFormatElement";
   public static String fontFaceFormatElementName = "font-face-format";
   public static String fontFaceFormatElementValue = "<font-face-format/>";
   public static short fontFaceFormatElementType = 1;
   public static String fontFaceFormatElementCategory = "Fonts";
   public static String fontFaceFormatElementDescription = "FontFaceFormat";
   public static String fontFaceNameElementMemberName = "fontFaceNameElement";
   public static String fontFaceNameElementName = "font-face-name";
   public static String fontFaceNameElementValue = "<font-face-name/>";
   public static short fontFaceNameElementType = 1;
   public static String fontFaceNameElementCategory = "Fonts";
   public static String fontFaceNameElementDescription = "FontFaceName";
   public static String fontFaceSrcElementMemberName = "fontFaceSrcElement";
   public static String fontFaceSrcElementName = "font-face-src";
   public static String fontFaceSrcElementValue = "<font-face-src/>";
   public static short fontFaceSrcElementType = 1;
   public static String fontFaceSrcElementCategory = "Fonts";
   public static String fontFaceSrcElementDescription = "FontFaceSrc";
   public static String fontFaceUriElementMemberName = "fontFaceUriElement";
   public static String fontFaceUriElementName = "font-face-uri";
   public static String fontFaceUriElementValue = "<font-face-uri/>";
   public static short fontFaceUriElementType = 1;
   public static String fontFaceUriElementCategory = "Fonts";
   public static String fontFaceUriElementDescription = "FontFaceUri";
   public static String animateElementMemberName = "fontFaceUriElement";
   public static String animateElementName = "animate";
   public static String animateElementValue = "<animate attributeName=\"fill\" from=\"white\" to=\"black\" dur=\"1s\"/>";
   public static short animateElementType = 1;
   public static String animateElementCategory = "Animation";
   public static String animateElementDescription = "Animate";
   public static String animateColorElementMemberName = "animateColorElement";
   public static String animateColorElementName = "animateColor";
   public static String animateColorElementValue = "<animateColor attributeName=\"fill\" from=\"white\" to=\"black\" dur=\"1s\"/>";
   public static short animateColorElementType = 1;
   public static String animateColorElementCategory = "Animation";
   public static String animateColorElementDescription = "AnimateColor";
   public static String animateMotionElementMemberName = "animateMotionElement";
   public static String animateMotionElementName = "animateMotion";
   public static String animateMotionElementValue = "<animateMotion dur=\"1s\" path=\"M0,0\"/>";
   public static short animateMotionElementType = 1;
   public static String animateMotionElementCategory = "Animation";
   public static String animateMotionElementDescription = "AnimateMotion";
   public static String animateTransformElementMemberName = "animateTransformElement";
   public static String animateTransformElementName = "animateTransform";
   public static String animateTransformElementValue = "<animateTransform attributeName=\"transform\" type=\"rotate\" from=\"0\" to=\"0\" dur=\"1s\"/>";
   public static short animateTransformElementType = 1;
   public static String animateTransformElementCategory = "Animation";
   public static String animateTransformElementDescription = "AnimateTransform";
   // $FF: synthetic field
   static Class class$java$lang$String;

   public NodeTemplates() {
      this.categoriesList.add("Document Structure");
      this.categoriesList.add("Styling");
      this.categoriesList.add("Paths");
      this.categoriesList.add("Basic Shapes");
      this.categoriesList.add("Text");
      this.categoriesList.add("Painting: Filling, Stroking and Marker Symbols");
      this.categoriesList.add("Color");
      this.categoriesList.add("Gradients and Patterns");
      this.categoriesList.add("Clipping, Masking and Compositing");
      this.categoriesList.add("Filter Effects");
      this.categoriesList.add("Interactivity");
      this.categoriesList.add("Linking");
      this.categoriesList.add("Scripting");
      this.categoriesList.add("Animation");
      this.categoriesList.add("Fonts");
      this.categoriesList.add("Metadata");
      this.categoriesList.add("Extensibility");
      this.initializeTemplates();
   }

   private void initializeTemplates() {
      Field[] var1 = this.getClass().getDeclaredFields();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         Field var3 = var1[var2];

         try {
            if (var3.getType() == (class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String) && var3.getName().endsWith("MemberName")) {
               boolean var4 = var3.isAccessible();
               var3.setAccessible(true);
               String var5 = var3.get(this).toString();
               String var6 = this.getClass().getField(var5 + "Value").get(this).toString();
               String var7 = this.getClass().getField(var5 + "Name").get(this).toString();
               short var8 = (Short)this.getClass().getField(var5 + "Type").get(this);
               String var9 = this.getClass().getField(var5 + "Description").get(this).toString();
               String var10 = this.getClass().getField(var5 + "Category").get(this).toString();
               NodeTemplateDescriptor var11 = new NodeTemplateDescriptor(var7, var6, var8, var10, var9);
               this.nodeTemplatesMap.put(var11.getName(), var11);
               var3.setAccessible(var4);
            }
         } catch (IllegalArgumentException var12) {
            var12.printStackTrace();
         } catch (IllegalAccessException var13) {
            var13.printStackTrace();
         } catch (SecurityException var14) {
            var14.printStackTrace();
         } catch (NoSuchFieldException var15) {
            var15.printStackTrace();
         }
      }

   }

   public ArrayList getCategories() {
      return this.categoriesList;
   }

   public Map getNodeTemplatesMap() {
      return this.nodeTemplatesMap;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public static class NodeTemplateDescriptor {
      private String name;
      private String xmlValue;
      private short type;
      private String category;
      private String description;

      public NodeTemplateDescriptor(String var1, String var2, short var3, String var4, String var5) {
         this.name = var1;
         this.xmlValue = var2;
         this.type = var3;
         this.category = var4;
         this.description = var5;
      }

      public String getCategory() {
         return this.category;
      }

      public void setCategory(String var1) {
         this.category = var1;
      }

      public String getDescription() {
         return this.description;
      }

      public void setDescription(String var1) {
         this.description = var1;
      }

      public String getName() {
         return this.name;
      }

      public void setName(String var1) {
         this.name = var1;
      }

      public short getType() {
         return this.type;
      }

      public void setType(short var1) {
         this.type = var1;
      }

      public String getXmlValue() {
         return this.xmlValue;
      }

      public void setXmlValue(String var1) {
         this.xmlValue = var1;
      }
   }
}
