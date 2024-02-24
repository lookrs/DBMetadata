package com.github.abel533.component.ea;

import com.github.abel533.component.DBData;
import com.github.abel533.database.IntrospectedColumn;
import com.github.abel533.database.IntrospectedTable;
import com.github.abel533.database.MatchType;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomMenu extends JMenuBar {
    public CustomMenu(JFrame frame) {
        // 创建一个菜单
        JMenu menu = new JMenu("File");

        // 创建菜单项并添加点击事件
        JMenuItem menuItem = new JMenuItem("sql转换");
        menuItem.addActionListener(e -> showCustomDialog(frame));
        // 将菜单项添加到菜单中
        menu.add(menuItem);

        // 将菜单添加到菜单栏中
        add(menu);
    }

    public static void showCustomDialog(JFrame frame) {
        JDialog dialog = new JDialog(frame, "sql文转换", true);
        JPanel panel = new JPanel(new GridLayout(1, 2));

        JLabel correlationTables = new JLabel("sql涉及的表(,):");
        JTextField correlationTablesField = new JTextField();
        correlationTablesField.setPreferredSize(new Dimension(250, 25));

        JPanel jPanel = new JPanel();
        jPanel.add(correlationTables);
        jPanel.add(correlationTablesField);
        JTextArea editableTextArea = new JTextArea();
        JTextArea nonEditableTextArea = new JTextArea();
        nonEditableTextArea.setEditable(false);

        panel.add(new JScrollPane(editableTextArea));
        panel.add(new JScrollPane(nonEditableTextArea));
        dialog.add(jPanel, BorderLayout.NORTH);
        dialog.add(panel, BorderLayout.CENTER);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            String inputTables = correlationTablesField.getText();
            String[] tableNames = inputTables.split(",");
            String japaneseSql = editableTextArea.getText();
            String processedText = getReplacedSql(tableNames, japaneseSql);
            nonEditableTextArea.setText(processedText);
        });
        dialog.add(okButton, BorderLayout.SOUTH);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width / 4;
        int height = screenSize.height / 4;
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(frame); // 对话框居中显示
        dialog.setVisible(true);
    }

    static String getReplacedSql(String[] tableNames, String japaneseSql) {
        ArrayList<String> searchText = new ArrayList<>();
        ArrayList<String> replacement = new ArrayList<>();
        Arrays.stream(tableNames).forEach(tableName -> {
            List<IntrospectedTable> tables = DBData.selectTables(null, tableName, MatchType.EQUALS, true);
            if (ObjectUtils.isNotEmpty(tables) && tables.size() == 1) {
                IntrospectedTable introspectedTable = tables.get(0);
                searchText.add(introspectedTable.getRemarks());//日文
                replacement.add(introspectedTable.getName());//英文
                List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
                allColumns.forEach(column -> {
                    searchText.add(column.getRemarks());
                    replacement.add(column.getName());
                });
            }
        });
        return StringUtils.replaceEach(japaneseSql, searchText.toArray(new String[0]), replacement.toArray(new String[0]));
    }
}
