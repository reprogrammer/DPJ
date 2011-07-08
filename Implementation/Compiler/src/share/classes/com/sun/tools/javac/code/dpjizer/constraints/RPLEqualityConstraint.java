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
public class RPLEqualityConstraint implements Constraint {

    private RPL firstRPL;
    private RPL secondRPL;

    public RPLEqualityConstraint(RPL firstRPL, RPL secondRPL) {
	this.firstRPL = firstRPL;
	this.secondRPL = secondRPL;
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
	return firstRPL + " should be equal to " + secondRPL;
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
	RPLEqualityConstraint other = (RPLEqualityConstraint) obj;
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
