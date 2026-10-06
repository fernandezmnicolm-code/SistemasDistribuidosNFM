package bo.edu.usfx.jgroups.segundoejerciciop;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ObjectMessage;
import org.jgroups.Receiver;
import org.jgroups.View;

public class NodoPuerta implements Receiver {

    private JChannel canal;

    private String nombre;
    private int aforoMaximo;
    private int ocupacionActual;

    private View vistaAnterior;


    public NodoPuerta(String nombre, int aforoMaximo) {

        this.nombre = nombre;
        this.aforoMaximo = aforoMaximo;
        this.ocupacionActual = 0;
    }


    public void iniciar() throws Exception {

        canal = new JChannel();

        canal.name(nombre);

        canal.setReceiver(this);

        canal.connect("AforoSIS258");


        // Si ya existen otras puertas,
        // pedimos el estado actual
        if (canal.getView().size() > 1) {

            canal.getState(null, 10000);
        }


        System.out.println("Puerta " + nombre + " conectada");

        System.out.println("Aforo maximo: " + aforoMaximo);
    }


    @Override
    public void viewAccepted(View vista) {

        if (vistaAnterior == null) {

            for (Address miembro : vista.getMembers()) {

                System.out.println("Entro la puerta: " + miembro);
            }

        } else {

            // Buscar puertas que entraron
            for (Address miembro : vista.getMembers()) {

                if (!vistaAnterior.getMembers().contains(miembro)) {

                    System.out.println("Entro la puerta: "+ miembro);
                }
            }

            // Buscar puertas que salieron
            for (Address miembro: vistaAnterior.getMembers()) {

                if (!vista .getMembers().contains(miembro)) {

                    System.out.println("Salio la puerta: "+ miembro);
                }
            }
        }

        System.out.println("Coordinador actual: "+ vista.getCoord());


        vistaAnterior = vista;
    }


    @Override
    public void receive(Message msg) {

        MensajeroAforo mensaje = (MensajeroAforo) msg.getObject();

        if (mensaje.getTipo() == MensajeroAforo.Tipo.SOLICITUD) {

            procesarSolicitud(msg,mensaje);

        } else if (mensaje.getTipo() == MensajeroAforo.Tipo.ACEPTADO ) {

            ocupacionActual = ocupacionActual + mensaje.getPersonas();

            System.out.println("Entrada aceptada: " + mensaje.getPersonas() + " personas por " + mensaje.getPuerta());

            System.out.println("Ocupacion: " + ocupacionActual + "/" + aforoMaximo);

            if (ocupacionActual == aforoMaximo) {

                System.out.println("AFORO COMPLETO");
            }

        } else if (mensaje.getTipo()== MensajeroAforo.Tipo.RECHAZADO) {

            System.out.println("Entrada rechazada");

        } else if (mensaje.getTipo()== MensajeroAforo.Tipo.SALIDA) {

            ocupacionActual = ocupacionActual - mensaje.getPersonas();

            System.out.println("Salida de " + mensaje.getPersonas() + " personas por "+ mensaje.getPuerta());

            System.out.println("Ocupacion: " + ocupacionActual + "/" + aforoMaximo);
        }
    }


    public void procesarSolicitud(Message msg,MensajeroAforo mensaje) {

        if (!canal.getAddress().equals(canal.getView().getCoord())) {

            return;
        }

        int nuevaOcupacion = ocupacionActual + mensaje.getPersonas();

        try {

            if (nuevaOcupacion <= aforoMaximo) {

                MensajeroAforo aceptado = new MensajeroAforo(
                                MensajeroAforo.Tipo.ACEPTADO,
                                mensaje.getPuerta(),
                                mensaje.getPersonas()
                        );
                
                canal.send(new ObjectMessage( null, aceptado)
                );
                
            } else {

                MensajeroAforo rechazado = new MensajeroAforo(MensajeroAforo.Tipo.RECHAZADO,
                                mensaje.getPuerta(),
                                mensaje.getPersonas()
                        );
                canal.send(new ObjectMessage( msg.getSrc(), rechazado));
            }
        } catch (Exception e) {

            System.out.println( "Error: " + e.getMessage());
        }
    }


    public void entrar(int personas) {

        try {

            Address coordinador = canal.getView().getCoord();

            MensajeroAforo solicitud = new MensajeroAforo(MensajeroAforo.Tipo.SOLICITUD,nombre,personas);

            canal.send(new ObjectMessage(coordinador,solicitud));

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }


    public void salir(int personas) {

        try {
            MensajeroAforo salida = new MensajeroAforo(MensajeroAforo.Tipo.SALIDA,nombre,personas);

            canal.send(new ObjectMessage(null, salida));

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage() );
        }
    }


    public void mostrarEstado() {

        System.out.println("Ocupacion: " + ocupacionActual + "/" + aforoMaximo);

        if (ocupacionActual == aforoMaximo) {

            System.out.println("AFORO COMPLETO");
        }
    }


    public void menu() {

        Scanner teclado = new Scanner(System.in);

        while (true) {

            System.out.print("> ");

            String linea = teclado.nextLine();

            String[] partes = linea.split(" ");

            if (partes[0].equalsIgnoreCase( "/entrar")) {

                int personas = Integer.parseInt(partes[1]);

                entrar(personas);

            } else if (partes[0].equalsIgnoreCase("/salir")) {

                int personas = Integer.parseInt( partes[1]);
                salir(personas);

            } else if (partes[0].equalsIgnoreCase("/estado")) {
            mostrarEstado();

            } else {
                System.out.println("Comando no reconocido");
            }
        }
    }


    @Override
    public void getState(OutputStream salida) throws Exception {

        DataOutputStream datos = new DataOutputStream(salida);
        
        datos.writeInt(ocupacionActual);
        
        System.out.println("Enviando estado: " + ocupacionActual);
    }


    @Override
    public void setState(InputStream entrada)throws Exception {

        DataInputStream datos =new DataInputStream(entrada);

        ocupacionActual =datos.readInt();

        System.out.println("Estado recibido: " + ocupacionActual);
    }


    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            System.out.println("Uso: java NodoPuerta " + "<nombre> <aforoMaximo>");
            return;
        }

        String nombre = args[0];

        int aforoMaximo = Integer.parseInt(args[1]);

        NodoPuerta nodo = new NodoPuerta(nombre,aforoMaximo);


        nodo.iniciar();

        nodo.menu();
    }
}
