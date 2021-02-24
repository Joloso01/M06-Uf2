package acb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

public class ACBMain {

	public static void main(String[] args) throws IOException, SQLException, ParseException {
		Menu menu = new Menu();
		Connection connection = null;
		Identity identity;
		int option;
		int intents = 0;
		DBAccessor dbaccessor = new DBAccessor();
		dbaccessor.init();
		while (intents < 3 && connection == null) {
			identity = menu.autenticacio(intents);
			// prova de test
			identity.toString();
			
			connection = dbaccessor.getConnection(identity);
			intents++;
		}

		option = menu.menuPral();
		while (option > 0 && option < 12) {
			switch (option) {
			case 1:
				dbaccessor.mostraEquipos();
				break;

			case 2:
				dbaccessor.mostraJugadors();
				break;

			case 3:
//				dbaccessor.mostraRevistesArticlesAutors();
				break;

			case 4:
//				dbaccessor.altaAutor();
				break;

			case 5:
//				dbaccessor.altaRevista();
				break;

			case 6:
//				dbaccessor.altaArticle();
				break;

			case 7:
//				dbaccessor.actualitzarTitolRevistes(conn);
				break;

			case 8:
//				dbaccessor.afegeixArticleARevista(conn);
				break;

			case 9:
//				dbaccessor.desassignaArticleARevista(conn);
				break;

			case 10:
//				dbaccessor.carregaAutors(conn);
				break;


			default:
				System.out.println("Introdueixi una de les opcions anteriors");
				break;

			}
			option = menu.menuPral();
		}

	}

}
