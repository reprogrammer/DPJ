/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.constraints;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.RPLElement;

/**
 * 
 * @author Stephen Heumann
 * @author Mohsen Vakilian
 * 
 */
public class SolverState {

    HashMap<RPL, BeginWithConstraint> beginWithConstraints;
    MultiMap<RegionVarElt, RPLElementContainmentConstraint> containmentConstraints;
    MultiMap<RPLElement, RPLElementDistinctnessConstraint> distinctnessConstraints;
    MultiMap<RPLElement, RPLElementEqualityConstraint> equalityConstraints;

    public final static SolverState INCONSISTENT_STATE = new InconsistentSolverState();

    public final static class InconsistentSolverState extends SolverState {

	@Override
	public boolean isConsistent() {
	    return false;
	}

    }

    public SolverState() {
	beginWithConstraints = new HashMap<RPL, BeginWithConstraint>();
	containmentConstraints = new MultiMap<RegionVarElt, RPLElementContainmentConstraint>();
	distinctnessConstraints = new MultiMap<RPLElement, RPLElementDistinctnessConstraint>();
	equalityConstraints = new MultiMap<RPLElement, RPLElementEqualityConstraint>();
    }

    private SolverState(
	    HashMap<RPL, BeginWithConstraint> beginWithConstraints,
	    MultiMap<RegionVarElt, RPLElementContainmentConstraint> containmentConstraints,
	    MultiMap<RPLElement, RPLElementDistinctnessConstraint> distinctnessConstraints,
	    MultiMap<RPLElement, RPLElementEqualityConstraint> equalityConstraints) {
	this.beginWithConstraints = beginWithConstraints;
	this.containmentConstraints = containmentConstraints;
	this.distinctnessConstraints = distinctnessConstraints;
	this.equalityConstraints = equalityConstraints;
    }

    public void add(BeginWithConstraint constraint) {
	BeginWithConstraint existingBeginWithConstraint = beginWithConstraints
		.get(constraint.getRPL());
	if (beginWithConstraints.containsKey(constraint.getRPL())) {
	    if (!existingBeginWithConstraint.getBeginning().equals(
		    constraint.getBeginning())) {
		// FIXME: Because the begin-with constraints may force the the
		// RPL begin with two RPLs of different lengths, we cannot
		// simply assume that the two RPLs are the same. Rather, one of
		// them just needs to begin with the other. For the time being,
		// we are making the two RPLs consist of the RPL element.
		RPLElementEqualityConstraint equalityConstraint = new RPLElementEqualityConstraint(
			existingBeginWithConstraint.getBeginning().elts.head,
			constraint.getBeginning().elts.head);
		add(equalityConstraint);
	    }
	} else {
	    beginWithConstraints.put(constraint.getRPL(), constraint);
	}
    }

    public void add(RPLElementContainmentConstraint constraint) {
	if (constraint.rpl.elts.head instanceof RPLElement.RPLParameterElement) {
	    add(new BeginWithConstraint(new RPL(constraint.regionVarElt),
		    constraint.rpl));
	} else {
	    containmentConstraints.put(constraint.regionVarElt, constraint);
	}
    }

    public void add(RPLElementDistinctnessConstraint constraint) {
	distinctnessConstraints
		.put(constraint.getFirstRPLElement(), constraint);
	distinctnessConstraints.put(constraint.getSecondRPLElement(),
		constraint);
    }

    public void add(RPLElementEqualityConstraint constraint) {
	equalityConstraints.put(constraint.getFirstRPLElement(), constraint);
	equalityConstraints.put(constraint.getSecondRPLElement(), constraint);
    }

    public boolean isConsistent() {
	for (RPLElement rplElement : equalityConstraints.keySet()) {
	    Set<RPLElement> equalityRPLElements = new HashSet<RPLElement>();
	    for (RPLElementEqualityConstraint constraint : equalityConstraints
		    .get(rplElement)) {
		equalityRPLElements.add(constraint.getFirstRPLElement());
		equalityRPLElements.add(constraint.getSecondRPLElement());
	    }
	    equalityRPLElements.remove(rplElement);

	    Set<RPLElement> distinctnessRPLElements = new HashSet<RPLElement>();
	    if (distinctnessConstraints.containsKey(rplElement)) {
		for (RPLElementDistinctnessConstraint constraint : distinctnessConstraints
			.get(rplElement)) {
		    distinctnessRPLElements
			    .add(constraint.getFirstRPLElement());
		    distinctnessRPLElements.add(constraint
			    .getSecondRPLElement());
		}
		distinctnessRPLElements.remove(rplElement);
	    }

	    equalityRPLElements.retainAll(distinctnessRPLElements);
	    if (!equalityRPLElements.isEmpty()) {
		return false;
	    }
	}
	return true;
    }

    @Override
    public SolverState clone() {
	return new SolverState(
		(HashMap<RPL, BeginWithConstraint>) beginWithConstraints
			.clone(),
		containmentConstraints.clone(),
		distinctnessConstraints.clone(), equalityConstraints.clone());
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("beginWithConstraints=");
	for (BeginWithConstraint constraint : beginWithConstraints.values()) {
	    builder.append(constraint.toString());
	    builder.append("\n");
	}
	builder.append("containmentConstraints=");
	for (RPLElementContainmentConstraint constraint : containmentConstraints
		.values()) {
	    builder.append(constraint.toString());
	    builder.append("\n");
	}
	builder.append("distinctnessConstraints=");
	for (RPLElementDistinctnessConstraint constraint : distinctnessConstraints
		.values()) {
	    builder.append(constraint.toString());
	    builder.append("\n");
	}
	builder.append("equalityConstraints=");
	for (RPLElementEqualityConstraint constraint : equalityConstraints
		.values()) {
	    builder.append(constraint.toString());
	    builder.append("\n");
	}
	return builder.toString();
    }

}
