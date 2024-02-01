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

public class Delete {
    public static void delete(ArrayList<String> arrayList, ArrayList<Control> tf, String s, TableView tableView, Connection connection) {
        StringBuilder stringBuilder = new StringBuilder("delete from `" + s + "` where");
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
        } else if (d.endsWith("where")) {
            d = d.substring(0, d.length() - 6);
        }
        tableView.getColumns().clear();
        tableView.getItems().clear();

        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        try {
            Statement stm = connection.createStatement();
            String ee = "select * from `" + s + "`";
            int c = getRowCount(connection, ee);
            stm.executeUpdate(d);
            Alert alert;
            if (c == getRowCount(connection, ee)) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("No such Data exist :(");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Done! :)");
                alert.showAndWait();
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
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static int getRowCount(Connection connection, String s) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(s);

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
