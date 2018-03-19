package com.akarpovich.interior.db.dao;

import com.akarpovich.interior.db.model.Test;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TestDao extends BaseDAO<Test> {

    public TestDao() {
        super("Test");
    }

    @Override
    protected Test convertToItem(ResultSet resultSet) throws SQLException {
        int fetchedId = resultSet.getInt(1);
        String fetchedVal = resultSet.getString(2);
        Test test = new Test();
        test.setId(fetchedId);
        test.setValue(fetchedVal);
        return test;
    }

    @Override
    protected Map<String, Object> convertToMap(Test object) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("value", object.getValue());
        return map;
    }
}
