package acb;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class ControladorJugadores {
    private Connection conn;
    private ControladorEquipos controladorEquipos;
    public ControladorJugadores(Connection conn) {
        this.conn = conn;
        controladorEquipos = new ControladorEquipos(conn);
    }

    public void crearJugador() throws SQLException, IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date parsed;

        try {

            Scanner reader = new Scanner(System.in);
            System.out.println("Introdueix el codi de la licencia de la federacio");
            String  licencia = reader.nextLine();
            System.out.println("Introdueix el nom");
            String nom = reader.nextLine();
            System.out.println("Introdueix el cognom");
            String cognom = reader.nextLine();
            System.out.println("Introdueix la data de naixement (yyyy-mm-dd)");
            String any_naixement =(reader.nextLine());
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
        }catch (Exception e){
            System.out.println("Ha ocurrido un error al crear Jugador");
        }
    }

    public void mostraJugadors() throws SQLException, IOException {
        try {
            Scanner reader = new Scanner(System.in);
            Statement st = conn.createStatement();
            ResultSet rs;

            System.out.println("Escriba el nombre del equipo:");
            String nombreEquipo = reader.nextLine();
            rs = st.executeQuery("SELECT * FROM player where team_name like '"+ nombreEquipo +"'");
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
        }catch (Exception e){
            System.out.println("error al encontrar el equipo");
        }
    }

    public void mostraJugadorsSenseEquip() throws SQLException, IOException {

        Statement st = conn.createStatement();
        Scanner reader = new Scanner(System.in);
        ResultSet rs;

        rs = st.executeQuery("select * from player where team_name like 'agente_libre';");
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
}
