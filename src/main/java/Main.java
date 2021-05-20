import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;

import java.io.EOFException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Class.*;

public class Main {
    public static void main(String[] args) throws Exception {
        //capture();
        process();
    }


    public static void capture() throws Exception {
        InetAddress addr = InetAddress.getByName("172.26.23.10");
        PcapNetworkInterface nif = Pcaps.getDevByAddress(addr);
        System.out.println("Using: " + nif.getName());

        int snapLen = 65536;
        PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
        int timeout = 10;
        PcapHandle handle = nif.openLive(snapLen, mode, timeout);
        Packet packet = handle.getNextPacketEx();
        handle.close();

        System.out.println(packet.getBuilder());
    }

    public static void process() throws Exception{
        List<Packet> pacotes = new ArrayList<>();
        PcapHandle handler = Pcaps.openOffline("captura.pcap");
        Packet packet;
        while (true) {
            try {
                packet = handler.getNextPacketEx();
            } catch (EOFException e) {
                break;
            }
            pacotes.add(packet);
        }

        System.out.println(pacotes.size());

        System.out.println(pacotes.get(10).get(UdpPacket.class).getHeader());

    }
}
