package vn.techmaster.imdb.repository;

import java.util.List;
import java.util.Map;


import vn.techmaster.imdb.model.Film;

public interface IFilmRepo {
  /**
   * Get all films into list
   * 
   * @param None
   * @return {@link List}<{@link Film}>: List of films 
   */
  public List<Film> getAll();


  /**
   * Classify films based on country 
   * 
   * @param None
   * @return {@link Map}<{@link String}, {@link List}<{@link Film}>>: Map of Country Name - List film 
   */  public Map<String, List<Film>> getFilmByCountry();

  /**
   * Find films product by a country from beginning year to ending year
   * @param country Name of country
   * @param fromYear Beginning year
   * @param toYear Ending year
   * @return {@link List}<{@link Film}> List of films satisfy the requirement
   */
  public List<Film> getFilmsMadeByCountryFromYearToYear(String country, int fromYear, int toYear);

  /**
   * Get the country that produce most of the films and the number of films that country has produced
   * 
   * @param None
   * @return {@link Map.Entry}<{@link String}, {@link Integer}> An pair of country name and number of films produced
   */
  public Map.Entry<String, Integer> getcountryMakeMostFilms();

    /**
   * Get the year that produce most of the films and the number of films has been produced in that year
   * 
   * @param None
   * @return {@link Map.Entry}<{@link Integer}, {@link Integer}> An pair of year and number of film produced
   */
  public Map.Entry<Integer, Integer> yearMakeMostFilms();

  /**
   * Return the list of all film genres
   * @param None
   * @return {@link List}<{@link String}> List of film genres 
   */
  public List<String> getAllGeneres();

  /**
   * Classify films based on its genre
   * @param None
   * @return {@link Map}<{@link String}, {@link List}<{@link Film}>>: Map of film genres - list films in that genre
   */
  public Map<String, List<Film>> categorizeFilmByGenere();


  /**
   * List of top 5 films with highest margin (margin = revenue - cost)
   * 
   * @param None
   * @return  {@link List}<{@link Film}> List of top 5 films with highest margin
   */
  public List<Film> top5HighMarginFilms();

  /**
   * List of top 5 films with highest margin (margin = revenue - cost) from 1990 to 2000
   * 
   * @param None
   * @return  {@link List}<{@link Film}> List of top 5 films with highest margin in the duration
   */
    public List<Film> top5HighMarginFilmsIn1990to2000();

  /**
   * The ratio of film between two genres
   * 
   * @param genreX Name of the first genre
   * @param genreY Name of the second genre
   * @return {@link Double} The ratio of number of films between first and second genre
   */
  public Double ratioBetweenGenere(String genreX, String genreY);

  /**
   * The list of top 5 films with highest rating but lowest margin
   * 
   * @param None
   * @return {@link List}<{@link Film}> The list of top 5 film with highest rating but lowest margin
   */
  public List<Film> top5FilmsHighRatingButLowMargin();
}
