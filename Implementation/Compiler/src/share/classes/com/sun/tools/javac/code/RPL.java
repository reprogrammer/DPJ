package com.sun.tools.javac.code;

import com.sun.mirror.declaration.Modifier;
import com.sun.tools.javac.code.RPLElement.ArrayIndexRPLElement;
import com.sun.tools.javac.code.RPLElement.RPLCaptureParameter;
import com.sun.tools.javac.code.RPLElement.VarRPLElement;
import com.sun.tools.javac.code.Symbol.RegionParameterSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Type.TypeVar;
import com.sun.tools.javac.code.dpjizer.IndexSubstitution;
import com.sun.tools.javac.code.dpjizer.RegionSubstitution;
import com.sun.tools.javac.code.dpjizer.Substitution;
import com.sun.tools.javac.code.dpjizer.SubstitutionChain;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.DPJNegationExpression;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

/**
 * A class for representing DPJ region path lists (RPLs). An RPL is a list of
 * RPL elements. Various operations on RPLs, pairs of RPLs, and lists of RPLs
 * required by the DPJ type system are supported.
 */
public class RPL {

    // /////////////////////////////////////////////////////////////////////////
    // Fields
    // /////////////////////////////////////////////////////////////////////////

    /** The elements comprising this RPL */
    public List<RPLElement> elts;

    // DPJIZER
    SubstitutionChain substitutionChain = SubstitutionChain.EMPTY;
    public static boolean resultOfIsIncludedIn = true;
    public static boolean captureInclusionConstraints = true;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public RPL(SubstitutionChain substitutionChain) {
	this.substitutionChain = substitutionChain;
    }

    public RPL() {
	this.elts = List.nil();
    }

    public RPL(RPLElement singletonElement) {
	this(List.of(singletonElement));
    }

    public RPL(List<RPLElement> elts) {
	this(elts, SubstitutionChain.EMPTY);
    }

    public RPL(List<RPLElement> elts, SubstitutionChain substitutionChain) {
	this.elts = elts;
	this.substitutionChain = substitutionChain;
    }

    // /////////////////////////////////////////////////////////////////////////
    // RPL query methods
    // /////////////////////////////////////////////////////////////////////////

    public int size() {
	return elts.size();
    }

    public boolean isEmpty() {
	return elts.isEmpty();
    }

    public boolean isAtomic() {
	for (RPLElement elt : elts) {
	    if (elt.isAtomic())
		return true;
	}
	return false;
    }

    // DPJIZER
    // /**
    // * RPL under relation See Section 1.2.2 of the DPJ Tech Report
    // */
    // public boolean isNestedUnder(RPL that) {
    // // Deal with z regions and capture parameters by converting first
    // // element
    // // to its upper bound
    // RPL upperBound = this.upperBound();
    // if (upperBound != this && upperBound.isNestedUnder(that))
    // return true;
    // // UNDER-ROOT
    // if (that.isRoot())
    // return true;
    // // UNDER-NAME
    // if (!this.isEmpty()) {
    // if (this.withoutLastElement().isNestedUnder(that))
    // return true;
    // }
    // // UNDER-STAR
    // if (this.endsWithStar()
    // && this.withoutLastElement().isNestedUnder(that))
    // return true;
    // // UNDER-INCLUDE
    // if (this.isIncludedIn(that))
    // return true;
    // return false;
    // }

    /**
     * Does this RPL start with a local region name, or is it under an RPL that
     * starts with a local region name?
     */
    public boolean isUnderLocal() {
	RPL upperBound = this.upperBound();
	if (upperBound != this && upperBound.isUnderLocal())
	    return true;
	if (elts.isEmpty() || elts.head == null)
	    return false;
	return elts.head.isLocalName();
    }

