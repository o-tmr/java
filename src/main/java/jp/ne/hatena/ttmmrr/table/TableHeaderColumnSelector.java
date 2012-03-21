// kanji=漢字 -*- mode: java; coding: utf-8; tab-width: 4; -*-
package jp.ne.hatena.ttmmrr.table;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;

/**
 * @author ttmmrr
 */
public class TableHeaderColumnSelector extends MouseAdapter {
    private final JTable _table;
    private final JTableHeader _header;
    private final JScrollPane _scrollPane;

    private int _columnSelectionMode;
    private int _draggedStartColumn = -1;

    public TableHeaderColumnSelector(final JTable table, final JScrollPane scrollPane) {
        super();
        _table = table;
        _header = table.getTableHeader();
        _scrollPane = scrollPane;
    }

    public void setup() {
        _header.addMouseListener(this);
        _header.addMouseMotionListener(this);
    }

    @Override public void mousePressed(final MouseEvent e) {
        final ListSelectionModel selectionModel = _table.getColumnModel().getSelectionModel();
        _columnSelectionMode = selectionModel.getSelectionMode();
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        final int col = _header.columnAtPoint(e.getPoint());
        if (!e.isShiftDown()) {
            _draggedStartColumn = col;
        }
    }

    @Override public void mouseReleased(final MouseEvent e) {
        mouseReleased0(e);
        _table.getColumnModel().getSelectionModel().setSelectionMode(_columnSelectionMode);
        _table.repaint();
    }

    private void mouseReleased0(final MouseEvent e) {
        final int col = _header.columnAtPoint(e.getPoint());
        if (-1 == col) {
            return;
        }
        final int[] columns = _table.getSelectedColumns();
        final boolean isAllRowsSelected = isAllRowsSelected();
        if (0 == columns.length || !isAllRowsSelected) {
            setSelectAllRows(col, col);
            return;
        }
        if (col == _draggedStartColumn) {
            if (isAllRowsSelected && e.isControlDown()) {
                if (isSelectedColumn(col)) {
                    removeSelectAllRows(col, col);
                } else {
                    addSelectAllRows(col, col);
                }
            } else {
                setSelectAllRows(col, col);
            }
            return;
        }
        final int min = Math.min(col, columns[0]);
        final int max = Math.max(col, columns[columns.length - 1]);
        addSelectAllRows(min, max);
        if (!e.isShiftDown()) {
            _draggedStartColumn = -1;
        }
    }

    @Override public void mouseDragged(final MouseEvent e) {
        if (-1 == _draggedStartColumn) { return; }

        final int col = _header.columnAtPoint(e.getPoint());
        if (-1 == col) {
            return;
        }
        setSelectAllRows(_draggedStartColumn, col);
        final Point viewPosition = _scrollPane.getViewport().getViewPosition();
        final int row = _table.rowAtPoint(viewPosition);
        final Rectangle rect = _table.getCellRect(row, col, true);
        _table.scrollRectToVisible(rect);
    }

    public void setSelectAllRows(final int col1, final int col2) {
        final int rowCount = _table.getRowCount();
        _table.setColumnSelectionInterval(col1, col2);
        _table.addRowSelectionInterval(0, rowCount - 1);
    }

    public void addSelectAllRows(final int col1, final int col2) {
        final int rowCount = _table.getRowCount();
        _table.addColumnSelectionInterval(col1, col2);
        _table.addRowSelectionInterval(0, rowCount - 1);
    }

    public void removeSelectAllRows(final int col1, final int col2) {
        _table.removeColumnSelectionInterval(col1, col2);
        if (0 == _table.getSelectedColumnCount()) {
            _table.getSelectionModel().clearSelection();
        }
    }

    private boolean isAllRowsSelected() {
        final int rowCount = _table.getRowCount();
        final int selectedRowCount = _table.getSelectedRowCount();
        return rowCount == selectedRowCount;
    }

    private boolean isSelectedColumn(final int col) {
        final int[] columns = _table.getSelectedColumns();
        for (final int c : columns) {
            if (c == col) {
                return true;
            }
        }
        return false;
    }
} // TableHeaderColumnSelector

