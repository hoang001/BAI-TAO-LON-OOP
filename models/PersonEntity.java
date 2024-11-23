package org.example.models;

public class PersonEntity extends BaseEntity {
    private String name;
    public PersonEntity() {}
    public PersonEntity(int personId, String name) {
        super(personId);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
