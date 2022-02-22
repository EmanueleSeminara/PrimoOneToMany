package it.prova.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.prova.connection.MyConnection;
import it.prova.model.Articolo;
import it.prova.model.Negozio;

public class NegozioDAO {

	public List<Negozio> list() {

		List<Negozio> result = new ArrayList<Negozio>();
		Negozio negozioTemp = null;

		try (Connection c = MyConnection.getConnection();
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery("select * from negozio a ")) {

			while (rs.next()) {
				negozioTemp = new Negozio();
				negozioTemp.setId(rs.getLong("id"));
				negozioTemp.setNome(rs.getString("nome"));
				negozioTemp.setIndirizzo(rs.getString("indirizzo"));

				result.add(negozioTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public Negozio selectById(Long idNegozioInput) {

		if (idNegozioInput == null || idNegozioInput < 1)
			return null;

		Negozio result = null;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from negozio i where i.id=?")) {

			ps.setLong(1, idNegozioInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Negozio();
					result.setId(rs.getLong("id"));
					result.setNome(rs.getString("nome"));
					result.setIndirizzo(rs.getString("indirizzo"));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public int insert(Negozio negozioInput) {
		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("INSERT INTO negozio (nome, indirizzo) VALUES (?, ?)")) {

			ps.setString(1, negozioInput.getNome());
			ps.setString(2, negozioInput.getIndirizzo());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	// TODO
	public int update(Negozio negozioInput) {
		if (negozioInput == null || negozioInput.getId() < 1) {
			return 0;
		}

		int result = 0;

		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("update negozio set nome=?, indirizzo=? where id=?")) {
			ps.setString(1, negozioInput.getNome());
			ps.setString(2, negozioInput.getIndirizzo());
			ps.setLong(3, negozioInput.getId());

			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return result;
	}

	public int delete(Negozio negozioInput) {
		if (negozioInput == null || negozioInput.getId() < 1) {
			return 0;
		}

		int result = 0;

		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("delete from negozio where id=?")) {
			ps.setLong(1, negozioInput.getId());

			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return result;
	}

	// prende negozioInput e grazie al suo id va sulla tabella articoli e poi
	// ad ogni iterazione sul resultset aggiunge agli articoli di negozioInput
	public void populateArticoli(Negozio negozioInput) {
		List<Articolo> result = new ArrayList<>();
		Articolo articoloTemp = null;

		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement(
						"select * from negozio n inner join articolo a on n.id=a.negozio_id where n.id=?")) {
			ps.setLong(1, negozioInput.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					if (!negozioInput.getNome().equals(rs.getString("nome"))
							&& !negozioInput.getIndirizzo().equals(rs.getString("indirizzo"))) {
						throw new RuntimeException("populateArticolo: FAILED, l'oggetto in ingresso non corrisponde ");
					}
					articoloTemp = new Articolo();
					articoloTemp.setId(rs.getLong("id"));
					articoloTemp.setNome(rs.getString("nome"));
					articoloTemp.setMatricola(rs.getString("matricola"));

					articoloTemp.setNegozio(negozioInput);

					result.add(articoloTemp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		negozioInput.setArticoli(result);
	}

	// implementare inoltre
	public List<Negozio> findAllByIniziali(String inizialeInput) {
		if (inizialeInput == null) {
			throw new RuntimeException("findAllByIniziali: FAILED, inizilae vuota");
		}
		Negozio temp = null;
		List<Negozio> result = new ArrayList<>();

		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from negozio where nome like ?;")) {

			ps.setString(1, inizialeInput + "%");

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					temp = new Negozio();
					temp.setNome(rs.getString("nome"));
					temp.setIndirizzo("indirizzo");
					temp.setId(rs.getLong("id"));
					result.add(temp);
				}

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

}
