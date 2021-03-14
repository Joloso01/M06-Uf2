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
		ControladorPartidos partidos = new ControladorPartidos(connection);
		ControladorJugadores jugadores = new ControladorJugadores(connection);
		ControladorEquipos equipos = new ControladorEquipos(connection);

		option = menu.menuPral();
		while (option > 0 && option < 12) {
			switch (option) {
			case 1:
				equipos.mostraEquipos();
				break;

			case 2:
				jugadores.mostraJugadors();
				break;

			case 3:
				equipos.crearEquip();
				break;

			case 4:
				jugadores.crearJugador();
				break;

			case 5:
				partidos.crearPartit();
				break;

			case 6:
				jugadores.mostraJugadorsSenseEquip();
				break;

			case 7:
				equipos.afegeixJugadorAEquip(connection);
				break;

			case 8:
				equipos.desassignaJugadorDeEquip(connection);
				break;

			case 9:
				partidos.carregaEstadistiques(connection);
				break;

			case 10:
				dbaccessor.sortir();
				break;


			default:
				System.out.println("Introdueixi una de les opcions anteriors");
				break;

			}
			option = menu.menuPral();
		}

	}

}
