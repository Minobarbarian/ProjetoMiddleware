package br.ufrn.imd.patterns.identification;

public class AbsoluteObjectReference {
	private final ObjectId objectId;
	private final Class<?> clazz;
	private final String host;
	private final int port;
	
	public AbsoluteObjectReference(ObjectId objectId, Class<?> clazz, String host, int port) {
		this.objectId = objectId;
		this.clazz = clazz;
        this.host = host;
        this.port = port;
	}
	
	public ObjectId getObjectId() {
        return objectId;
    }
	
	public Class<?> getClazz() {
        return clazz;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
    	return String.format("[id=%s, class=%s, host=%s, port=%d]", objectId, clazz.getName(), host, port);
    }
}