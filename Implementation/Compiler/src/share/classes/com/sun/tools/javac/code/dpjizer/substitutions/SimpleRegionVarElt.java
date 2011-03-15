/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.substitutions;

import com.sun.tools.javac.code.RPLElement;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class SimpleRegionVarElt extends RPLElement {

    static int numIDs = 1;
    int ID;

    public SimpleRegionVarElt() {
	this.ID = numIDs++;
    }

    @Override
    public String toString() {
	return "SPi" + ID;
    }

}
