package Database;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CategoryContainer {

    @JsonProperty
    private List<CategoryQuiz> categories;

    public List<CategoryQuiz> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryQuiz> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "categories=" + categories +
                '}';
    }
}