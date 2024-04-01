package com.hs.goji.vtjdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;


public abstract class DAO<T>{

    public static interface RowParser<T> {
        public Stored<T> toRow(final ResultSet rs) throws SQLException;
    }

    protected final Connection _conn;

    public DAO(final Connection conn) {
        _conn = conn;
    }


    public abstract RowParser<T> rowParser();

    public Optional<Stored<T>> firstOption(final ResultSet rs) throws SQLException {
        return rs.next() ? Optional.of(rowParser().toRow(rs)) : Optional.empty();
    }


    public static long id(final Statement stmt) throws SQLException {
        try(ResultSet keys = stmt.getGeneratedKeys()) {
            if(keys.next()) {
                return keys.getLong(1);
            } else {
                throw new SQLException("Can't fetch generation ID.");
            }
        }
    }

}
