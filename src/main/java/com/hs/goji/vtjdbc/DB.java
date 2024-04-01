package com.hs.goji.vtjdbc;

import io.jenetics.jpx.Link;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DB {
    public interface Executable<T> {
        public T execute(final Connection conn) throws SQLException;
    }


    public interface Callable {
        public void call(final Connection conn) throws SQLException;
    }


    public abstract Connection getConnection() throws SQLException;

    public <T> T transaction(final Connection connection, final Executable<T> executable) throws SQLException {

        try {
            if(connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            final T result = executable.execute(connection);
            connection.commit();
            return result;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (Exception suppressed) {
                e.addSuppressed(suppressed);
            }
            throw e;
        }
    }


    public <T> T transaction(final Executable<T> executable) throws SQLException {
        try (Connection conn = getConnection()) {
            return transaction(conn, executable);
        }
    }


    public void transaction(final Callable callable) throws SQLException {
        try (Connection conn = getConnection()) {
            transaction(conn, c -> {
                callable.call(c);
                return null;
            });
        }
    }



    public void foo() throws SQLException {
        final Link link = transaction(conn -> {
            return Link.of("http://jenetics.io");
        });
    }

}
