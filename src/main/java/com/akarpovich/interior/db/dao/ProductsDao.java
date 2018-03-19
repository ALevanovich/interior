package com.akarpovich.interior.db.dao;

import com.akarpovich.interior.db.model.Products;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class ProductsDao extends BaseDAO<Products> {

    private Connection connection;

    public ProductsDao() {
        super("Products");
    }

    @Override
    protected Products convertToItem(ResultSet resultSet) throws SQLException {
        int fetchedId = resultSet.getInt(1);
        String fetchedName = resultSet.getString(2);
        String fetchedDescription = resultSet.getString(3);
        String fetchedImg = resultSet.getString(4);
        String fetchedCurrentPrice = resultSet.getString(5);
        String fetchedOldPrice = resultSet.getString(6);
        String fetchedSale = resultSet.getString(7);
        String fetchedTag = resultSet.getString(8);
        Products product = new Products();
        product.setId(fetchedId);
        product.setName(fetchedName);
        product.setDescription(fetchedDescription);
        product.setImg(fetchedImg);
        product.setCurrentPrice(fetchedCurrentPrice);
        product.setOldPrice(fetchedOldPrice);
        product.setSale(fetchedSale);
        product.setTag(fetchedTag);

        return product;
    }

    @Override
    protected Map<String, Object> convertToMap(Products object) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", object.getName());
        map.put("description", object.getDescription());
        map.put("img", object.getImg());
        map.put("currentPrice", object.getCurrentPrice());
        map.put("oldPrice", object.getOldPrice());
        map.put("sale", object.getSale());
        map.put("tag", object.getTag());
        return map;
    }
}
