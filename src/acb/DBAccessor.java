package acb;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Scanner;

public class DBAccessor {
	private String dbname;
	private String host;
	private String port;
	private String user;
	private String passwd;
	private String schema;
	Connection conn = null;

	/**
	 * Initializes the class loading the database properties file and assigns
	 * values to the instance variables.
	 * 
	 * @throws RuntimeException
	 *             Properties file could not be found.
	 */
	public void init() {
		Properties prop = new Properties();
		InputStream propStream = this.getClass().getClassLoader().getResourceAsStream("acb/db.properties");

		try {
			prop.load(propStream);
			this.host = prop.getProperty("host");
			this.port = prop.getProperty("port");
			this.dbname = prop.getProperty("dbname");
			this.schema = prop.getProperty("schema");
		} catch (IOException e) {
			String message = "ERROR: db.properties file could not be found";
			System.err.println(message);
			throw new RuntimeException(message, e);
		}
	}

	/**
	 * Obtains a {@link Connection} to the database, based on the values of the
	 * <code>db.properties</code> file.
	 * 
	 * @return DB connection or null if a problem occurred when trying to
	 *         connect.
	 */
	public Connection getConnection(Identity identity) {

		// Implement the DB connection
		String url = null;
		try {
			// Loads the driver
			Class.forName("org.postgresql.Driver");

			// Preprara connexió a la base de dades
			StringBuffer sbUrl = new StringBuffer();
			sbUrl.append("jdbc:postgresql:");
			if (host != null && !host.equals("")) {
				sbUrl.append("//").append(host);
				if (port != null && !port.equals("")) {
					sbUrl.append(":").append(port);
				}
			}
			sbUrl.append("/").append(dbname);
			url = sbUrl.toString();

			// Utilitza connexió a la base de dades
			conn = DriverManager.getConnection(url, identity.getUser(), identity.getPassword());
			conn.setAutoCommit(true);
		} catch (ClassNotFoundException e1) {
			System.err.println("ERROR: Al Carregar el driver JDBC");
			System.err.println(e1.getMessage());
		} catch (SQLException e2) {
			System.err.println("ERROR: No connectat  a la BD " + url);
			System.err.println(e2.getMessage());
		}

		// Sets the search_path
		if (conn != null) {
			Statement statement = null;
			try {
				statement = conn.createStatement();
				statement.executeUpdate("SET search_path TO " + this.schema);
				// missatge de prova: verificació
				System.out.println("OK: connectat a l'esquema " + this.schema + " de la base de dades " + url
						+ " usuari: " + user + " password:" + passwd);
				System.out.println();
				//
			} catch (SQLException e) {
				System.err.println("ERROR: Unable to set search_path");
				System.err.println(e.getMessage());
			} finally {
				try {
					statement.close();
				} catch (SQLException e) {
					System.err.println("ERROR: Closing statement");
					System.err.println(e.getMessage());
				}
			}
		}

		return conn;
	}


	public void crearJugador() throws SQLException, IOException {
		Scanner reader = new Scanner(System.in);
		System.out.println("Introdueix el codi de la licencia de la federacio");
		String  licencia = reader.nextLine();
		System.out.println("Introdueix el nom");
		String nom = reader.nextLine();
		System.out.println("Introdueix el cognom");
		String cognom = reader.nextLine();
		System.out.println("Introdueix l'any de naixement");
		String any_naixement = reader.nextLine();
		System.out.println("Introdueix el genera (M/F)");
		String genera = reader.nextLine();
		System.out.println("Introdueix la altura");
		int altura = reader.nextInt();
		System.out.println("Introdueix el nom del equip");
		reader.nextLine();
		String nom_del_equip = reader.nextLine();
		System.out.println("mvp_totals:");
		int mvp = reader.nextInt();

		String sql = "INSERT INTO player (federation_license_code, first_name, last_name, birth_date, gender, height, team_name, mvp_total) VALUES (?,?,?,?,?,?,?,?)";
		conn.prepareStatement(sql);
		PreparedStatement pst = conn.prepareStatement(sql);

		pst.setString(1,licencia);
		pst.setString(2,nom);
		pst.setString(3,cognom);
		pst.setDate(4, Date.valueOf(any_naixement));
		pst.setString(5,genera);
		pst.setInt(6,altura);
		pst.setString(7,nom_del_equip);
		pst.setInt(8,mvp);
		pst.executeUpdate();
	}

