import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Vector;

public class GraphicInterface extends JFrame {

    private Container container; // ventana que posee componentes tipo swing
    private JButton jButton; // Boton continuar
    private JButton jButton1; // Boton empezar simulacion
    private JButton jButton2; // Boton reiniciar parametros
    //Etiquetas de estado actual
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel13;
    private JLabel jLabel14;
    private JLabel jLabel15;
    private JLabel jLabel16;
    private JLabel jLabel17;

    private JPanel jPanel; //Panel que pide nombre
    private JPanel jPanel1; //Panel que pide parametros
    private JPanel jPanel2; //Panel que muestra la lista de eventos
    private JPanel jPanel3; //Panel que muestra los servidores que hay, el reloj y el evento actual
    private JPanel jPanel4; //Panel que muestra datos mientras se corre la simulacion
    private JPanel jPanel5; //Panel que muestra las estadisticas al final de la corrida queryProcessingServers
    private JPanel jPanel6; //Panel que muestra las estadisticas al final de todas las corridas
    private JPanel window; //Panel para ingresar nombre y pulsar boton

    private JCheckBox jCheckBox; //El usuario marca si desea correr en modo lento
    private JTextField jTextField; //nombre
    private JTextField jTextField1; //connectionsServer conexiones
    private JTextField jTextField2; //timeOut
    private JTextField jTextField3; //queryProcessingServers conexiones
    private JTextField jTextField4; //transactionsServers conexiones
    private JTextField jTextField5; //executorServers conexiones
    private JTextField jTextField6; //tiempo de la simulacion
    private JTextField jTextField7; //segundos entre cada evento
    private JTextField jTextField8; //Cantidad de corridas

    private JList jList; //Lista
    private JList jList1;
    private JList jList2;
    private DefaultListModel defaultListModel; //Lista de eventos
    private DefaultListModel defaultListModel1;
    private DefaultListModel defaultListModel2;

    private JScrollPane jScrollPane; //barra de lista de eventos
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;


    private Simulation simulation;
    private Double timeOut;
    private int connectionsServer;
    private int queryProcessingServers;
    private int transactionsServers;
    private int executorServers;
    private Double simulationTime;
    private int seconds; //segundos entre cada evento
    private int totalCounter; //total de corridas
    private int counter; //numero corrida actual
    private Statistics statistics; //estadisticas de la corrida actual
    private Vector<Statistics> statisticsVector; //vector de estadisticas
    private Statistics averageStatistics; //promedio de estadisticas
    private int index;
    private Timer timer;

