package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.sql.*;
import java.util.ArrayList;

public class Insert {

    public static void insert(ArrayList<String> arrayList, ArrayList<Control> tf, String s, TableView tableView, Connection connection, ResultSetMetaData metaData, DatabaseMetaData meta) {

        StringBuilder stringBuilder = new StringBuilder("INSERT INTO`" + s + "`(`");
        for (int i = 0; i < arrayList.size(); i++) {
            String value = arrayList.get(i);
            if (value.length() > 0) {
                stringBuilder.append(value + "`, `");
            }
        }
        stringBuilder = new StringBuilder(stringBuilder.substring(0, stringBuilder.length() - 3));
        stringBuilder.append(") VALUES ('");
        int c = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if (tf.get(i) instanceof TextField) {
                String value = ((TextField) tf.get(i)).getText();
                if (value.length() > 0) {
                    stringBuilder.append(value + "','");
                    c++;
                }
            } else if (tf.get(i) instanceof ComboBox) {
                String value = ((ComboBox<String>) tf.get(i)).getValue();
                if (value != null && !value.isEmpty()) {
                    stringBuilder.append(value + "','");
                    c++;
                }
            }
        }
        if (c == arrayList.size()) {
            stringBuilder = new StringBuilder(stringBuilder.substring(0, stringBuilder.length() - 2));
            stringBuilder.append(")");
            String d = stringBuilder.toString();
            tableView.getColumns().clear();
            tableView.getItems().clear();

            ObservableList<ObservableList> data = FXCollections.observableArrayList();
            try {
                String pk = "";
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String columnName = metaData.getColumnName(i);

                    ResultSet exportedKeys = meta.getExportedKeys(connection.getCatalog(), null, s);

                    while (exportedKeys.next()) {
                        String primaryKeyColumnName = exportedKeys.getString("PKCOLUMN_NAME");
                        if (columnName.equals(primaryKeyColumnName)) {
                            pk = columnName;
                            break;
                        }
                    }
                }
                Statement stm = connection.createStatement();
                if (pk != "" && arrayList.contains(pk))
                    stm.executeUpdate(d);
                else if (pk == "") stm.executeUpdate(d);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Data inserted:)");
                alert.showAndWait();
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("you may have entered an existed primary key, or an incorrect data type.");
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
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Make sure to fill all the blanks");
            alert.showAndWait();
        }
    }
}
