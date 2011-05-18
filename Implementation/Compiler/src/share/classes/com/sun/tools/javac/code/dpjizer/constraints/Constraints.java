/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.constraints;

import java.util.Collection;
import java.util.List;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public interface Constraints extends Collection<Constraint> {

    List<Constraint> sortedConstraints();

    boolean equals(Object o);

    int hashCode();

}
