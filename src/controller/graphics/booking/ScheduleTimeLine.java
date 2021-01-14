package controller.graphics.booking;

import controller.graphics.FXMLLoadersCommunicator;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import model.Schedule;
import model.TimeInterval;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;

public class ScheduleTimeLine implements Initializable {


    @FXML
    private AnchorPane mainPane;

    double MARGIN = 20;
    double DAY_BLOCK_WIDTH = 2000;
    double DAY_BLOCK_HEIGHT = 250;
    private ArrayList<Node> timeLineComponents = new ArrayList<>();
    private ArrayList<Node> descriptions = null;

    public void createTimeLine(Schedule schedule) {
        ArrayList<TimeInterval> intervals = schedule.getIntervals();
        //schedule.printIntervals();
        LocalDate beginDate = intervals.get(0).beginDate;
        LocalDate endDate = intervals.get(intervals.size() - 1).endDate;
        long daysBetween = DAYS.between(beginDate, endDate);
        System.out.println(daysBetween);

        mainPane.setPrefHeight(200);
        mainPane.setPrefWidth(MARGIN + 300 * (daysBetween + 1) + MARGIN);
        mainPane.getChildren().clear();

        timeLineComponents = new ArrayList<>();
        System.out.println(mainPane.getHeight());
        for (int i = 0; i <= daysBetween; i++) {
            Line verticalLine = new Line(MARGIN + i * DAY_BLOCK_WIDTH, MARGIN, MARGIN + i * DAY_BLOCK_WIDTH, DAY_BLOCK_HEIGHT - MARGIN);

            Line horizontalLine = new Line(MARGIN + i * DAY_BLOCK_WIDTH + 10, MARGIN + 40,
                    MARGIN + i * DAY_BLOCK_WIDTH + DAY_BLOCK_WIDTH - 10, MARGIN + 40);
            for (int j = 0; j < 24; j++) {
                Label label = new Label(String.format("%02d : 00", j));
                label.setTranslateY(horizontalLine.getStartY() + 2);
                label.setTranslateX(horizontalLine.getStartX() + (DAY_BLOCK_WIDTH - 20) / 24 * j);
                timeLineComponents.add(label);
            }
            Label label = new Label(beginDate.plusDays(i).toString());
            label.setTranslateX(MARGIN + i * DAY_BLOCK_WIDTH + 10);
            label.setTranslateY(MARGIN + 10);
            timeLineComponents.add(verticalLine);
            timeLineComponents.add(horizontalLine);
            timeLineComponents.add(label);
        }
        int cnt = 0;
        for (TimeInterval interval : intervals) {
            long daysFromBegin = DAYS.between(beginDate, interval.beginDate);
            double wholeWidth = DAY_BLOCK_WIDTH - 20;
            double dayInSeconds = 60 * 60 * 24;
            double width = SECONDS.between(interval.beginTime, interval.endTime) / dayInSeconds * wholeWidth;
            double excess = interval.beginTime.toSecondOfDay() / dayInSeconds * wholeWidth;

            Rectangle rect = new Rectangle(width, 20);
            rect.setTranslateX(MARGIN + daysFromBegin * DAY_BLOCK_WIDTH + 10 + excess);
            rect.setTranslateY(MARGIN + 70);
            rect.setFill(Color.rgb(255, 204, 12));
            if (interval.description.toLowerCase().matches(".*doctor not available.*"))
                rect.setFill(Color.RED);
            rect.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    createDescriptionBox(interval, DAY_BLOCK_WIDTH / 5, 100,
                            rect.getTranslateX() + rect.getWidth()/2 - DAY_BLOCK_WIDTH / 10, rect.getTranslateY() + 20 + 5);
                }
            });

            rect.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    eraseDescriptionBox();
                }
            });

            rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    BookingMenu controller = FXMLLoadersCommunicator.getLoader("BookingMenu").getController();
                    controller.selectOccupiedTimeOnTimeLine(interval);
                }
            });

            timeLineComponents.add(rect);
        }
        mainPane.getChildren().addAll(timeLineComponents);
    }

    private void eraseDescriptionBox() {
        if (descriptions != null) {
            mainPane.getChildren().removeAll(descriptions);
            descriptions = null;
        }
    }

    private void createDescriptionBox(TimeInterval interval, double width, double height, double X, double Y) {
        if (descriptions != null) {
            mainPane.getChildren().removeAll(descriptions);
            descriptions = null;
        }
        descriptions = new ArrayList<>();
        Rectangle rect = new Rectangle(width, height);
        rect.setTranslateY(Y);
        rect.setTranslateX(X);
        rect.setFill(Color.WHITE);

        Label label = new Label("Date: " + interval.beginDate.toString());
        label.setTranslateX(X + 2);
        label.setTranslateY(Y + 2);

        Label label1 = new Label("Begin Time: " + interval.beginTime);
        label1.setTranslateX(X + 2);
        label1.setTranslateY(Y + 2 + 17);

        Label label2 = new Label("End Time: " + interval.endTime);
        label2.setTranslateX(X + 2);
        label2.setTranslateY(Y + 2 + 17 + 17);

        Label label3 = new Label("Description: " + interval.description);
        label3.setTranslateX(X + 2);
        label3.setTranslateY(Y + 2 + 17 + 17 + 17);

        descriptions.addAll(Arrays.asList(rect, label, label1, label2, label3));
        mainPane.getChildren().addAll(descriptions);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
