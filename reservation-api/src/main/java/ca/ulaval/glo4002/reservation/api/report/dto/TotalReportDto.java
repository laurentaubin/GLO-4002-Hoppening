package ca.ulaval.glo4002.reservation.api.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.List;


@JsonPropertyOrder({ "ingredients", "totalPrice" })
public class TotalReportDto {
    @JsonProperty("ingredients")
    private List<IngredientsReportInformationDto> ingredients;
    private BigDecimal totalPrice;

    public TotalReportDto() {
    }

    public TotalReportDto(List<IngredientsReportInformationDto> ingredients, BigDecimal totalPrice) {
        this.ingredients = ingredients;
        this.totalPrice = totalPrice;
    }

    public List<IngredientsReportInformationDto> getIngredients() { return ingredients; }

    public BigDecimal getTotalPrice() { return totalPrice; }
}
