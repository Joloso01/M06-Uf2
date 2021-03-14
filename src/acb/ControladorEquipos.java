package acb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Scanner;

public class ControladorEquipos {
    private Connection conn;

    public ControladorEquipos(Connection conn) {
        this.conn = conn;
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

    public void afegeixJugadorAEquip(Connection conn) throws SQLException {

        ResultSet rs = null;
        Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        try {
            rs = st.executeQuery("SELECT * FROM player WHERE team_name = 'agente_libre'");

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

    public void desassignaJugadorDeEquip(Connection conn) throws SQLException {


        ResultSet rs = null;
        Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        try {
            rs = st.executeQuery("SELECT * FROM player WHERE team_name != 'agente_libre'");

            if (!rs.next()) {
                System.out.println("No hi ha jugadors pendents d'associar a equips. ");
            } else {
                do{
                    System.out.println("Nom y Cognom: " + rs.getString("first_name")+" "+rs.getString("last_name"));

                    System.out.println("quiere desasignar este jugador de un equipo? (si/no/skip)");
                    String resposta = br.readLine();

                    if (resposta.equals("si")) {
                        // actualitza el camp
                        rs.updateString("team_name","agente_libre");
                        // actualitza la fila
                        rs.updateRow();
                    }else if(resposta.equals("skip")){
                        while (rs.next()){
                        }
                    }
                }while (rs.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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


}
