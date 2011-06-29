/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.sun.tools.javac.code.dpjizer.constraints;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Stephen Heumann
 * @author Mohsen Vakilian
 * 
 */
public class MultiMap<K, V> implements Cloneable {

    private HashMap<K, Set<V>> map;

    public MultiMap() {
	map = new HashMap<K, Set<V>>();
    }

    private MultiMap(HashMap<K, Set<V>> map) {
	this.map = map;
    }

    public boolean containsKey(Object key) {
	return map.containsKey(key);
    }

    public Set<V> get(Object key) {
	return map.get(key);
    }

    public void put(K key, V value) {
	Set<V> set;
	if (!map.containsKey(key)) {
	    set = new HashSet<V>();
	} else {
	    set = map.get(key);
	}
	set.add(value);
	map.put(key, set);
    }

    public Set<K> keySet() {
	return map.keySet();
    }

    public Set<V> values() {
	Set<V> values = new HashSet<V>();
	for (K k : keySet()) {
	    values.addAll(map.get(k));
	}
	return values;
    }

    @Override
    public MultiMap<K, V> clone() {
	return new MultiMap<K, V>((HashMap<K, Set<V>>) map.clone());
    }

}