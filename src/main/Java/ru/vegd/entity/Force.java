package ru.vegd.entity;

import java.io.Serializable;
import java.util.Objects;

public class Force implements Serializable {

    private static final long serialVersionUID = -8803513478606613346L;

    private String id;
    private String name;

    public Force() {}

    public Force(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Force that = (Force) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Force{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