    // DPJIZER
    // /**
    // * RPL inclusion relation See Section 1.2.3 of the DPJ Tech Report
    // */
    // public boolean isIncludedIn(RPL that) {
    // // Handle capture parameters
    // if (this.elts.head instanceof RPLCaptureParameter) {
    // return this.upperBound().isIncludedIn(that);
    // }
    // // Handle undetermined parameters
    // if (that.elts.head instanceof UndetRPLParameterElement) {
    // UndetRPLParameterElement element = (UndetRPLParameterElement)
    // that.elts.head;
    // if (element.includedIn == null) {
    // // Nothing to do
    // } else if (this.isIncludedIn(element.includedIn)) {
    // element.includedIn = this;
    // } else {
    // element.includedIn = null;
    // }
    // return true;
    // }
    // // Reflexivity
    // if (this.equals(that))
    // return true;
    // // INCLUDE-STAR
    // if (that.endsWithStar()) {
    // if (this.isNestedUnder(that.withoutLastElement()))
    // return true;
    // }
    // // INCLUDE-NAME
    // if (!this.isEmpty() && !that.isEmpty()) {
    // if (this.elts.last().isIncludedIn(that.elts.last())
    // && this.withoutLastElement().isIncludedIn(
    // that.withoutLastElement()))
    // return true;
    // }
    // return false;
    // }
    // TODO: I need to figure out the rules for inclusion of RPLs that contain
    // region variables.
    public boolean isIncludedIn(RPL that) {
	if (resultOfIsIncludedIn) {
	    // constraints.add(new InclusionConstraint(this, that));
	    if (captureInclusionConstraints) {
		System.out.println(this + " is assumed to be included in "
			+ that);
	    }
	    return true;
	} else {
	    return false;
	}
    }

    // DPJIZER
    // protected boolean endsWithStar() {
    // return size() > 1 && elts.last() == RPLElement.STAR;
    // }

    private boolean isRoot() {
	return size() == 1 && (elts.last() == RPLElement.ROOT_ELEMENT);
    }

    /**
     * Is this RPL fully specified? An RPL is fully specified if it contains no
     * * or [?].
     */

    public boolean isFullySpecified() {
	for (RPLElement e : elts) {
	    if (!e.isFullySpecified())
		return false;
	}
	return true;
    }

    /**
     * Compute an upper bound for this RPL
     */
    public RPL upperBound() {
	if (elts.isEmpty()
		|| (!(elts.head instanceof VarRPLElement) && !(elts.head instanceof RPLCaptureParameter)))
	    return this;
	RPL upperBound = elts.head.upperBound();
	// if (elts.size() == 1) return upperBound;

	// DPJIZER: Replaced a direct "new" statement by a method call.
	// return new RPL(upperBound.elts.appendList(elts.tail));
	return replaceHeadBy(upperBound);
    }

    // /////////////////////////////////////////////////////////////////////////
    // RPL manipulation methods
    // /////////////////////////////////////////////////////////////////////////

    private RPL withoutLastElement() {
	ListBuffer<RPLElement> buf = new ListBuffer<RPLElement>();
	List<RPLElement> elts = this.elts;
	while (elts.tail != null) {
	    if (elts.tail.tail == null)
		break;
	    buf.append(elts.head);
	    elts = elts.tail;
	}

	// DPJIZER: Append the substitution chain.
	return new RPL(buf.toList(), substitutionChain);
    }

    // DPJIZER
    public RPL followedBy(Substitution substitution) {
	return new RPL(elts, substitutionChain.followedBy(substitution));
    }

    // DPJIZER
    // public RPL substForParam(RegionParameterSymbol param, RPL rpl) {
    // if (!this.elts.head.equals(param)) {
    // return this;
    // }
    // return new RPL(rpl.elts.appendList(this.elts.tail));
    // }
    public RPL substForParam(RegionParameterSymbol param, RPL rpl) {
	return followedBy(new RegionSubstitution(param, rpl));
    }

    public RPL substForParams(List<RegionParameterSymbol> from, List<RPL> to) {
	RPL result = this;
	while (from.nonEmpty() && to.nonEmpty()) {
	    result = this.substForParam(from.head, to.head);
	    if (result != this) {
		break;
	    }
	    from = from.tail;
	    to = to.tail;
	}
	return result;
    }

