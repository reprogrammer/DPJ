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
public class RPLElementEqualityConstraint implements Constraint {

    RegionVariableElement regionVariableElement;
    RPLElement rplElement;

    public RPLElementEqualityConstraint(
	    RegionVariableElement regionVariableElement, RPLElement rplElement) {
	this.regionVariableElement = regionVariableElement;
	this.rplElement = rplElement;
    }

    @Override
    public String toString() {
	return regionVariableElement + " = " + rplElement;
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
	RPLElementEqualityConstraint other = (RPLElementEqualityConstraint) obj;
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
