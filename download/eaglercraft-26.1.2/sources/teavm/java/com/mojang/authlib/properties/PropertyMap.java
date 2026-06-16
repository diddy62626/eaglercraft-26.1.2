package com.mojang.authlib.properties;

import java.util.Collection;
import java.util.Collections;

/**
 * EaglerCraft 26.1.2 browser stub for com.mojang.authlib.properties.PropertyMap.
 * Minimal property map - EaglerCraft doesn't use real Mojang authentication,
 * so this is always empty.
 */
public class PropertyMap {

	public PropertyMap() {
	}

	public Collection<Property> get(String key) {
		return Collections.emptyList();
	}

	public void put(String key, Property property) {
		// no-op
	}

	public boolean containsKey(String key) {
		return false;
	}

	public boolean isEmpty() {
		return true;
	}

	public int size() {
		return 0;
	}

	/**
	 * Property stub.
	 */
	public static class Property {
		private final String name;
		private final String value;
		private final String signature;

		public Property(String name, String value) {
			this(name, value, null);
		}

		public Property(String name, String value, String signature) {
			this.name = name;
			this.value = value;
			this.signature = signature;
		}

		public String getName() { return name; }
		public String getValue() { return value; }
		public String getSignature() { return signature; }
		public boolean hasSignature() { return signature != null; }
	}
}
