package net.giro.clientes.plataforma;

import java.util.*;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class MiMapa implements Map{
	private HashMap miHashMap;
	
	public MiMapa(){
		this.setMiHashMap(new HashMap());
	}
	
	public void clear() {
		this.miHashMap.clear();
	}

	public boolean containsKey(Object key) {
		return this.miHashMap.containsKey(key);
	}

	public boolean containsValue(Object arg0) {
		this.containsValue(arg0);
		return false;
	}

	public Set entrySet() {
		return this.miHashMap.entrySet();
	}

	public Object get(Object arg0) {
		return this.miHashMap.get(arg0);
	}

	public boolean isEmpty() {
		return this.miHashMap.isEmpty();
	}

	public Set keySet() {
		return this.miHashMap.keySet();
	}

	public Object put(Object arg0, Object arg1) {
		return this.miHashMap.put(arg0, arg1);
	}

	public void putAll(Map arg0) {
		this.miHashMap.putAll(arg0);
	}

	public Object remove(Object arg0) {
		return this.miHashMap.remove(arg0);
	}

	public int size() {
		return this.miHashMap.size();
	}

	public Collection values() {
		return this.miHashMap.values();
	}

	public void setMiHashMap(HashMap miHashMap) {
		this.miHashMap = miHashMap;
	}

	public HashMap getMiHashMap() {
		return miHashMap;
	}

}
