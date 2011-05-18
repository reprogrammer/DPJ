/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.constraints;

import com.sun.tools.javac.code.RPLElement;
import com.sun.tools.javac.code.dpjizer.substitutions.RegionVariableElement;

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

}
