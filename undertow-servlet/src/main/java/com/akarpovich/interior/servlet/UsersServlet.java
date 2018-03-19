package com.akarpovich.interior.servlet;

import com.akarpovich.interior.db.dao.UsersDao;
import com.akarpovich.interior.db.model.Users;
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

@WebServlet("/users")
public class UsersServlet extends HttpServlet {

    private UsersDao usersDao;

    @Override
    public void init() throws ServletException {
        super.init();
        usersDao = new UsersDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Users result = usersDao.findById(1);
        resp.setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        String json = ConversionUtils.toJson(usersDao.findAll());
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

        Users user = ConversionUtils.fromJson(json, Users.class);

        boolean added = usersDao.save(user);
        if (!added) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else{
            resp.getOutputStream().write(ConversionUtils.toUTF8Json(user));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int read = -1;
        byte[] buff = new byte[1024];
        ServletInputStream stream = req.getInputStream();
        ByteArrayOutputStream builder = new ByteArrayOutputStream();
        while ((read = stream.read(buff)) != -1) {
            builder.write(buff, 0, read);
        }
        String json = builder.toString("UTF-8");

        Users user = ConversionUtils.fromJson(json, Users.class);

        boolean updated = usersDao.update(user);
        if (!updated) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else{
            resp.getOutputStream().write(ConversionUtils.toUTF8Json(user));
        }
    }
}
