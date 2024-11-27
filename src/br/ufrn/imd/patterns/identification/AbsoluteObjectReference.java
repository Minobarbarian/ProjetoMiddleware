package br.ufrn.imd.patterns.identification;

public class AbsoluteObjectReference {
	private final ObjectId objectId;
	private final String host;
	private final int port;
	
	public AbsoluteObjectReference(ObjectId objectId, String host, int port) {
		this.objectId = objectId;
        this.host = host;
        this.port = port;
	}
	
	public ObjectId getObjectId() {
        return objectId;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return String.format("[id=%s, host=%s, port=%d]", objectId, host, port);
    }
}