/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.constraints;

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
    RPLElement rplElement;

    public static Context context;

    private RPLElementContainmentConstraint(RegionVarElt regionVariableElement,
	    RPLElement rplElement) {
	this.regionVarElt = regionVariableElement;
	this.rplElement = rplElement;
    }

    public static Constraint newRPLElementContainmentConstraint(
	    RegionVarElt regionVariableElement, RPLElement rplElement) {
	if (canBeEqual(regionVariableElement, rplElement)) {
	    return new RPLElementContainmentConstraint(regionVariableElement,
		    rplElement);
	} else {
	    return CompositeConstraint.ALWAYS_FALSE;
	}
    }

    private static boolean canBeEqual(RegionVarElt regionVariableElement,
	    RPLElement rplElement) {

	if (context == null) {
	    throw new AssertionError("Expected a valid context.");
	}

	Resolve resolve = Resolve.instance(context);
	boolean isRPLElementInScopeOfRegionVariable = resolve.dpjizerIsInScope(
		rplElement, regionVariableElement.getEnv());
	return isRPLElementInScopeOfRegionVariable;
    }

    @Override
    public String toString() {
	return regionVarElt + " contains " + rplElement;
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
	if (rplElement == null) {
	    if (other.rplElement != null)
		return false;
	} else if (!rplElement.equals(other.rplElement))
	    return false;
	return true;
    }

}
