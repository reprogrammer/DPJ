/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.constraints;

import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.RPLElement;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.util.Context;

/**
 * 
 * @author Stephen Heumann
 * @author Mohsen Vakilian
 * 
 */
public class RPLElementContainmentConstraint implements Constraint {

    RegionVarElt regionVarElt;
    RPL rpl;

    public static Context context;

    private RPLElementContainmentConstraint(RegionVarElt regionVariableElement,
	    RPL rpl) {
	this.regionVarElt = regionVariableElement;
	this.rpl = rpl;
    }

    public static Constraint newRPLElementContainmentConstraint(
	    RegionVarElt regionVariableElement, RPL rpl) {
	if (canBeEqual(regionVariableElement, rpl)) {
	    return new RPLElementContainmentConstraint(regionVariableElement,
		    rpl);
	} else {
	    return CompositeConstraint.ALWAYS_FALSE;
	}
    }

    private static boolean canBeEqual(RegionVarElt regionVariableElement,
	    RPL rpl) {

	if (context == null) {
	    throw new AssertionError("Expected a valid context.");
	}

	for (RPLElement rplElement : rpl.elts) {
	    Resolve resolve = Resolve.instance(context);
	    boolean isRPLElementInScopeOfRegionVariable = resolve
		    .dpjizerIsInScope(rplElement,
			    regionVariableElement.getEnv());
	    if (!isRPLElementInScopeOfRegionVariable) {
		return false;
	    }
	}
	return true;
    }

    @Override
    public SolverState solve(SolverState solverState) {
	solverState.add(this);
	return solverState;
    }

    @Override
    public String toString() {
	return regionVarElt + " contains " + rpl;
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
	RPLElementContainmentConstraint other = (RPLElementContainmentConstraint) obj;
	if (regionVarElt == null) {
	    if (other.regionVarElt != null)
		return false;
	} else if (!regionVarElt.equals(other.regionVarElt))
	    return false;
	if (rpl == null) {
	    if (other.rpl != null)
		return false;
	} else if (!rpl.equals(other.rpl))
	    return false;
	return true;
    }

}
