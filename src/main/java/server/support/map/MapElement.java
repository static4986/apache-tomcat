package server.support.map;

import server.pojo.HttpServlet;

public class MapElement<T> {
    private String name;
    private T value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public MapElement(String name, T value) {
        this.name = name;
        this.value = value;
    }
}
