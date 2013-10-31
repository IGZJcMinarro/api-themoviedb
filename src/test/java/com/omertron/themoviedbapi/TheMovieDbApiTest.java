/*
 *      Copyright (c) 2004-2013 Stuart Boston
 *
 *      This file is part of TheMovieDB API.
 *
 *      TheMovieDB API is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      TheMovieDB API is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with TheMovieDB API.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.omertron.themoviedbapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.omertron.themoviedbapi.model.Account;
import com.omertron.themoviedbapi.model.AlternativeTitle;
import com.omertron.themoviedbapi.model.Artwork;
import com.omertron.themoviedbapi.model.ChangedItem;
import com.omertron.themoviedbapi.model.ChangedMovie;
import com.omertron.themoviedbapi.model.Collection;
import com.omertron.themoviedbapi.model.CollectionInfo;
import com.omertron.themoviedbapi.model.Company;
import com.omertron.themoviedbapi.model.Discover;
import com.omertron.themoviedbapi.model.ExternalIds;
import com.omertron.themoviedbapi.model.Genre;
import com.omertron.themoviedbapi.model.JobDepartment;
import com.omertron.themoviedbapi.model.Keyword;
import com.omertron.themoviedbapi.model.ReleaseInfo;
import com.omertron.themoviedbapi.model.Reviews;
import com.omertron.themoviedbapi.model.StatusCode;
import com.omertron.themoviedbapi.model.TmdbConfiguration;
import com.omertron.themoviedbapi.model.TokenAuthorisation;
import com.omertron.themoviedbapi.model.TokenSession;
import com.omertron.themoviedbapi.model.Trailer;
import com.omertron.themoviedbapi.model.Translation;
import com.omertron.themoviedbapi.model.movie.MovieDb;
import com.omertron.themoviedbapi.model.movie.MovieDbBasic;
import com.omertron.themoviedbapi.model.movie.MovieDbList;
import com.omertron.themoviedbapi.model.movie.MovieList;
import com.omertron.themoviedbapi.model.person.Person;
import com.omertron.themoviedbapi.model.person.PersonCredit;
import com.omertron.themoviedbapi.model.tv.TVEpisode;
import com.omertron.themoviedbapi.model.tv.TVSeason;
import com.omertron.themoviedbapi.model.tv.TVSeries;
import com.omertron.themoviedbapi.model.tv.TVSeriesBasic;
import com.omertron.themoviedbapi.results.TmdbResultsList;
import com.omertron.themoviedbapi.results.TmdbResultsMap;
import org.junit.Test;

/**
 * Test cases for TheMovieDbApi API
 *
 * @author stuart.boston
 */
public class TheMovieDbApiTest {

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(TheMovieDbApiTest.class);
    // API Key
    private static final String API_KEY = "5a1a77e2eba8984804586122754f969f";
    private static TheMovieDbApi tmdb;
    // Test data
    private static final int ID_MOVIE_BLADE_RUNNER = 78;
    private static final int ID_MOVIE_THE_AVENGERS = 24428;
    private static final int ID_COLLECTION_STAR_WARS = 10;
    private static final int ID_PERSON_BRUCE_WILLIS = 62;
    private static final int ID_COMPANY = 2;
    private static final String COMPANY_NAME = "Marvel Studios";
    private static final int ID_GENRE_ACTION = 28;
    private static final String ID_KEYWORD = "1721";
    private static final int ID_BIG_BANG_THEORY = 1418;
    // Languages
    private static final String LANGUAGE_DEFAULT = "";
    private static final String LANGUAGE_ENGLISH = "en";
    private static final String LANGUAGE_RUSSIAN = "ru";
    // session and account id of test users named 'apitests'
    private static final String SESSION_ID_APITESTS = "63c85deb39337e29b69d78265eb28d639cbd6f72";
    private static final int ACCOUNT_ID_APITESTS = 6065849;

    public TheMovieDbApiTest() throws MovieDbException {
    }

    @BeforeClass
    public static void setUpClass() throws MovieDbException {
        tmdb = new TheMovieDbApi(API_KEY);
        TestLogger.Configure();
    }

