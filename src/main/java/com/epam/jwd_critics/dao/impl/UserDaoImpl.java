package com.epam.jwd_critics.dao.impl;

import com.epam.jwd_critics.dao.AbstractBaseDao;
import com.epam.jwd_critics.dao.UserDao;
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
import java.util.*;
import java.util.stream.Collectors;

public class UserDaoImpl extends AbstractBaseDao<Integer, User> implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Language("SQL")
    private static final String SELECT_ALL_USERS = "SELECT * FROM jwd_critics.user";
    @Language("SQL")
    private static final String SELECT_USER_BY_ID = "SELECT * FROM jwd_critics.user U WHERE U.id = ?";
    @Language("SQL")
    private static final String DELETE_USER_BY_ID = "DELETE FROM jwd_critics.user U WHERE U.id = ?";
    @Language("SQL")
    private static final String DELETE_USER_BY_LOGIN = "DELETE FROM jwd_critics.user U WHERE U.login = ?";
    @Language("SQL")
    private static final String INSERT_USER = "INSERT INTO jwd_critics.user (first_name, last_name, email, login, password, rating, role_id, status_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    @Language("SQL")
    private static final String UPDATE_USER = "UPDATE jwd_critics.user U SET U.first_name = ?, U.last_name = ?, U.email = ?, U.login = ?, U.rating = ?, U.role_id = ?, U.status_id = ? WHERE U.id = ?";

    @Override
    public List<User> findAll() {
        List<User> list = new LinkedList<>();
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
    public Optional<User> get(Integer id) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(SELECT_USER_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                resultSet.next();
                return Optional.ofNullable(buildUser(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(DELETE_USER_BY_ID)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(String login) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(DELETE_USER_BY_LOGIN)) {
            ps.setString(1, login);
            ps.executeUpdate();
            return true;
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
                int generatedId = ps.getGeneratedKeys().getInt(1);
                user.setId(generatedId);
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
//        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER)) {
//            preparedStatement.setString(1, user.getLogin());
//            preparedStatement.setString(2, user.getEmail());
//            preparedStatement.setString(3, user.getFirstName());
//            preparedStatement.setString(4, user.getSecondName());
//            preparedStatement.setString(5, user.getPicture());
//            preparedStatement.setInt(6, user.getRole().ordinal());
//            preparedStatement.setInt(7, user.getState().ordinal());
//            preparedStatement.setInt(8, user.getRating());
//            preparedStatement.setInt(9, user.getId());
//            preparedStatement.executeUpdate();
//            return user;
//        } catch (SQLException e) {
//            logger.error(e);
//            throw new DaoException(e);
//        }
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        if (resultSet.wasNull()) {
            return null;
        }
        Map<String, String> columnNames = Arrays.stream(User.class.getDeclaredFields()).
                collect(Collectors.toMap(Field::getName, field -> field.getAnnotation(Column.class).columnName()));
        Field idField = null;
        try {
            idField = User.class.getSuperclass().getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            logger.error(e.getMessage(), e);
        }
        columnNames.put(idField.getName(), idField.getAnnotation(Column.class).columnName());
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
