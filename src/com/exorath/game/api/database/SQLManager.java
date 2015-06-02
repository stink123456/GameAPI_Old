package com.exorath.game.api.database;

import com.exorath.game.lib.util.GameUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Modified by toon on 31/05/2015. Created by Nick
 */
public class SQLManager {
    private String host;
    private int port;
    private String database;
    private String user;
    private String password;

    private Map<String, SQLTable> tables = new HashMap<>();
    private Connection connection;

    public SQLManager(String host, int port, String database, String user, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.host = host;
            this.port = port;
            this.database = database;
            this.user = user;
            this.password = password;

            this.connection = this.open();

            loadTables();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Get an SQLTable, formatted as pluginName_name
     *
     * @param plugin    plugin which table is used by.
     * @param tableName name of table.
     * @return Existing or new SQLTable with the formatted name.
     */
    public SQLTable getTable(JavaPlugin plugin, String tableName) {
        return getTable(plugin.getName(), tableName);
    }

    public SQLTable getTable(String pluginName, String tableName) {
        String name = pluginName + "__" + tableName;
        if (tables.containsKey(name)) return tables.get(pluginName);
        return addTable(name);
    }

    protected Connection open() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + "/" + this.database + "?user="
                    + this.user + "&password=" + this.password);
        } catch (SQLException ex) {
            throw new IllegalArgumentException("Invalid SQL server/database information", ex);
        }

        return this.connection;
    }

    public void refresh() {
        if (!this.checkConnection()) {
            this.connection = this.open();
        }
    }

    public boolean checkConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                return true;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    protected String[] getCredentials() {
        return new String[]{this.host, String.valueOf(this.port), this.database, this.user, this.password};
    }

    protected Connection getConnection() {
        return this.connection;
    }

    /**
     * Load tables into tables HashMap.
     */
    private void loadTables() {
        try {
            this.tables.clear();
            ResultSet res = executeQuery("SHOW TABLES FROM " + this.database);

            while (res.next()) {
                String tableName = res.getString("Tables_in_" + this.database);
                SQLTable table = new SQLTable(tableName);

                ResultSet columnsRes = executeQuery("select * from information_schema.columns where table_schema = '" + this.database + "' and table_name = '" + tableName + "'");
                while (columnsRes.next()) {
                    String columnName = columnsRes.getString("COLUMN_NAME"); //get data type out of column result set
                    String columnDataType = columnsRes.getString("DATA_TYPE"); //get data type out of column result set
                    ColumnType columnType = ColumnType.getColumnType(columnDataType); //Column type found by data type
                    if(columnType == null) continue;
                    if (columnType.isVarChar()) {
                        int maxCharLength = columnsRes.getInt("CHARACTER_MAXIMUM_LENGTH"); // get max char length from varchar column
                        if (maxCharLength != 0) {
                            columnType = ColumnType.getColumnType(maxCharLength); // adjust columnType
                        }
                    }
                    table.loadColumn(new SQLColumn(columnName, columnType));
                }
                this.tables.put(tableName, table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private SQLTable addTable(String name) {
        executeQuery("CREATE TABLE " + name + " (" + SQLTable.KEY + "varchar(255))");
        loadTables();
        if (tables.containsKey(name))
            return tables.get(name);
        return null;
    }

    public ResultSet executeQuery(String query) {
        try {
            refresh();
            PreparedStatement statement = this.connection.prepareStatement(query);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}