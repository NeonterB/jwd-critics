package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.Column;
import com.epam.jwd_critics.entity.User;
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

public class UserDao extends AbstractUserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Language("SQL")
    private static final String SELECT_ALL_USERS = "SELECT * FROM jwd_critics.user";
    @Language("SQL")
    private static final String SELECT_USER_BY_ID = "SELECT * FROM jwd_critics.user U WHERE U.id = ?";
    @Language("SQL")
    private static final String DELETE_USER_BY_ID = "DELETE FROM jwd_critics.user U WHERE U.id = ?";
    @Language("SQL")
    private static final String INSERT_USER = "INSERT INTO jwd_critics.user (first_name, last_name, email, login, password, rating, role_id, status_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    @Language("SQL")
    private static final String UPDATE_USER = "UPDATE jwd_critics.user U SET U.first_name = ?, U.last_name = ?, U.email = ?, U.login = ?, U.rating = ?, U.role_id = ?, U.status_id = ? WHERE U.id = ?";

    private static class UserDaoSingleton {
        private static final UserDao INSTANCE = new UserDao();
    }

    public static UserDao getInstance() {
        return UserDaoSingleton.INSTANCE;
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        try (PreparedStatement ps = getPreparedStatement(SELECT_ALL_USERS)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(buildUser(rs));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Optional<User> findEntityById(Integer id) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(SELECT_USER_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.ofNullable(buildUser(resultSet));
                }
                else return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void deleteEntityById(Integer id) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(DELETE_USER_BY_ID)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public User create(User user) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getLogin());
            ps.setString(5, user.getPassword());
            ps.setInt(6, user.getRating());
            ps.setInt(7, user.getRole().getId());
            ps.setInt(8, user.getStatus().getId());
            int updatedRowCount = ps.executeUpdate();
            if (updatedRowCount == 1) {
                ResultSet resultSet = ps.getGeneratedKeys();
                if (resultSet.next()) {
                    int generatedId = resultSet.getInt(1);
                    user.setId(generatedId);
                }
            } else {
                throw new DaoException("Failed to insert " + user);
            }
            return user;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }


    @Override
    public void update(User user) throws DaoException {
        try (PreparedStatement preparedStatement = getPreparedStatement(UPDATE_USER)) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getLogin());
            preparedStatement.setInt(5, user.getRating());
            preparedStatement.setInt(6, user.getRole().getId());
            preparedStatement.setInt(7, user.getStatus().getId());
            preparedStatement.setInt(8, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }
//    @Override
//    public int countUsers() throws DaoException {
//        int usersCount = 0;
//        try (PreparedStatement ps = getPreparedStatement(COUNT_USERS)) {
//            try (ResultSet resultSet = ps.executeQuery()) {
//                if (resultSet.next()) {
//                    usersCount = resultSet.getInt(1);
//                }
//            }
//        } catch (SQLException e) {
//            logger.error(e.getMessage(), e);
//            throw new DaoException(e);
//        }
//        return usersCount;
//    }

//    @Override
//    public Status detectStatusById(int id) throws DaoException {
//        Status userStatus = null;
//        try (PreparedStatement preparedStatement = getPreparedStatement(SELECT_USER_STATUS_BY_ID)) {
//            preparedStatement.setInt(1, id);
//            try(ResultSet resultSet = preparedStatement.executeQuery()) {
//                resultSet.next();
//                userStatus = Status.valueOf(resultSet.getString(
//                        User.class.getDeclaredField("status").getAnnotation(Column.class).columnName()
//                ));
//            }
//        } catch (SQLException e) {
//            logger.error(e.getMessage(), e);
//            throw new DaoException(e);
//        } catch (NoSuchFieldException e){
//            logger.error(e.getMessage(), e);
//        }
//        return userStatus;
//    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        if (resultSet.wasNull()) {
            return null;
        }
        Map<String, String> columnNames = Arrays.stream(User.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .collect(Collectors.toMap(Field::getName, field -> field.getAnnotation(Column.class).name()));
        Field idField = null;
        try {
            idField = User.class.getSuperclass().getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            logger.error(e.getMessage(), e);
        }
        columnNames.put(idField.getName(), idField.getAnnotation(Column.class).name());
        return User.newBuilder().setId(resultSet.getInt(columnNames.get("id")))
                .setFirstName(resultSet.getString(columnNames.get("firstName")))
                .setLastName(resultSet.getString(columnNames.get("lastName")))
                .setLogin(resultSet.getString(columnNames.get("login")))
                .setPassword(resultSet.getString(columnNames.get("password")))
                .setEmail(resultSet.getString(columnNames.get("email")))
                .setRating(resultSet.getInt(columnNames.get("rating")))
                .setStatus(resultSet.getInt(columnNames.get("status")))
                .setRole(resultSet.getInt(columnNames.get("role")))
                .build();
    }
}
