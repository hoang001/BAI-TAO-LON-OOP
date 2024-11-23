package org.example.models;

public class CategoryEntity extends BaseEntity{
    private String categoryName;
    private String description;

    public CategoryEntity() {}
    public CategoryEntity(int categoryNameId, String categoryName, String description) {
        super(categoryNameId);
        this.categoryName = categoryName;
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