    /**
     * Substitute for type region params
     */
    public RPL substForTRParams(Type from, Type to) {
	if (!(from instanceof TypeVar))
	    return this;
	List<RegionParameterSymbol> params = from.tsym.type.getRegionParams();
	List<RPL> args = to.getRegionActuals();
	return this.substForParams(params, args);
    }

    public RPL substForTRParams(List<Type> from, List<Type> to) {
	RPL result = this;
	while (from.nonEmpty() && to.nonEmpty()) {
	    result = this.substForTRParams(from.head, to.head);
	    if (result != this) {
		break;
	    }
	    from = from.tail;
	    to = to.tail;
	}
	return result;
    }

    /**
     * Do all the RPL parameter substitutions implied by the bindings of t
     */
    public RPL substForAllParams(Type t) {
	RPL result = this.substForParams(t.getRegionParams(),
		t.getRegionActuals());
	result = result.substForTRParams(t.tsym.type.getTypeArguments(),
		t.getTypeArguments());
	return result;
    }

    public RPL substForThis(VarSymbol vsym) {
	if (!(this.elts.head instanceof RPLElement.VarRPLElement))
	    return this;
	RPLElement.VarRPLElement vrs = (RPLElement.VarRPLElement) this.elts.head;
	if (!vrs.vsym.name.toString().equals("this"))
	    return this;

	// DPJIZER: Replaced new by a method call.
	return replaceHeadBy(new RPL(new RPLElement.VarRPLElement(vsym)));
	// return new RPL(List.<RPLElement> of(new
	// RPLElement.VarRPLElement(vsym))
	// .appendList(this.elts.tail));
    }

    public RPL substForThis(RPL rpl) {
	if (!(this.elts.head instanceof RPLElement.VarRPLElement))
	    return this;
	RPLElement.VarRPLElement vrs = (RPLElement.VarRPLElement) this.elts.head;
	if (!vrs.vsym.name.toString().equals("this"))
	    return this;

	// DPJIZER: Replaced new by a method call.
	return replaceHeadBy(rpl);
	// return new RPL(rpl.elts.appendList(this.elts.tail));
    }

    public static RPL exprToRPL(JCExpression tree) {
	Symbol sym = tree.getSymbol();
	if (sym != null)
	    return symToRPL(sym);

	RPL owner = tree.type.getOwner();

	// DPJIZER: Replaced RPL result = new
	// RPL(owner.elts.append(RPLElement.STAR)); by the following.
	RPL result = owner.appenedByStar();

	return result;
    }

    public static RPL symToRPL(Symbol sym) {
	RPL result = null;
	if ((sym instanceof VarSymbol)
		&& (sym.owner.kind == Kinds.MTH || sym.name.equals("this"))
		&& (sym.flags() & Flags.FINAL) != 0) {
	    // If the variable is a final local variable, use it as the RPL
	    result = new RPL(List.<RPLElement> of(new RPLElement.VarRPLElement(
		    (VarSymbol) sym)));
	} else {
	    // Otherwise, use the owner region
	    RPL owner = sym.type.getOwner();

	    // DPJIZER: Use a method instead of list manipulation.
	    // result = new RPL(owner.elts.append(RPLElement.STAR));
	    result = owner.appenedByStar();
	}
	return result;
    }

    // DPJIZER: Added the following method.
    public RPL appenedByStar() {
	return new RPL(this.elts.append(RPLElement.STAR), substitutionChain);
    }

    // DPJIZER: Added the following method.
    public RPL replaceHeadBy(RPL rpl) {
	// Use the double dispatch pattern to make sure that the type of the
	// resulting RPL is decided based on the types of both RPLs.
	return rpl.appendTailOf(this);
    }

    // DPJIZER: Added the following method.
    protected RPL appendTailOf(RPL rpl) {
	return new RPL(elts.appendList(rpl.elts.tail),
		substitutionChain.followedBy(rpl.substitutionChain));
    }

