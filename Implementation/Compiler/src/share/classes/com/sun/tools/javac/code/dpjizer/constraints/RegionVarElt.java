/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.constraints;

import static com.sun.tools.javac.code.Flags.STATIC;

import com.sun.tools.javac.code.RPLElement;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.dpjizer.substitutions.RegionVariableElement;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Name.Table;

/**
 * 
 * @author Stephen Heumann
 * @author Mohsen Vakilian
 * 
 */
public class RegionVarElt extends RPLElement implements RegionVariableElement {

    public RegionVarEltSymbol sym;

    public RegionVarElt(RegionVarEltSymbol sym) {
	this.sym = sym;
    }

    @Override
    public String toString() {
	return sym.toString();
    }

    @Override
    public int hashCode() {
	return 97;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	RegionVarElt other = (RegionVarElt) obj;
	if (sym == null) {
	    if (other.sym != null)
		return false;
	} else if (!sym.equals(other.sym))
	    return false;
	return true;
    }

    public static RegionVarElt getFreshRegionVarElt(Table names,
	    Env<AttrContext> env) {
	Scope originalScope = env.info.scope;
	Scope clonedScope = originalScope.clone();
	Env<AttrContext> localEnv = env
		.dup(env.tree, env.info.dup(clonedScope));
	RegionVarEltSymbol regionVarSym = new RegionVarEltSymbol(STATIC,
		Name.fromString(names, new String("Pi"
			+ RegionVarEltSymbol.numIDs)),
		localEnv.info.scope.owner, localEnv);

	RegionVarElt regionVarElt = new RegionVarElt(regionVarSym);
	return regionVarElt;
    }

    @Override
    public Env<AttrContext> getEnv() {
	return sym.env;
    }

}
