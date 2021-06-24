package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.Column;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.exception.DaoException;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MovieReviewDao extends AbstractMovieReviewDao {
    private static final Logger logger = LoggerFactory.getLogger(MovieReviewDao.class);

    @Language("SQL")
    private static final String SELECT_ALL_REVIEWS_BETWEEN = "SELECT * FROM jwd_critics.review limit ?, ?";
    @Language("SQL")
    private static final String COUNT_REVIEWS = "SELECT COUNT(*) FROM jwd_critics.review";
    @Language("SQL")
    private static final String SELECT_REVIEW_BY_ID = "SELECT * FROM jwd_critics.review WHERE id = ?";
    @Language("SQL")
    private static final String SELECT_REVIEW_BY_USER_ID_AND_MOVIE_ID = "SELECT * FROM jwd_critics.review WHERE user_id = ? and movie_id = ?";
    @Language("SQL")
    private static final String DELETE_REVIEW_BY_ID = "DELETE FROM jwd_critics.review WHERE id = ?";
    @Language("SQL")
    private static final String INSERT_REVIEW = "INSERT INTO jwd_critics.review (movie_id, user_id, text, score) VALUES (?, ?, ?, ?)";
    @Language("SQL")
    private static final String UPDATE_REVIEW = "UPDATE jwd_critics.review SET movie_id = ?, user_id = ?, text = ?, score = ? WHERE id = ?";
    @Language("SQL")
    private static final String SELECT_REVIEWS_BY_MOVIE_ID = "SELECT * FROM jwd_critics.review WHERE movie_id = ? limit ?, ?";
    @Language("SQL")
    private static final String COUNT_REVIEWS_BY_MOVIE_ID = "SELECT COUNT(*) FROM jwd_critics.review where movie_id = ?";
    @Language("SQL")
    private static final String SELECT_REVIEWS_BY_USER_ID = "SELECT * FROM jwd_critics.review WHERE user_id = ? limit ?, ?";
    @Language("SQL")
    private static final String COUNT_REVIEWS_BY_USER_ID = "SELECT COUNT(*) FROM jwd_critics.review where user_id = ?";
    @Language("SQL")
    private static final String ID_EXISTS = "SELECT EXISTS(SELECT id FROM jwd_critics.review WHERE id = ?)";
    @Language("SQL")
    private static final String REVIEW_EXISTS = "SELECT EXISTS(SELECT id FROM jwd_critics.review WHERE user_id = ? and movie_id = ?)";

    public static MovieReviewDao getInstance() {
        return MovieReviewDaoSingleton.INSTANCE;
    }

    @Override
    public List<MovieReview> getAllBetween(int begin, int end) throws DaoException {
        List<MovieReview> list = new ArrayList<>();
        try (PreparedStatement ps = getPreparedStatement(SELECT_ALL_REVIEWS_BETWEEN)) {
            ps.setInt(1, begin);
            ps.setInt(2, end);
            ResultSet rs = ps.executeQuery();
            MovieReview review = buildMovieReview(rs);
            while (review != null) {
                list.add(review);
                review = buildMovieReview(rs);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    @Override
    public int getCount() throws DaoException {
        return getCount(COUNT_REVIEWS);
    }

    @Override
    public Optional<MovieReview> getEntityById(Integer movieReviewId) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(SELECT_REVIEW_BY_ID)) {
            ps.setInt(1, movieReviewId);
            try (ResultSet rs = ps.executeQuery()) {
                return Optional.ofNullable(buildMovieReview(rs));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Integer movieReviewId) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(DELETE_REVIEW_BY_ID)) {
            ps.setInt(1, movieReviewId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public MovieReview create(MovieReview movieReview) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(INSERT_REVIEW, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, movieReview.getMovieId());
            ps.setInt(2, movieReview.getUserId());
            ps.setString(3, movieReview.getText());
            ps.setInt(4, movieReview.getScore());
            movieReview.setId(executeQueryAndGetGeneratesKeys(ps));
            return movieReview;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(MovieReview movieReview) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(UPDATE_REVIEW)) {
            ps.setInt(1, movieReview.getMovieId());
            ps.setInt(2, movieReview.getUserId());
            ps.setString(3, movieReview.getText());
            ps.setInt(4, movieReview.getScore());
            ps.setInt(5, movieReview.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean idExists(Integer reviewId) throws DaoException {
        return idExists(reviewId, ID_EXISTS);
    }

    @Override
    public List<MovieReview> getMovieReviewsByMovieId(Integer movieId, Integer begin, Integer end) throws DaoException {
        try {
            return getMovieReviewList(movieId, SELECT_REVIEWS_BY_MOVIE_ID, begin, end);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<MovieReview> getMovieReviewsByUserId(Integer userId, Integer begin, Integer end) throws DaoException {
        try {
            return getMovieReviewList(userId, SELECT_REVIEWS_BY_USER_ID, begin, end);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int getCountByMovieId(Integer movieId) throws DaoException {
        return getCount(COUNT_REVIEWS_BY_MOVIE_ID, movieId);
    }

    @Override
    public int getCountByUserId(Integer userId) throws DaoException {
        return getCount(COUNT_REVIEWS_BY_USER_ID, userId);
    }

    @Override
    public Optional<MovieReview> getEntity(Integer userId, Integer movieId) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(SELECT_REVIEW_BY_USER_ID_AND_MOVIE_ID)) {
            ps.setInt(1, userId);
            ps.setInt(2, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                return Optional.ofNullable(buildMovieReview(rs));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean reviewExists(Integer userId, Integer movieId) throws DaoException {
        boolean result = false;
        try (PreparedStatement ps = getPreparedStatement(REVIEW_EXISTS)) {
            ps.setInt(1, userId);
            ps.setInt(2, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt(1) != 0;
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    private Integer getCount(String query, Integer id) throws DaoException {
        int count = 0;
        try (PreparedStatement ps = getPreparedStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return count;
    }

    private List<MovieReview> getMovieReviewList(Integer id, String selectMovieReviewsQuery, Integer begin, Integer end) throws SQLException {
        List<MovieReview> list = new ArrayList<>();
        try (PreparedStatement ps = getPreparedStatement(selectMovieReviewsQuery)) {
            ps.setInt(1, id);
            ps.setInt(2, begin);
            ps.setInt(3, end);
            try (ResultSet rs = ps.executeQuery()) {
                MovieReview review = buildMovieReview(rs);
                while (review != null) {
                    list.add(review);
                    review = buildMovieReview(rs);
                }
            }
            return list;
        }
    }


    private MovieReview buildMovieReview(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }
        Map<String, String> columnNames = Arrays.stream(MovieReview.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .collect(Collectors.toMap(Field::getName, field -> field.getAnnotation(Column.class).name()));
        Field idField = null;
        try {
            idField = Movie.class.getSuperclass().getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            logger.error(e.getMessage(), e);
        }
        assert idField != null;
        columnNames.put(idField.getName(), idField.getAnnotation(Column.class).name());

        return new MovieReview(
                rs.getInt(columnNames.get("id")),
                rs.getString(columnNames.get("text")),
                rs.getInt(columnNames.get("score")),
                rs.getInt(columnNames.get("userId")),
                rs.getInt(columnNames.get("movieId"))
        );
    }

    private static class MovieReviewDaoSingleton {

        private static final MovieReviewDao INSTANCE = new MovieReviewDao();
    }
}
