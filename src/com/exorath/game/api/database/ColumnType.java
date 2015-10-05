package com.exorath.game.api.database;

import com.exorath.game.GameAPI;

/**
 * Created by Toon on 31/05/2015.
 * SQL Column types.
 */
public enum ColumnType {
    TINY_INT("TINYINT"),
    SMALL_INT("SMALLINT"),
    MEDIUM_INT("MEDIUMINT"),
    BIG_INT("BIGINT", 20),
    INT("INT", 11),
    DECIMAL("DECIMAL", 20, 10),
    DOUBLE("DOUBLE", 16, 4),
    FLOAT("FLOAT", 10, 2),
    DATE("DATE"),
    DATE_TIME("DATETIME"),
    TIME("TIME"),
    STRING_32("VARCHAR", 32),
    STRING_64("VARCHAR", 64),
    STRING_128("VARCHAR", 128),
    STRING_256("VARCHAR", 256),
    STRING_512("VARCHAR", 512),
    STRING_1024("VARCHAR", 1024),
    STRING_2048("VARCHAR", 2048),
    STRING_4096("VARCHAR", 4096),
    BLOB("BLOB"),
    TINY_BLOB("TINYBLOB"),
    LONG_BLOB("LONGBLOB");

    private String name;
    private int maxCharLength;
    private boolean cappedLength;

    private int decimals;
    private boolean decimal;

    ColumnType(String name) {
        this.name = name;
        this.cappedLength = false;
        this.decimal = false;
    }

    ColumnType(String name, int maxCharLength) {
        this.name = name;
        this.maxCharLength = maxCharLength;
        this.cappedLength = true;
        this.decimal = false;
    }

    ColumnType(String name, int maxCharLength, int decimals) {
        this.name = name;
        this.maxCharLength = maxCharLength;
        this.cappedLength = true;
        this.decimals = decimals;
        this.decimal = true;
    }

    /**
     * Get the DataType of this column, no parameters.
     *
     * @return The DataType of this column, no parameters.
     */
    public String getDataType() {
        return this.name;
    }

    /**
     * Get the maximum amount of characters/digits in this column
     *
     * @return The maximum amount of characters/digits in this column
     */
    public int getMaxCharLength() {
        return this.maxCharLength;
    }

    /**
     * Get the columntype with solely a dataType
     *
     * @param dataType
     *            dataType to look up
     * @return ColumnType that has the given dataType as dataType
     */
    public static ColumnType getColumnType(String dataType) {
        GameAPI.printConsole("");
        if (dataType == null)
            return null;

        for (ColumnType type : ColumnType.values()) {
            if (type.getDataType().equalsIgnoreCase(dataType))
                return type;
        }
        return null;
    }

    /**
     * Get the ColumnType of a dataType and maxCharLength
     *
     * @param dataType
     * @param maxCharLength
     * @return The ColumnType of a dataType and maxCharLength
     */
    public static ColumnType getColumnType(String dataType, int maxCharLength) {
        if (dataType == null)
            return null;

        for (ColumnType type : ColumnType.values()) {
            if (type.getMaxCharLength() == 0)
                return null;
            if (type.getMaxCharLength() == maxCharLength && type.getDataType().equals(dataType))
                return type;
        }
        return null;
    }

    /**
     * return the dataType with parameter layout: DATA_TYPE(P,M)
     *
     * @return The dataType with parameter layout: DATA_TYPE(P,M)
     */
    public String getDataTypeStructured() {
        if (this.cappedLength && this.decimal)
            return this.name + "(" + this.maxCharLength + "," + this.decimals + ")";
        if (this.cappedLength)
            return this.name + "(" + this.maxCharLength + ")";
        if (this.decimal)
            return this.name + "(" + this.decimals + ")";
        return this.name;
    }

    /**
     * Check whether or not this type is a varchar
     *
     * @return Whether or not this type is a varchar
     */
    public boolean isVarChar() {
        return this.name.equals("varchar");
    }

    /**
     * Check whether or not this column has a defined max character length
     *
     * @return Whether or not this column has a defined max character length
     */
    public boolean hasCappedLength() {
        return this.cappedLength;
    }
}
