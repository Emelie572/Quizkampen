package Database;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryQuiz {

    CategoryContainer categoryContainer = new CategoryContainer();

    @JsonProperty
    private String categoryName;

    @JsonProperty
    private int categoryId;

    @Override
    public String toString() {
        return "CategoryQuiz{" +
                "categoryName='" + categoryName + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String name) {
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}





