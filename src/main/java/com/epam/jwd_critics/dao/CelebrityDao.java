package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.entity.Column;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CelebrityDao extends AbstractCelebrityDao {
    private static final Logger logger = LoggerFactory.getLogger(CelebrityDao.class);

    @Language("SQL")
    private static final String SELECT_ALL_CELEBRITIES_BETWEEN = "SELECT * FROM jwd_critics.celebrity order by celebrity.last_name, celebrity.first_name limit ?, ?";
    @Language("SQL")
    private static final String COUNT_CELEBRITIES = "SELECT COUNT(*) FROM celebrity";
    @Language("SQL")
    private static final String SELECT_CELEBRITIES_BY_MOVIE_ID = "select C.*, P.position from jwd_critics.celebrity C inner join jwd_critics.movie_staff MS on C.id = MS.celebrity_id inner join jwd_critics.position P on MS.position_id = P.id where movie_id = ? order by P.id";
    @Language("SQL")
    private static final String SELECT_CELEBRITY_BY_ID = "SELECT * FROM jwd_critics.celebrity WHERE id = ?";
    @Language("SQL")
    private static final String DELETE_CELEBRITY_BY_ID = "DELETE FROM jwd_critics.celebrity WHERE id = ?";
    @Language("SQL")
    private static final String INSERT_CELEBRITY = "INSERT INTO jwd_critics.celebrity (first_name, last_name, image_path) VALUES (?, ?, ?)";
    @Language("SQL")
    private static final String UPDATE_CELEBRITY = "UPDATE jwd_critics.celebrity U SET U.first_name = ?, U.last_name = ?, U.image_path = ? WHERE U.id = ?";
    @Language("SQL")
    private static final String ID_EXISTS = "SELECT EXISTS(SELECT id FROM jwd_critics.celebrity WHERE id = ?)";

    public static CelebrityDao getInstance() {
        return CelebrityDaoSingleton.INSTANCE;
    }

    @Override
    public List<Celebrity> getAllBetween(int begin, int end) throws DaoException {
        List<Celebrity> list = new ArrayList<>();
        try (PreparedStatement ps = getPreparedStatement(SELECT_ALL_CELEBRITIES_BETWEEN)) {
            ps.setInt(1, begin);
            ps.setInt(2, end);
            ResultSet rs = ps.executeQuery();
            Celebrity celebrity = buildCelebrity(rs);
            while (celebrity != null) {
                list.add(celebrity);
                celebrity = buildCelebrity(rs);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    @Override
    public int getCount() throws DaoException {
        return getCount(COUNT_CELEBRITIES);
    }

    @Override
    public Optional<Celebrity> getEntityById(Integer celebrityId) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(SELECT_CELEBRITY_BY_ID)) {
            ps.setInt(1, celebrityId);
            try (ResultSet rs = ps.executeQuery()) {
                return Optional.ofNullable(buildCelebrity(rs));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Integer celebrityId) throws DaoException {
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
            ps.setString(3, celebrity.getImagePath());
            celebrity.setId(executeQueryAndGetGeneratesKeys(ps));
            return celebrity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Celebrity celebrity) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(UPDATE_CELEBRITY)) {
            ps.setString(1, celebrity.getFirstName());
            ps.setString(2, celebrity.getLastName());
            ps.setString(3, celebrity.getImagePath());
            ps.setInt(4, celebrity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean idExists(Integer celebrityId) throws DaoException {
        return idExists(celebrityId, ID_EXISTS);
    }

    @Override
    public Map<Position, List<Celebrity>> getStaffByMovieId(Integer movieId) throws DaoException {
        Map<Position, List<Celebrity>> crew = new LinkedHashMap<>();
        try (PreparedStatement ps = getPreparedStatement(SELECT_CELEBRITIES_BY_MOVIE_ID)) {
            ps.setInt(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                String positionColumnName = Position.class.getAnnotation(Column.class).name();
                Celebrity celebrity = buildCelebrity(rs);
                while (celebrity != null) {
                    Position position = Position.valueOf(rs.getString(positionColumnName).toUpperCase());
                    if (!crew.containsKey(position)) {
                        ArrayList<Celebrity> celebrities = new ArrayList<>();
                        celebrities.add(celebrity);
                        crew.put(position, celebrities);
                    } else {
                        crew.get(position).add(celebrity);
                    }
                    celebrity = buildCelebrity(rs);
                }
            }
            return crew;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Celebrity buildCelebrity(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }
        Map<String, String> columnNames = Arrays.stream(Celebrity.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .collect(Collectors.toMap(Field::getName, field -> field.getAnnotation(Column.class).name()));
        Field idField;
        try {
            idField = Celebrity.class.getSuperclass().getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
        columnNames.put(idField.getName(), idField.getAnnotation(Column.class).name());
        return Celebrity.newBuilder()
                .setId(rs.getInt(columnNames.get("id")))
                .setFirstName(rs.getString(columnNames.get("firstName")))
                .setLastName(rs.getString(columnNames.get("lastName")))
                .setImagePath(rs.getString(columnNames.get("imagePath")))
                .build();
    }

    private static class CelebrityDaoSingleton {
        private static final CelebrityDao INSTANCE = new CelebrityDao();
    }
}
