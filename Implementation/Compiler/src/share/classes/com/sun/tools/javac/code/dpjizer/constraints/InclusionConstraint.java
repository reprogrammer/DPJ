/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.constraints;

import com.sun.tools.javac.code.RPL;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class InclusionConstraint implements Constraint {

    private final RPL contained;
    private final RPL container;

    public InclusionConstraint(RPL contained, RPL container) {
	this.contained = contained;
	this.container = container;
    }

    public RPL getContained() {
	return contained;
    }

    public RPL getContainer() {
	return container;
    }

    @Override
    public SolverState solve(SolverState solverState) {
	throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
	return contained + " is in " + container;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((contained == null) ? 0 : contained.hashCode());
	result = prime * result
		+ ((container == null) ? 0 : container.hashCode());
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
	InclusionConstraint other = (InclusionConstraint) obj;
	if (contained == null) {
	    if (other.contained != null)
		return false;
	} else if (!contained.equals(other.contained))
	    return false;
	if (container == null) {
	    if (other.container != null)
		return false;
	} else if (!container.equals(other.container))
	    return false;
	return true;
    }

}
