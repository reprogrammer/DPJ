/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.constraints;

import com.google.inject.Inject;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.RPLElement;
import com.sun.tools.javac.code.dpjizer.substitutions.RegionVariableElement;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.util.Context;

/**
 * 
 * @author Stephen Heumann
 * @author Mohsen Vakilian
 * 
 */
public class RPLElementContainmentConstraint implements Constraint {

    RegionVariableElement regionVariableElement;
    RPLElement rplElement;

    public static Context context;

    private RPLElementContainmentConstraint(
	    RegionVariableElement regionVariableElement, RPLElement rplElement) {
	this.regionVariableElement = regionVariableElement;
	this.rplElement = rplElement;
    }

    public static Constraint newRPLElementEqualityConstraint(
	    RegionVariableElement regionVariableElement, RPLElement rplElement) {
	if (canBeEqual(regionVariableElement, rplElement)) {
	    return new RPLElementContainmentConstraint(regionVariableElement,
		    rplElement);
	} else {
	    return CompositeConstraint.ALWAYS_FALSE;
	}
    }

    private static boolean canBeEqual(
	    RegionVariableElement regionVariableElement, RPLElement rplElement) {

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
	return regionVariableElement + " contains " + rplElement;
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
	if (regionVariableElement == null) {
	    if (other.regionVariableElement != null)
		return false;
	} else if (!regionVariableElement.equals(other.regionVariableElement))
	    return false;
	if (rplElement == null) {
	    if (other.rplElement != null)
		return false;
	} else if (!rplElement.equals(other.rplElement))
	    return false;
	return true;
    }

}