    public RPL substForVar(VarSymbol from, RPL to) {
	if (!(this.elts.head instanceof RPLElement.VarRPLElement))
	    return this;
	RPLElement.VarRPLElement vrs = (RPLElement.VarRPLElement) this.elts.head;
	if (vrs.vsym != from)
	    return this;

	// DPJIZER: Replaced a direct "new" statement by a method call.
	// return new RPL(to.elts.appendList(this.elts.tail));
	return replaceHeadBy(to);
    }

    public RPL substForVar(VarSymbol from, VarSymbol to) {
	RPL toRPL = symToRPL(to);
	return (toRPL == null) ? this : substForVar(from, toRPL);
    }

    public RPL substForVars(List<VarSymbol> from, List<VarSymbol> to) {
	RPL result = this;
	while (from.nonEmpty()) {
	    result = this.substForVar(from.head, to.head);
	    if (result != this) {
		break;
	    }
	    from = from.tail;
	    to = to.tail;
	}
	return result;
    }

    public RPL substExpForVar(VarSymbol from, JCExpression to) {
	RPL toRPL = exprToRPL(to);
	return (toRPL == null) ? this : substForVar(from, toRPL);
    }

    public RPL substExpsForVars(List<VarSymbol> from, List<JCExpression> to) {
	RPL result = this;
	while (from.nonEmpty() && to.nonEmpty()) {
	    result = this.substExpForVar(from.head, to.head);
	    if (result != this) {
		break;
	    }
	    from = from.tail;
	    to = to.tail;
	}
	return result;
    }

    public RPL substIndices(List<VarSymbol> from, List<JCExpression> to) {
	RPL result = this;
	while (!from.isEmpty() && !to.isEmpty()) {
	    result = result.substIndex(from.head, to.head);
	    from = from.tail;
	    to = to.tail;
	}
	return result;
    }

    // DPJIZER
    // public RPL substIndex(VarSymbol from, JCExpression to) {
    // ListBuffer<RPLElement> buf = ListBuffer.<RPLElement> lb();
    // for (RPLElement e : elts) {
    // if (e instanceof ArrayIndexRPLElement) {
    // ArrayIndexRPLElement ai = (ArrayIndexRPLElement) e;
    // JCExpression subst = substIndex(ai.indexExp, from, to);
    // if (subst != ai.indexExp)
    // e = new ArrayIndexRPLElement(subst);
    // }
    // buf.append(e);
    // }
    // return new RPL(buf.toList());
    // }

    private boolean isInt(VarSymbol var) {
	return var.type.tag == TypeTags.INT;
    }

    private boolean isFinal(VarSymbol var) {
	return var.getModifiers().contains(Modifier.FINAL);
    }

    private boolean canAppearInArrayIndexRPLElement(VarSymbol from) {
	return (isInt(from)) || (from.toString().equals("_"));
    }

    public RPL substIndex(VarSymbol from, JCExpression to) {
	if (!(canAppearInArrayIndexRPLElement(from))) {
	    return this;
	}
	return followedBy(new IndexSubstitution(from, to));
    }

    protected JCExpression substIndex(JCExpression tree, VarSymbol from,
	    JCExpression to) {
	return (new SubstIndexVisitor(from, to)).substIndex(tree);
    }

    /**
     * A simple visitor for substituting expressions for index variables. Note
     * the following:
     * 
     * 1. We can't use TreeTranslator here, because we don't want to mess up the
     * actual AST used by the program! So we have to replicate every node that
     * we want to modify.
     * 
     * 2. This simple visitor only handles singleton indices and (recursively)
     * binary expressions containing singleton indices. However, that's enough
     * for now: because we can only disambiguate constants, negated variables,
     * and binary expressions in array typing, expanding this visitor to do more
     * would be pointless. If and when we add more robust expression
     * disambiguation, we can expand this visitor as necessary.
     */
    private static class SubstIndexVisitor extends JCTree.Visitor {
	private VarSymbol from = null;
	private JCExpression to = null;
	public JCExpression result = null;

	public SubstIndexVisitor(VarSymbol from, JCExpression to) {
	    this.from = from;
	    this.to = to;
	}

	public JCExpression substIndex(JCExpression tree) {
	    result = tree;
	    if (tree != null)
		tree.accept(this);
	    return result;
	}

