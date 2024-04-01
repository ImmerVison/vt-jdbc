package com.hs.goji.vtjdbc;



import io.jenetics.jpx.Link;

import java.sql.*;
import java.util.Optional;

public final class LinkDAO extends DAO<Link> {

    public LinkDAO(final Connection conn) {
        super(conn);
    }

    @Override
    public RowParser<Link> rowParser() {
        return rs -> Stored.of(
                rs.getLong("id"),
                Link.of(
                        rs.getString("href"),
                        rs.getString("text"),
                        rs.getString("type")
                )
        );
    }


    public Stored<Link> insert(final Link link) throws SQLException {

        final String query = "INSERT INTO link(href, text, type) VALUES(?, ?, ?);";

        PreparedStatement stmt = _conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, link.getHref().toString());
        stmt.setString(2, link.getText().orElse(null));
        stmt.setString(3, link.getType().orElse(null));

        stmt.executeUpdate();
        return Stored.of(DAO.id(stmt), link);
    }


    public Optional<Stored<Link>> selectByID(final long id) throws SQLException {

        final String query = "SELECT id, href, text, type FROM link WHERE id = ?;";

        PreparedStatement stmt = _conn.prepareStatement(query);
        stmt.setLong(1, id);

        try(ResultSet rs = stmt.executeQuery()) {
            return firstOption(rs);
        }
    }



    public static LinkDAO of(final Connection conn) {
        return new LinkDAO(conn);
    }
}
