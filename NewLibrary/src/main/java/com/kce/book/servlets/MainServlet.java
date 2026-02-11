package com.kce.book.servlets;

import java.io.IOException;

import com.kce.book.bean.AuthorBean;
import com.kce.book.bean.BookBean;
import com.kce.book.dao.AuthorDAO;
import com.kce.book.service.Administrator;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String operation = request.getParameter("operation");

        if ("AddBook".equals(operation)) {

            String result = addBook(request);

            if ("SUCCESS".equals(result)) {
                response.sendRedirect("Menu.html");
            } else if ("INVALID".equals(result)) {
                response.sendRedirect("Invalid.html");
            } else {
                response.sendRedirect("Failure.html");
            }

        } else if ("Search".equals(operation)) {

            String isbn = request.getParameter("isbn");
            BookBean bookBean = viewBook(isbn);

            if (bookBean == null) {
                response.sendRedirect("Invalid.html");
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("book", bookBean);
                RequestDispatcher rd = request.getRequestDispatcher("ViewServlet");
                rd.forward(request, response);
            }
        }
    }
    public String addBook(HttpServletRequest request) {

        try {
            String isbn = request.getParameter("isbn");
            String bookName = request.getParameter("bookName");
            String bookType = request.getParameter("bookType");
            String authorName = request.getParameter("authorName");
            String cost = request.getParameter("cost");
            if (isbn == null || bookName == null || bookType == null ||
                authorName == null || cost == null ||
                isbn.trim().isEmpty() || bookName.trim().isEmpty() ||
                authorName.trim().isEmpty()) {

                return "INVALID";
            }
            AuthorDAO authorDAO = new AuthorDAO();
            AuthorBean author = authorDAO.getAuthor(authorName);

            if (author == null) {
                return "INVALID"; 
            }
            BookBean bookBean = new BookBean();
            bookBean.setIsbn(isbn);
            bookBean.setBookName(bookName);
            bookBean.setBookType(bookType.charAt(0));
            bookBean.setCost(Float.parseFloat(cost));
            bookBean.setAuthor(author);   
            return new Administrator().addBook(bookBean);

        } catch (Exception e) {
            e.printStackTrace();
            return "FAILURE";
        }
    }
    public BookBean viewBook(String isbn) {
        return new Administrator().viewBook(isbn);
    }
}