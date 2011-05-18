/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.substitutions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class Substitutions {

    List<Substitution> substitutions = new ArrayList<Substitution>();

    public static Substitutions EMPTY = new Substitutions();

    protected Substitutions() {

    }

    public Substitutions(Substitution substitution) {
	add(substitution);
    }

    public Substitutions(List<Substitution> substitutions) {
	this.substitutions = substitutions;
    }

    public void add(Substitution substitution) {
	substitutions.add(substitution);
    }

    public Substitutions inEnvironment(Resolve rs, Env<AttrContext> env) {
	List<Substitution> substitutions = new ArrayList<Substitution>();
	for (Substitution substitution : this.substitutions) {
	    substitutions.add(substitution.inEnvironment(rs, env));
	}
	return new Substitutions(substitutions);
    }

    public List<Substitution> getSubstitutions() {
	return Collections.unmodifiableList(substitutions);
    }

    public boolean isEmpty() {
	return substitutions.size() == 0;
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append("[");
	Iterator<Substitution> iter = substitutions.iterator();
	while (iter.hasNext()) {
	    sb.append(iter.next());
	    if (iter.hasNext())
		sb.append(", ");
	}
	sb.append("]");
	return sb.toString();
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((substitutions == null) ? 0 : substitutions.hashCode());
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
	Substitutions other = (Substitutions) obj;
	if (substitutions == null) {
	    if (other.substitutions != null)
		return false;
	} else if (!substitutions.equals(other.substitutions))
	    return false;
	return true;
    }

}
