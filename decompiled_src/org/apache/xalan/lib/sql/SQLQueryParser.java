package org.apache.xalan.lib.sql;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xml.utils.QName;
import org.apache.xpath.objects.XObject;

public class SQLQueryParser {
   private boolean m_InlineVariables = false;
   private boolean m_IsCallable = false;
   private String m_OrigQuery = null;
   private StringBuffer m_ParsedQuery = null;
   private Vector m_Parameters = null;
   private boolean m_hasOutput = false;
   private boolean m_HasParameters;
   public static final int NO_OVERRIDE = 0;
   public static final int NO_INLINE_PARSER = 1;
   public static final int INLINE_PARSER = 2;

   public SQLQueryParser() {
      this.init();
   }

   private SQLQueryParser(String query) {
      this.m_OrigQuery = query;
   }

   private void init() {
   }

   public SQLQueryParser parse(XConnection xconn, String query, int override) {
      SQLQueryParser parser = new SQLQueryParser(query);
      parser.parse(xconn, override);
      return parser;
   }

   private void parse(XConnection xconn, int override) {
      this.m_InlineVariables = "true".equals(xconn.getFeature("inline-variables"));
      if (override == 1) {
         this.m_InlineVariables = false;
      } else if (override == 2) {
         this.m_InlineVariables = true;
      }

      if (this.m_InlineVariables) {
         this.inlineParser();
      }

   }

   public boolean hasParameters() {
      return this.m_HasParameters;
   }

   public boolean isCallable() {
      return this.m_IsCallable;
   }

   public Vector getParameters() {
      return this.m_Parameters;
   }

   public void setParameters(Vector p) {
      this.m_HasParameters = true;
      this.m_Parameters = p;
   }

   public String getSQLQuery() {
      return this.m_InlineVariables ? this.m_ParsedQuery.toString() : this.m_OrigQuery;
   }

   public void populateStatement(PreparedStatement stmt, ExpressionContext ctx) {
      for(int indx = 0; indx < this.m_Parameters.size(); ++indx) {
         QueryParameter parm = (QueryParameter)this.m_Parameters.elementAt(indx);

         try {
            if (this.m_InlineVariables) {
               XObject value = ctx.getVariableOrParam(new QName(parm.getName()));
               if (value != null) {
                  stmt.setObject(indx + 1, value.object(), parm.getType(), 4);
               } else {
                  stmt.setNull(indx + 1, parm.getType());
               }
            } else {
               String value = parm.getValue();
               if (value != null) {
                  stmt.setObject(indx + 1, value, parm.getType(), 4);
               } else {
                  stmt.setNull(indx + 1, parm.getType());
               }
            }
         } catch (Exception var6) {
         }
      }

   }

   public void registerOutputParameters(CallableStatement cstmt) throws SQLException {
      if (this.m_IsCallable && this.m_hasOutput) {
         for(int indx = 0; indx < this.m_Parameters.size(); ++indx) {
            QueryParameter parm = (QueryParameter)this.m_Parameters.elementAt(indx);
            if (parm.isOutput()) {
               cstmt.registerOutParameter(indx + 1, parm.getType());
            }
         }
      }

   }

   protected void inlineParser() {
      QueryParameter curParm = null;
      int state = 0;
      StringBuffer tok = new StringBuffer();
      boolean firstword = true;
      if (this.m_Parameters == null) {
         this.m_Parameters = new Vector();
      }

      if (this.m_ParsedQuery == null) {
         this.m_ParsedQuery = new StringBuffer();
      }

      for(int idx = 0; idx < this.m_OrigQuery.length(); ++idx) {
         char ch = this.m_OrigQuery.charAt(idx);
         switch (state) {
            case 0:
               if (ch == '\'') {
                  state = 1;
               } else if (ch == '?') {
                  state = 4;
               } else if (firstword && (Character.isLetterOrDigit(ch) || ch == '#')) {
                  tok.append(ch);
                  state = 3;
               }

               this.m_ParsedQuery.append(ch);
               break;
            case 1:
               if (ch == '\'') {
                  state = 0;
               } else if (ch == '\\') {
                  state = 2;
               }

               this.m_ParsedQuery.append(ch);
               break;
            case 2:
               state = 1;
               this.m_ParsedQuery.append(ch);
               break;
            case 3:
               if (!Character.isLetterOrDigit(ch) && ch != '#' && ch != '_') {
                  if (tok.toString().equalsIgnoreCase("call")) {
                     this.m_IsCallable = true;
                     if (curParm != null) {
                        curParm.setIsOutput(true);
                     }
                  }

                  firstword = false;
                  tok = new StringBuffer();
                  if (ch == '\'') {
                     state = 1;
                  } else if (ch == '?') {
                     state = 4;
                  } else {
                     state = 0;
                  }
               } else {
                  tok.append(ch);
               }

               this.m_ParsedQuery.append(ch);
               break;
            case 4:
               if (ch == '[') {
                  state = 5;
               }
               break;
            case 5:
               if (!Character.isWhitespace(ch) && ch != '=') {
                  tok.append(Character.toUpperCase(ch));
               } else if (tok.length() > 0) {
                  this.m_HasParameters = true;
                  curParm = new QueryParameter();
                  curParm.setTypeName(tok.toString());
                  this.m_Parameters.addElement(curParm);
                  tok = new StringBuffer();
                  if (ch == '=') {
                     state = 7;
                  } else {
                     state = 6;
                  }
               }
               break;
            case 6:
               if (ch == '=') {
                  state = 7;
               }
               break;
            case 7:
               if (!Character.isWhitespace(ch) && ch != ']') {
                  tok.append(ch);
               } else if (tok.length() > 0) {
                  curParm.setName(tok.toString());
                  tok = new StringBuffer();
                  if (ch == ']') {
                     state = 0;
                  } else {
                     state = 8;
                  }
               }
               break;
            case 8:
               if (!Character.isWhitespace(ch) && ch != ']') {
                  tok.append(ch);
               } else if (tok.length() > 0) {
                  tok.setLength(3);
                  if (tok.toString().equalsIgnoreCase("OUT")) {
                     curParm.setIsOutput(true);
                     this.m_hasOutput = true;
                  }

                  tok = new StringBuffer();
                  if (ch == ']') {
                     state = 0;
                  }
               }
         }
      }

      if (this.m_IsCallable) {
         this.m_ParsedQuery.insert(0, '{');
         this.m_ParsedQuery.append('}');
      }

   }
}
