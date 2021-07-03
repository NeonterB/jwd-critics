package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.AgeRestriction;
import com.epam.jwd_critics.entity.Column;
import com.epam.jwd_critics.entity.Country;
import com.epam.jwd_critics.entity.Genre;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.Position;
import com.epam.jwd_critics.exception.DaoException;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MovieDao extends AbstractMovieDao {
    private static final Logger logger = LoggerFactory.getLogger(MovieDao.class);

    @Language("SQL")
    private static final String SELECT_ALL_MOVIES_BETWEEN = "SELECT M.id, M.image_path, M.name, M.summary, M.runtime, AG.restriction, C.country, M.rating, M.review_count, M.release_date FROM jwd_critics.movie M inner join jwd_critics.age_restriction AG on M.age_restriction_id = AG.id inner join jwd_critics.country C on M.country_id = C.id order by M.name limit ?, ?";
    @Language("SQL")
    private static final String COUNT_MOVIES = "SELECT COUNT(*) FROM movie;";
    @Language("SQL")
    private static final String SELECT_GENRES_BY_MOVIE_ID = "SELECT G.genre from jwd_critics.movie_genre MG inner join jwd_critics.genre G on MG.genre_id = G.id where movie_id = ?";
    @Language("SQL")
    private static final String SELECT_MOVIES_BY_CELEBRITY_ID = "select M.id, M.image_path, M.name, M.summary, M.runtime, AG.restriction, C.country, M.rating, M.review_count, M.release_date, P.position from jwd_critics.movie_staff MS inner join jwd_critics.movie M on MS.movie_id = M.id inner join jwd_critics.age_restriction AG on M.age_restriction_id = AG.id inner join jwd_critics.country C on M.country_id = C.id inner join jwd_critics.position P on MS.position_id = P.id where MS.celebrity_id = ?";
    @Language("SQL")
    private static final String SELECT_MOVIE_BY_ID = "SELECT M.id, M.image_path, M.name, M.summary, M.runtime, AG.restriction, C.country, M.rating, M.review_count, M.release_date FROM jwd_critics.movie M inner join jwd_critics.age_restriction AG on M.age_restriction_id = AG.id inner join jwd_critics.country C on M.country_id = C.id WHERE M.id = ?";
    @Language("SQL")
    private static final String SELECT_MOVIES_BY_NAME_PART = "SELECT M.id, M.image_path, M.name, M.summary, M.runtime, AG.restriction, C.country, M.rating, M.review_count, M.release_date FROM jwd_critics.movie M inner join jwd_critics.age_restriction AG on M.age_restriction_id = AG.id inner join jwd_critics.country C on M.country_id = C.id WHERE UPPER(M.name) LIKE ?";
    @Language("SQL")
    private static final String DELETE_MOVIE_BY_ID = "DELETE FROM jwd_critics.movie WHERE id = ?";
    @Language("SQL")
    private static final String INSERT_MOVIE = "INSERT INTO jwd_critics.movie (image_path, name, summary, runtime, age_restriction_id, country_id, release_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
    @Language("SQL")
    private static final String UPDATE_MOVIE = "UPDATE jwd_critics.movie M SET M.image_path = ?, M.name = ?, M.summary = ?, M.runtime = ?, M.age_restriction_id = ?, M.country_id = ?, M.release_date = ?, M.image_path = ? WHERE M.id = ?";
    @Language("SQL")
    private static final String ADD_GENRE = "INSERT INTO jwd_critics.movie_genre (movie_id, genre_id) VALUES (?, ?)";
    @Language("SQL")
    private static final String DELETE_GENRE = "DELETE FROM jwd_critics.movie_genre where movie_id = ? and genre_id = ?";
    @Language("SQL")
    private static final String ADD_STAFF = "INSERT INTO jwd_critics.movie_staff (movie_id, celebrity_id, position_id) VALUES (?, ?, ?)";
    @Language("SQL")
    private static final String DELETE_STAFF = "DELETE FROM jwd_critics.movie_staff where movie_id = ? and celebrity_id = ? and position_id = ?";
    @Language("SQL")
    private static final String ID_EXISTS = "SELECT EXISTS(SELECT id FROM jwd_critics.movie WHERE id = ?)";

    @Override
    public List<Movie> getAllBetween(int begin, int end) throws DaoException {
        List<Movie> list = new ArrayList<>();
        try (PreparedStatement ps = getPreparedStatement(SELECT_ALL_MOVIES_BETWEEN)) {
            ps.setInt(1, begin);
            ps.setInt(2, end);
            ResultSet rs = ps.executeQuery();
            Movie movie = buildMovie(rs);
            while (movie != null) {
                list.add(movie);
                movie = buildMovie(rs);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    @Override
    public int getCount() throws DaoException {
        return getCount(COUNT_MOVIES);
    }

    @Override
    public Optional<Movie> getEntityById(Integer id) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(SELECT_MOVIE_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return Optional.ofNullable(buildMovie(rs));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Integer movieId) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(DELETE_MOVIE_BY_ID)) {
            ps.setInt(1, movieId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Movie create(Movie movie) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(INSERT_MOVIE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, movie.getImagePath());
            ps.setString(2, movie.getName());
            ps.setString(3, movie.getSummary());
            ps.setString(4, movie.getRuntime().toString());
            ps.setInt(5, movie.getAgeRestriction().getId());
            ps.setInt(6, movie.getCountry().getId());
            ps.setString(7, movie.getReleaseDate().toString());
            movie.setId(executeQueryAndGetGeneratesKeys(ps));
            return movie;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Movie movie) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(UPDATE_MOVIE)) {
            ps.setString(1, movie.getImagePath());
            ps.setString(2, movie.getName());
            ps.setString(3, movie.getSummary());
            ps.setString(4, movie.getRuntime().toString());
            ps.setInt(5, movie.getAgeRestriction().getId());
            ps.setInt(6, movie.getCountry().getId());
            ps.setString(7, movie.getReleaseDate().toString());
            ps.setString(8, movie.getImagePath());
            ps.setInt(9, movie.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean idExists(Integer movieID) throws DaoException {
        return existsQuery(ID_EXISTS, movieID);
    }

    @Override
    public Map<Movie, List<Position>> getJobsByCelebrityId(Integer celebrityId) throws DaoException {
        Map<Movie, List<Position>> crew = new HashMap<>();
        try (PreparedStatement ps = getPreparedStatement(SELECT_MOVIES_BY_CELEBRITY_ID)) {
            ps.setInt(1, celebrityId);
            try (ResultSet rs = ps.executeQuery()) {
                String positionColumnName = Position.class.getAnnotation(Column.class).name();
                Movie movie = buildMovie(rs);
                while (movie != null) {
                    if (!crew.containsKey(movie)) {
                        ArrayList<Position> positions = new ArrayList<>();
                        positions.add(Position.valueOf(rs.getString(positionColumnName).toUpperCase()));
                        crew.put(movie, positions);
                    } else {
                        crew.get(movie).add(Position.valueOf(rs.getString(positionColumnName).toUpperCase()));
                    }
                    movie = buildMovie(rs);
                }
            }
            return crew;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Genre> getMovieGenresById(Integer movieId) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(SELECT_GENRES_BY_MOVIE_ID)) {
            ps.setInt(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Genre> genres = new ArrayList<>();
                while (rs.next()) {
                    genres.add(Genre.valueOf(rs.getString(1).toUpperCase().replaceAll("[/s-]", "_")));
                }
                return genres;
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean addGenre(Integer movieId, Genre genre) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(ADD_GENRE)) {
            ps.setInt(1, movieId);
            ps.setInt(2, genre.getId());
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean removeGenre(Integer movieId, Genre genre) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(DELETE_GENRE)) {
            ps.setInt(1, movieId);
            ps.setInt(2, genre.getId());
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean assignCelebrityOnPosition(Integer movieId, Integer celebrityId, Position position) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(ADD_STAFF)) {
            ps.setInt(1, movieId);
            ps.setInt(2, celebrityId);
            ps.setInt(3, position.getId());
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean removeCelebrityFromPosition(Integer movieId, Integer celebrityId, Position position) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(DELETE_STAFF)) {
            ps.setInt(1, movieId);
            ps.setInt(2, celebrityId);
            ps.setInt(3, position.getId());
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Movie> getMoviesByNamePart(String namePart) throws DaoException {
        List<Movie> list = new LinkedList<>();
        namePart = namePart.toUpperCase();
        try (PreparedStatement ps = getPreparedStatement(SELECT_MOVIES_BY_NAME_PART)) {
            ps.setString(1, "%" + namePart + "%");
            try (ResultSet rs = ps.executeQuery()) {
                Movie movie = buildMovie(rs);
                while (movie != null) {
                    list.add(movie);
                    movie = buildMovie(rs);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    private Movie buildMovie(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }
        Map<String, String> columnNames = Arrays.stream(Movie.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .collect(Collectors.toMap(Field::getName, field -> field.getAnnotation(Column.class).name()));
        Field idField;
        try {
            idField = Movie.class.getSuperclass().getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
        columnNames.put(idField.getName(), idField.getAnnotation(Column.class).name());

        return Movie.newBuilder()
                .setId(rs.getInt(columnNames.get("id")))
                .setName(rs.getString(columnNames.get("name")))
                .setSummary(rs.getString(columnNames.get("summary")))
                .setRuntime(Duration.parse(rs.getString(columnNames.get("runtime"))))
                .setCountry(Country.valueOf(rs.getString(columnNames.get("country")).toUpperCase()))
                .setRating(rs.getInt(columnNames.get("rating")))
                .setReviewCount(rs.getInt(columnNames.get("reviewCount")))
                .setReleaseDate(LocalDate.parse(rs.getString(columnNames.get("releaseDate"))))
                .setAgeRestriction(AgeRestriction.valueOf(rs.getString(columnNames.get("ageRestriction")).toUpperCase()))
                .setImagePath(rs.getString(columnNames.get("imagePath")))
                .build();
    }
}
