package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Update {


    public static void update(ArrayList<String> arrayList, ArrayList<Control> tf, String s,
                              ArrayList<String> arrayListu, ArrayList<Control> tfu, TableView tableView, Connection connection) {
        boolean b = false,u=false;
        StringBuilder stringBuilder = new StringBuilder("UPDATE `" + s + "` SET `");
        for (int i = 0; i < arrayListu.size(); i++) {
            if (tf.get(i) instanceof TextField) {
                String value = ((TextField) tf.get(i)).getText();
                if (value.length() > 0) {
                    b = true;
                }
            } else if (tf.get(i) instanceof ComboBox) {
                String value = ((ComboBox<String>) tf.get(i)).getValue();
                if (value != null && !value.isEmpty()) {
                    b = true;
                }
            }
            if (tfu.get(i) instanceof TextField) {
                String value = ((TextField) tfu.get(i)).getText();
                if (value.length() > 0) {
                    stringBuilder.append(arrayListu.get(i) + "` = '" + value + "' , `");
                    u=true;
                }
            } else if (tfu.get(i) instanceof ComboBox) {
                String value = ((ComboBox<String>) tfu.get(i)).getValue();
                if (value != null && !value.isEmpty()) {
                    stringBuilder.append(arrayList.get(i) + "` = '" + value + "' , `");
                    u=true;
                }
            }
        }
        if (b && u) {
            stringBuilder = new StringBuilder(stringBuilder.substring(0, stringBuilder.length() - 3));
            stringBuilder.append("WHERE ");
            for (int i = 0; i < arrayList.size(); i++) {
                if (tf.get(i) instanceof TextField) {
                    String value = ((TextField) tf.get(i)).getText();
                    if (value.length() > 0) {
                        stringBuilder.append(" `" + arrayList.get(i) + "` = '" + value + "' and");
                    }
                } else if (tf.get(i) instanceof ComboBox) {
                    String value = ((ComboBox<String>) tf.get(i)).getValue();
                    if (value != null && !value.isEmpty()) {
                        stringBuilder.append(" `" + arrayList.get(i) + "` = '" + value + "' and");
                    }
                }
            }
            String d = stringBuilder.toString();
            if (d.endsWith("and")) {
                d = d.substring(0, d.length() - 4);
            }
            tableView.getColumns().clear();
            tableView.getItems().clear();

            ObservableList<ObservableList> data = FXCollections.observableArrayList();
            try {
                Statement stm = connection.createStatement();
                stm.executeUpdate(d);
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Make sure the data types are right!");
                alert.showAndWait();
            }
            String ee = "select * from `" + s + "`";
            try {

                ResultSet rs = connection.createStatement().executeQuery(ee);

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
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("You may forgot to enter some necessary information for this operation to be done.");
            alert.showAndWait();
        }
    }

}