    @AfterClass
    public static void tearDownClass() throws MovieDbException {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getConfiguration method, of class TheMovieDbApi.
     */
    //@Test
    public void testConfiguration() {
        LOG.info("Test Configuration");

        TmdbConfiguration tmdbConfig = tmdb.getConfiguration();
        assertNotNull("Configuration failed", tmdbConfig);
        assertTrue("No base URL", StringUtils.isNotBlank(tmdbConfig.getBaseUrl()));
        assertTrue("No backdrop sizes", tmdbConfig.getBackdropSizes().size() > 0);
        assertTrue("No poster sizes", tmdbConfig.getPosterSizes().size() > 0);
        assertTrue("No profile sizes", tmdbConfig.getProfileSizes().size() > 0);
        LOG.info(tmdbConfig.toString());
    }

    //@Test
    public void testAccount() throws MovieDbException {
        Account account = tmdb.getAccount(SESSION_ID_APITESTS);

        // Make sure properties are extracted correctly
        assertEquals(account.getUserName(), "apitests");
        assertEquals(account.getId(), ACCOUNT_ID_APITESTS);
    }

    @Ignore("Session required")
    public void testWatchList() throws MovieDbException {
        // make sure it's empty (because it's just a test account
        Assert.assertTrue(tmdb.getWatchList(SESSION_ID_APITESTS, ACCOUNT_ID_APITESTS).isEmpty());

        // add a movie
        tmdb.addToWatchList(SESSION_ID_APITESTS, ACCOUNT_ID_APITESTS, 550);

        List<MovieDb> watchList = tmdb.getWatchList(SESSION_ID_APITESTS, ACCOUNT_ID_APITESTS);
        assertNotNull("Empty watch list returned", watchList);
        assertEquals("Watchlist wrong size", 1, watchList.size());

        // clean up again
        tmdb.removeFromWatchList(SESSION_ID_APITESTS, ACCOUNT_ID_APITESTS, 550);

        Assert.assertTrue(tmdb.getWatchList(SESSION_ID_APITESTS, ACCOUNT_ID_APITESTS).isEmpty());
    }

    @Ignore("Session required")
    public void testFavorites() throws MovieDbException {
        // make sure it's empty (because it's just a test account
        Assert.assertTrue(tmdb.getFavoriteMovies(SESSION_ID_APITESTS, ACCOUNT_ID_APITESTS).isEmpty());

        // add a movie
        tmdb.changeFavoriteStatus(SESSION_ID_APITESTS, ACCOUNT_ID_APITESTS, 550, true);

        List<MovieDb> watchList = tmdb.getFavoriteMovies(SESSION_ID_APITESTS, ACCOUNT_ID_APITESTS);
        assertNotNull("Empty watch list returned", watchList);
        assertEquals("Watchlist wrong size", 1, watchList.size());

        // clean up again
        tmdb.changeFavoriteStatus(SESSION_ID_APITESTS, ACCOUNT_ID_APITESTS, 550, false);

        Assert.assertTrue(tmdb.getFavoriteMovies(SESSION_ID_APITESTS, ACCOUNT_ID_APITESTS).isEmpty());
    }

    /**
     * Test of searchMovie method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testSearchMovie() throws MovieDbException {
        LOG.info("searchMovie");

        // Try a movie with less than 1 page of results
        TmdbResultsList<MovieDb> movieList = tmdb.searchMovie("Blade Runner", 0, "", true, 0);
//        List<MovieDb> movieList = tmdb.searchMovie("Blade Runner", "", true);
        assertTrue("No movies found, should be at least 1", movieList.getResults().size() > 0);

        // Try a russian langugage movie
        movieList = tmdb.searchMovie("О чём говорят мужчины", 0, LANGUAGE_RUSSIAN, true, 0);
        assertTrue("No 'RU' movies found, should be at least 1", movieList.getResults().size() > 0);

        // Try a movie with more than 20 results
        movieList = tmdb.searchMovie("Star Wars", 0, LANGUAGE_ENGLISH, false, 0);
        assertTrue("Not enough movies found, should be over 15, found " + movieList.getResults().size(), movieList.getResults().size() >= 15);
    }

    /**
     * Test of getMovieInfo method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetMovieInfo() throws MovieDbException {
        LOG.info("getMovieInfo");
        MovieDb result = tmdb.getMovieInfo(ID_MOVIE_BLADE_RUNNER, LANGUAGE_ENGLISH, "alternative_titles,casts,images,keywords,releases,trailers,translations,similar_movies,reviews,lists");
        assertEquals("Incorrect movie information", "Blade Runner", result.getOriginalTitle());
    }

    /**
     * Test of getMovieAlternativeTitles method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetMovieAlternativeTitles() throws MovieDbException {
        LOG.info("getMovieAlternativeTitles");
        String country = "";
        TmdbResultsList<AlternativeTitle> result = tmdb.getMovieAlternativeTitles(ID_MOVIE_BLADE_RUNNER, country, "casts,images,keywords,releases,trailers,translations,similar_movies,reviews,lists");
        assertTrue("No alternative titles found", result.getResults().size() > 0);

        country = "US";
        result = tmdb.getMovieAlternativeTitles(ID_MOVIE_BLADE_RUNNER, country);
        assertTrue("No alternative titles found", result.getResults().size() > 0);

    }

    /**
     * Test of getMovieCasts method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetMovieCasts() throws MovieDbException {
        LOG.info("getMovieCasts");
        TmdbResultsList<Person> people = tmdb.getMovieCasts(ID_MOVIE_BLADE_RUNNER, "alternative_titles,casts,images,keywords,releases,trailers,translations,similar_movies,reviews,lists");
        assertTrue("No cast information", people.getResults().size() > 0);

        String name1 = "Harrison Ford";
        String name2 = "Charles Knode";
        boolean foundName1 = Boolean.FALSE;
        boolean foundName2 = Boolean.FALSE;

        for (Person person : people.getResults()) {
            if (!foundName1 && person.getName().equalsIgnoreCase(name1)) {
                foundName1 = Boolean.TRUE;
            }

            if (!foundName2 && person.getName().equalsIgnoreCase(name2)) {
                foundName2 = Boolean.TRUE;
            }
        }
        assertTrue("Couldn't find " + name1, foundName1);
        assertTrue("Couldn't find " + name2, foundName2);
    }

    /**
     * Test of getMovieImages method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetMovieImages() throws MovieDbException {
        LOG.info("getMovieImages");
        String language = "";
        TmdbResultsList<Artwork> result = tmdb.getMovieImages(ID_MOVIE_BLADE_RUNNER, language);
        assertFalse("No artwork found", result.getResults().isEmpty());
    }

    /**
     * Test of getMovieKeywords method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetMovieKeywords() throws MovieDbException {
        LOG.info("getMovieKeywords");
        TmdbResultsList<Keyword> result = tmdb.getMovieKeywords(ID_MOVIE_BLADE_RUNNER);
        assertFalse("No keywords found", result.getResults().isEmpty());
    }

    /**
     * Test of getMovieReleaseInfo method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetMovieReleaseInfo() throws MovieDbException {
        LOG.info("getMovieReleaseInfo");
        TmdbResultsList<ReleaseInfo> result = tmdb.getMovieReleaseInfo(ID_MOVIE_BLADE_RUNNER, "");
        assertFalse("Release information missing", result.getResults().isEmpty());
    }

    /**
     * Test of getMovieTrailers method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetMovieTrailers() throws MovieDbException {
        LOG.info("getMovieTrailers");
        TmdbResultsList<Trailer> result = tmdb.getMovieTrailers(ID_MOVIE_BLADE_RUNNER, "");
        assertFalse("Movie trailers missing", result.getResults().isEmpty());
    }

    /**
     * Test of getMovieTranslations method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetMovieTranslations() throws MovieDbException {
        LOG.info("getMovieTranslations");
        TmdbResultsList<Translation> result = tmdb.getMovieTranslations(ID_MOVIE_BLADE_RUNNER);
        assertFalse("No translations found", result.getResults().isEmpty());
    }

    /**
     * Test of getCollectionInfo method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetCollectionInfo() throws MovieDbException {
        LOG.info("getCollectionInfo");
        String language = "";
        CollectionInfo result = tmdb.getCollectionInfo(ID_COLLECTION_STAR_WARS, language);
        assertFalse("No collection information", result.getParts().isEmpty());
    }

    /**
     * Test of createImageUrl method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testCreateImageUrl() throws MovieDbException {
        LOG.info("createImageUrl");
        MovieDb movie = tmdb.getMovieInfo(ID_MOVIE_BLADE_RUNNER, "");
        String result = tmdb.createImageUrl(movie.getPosterPath(), "original").toString();
        assertTrue("Error compiling image URL", !result.isEmpty());
    }

    /**
     * Test of getMovieInfoImdb method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetMovieInfoImdb() throws MovieDbException {
        LOG.info("getMovieInfoImdb");
        MovieDb result = tmdb.getMovieInfoImdb("tt0076759", "en-US");
        assertTrue("Error getting the movie from IMDB ID", result.getId() == 11);
    }

    /**
     * Test of getApiKey method, of class TheMovieDbApi.
     *
     */
    @Ignore("Test not required")
    public void testGetApiKey() {
        // Not required
    }

    /**
     * Test of getApiBase method, of class TheMovieDbApi.
     *
     */
    @Ignore("Test not required")
    public void testGetApiBase() {
        // Not required
    }

    /**
     * Test of getConfiguration method, of class TheMovieDbApi.
     *
     */
    @Ignore("Test not required")
    public void testGetConfiguration() {
        // Not required
    }

    /**
     * Test of searchPeople method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testSearchPeople() throws MovieDbException {
        LOG.info("searchPeople");
        String personName = "Bruce Willis";
        boolean includeAdult = false;
        TmdbResultsList<Person> result = tmdb.searchPeople(personName, includeAdult, 0);
        assertTrue("Couldn't find the person", result.getResults().size() > 0);
    }

    /**
     * Test of getPersonInfo method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetPersonInfo() throws MovieDbException {
        LOG.info("getPersonInfo");
        Person result = tmdb.getPersonInfo(ID_PERSON_BRUCE_WILLIS);
        assertTrue("Wrong actor returned", result.getId() == ID_PERSON_BRUCE_WILLIS);
    }

    /**
     * Test of getPersonCredits method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetPersonCredits() throws MovieDbException {
        LOG.info("getPersonCredits");

        TmdbResultsList<PersonCredit> result = tmdb.getPersonCredits(ID_PERSON_BRUCE_WILLIS);
        assertTrue("No cast information", result.getResults().size() > 0);
    }

    /**
     * Test of getPersonImages method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetPersonImages() throws MovieDbException {
        LOG.info("getPersonImages");

        TmdbResultsList<Artwork> result = tmdb.getPersonImages(ID_PERSON_BRUCE_WILLIS);
        assertTrue("No cast information", result.getResults().size() > 0);
    }

    /**
     * Test of getLatestMovie method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetLatestMovie() throws MovieDbException {
        LOG.info("getLatestMovie");
        MovieDb result = tmdb.getLatestMovie();
        assertTrue("No latest movie found", result != null);
        assertTrue("No latest movie found", result.getId() > 0);
    }

    /**
     * Test of compareMovies method, of class TheMovieDbApi.
     *
     */
    //@Test
    public void testCompareMovies() {
        // Not required
    }

    /**
     * Test of setProxy method, of class TheMovieDbApi.
     *
     */
    //@Test
    public void testSetProxy() {
        // Not required
    }

    /**
     * Test of setTimeout method, of class TheMovieDbApi.
     *
     */
    //@Test
    public void testSetTimeout() {
        // Not required
    }

    /**
     * Test of getNowPlayingMovies method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetNowPlayingMovies() throws MovieDbException {
        LOG.info("getNowPlayingMovies");
        TmdbResultsList<MovieDb> result = tmdb.getNowPlayingMovies(LANGUAGE_DEFAULT, 0);
        assertTrue("No now playing movies found", !result.getResults().isEmpty());
    }

    /**
     * Test of getPopularMovieList method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetPopularMovieList() throws MovieDbException {
        LOG.info("getPopularMovieList");
        TmdbResultsList<MovieDb> result = tmdb.getPopularMovieList(LANGUAGE_DEFAULT, 0);
        assertTrue("No popular movies found", !result.getResults().isEmpty());
    }

    /**
     * Test of getTopRatedMovies method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetTopRatedMovies() throws MovieDbException {
        LOG.info("getTopRatedMovies");
        TmdbResultsList<MovieDb> result = tmdb.getTopRatedMovies(LANGUAGE_DEFAULT, 0);
        assertTrue("No top rated movies found", !result.getResults().isEmpty());
    }

    /**
     * Test of getCompanyInfo method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetCompanyInfo() throws MovieDbException {
        LOG.info("getCompanyInfo");
        Company company = tmdb.getCompanyInfo(ID_COMPANY);
        assertTrue("No company information found", company.getCompanyId() > 0);
        assertNotNull("No parent company found", company.getParentCompany());
    }

    /**
     * Test of getCompanyMovies method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetCompanyMovies() throws MovieDbException {
        LOG.info("getCompanyMovies");
        TmdbResultsList<MovieDb> result = tmdb.getCompanyMovies(ID_COMPANY, LANGUAGE_DEFAULT, 0);
        assertTrue("No company movies found", !result.getResults().isEmpty());
    }

    /**
     * Test of searchCompanies method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testSearchCompanies() throws MovieDbException {
        LOG.info("searchCompanies");
        TmdbResultsList<Company> result = tmdb.searchCompanies(COMPANY_NAME, 0);
        assertTrue("No company information found", !result.getResults().isEmpty());
    }

    /**
     * Test of getSimilarMovies method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetSimilarMovies() throws MovieDbException {
        LOG.info("getSimilarMovies");
        TmdbResultsList<MovieDb> result = tmdb.getSimilarMovies(ID_MOVIE_BLADE_RUNNER, LANGUAGE_DEFAULT, 0);
        assertTrue("No similar movies found", !result.getResults().isEmpty());
    }

    /**
     * Test of getGenreList method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetGenreList() throws MovieDbException {
        LOG.info("getGenreList");
        TmdbResultsList<Genre> result = tmdb.getGenreList(LANGUAGE_DEFAULT);
        assertTrue("No genres found", !result.getResults().isEmpty());
    }

    /**
     * Test of getGenreMovies method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetGenreMovies() throws MovieDbException {
        LOG.info("getGenreMovies");
        TmdbResultsList<MovieDb> result = tmdb.getGenreMovies(ID_GENRE_ACTION, LANGUAGE_DEFAULT, 0, Boolean.TRUE);
        assertTrue("No genre movies found", !result.getResults().isEmpty());
    }

    /**
     * Test of getUpcoming method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetUpcoming() throws MovieDbException {
        LOG.info("getUpcoming");
        TmdbResultsList<MovieDb> result = tmdb.getUpcoming(LANGUAGE_DEFAULT, 0);
        assertTrue("No upcoming movies found", !result.getResults().isEmpty());
    }

    /**
     * Test of getCollectionImages method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetCollectionImages() throws MovieDbException {
        LOG.info("getCollectionImages");
        TmdbResultsList<Artwork> result = tmdb.getCollectionImages(ID_COLLECTION_STAR_WARS, LANGUAGE_DEFAULT);
        assertFalse("No artwork found", result.getResults().isEmpty());
    }

    /**
     * Test of getAuthorisationToken method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetAuthorisationToken() throws MovieDbException {
        LOG.info("getAuthorisationToken");
        TokenAuthorisation result = tmdb.getAuthorisationToken();
        assertFalse("Token is null", result == null);
        assertTrue("Token is not valid", result.getSuccess());
        LOG.info(result.toString());
    }

    /**
     * Test of getSessionToken method, of class TheMovieDbApi.
     *
     * TODO: Cannot be tested without a HTTP authorisation: http://help.themoviedb.org/kb/api/user-authentication
     *
     * @throws MovieDbException
     */
    @Ignore("Session required")
    public void testGetSessionToken() throws MovieDbException {
        LOG.info("getSessionToken");
        TokenAuthorisation token = tmdb.getAuthorisationToken();
        assertFalse("Token is null", token == null);
        assertTrue("Token is not valid", token.getSuccess());
        LOG.info("Token: {}", token.toString());

        TokenSession result = tmdb.getSessionToken(token);
        assertFalse("Session token is null", result == null);
        assertTrue("Session token is not valid", result.getSuccess());
        LOG.info(result.toString());
    }

    /**
     * Test of getGuestSessionToken method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    @Ignore("Not ready yet")
    public void testGetGuestSessionToken() throws MovieDbException {
        LOG.info("getGuestSessionToken");
        TokenSession result = tmdb.getGuestSessionToken();

        assertTrue("Failed to get guest session", result.getSuccess());
    }

    //@Test
    public void testGetMovieLists() throws MovieDbException {
        LOG.info("getMovieLists");
        TmdbResultsList<MovieList> result = tmdb.getMovieLists(ID_MOVIE_BLADE_RUNNER, LANGUAGE_ENGLISH, 0);
        assertNotNull("No results found", result);
        assertTrue("No results found", result.getResults().size() > 0);
    }

    /**
     * Test of getMovieChanges method,of class TheMovieDbApi
     *
     * TODO: Do not test this until it is fixed
     *
     * @throws MovieDbException
     */
    @Ignore("Do not test this until it is fixed")
    public void testGetMovieChanges() throws MovieDbException {
        LOG.info("getMovieChanges");

        String startDate = "";
        String endDate = null;

        // Get some popular movies
        TmdbResultsList<MovieDb> movieList = tmdb.getPopularMovieList(LANGUAGE_DEFAULT, 0);
        for (MovieDb movie : movieList.getResults()) {
            TmdbResultsMap<String, List<ChangedItem>> result = tmdb.getMovieChanges(movie.getId(), startDate, endDate);
            LOG.info("{} has {} changes.", movie.getTitle(), result.getResults().size());
            assertTrue("No changes found", result.getResults().size() > 0);
            break;
        }
    }

    //@Test
    public void testGetPersonLatest() throws MovieDbException {
        LOG.info("getPersonLatest");

        Person result = tmdb.getPersonLatest();

        assertNotNull("No results found", result);
        assertTrue("No results found", StringUtils.isNotBlank(result.getName()));
    }

    /**
     * Test of searchCollection method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testSearchCollection() throws MovieDbException {
        LOG.info("searchCollection");
        String query = "batman";
        int page = 0;
        TmdbResultsList<Collection> result = tmdb.searchCollection(query, LANGUAGE_DEFAULT, page);
        assertFalse("No collections found", result == null);
        assertTrue("No collections found", result.getResults().size() > 0);
    }

    /**
     * Test of searchList method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testSearchList() throws MovieDbException {
        LOG.info("searchList");
        String query = "watch";
        int page = 0;
        TmdbResultsList<MovieList> result = tmdb.searchList(query, LANGUAGE_DEFAULT, page);
        assertFalse("No lists found", result.getResults() == null);
        assertTrue("No lists found", result.getResults().size() > 0);
    }

    /**
     * Test of searchKeyword method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testSearchKeyword() throws MovieDbException {
        LOG.info("searchKeyword");
        String query = "action";
        int page = 0;
        TmdbResultsList<Keyword> result = tmdb.searchKeyword(query, page);
        assertFalse("No keywords found", result.getResults() == null);
        assertTrue("No keywords found", result.getResults().size() > 0);
    }

    /**
     * Test of postMovieRating method, of class TheMovieDbApi.
     *
     * TODO: Cannot be tested without a HTTP authorisation: http://help.themoviedb.org/kb/api/user-authentication
     *
     * @throws MovieDbException
     */
    @Ignore("Session required")
    public void testMovieRating() throws MovieDbException {
        LOG.info("postMovieRating");
        Integer movieID = 68724;
        Integer rating = new Random().nextInt(10) + 1;

        boolean wasPosted = tmdb.postMovieRating(SESSION_ID_APITESTS, movieID, rating);

        assertNotNull(wasPosted);
        assertTrue(wasPosted);

        // get all rated movies
        List<MovieDb> ratedMovies = tmdb.getRatedMovies(SESSION_ID_APITESTS, ACCOUNT_ID_APITESTS);
        assertTrue(ratedMovies.size() > 0);

        // make sure that we find the movie and it is rated correctly
        boolean foundMovie = false;
        for (MovieDb movie : ratedMovies) {
            if (movie.getId() == movieID) {
                assertEquals(movie.getUserRating(), (float) rating, 0);
                foundMovie = true;
            }
        }
        assertTrue(foundMovie);
    }

    @Ignore("Session required")
    public void testMovieLists() throws MovieDbException {
        Integer movieID = 68724;

        // use a random name to avoid that we clash we leftovers of incomplete test runs
        String name = "test list " + new Random().nextInt(100);

        // create the list
        String listId = tmdb.createList(SESSION_ID_APITESTS, name, "api testing only");

        // add a movie, and test that it is on the list now
        tmdb.addMovieToList(SESSION_ID_APITESTS, listId, movieID);
        MovieDbList list = tmdb.getList(listId);
        assertNotNull("Movie list returned was null", list);
        assertEquals("Unexpected number of items returned", 1, list.getItemCount());
        assertEquals((int) movieID, list.getItems().get(0).getId());

        // now remove the movie
        tmdb.removeMovieFromList(SESSION_ID_APITESTS, listId, movieID);
        assertEquals(tmdb.getList(listId).getItemCount(), 0);

        // delete the test list
        StatusCode statusCode = tmdb.deleteMovieList(SESSION_ID_APITESTS, listId);
        assertEquals(statusCode.getStatusCode(), 13);
    }

    /**
     * Test of getPersonChanges method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    @Ignore("Not ready yet")
    public void testGetPersonChanges() throws MovieDbException {
        LOG.info("getPersonChanges");
        String startDate = "";
        String endDate = "";
        tmdb.getPersonChanges(ID_PERSON_BRUCE_WILLIS, startDate, endDate);
    }

    /**
     * Test of getList method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetList() throws MovieDbException {
        LOG.info("getList");
        String listId = "509ec17b19c2950a0600050d";
        MovieDbList result = tmdb.getList(listId);
        assertFalse("List not found", result.getItems().isEmpty());
    }

    /**
     * Test of getKeyword method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetKeyword() throws MovieDbException {
        LOG.info("getKeyword");
        Keyword result = tmdb.getKeyword(ID_KEYWORD);
        assertEquals("fight", result.getName());
    }

    /**
     * Test of getMovieDbBasics method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetKeywordMovies() throws MovieDbException {
        LOG.info("getKeywordMovies");
        int page = 0;
        TmdbResultsList<MovieDbBasic> result = tmdb.getKeywordMovies(ID_KEYWORD, LANGUAGE_DEFAULT, page);
        assertFalse("No keyword movies found", result.getResults().isEmpty());
    }

    /**
     * Test of getReviews method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetReviews() throws MovieDbException {
        LOG.info("getReviews");
        int page = 0;
        TmdbResultsList<Reviews> result = tmdb.getReviews(ID_MOVIE_THE_AVENGERS, LANGUAGE_DEFAULT, page);

        assertFalse("No reviews found", result.getResults().isEmpty());
    }

    /**
     * Test of compareMovies method, of class TheMovieDbApi.
     *
     */
    @Ignore("Test not required")
    public void testCompareMovies_3args() {
    }

    /**
     * Test of compareMovies method, of class TheMovieDbApi.
     *
     */
    @Ignore("Test not required")
    public void testCompareMovies_4args() {
    }

    /**
     * Test of getPersonPopular method, of class TheMovieDbApi.
     */
    @Ignore("Test not required")
    public void testGetPersonPopular_0args() {
    }

    /**
     * Test of getPersonPopular method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetPersonPopular_int() throws MovieDbException {
        LOG.info("getPersonPopular");
        int page = 0;
        TmdbResultsList<Person> result = tmdb.getPersonPopular(page);
        assertFalse("No popular people", result.getResults().isEmpty());
    }

    /**
     * Test of getGenreMovies method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    @Ignore("Not required")
    public void testGetGenreMovies_3args() throws MovieDbException {
    }

    /**
     * Test of getGenreMovies method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    @Ignore("Not required")
    public void testGetGenreMovies_4args() throws MovieDbException {
    }

    /**
     * Test of getMovieChangesList method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetMovieChangesList() throws MovieDbException {
        LOG.info("getMovieChangesList");
        int page = 0;
        String startDate = "";
        String endDate = "";
        TmdbResultsList<ChangedMovie> result = tmdb.getMovieChangesList(page, startDate, endDate);
        assertFalse("No movie changes.", result.getResults().isEmpty());
    }

    /**
     * Test of getPersonChangesList method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    @Ignore("Not ready yet")
    public void testGetPersonChangesList() throws MovieDbException {
        LOG.info("getPersonChangesList");
        int page = 0;
        String startDate = "";
        String endDate = "";
        tmdb.getPersonChangesList(page, startDate, endDate);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getJobs method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetJobs() throws MovieDbException {
        LOG.info("getJobs");
        TmdbResultsList<JobDepartment> result = tmdb.getJobs();
        assertFalse("No jobs found", result.getResults().isEmpty());
    }

    /**
     * Test of getDiscover method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    @Ignore("Test not required")
    public void testGetDiscover_14args() throws MovieDbException {
    }

    /**
     * Test of getDiscover method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetDiscover_Discover() throws MovieDbException {
        LOG.info("getDiscover");
        Discover discover = new Discover();
        discover.year(2013).language(LANGUAGE_ENGLISH);

        TmdbResultsList<MovieDb> result = tmdb.getDiscover(discover);
        assertFalse("No movies discovered", result.getResults().isEmpty());
    }

    /**
     * Test of searchTv method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
//    @Test
    public void testSearchTv() throws MovieDbException {
        LOG.info("searchTv");

        TmdbResultsList<TVSeriesBasic> results = tmdb.searchTv("Big Bang Theory", 0, LANGUAGE_DEFAULT);
        assertFalse("No shows found, should be at least 1", results.getResults().isEmpty());
    }

    /**
     * Test of getTv method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
//    @Test
    public void testGetTv() throws MovieDbException {
        LOG.info("getTv");
        TVSeries result = tmdb.getTv(ID_BIG_BANG_THEORY, LANGUAGE_DEFAULT);
        assertEquals("Wrong title returned", "The Big Bang Theory", result.getName());
        assertTrue("No seasons returned", result.getNumberSeasons() >= 5);
        assertTrue("No episodes returned", result.getNumberEpisodes() > 100);
        assertFalse("No genres returned", result.getGenres().isEmpty());
        assertFalse("No created by", result.getCreatedBy().isEmpty());
    }

    //@Test
    public void testGetTvCredits() throws MovieDbException {
        LOG.info("getTvCredits");

        TmdbResultsList<Person> result = tmdb.getTvCredits(ID_BIG_BANG_THEORY, LANGUAGE_DEFAULT);

        assertNotNull("Null results", result.getResults());
        assertFalse("Empty results", result.getResults().isEmpty());
        assertTrue("No credits returned", result.getResults().size() > 0);
    }

    /**
     * Test of getTvExternalIds method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetTvExternalIds() throws MovieDbException {
        LOG.info("getTvExternalIds");
        ExternalIds result = tmdb.getTvExternalIds(ID_BIG_BANG_THEORY, LANGUAGE_DEFAULT);
        assertFalse("No ids found", result.getIds().isEmpty());
        assertTrue("TMDB Id not found", result.hasId("id"));
        assertEquals("Wrong id returned", Integer.toString(ID_BIG_BANG_THEORY), result.getId("id"));
        assertEquals("Wrong TVDB Id returned", "80379", result.getId("tvdb_id"));
    }

    /**
     * Test of getTvImages method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetTvImages() throws MovieDbException {
        LOG.info("getTvImages");
        TmdbResultsList<Artwork> result = tmdb.getTvImages(ID_BIG_BANG_THEORY, LANGUAGE_DEFAULT);
        assertFalse("No results found", result.getResults().isEmpty());
    }

    /**
     * Test of getTvSeason method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
//    @Test
    public void testGetTvSeason() throws MovieDbException {
        LOG.info("getTvSeason");

        TVSeason result = tmdb.getTvSeason(ID_BIG_BANG_THEORY, 1, LANGUAGE_DEFAULT);
        assertFalse("No episodes found for season", result.getEpisodes().isEmpty());
        assertEquals("Wrong air date", "2007-09-24", result.getAirDate());
        assertEquals("Wrong season name", "Season 1", result.getName());
    }

    /**
     * Test of getTvSeasonExternalIds method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
//    @Test
    public void testGetTvSeasonExternalIds() throws MovieDbException {
        LOG.info("getTvSeasonExternalIds");

        ExternalIds result = tmdb.getTvSeasonExternalIds(ID_BIG_BANG_THEORY, 1, LANGUAGE_DEFAULT);
        assertFalse("No ids found", result.getIds().isEmpty());
        assertTrue("TMDB Id not found", result.hasId("id"));
        assertEquals("Wrong season id returned", "3738", result.getId("id"));
        assertEquals("Wrong TVDB Id returned", "28047", result.getId("tvdb_id"));
    }

    /**
     * Test of getTvSeasonImages method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
//    @Test
    public void testGetTvSeasonImages() throws MovieDbException {
        LOG.info("getTvSeasonImages");
        TmdbResultsList<Artwork> result = tmdb.getTvSeasonImages(ID_BIG_BANG_THEORY, 1, LANGUAGE_DEFAULT);
        assertFalse("No results found", result.getResults().isEmpty());
    }

    /**
     * Test of getTvEpisode method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
//    @Test
    public void testGetTvEpisode() throws MovieDbException {
        LOG.info("getTvEpisode");
        TVEpisode result = tmdb.getTvEpisode(ID_BIG_BANG_THEORY, 1, 1, LANGUAGE_DEFAULT);
        assertEquals("Wrong ID", "64766", result.getId());
        assertEquals("Wrong date", "2007-09-24", result.getAirDate());
        assertTrue("No overview", StringUtils.isNotBlank(result.getOverview()));
    }

    /**
     * Test of getTvEpisodeCredits method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    @Test
    public void testGetTvEpisodeCredits() throws MovieDbException {
        LOG.info("getTvEpisodeCredits");
        TmdbResultsList<Person> result = tmdb.getTvEpisodeCredits(ID_BIG_BANG_THEORY, 1, 1, LANGUAGE_DEFAULT);
        LOG.info("{}",result );
    }

    /**
     * Test of getTvEpisodeExternalIds method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    @Ignore("Find an episode with ids to test this on")
    public void testGetTvEpisodeExternalIds() throws MovieDbException {
        LOG.info("getTvEpisodeExternalIds");
        ExternalIds result = tmdb.getTvEpisodeExternalIds(ID_BIG_BANG_THEORY, 1, 1, LANGUAGE_DEFAULT);
        LOG.info("{}", result);
        assertFalse("No ids found", result.getIds().isEmpty());
        assertTrue("TMDB Id not found", result.hasId("id"));
        assertEquals("Wrong season id returned", "3738", result.getId("id"));
        assertEquals("Wrong TVDB Id returned", "28047", result.getId("tvdb_id"));
    }

    /**
     * Test of getTvEpisodeImages method, of class TheMovieDbApi.
     *
     * @throws MovieDbException
     */
    //@Test
    public void testGetTvEpisodeImages() throws MovieDbException {
        LOG.info("getTvEpisodeImages");
        String result = tmdb.getTvEpisodeImages(ID_BIG_BANG_THEORY, 1, 1, LANGUAGE_DEFAULT);
    }

    /**
     * Test of convertToJson method, of class TheMovieDbApi.
     */
    @Ignore("No test needed")
    public void testConvertToJson() {
    }
}
