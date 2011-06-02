/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.substitutions;

import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;

/**
 * 
 * @author Stephen Heumann
 * @author Mohsen Vakilian
 * 
 */
public interface RegionVariableElement {

    Env<AttrContext> getEnv();

}