	public void visitIdent(JCIdent tree) {
	    if (tree.sym == from) {
		result = to;
	    }
	}

	public void visitBinary(JCBinary tree) {
	    JCExpression lhs = substIndex(tree.lhs);
	    JCExpression rhs = substIndex(tree.rhs);
	    if (lhs != tree.lhs || rhs != tree.rhs) {
		result = new JCBinary(tree.getTag(), lhs, rhs,
			tree.getOperator());
		result.pos = tree.pos;
	    }
	}

	@Override
	public void visitTree(JCTree tree) {
	}
    };

    /**
     * Compute the capture of an RPL: - If the RPL is fully specified, then the
     * capture is the same as the input - If the RPL is partially specified,
     * then the capture is a fresh RPL consisting of a fresh capture parameter
     * under the input RPL
     */

    public RPL capture() {
	return this.isFullySpecified() ? this : new RPL(
		new RPLElement.RPLCaptureParameter(this));
    }

    /**
     * The RPL as a member of t
     * 
     * @param t
     *            The type where we want this to be a member, after translation
     * @param owner
     *            The symbol associated with the class where this was defined
     */
    public RPL asMemberOf(Types types, Type t, Symbol owner) {
	RPL result = this;
	if (owner.type.hasRegionParams()) {
	    Type base = types.asOuterSuper(t, owner);
	    if (base != null) {
		List<RegionParameterSymbol> from = owner.type.allrgnparams();
		List<RPL> to = base.allrgnactuals();
		if (from.nonEmpty()) {
		    result = result.substForParams(from, to);
		}
		result = result.substForTRParams(owner.type.alltyparams(),
			base.alltyparams());
	    }
	}
	return result;

    }

    private static RPL substitutionInEnvironment(RPL rpl, Resolve rs,
	    Env<AttrContext> env) {
	if (rpl == null) {
	    return null;
	} else {
	    return new RPL(rpl.elts, rpl.substitutionChain.inEnvironment(rs,
		    env));
	}
    }

    /**
     * Conform the RPL to an enclosing environment. An RPL may contain elements
     * written in terms of local region names and/ or local variables that are
     * no longer in scope. If so, we need to either (1) replace the RPL with a
     * more general RPL whose elements are in scope; or (2) delete the RPL
     * (i.e., return null), if all regions it represents are out of scope.
     */
    public RPL inEnvironment(Resolve rs, Env<AttrContext> env,
	    boolean pruneLocalEffects) {
	// If the RPL is a capture parameter, compute its bound in the enclosing
	// environment
	if (elts.head instanceof RPLCaptureParameter) {
	    RPLCaptureParameter capture = (RPLCaptureParameter) elts.head;
	    RPL includedIn = capture.includedIn.inEnvironment(rs, env,
		    pruneLocalEffects);
	    // If bound is out of scope, so is capture parameter
	    if (includedIn == null)
		return null;
	    // Otherwise return new parameter only if bound changed
	    return substitutionInEnvironment(
		    (includedIn == capture.includedIn) ? this :
		    // DPJIZER
		    // new RPL(List.<RPLElement> of(new
		    // RPLCaptureParameter(includedIn)).appendList(elts.tail));
			    replaceHeadBy(new RPL(new RPLCaptureParameter(
				    includedIn))), rs, env);
	}
	// If the RPL starts with a variable v that is out of scope, then
	// replace the whole thing with R : *, where R is the owner parameter
	// of v's type, in this environment. Note that if R itself is out of
	// scope, the whole RPL may be deleted.
	if (elts.head instanceof RPLElement.VarRPLElement) {
	    RPLElement.VarRPLElement vrs = (RPLElement.VarRPLElement) elts.head;
	    if (!rs.isInScope(vrs.vsym, env)) {
		if (vrs.vsym.type instanceof ClassType) {
		    ClassType ct = (ClassType) vrs.vsym.type;
		    List<RPL> actuals = ct.getRegionActuals();
		    RPL owner = actuals.isEmpty() ? RPLs.ROOT : actuals.head;
		    // DPJIZER
		    // return new RPL(owner.elts.append(RPLElement.STAR)
		    // .appendList(elts.tail)).inEnvironment(rs, env,
		    // pruneLocalEffects);
		    RPL ownerStar = replaceHeadBy(owner.appenedByStar());
		    return ownerStar.inEnvironment(rs, env, pruneLocalEffects);
		}
	    }
	}
	// Truncate an RPL containing a non-variable element E that is out of
	// scope. If
	// E occurs in the first position, then the whole RPL is out of scope;
	// return null.
	// Otherwise, replace E and all following elements with *.
	for (RPLElement elt : elts) {
	    if (!rs.isInScope(elt, env))
		return substitutionInEnvironment(this.truncateTo(elt), rs, env);
	    if (pruneLocalEffects && elt.isLocalName())
		return substitutionInEnvironment(this.truncateTo(elt), rs, env);
	    /*
	     * if (elt instanceof StackRPLElement) { StackRPLElement sre =
	     * (StackRPLElement) elt; if (!rs.isInScope(sre.sym, env)) return
	     * null; } if (elt.isLocalName() && pruneLocalEffects) return null;
	     */
	}
	// Otherwise, go through the elements and look for array index elements
	// [e] where e is out of scope. Replace every such [e] with [?].
	ListBuffer<RPLElement> buf = ListBuffer.lb();
	boolean added = false;
	for (RPLElement elt : elts) {
	    if (elt instanceof ArrayIndexRPLElement) {
		ArrayIndexRPLElement ae = (ArrayIndexRPLElement) elt;
		if (!rs.isInScope(ae.indexExp, env)) {
		    // Replace elt with [?]
		    elt = new ArrayIndexRPLElement(null);
		    added = true;
		}
	    }
	    buf.append(elt);
	}
	return substitutionInEnvironment((added ? new RPL(buf.toList(),
		substitutionChain) : this), rs, env);
    }

