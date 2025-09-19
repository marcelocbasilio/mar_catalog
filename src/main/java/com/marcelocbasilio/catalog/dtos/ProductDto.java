package com.marcelocbasilio.catalog.dtos;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.marcelocbasilio.catalog.entities.Category;
import com.marcelocbasilio.catalog.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO for {@link com.marcelocbasilio.catalog.entities.Product}
 */
public class ProductDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

    @Size(min = 5, max = 60, message = "Nome deve ter entre 5 e 60 caracteres.")
    @NotBlank(message = "Campo nome obrigatório")
    private String name;

    @NotBlank(message = "Campo descrição obrigatório.")
    private String description;

    @Positive(message = "Preço deve ser um valor positivo.")
    private Double price;
    private String imgUrl;

    @PastOrPresent(message = "A data do produto não pode ser futura.")
    private Instant date;

    private List<CategoryDto> categories = new ArrayList<>();

    public ProductDto() {
    }

    public ProductDto(Long id, String name, String description, Double price, String imgUrl, Instant date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
    }

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imgUrl = product.getImgUrl();
        this.date = product.getDate();
    }

    public ProductDto(Product product, Set<Category> categories) {
        this(product);
        categories.forEach(category -> this.categories.add(new CategoryDto(category)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDto entity = (ProductDto) o;
        return Objects.equals(this.id, entity.id) && Objects.equals(this.name, entity.name) && Objects.equals(this.description, entity.description) && Objects.equals(this.price, entity.price) && Objects.equals(this.imgUrl, entity.imgUrl) && Objects.equals(this.date, entity.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, imgUrl, date);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + "id = " + id + ", " + "name = " + name + ", " + "description = " + description + ", " + "price = " + price + ", " + "imgUrl = " + imgUrl + ", " + "date = " + date + ")";
    }
}