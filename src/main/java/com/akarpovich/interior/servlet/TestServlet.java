package com.akarpovich.interior.servlet;

import com.akarpovich.interior.db.dao.TestDao;
import com.akarpovich.interior.db.model.Test;
import com.akarpovich.interior.utils.ConversionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/test")
public class TestServlet extends HttpServlet {

    private TestDao testDao;

    @Override
    public void init() throws ServletException {
        super.init();
        testDao = new TestDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Test newOne = new Test();
        newOne.setValue("This is value for " + System.currentTimeMillis());
        testDao.save(newOne);
        resp.getOutputStream().write(ConversionUtils.toJson(testDao.findAll()).getBytes());
    }
}
