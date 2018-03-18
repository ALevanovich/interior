package com.akarpovich.interior.db.dao;

import com.akarpovich.interior.db.model.Users;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class UsersDao extends BaseDAO<Users> {

    private Connection connection;

    public UsersDao() {
        super("Users");
    }

    @Override
    protected Users convertToItem(ResultSet resultSet) throws SQLException {
        int fetchedId = resultSet.getInt(1);
        String fetchedFirstName = resultSet.getString(2);
        String fetchedLastName = resultSet.getString(3);
        String fetchedEmail = resultSet.getString(4);
        String fetchedPassword = resultSet.getString(5);
        String fetchedRole = resultSet.getString(6);
        Users user = new Users();
        user.setId(fetchedId);
        user.setFirstName(fetchedFirstName);
        user.setLastName(fetchedLastName);
        user.setEmail(fetchedEmail);
        user.setPassword(fetchedPassword);
        user.setRole(fetchedRole);

        return user;
    }

    @Override
    protected Map<String, Object> convertToMap(Users object) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("firstName", object.getFirstName());
        map.put("lastName", object.getLastName());
        map.put("email", object.getEmail());
        map.put("password", object.getPassword());
        map.put("role", object.getRole());
        return map;
    }
}
