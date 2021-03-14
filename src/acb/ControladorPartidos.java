package acb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class ControladorPartidos {
    private Connection conn;
    private ControladorEquipos controladorEquipos;
    private ControladorJugadores controladorJugadores;
    public ControladorPartidos(Connection conn) {
        this.conn = conn;
        controladorEquipos = new ControladorEquipos(conn);
        controladorJugadores = new ControladorJugadores(conn);
    }

    public void crearPartit() throws SQLException, NumberFormatException {

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

    // TODO
    public void carregaEstadistiques(Connection conn) throws SQLException, NumberFormatException, IOException, ParseException {
        // TODO
        // mitjançant Prepared Statement
        // per a cada línia del fitxer estadistiques.csv
        //realitzar la inserció corresponent

        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/acb/estadistiques.csv"));
        String linea = bufferedReader.readLine();
        String[] array = new String[0];
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date parsed;


        while (linea != null) {
            System.out.println(linea);
            linea = bufferedReader.readLine();
          if (linea == null){
            break;
          }else{
              if (linea.length() > 2){
                  array = linea.split(",");
                  parsed = sdf.parse(array[2]);
                  Date data = new Date(parsed.getTime());
                  if (buscarPartido(array[0], array[1], data)){
                      actualizarEstadisticas(array);
                  }else if (!buscarPartido(array[0], array[1], data)){
                      crearEstadisticaDelPartido(array);
                  }
              }
          }

        }
    }

    public boolean buscarPartido(String equipoLocal, String equipoVisitante, Date fechaDelPartido) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs;
            rs = st.executeQuery("SELECT * FROM match_statistics where home_team ='" + equipoLocal + "' and visitor_team='" + equipoVisitante + "' and match_date='" + fechaDelPartido + "'");
            if (!rs.next()) {
                System.out.println("el partido no ha sido encontrado");
                System.out.println("creando partido");
                return false;
            } else {
                System.out.println("partido encontrado");
                return true;
            }
    }



    public void crearEstadisticaDelPartido(String[] array) throws SQLException, NumberFormatException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date parsed = null;
        parsed = sdf.parse(array[2]);
        Date data = new Date(parsed.getTime());

        String sql = "INSERT INTO match_statistics (home_team, visitor_team, match_date, player, minutes_played,points, offensive_rebounds, defensive_rebounds, assists, committed_fouls,received_fouls, free_throw_attempts, free_throw_made, two_point_attempts,two_point_made, three_point_attempts, three_point_made, blocks,blocks_against, steals, turnovers, mvp_score) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        conn.prepareStatement(sql);
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setString(1,array[0]);
        pst.setString(2,array[1]);
        pst.setDate(3, data);
        pst.setString(4, array[3]);
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

    public void actualizarEstadisticas(String[] array) throws SQLException, NumberFormatException, IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date parsed;

        parsed = sdf.parse(array[2]);
        Date data = new Date(parsed.getTime());
        String sql = "UPDATE match_statistics SET minutes_played = ?,points = ?, offensive_rebounds = ?, defensive_rebounds = ?, assists = ?, committed_fouls = ?,received_fouls = ?, free_throw_attempts = ?, free_throw_made = ?, two_point_attempts = ?,two_point_made = ?, three_point_attempts = ?, three_point_made = ?, blocks = ?,blocks_against = ?, steals = ?, turnovers = ?, mvp_score = ? WHERE home_team = ? and visitor_team = ? and match_date = ? and player = ?";
        conn.prepareStatement(sql);
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setInt(1, Integer.parseInt(array[4]));
        pst.setInt(2, Integer.parseInt(array[5]));
        pst.setInt(3, Integer.parseInt(array[6]));
        pst.setInt(4, Integer.parseInt(array[7]));
        pst.setInt(5, Integer.parseInt(array[8]));
        pst.setInt(6, Integer.parseInt(array[9]));
        pst.setInt(7, Integer.parseInt(array[10]));
        pst.setInt(8, Integer.parseInt(array[11]));
        pst.setInt(9, Integer.parseInt(array[12]));
        pst.setInt(10, Integer.parseInt(array[13]));
        pst.setInt(11, Integer.parseInt(array[14]));
        pst.setInt(12, Integer.parseInt(array[15]));
        pst.setInt(13, Integer.parseInt(array[16]));
        pst.setInt(14, Integer.parseInt(array[17]));
        pst.setInt(15, Integer.parseInt(array[18]));
        pst.setInt(16, Integer.parseInt(array[19]));
        pst.setInt(17, Integer.parseInt(array[20]));
        pst.setInt(18, Integer.parseInt(array[21]));
        pst.setString(19,array[0]);
        pst.setString(20, array[1]);
        pst.setDate(21, data);
        pst.setString(22, array[3]);
        pst.executeUpdate();
    }
}
