package app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.util.Scanner;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class Sender implements Runnable {
    Client cliente;

    public Sender(Client cliente) {
        this.cliente = cliente;
    }

    public void run() {
        try {
            byte[] data = new byte[1024];
            DatagramPacket sendPacket;
            System.out.println("\n\nConnected!");
            // Enviar numero de pacotes----------------------------------------------
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(this.cliente.getNumPacotes());
            data = outputStream.toByteArray();
            sendPacket = new DatagramPacket(data, data.length, this.cliente.getAddress(), this.cliente.getPorta());
            this.cliente.getUdpSocket().send(sendPacket);
            objectOutputStream.flush();
            outputStream.flush();
            objectOutputStream.close();
            outputStream.close();
            Thread.sleep(100);
            // --------------------------------------------------------------------------------------------
            for (int i = 0; i < this.cliente.getNumPacotes(); i++) {
                outputStream = new ByteArrayOutputStream();
                objectOutputStream = new ObjectOutputStream(outputStream);
                Datagrama d = new Datagrama(i);
                d.setSaida((new Timestamp(System.currentTimeMillis())).getTime());
                objectOutputStream.writeObject(d);
                data = outputStream.toByteArray();
                sendPacket = new DatagramPacket(data, data.length, this.cliente.getAddress(), this.cliente.getPorta());
                this.cliente.getUdpSocket().send(sendPacket);
                // System.out.println("Mensagem->" + d.getId());
                objectOutputStream.flush();
                outputStream.flush();
                objectOutputStream.close();
                outputStream.close();
                Thread.sleep(100);
            }
            System.out.println("Mensagens enviadas");
        } catch (IOException e) {
        } catch (InterruptedException e) {
        }
    }
}

class Receiver implements Runnable {
    Client cliente;
    boolean cont = true;

    public Receiver(Client cliente) {
        this.cliente = cliente;
    }

    public void run() {
        byte[] incomingData = new byte[1024];
        byte[] data = new byte[1024];
        long timeout = 2000;
        try {
            for (int i = 0; i < this.cliente.getNumPacotes(); i++) {
                if (cont) {
                    long startTime = System.currentTimeMillis();
                    DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                    this.cliente.getUdpSocket().receive(incomingPacket);
                    data = incomingPacket.getData();
                    ByteArrayInputStream in = new ByteArrayInputStream(data);
                    ObjectInputStream is = new ObjectInputStream(in);
                    Object o = is.readObject();
                    Datagrama d = (Datagrama) o;
                    d.setChegada(new Timestamp(System.currentTimeMillis()).getTime());
                    // System.out.println("Mensagem " + d.getId() + "->" + (d.getChegada() -
                    // d.getSaida()));
                    this.cliente.getListOfMessages().add(d);
                    is.close();
                    in.close();
                    long elapsed = System.currentTimeMillis() - startTime;
                    if (elapsed > timeout)
                        break;
                } else {
                    break;
                }
            }
            System.out.println("Resposta da Probe");
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        }
    }
}

public class Client {

    int numpacotes;
    int tampacotes;
    InetAddress address;
    int porta;
    DatagramSocket udpSocket;
    ArrayList<Long> latencia;
    ArrayList<Long> jitter;
    List<Datagrama> mensagens;
    int pl;

    public int getNumPacotes() {
        return this.numpacotes;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public int getPorta() {
        return this.porta;
    }

    public DatagramSocket getUdpSocket() {
        return this.udpSocket;
    }

    public List<Datagrama> getListOfMessages() {
        return this.mensagens;
    }

    private Client(InetAddress serverAddress, int serverPort, int num, ArrayList<Long> latencia, ArrayList<Long> jitter,
            List<Datagrama> mensagens, int pl) throws Exception {
        this.udpSocket = new DatagramSocket();
        this.porta = serverPort;
        this.address = serverAddress;
        this.numpacotes = num;
        this.latencia = latencia;
        this.jitter = jitter;
        this.mensagens = mensagens;
        this.pl = pl;
    }

    private long calculateAverage(List<Long> lista) {
        long sum = 0;
        if (!lista.isEmpty()) {
            for (Long total : lista) {
                sum += total;
            }
            return sum / lista.size();
        }
        return sum;
    }

    public void service() {
        // Tratamento de pacotes
        this.pl = this.numpacotes - this.mensagens.size();
        for (Datagrama d : this.mensagens) {
            if (d.getId() != 0) {// primeiro pacote é descartado
                long c = d.getChegada();
                long s = d.getSaida();
                this.latencia.add(c - s);
            }
        }
        for (int i = 0; i < this.latencia.size() - 1; i++) {
            long a = this.latencia.get(i);
            long b = this.latencia.get(i + 1);
            if (a > b)
                this.jitter.add(a - b);
            else
                this.jitter.add(b - a);
        }
        long diff = 0;
        for (long a : this.jitter) {
            diff += a;
        }
        System.out.println("Latência média->" + calculateAverage(this.latencia) + "ms");
        System.out.println("Jitter-> " + diff / (float) this.jitter.size() + "ms");
        if (pl == 0) {
            System.out.println("Pacotes perdidos->" + 0);
        } else {
            System.out.println("Pacotes perdidos->" + this.pl + " || Total->" + this.numpacotes + " || Percentagem ->"
                    + (((double) this.pl / (double) this.numpacotes) * 100) + "%");
        }
    }

    public static void main(String[] args) throws Exception, InterruptedException {
        Scanner kb = new Scanner(System.in);
        int num = 0;
        while (num <= 0) {
            System.out.println("Insira o número de pacotes a enviar");
            num = kb.nextInt();
            if (num < 0)
                System.out.println("Número de pacotes inválido");
        }
        kb.close();
        ArrayList<Long> latencia = new ArrayList<>();
        ArrayList<Long> jitter = new ArrayList<>();
        List<Datagrama> listOfMessages = new ArrayList<>();
        int pl = 0;

        Client client = new Client(InetAddress.getByName(args[0]), 3000, num, latencia, jitter, listOfMessages, pl);
        Sender s = new Sender(client);
        Receiver r = new Receiver(client);
        long timeout = 200 * num; // number of milliseconds before timeout
        Thread envia = new Thread(s);
        Thread recebe = new Thread(r);
        envia.start();
        recebe.start();
        Thread.sleep(timeout);
        r.cont = false;
        client.service();
    }
}
