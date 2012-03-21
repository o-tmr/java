// kanji=漢字 -*- mode: java; coding: utf-8; tab-width: 4; -*-
package jp.ne.hatena.ttmmrr.table.sample;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import jp.ne.hatena.ttmmrr.table.TableHeaderColumnSelector;

/**
 * @author ttmmrr
 */
@SuppressWarnings("serial")
public class Sample extends JFrame {
    Sample() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Container contentPane = getContentPane();
        final JTable jTable = createJTable();
        JScrollPane jScrollPane = new JScrollPane(jTable);

        final TableHeaderColumnSelector thcs = new TableHeaderColumnSelector(jTable, jScrollPane); // *1-a
        thcs.setup(); // *1-b

        contentPane.add(jScrollPane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    JTable createJTable() {
        final Object[][] data = {
            {"あ", "い", "う", "え", "お" },
            {"か", "き", "く", "け", "こ" },
            {"さ", "し", "す", "せ", "そ" },
            {"た", "ち", "つ", "て", "と" },
            {"な", "に", "ぬ", "ね", "の" },
        };
        final Object[] columnNames = {
            "A列", "B列", "C列", "D列", "E列"
        };
        final JTable jTable = new JTable(data, columnNames);
        jTable.setColumnSelectionAllowed(true);
        return jTable;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Sample();
            }
        });
    }
} // Sample