    public GraphicInterface() {
        super("Proyecto I.O. (Hecho por: Kevin Leon, Renato Mainieri, Diego Mora)");
        setFont(new Font("Comic Sans", Font.ITALIC, 12));//cambia el fondo del titulo
        setSize(getMaximumSize()); //Tamano maximo de la pantalla
        setResizable(false); //No se puede cambiar el tamano en ejecucion
        //Inicializacion de variables
        connectionsServer = 0;
        queryProcessingServers = 0;
        transactionsServers = 0;
        executorServers = 0;
        simulationTime = 0.0;
        totalCounter = 10;
        counter = 0;
        timeOut = 0.0;
        index = 0;
        statisticsVector = new Vector<Statistics>();

        jButton = new JButton("Aceptar");
        jButton1 = new JButton("Primera Corrida");
        jButton2 = new JButton("Reiniciar Simulacion");

        //Se cambia la letra de los botones
        jButton.setFont(new Font("Comic Sans", Font.ITALIC, 14));
        jButton1.setFont(new Font("Comic Sans", Font.ITALIC, 14));
        jButton2.setFont(new Font("Comic Sans", Font.ITALIC, 14));

        jTextField = new JTextField(10);
        jTextField1 = new JTextField(10);
        jTextField2 = new JTextField(10);
        jTextField3 = new JTextField(10);
        jTextField4 = new JTextField(10);
        jTextField5 = new JTextField(10);
        jTextField6 = new JTextField(10);
        jTextField7 = new JTextField(10);
        jTextField8 = new JTextField(10);

        jCheckBox = new JCheckBox("Slow Motion");
        jCheckBox.setFont(new Font("Comic Sans", Font.ITALIC, 12));

        jPanel = new JPanel();
        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        jPanel3 = new JPanel();
        jPanel4 = new JPanel();
        jPanel5 = new JPanel();
        jPanel6 = new JPanel();
        window = new JPanelImage("/database-management.jpg");

        defaultListModel = new DefaultListModel();
        defaultListModel1 = new DefaultListModel();
        defaultListModel2 = new DefaultListModel();

        jList = new JList(defaultListModel);
        jList1 = new JList(defaultListModel1);
        jList2 = new JList(defaultListModel2);


        jScrollPane = new JScrollPane(jList);
        jScrollPane1 = new JScrollPane(jList1);
        jScrollPane2 = new JScrollPane(jList2);


        //Etiquetas de datos mientras se corre la simulacion
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        jLabel9 = new JLabel();
        jLabel10 = new JLabel();
        jLabel11 = new JLabel();
        jLabel12 = new JLabel();
        jLabel13 = new JLabel();
        jLabel14 = new JLabel();
        jLabel15 = new JLabel();
        jLabel16 = new JLabel();
        jLabel17 = new JLabel();


        // se cambia el layout de los paneles
        jPanel.setLayout(new GridLayout(1, 2, 5, 5));
        jPanel1.setLayout(new GridLayout(10, 2, 0, 0));
        jPanel3.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 14));
        jPanel4.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 12));

        container = getContentPane(); //se guarda la ventana en un contenedor
        container.setBackground(Color.LIGHT_GRAY); // se cambia el color de fondo
        setVisible(true); //hace visible la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE); // si el usuario presiona x se cierra el programa
    }

    //Metodo que inicia la interfaz y agrega acciones a los componentes
    public void startSimulation() {
        jPanel.add(new JLabel("Nombre"));
        jPanel.add(jTextField);
        window.setLayout(new FlowLayout(FlowLayout.CENTER, 0, (int) (getHeight() / 3.6)));
        window.add(jPanel);
        window.add(jButton);
        window.setPreferredSize(new Dimension(getWidth(), getHeight()));
        container.add(window); // Agrega el panel con el nombre y boton al contenedor
        jTextField.requestFocus(true); //enfoca el texfield del nombre
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                requestParameters();
            }
        });
        jButton.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    requestParameters();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        jTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    requestParameters();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateInterface();
            }
        });
        jTextField8.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    updateInterface();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        jCheckBox.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    jCheckBox.setSelected(true);
                    updateInterface();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        jButton1.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    updateInterface();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTextField1.setText("");
                jTextField2.setText("");
                jTextField3.setText("");
                jTextField4.setText("");
                jTextField5.setText("");
                jTextField6.setText("");
                jTextField7.setText("");
                jTextField8.setText("");
                counter = 0;
                jButton1.setVisible(true);
                jButton1.setText("Primera Corrida");
                statisticsVector.removeAllElements();
                container.removeAll();
                jPanel1.removeAll();
                jPanel2.removeAll();
                jPanel3.removeAll();
                jPanel4.removeAll();
                jPanel5.removeAll();
                jPanel6.removeAll();
                requestParameters();
                SwingUtilities.updateComponentTreeUI(container);
            }
        });
    }

    //Metodo que le pide al usuario los parametros
    private void requestParameters() {
        container.setLayout(new FlowLayout(FlowLayout.LEFT)); //agrega empezando a la izquierda
        window.setVisible(false); //oculta el primer panel
        //Pide los parametros al usuario
        jPanel1.add(new JLabel("Simulation of a DBMS"));
        jPanel1.add(jButton2);
        jPanel1.add(new JLabel("Servidores en Connections Module:"));
        jPanel1.add(jTextField1);
        jPanel1.add(new JLabel("TimeOut de las conexiones:"));
        jPanel1.add(jTextField2);
        jPanel1.add(new JLabel("Servidores en QueryProcesing Module:"));
        jPanel1.add(jTextField3);
        jPanel1.add(new JLabel("Servidores en Transactions Module:"));
        jPanel1.add(jTextField4);
        jPanel1.add(new JLabel("Servidores en Executor Module:"));
        jPanel1.add(jTextField5);
        jPanel1.add(new JLabel("Tiempo de la Simulacion:"));
        jPanel1.add(jTextField6);
        jPanel1.add(new JLabel("Segundos entre cada evento"));
        jPanel1.add(jTextField7);
        jPanel1.add(new JLabel("Cantidad de corridas"));
        jPanel1.add(jTextField8);
        jPanel1.add(jCheckBox);
        jPanel1.add(jButton1);

        setTitle("Bienvenido " + jTextField.getText() + ", digite los parametros de su simulacion"); //cambia el titulo
        //cambia el tamano de los paneles y los agrega al contenedor
        jPanel1.setPreferredSize(new Dimension((int) (getWidth() / 4.1), (int) (getHeight() / 2.1)));
        jPanel2.setPreferredSize(new Dimension((int) (getWidth() / 4.1), (int) (getHeight() / 2.1)));
        jPanel3.setPreferredSize(new Dimension((int) (getWidth() / 4.1), (int) (getHeight() / 2.1)));
        jPanel4.setPreferredSize(new Dimension((int) (getWidth() / 4.1), (int) (getHeight() / 2.1)));
        jPanel5.setPreferredSize(new Dimension((int) (getWidth() / 2.04), (int) (getHeight() / 2.1)));
        jPanel6.setPreferredSize(new Dimension((int) (getWidth() / 2.04), (int) (getHeight() / 2.1)));

        container.add(jPanel1, FlowLayout.LEFT);
        container.add(jPanel2, FlowLayout.CENTER);
        container.add(jPanel3, FlowLayout.CENTER);
        container.add(jPanel4, FlowLayout.RIGHT);
        container.add(jPanel5, null);
        container.add(jPanel6, null);

        jList.setVisibleRowCount((int) (getHeight() / 40)); //filas que se ven de la lista de eventos
        jList1.setVisibleRowCount((int) (getHeight() / 40)); //filas que se ven de la lista de eventos
        jList2.setVisibleRowCount((int) (getHeight() / 40)); //filas que se ven de la lista de eventos


    }

    //Metodo que obtiene los parametros dados por el usuario
    public void setParameters() {
        timeOut = Double.parseDouble(jTextField2.getText());
        connectionsServer = Integer.parseInt(jTextField1.getText());
        queryProcessingServers = Integer.parseInt(jTextField3.getText());
        transactionsServers = Integer.parseInt(jTextField4.getText());
        executorServers = Integer.parseInt(jTextField5.getText());
        simulationTime = Double.parseDouble(jTextField6.getText());
        seconds = Integer.parseInt(jTextField7.getText());
        totalCounter = Integer.parseInt(jTextField8.getText());

        //Agrega los Datos iniciales
        jPanel3.add(new JLabel("Datos iniciales:"));
        jPanel3.add(new JLabel("Total de servidores en Connections Module: " + connectionsServer));
        jPanel3.add(new JLabel("Total de servidores en Process Module: " + 1));
        jPanel3.add(new JLabel("Total de servidores en QueryProcessing Module: " + queryProcessingServers));
        jPanel3.add(new JLabel("Total de servidores en Transactions Module: " + transactionsServers));
        jPanel3.add(new JLabel("Total de servidores en Executor Module: " + executorServers));
    }

    //metodo que actualiza las estadisticas y la interfaz la simulacion
    public void updateInterface() {
        if (counter < totalCounter) { //si aun quedan corridas
            ++counter;
            if (counter == 1) { //Guarda los parametros solo la primera vez
                setParameters();
            }
            jButton1.setText("Siguiente Corrida(" + (counter + 1) + ")"); //actualiza el boton que deja al usuario correr la simulacion
            if (counter == totalCounter - 1) { //actualiza el boton en caso de que sea la ultima  corrida
                jButton1.setText("Ultima Corrida");
            } else if (counter == totalCounter) {
                jButton1.setText("Estadisticas Totales");
            }
            //Borra lo que hay en los paneles
            jPanel2.removeAll();
            jPanel4.removeAll();
            jPanel5.removeAll();
            jPanel6.removeAll();

            defaultListModel.removeAllElements();
            defaultListModel1.removeAllElements();
            defaultListModel2.removeAllElements();

            jPanel2.add(jScrollPane);

            jPanel4.add(new JLabel("Datos mientras se corre la simulacion numero " + counter + " :"));

            simulation = new Simulation(connectionsServer, timeOut, queryProcessingServers, transactionsServers, executorServers);
            if (jCheckBox.isSelected()) {
                index = 0;
                this.addLabel();
                simulation.generateFirstArrival();
                timer = new Timer(seconds * 1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (simulation.getClock() < simulationTime) {
                            delaySimulation();
                        } else {
                            timer.stop();
                            timer.setRepeats(false);
                            statistics = new Statistics(simulation.getQueryStatistics(), simulation.getConnectionsModule().getModuleStatistics(),
                                    simulation.getProcessModule().getModuleStatistics(), simulation.getQueryProcessingModule().getModuleStatistics()
                                    , simulation.getTransactionsModule().getModuleStatistics(), simulation.getExecutorModule().getModuleStatistics(), simulation.getClock());
                            showActualStatistics();
                            statisticsVector.add(statistics);
                            SwingUtilities.updateComponentTreeUI(container);
                        }
                    }
                });
                timer.start();
                timer.setRepeats(true);
            } else {
                this.addLabel();
                this.fastSimulation();
                this.showActualStatistics(); //Imprime las estadisticas actuales
                statisticsVector.add(statistics); //Agrega las estadisticas actuales al vector
            }
        }

        //Si es la ultima corrida muestra el promedio
        else if (counter == totalCounter) {
            jPanel6.add(jScrollPane2);
            SwingUtilities.updateComponentTreeUI(container);
            GeneralStatistics generalStatistics = new GeneralStatistics(statisticsVector);
            averageStatistics = generalStatistics.getGeneralStatisitcs();
            this.showLastStatistics(averageStatistics);
            ++counter;
            jButton1.setText("Simulacion Finalizada");
            statisticsVector.add(averageStatistics);
            JOptionPane.showMessageDialog(null, "Ubicación del archivo index.html: \\Simulacion Grupo 2\\statistics");
            HTMLGenerator htmlGenerator = new HTMLGenerator(statisticsVector, connectionsServer, timeOut, queryProcessingServers, transactionsServers,
                    executorServers, totalCounter);
            try {
                htmlGenerator.generateHTML();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            ++counter;
        } else if (counter > totalCounter) {
            JOptionPane.showMessageDialog(null, "Esta intentando exceder el numero total de corridas definido, si desea empezar otra simulacion presione Reiniciar Simulacion'");
            jButton1.setVisible(false);
        }
    }


    //Corre la  simulacion sin delay
    public void fastSimulation() {
        int index = 0;
        simulation.generateFirstArrival();
        while (simulation.getClock() < simulationTime) {
            simulation.processEvent();
            defaultListModel.add(index, simulation.getEvent().getEventType().toString());
            ++index;
        }
        jLabel1.setText("Reloj del Sistema: " + simulation.getClock());
        jLabel2.setText("Evento Actual: " + simulation.getEvent().getEventType().toString());
        jLabel3.setText("Servidores disponibles en Connections Module: " + simulation.getConnectionsModule().getAvailableServers());
        jLabel4.setText("Servidores disponibles en Process Module: " + simulation.getProcessModule().getAvailableServers());
        jLabel5.setText("Servidores disponibles en QueryProcessing Module: " + simulation.getQueryProcessingModule().getAvailableServers());
        jLabel6.setText("Servidores disponibles en Transactions Module: " + simulation.getTransactionsModule().getAvailableServers());
        jLabel7.setText("Servidores disponibles en Executor Module: " + simulation.getExecutorModule().getAvailableServers());
        jLabel8.setText("Cantidad de consultas descartadas: " + simulation.getConnectionsModule().getQueryStatistics().getDiscardedConnections());
        jLabel9.setText("Longitud de la cola en Process Module: " + simulation.getProcessModule().getModuleStatistics().getActualQueueSize());
        jLabel10.setText("Longitud de la cola en QueryProcessing Module: " + simulation.getQueryProcessingModule().getModuleStatistics().getActualQueueSize());
        jLabel11.setText("Longitud de la cola en Transactions Module: " + simulation.getTransactionsModule().getModuleStatistics().getActualQueueSize());
        jLabel12.setText("Longitud de la cola en Executor Module: " + simulation.getExecutorModule().getModuleStatistics().getActualQueueSize());
        jLabel13.setText("Clientes atendidos en Connections Module: " + simulation.getConnectionsModule().getModuleStatistics().getServedClients());
        jLabel14.setText("Clientes atendidos en Process Module: " + simulation.getProcessModule().getModuleStatistics().getServedClients());
        jLabel15.setText("Clientes atendidos en QueryProcessing Module: " + simulation.getQueryProcessingModule().getModuleStatistics().getServedClients());
        jLabel16.setText("Clientes atendidos en Transactions Module: " + simulation.getTransactionsModule().getModuleStatistics().getServedClients());
        jLabel17.setText("Clientes atendidos en Executor Module: " + simulation.getExecutorModule().getModuleStatistics().getServedClients());
        statistics = new Statistics(simulation.getQueryStatistics(), simulation.getConnectionsModule().getModuleStatistics(),
                simulation.getProcessModule().getModuleStatistics(), simulation.getQueryProcessingModule().getModuleStatistics()
                , simulation.getTransactionsModule().getModuleStatistics(), simulation.getExecutorModule().getModuleStatistics(), simulation.getClock());
    }

    //corre la simulacion con delaay
    public void delaySimulation() {
        simulation.processEvent();
        defaultListModel.add(index, simulation.getEvent().getEventType().toString());
        jLabel1.setText("Reloj del Sistema: " + simulation.getClock());
        jLabel2.setText("Evento Actual: " + simulation.getEvent().getEventType().toString());
        jLabel3.setText("Servidores disponibles en Connections Module: " + simulation.getConnectionsModule().getAvailableServers());
        jLabel4.setText("Servidores disponibles en Process Module: " + simulation.getProcessModule().getAvailableServers());
        jLabel5.setText("Servidores disponibles en QueryProcessing Module: " + simulation.getQueryProcessingModule().getAvailableServers());
        jLabel6.setText("Servidores disponibles en Transactions Module: " + simulation.getTransactionsModule().getAvailableServers());
        jLabel7.setText("Servidores disponibles en Executor Module: " + simulation.getExecutorModule().getAvailableServers());
        jLabel8.setText("Cantidad de consultas descartadas: " + simulation.getConnectionsModule().getQueryStatistics().getDiscardedConnections());
        jLabel9.setText("Longitud de la cola en Process Module: " + simulation.getProcessModule().getModuleStatistics().getActualQueueSize());
        jLabel10.setText("Longitud de la cola en QueryProcessing Module: " + simulation.getQueryProcessingModule().getModuleStatistics().getActualQueueSize());
        jLabel11.setText("Longitud de la cola en Transactions Module: " + simulation.getTransactionsModule().getModuleStatistics().getActualQueueSize());
        jLabel12.setText("Longitud de la cola en Executor Module: " + simulation.getExecutorModule().getModuleStatistics().getActualQueueSize());
        jLabel13.setText("Clientes atendidos en Connections Module: " + simulation.getConnectionsModule().getModuleStatistics().getServedClients());
        jLabel14.setText("Clientes atendidos en Process Module: " + simulation.getProcessModule().getModuleStatistics().getServedClients());
        jLabel15.setText("Clientes atendidos en QueryProcessing Module: " + simulation.getQueryProcessingModule().getModuleStatistics().getServedClients());
        jLabel16.setText("Clientes atendidos en Transactions Module: " + simulation.getTransactionsModule().getModuleStatistics().getServedClients());
        jLabel17.setText("Clientes atendidos en Executor Module: " + simulation.getExecutorModule().getModuleStatistics().getServedClients());
        ++index;
    }

    public void showActualStatistics() {
        jPanel5.add(jScrollPane1);
        SwingUtilities.updateComponentTreeUI(container);
        statistics.getConnectionsModuleStatistics().getArrivalRate(simulation.getClock());
        statistics.getProcessModuleStatistics().getArrivalRate(simulation.getClock());
        statistics.getQueryProcessingStatistics().getArrivalRate(simulation.getClock());
        statistics.getTransactionsModuleStatistics().getArrivalRate(simulation.getClock());
        statistics.getExecutorModuleStatistics().getArrivalRate(simulation.getClock());
        defaultListModel1.add(0, "Estadisticas obtenidas al final de la corrida numero: " + counter);
        defaultListModel1.add(1, "Consultas descartadas por el servidor: " + statistics.getQueryStatistics().getDiscardedConnections());
        defaultListModel1.add(2, "Tamano promedio de la cola en Process Module: " + statistics.getProcessModuleStatistics().generateAverageQueueSize());
        defaultListModel1.add(3, "Tamano promedio de la cola en QueryProcessing Module: " + statistics.getQueryProcessingStatistics().generateAverageQueueSize());
        defaultListModel1.add(4, "Tamano promedio de la cola en Transactions Module: " + statistics.getTransactionsModuleStatistics().generateAverageQueueSize());
        defaultListModel1.add(5, "Tamano promedio de la cola en Executor Module: " + statistics.getExecutorModuleStatistics().generateAverageQueueSize());
        defaultListModel1.add(6, "Tiempo promedio de vida de una conexion: " + statistics.getQueryStatistics().getTotalAverageConnectionLifeTime());
        defaultListModel1.add(7, "Tiempo que el " + statistics.getConnectionsModuleStatistics().getModuleType().toString() + " pasa ocioso: " + statistics.getConnectionsModuleStatistics().getIdleTime());
        defaultListModel1.add(8, "Tiempo que     el " + statistics.getProcessModuleStatistics().getModuleType().toString() + " pasa ocioso: " + statistics.getProcessModuleStatistics().getIdleTime());
        defaultListModel1.add(9, "Tiempo que el " + statistics.getQueryProcessingStatistics().getModuleType().toString() + " pasa ocioso: " + statistics.getQueryProcessingStatistics().getIdleTime());
        defaultListModel1.add(10, "Tiempo que el " + statistics.getTransactionsModuleStatistics().getModuleType().toString() + " pasa ocioso: " + statistics.getTransactionsModuleStatistics().getIdleTime());
        defaultListModel1.add(11, "Tiempo que el " + statistics.getExecutorModuleStatistics().getModuleType().toString() + " pasa ocioso: " + statistics.getExecutorModuleStatistics().getIdleTime());
        defaultListModel1.add(12, "Tiempo promedio en " + statistics.getConnectionsModuleStatistics().getModuleType() + " de una DDL: " + statistics.getConnectionsModuleStatistics().getAverageTimeInModulePerSentence(1));
        defaultListModel1.add(13, "Tiempo promedio en " + statistics.getProcessModuleStatistics().getModuleType() + " de una DDL: " + statistics.getProcessModuleStatistics().getAverageTimeInModulePerSentence(1));
        defaultListModel1.add(14, "Tiempo promedio en " + statistics.getQueryProcessingStatistics().getModuleType() + " de una DDL: " + statistics.getQueryProcessingStatistics().getAverageTimeInModulePerSentence(1));
        defaultListModel1.add(15, "Tiempo promedio en " + statistics.getTransactionsModuleStatistics().getModuleType() + " de una DDL: " + statistics.getTransactionsModuleStatistics().getAverageTimeInModulePerSentence(1));
        defaultListModel1.add(16, "Tiempo promedio en " + statistics.getExecutorModuleStatistics().getModuleType() + " de una DDL: " + statistics.getExecutorModuleStatistics().getAverageTimeInModulePerSentence(1));
        defaultListModel1.add(17, "Tiempo promedio en " + statistics.getConnectionsModuleStatistics().getModuleType() + " de una Update: " + statistics.getConnectionsModuleStatistics().getAverageTimeInModulePerSentence(2));
        defaultListModel1.add(18, "Tiempo promedio en " + statistics.getProcessModuleStatistics().getModuleType() + " de una Update: " + statistics.getProcessModuleStatistics().getAverageTimeInModulePerSentence(2));
        defaultListModel1.add(19, "Tiempo promedio en " + statistics.getQueryProcessingStatistics().getModuleType() + " de una Update: " + statistics.getQueryProcessingStatistics().getAverageTimeInModulePerSentence(2));
        defaultListModel1.add(20, "Tiempo promedio en " + statistics.getTransactionsModuleStatistics().getModuleType() + " de una Update: " + statistics.getTransactionsModuleStatistics().getAverageTimeInModulePerSentence(2));
        defaultListModel1.add(21, "Tiempo promedio en " + statistics.getExecutorModuleStatistics().getModuleType() + " de una Update: " + statistics.getExecutorModuleStatistics().getAverageTimeInModulePerSentence(2));
        defaultListModel1.add(22, "Tiempo promedio en " + statistics.getConnectionsModuleStatistics().getModuleType() + " de una Join: " + statistics.getConnectionsModuleStatistics().getAverageTimeInModulePerSentence(3));
        defaultListModel1.add(23, "Tiempo promedio en " + statistics.getProcessModuleStatistics().getModuleType() + " de una Join: " + statistics.getProcessModuleStatistics().getAverageTimeInModulePerSentence(3));
        defaultListModel1.add(24, "Tiempo promedio en " + statistics.getQueryProcessingStatistics().getModuleType() + " de una Join: " + statistics.getQueryProcessingStatistics().getAverageTimeInModulePerSentence(3));
        defaultListModel1.add(25, "Tiempo promedio en " + statistics.getTransactionsModuleStatistics().getModuleType() + " de una Join: " + statistics.getTransactionsModuleStatistics().getAverageTimeInModulePerSentence(3));
        defaultListModel1.add(26, "Tiempo promedio en " + statistics.getExecutorModuleStatistics().getModuleType() + " de una Join: " + statistics.getExecutorModuleStatistics().getAverageTimeInModulePerSentence(3));
        defaultListModel1.add(27, "Tiempo promedio en " + statistics.getConnectionsModuleStatistics().getModuleType() + " de una Select: " + statistics.getConnectionsModuleStatistics().getAverageTimeInModulePerSentence(4));
        defaultListModel1.add(28, "Tiempo promedio en " + statistics.getProcessModuleStatistics().getModuleType() + " de una Select: " + statistics.getProcessModuleStatistics().getAverageTimeInModulePerSentence(4));
        defaultListModel1.add(29, "Tiempo promedio en " + statistics.getQueryProcessingStatistics().getModuleType() + " de una Select: " + statistics.getQueryProcessingStatistics().getAverageTimeInModulePerSentence(4));
        defaultListModel1.add(30, "Tiempo promedio en " + statistics.getTransactionsModuleStatistics().getModuleType() + " de una Select: " + statistics.getTransactionsModuleStatistics().getAverageTimeInModulePerSentence(4));
        defaultListModel1.add(31, "Tiempo promedio en " + statistics.getExecutorModuleStatistics().getModuleType() + " de una Select: " + statistics.getExecutorModuleStatistics().getAverageTimeInModulePerSentence(4));
        defaultListModel1.add(32, "Cantidad de consultas que hicieron timeOut: " + simulation.getTotalKilledQueries());
    }

    public void showLastStatistics(Statistics averageStatistics) {
        averageStatistics.getConnectionsModuleStatistics().getArrivalRate(simulation.getClock());
        averageStatistics.getProcessModuleStatistics().getArrivalRate(simulation.getClock());
        averageStatistics.getQueryProcessingStatistics().getArrivalRate(simulation.getClock());
        averageStatistics.getTransactionsModuleStatistics().getArrivalRate(simulation.getClock());
        averageStatistics.getExecutorModuleStatistics().getArrivalRate(simulation.getClock());
        defaultListModel2.add(0, "Estadisticas en promedio despues de las " + (counter) + " corridas:");
        defaultListModel2.add(1, "Consultas descartadas por el servidor: " + averageStatistics.getQueryStatistics().getDiscardedConnections());
        defaultListModel2.add(2, "Tamano promedio de la cola en Process Module: " + averageStatistics.getProcessModuleStatistics().generateAverageQueueSize());
        defaultListModel2.add(3, "Tamano promedio de la cola en QueryProcessing Module: " + averageStatistics.getQueryProcessingStatistics().generateAverageQueueSize());
        defaultListModel2.add(4, "Tamano promedio de la cola en Transactions Module: " + averageStatistics.getTransactionsModuleStatistics().generateAverageQueueSize());
        defaultListModel2.add(5, "Tamano promedio de la cola en Executor Module: " + averageStatistics.getExecutorModuleStatistics().generateAverageQueueSize());
        defaultListModel2.add(6, "Tiempo promedio de vida de una conexion: " + averageStatistics.getQueryStatistics().getTotalAverageConnectionLifeTime());
        defaultListModel2.add(7, "Tiempo que el " + averageStatistics.getConnectionsModuleStatistics().getModuleType().toString() + " pasa ocioso: " + averageStatistics.getConnectionsModuleStatistics().getIdleTime());
        defaultListModel2.add(8, "Tiempo que el " + averageStatistics.getProcessModuleStatistics().getModuleType().toString() + " pasa ocioso: " + averageStatistics.getProcessModuleStatistics().getIdleTime());
        defaultListModel2.add(9, "Tiempo que el " + averageStatistics.getQueryProcessingStatistics().getModuleType().toString() + " pasa ocioso: " + averageStatistics.getQueryProcessingStatistics().getIdleTime());
        defaultListModel2.add(10, "Tiempo que el " + averageStatistics.getTransactionsModuleStatistics().getModuleType().toString() + " pasa ocioso: " + averageStatistics.getTransactionsModuleStatistics().getIdleTime());
        defaultListModel2.add(11, "Tiempo que el " + averageStatistics.getExecutorModuleStatistics().getModuleType().toString() + " pasa ocioso: " + averageStatistics.getExecutorModuleStatistics().getIdleTime());
        defaultListModel2.add(12, "Tiempo promedio en " + averageStatistics.getConnectionsModuleStatistics().getModuleType() + " de una DDL: " + averageStatistics.getConnectionsModuleStatistics().getAverageTimeInModulePerSentence(1));
        defaultListModel2.add(13, "Tiempo promedio en " + averageStatistics.getProcessModuleStatistics().getModuleType() + " de una DDL: " + averageStatistics.getProcessModuleStatistics().getAverageTimeInModulePerSentence(1));
        defaultListModel2.add(14, "Tiempo promedio en " + averageStatistics.getQueryProcessingStatistics().getModuleType() + " de una DDL: " + averageStatistics.getQueryProcessingStatistics().getAverageTimeInModulePerSentence(1));
        defaultListModel2.add(15, "Tiempo promedio en " + averageStatistics.getTransactionsModuleStatistics().getModuleType() + " de una DDL: " + averageStatistics.getTransactionsModuleStatistics().getAverageTimeInModulePerSentence(1));
        defaultListModel2.add(16, "Tiempo promedio en " + averageStatistics.getExecutorModuleStatistics().getModuleType() + " de una DDL: " + averageStatistics.getExecutorModuleStatistics().getAverageTimeInModulePerSentence(1));
        defaultListModel2.add(17, "Tiempo promedio en " + averageStatistics.getConnectionsModuleStatistics().getModuleType() + " de una Update: " + averageStatistics.getConnectionsModuleStatistics().getAverageTimeInModulePerSentence(2));
        defaultListModel2.add(18, "Tiempo promedio en " + averageStatistics.getProcessModuleStatistics().getModuleType() + " de una Update: " + averageStatistics.getProcessModuleStatistics().getAverageTimeInModulePerSentence(2));
        defaultListModel2.add(19, "Tiempo promedio en " + averageStatistics.getQueryProcessingStatistics().getModuleType() + " de una Update: " + averageStatistics.getQueryProcessingStatistics().getAverageTimeInModulePerSentence(2));
        defaultListModel2.add(20, "Tiempo promedio en " + averageStatistics.getTransactionsModuleStatistics().getModuleType() + " de una Update: " + averageStatistics.getTransactionsModuleStatistics().getAverageTimeInModulePerSentence(2));
        defaultListModel2.add(21, "Tiempo promedio en " + averageStatistics.getExecutorModuleStatistics().getModuleType() + " de una Update: " + averageStatistics.getExecutorModuleStatistics().getAverageTimeInModulePerSentence(2));
        defaultListModel2.add(22, "Tiempo promedio en " + averageStatistics.getConnectionsModuleStatistics().getModuleType() + " de una Join: " + averageStatistics.getConnectionsModuleStatistics().getAverageTimeInModulePerSentence(3));
        defaultListModel2.add(23, "Tiempo promedio en " + averageStatistics.getProcessModuleStatistics().getModuleType() + " de una Join:" + averageStatistics.getProcessModuleStatistics().getAverageTimeInModulePerSentence(3));
        defaultListModel2.add(24, "Tiempo promedio en " + averageStatistics.getQueryProcessingStatistics().getModuleType() + " de una Join: " + averageStatistics.getQueryProcessingStatistics().getAverageTimeInModulePerSentence(3));
        defaultListModel2.add(25, "Tiempo promedio en " + averageStatistics.getTransactionsModuleStatistics().getModuleType() + " de una Join:" + averageStatistics.getTransactionsModuleStatistics().getAverageTimeInModulePerSentence(3));
        defaultListModel2.add(26, "Tiempo promedio en " + averageStatistics.getExecutorModuleStatistics().getModuleType() + " de una Join: " + averageStatistics.getExecutorModuleStatistics().getAverageTimeInModulePerSentence(3));
        defaultListModel2.add(27, "Tiempo promedio en " + averageStatistics.getConnectionsModuleStatistics().getModuleType() + " de una Select: " + averageStatistics.getConnectionsModuleStatistics().getAverageTimeInModulePerSentence(4));
        defaultListModel2.add(28, "Tiempo promedio en " + averageStatistics.getProcessModuleStatistics().getModuleType() + " de una Select: " + averageStatistics.getProcessModuleStatistics().getAverageTimeInModulePerSentence(4));
        defaultListModel2.add(29, "Tiempo promedio en " + averageStatistics.getQueryProcessingStatistics().getModuleType() + " de una Select: " + averageStatistics.getQueryProcessingStatistics().getAverageTimeInModulePerSentence(4));
        defaultListModel2.add(30, "Tiempo promedio en " + averageStatistics.getTransactionsModuleStatistics().getModuleType() + " de una Select: " + averageStatistics.getTransactionsModuleStatistics().getAverageTimeInModulePerSentence(4));
        defaultListModel2.add(31, "Tiempo promedio en " + averageStatistics.getExecutorModuleStatistics().getModuleType() + " de una Select: " + averageStatistics.getExecutorModuleStatistics().getAverageTimeInModulePerSentence(4));
    }

    public void addLabel() {
        jPanel4.add(jLabel1);
        jPanel4.add(jLabel2);
        jPanel4.add(jLabel3);
        jPanel4.add(jLabel4);
        jPanel4.add(jLabel5);
        jPanel4.add(jLabel6);
        jPanel4.add(jLabel7);
        jPanel4.add(jLabel8);
        jPanel4.add(jLabel9);
        jPanel4.add(jLabel10);
        jPanel4.add(jLabel11);
        jPanel4.add(jLabel12);
        jPanel4.add(jLabel13);
        jPanel4.add(jLabel14);
        jPanel4.add(jLabel15);
        jPanel4.add(jLabel16);
        jPanel4.add(jLabel17);
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public Double getTimeOut() {
        return timeOut;
    }

    public int getConnectionsServer() {
        return connectionsServer;
    }

    public int getQueryProcessingServers() {
        return queryProcessingServers;
    }

    public int getTransactionsServers() {
        return transactionsServers;
    }}