	public void crearEquip() throws SQLException, NumberFormatException {
		Scanner reader = new Scanner(System.in);
		System.out.println("Introdueix el nom del equip");
		String  nom = reader.nextLine();
		System.out.println("Introdueix el tipus de equip (Club / National Team)");
		String tipus = reader.nextLine();
		System.out.println("Introdueix el pais del equip");
		String  pais = reader.nextLine();
		System.out.println("Introdueix la ciutat del equip");
		String  ciutat = reader.nextLine();
		System.out.println("Introdueix el estadi del equip");
		String  estadi = reader.nextLine();

		String sql = "INSERT INTO team (name, type, country, city, court_name) VALUES (?,?,?,?,?)";
		conn.prepareStatement(sql);
		PreparedStatement pst = conn.prepareStatement(sql);

		pst.setString(1,nom);
		pst.setString(2,tipus);
		pst.setString(3,pais);
		pst.setString(4, ciutat);
		pst.setString(5,estadi);
		pst.executeUpdate();
	}


	public void crearPartit() throws SQLException, NumberFormatException {

		// TODO demana per consola els valors dels diferents atributs
		// d'article, excepte aquells que poden ser nuls , i realitza la
		// inserció d'un registre

		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Scanner reader = new Scanner(System.in);


		System.out.println("Introdueix el equip local ");
		String equipLocal = reader.nextLine();

		System.out.println("Introdueix el equip visitant");
		String equipVisitant = reader.nextLine();

		System.out.println("Introdueix el data del partit (yyyy-mm-dd)");
		java.sql.Date dateAr = java.sql.Date.valueOf(reader.nextLine());

		System.out.println("Introdueix la asistencia");
		int asistencia = reader.nextInt();

		System.out.println("Introdueix el mvp del partit (numero de llicencia)");
		reader.nextLine();
		String numeroLlicencia = reader.nextLine();

//		Statement statement = null;
//		statement = conn.createStatement();

		String sql = "INSERT INTO match (home_team, visitor_team, match_date, attendance, mvp_player) VALUES (?,?,?,?,?)";
		conn.prepareStatement(sql);
		PreparedStatement pst = conn.prepareStatement(sql);

		pst.setString(1,equipLocal);
		pst.setString(2,equipVisitant);
		pst.setDate(3,dateAr);
		pst.setInt(4, asistencia);
		pst.setString(5,numeroLlicencia);
		pst.executeUpdate();
	}
	
	public void afegeixJugadorAEquip(Connection conn) throws SQLException {

		ResultSet rs = null;
		Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);

