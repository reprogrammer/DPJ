/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer;

import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public interface Substitution {

    public Substitution inEnvironment(Resolve rs, Env<AttrContext> env);

}
