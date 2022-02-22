package it.prova.test;

import java.util.List;

import it.prova.dao.ArticoloDAO;
import it.prova.dao.NegozioDAO;
import it.prova.model.Articolo;
import it.prova.model.Negozio;

public class NegozioTest {

	public static void main(String[] args) {
		NegozioDAO negozioDAOInstance = new NegozioDAO();
		ArticoloDAO articoloDAOInstance = new ArticoloDAO();

		// ora con i dao posso fare tutte le invocazioni che mi servono
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testInserimentoNegozio(negozioDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");

		testFindByIdNegozio(negozioDAOInstance);

		testInsertArticolo(negozioDAOInstance, articoloDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testFindByIdArticolo(articoloDAOInstance);
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testSelectById(negozioDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");

		testList(negozioDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");

		testUpdate(negozioDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");

		testDelete(negozioDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");

		testInserimentoArticolo(articoloDAOInstance, negozioDAOInstance);
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testSelectById(articoloDAOInstance, negozioDAOInstance);
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testList(articoloDAOInstance, negozioDAOInstance);
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testUpdate(articoloDAOInstance, negozioDAOInstance);
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testDelete(articoloDAOInstance);
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testPopulateArticoli(negozioDAOInstance, articoloDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");

		// ESERCIZIO: COMPLETARE DAO E TEST RELATIVI

		// ESERCIZIO SUCCESSIVO
		/*
		 * se io voglio caricare un negozio e contestualmente anche i suoi articoli
		 * dovrò sfruttare il populateArticoli presente dentro negoziodao. Per esempio
		 * Negozio negozioCaricatoDalDb = negozioDAOInstance.selectById...
		 * 
		 * negozioDAOInstance.populateArticoli(negozioCaricatoDalDb);
		 * 
		 * e da qui in poi il negozioCaricatoDalDb.getArticoli() non deve essere più a
		 * size=0 (se ha articoli ovviamente) LAZY FETCHING (poi ve lo spiego)
		 */

	}

	// ========== TEST NegozioDAO ==========
	private static void testInserimentoNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testInserimentoNegozio inizio.............");
		int quantiNegoziInseriti = negozioDAOInstance.insert(new Negozio("Negozio1", "via dei mille 14"));
		if (quantiNegoziInseriti < 1)
			throw new RuntimeException("testInserimentoNegozio : FAILED");

		System.out.println(".......testInserimentoNegozio fine: PASSED.............");
	}

	private static void testFindByIdNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testFindByIdNegozio inizio.............");
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testFindByIdNegozio : FAILED, non ci sono negozi sul DB");

		Negozio primoNegozioDellaLista = elencoNegoziPresenti.get(0);

		Negozio negozioCheRicercoColDAO = negozioDAOInstance.selectById(primoNegozioDellaLista.getId());
		if (negozioCheRicercoColDAO == null
				|| !negozioCheRicercoColDAO.getNome().equals(primoNegozioDellaLista.getNome()))
			throw new RuntimeException("testFindByIdNegozio : FAILED, i nomi non corrispondono");

		System.out.println(".......testFindByIdNegozio fine: PASSED.............");
	}

	private static void testInsertArticolo(NegozioDAO negozioDAOInstance, ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testInsertArticolo inizio.............");
		// mi serve un negozio esistente
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testInsertArticolo : FAILED, non ci sono negozi sul DB");

		Negozio primoNegozioDellaLista = elencoNegoziPresenti.get(0);

		int quantiArticoliInseriti = articoloDAOInstance
				.insert(new Articolo("articolo1", "matricola1", primoNegozioDellaLista));
		if (quantiArticoliInseriti < 1)
			throw new RuntimeException("testInsertArticolo : FAILED");

		System.out.println(".......testInsertArticolo fine: PASSED.............");
	}

	private static void testSelectById(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testSelectById inizio.............");
		int quantiNegoziInseriti = negozioDAOInstance.insert(new Negozio("Pippo", "Via piero"));
		if (quantiNegoziInseriti < 1) {
			throw new RuntimeException("testSelectById : FAILED");
		}
		Negozio negozioDaTrovare = negozioDAOInstance.list().get(0);
		Negozio negozioTrovato = negozioDAOInstance.selectById(negozioDaTrovare.getId());
		if (negozioTrovato == null) {
			throw new RuntimeException("testSelectById : FAILED");
		}

		System.out.println(".......testSelectById fine: PASSED.............");
	}

	private static void testList(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testList inizio.............");
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testList : FAILED, non ci sono negozi sul DB");
		int quantiNegoziInseriti = negozioDAOInstance.insert(new Negozio("Negozio1", "via dei mille 14"));
		if (quantiNegoziInseriti < 1)
			throw new RuntimeException("testList : FAILED");
		List<Negozio> elencoNegoziPresentiDopoInserimento = negozioDAOInstance.list();
		if (elencoNegoziPresentiDopoInserimento.size() != elencoNegoziPresenti.size() + 1) {
			throw new RuntimeException("testList : FAILED, non ci sono negozi sul DB");
		}

		System.out.println(".......testList fine: PASSED.............");
	}

	private static void testUpdate(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testUpdate inizio.............");
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testUpdate : FAILED, non ci sono negozi sul DB");
		Negozio negozioDaModificare = elencoNegoziPresenti.get(0);

		negozioDaModificare.setNome("Pierone");
		int numeroModifiche = negozioDAOInstance.update(negozioDaModificare);
		if (numeroModifiche != 1) {
			throw new RuntimeException("testUpdate : FAILED, nessun elemento modificato nel DB");
		}

		Negozio negozioDaVerificare = negozioDAOInstance.selectById(negozioDaModificare.getId());

		if (!negozioDaVerificare.getNome().equals(negozioDaModificare.getNome())) {
			throw new RuntimeException("testUpdate : FAILED, i dati aggiornati non corrispondono");
		}

		System.out.println(".......testUpdate fine: PASSED.............");
	}

	private static void testDelete(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testDelete inizio.............");
		int quantiNegoziInseriti = negozioDAOInstance.insert(new Negozio("Negozio1", "via dei mille 14"));
		if (quantiNegoziInseriti < 1)
			throw new RuntimeException("testInserimentoNegozio : FAILED");
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testDelete : FAILED, non ci sono negozi sul DB");

		Negozio negozioDaEliminare = elencoNegoziPresenti.get(elencoNegoziPresenti.size() - 1);
		int negoziEliminati = negozioDAOInstance.delete(negozioDaEliminare);
		if (negoziEliminati != 1) {
			throw new RuntimeException("testDelete : FAILED, non ci sono negozi sul DB");
		}

		System.out.println(".......testDelete fine: PASSED.............");
	}

	public static void testPopulateArticoli(NegozioDAO negozioDaoIstance, ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testDelete inizio.............");
		// Negozio negozioDiProva = new Negozio("Pini gratis", "Ciao pino");
		int quantiNegoziInseriti = negozioDaoIstance.insert(new Negozio("Negozio1", "via dei mille 14"));
		if (quantiNegoziInseriti < 1)
			throw new RuntimeException("testPopulateArticoli : FAILED");
		List<Negozio> elencoNegoziPresenti = negozioDaoIstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testDelete : FAILED, non ci sono negozi sul DB");

		Negozio negozioDaPopolare = elencoNegoziPresenti.get(elencoNegoziPresenti.size() - 1);
		long idNegozioDaPopolare = negozioDaPopolare.getId();

		int sizeVecchia = negozioDaPopolare.getArticoli().size();

		int quantiArticoliInseriti = articoloDAOInstance
				.insert(new Articolo("Articolo1", "via dei mille 14", negozioDaPopolare));
		if (quantiNegoziInseriti != 1)
			throw new RuntimeException("testInserimentoArticolo : FAILED");

		negozioDaoIstance.populateArticoli(negozioDaPopolare);
		if (negozioDaPopolare.getArticoli().size() != sizeVecchia + 1) {
			throw new RuntimeException("testPopulateArticoli : FAILED");
		}

		System.out.println(".......testPopulateArticoli fine: PASSED.............");
	}

	private static void testfindAllByIniziali(NegozioDAO negozioDAOInstance) {

	}

	// ========== TEST ArticoloDao ==========
	private static void testFindByIdArticolo(ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testFindByIdArticolo inizio.............");
		List<Articolo> elencoArticoliPresenti = articoloDAOInstance.list();
		if (elencoArticoliPresenti.size() < 1)
			throw new RuntimeException("testFindByIdArticolo : FAILED, non ci sono articoli sul DB");

		Articolo primoArticoloDellaLista = elencoArticoliPresenti.get(0);

		Articolo articoloCheRicercoColDAO = articoloDAOInstance.selectById(primoArticoloDellaLista.getId());
		if (articoloCheRicercoColDAO == null
				|| !articoloCheRicercoColDAO.getNome().equals(primoArticoloDellaLista.getNome()))
			throw new RuntimeException("testFindByIdArticolo : FAILED, i nomi non corrispondono");

		System.out.println(".......testFindByIdArticolo fine: PASSED.............");
	}

	private static void testInserimentoArticolo(ArticoloDAO articoloDAOInstance, NegozioDAO negozioDaoIstance) {
		System.out.println(".......testInserimentoArticolo inizio.............");
		int numeroNegozi = negozioDaoIstance.list().size();
		if (numeroNegozi < 0) {
			throw new RuntimeException("testFindByIdArticolo : FAILED, i nomi non corrispondono");
		}
		Negozio negozioPerTest = negozioDaoIstance.list().get(0);
		int quantiNegoziInseriti = articoloDAOInstance
				.insert(new Articolo("Articolo1", "via dei mille 14", negozioPerTest));
		if (quantiNegoziInseriti != 1)
			throw new RuntimeException("testInserimentoArticolo : FAILED");

		System.out.println(".......testInserimentoArticolo fine: PASSED.............");
	}

	private static void testSelectById(ArticoloDAO articoloDAOInstance, NegozioDAO negozioDaoIstance) {
		System.out.println(".......testSelectById inizio.............");
		int numeroNegozi = negozioDaoIstance.list().size();
		if (numeroNegozi < 0) {
			throw new RuntimeException("testFindByIdArticolo : FAILED, i nomi non corrispondono");
		}
		Negozio negozioPerTest = negozioDaoIstance.list().get(0);
		int quantiNegoziInseriti = articoloDAOInstance.insert(new Articolo("Pippo", "Via piero", negozioPerTest));
		if (quantiNegoziInseriti < 1) {
			throw new RuntimeException("testSelectById : FAILED");
		}
		Articolo articoloDaTrovare = articoloDAOInstance.list().get(0);
		Articolo articoloTrovato = articoloDAOInstance.selectById(articoloDaTrovare.getId());
		if (articoloTrovato == null) {
			throw new RuntimeException("testSelectById : FAILED");
		}

		System.out.println(".......testSelectById fine: PASSED.............");
	}

	private static void testList(ArticoloDAO articoloDAOInstance, NegozioDAO negozioDaoIstance) {
		System.out.println(".......testList inizio.............");
		List<Articolo> elencoNegoziPresenti = articoloDAOInstance.list();
		int numeroNegozi = negozioDaoIstance.list().size();
		if (numeroNegozi < 0) {
			throw new RuntimeException("testFindByIdArticolo : FAILED, i nomi non corrispondono");
		}
		Negozio negozioPerTest = negozioDaoIstance.list().get(0);
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testList : FAILED, non ci sono negozi sul DB");
		int quantiNegoziInseriti = articoloDAOInstance
				.insert(new Articolo("Articolo1", "via dei mille 14", negozioPerTest));
		if (quantiNegoziInseriti < 1)
			throw new RuntimeException("testList : FAILED");
		List<Articolo> elencoNegoziPresentiDopoInserimento = articoloDAOInstance.list();
		if (elencoNegoziPresentiDopoInserimento.size() != elencoNegoziPresenti.size() + 1) {
			throw new RuntimeException("testList : FAILED, non ci sono negozi sul DB");
		}

		System.out.println(".......testList fine: PASSED.............");
	}

	private static void testUpdate(ArticoloDAO articoloDAOInstance, NegozioDAO negozioDaoIstance) {
		System.out.println(".......testUpdate inizio.............");
		List<Articolo> elencoArticoliPresenti = articoloDAOInstance.list();
		if (elencoArticoliPresenti.size() < 1)
			throw new RuntimeException("testUpdate : FAILED, non ci sono negozi sul DB");

		Articolo articoloDaModificare = elencoArticoliPresenti.get(0);
		articoloDaModificare.setNome("Pierone");
		int numeroModifiche = articoloDAOInstance.update(articoloDaModificare);
		if (numeroModifiche != 1) {
			throw new RuntimeException("testUpdate : FAILED, nessun elemento modificato nel DB");
		}

		Articolo articoloDaVerificare = articoloDAOInstance.selectById(articoloDaModificare.getId());

		if (!articoloDaVerificare.getNome().equals(articoloDaModificare.getNome())) {
			throw new RuntimeException("testUpdate : FAILED, i dati aggiornati non corrispondono");
		}

		System.out.println(".......testUpdate fine: PASSED.............");
	}

	private static void testDelete(ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testDelete inizio.............");
		List<Articolo> elencoNegoziPresenti = articoloDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testDelete : FAILED, non ci sono negozi sul DB");

		Articolo articoloDaEliminare = elencoNegoziPresenti.get(0);
		int negoziEliminati = articoloDAOInstance.delete(articoloDaEliminare);
		if (negoziEliminati != 1) {
			throw new RuntimeException("testDelete : FAILED, non ci sono negozi sul DB");
		}

		System.out.println(".......testDelete fine: PASSED.............");
	}

	private static void testFindAllByNegozio(ArticoloDAO articoloDAOInstance) {
		if (articoloDAOInstance == null) {
			throw new RuntimeException("testFindAllByNegozio : FAILED, articolo passato nullo");
		}
	}

	private static void testFindAllByMatricola(ArticoloDAO articoloDAOInstance) {
		if (articoloDAOInstance == null) {
			throw new RuntimeException("testFindAllByNegozio : FAILED, articolo passato nullo");
		}
	}

	private static void testFindAllByIndirizzoNegozio(ArticoloDAO articoloDAOInstance) {
		if (articoloDAOInstance == null) {
			throw new RuntimeException("testFindAllByNegozio : FAILED, articolo passato nullo");
		}
	}

}
