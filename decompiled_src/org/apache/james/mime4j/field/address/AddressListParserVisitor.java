package org.apache.james.mime4j.field.address;

public interface AddressListParserVisitor {
   Object visit(SimpleNode var1, Object var2);

   Object visit(ASTaddress_list var1, Object var2);

   Object visit(ASTaddress var1, Object var2);

   Object visit(ASTmailbox var1, Object var2);

   Object visit(ASTname_addr var1, Object var2);

   Object visit(ASTgroup_body var1, Object var2);

   Object visit(ASTangle_addr var1, Object var2);

   Object visit(ASTroute var1, Object var2);

   Object visit(ASTphrase var1, Object var2);

   Object visit(ASTaddr_spec var1, Object var2);

   Object visit(ASTlocal_part var1, Object var2);

   Object visit(ASTdomain var1, Object var2);
}
