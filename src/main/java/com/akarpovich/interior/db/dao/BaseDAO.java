package com.akarpovich.interior.db.dao;

import com.akarpovich.interior.db.DatabaseConnection;
import com.akarpovich.interior.db.model.Identifiable;
import com.mysql.jdbc.Statement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class BaseDAO<T> implements DAO<T> {

    private final String tableName;

    protected BaseDAO(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Converts result set row to Java object
     */
    protected abstract T convertToItem(ResultSet resultSet) throws SQLException;

    protected abstract Map<String,Object> convertToMap(T object);

    @Override
    public T findById(Integer id) {
        PreparedStatement statement = null;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement(String.format("SELECT * FROM %s WHERE id = ?", tableName));
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet == null || !resultSet.first()) {
                return null;
            }
            return convertToItem(resultSet);
        } catch (SQLException e) {
            System.out.println("An error occurred when loading from database");
            e.printStackTrace();
        } finally {
            closeStatement(statement);
        }
        return null;
    }

    @Override
    public List<T> findAll() {
        PreparedStatement statement = null;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            statement = connection.prepareStatement(String.format("SELECT * FROM %s", tableName));

            ResultSet resultSet = statement.executeQuery();
            if (resultSet == null || !resultSet.first()) {
                return Collections.emptyList();
            }
            List<T> items = new ArrayList<T>();
            do {
                items.add(convertToItem(resultSet));
            } while (resultSet.next());
            return items;
        } catch (SQLException e) {
            System.out.println("An error occurred when loading from database");
            e.printStackTrace();
        } finally {
            closeStatement(statement);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean save(T object) {
        PreparedStatement statement = null;
        try {
            Map<String, Object> columnValuePairs = convertToMap(object);
            if (columnValuePairs == null || columnValuePairs.isEmpty()) {
                return false;
            }
            StringBuilder columns = new StringBuilder("(");
            StringBuilder valsHolders = new StringBuilder("(");
            List<Object> vals = new ArrayList<Object>(columnValuePairs.size());
            for (Map.Entry<String, Object> entry : columnValuePairs.entrySet()) {
                columns.append(entry.getKey()).append(',');
                valsHolders.append('?').append(',');
                vals.add(entry.getValue());
            }
            columns.deleteCharAt(columns.length() - 1).append(')');
            valsHolders.deleteCharAt(valsHolders.length() - 1).append(')');
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String sql = String.format("INSERT INTO %s %s VALUES %s", tableName, columns, valsHolders);
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < vals.size(); i++) {
                statement.setObject(i + 1, vals.get(i));
            }
            boolean added = statement.executeUpdate() > 0;
            if (added && object instanceof Identifiable) {
                ResultSet genKeys = statement.getGeneratedKeys();
                if (genKeys.next()) {
                    int id = genKeys.getInt(1);
                    ((Identifiable) object).setId(id);
                }
            }
            return added;
        } catch (SQLException e) {
            System.out.println("An error occurred when saving to database");
            e.printStackTrace();
        } finally {
            closeStatement(statement);
        }
        return false;
    }

    @Override
    public boolean update(T object) {
        PreparedStatement statement = null;
        try {
            Map<String, Object> columnValuePairs = convertToMap(object);
            if (columnValuePairs == null || columnValuePairs.isEmpty()) {
                return false;
            }
            StringBuilder columns = new StringBuilder("(");
            StringBuilder valsHolders = new StringBuilder("(");
            List<Object> vals = new ArrayList<Object>(columnValuePairs.size());
            for (Map.Entry<String, Object> entry : columnValuePairs.entrySet()) {
                columns.append(entry.getKey()).append(',');
                valsHolders.append('?').append(',');
                vals.add(entry.getValue());
            }
            columns.deleteCharAt(columns.length() - 1).append(')');
            valsHolders.deleteCharAt(valsHolders.length() - 1).append(')');
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String sql = String.format("UPDATED INTO %s %s VALUES %s", tableName, columns, valsHolders);
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < vals.size(); i++) {
                statement.setObject(i + 1, vals.get(i));
            }
            boolean updated = statement.executeUpdate() > 0;
            if (updated && object instanceof Identifiable) {
                ResultSet genKeys = statement.getGeneratedKeys();
                if (genKeys.next()) {
                    int id = genKeys.getInt(1);
                    ((Identifiable) object).setId(id);
                }
            }
            return updated;
        } catch (SQLException e) {
            System.out.println("An error occurred when saving to database");
            e.printStackTrace();
        } finally {
            closeStatement(statement);
        }
        return false;
    }

    private void closeStatement(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.out.println("An error occurred when closing statement");
                e.printStackTrace();
            }
        }
    }
}
