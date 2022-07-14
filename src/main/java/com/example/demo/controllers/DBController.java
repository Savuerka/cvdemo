package com.example.demo.controllers;

import com.example.demo.model.Checks;
import com.example.demo.model.Goods;
import com.example.demo.model.Checklines;
import com.sun.tools.javac.comp.Check;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBController {

    private String jdbc = "jdbc:postgresql://192.168.0.137:5432/cashtest";
    private String user = "davide";
    private String pass = "jw8s0F4";
    private String SQL_SELECT = "select * from goods;";

    public List<Goods> getPrice(Connection connection) throws SQLException {

        List<Goods> result = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Goods goods = new Goods();
                goods.setId(resultSet.getLong("id"));
                goods.setName(resultSet.getString("name"));
                goods.setPrice(resultSet.getFloat("price"));
                goods.setCount(1);
                result.add(goods);
            }
        }

        return result;

    }

    public void setCheck(Connection connection, Checks check){
        String sql = "insert into checks(date, time, summ, identificator) values (?, ?, ?, ?)";
        long id = 0;
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setDate(1, check.getDate());
            pstmt.setTime(2, check.getTime());
            pstmt.setFloat(3, check.getSumm());
            pstmt.setString(4, check.getIdentificator());
            int affectedRows = pstmt.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCheckLines(Connection connection,  List<Checklines> checkLines ){
        String sql = "insert into checklines( product_id, str_number, product_count, summ, identificator )  values (?, ?, ?, ?, ?)";
        long id = 0;
        for (Checklines checkLine : checkLines) {
            try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setLong(1, checkLine.getProduct_id());
                pstmt.setInt(2, checkLine.getStr_number());
                pstmt.setInt(3, checkLine.getProduct_count());
                pstmt.setFloat(4, checkLine.getSumm());
                pstmt.setString(5, checkLine.getIdentificator());

                int affectedRows = pstmt.executeUpdate();
                // check the affected rows
                if (affectedRows > 0) {
                    // get the ID back
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            id = rs.getLong(1);
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }


    public Connection connect() throws SQLException {
        return DriverManager.getConnection( jdbc, user, pass);
    }
}
