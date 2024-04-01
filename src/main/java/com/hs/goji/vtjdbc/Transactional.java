package com.hs.goji.vtjdbc;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public interface Transactional {
    public interface Executable<T> {
        public T execute(final Connection conn) throws SQLException;

        public interface Callable {
            public void call(final Connection conn) throws SQLException;
        }

        public DataSource dataSource();


        public default <T> T transaction(final Executable<T> executable) throws SQLException {
            try (Connection conn = dataSource().getConnection()) {
                return transaction(conn, executable);
            }
        }


        public default void transaction(final Callable callable) throws SQLException {
            try (Connection conn = dataSource().getConnection()) {
                transaction(conn, c -> {callable.call(c); return null;});
            }
        }







        static <T> T transaction(
                final Connection connection,
                final Executable<T> executable
        )
                throws SQLException
        {
            try {
                if (connection.getAutoCommit()) {
                    connection.setAutoCommit(false);
                }

                final T result = executable.execute(connection);
                connection.commit();
                return result;
            } catch (Throwable e) {
                try {
                    connection.rollback();
                } catch (Exception suppressed) {
                    e.addSuppressed(suppressed);
                }
                throw e;
            }
        }


    }
}
