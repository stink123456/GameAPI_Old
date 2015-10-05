package com.exorath.game.api.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import com.exorath.game.GameAPI;

/**
 * Created by too on 31/05/2015.
 */
public class SQLTable {

    public static final String KEY = "UUID";
    private String name;
    private HashMap<String, SQLColumn> columns = new HashMap<>();

    public SQLTable(String name) {
        this.name = name;
    }


    /**
     * Update or insert data into database
     *
     * @param data
     *            SQLData you want to insert or update
     */
    public void setData(SQLData data) {
        if (this.rowExists(data.getUuid().toString())) {
            this.createRow(data);
        } else {
            this.updateRow(data);
        }
    }

    /**
     * Create a column in the database
     *
     * @param column
     */
    public void addColumn(SQLColumn column) {
        GameAPI.getSQLManager().executeQuery(
                "ALTER TABLE " + this.name + " ADD " + column.getKey() + column.getType().getDataTypeStructured());
        this.loadColumn(column);
    }
    /**
     * loads the data from the database of the given key
     */
    public boolean load(SQLData data) {
        //TODO: If exists: load and return. Else: return null;
        ResultSet rs = GameAPI.getSQLManager().executeQuery(
                "SELECT * FROM " + this.name + " WHERE " + SQLTable.KEY + "='" + data.getUuid() + "' LIMIT 1");
        try {
            if (rs.next()) {
                ResultSetMetaData rsMeta = rs.getMetaData();
                for (int i = 1; i <= rsMeta.getColumnCount(); i++) {
                    String columnName = rsMeta.getColumnName(i);
                    if(columnName.equals(KEY))
                        continue;
                    Object obj = rs.getObject(columnName);
                    if (obj == null) {
                        continue;
                    }
                    data.setData(columnName, obj);
                }
            } else {
                GameAPI.printConsole(
                        "SQLTable.getData with key " + data.getUuid() + " failed to load data. SQLData doesn't exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Load a column in the columns hashmap
     *
     * @param column
     */
    protected void loadColumn(SQLColumn column) {
        this.columns.put(column.getKey(), column);
    }

    /**
     * Create a new row, this will only happen if the row key doesn't exist yet.
     *
     * @param data
     */
    public void createRow(SQLData data) {
        GameAPI.printConsole("INSERT INTO " + this.name + " " + data.getValuesString() + ";");
        int rowsChanged = GameAPI.getSQLManager()
                .executeUpdate("INSERT INTO " + this.name + " " + data.getValuesString() + ";");//create new row with data
        if (rowsChanged >= 0) {
            GameAPI.printConsole(
                    "Created row " + data.getUuid().toString() + " in table " + this.name + " successfully.");
        } else {
            GameAPI.printConsole(
                    "Attempt to create row " + data.getUuid().toString() + " in table " + this.name + " failed.");
        }
    }

    /**
     * Update an already existing row
     * <<<<<<< HEAD
     * 
     * @param data
     *            SQLData you want to update
     */
    public void updateRow(SQLData data) {
        StringBuilder query = new StringBuilder("UPDATE " + this.name + " SET ");
        for (String dataKey : data.getData().keySet()) {
            query.append(dataKey);
            query.append("='");
            query.append(data.getData().get(dataKey).toString());
            query.append("' ");
            query.append(",");
        }
        if(data.getData().size() > 0)
            query.deleteCharAt(query.length() - 1);
        query.append("WHERE " + KEY + "=" + "'" + data.getUuid() + "'");
        GameAPI.printConsole(query.toString());
        GameAPI.getSQLManager().executeUpdate(query.toString());//update row with data

        GameAPI.printConsole("Updated row " + data.getUuid() + " in table " + this.name + " successfully.");
        //TODO: Add error reporting
    }

    /**
     * Check if the row with given key exists in the table
     *
     * <<<<<<< HEAD
     * 
     * @param key
     *            Key that should be checked
     *            =======
     * @param key
     *            Key that should be checked
     *            >>>>>>> master
     * @return whether or not the row exists.
     */
    public boolean rowExists(String key) {
        try {
            ResultSet rs = GameAPI.getSQLManager().executeQuery(
                    "SELECT 1 FROM " + this.name + " WHERE " + KEY + "='" + key + "';");
            if (rs == null)
                return false;
            if (rs.next())
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Add a column to the MYSQL Table
     *
     * <<<<<<< HEAD
     * 
     * @param columnName
     *            name this column should have
     * @param type
     *            Type this column should have
     */
    public void addColumn(String columnName, ColumnType type) {
        GameAPI.getSQLManager().execute(
                "ALTER TABLE " + this.name + " ADD " + columnName + " " + type.getDataTypeStructured() + " UNIQUE;");

        GameAPI.printConsole("Added column " + columnName + " with type " + type.getDataTypeStructured() + " to table "
                + this.name + ".");
    }

    /**
     * Refresh the tables. TODO: Double check if this causes issues because
     * tables are cleared.
     */
    protected void refresh() {
        GameAPI.getSQLManager().refresh();
    }

    /**
     * Get a HashMap of all the columns and their names in this table
     *
     * @return A HashMap of all the columns and their names in this table
     */
    public HashMap<String, SQLColumn> getColumns() {
        return this.columns;
    }
}