		try {
			rs = st.executeQuery("SELECT * FROM player WHERE team_name IS NULL");

			if (!rs.next()) {
				System.out.println("No hi ha jugadors pendents d'associar a equips. ");
			} else {
				do{
					System.out.println("Nom y cognom: " + rs.getString("first_name")+" "+rs.getString("last_name"));
					
					System.out.println("Vol incorporar aquest jugador a un equip? (si/no)");
					String resposta = br.readLine();

					if (resposta.equals("si")) {
						// demana l'identificador de la revista
						System.out.println("Introdueix el nom del equip:");
						String  nomEquip = br.readLine();
						// actualitza el camp
						rs.updateString("team_name", nomEquip);
						// actualitza la fila
						rs.updateRow();
					}
				}while (rs.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	// TODO
	public void actualitzarTitolRevistes(Connection conn) throws SQLException {
		// TODO
		// seguint l'exemple de la funció afegeixArticleARevista:
		// definir variables locals
		// realitzar la consulta de totes les revistes
		// mentre hi hagi revistes:
		// Mostrar el títol de la revista
		// demanar si es vol canviar el seu títol
		// en cas de que la resposta sigui "si"
		// demanar el nou títol per la revista
		// actualitzar el camp
		// actualitzar la fila

		ResultSet rs = null;
		Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
		InputStreamReader isr = new InputStreamReader(System.in);
		Scanner sc = new Scanner(System.in);
		BufferedReader br = new BufferedReader(isr);

		System.out.println(" Actualizar el titulo revistas");


		try {
			st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			rs = st.executeQuery("SELECT * FROM revistes");

			if (!rs.next()) {
				System.out.println("No hi ha revistes. ");
			} else {
				 do{
					System.out.println("Titol: " + rs.getString("titol"));
					System.out.println("Quiere modificar esta revista: (si/no)");
					String esta;
					esta = sc.nextLine();
						if (esta.equals("si")){
							System.out.println("Introduzca el nuevo titulo de la revista:");
							String resposta = sc.nextLine();
							// actualitza el camp
							rs.updateString("titol", resposta);
							// actualitza la fila
							rs.updateRow();
						}


				}while (rs.next());
				 //se tiene que actualizar el next abajo porque arriba ya coje el primero.
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	// TODO
	public void desassignaArticleARevista(Connection conn) throws SQLException {

		// TODO
		// seguint l'exemple de la funció afegeixArticleARevista:
		// definir variables locals
		// sol·licitar l'identificador de la revista
		// realitzar la consulta de tots els articles que corresponen a aquesta
		// revista
		// si no hi ha articles, emetre el missatge corresponent
		// en altre cas, mentre hi hagi articles:
		// Mostrar el títol de l'article i l'identificador de la revista
		// demanar si es vol rescindir la seva incorporació a la revista
		// en cas de que la resposta sigui "si"
		// actualitzar el camp corresponent a null
		// actualitzar la fila
		// en altre cas imprimir "operació cancel·lada"

		ResultSet rs = null;
		Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);

		try {
			rs = st.executeQuery("SELECT * FROM player WHERE team_name IS NOT NULL ");

			if (!rs.next()) {
				System.out.println("No hi ha jugadors pendents d'associar a equips. ");
			} else {
				do{
					System.out.println("Nom y Cognom: " + rs.getString("first_name")+" "+rs.getString("last_name"));

					System.out.println("quiere desasignar este jugador de un equipo? (si/no)");
					String resposta = br.readLine();

					if (resposta.equals("si")) {
						// actualitza el camp
						rs.updateNull("team_name");
						// actualitza la fila
						rs.updateRow();
					}
				}while (rs.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	public void mostraJugadors() throws SQLException, IOException {
		Statement st = conn.createStatement();
		Scanner reader = new Scanner(System.in);
		ResultSet rs;

		rs = st.executeQuery("SELECT * FROM player");
		while (rs.next()) System.out.println("ID: " +rs.getString("federation_license_code") +
											"\tNom: " + rs.getString("first_name") +
										    "\tCognom: "+rs.getString("last_name")+
											"\tData de naixement: " + rs.getString("birth_date") +
											"\tGenero: " + rs.getString("gender") +
											"\tAltura: " + rs.getInt("height")+
											"\tEquip: " + rs.getString("team_name")+
											"\tMVP totales: "+ rs.getInt("mvp_total"));
		rs.close();
		st.close();
	}


	public void mostraJugadorsSenseEquip() throws SQLException, IOException {
		Statement st = conn.createStatement();
		Scanner reader = new Scanner(System.in);
		ResultSet rs;

		rs = st.executeQuery("select * from player where team_name is null;");
		while (rs.next()) System.out.println("ID: " +rs.getString("federation_license_code") +
				"\tNom: " + rs.getString("first_name") +
				"\tCognom: "+rs.getString("last_name")+
				"\tData de naixement: " + rs.getString("birth_date") +
				"\tGenero: " + rs.getString("gender") +
				"\tAltura: " + rs.getInt("height")+
				"\tEquip: " + rs.getString("team_name")+
				"\tMVP totales: "+ rs.getInt("mvp_total"));
		rs.close();
		st.close();
	}

	
	public void mostraEquipos() throws SQLException, IOException {
		Statement st = conn.createStatement();
		Scanner reader = new Scanner(System.in);
		ResultSet rs;

		rs = st.executeQuery("SELECT * FROM team");
		while (rs.next()) System.out.println("Nom: " +rs.getString("name") +
											"\tTipus: " + rs.getString("type") +
											"\tPais: " + rs.getString("country")+
											"\tCiutat: "+ rs.getString("city")+
											"\tNom del estadi: "+ rs.getString("court_name"));
		rs.close();
		st.close();
	}

	
	public void mostraRevistesArticlesAutors() throws SQLException, IOException {
		Statement st = conn.createStatement();
		Scanner reader = new Scanner(System.in);
		ResultSet rs;

		rs = st.executeQuery("SELECT a.nom, r.titol, ar.titol FROM autors a, revistes r, articles ar WHERE ar.id_autor=a.id_autor AND ar.id_revista=r.id_revista");
		while (rs.next()) System.out.println("Nom autor: " +rs.getString(1) + "\tNomRevista: " + rs.getString(2) + "\tNom article: " + rs.getString(3));
		rs.close();
		st.close();

	}

	public void sortir() throws SQLException {
		System.out.println("ADÉU!");
		conn.commit();
		conn.close();
		System.exit(0);

	}
	
	// TODO
	public void carregaJugador(Connection conn) throws SQLException, NumberFormatException, IOException {
		// TODO
		// mitjançant Prepared Statement
		// per a cada línia del fitxer estadistiques.csv
		//realitzar la inserció corresponent



		String sql = "INSERT INTO match_statistics (home_team, visitor_team, match_date, player, minutes_played, points, offensive_rebounds, defensive_rebounds, assists, committed_fouls, received_fouls, free_throw_attempts, free_throw_made, two_point_attempts, two_point_made, three_point_attempts, three_point_made, blocks, blocks_against, steals, turnovers, mvp_score) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		BufferedReader bufferedReader =new BufferedReader(new FileReader("src/acb/estadistiques.csv"));
		String linea = bufferedReader.readLine();

		while (linea != null){
			System.out.println(linea);
			linea = bufferedReader.readLine();
			String[] array;
				 array = linea.split(",");

			conn.prepareStatement(sql);
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1,array[0]);
			pst.setString(2,array[1]);
			pst.setDate(3, Date.valueOf(array[2]));
			pst.setString(4,array[3]);
			pst.setInt(5, Integer.parseInt(array[4]));
			pst.setInt(6, Integer.parseInt(array[5]));
			pst.setInt(7, Integer.parseInt(array[6]));
			pst.setInt(8, Integer.parseInt(array[7]));
			pst.setInt(9, Integer.parseInt(array[8]));
			pst.setInt(10, Integer.parseInt(array[9]));
			pst.setInt(11, Integer.parseInt(array[10]));
			pst.setInt(12, Integer.parseInt(array[11]));
			pst.setInt(13, Integer.parseInt(array[12]));
			pst.setInt(14, Integer.parseInt(array[13]));
			pst.setInt(15, Integer.parseInt(array[14]));
			pst.setInt(16, Integer.parseInt(array[15]));
			pst.setInt(17, Integer.parseInt(array[16]));
			pst.setInt(18, Integer.parseInt(array[17]));
			pst.setInt(19, Integer.parseInt(array[18]));
			pst.setInt(20, Integer.parseInt(array[19]));
			pst.setInt(21, Integer.parseInt(array[20]));
			pst.setInt(22, Integer.parseInt(array[21]));
			pst.executeUpdate();
		}
		
	}
}
