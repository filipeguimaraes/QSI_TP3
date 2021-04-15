package app;

import java.io.*;
import java.net.SocketException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Probe {
    int porta = 3000;
    private DatagramSocket socket;
    int numpacotes;

    public Probe() throws SocketException {
        this.socket = new DatagramSocket(this.porta);
    }

    public Object recebe(DatagramPacket incomingPacket) throws IOException, ClassNotFoundException {
        this.socket.receive(incomingPacket);
        byte[] data = incomingPacket.getData();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        Object o = is.readObject();
        is.close();
        in.close();
        return o;
    }

    public void envia(Datagrama d, DatagramPacket incomingPacket) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(d);
        InetAddress IPAddress = incomingPacket.getAddress();
        int port = incomingPacket.getPort();
        byte[] resposta = bos.toByteArray();
        DatagramPacket replyPacket = new DatagramPacket(resposta, resposta.length, IPAddress, port);
        this.socket.send(replyPacket);
        out.flush();
        bos.flush();
        out.close();
        bos.close();
    }

    private void service() throws IOException, ClassNotFoundException, InterruptedException {
        List<Datagrama> listOfMessages = new ArrayList<>();
        try {
            while (true) {
                byte[] incomingData = new byte[1024];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                Object o = recebe(incomingPacket);
                if (o instanceof Integer) {
                    this.numpacotes = (Integer) o;
                    System.out.println("Á espera de " + this.numpacotes + " pacotes");
                }
                if (o instanceof Datagrama) {
                    for (int i = 0; i < this.numpacotes; i++) {
                        Datagrama d = (Datagrama) o;
                        d.setIntermedio(new Timestamp(System.currentTimeMillis()).getTime());
                        listOfMessages.add(d);
                        // System.out.println(d.getId() + "->" + (d.getIntermedio() - d.getSaida()) +
                        // "ms");
                        envia(d, incomingPacket);
                        if (i < this.numpacotes - 1)
                            o = recebe(incomingPacket);
                    }
                    break;
                }
            }
            this.socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            Probe app = new Probe();
            app.service();

        } catch (SocketException ex) {
            System.out.println("Socket error: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
