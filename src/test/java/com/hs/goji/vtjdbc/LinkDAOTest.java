package com.hs.goji.vtjdbc;

import io.jenetics.jpx.Link;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class LinkDAOTest {

	private final DB db = MySQL.INSTANCE;

	@Test
	public void insert() throws SQLException {
		db.transaction(conn -> {
			final Stored<Link> link = LinkDAO.of(conn).insert(
				Link.of("https://jenetics.io/jpx", "Homepage", "Wep")
			);

			System.out.println(link);
		});

	}

}