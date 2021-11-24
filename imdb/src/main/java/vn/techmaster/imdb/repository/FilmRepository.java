package vn.techmaster.imdb.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import lombok.extern.slf4j.Slf4j;
import vn.techmaster.imdb.model.Film;

@Repository
@Slf4j
public class FilmRepository implements IFilmRepo {
  private List<Film> films;

  public FilmRepository(@Value("${datafile}") String datafile) {
    try {
      File file = ResourceUtils.getFile("classpath:static/" + datafile);
      ObjectMapper mapper = new ObjectMapper(); // Dùng để ánh xạ cột trong CSV với từng trường
                                                // trong POJO
      films = Arrays.asList(mapper.readValue(file, Film[].class));
    } catch (IOException e) {
      log.info(e.getMessage());
    }
  }

  @Override
  public List<Film> getAll() {
    return films;
  }

  @Override
  public Map<String, List<Film>> getFilmByCountry() {
    Map<String, List<Film>> map = new HashMap<>();
    films.stream().parallel().forEach(film -> {
      List<Film> filmList = map.getOrDefault(film.getCountry(), new ArrayList<>());
      filmList.add(film);
      map.put(film.getCountry(), filmList);
    });
    return map;
  }

  @Override
  public Entry<String, Integer> getcountryMakeMostFilms() {
    Map<String, Integer> resultMap = new HashMap<>();
    films.stream().parallel()
        .forEach(film -> resultMap.put(film.getCountry(),
            resultMap.getOrDefault(film.getCountry(), 0) + 1));
    return Collections.max(resultMap.entrySet(), (e1, e2) -> e1.getValue() - e2.getValue());
  }

  @Override
  public Entry<Integer, Integer> yearMakeMostFilms() {
    Map<Integer, Integer> resultMap = new HashMap<>();
    films.stream().parallel()
        .forEach(
            film -> resultMap.put(film.getYear(),
                resultMap.getOrDefault(film.getYear(), 0) + 1));
    return Collections.max(resultMap.entrySet(), (e1, e2) -> e1.getValue() - e2.getValue());
  }

  @Override
  public List<String> getAllGeneres() {
    Set<String> filmGenre = new HashSet<>();
    films.stream().parallel()
        .forEach(film -> film.getGeneres().stream().parallel()
            .forEach(genre -> {
              if (!genre.isBlank())
                filmGenre.add(genre);
            }));
    return List.copyOf(filmGenre);
  }

  @Override
  public List<Film> getFilmsMadeByCountryFromYearToYear(String country, int fromYear, int toYear) {
    return films.stream().parallel()
        .filter(film -> ((film.getYear() <= toYear) &&
            (film.getYear() >= fromYear) &&
            (film.getCountry().equalsIgnoreCase(country))))
        .toList();
  }

  @Override
  public Map<String, List<Film>> categorizeFilmByGenere() {
    Map<String, List<Film>> resultMap = new HashMap<>();
    films.stream().parallel()
        .forEach(film -> film.getGeneres().stream().parallel()
            .forEach(genre -> {
              if (!genre.isBlank()) {
                List<Film> listFilm = resultMap.getOrDefault(genre, new ArrayList<>());
                listFilm.add(film);
                resultMap.put(genre, listFilm);
              }
            }));
    return resultMap;
  }

  @Override
  public List<Film> top5HighMarginFilms() {
    return films.stream().sorted((f1, f2) -> f2.getMargin() - f1.getMargin()).limit(5).toList();
  }

  @Override
  public List<Film> top5HighMarginFilmsIn1990to2000() {
    return films.stream().filter(film -> ((film.getYear() >= 1990) && (film.getYear() <= 2000)))
        .sorted((f1, f2) -> f2.getMargin() - f1.getMargin())
        .limit(5).toList();
  }

  @Override
  public Double ratioBetweenGenere(String genreX, String genreY) {
    Map<String, List<Film>> listFilmByGenre = this.categorizeFilmByGenere();
    return (listFilmByGenre.get(genreX).size() / (double) listFilmByGenre.get(genreY).size());
  }

  @Override
  public List<Film> top5FilmsHighRatingButLowMargin() {
    Comparator<Film> filmComparyByRatingAndMargin = Comparator.comparing(Film::getRating).reversed()
        .thenComparing(Film::getMargin);
    return films.stream().sorted(filmComparyByRatingAndMargin).limit(5).toList();
  }

}
