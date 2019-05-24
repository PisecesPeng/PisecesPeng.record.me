<h2> JavaNIO简单聊天系统示例 </h2>

> 本短文通过注释,尽量说明这个NIO聊天Demo的流程.
<hr>

```java
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 服务端
 */
public class ServerService {

    // 服务端的端口
    private static int port = 8888;
    // 用于字符集编解码
    private static Charset charset = Charset.forName("UTF-8");
    // 用于接收数据的缓冲区
    private static ByteBuffer rBuffer = ByteBuffer.allocate(1024);
    // 用于发送数据的缓冲区
    private static ByteBuffer sBuffer = ByteBuffer.allocate(1024);
    // 用于存放客户端SocketChannel集合
    private static Map<String, SocketChannel> clientMap = new HashMap();
    // 用于监听通道事件
    private static Selector selector;

    // 实例化类的时候调用一次
    static {
        try {
            init();
            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化服务器
     */
    private static void init() throws IOException {
        // ServerSocketChannel是个基于socket的监听器
        // .open()创建新对象,并会返回同一个未绑定的ServerSocket关联的通道(后通过.socket()方法获取)
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置非阻塞
        serverSocketChannel.configureBlocking(false);

        // 获取ServerSocket
        ServerSocket serverSocket = serverSocketChannel.socket();
        // 监听指定端口
        serverSocket.bind(new InetSocketAddress(port));

        // 获取选择器
        selector = Selector.open();
        // 注册Channel到selector,第二个参数用于监控通道的什么状态
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("服务器启动,端口为：" + port);
    }

    /**
     * 服务器端轮询监听
     */
    public static void listen() {
        while (true) {
            try {
                // 本次触发的事件数是否大于0
                if (selector.select() <= 0) continue;

                //获取当前选择器中所有注册的选择键(已就绪监听键)
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                // 处理
                selectionKeys.forEach(selectionKey -> handle(selectionKey));

                // 清除处理过的事件
                selectionKeys.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 处理事件
     */
    private static void handle(SelectionKey selectionKey) {
        try {
            // 客户端连接就绪
            if (selectionKey.isAcceptable()) {
                // 获得当前监听器中的SocketChannel(符合'OP_ACCEPT'状态)
                ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();

                // 建立SocketChannel连接
                SocketChannel client = server.accept();
                // 设置非阻塞
                client.configureBlocking(false);

                // 注册读监听
                client.register(selector, SelectionKey.OP_READ);

                // 将此客户端信息保存
                clientMap.put(getKeyName(client), client);
            }
            // 读取客户端就绪
            else if (selectionKey.isReadable()) {
                // 获得当前监听器中的SocketChannel(符合'OP_READ'状态)
                SocketChannel client = (SocketChannel) selectionKey.channel();

                // 将缓存区清空,一般在把数据写入缓冲区前调用
                rBuffer.clear();

                // 是否有数据
                if (client.read(rBuffer) > 0) {
                    // 反转缓冲区,通常在准备从缓冲区中读取数据时调用
                    rBuffer.flip();
                    // 从缓冲区读取数据
                    String receiveText = String.valueOf(charset.decode(rBuffer));
                    System.out.println(client.toString() + ":" + receiveText);

                    dispatch(client, receiveText);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转发消息给客户端
     */
    private static void dispatch(SocketChannel client, String info) throws IOException {
        // 客户端SocketChannel集合不为空
        if (!clientMap.isEmpty()) {
            for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                // 取出客户端SocketChannel
                SocketChannel temp = entry.getValue();

                if (!client.equals(temp)) {  // 非发送该消息的客户端均进行下列操作

                    // 将缓存区清空,一般在把数据写入缓冲区前调用
                    sBuffer.clear();
                    // 将信息写入缓存区
                    sBuffer.put(charset.encode(getKeyName(client) + ":" + info));
                    // 反转缓冲区,通常在准备从缓冲区中读取数据时调用
                    sBuffer.flip();

                    // 写入客户端SocketChannel
                    temp.write(sBuffer);
                }
            }
        }
    }

    /**
     * 生成Key的值
     */
    private static String getKeyName(SocketChannel client){
        return "[" + client.socket().getInetAddress().toString().substring(1) + ":" + Integer.toHexString(client.hashCode()) + "]";
    }

    /**
     * 启动服务端
     */
    public static void main(String[] args) {
        new ServerService();
    }

}
```

```java
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.Set;

/**
 * 客户端
 */
public class ClientService {

    // 服务端地址
    private InetSocketAddress SERVER;
    // 用于接收数据的缓冲区
    private ByteBuffer rBuffer = ByteBuffer.allocate(1024);
    // 用于发送数据的缓冲区
    private ByteBuffer sBuffer = ByteBuffer.allocate(1024);
    // 用于监听通道事件
    private static Selector selector;
    // 用于编/解码 buffer
    private Charset charset = Charset.forName("UTF-8");

    public ClientService(int port) {
        // IP+PORT,连接服务端
        SERVER = new InetSocketAddress("localhost", port);
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 初始化客户端
    private void init() throws IOException {

        // 获得一个SocketChannel
        SocketChannel socketChannel = SocketChannel.open();
        // 设置非阻塞
        socketChannel.configureBlocking(false);
        // 获取选择器
        selector = Selector.open();
        // 注册连接监听
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        // 连接到对应服务器端
        socketChannel.connect(SERVER);

        while (true) {
            // 本次触发的事件数是否大于0
            if (selector.select() <= 0) continue;

            //获取当前选择器中所有注册的选择键(已就绪监听键)
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 处理
            selectionKeys.forEach(selectionKey -> handle(selectionKey));

            // 清除处理过的事件
            selectionKeys.clear();
        }

    }

    /**
     * 处理事件
     */
    private void handle(SelectionKey selectionKey) {
        try {
            // 连接服务端就绪
            if (selectionKey.isConnectable()) {
                // 获得当前监听器中的SocketChannel(符合'OP_CONNECT'状态)
                SocketChannel client = (SocketChannel) selectionKey.channel();

                // 判断此通道上是否正在进行连接操作
                if (client.isConnectionPending()) {
                    // 完成连接的建立
                    client.finishConnect();
                    System.out.println("连接成功!");

                    new Thread(() -> {
                        while (true) {
                            try {
                                // 将缓存区清空,一般在把数据写入缓冲区前调用
                                sBuffer.clear();
                                // 获取用户键盘输入
                                Scanner scanner = new Scanner(System.in);
                                String sendText = scanner.nextLine();

                                // 将信息写入缓存区
                                sBuffer.put(charset.encode(sendText));
                                // 反转缓冲区,通常在准备从缓冲区中读取数据时调用
                                sBuffer.flip();

                                // 写入客户端SocketChannel
                                client.write(sBuffer);
                                System.out.println("发送成功");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

                // 注册读监听(服务端会去读)
                client.register(selector, SelectionKey.OP_READ);
            }
            // 读取客户端就绪
            else if (selectionKey.isReadable()) {
                // 获得当前监听器中的SocketChannel(符合'OP_READ'状态)
                SocketChannel client = (SocketChannel) selectionKey.channel();
                // 把position设为0,把limit设为capacity,并将mark设为-1(取消标记)
                rBuffer.clear();

                // 是否有数据
                int count = client.read(rBuffer);
                if (count > 0) {
                    // 从缓存区取出数据(缓存区数据已由服务端写入)
                    String receiveText = new String(rBuffer.array(), 0, count);
                    System.out.println(receiveText);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动客户端(多个启动)
     */
    public static void main(String[] args) {
        new ClientService(8888);
    }

}
```