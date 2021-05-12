package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.entity.Column;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CelebrityDao extends AbstractCelebrityDao {
    private static final Logger logger = LoggerFactory.getLogger(CelebrityDao.class);

    @Language("SQL")
    private static final String SELECT_ALL_CELEBRITIES = "SELECT * FROM jwd_critics.celebrity";
    @Language("SQL")
    private static final String SELECT_CELEBRITIES_BY_MOVIE_ID = "select C.*, MS.position_id from jwd_critics.celebrity C inner join jwd_critics.movie_staff MS on C.id = MS.celebrity_id where movie_id = ?";
    @Language("SQL")
    private static final String SELECT_CELEBRITY_BY_ID = "SELECT * FROM jwd_critics.celebrity WHERE id = ?";
    @Language("SQL")
    private static final String DELETE_CELEBRITY_BY_ID = "DELETE FROM jwd_critics.celebrity WHERE id = ?";
    @Language("SQL")
    private static final String INSERT_CELEBRITY = "INSERT INTO jwd_critics.celebrity (first_name, last_name) VALUES (?, ?)";
    @Language("SQL")
    private static final String UPDATE_CELEBRITY = "UPDATE jwd_critics.celebrity U SET U.first_name = ?, U.last_name = ? WHERE U.id = ?";


    private static class CelebrityDaoSingleton {
        private static final CelebrityDao INSTANCE = new CelebrityDao();
    }

    public static CelebrityDao getInstance() {
        return CelebrityDaoSingleton.INSTANCE;
    }

    @Override
    public List<Celebrity> findAll() throws DaoException {
        List<Celebrity> list = new ArrayList<>();
        try (PreparedStatement ps = getPreparedStatement(SELECT_ALL_CELEBRITIES)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(buildCelebrity(rs));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Map<Celebrity, List<Position>> findCrewByMovieId(Integer movieId) throws DaoException {
        Map<Celebrity, List<Position>> crew = new HashMap<>();
        try (PreparedStatement preparedStatement = getPreparedStatement(SELECT_CELEBRITIES_BY_MOVIE_ID)) {
            preparedStatement.setInt(1, movieId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Celebrity celebrity = buildCelebrity(resultSet);
                    if (!crew.containsKey(celebrity)) {
                        ArrayList<Position> positions = new ArrayList<>();
                        positions.add(Position.resolvePositionById(resultSet.getInt("position_id")));
                        crew.put(celebrity, positions);
                    } else {
                        crew.get(celebrity).add(Position.resolvePositionById(resultSet.getInt(4)));
                    }
                }
            }
            return crew;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Celebrity> findEntityById(Integer id) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(SELECT_CELEBRITY_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.ofNullable(buildCelebrity(resultSet));
                }
                else return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void deleteEntityById(Integer celebrityId) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(DELETE_CELEBRITY_BY_ID)) {
            ps.setInt(1, celebrityId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Celebrity create(Celebrity celebrity) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(INSERT_CELEBRITY, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, celebrity.getFirstName());
            ps.setString(2, celebrity.getLastName());
            celebrity.setId(executeQueryAndGetGeneratesKeys(ps));
            return celebrity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Celebrity celebrity) throws DaoException {
        try (PreparedStatement preparedStatement = getPreparedStatement(UPDATE_CELEBRITY)) {
            preparedStatement.setString(1, celebrity.getFirstName());
            preparedStatement.setString(2, celebrity.getLastName());
            preparedStatement.setInt(3, celebrity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }

    private Celebrity buildCelebrity(ResultSet resultSet) throws SQLException {
        if (resultSet.wasNull()) {
            return null;
        }
        Map<String, String> columnNames = Arrays.stream(Celebrity.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .collect(Collectors.toMap(Field::getName, field -> field.getAnnotation(Column.class).name()));
        Field idField = null;
        try {
            idField = Celebrity.class.getSuperclass().getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            logger.error(e.getMessage(), e);
        }
        columnNames.put(idField.getName(), idField.getAnnotation(Column.class).name());
        return Celebrity.newBuilder()
                .setId(resultSet.getInt(columnNames.get("id")))
                .setFirstName(resultSet.getString(columnNames.get("firstName")))
                .setLastName(resultSet.getString(columnNames.get("lastName")))
                .build();
    }
}
