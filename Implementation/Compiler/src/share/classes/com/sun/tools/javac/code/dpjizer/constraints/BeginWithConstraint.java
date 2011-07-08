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
public class BeginWithConstraint implements Constraint {

    private RPL rpl;
    private RPL beginning;

    public BeginWithConstraint(RPL first, RPL beginning) {
	this.rpl = first;
	this.beginning = beginning;
    }

    public RPL getRPL() {
	return rpl;
    }

    public RPL getBeginning() {
	return beginning;
    }

    @Override
    public SolverState solve(SolverState solverState) {
	solverState.add(this);
	return solverState;
    }

    @Override
    public String toString() {
	return rpl + " begins with " + beginning;
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
	BeginWithConstraint other = (BeginWithConstraint) obj;
	if (beginning == null) {
	    if (other.beginning != null)
		return false;
	} else if (!beginning.equals(other.beginning))
	    return false;
	if (rpl == null) {
	    if (other.rpl != null)
		return false;
	} else if (!rpl.equals(other.rpl))
	    return false;
	return true;
    }

}
