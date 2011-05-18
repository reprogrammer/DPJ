/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.substitutions;

import com.sun.tools.javac.code.RPLElement;

/**
 * 
 * @author Stephen Heumann
 * @author Mohsen Vakilian
 * 
 */
public class SimpleRegionVarElt extends RPLElement implements
	RegionVariableElement {

    static int numIDs = 1;
    int ID;

    public SimpleRegionVarElt() {
	this.ID = numIDs++;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ID;
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
	SimpleRegionVarElt other = (SimpleRegionVarElt) obj;
	if (ID != other.ID)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "SPi" + ID;
    }

}
