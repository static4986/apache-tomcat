package server.support.map;

public class MappedContext extends MapElement<Context>{

    private MappedWrapper[] mapperWrapper;

    public MappedWrapper[] getMapperWrapper() {
        return mapperWrapper;
    }

    public void setMapperWrapper(MappedWrapper[] mapperWrapper) {
        this.mapperWrapper = mapperWrapper;
    }

    public MappedContext(String name, Context value) {
        super(name, value);
    }
}
