package server.support.map;

public class MappedHost extends MapElement<Host>{

    private ContextList contextLists;

    public ContextList getContextLists() {
        return contextLists;
    }

    public void setContextLists(ContextList contextLists) {
        this.contextLists = contextLists;
    }

    public MappedHost(String name, Host value) {
        super(name, value);
    }
}