    RPL truncateTo(RPLElement elt) {
	ListBuffer<RPLElement> buf = ListBuffer.lb();
	List<RPLElement> list = elts;
	while (list.nonEmpty() && list.head != elt) {
	    buf.append(list.head);
	    list = list.tail;
	}
	if (buf.isEmpty())
	    return null;
	buf.append(RPLElement.STAR);
	return new RPL(buf.toList(), substitutionChain);
    }

    // /////////////////////////////////////////////////////////////////////////

    // DPJIZER: The equality and hachCode of RPL's depends on their
    // substitution.
    // @Override
    // public boolean equals(Object other) {
    // if (this == other)
    // return true;
    // else if (other != null && other instanceof RPL)
    // return this.elts.equals(((RPL) other).elts);
    // else
    // return false;
    // }
    // @Override
    // public int hashCode() {
    // return elts.hashCode();
    // }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((elts == null) ? 0 : elts.hashCode());
	result = prime
		* result
		+ ((substitutionChain == null) ? 0 : substitutionChain
			.hashCode());
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
	RPL other = (RPL) obj;
	if (elts == null) {
	    if (other.elts != null)
		return false;
	} else if (!elts.equals(other.elts))
	    return false;
	if (substitutionChain == null) {
	    if (other.substitutionChain != null)
		return false;
	} else if (!substitutionChain.equals(other.substitutionChain))
	    return false;
	return true;
    }

    // /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	boolean first = true;
	for (RPLElement e : elts) {
	    if (first)
		first = false;
	    else
		sb.append(" : ");
	    sb.append(e);
	}

	// DPJIZER: Append the substitution chain.
	sb.append(substitutionChain);

	return sb.toString();
    }

    /**
     * The Java source which this RPL list represents. A List is represented as
     * a comma-separated listing of the elements in that list.
     */
    public static String toString(java.util.List<RPL> rpls) {
	return rpls.toString();
    }

    public boolean containsArrayAccess() {
	for (RPLElement e : elts)
	    if (e instanceof ArrayIndexRPLElement)
		return true;
	return false;
    }

}
