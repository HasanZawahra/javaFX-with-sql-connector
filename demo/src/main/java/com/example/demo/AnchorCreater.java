package com.example.demo;

import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.sql.*;
import java.util.ArrayList;

public class AnchorCreater {
    public static AnchorPane createTabContent(String s) {
        AnchorPane anchorPane = new AnchorPane();
        TableView tableView = new TableView<>();
        tableView.setPrefSize(310, 430);

        Button button = new Button("insert"), n = new Button("search"),
                h = new Button("delete"), u = new Button("Update"), r = new Button("Reset");

        Text text = new Text("to");
        text.setFont(Font.font("Arial", FontWeight.BOLD, 35));
        Text t = new Text("Done by Hasan");
        t.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        AnchorPane.setTopAnchor(n, 20.0);
        AnchorPane.setLeftAnchor(n, 20.0);
        AnchorPane.setTopAnchor(button, 60.0);
        AnchorPane.setLeftAnchor(button, 20.0);
        AnchorPane.setTopAnchor(h, 100.0);
        AnchorPane.setLeftAnchor(h, 20.0);
        AnchorPane.setTopAnchor(r, 140.0);
        AnchorPane.setLeftAnchor(r, 20.0);
        AnchorPane.setTopAnchor(u, 20.0);
        AnchorPane.setLeftAnchor(u, 300.0);
        AnchorPane.setLeftAnchor(tableView, 570.0);
        AnchorPane.setTopAnchor(text, 125.0);
        AnchorPane.setLeftAnchor(text, 310.0);
        AnchorPane.setBottomAnchor(t, 10.0);
        AnchorPane.setLeftAnchor(t, 220.0);
        anchorPane.getChildren().addAll(n, button, h, u, text,t, r);

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hasan", "root", "");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + s);

            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList<Control> tf = new ArrayList<>();
            ArrayList<String> arrayListu = new ArrayList<>();
            ArrayList<Control> tfu = new ArrayList<>();
            Label label;
            Control control;
            Label labelu;
            Control controlu;

            ResultSetMetaData metaData = resultSet.getMetaData();
            DatabaseMetaData meta = connection.getMetaData();


            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);

                ResultSet foreignKeys = meta.getImportedKeys(connection.getCatalog(), null, s);
                boolean isForeignKey = false;
                String referencedTable = null;

                while (foreignKeys.next()) {
                    String foreignKeyColumnName = foreignKeys.getString("FKCOLUMN_NAME");
                    if (columnName.equals(foreignKeyColumnName)) {
                        isForeignKey = true;
                        referencedTable = foreignKeys.getString("PKTABLE_NAME");
                        break;
                    }
                }

                if (isForeignKey) {
                    ComboBox<String> comboBox = new ComboBox<>(), comboBox1 = new ComboBox<>();
                    ResultSet referencedData = statement.executeQuery("SELECT * FROM " + referencedTable);

                    while (referencedData.next()) {
                        comboBox.getItems().add(referencedData.getString(1));
                        comboBox1.getItems().add(referencedData.getString(1));
                    }
                    label = new Label(columnName);
                    control = comboBox;
                    labelu = new Label("new " + columnName);
                    controlu = comboBox1;
                } else {
                    label = new Label(columnName);
                    control = new TextField();
                    labelu = new Label("new " + columnName);
                    controlu = new TextField();
                }

                AnchorPane.setTopAnchor(label, i * 20.0 + ((i - 1) * 40));
                AnchorPane.setLeftAnchor(label, 100.0);
                AnchorPane.setTopAnchor(control, (i * 20.0 + ((i - 1) * 40)) + 20);
                AnchorPane.setLeftAnchor(control, 100.0);

                AnchorPane.setTopAnchor(labelu, i * 20.0 + ((i - 1) * 40));
                AnchorPane.setLeftAnchor(labelu, 380.0);
                AnchorPane.setTopAnchor(controlu, (i * 20.0 + ((i - 1) * 40)) + 20);
                AnchorPane.setLeftAnchor(controlu, 380.0);
                anchorPane.getChildren().addAll(label, control, labelu, controlu);

                arrayList.add(label.getText());
                tf.add(control);
                arrayListu.add(labelu.getText().substring(4));
                tfu.add(controlu);
            }

            Reset.reset(s,tableView,connection);

            anchorPane.getChildren().add(tableView);
            n.setOnAction(e -> {
                Select.select(arrayList, tf, s, tableView, connection);
            });

            r.setOnAction(e -> {
                Reset.reset(s, tableView, connection);
            });

            u.setOnAction(e -> {
                Update.update(arrayList, tf, s, arrayListu, tfu, tableView, connection);
            });


            h.setOnAction(e -> {
                Delete.delete(arrayList, tf, s, tableView, connection);
            });

            button.setOnAction(e -> {
                Insert.insert(arrayList, tf, s, tableView, connection, metaData, meta);
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return anchorPane;
    }
}
