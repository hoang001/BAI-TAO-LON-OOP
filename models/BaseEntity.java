package org.example.models;

public class BaseEntity {
    int id;
    public BaseEntity() {}
    public BaseEntity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
