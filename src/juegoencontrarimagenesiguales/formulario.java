/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoencontrarimagenesiguales;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author David
 */
public class formulario extends JFrame implements ActionListener{

    JPanel panelN,panelS,panelE,panelO,panelC;
    JButton [] vectorBotones = new JButton[50];
    Integer [] vectorNumerico = new Integer[50];
    Random aleatorio = new Random();
    JButton btnInicio, btnBuscar,btnSalir;
    JLabel lbpuntos, lbSegundos;
    int numeroMuestra;
    int contSegundos=0;
    JProgressBar barraTiempo = new JProgressBar();
    int puntos=0;
    Timer temporizador;
    ActionListener actualizador;
    int contadorCoincid=0;
    int contacierto=0;
    //Cadena de conexion
    Connection conexion;
    //Ejecuta las consultas sql
    Statement stm;
    //Resultado de los select
    ResultSet rst;
    
    public formulario() {
        //Construir Frame
        setSize(1200, 700);
        setDefaultCloseOperation(3);
        setVisible(true);
        setLayout(new BorderLayout());
        setResizable(false);
        setTitle("Juego Coincidencia");
        setLocation(70, 10);
        try {
            //Establecer conecxion con el servidor
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(formulario.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Metodo que permite establecer conexion con la base de datos
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://sql7.freemysqlhosting.net/sql7260159","sql7260159","ciWdAzFMda");
        } catch (SQLException ex) {
            Logger.getLogger(formulario.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());

        }
        
        try {
            stm = conexion.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(formulario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Panel norte
        panelN = new JPanel();
        panelN.setBackground(Color.CYAN);
        add(panelN,"North");
        //Panel sur
        panelS = new JPanel();
        panelS.setBackground(Color.CYAN);
        add(panelS,"South");
        //Panel este
        panelE = new JPanel();
        panelE.setBackground(Color.CYAN);
        add(panelE,"East");
        //Panel oeste
        panelO = new JPanel();
        panelO.setBackground(Color.CYAN);
        add(panelO,"West");
        //Panel central
        panelC = new JPanel();
        panelC.setBackground(Color.PINK);
        add(panelC, "Center");
       panelC.setLayout(new GridLayout(10,5));
       //Boton inicio
       btnInicio = new JButton("Inicio");
       btnInicio.addActionListener(this);
       panelN.add(btnInicio);
       //Boton Muestra
       btnBuscar = new JButton();
       btnBuscar.setIcon(new ImageIcon(getClass().getResource("/simpsons/quien.jpg")));
       panelE.add(btnBuscar);
       //Label de puntos
       lbpuntos = new JLabel("0");
       lbpuntos.setFont(new Font("Arial",Font.BOLD,20));
       panelE.add(lbpuntos);
       lbSegundos = new JLabel("0");
       lbSegundos.setFont(new Font("Arial",Font.BOLD,20));
       panelE.add(lbSegundos);
        for (int i = 0; i < 50; i++) {
            vectorBotones[i]=new JButton();
            vectorBotones[i].setIcon(new ImageIcon(getClass().getResource("/simpsons/quien.jpg")));
            vectorBotones[i].addActionListener(this);
            panelC.add(vectorBotones[i]);
        }
        //Barra de progreso
        panelS.add(barraTiempo);
        barraTiempo.setMinimum(0);
        barraTiempo.setMaximum(30);
        barraTiempo.setBackground(Color.red);
        
        
        //Construir temporizador
        panelS.setLayout(new GridLayout(1,1));
        panelE.setLayout(new GridLayout(3,1));
        actualizador =new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               contSegundos++;
               
               barraTiempo.setValue(contSegundos);
               lbSegundos.setText(String.valueOf(contSegundos));
               if(contSegundos==30){
                   JOptionPane.showMessageDialog(null, "Se acabó el tiempo", "Juegote", JOptionPane.INFORMATION_MESSAGE);
                   insertarRanking();    
                   puntos=0;
                   contSegundos=0;
                   temporizador.restart();
               }
            }

            
        };
        temporizador = new Timer(1000, actualizador);
        
        
        
        revalidate();
        repaint();
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource()==btnInicio){
            generarBotones();
            temporizador.start();
            contacierto=0;
        }
      
           
            for (int i = 0; i < vectorBotones.length; i++) {
             if(contadorCoincid==contacierto){
                 
                 
             reiniciar();
             generarBotones();
       
       }
       else{
            if(e.getSource()==vectorBotones[i]){
                if(numeroMuestra==vectorNumerico[i]){
                    puntos++;
                    vectorBotones[i].setEnabled(false);
                    contacierto++;
                }
                else{
                    puntos--;
                }   
                lbpuntos.setText(String.valueOf(puntos));
            }
        }
       }
    }

    private void generarBotones() {
         reiniciar();
        lbpuntos.setText(String.valueOf(puntos));
        numeroMuestra=aleatorio.nextInt(20);
        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/simpsons/"+numeroMuestra+".jpg")));
         for (int i=0;i<vectorBotones.length;i++){
       //un número aleatorio para seleccionar la imagen
       int numero=aleatorio.nextInt(20);
       vectorBotones[i]=new JButton(new ImageIcon(getClass().getResource("/simpsons/"+numero+".jpg")));
       vectorBotones[i].addActionListener(this);
       //cargar el icono en el botón a partir del número
       
      //agregar al panel
       panelC.add(vectorBotones[i]);
       vectorNumerico[i]=numero;
       if(numeroMuestra==numero){
           contadorCoincid++;
       }
   }
         
    revalidate();
    repaint();
        
    }

    private void reiniciar() {
        
        panelC.removeAll();
        
        contacierto=0;
        contadorCoincid=0;
        
        
        
    }
    private void insertarRanking() {
        String nombreJugador = JOptionPane.showInputDialog(this,"Teclea el nombre de jugador");
        
        String cadenaSql="Insert into ranking values('"+nombreJugador+"',"+puntos+")";
        //String cadenaSql2="Insert into ranking values(?,?)";
        
        try {
            stm.executeUpdate(cadenaSql);
        } catch (SQLException ex) {
            Logger.getLogger(formulario.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
    }
        
}
