/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.constraints;

import com.sun.tools.javac.code.RPL;

/**
 * 
 * @author Stephen Heumann
 * @author Mohsen Vakilian
 * 
 */
public class DisjointnessConstraint implements Constraint {

    RPL firstRPL;
    RPL secondRPL;

    public DisjointnessConstraint(RPL first, RPL second) {
	this.firstRPL = first;
	this.secondRPL = second;
    }

    public RPL getFirstRPL() {
	return firstRPL;
    }

    public RPL getSecondRPL() {
	return secondRPL;
    }

    @Override
    public SolverState solve(SolverState solverState) {
	throw new UnsupportedOperationException();

    }

    @Override
    public String toString() {
	return firstRPL + " # " + secondRPL;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((firstRPL == null) ? 0 : firstRPL.hashCode());
	result = prime * result
		+ ((secondRPL == null) ? 0 : secondRPL.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	DisjointnessConstraint other = (DisjointnessConstraint) obj;
	if (firstRPL == null) {
	    if (other.firstRPL != null)
		return false;
	} else if (!firstRPL.equals(other.firstRPL))
	    return false;
	if (secondRPL == null) {
	    if (other.secondRPL != null)
		return false;
	} else if (!secondRPL.equals(other.secondRPL))
	    return false;
	return true;
    }

}
