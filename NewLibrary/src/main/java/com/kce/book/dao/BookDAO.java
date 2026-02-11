package com.kce.book.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;


import com.kce.book.bean.BookBean;
import com.kce.book.util.DBUtil;

public class BookDAO {
     public int createBook(BookBean bookBean) {
    	 Connection connection=DBUtil.getDBConnection();
    	 String query="insert into Book_TBL VALUES(?,?,?,?,?)";
    	 try {
    	 PreparedStatement ps = connection.prepareStatement(query);
         ps.setString(1, bookBean.getIsbn());
         ps.setString(2, bookBean.getBookName());
         ps.setString(3, String.valueOf(bookBean.getBookType()));
         ps.setInt(4, bookBean.getAuthor().getAuthorCode());
         ps.setFloat(5, bookBean.getCost());
         int rows = ps.executeUpdate();
         if(rows > 0) {
         	return 1;}
         else {
         	return 0;

	   } 

     } catch (Exception e) {
         e.printStackTrace();
     }
	 
	 return 0;
}
     
     public BookBean fetchBook(String isbn) {
    	 Connection connection=DBUtil.getDBConnection();
    	 String query="SELECT * FROM Book_Tbl WHERE ISBN=?";
    	 try {
        	 PreparedStatement ps = connection.prepareStatement(query);
	         ps.setString(1, isbn);
	         ResultSet rs=ps.executeQuery();
    		 if(rs.next()) {
    			 BookBean bookBean=new BookBean();
    		
    			 bookBean.setIsbn(rs.getString(1));
    			 bookBean.setBookName(rs.getString(2));
    			 bookBean.setBookType(rs.getString(3).charAt(0));
    			 bookBean.setAuthor(new AuthorDAO().getAuthor(rs.getInt(4)));
    			 bookBean.setCost(rs.getFloat(5));
    			 return bookBean;
    		 }
			 
		} catch (Exception e) {
			 e.printStackTrace();	
		}
    	 return null;
     }
}