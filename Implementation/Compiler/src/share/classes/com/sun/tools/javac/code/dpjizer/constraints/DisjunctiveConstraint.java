/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.constraints;

/**
 * 
 * @author Stephen Heumann
 * @author Mohsen Vakilian
 * 
 */
public class DisjunctiveConstraint extends CompositeConstraint {

    protected DisjunctiveConstraint(Constraints constraints) {
	super(constraints);
    }

    protected DisjunctiveConstraint() {
	this(new ConstraintsSet());
    }

    public static Constraint newDisjunctiveConstraint(Constraints constraints) {
	return new DisjunctiveConstraint(constraints).simplified();
    }

    @Override
    protected Constraint simplified() {
	ConstraintsSet newConstraints = new ConstraintsSet();
	for (Constraint constraint : constraints) {
	    if (ALWAYS_TRUE.equals(constraint)) {
		return ALWAYS_TRUE;
	    } else if (!ALWAYS_FALSE.equals(constraint)) {
		if (constraint instanceof DisjunctiveConstraint) {
		    Constraints constituentConstraints = ((DisjunctiveConstraint) constraint).constraints;
		    newConstraints.addAll(constituentConstraints);
		} else {
		    newConstraints.add(constraint);
		}
	    }
	}
	if (newConstraints.size() != 1) {
	    return new DisjunctiveConstraint(newConstraints);
	} else {
	    return newConstraints.iterator().next();
	}
    }

    @Override
    public String toString() {
	if (constraints.isEmpty()) {
	    return "FALSE";
	}
	return toString("v");
    }

}
