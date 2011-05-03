/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.substitutions;

import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.Symbol.RegionParameterSymbol;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class RegionSubstitution implements Substitution {

    RegionParameterSymbol regionParamSym;

    RPL rpl;

    public RegionSubstitution(RegionParameterSymbol regionParamSym, RPL rpl) {
	super();
	this.regionParamSym = regionParamSym;
	this.rpl = rpl;
    }

    public Substitution inEnvironment(Resolve rs, Env<AttrContext> env) {
	return new RegionSubstitution(regionParamSym, rpl.inEnvironment(rs, env, true));
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append(regionParamSym.toString());
	sb.append(" <- ");
	sb.append(rpl.toString());
	return sb.toString();
    }

}
