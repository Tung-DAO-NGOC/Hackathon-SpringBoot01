package vn.techmaster.imdb.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Film {
    private int id;
    private String title;
    private int year;
    private String country;
    private double rating;
    private List<String> generes;
    private int cost;
    private int revenue;


    public int getMargin() {
        return this.revenue - this.cost;
    }
}
