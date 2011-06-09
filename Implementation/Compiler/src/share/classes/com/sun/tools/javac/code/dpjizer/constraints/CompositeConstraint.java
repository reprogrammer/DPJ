/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.constraints;

import java.util.Iterator;

/**
 * 
 * @author Stephen Heumann
 * @author Mohsen Vakilian
 * 
 */
public abstract class CompositeConstraint implements Constraint {

    protected Constraints constraints;

    public static final CompositeConstraint ALWAYS_TRUE = new ConjunctiveConstraint();

    public static final CompositeConstraint ALWAYS_FALSE = new DisjunctiveConstraint();

    protected CompositeConstraint(Constraints constraints) {
	this.constraints = constraints;
    }

    protected abstract Constraint simplified();

    abstract public boolean isAlwaysTrue();
    
    abstract public boolean isAlwaysFalse();
    
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((constraints == null) ? 0 : constraints.hashCode());
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
	CompositeConstraint other = (CompositeConstraint) obj;
	if (constraints == null) {
	    if (other.constraints != null)
		return false;
	} else if (!constraints.equals(other.constraints))
	    return false;
	return true;
    }

    protected String toString(String operator) {
	StringBuilder sb = new StringBuilder();
	sb.append("(");
	Iterator<Constraint> iter = constraints.iterator();
	while (iter.hasNext()) {
	    Constraint nextConstraint = iter.next();
	    sb.append(nextConstraint.toString());
	    if (iter.hasNext()) {
		sb.append(" ");
		sb.append(operator);
		sb.append(" ");
	    }
	}
	sb.append(")");
	return sb.toString();
    }

}
