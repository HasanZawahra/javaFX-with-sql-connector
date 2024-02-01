package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;

public class Reset {

    public static void reset(String s, TableView tableView, Connection connection) {
        StringBuilder stringBuilder = new StringBuilder("select * from `" + s + "` where");


        String d = stringBuilder.toString();
        if (d.endsWith("and")) {
            d = d.substring(0, d.length() - 4);
        } else if (d.endsWith("where")) {
            d = d.substring(0, d.length() - 6);
        }

        tableView.getColumns().clear();
        tableView.getItems().clear();
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        try {

            ResultSet rs = connection.createStatement().executeQuery(d);

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableView.getColumns().addAll(col);
            }

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);

            }

            tableView.setItems(data);
        } catch (Exception v) {
            v.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }
}
