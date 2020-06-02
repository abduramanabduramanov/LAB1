import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client extends Thread{
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        try
        {
            socket = new Socket("localhost", 5050); //создаем сокет
            new Output(socket);
            new Input(socket);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}

class Output extends Thread{ //поток отправки сообщений на сервер
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private String nickname;

    public Output(Socket s) throws IOException{
        socket = s;
        in = new Scanner(System.in); //сканер для чтения строки введеной пользователем
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true); // поток отправки сообщения на сервер
        System.out.println("Enter nickname");
        nickname = in.nextLine(); //сохраняем ник пользователя
        start();
    }

    public void run(){
        while(true){
            String data = in.nextLine(); //читаем сообщение введеное пользователем

            if(data.equals("END")) { //если пользователь написал END закрываем выходи
                break;
            }
            out.println(nickname + ": "+ data);  //отправляем сообщения на сервер
        }
    }
}

class Input extends Thread{ //поток чтения сообщений из сервер
    private Socket socket;
    private BufferedReader in;

    public Input(Socket s) throws IOException{
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // поток чтения сообщения из сервер
        start();
    }

    public void run(){
        try {
            while(true){
                String data = in.readLine();    //читаем сообщения
                System.out.println( data);  //выводим его
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}