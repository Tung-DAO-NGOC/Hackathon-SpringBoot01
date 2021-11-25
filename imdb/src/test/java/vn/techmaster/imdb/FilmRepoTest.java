package vn.techmaster.imdb;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import vn.techmaster.imdb.model.Film;
import vn.techmaster.imdb.repository.FilmRepository;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmRepoTest {
	@Autowired
	private FilmRepository filmRepo;

	@Test
	@Order(1)
	public void getAll() {
		List<Film> filmList = filmRepo.getAll();
		log.info("--Testing getAll--");
		SoftAssertions softAssertions = new SoftAssertions();
		softAssertions.assertThat(filmList).hasOnlyElementsOfType(Film.class);
		softAssertions.assertThat(filmList.get(filmList.size() - 1).getId())
				.isEqualTo(filmList.size());
		softAssertions.assertAll();
	}

	@Test
	@Order(2)
	public void getFilmByCountry() {
		Map<String, List<Film>> resultMap = filmRepo.getFilmByCountry();
		List<Film> filmList = filmRepo.getAll();

		log.info("--Testing getFilmByCountry--");
		Assertions.assertThat(resultMap.values().stream().mapToInt(List::size).sum())
				.isEqualTo(filmList.size());
	};

	@Test
	@Order(3)
	public void getcountryMakeMostFilms() {
		Entry<String, Integer> resultEntry = filmRepo.getcountryMakeMostFilms();
		Map<String, List<Film>> resultMap = filmRepo.getFilmByCountry();
		log.info("--Testing getcountryMakeMostFilms--");
		SoftAssertions softAssertions = new SoftAssertions();
		softAssertions.assertThat(resultEntry.getValue()).isLessThanOrEqualTo(
				resultMap.values().stream().mapToInt(List::size).sum());
		softAssertions.assertThat(resultMap.keySet().contains(resultEntry.getKey())).isTrue();
		softAssertions.assertAll();
	}

	@Test
	@Order(4)
	public void yearMakeMostFilms() {
		Entry<Integer, Integer> resultEntry = filmRepo.yearMakeMostFilms();
		log.info("--Testing yearMakeMostFilms--");
		log.info(resultEntry.toString());
		Assertions.assertThat(resultEntry.getKey()).isEqualTo(1985);
	}

	@Test
	@Order(5)
	public void getAllGeneres() {
		SoftAssertions softAssertions = new SoftAssertions();
		List<String> listGenre = filmRepo.getAllGeneres();
		log.info("--Testing getAllGeneres--");
		log.info(String.valueOf(listGenre.size()));
		softAssertions.assertThat(listGenre).doesNotHaveDuplicates();
		softAssertions.assertThat(listGenre).doesNotContain("");
		softAssertions.assertAll();
	}

	@Test
	@Order(6)
	public void categorizeFilmByGenere() {
		SoftAssertions softAssertions = new SoftAssertions();
		log.info("--Testing categorizeFilmByGenere--");
		Map<String, List<Film>> resultMap = filmRepo.categorizeFilmByGenere();
		softAssertions.assertThat(resultMap).hasSize(15);
		softAssertions.assertThat(resultMap).doesNotContainKey("");
		softAssertions.assertAll();
	}

	@Test
	@Order(7)
	public void top5HighMarginFilms() {
		List<Film> resultList = filmRepo.top5HighMarginFilms();
		log.info("--Testing top5HighMarginFilms--");
		log.info(resultList.get(4).getTitle());
		log.info(resultList.get(0).getTitle());
		Assertions.assertThat(resultList.get(0).getMargin())
				.isGreaterThan(resultList.get(4).getMargin());
	}

	@Test
	@Order(8)
	public void top5HighMarginFilmsIn1990to2000() {
		List<Film> resultList = filmRepo.top5HighMarginFilmsIn1990to2000();
		log.info("--Testing top5HighMarginFilmsIn1990to2000--");
		log.info(resultList.get(0).getTitle());
		log.info(resultList.get(4).getTitle());
		SoftAssertions softAssertions = new SoftAssertions();
		softAssertions.assertThat(resultList)
				.allMatch(p -> p.getYear() >= 1990 && p.getYear() <= 2000);
		softAssertions.assertThat(resultList.get(0).getMargin() > resultList.get(4).getMargin())
				.isTrue();
		softAssertions.assertAll();
	}

	@Test
	@Order(9)
	public void ratioBetweenGenere() {
		List<String> allGenres = filmRepo.getAllGeneres();
		log.info("--Testing ratioBetweenGenere--");
		Double ratioCheck = filmRepo.ratioBetweenGenere(allGenres.get(0), allGenres.get(1));
		Double ratioReverse = filmRepo.ratioBetweenGenere(allGenres.get(1), allGenres.get(0));
		Assertions.assertThat(ratioCheck * ratioReverse).isEqualTo(1d);
	}

	@Test
	@Order(10)
	public void top5FilmsHighRatingButLowMargin() {
		List<Film> resultList = filmRepo.top5FilmsHighRatingButLowMargin();
		log.info("--Testing top5FilmsHighRatingButLowMargin--");
		Assertions.assertThat(resultList.get(0).getRating())
				.isGreaterThan(resultList.get(4).getRating());
	}

}
