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
public class ConjunctiveConstraint extends CompositeConstraint {

    protected ConjunctiveConstraint(Constraints constraints) {
	super(constraints);
    }

    protected ConjunctiveConstraint() {
	this(new ConstraintsSet());
    }

    public static Constraint newConjunctiveConstraint(Constraints constraints) {
	return new ConjunctiveConstraint(constraints).simplified();
    }

    @Override
    protected Constraint simplified() {
	ConstraintsSet newConstraints = new ConstraintsSet();
	for (Constraint constraint : constraints) {
	    if (ALWAYS_FALSE.equals(constraint)) {
		return ALWAYS_FALSE;
	    } else if (!ALWAYS_TRUE.equals(constraint)) {
		if (constraint instanceof ConjunctiveConstraint) {
		    Constraints constituentConstraints = ((ConjunctiveConstraint) constraint).constraints;
		    newConstraints.addAll(constituentConstraints);
		} else {
		    newConstraints.add(constraint);
		}
	    }
	}
	if (newConstraints.size() != 1) {
	    return new ConjunctiveConstraint(newConstraints);
	} else {
	    return newConstraints.iterator().next();
	}
    }

    @Override
    public String toString() {
	if (constraints.isEmpty()) {
	    return "TRUE";
	}
	return toString("^");
    }

}
