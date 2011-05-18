/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.constraints;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.illinois.dpjizer.utils.Logger;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class ConstraintsSet implements Constraints {

    Set<Constraint> constraints;

    public ConstraintsSet() {
	this.constraints = new HashSet<Constraint>();
    }

    @Override
    public boolean add(Constraint constraint) {
	Logger.log("Adding the constraint " + constraint.toString());
	return constraints.add(constraint);
    }

    @Override
    public boolean addAll(Collection<? extends Constraint> constraints) {
	boolean addedSomething = false;
	for (Constraint constraint : constraints) {
	    addedSomething |= add(constraint);
	}
	return addedSomething;
    }

    @Override
    public void clear() {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object object) {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
	return constraints.size() == 0;
    }

    @Override
    public Iterator<Constraint> iterator() {
	return constraints.iterator();
    }

    @Override
    public boolean remove(Object arg0) {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> arg0) {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> arg0) {
	throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
	return constraints.size();
    }

    @Override
    public Object[] toArray() {
	throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] arg0) {
	throw new UnsupportedOperationException();
    }

    @Override
    public List<Constraint> sortedConstraints() {
	List<Constraint> sortedListOfConstraints = new ArrayList<Constraint>(
		constraints);
	Collections.sort(sortedListOfConstraints, new Comparator<Constraint>() {

	    @Override
	    public int compare(Constraint c1, Constraint c2) {
		return c1.toString().compareTo(c2.toString());
	    }
	});
	return Collections.unmodifiableList(sortedListOfConstraints);
    }

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
	ConstraintsSet other = (ConstraintsSet) obj;
	if (constraints == null) {
	    if (other.constraints != null)
		return false;
	} else if (!constraints.equals(other.constraints))
	    return false;
	return true;
    }

}
