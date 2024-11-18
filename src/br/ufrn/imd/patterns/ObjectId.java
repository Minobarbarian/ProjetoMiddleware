package br.ufrn.imd.patterns;

import java.util.UUID;

public class ObjectId {
	private final String id;
	
	public ObjectId() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return id;
	}
}
