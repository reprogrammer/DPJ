/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.constraints;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.RPLElement;
import com.sun.tools.javac.code.RPLElement.NameRPLElement;
import com.sun.tools.javac.code.dpjizer.FreshRPLElementFactory;
import com.sun.tools.javac.code.dpjizer.dirs.Dirs;
import com.sun.tools.javac.code.dpjizer.dirs.FileUtils;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.util.Name;

import edu.illinois.dpjizer.utils.Logger;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class ConstraintRepository {

    Constraints constraints;

    Constraints replacedConstraints = new ConstraintsSet();

    Dirs dirs;

    private Env<AttrContext> freshNameRPLElementEnv;

    private Name.Table names;

    int counter = 1;

    private Map<RPL, BeginWithConstraint> beginWithMap = new HashMap<RPL, BeginWithConstraint>();

    private static ConstraintRepository instance;

    public static ConstraintRepository getInstance() {
	return instance;
    }

    @Inject
    public ConstraintRepository(Constraints constraints, Dirs dirs) {
	this.constraints = constraints;
	this.dirs = dirs;
	instance = this;
    }

    public void setFreshNameRPLElementEnvIfIsUnknown(Env<AttrContext> env) {
	if (freshNameRPLElementEnv == null) {
	    freshNameRPLElementEnv = env;
	}
    }

    public void setNames(Name.Table names) {
	if (this.names == null) {
	    this.names = names;
	}
    }

    public void add(Constraint constraint) {
	if (constraint instanceof InclusionConstraint) {
	    InclusionConstraint inclusionConstraint = (InclusionConstraint) constraint;
	    RPL contained = inclusionConstraint.getContained();
	    RPL container = inclusionConstraint.getContainer();
	    NameRPLElement freshNameRPLElement = FreshRPLElementFactory
		    .getFreshNameRPLElement(names, freshNameRPLElementEnv);
	    Constraint constraintOnContained = contained
		    .shouldContainRPLElement(freshNameRPLElement);
	    Logger.log("Adding the following constraint because of an inclusion constraint:\n"
		    + constraintOnContained);
	    add(constraintOnContained);
	    Constraint constraintOnContainer = container
		    .shouldContainRPLElement(freshNameRPLElement);
	    Logger.log("Adding the following constraint because of an inclusion constraint:\n"
		    + constraintOnContainer);
	    add(constraintOnContainer);
	} else {
	    constraints.add(constraint);

	    if (constraint instanceof BeginWithConstraint) {
		BeginWithConstraint beginWithConstraint = (BeginWithConstraint) constraint;
		beginWithMap.put(beginWithConstraint.getRPL(),
			beginWithConstraint);
	    }
	}
    }

    public Collection<BeginWithConstraint> getBeginWithConstraints() {
	return beginWithMap.values();
    }

    public boolean doesBeginWithAnything(RPL rpl) {
	return beginWithMap.containsKey(rpl);
    }

    public RPLElement getBeginning(RPL rpl) {
	return beginWithMap.get(rpl).getBeginning();
    }

    public void markAsReplacedConstraint(Constraint constraint) {
	constraints.remove(constraint);
	replacedConstraints.add(constraint);
    }

    @Override
    public String toString() {
	return constraints.toString();
    }

    public void writeToFile() {
	FileUtils.createDirIfNotFound(dirs.getLogDirName());
	PrintWriter printWriter = null;
	try {
	    printWriter = new PrintWriter(dirs.getConstraintsFileName());
	    List<Constraint> sortedConstraints = constraints
		    .sortedConstraints();
	    for (Constraint constraint : sortedConstraints) {
		printWriter.println(constraint);
	    }
	} catch (FileNotFoundException e) {
	    throw new RuntimeException(e);
	} finally {
	    if (printWriter != null)
		printWriter.close();
	}
    }

    public void solve() {
	Constraint allConstraints = ConjunctiveConstraint
		.newConjunctiveConstraint(constraints);
	SolverState solverState = new SolverState();
	solverState = allConstraints.solve(solverState);
	Logger.log("The final solver state is:\n" + solverState);
    }
}
