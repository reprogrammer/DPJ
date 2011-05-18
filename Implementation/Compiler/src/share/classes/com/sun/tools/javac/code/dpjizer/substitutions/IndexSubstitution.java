/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.substitutions;

import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.JCTree.DPJNegationExpression;
import com.sun.tools.javac.tree.JCTree.JCExpression;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class IndexSubstitution implements Substitution {

    VarSymbol varSymbol;

    JCExpression expression;

    public IndexSubstitution(VarSymbol varSymbol, JCExpression expression) {
	this.varSymbol = varSymbol;
	this.expression = expression;
    }

    public VarSymbol getLHS() {
	return varSymbol;
    }

    public JCExpression getRHS() {
	return expression;
    }

    public boolean isRHSAnyIndex() {
	return getRHS() instanceof DPJNegationExpression;
    }

    public Substitution inEnvironment(Resolve rs, Env<AttrContext> env) {
	JCExpression resultingRPL = expression;
	if (!rs.isInScope(expression, env)) {
	    resultingRPL = new DPJNegationExpression(null);
	}
	return new IndexSubstitution(varSymbol, resultingRPL);
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append(varSymbol.toString());
	sb.append(" <- ");
	if (expression instanceof DPJNegationExpression) {
	    sb.append("?");
	} else {
	    sb.append(expression.toString());
	}
	return sb.toString();
    }

}
