package model;

import controller.DataBaseQueryController;

import java.sql.SQLException;

public class AdvancedQueriesRunner {

    public static void main(String[] args) {

        DataBaseQueryController dbcontroller = DataBaseQueryController.getInstance();
        dbcontroller.setUsername("postgres");
        dbcontroller.setPassword("kian");
        dbcontroller.setUrl("jdbc:postgresql://localhost:5432/kian");
        try {
            dbcontroller.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connection successfully created!");

        String[] a = {"some followUp"};
        try {
            dbcontroller.handleQuery(QueryType.FUTURE_REFERRALS_GIVEN_REASON, a);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
