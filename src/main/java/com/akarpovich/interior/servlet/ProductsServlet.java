package com.akarpovich.interior.servlet;

import com.akarpovich.interior.db.dao.ProductsDao;
import com.akarpovich.interior.db.model.Products;
import com.akarpovich.interior.utils.ConversionUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@WebServlet("/products")
public class ProductsServlet extends HttpServlet {

    private ProductsDao productsDao;

    @Override
    public void init() throws ServletException {
        super.init();
        productsDao = new ProductsDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Products result = productsDao.findById(1);
        resp.setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        String json = ConversionUtils.toJson(productsDao.findAll());
        resp.getOutputStream().write(ConversionUtils.toUTF8Bytes(json));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int read = -1;
        byte[] buff = new byte[1024];
        ServletInputStream stream = req.getInputStream();
        ByteArrayOutputStream builder = new ByteArrayOutputStream();
        while ((read = stream.read(buff)) != -1) {
            builder.write(buff, 0, read);
        }
        String json = builder.toString("UTF-8");

        Products product = ConversionUtils.fromJson(json, Products.class);
        boolean added = productsDao.save(product);
        if (!added) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
