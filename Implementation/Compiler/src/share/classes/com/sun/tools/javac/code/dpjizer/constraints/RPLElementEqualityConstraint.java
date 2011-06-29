/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.constraints;

import com.sun.tools.javac.code.RPLElement;

/**
 * 
 * @author Stephen Heumann
 * @author Mohsen Vakilian
 * 
 */
public class RPLElementEqualityConstraint implements Constraint {

    private RPLElement firstRPLElement;
    private RPLElement secondRPLElement;

    public RPLElementEqualityConstraint(RPLElement firstRPLElement,
	    RPLElement secondRPLElement) {
	this.firstRPLElement = firstRPLElement;
	this.secondRPLElement = secondRPLElement;
    }

    public RPLElement getFirstRPLElement() {
	return firstRPLElement;
    }

    public RPLElement getSecondRPLElement() {
	return secondRPLElement;
    }

    @Override
    public SolverState solve(SolverState solverState) {
	solverState.add(this);
	return solverState;
    }

    @Override
    public String toString() {
	return firstRPLElement + " should be equal to " + secondRPLElement;
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
	if (firstRPLElement == null) {
	    if (other.firstRPLElement != null)
		return false;
	} else if (!firstRPLElement.equals(other.firstRPLElement))
	    return false;
	if (secondRPLElement == null) {
	    if (other.secondRPLElement != null)
		return false;
	} else if (!secondRPLElement.equals(other.secondRPLElement))
	    return false;
	return true;
    }

}
