package vn.techmaster.imdb;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import vn.techmaster.imdb.model.Film;
import vn.techmaster.imdb.repository.FilmRepository;

@SpringBootTest
@Slf4j
class FilmRepoTest {
	@Autowired
	private FilmRepository filmRepo;

	@Test
	@Order(1)
	public void getAll() {
		List<Film> filmList = filmRepo.getAll();
		log.info("--Testing getAll--");
		Assertions.assertThat(filmList).hasSize(30);
	}

	@Test
	@Order(2)
	public void getFilmByCountry() {
		Map<String, List<Film>> newMap = filmRepo.getFilmByCountry();
		log.info("--Testing getFilmByCountry--");
		Assertions.assertThat(newMap).hasSize(19);
	}

	@Test
	@Order(3)
	public void getcountryMakeMostFilms() {
		Entry<String, Integer> resultEntry = filmRepo.getcountryMakeMostFilms();
		log.info("--Testing getcountryMakeMostFilms--");
		log.info(resultEntry.toString());
		Assertions.assertThat(resultEntry.getKey()).hasToString("China");
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
		softAssertions.assertThat(resultMap.get("horror")).hasSize(4);
		softAssertions.assertAll();
	}

	@Test
	@Order(7)
	public void top5HighMarginFilms() {
		SoftAssertions softAssertions = new SoftAssertions();
		List<Film> resultList = filmRepo.top5HighMarginFilms();
		log.info("--Testing top5HighMarginFilms--");
		log.info(resultList.get(4).getTitle());
		log.info(resultList.get(0).getTitle());
		softAssertions.assertThat(resultList.get(0).getTitle()).hasToString("Resident Evil");
		softAssertions.assertThat(resultList.get(4).getTitle()).contains("Spring in a Small Town");
		softAssertions.assertThat(resultList.get(0).getMargin())
				.isGreaterThan(resultList.get(4).getMargin());
		softAssertions.assertAll();
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
		SoftAssertions softAssertions = new SoftAssertions();
		log.info("--Testing ratioBetweenGenere--");
		softAssertions.assertThat(filmRepo.ratioBetweenGenere("adventure", "history"))
				.isEqualTo(0.875);
		softAssertions.assertThat(filmRepo.ratioBetweenGenere("science", "horror")).isEqualTo(1);
		softAssertions.assertAll();
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
