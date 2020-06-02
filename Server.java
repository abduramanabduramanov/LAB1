import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {
    public ArrayList connections;

    public static void main(String[] args) {
        new Server().go();
    }

    public void go() {
        connections = new ArrayList();
        try {
            ServerSocket server = new ServerSocket(5050); //порт на котором запустится сервер
            System.out.println("Server start.");

            while (true) {
                Socket clientSocket = server.accept();  //принимаем запрос на подключения от клиента
                PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
                connections.add(pw); //добовляем коиента в ArrayList

                Thread thread = new Thread(new Talker(clientSocket));//выделяем поток для работы сервера с коиентом
                thread.start(); //запускаем поток
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class Talker implements Runnable {

        BufferedReader breader;
        Socket sock;

        Talker(Socket clientSocket) {
            try {
                sock = clientSocket;
                breader = new BufferedReader(new InputStreamReader(sock.getInputStream())); //создаем потк чтения из сокета клиента
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void run() {
            String message;
            try {
                while ((message = breader.readLine()) != null) { //слушаем пока клиент не напишит
                    System.out.println("serv read " + message);
                    tellEveryone(message); //отправляем сообщение дркгим клиетам
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void tellEveryone(String message) {
            Iterator i = connections.iterator();
            while (i.hasNext()) {//перебераем список всех клиенотов
                try {
                    PrintWriter pw = (PrintWriter) i.next();
                    pw.println(message); //отправляем сообщение всем клиентам
                    pw.flush();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}