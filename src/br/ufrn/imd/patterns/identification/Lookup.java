package br.ufrn.imd.patterns.identification;

import java.util.HashMap;
import java.util.Map;

public class Lookup {
	private static final Map<String, Object> lookupTable = new HashMap<>();
	
	public static void registerObject(String name, Object object) {
        lookupTable.put(name, object);
    }

    public static void registerObject(ObjectId objectId, Object object) {
        lookupTable.put(objectId.toString(), object);
    }

    public static Object getObject(String nameOrId) {
        return lookupTable.get(nameOrId);
    }

    public static boolean contains(String nameOrId) {
        return lookupTable.containsKey(nameOrId);
    }
}