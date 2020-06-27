# netty
来自尚硅谷的netty教程,参考视频来自 https://www.bilibili.com/video/BV1DJ411m7NR?t=119&p=12

[TOC]

**简介:**
- jboss开源框架
- 异步,事件驱动,高性能的网络io
- tcp协议,高并发
**应用:**
- RPC,基础的通信组件
- 游戏行业
- hadoop
- 进行二次发封装

| Netty |
| ----- |
| NIO   |
| JDK   |
| TCP   |



**IO 模型:**
AIO:异步非阻塞  连接数目多且连接比较长（重操作）的架构，比如相册服务器，充分调用 OS 参与并发操作，
编程比较复杂，JDK7 开始支持。
BIO:阻塞式,连接数目比较小且固定
NIO:同步非阻塞式,多路复用,连接数目多且连接比较短（轻操作）的架构，比如聊天服务器

# BIO

在包,java.IO

```sh

阻塞式IO,需要多个线程,对服务器的要求比较高
1.服务器端启动一个 ServerSocket
2.客户端启动 Socket 对服务器进行通信，默认情况下服务器端需要对每个客户 建立一个线程与之通讯
3.客户端发出请求后, 先咨询服务器是否有线程响应，如果没有则会等待，或者被拒绝
4.如果有响应，客户端线程会等待请求结束后，在继续执行

```

![image-20200601001538108](..\img\netty\nio-1.png)



```java

  public static void main(String[] args) throws Exception {

//思路
//1. 创建一个线程池
//2. 如果有客户端连接，就创建一个线程，与之通讯(单独写一个方法)
        ExecutorService newCachedThreadPool;
        newCachedThreadPool = Executors.newCachedThreadPool();
//创建 ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");
        while (true) {
            System.out.println(" 线 程 信 息 id =" + Thread.currentThread().getId() + " 名 字 =" +
                    Thread.currentThread().getName());
//监听，等待客户端连接
            System.out.println("等待连接....");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
//就创建一个线程，与之通讯(单独写一个方法)
            newCachedThreadPool.execute(new Runnable() {
                public void run() { //我们重写
//可以和客户端通讯
                    handler(socket);
                }

            });
        }
    }

    //编写一个 handler 方法，和客户端通讯
    public static void handler(Socket socket) {
        try {
            System.out.println(" 线 程 信 息 id =" + Thread.currentThread().getId() + " 名 字 =" +
                    Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
//通过 socket 获取输入流
            InputStream inputStream = socket.getInputStream();
//循环的读取客户端发送的数据
            while (true) {
                System.out.println(" 线 程 信 息 id =" + Thread.currentThread().getId() + " 名 字 =" +
                        Thread.currentThread().getName());
                System.out.println("read....");
                int read = inputStream.read(bytes);
                if (read != -1) {

                    System.out.println(new String(bytes, 0, read
                    )); //输出客户端发送的数据
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和 client 的连接");
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

```



**存在的问题**

```sh
1每个请求都需要创建独立的线程，与对应的客户端进行数据 Read，业务处理，数据 Write 。
2) 当并发数较大时，需要创建大量线程来处理连接，系统资源占用较大。
3) 连接建立后，如果当前线程暂时没有数据可读，则线程就阻塞在 Read 操作上，造成线程资源浪费
```

# NIO

简介:
- non-blocking IO 是指 JDK 提供的新 API。从 JDK1.4 开始，Java 提供了一系列改进的
- 输入/输出的新特性，被统称为 NIO(即 New IO)，是同步非阻塞的,对应的包在java.nio中,
- 三个组件: channel:通道 buffer:缓冲区 selector:选择器
- NIO 是 是 区 面向缓冲区 ，向 或者面向 块 块 编程的



NIO是非阻塞的, 使一个线程从某通道发送请求或者读取数据，  
HTTP2.0 使用了多路复用的技术，做到同一个连接并发处理多个请求，而且并发请求的数量比 HTTP1.1

**比较:**

- BIO 以流的方式处理数据,而 NIO 以块的方式处理数据,块 I/O 的效率比流 I/O 高很多
-  BIO 是阻塞的，NIO 则是非阻塞的
-  BIO 基于字节流和字符流进行操作，而 NIO 基于 Channel(通道)和 Buffer(缓冲区)进行操作，数据总是从通道读取到缓冲区中，或者从缓冲区写入到通道中。Selector(选择器)用于监听多个通道的事件（比如：连接请求,数据到达等），因此使用单个线程就可以监听多个客户端通道

![image-20200603003112414](..\img\netty\image-20200603003112414.png)

1. 每个 channel 都会对应一个 Buffer
2. Selector 对应一个线程， 一个线程对应多个 channel(连接)
3. 该图反应了有三个 channel 注册到 该 selector //程序
4. 程序切换到哪个 channel 是有事件决定的, Event 就是一个重要的概念
5. Selector 会根据不同的事件，在各个通道上切换
6. Buffer 就是一个内存块 ， 底层是有一个数组
7. 数据的读取写入是通过 Buffer, 这个和 BIO , BIO 中要么是输入流，或者是输出流, 不能双向，但是 NIO 的 Buffer 是可以读也可以写, 需要 flip 方法切换channel 是双向的, 可以返回底层操作系统的情况, 比如 Linux ， 底层的操作系统通道就是双向的.

## 缓冲区Buffer

缓冲区（Buffer）：缓冲区本质上是一个 可以读写数据的内存块，可以理解成是一个 容器对象( 含数组)，该对象提供了一组方法，可以更轻松地使用内存块,


![image-20200603003550546](..\img\netty\image-20200603003550546.png)

**buffer的子类**

|  类名| 描述     |
| ---- | ---- |
|  ByteBuffer    | 字节数据到缓冲区    |
|  ShortBuffer    |  字符串数据到缓冲区    |
|   CharBuffer   |   字符数据到缓冲区   |
|   IntBuffer   |  整数数据到缓冲区    |
|   LongBuffer   |  长整型数据到缓冲区    |
|   DoubleBuffer   |  小数到缓冲区    |
|   FloatBuffer   |   浮点数到缓冲区   |

**四个元素:**
|  属性| 描述     |
| ---- | ---- |
|  Capacity    | 容量，即可以容纳的最大数据量；在缓冲区创建时被设定并且不能改变   |
|  Limit    |  表示缓冲区的当前终点，不能对缓冲区超过极限的位置进行读写操作。且极限是可以修改的    |
|   Position   |   位置，下一个要被读或写的元素的索引，每次读写缓冲区数据时都会改变改值，为下次读写作准备   |
|   Mark   |  标记    |

常见方法:
```java

public abstract class Buffer {
    //JDK1.4时，引入的api
    public final int capacity( )//返回此缓冲区的容量
    public final int position( )//返回此缓冲区的位置
    public final Buffer position (int newPositio)//设置此缓冲区的位置
    public final int limit( )//返回此缓冲区的限制
    public final Buffer limit (int newLimit)//设置此缓冲区的限制
    public final Buffer mark( )//在此缓冲区的位置设置标记
    public final Buffer reset( )//将此缓冲区的位置重置为以前标记的位置
    public final Buffer clear( )//清除此缓冲区, 即将各个标记恢复到初始状态，但是数据并没有真正擦除, 后面操作会覆盖
    public final Buffer flip( )//反转此缓冲区
    public final Buffer rewind( )//重绕此缓冲区
    public final int remaining( )//返回当前位置与限制之间的元素数
    public final boolean hasRemaining( )//告知在当前位置和限制之间是否有元素
    public abstract boolean isReadOnly( );//告知此缓冲区是否为只读缓冲区
 
    //JDK1.6时引入的api
    public abstract boolean hasArray();//告知此缓冲区是否具有可访问的底层实现数组
    public abstract Object array();//返回此缓冲区的底层实现数组
    public abstract int arrayOffset();//返回此缓冲区的底层实现数组中第一个缓冲区元素的偏移量
    public abstract boolean isDirect();//告知此缓冲区是否为直接缓冲区
}
```



## 通道(Channel)

NIO 的通道类似于流，但有些区别如下：
- 通道可以同时进行读写，而流只能读或者只能写
- 通道可以实现异步读写数据
- 通道可以从缓冲读数据，也可以写数据到缓冲
Channel 在 NIO 中是一个接口
```java
public interface Channel extends Closeable{}
```
常用的Channel类有：FileChannel、DatagramChannel、ServerSocketChannel和SocketChannel。
(ServerSocketChannel类似ServerSocket,SocketChannel类似Socket)
FileChannel用于文件的数据读写，DatagramChannel用于UDP的数据读写，ServerSocketChannel和SocketChannel
用于TCP的数据读写。

### FileChannel 
 FileChannel 主要用来对本地文件进行 IO 操作，常见的方法有
 ```java

 public int read(ByteBuffer dst)//从通道读取数据并放到缓冲区中
 public int write(ByteBuffer src) //把缓冲区的数据写到通道中
 public long transferFrom(ReadableByteChannel src, long position, long count)//从目标通道中复制数据到当前通道
 public long transferTo(long position, long count, WritableByteChannel target)//把数据从当前通道复制给目标通道
```
**写入到文件**
1. 创建通道
2. 创建缓冲区
3. 数据写入缓冲区
4. 写入数据到通道

**读取文件数据**

1. 创建文件流
2. 获得通道,获取对应的 FileChannel -> 实际类型 FileChannelImpl
3. 创建缓冲区
4. 将 byteBuffer 的 字节数据 转成 String

**拷贝文件**
1. 创建文件读取流,及通道
2. 文件输出流及通道
3. 文件缓冲区,
4. 读取文件并写入
5. 关闭流
```java
public class NioChannel003 {
public static void main(String[] args) throws Exception {
        //获得文件及通道
        FileInputStream fileInputStream = new FileInputStream("d:\\file01.txt");
        FileChannel fileChannelRead = fileInputStream.getChannel();
        //文件输出及通道
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file02.txt");
        FileChannel fileChannelWrite = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        //循环读取
        while (true) {
          
            byteBuffer.clear(); //清空 buffer
            int read = fileChannelRead.read(byteBuffer);
            System.out.println("read =" + read);
            //表示读完
            if (read == -1) {
                break;
            }
            //将 buffer 中的数据写入到 fileChannelWrite -- 2.txt
            byteBuffer.flip();
            fileChannelWrite.write(byteBuffer);
        }
        fileInputStream.close();
        fileOutputStream.close();

    }
}
```
transferFrom拷贝
```java
//获取各个流对应的 filechannel
FileChannel sourceCh = fileInputStream.getChannel();
FileChannel destCh = fileOutputStream.getChannel();
//使用 transferForm 完成拷贝
destCh.transferFrom(sourceCh,0,sourceCh.size());
//关闭相关通道和流
sourceCh.close();
destCh.close();
fileInputStream.close();
fileOutputStream.close();

```
 ByteBuffer 支持类型化的 put 和 get, put 放入的是什么数据类型，get 就应该使用相应的数据类型来取出，否
 则可能有 BufferUnderflowException 异常
 只读流:
 ```java
ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
写入的时候会报错
```
**MappedByteBuffer**
让文件在内存中(堆外的内存)修改,操作系统不需要拷贝一次,
内存中修改文件:
```java
 public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("d:\\file01.txt", "rw");
//获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();
/**
 * 参数 1: FileChannel.MapMode.READ_WRITE 使用的读写模式
 * 参数 2： 0 ： 可以直接修改的起始位置
 * 参数 3: 5: 是映射到内存的大小(不是索引位置) ,即将 1.txt 的多少个字节映射到内存
 * 可以直接修改的范围就是 0-5
 * 实际类型 DirectByteBuffer
 */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');
//        mappedByteBuffer.put(5, (byte) 'Y');//IndexOutOfBoundsException
        randomAccessFile.close();
        System.out.println("修改成功~~");

    }
```
**多个buffer**
- Scattering：将数据写入到 buffer 时，可以采用 buffer 数组，依次写入 (分散)
- Gathering: 从 buffer 读取数据时，可以采用 buffer 数组，依次读

```java
//todo
```

## 选择器(Selector)
Selector 能够检测多个注册的通道上是否有事件发生,一个单线程去管理多个通道，也就是管理多个连接和请求

是一个抽象类, 
```java

public abstract class Selector implements Closeable { 
public static Selector open();//得到一个选择器对象
public int select(long timeout);//监控所有注册的通道，当其中有 IO 操作可以进行时，将
//对应的 SelectionKey 加入到内部集合中并返回,参数用来设置超时时间
public Set<SelectionKey> selectedKeys();//从内部集合中得到所有的 SelectionKey	
}

selector.select()//阻塞
selector.select(1000);//阻塞1000毫秒，在1000毫秒后返回
selector.wakeup();//唤醒selector
selector.selectNow();//不阻塞，立马返还

```


1. NioEventLoop 聚合了 Selector(选择器，也叫多路复用器)，可以同时并发处理成百上千个客
户端连接

2. Socket 通道进行读写数据时，若没有数据可用时，该线程可以进行其他任务。
3. 线程通常将非阻塞 IO 的空闲时间用于在其他通道上执行 IO 操作，所以单独的线程可以管理多个输入和输出通道。
4. 由于读写操作都是非阻塞的，这就可以充分提升 IO 线程的运行效率，避免由于频繁 I/O 阻塞导致的线程挂起。
5. 一个 I/O 线程可以并发处理 N 个客户端连接和读写操作，这从根本上解决了传统同步阻塞 I/O 一连接一线
程模型，架构的性能、弹性伸缩能力和可靠性都得到了极大的提升。

selector的相关方法
```java
 Selector open();//得到一个选择器对象  
int select();//阻塞,有事件才会返回
int select(long timeout);//监控所有注册的通道,当其中有io操作可以进行时,将对相应的selectionkey加入到内部集合并返回,参数设置超时时间
Set<SelectionKey> selectedKeys();//内部集合中得到的所有selectionKey
```
**关系图:**
Selector、SelectionKey、ServerScoketChannel和SocketChannel
![Selector_SelectionKey_ServerScoketChannel](..\img\netty\Selector_SelectionKey_ServerScoketChannel.png)

- 当客户端连接时，会通过ServerSocketChannel 得到 SocketChannel
- Selector 进行监听  select 方法, 返回有事件发生的通道的个数.
- 将socketChannel注册到Selector上, register(Selector sel, int ops), 一个selector上可以注册多个SocketChannel

- 注册后返回一个 SelectionKey, 会和该Selector 关联(集合)
- 进一步得到各个 SelectionKey (有事件发生)
- 在通过 SelectionKey  反向获取 SocketChannel , 方法 channel()
- 可以通过得到的 channel, 完成业务处理

服务端:
```java

 public static void main(String[] args) throws Exception {
        //创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(8888));

        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把 serverSocketChannel 注册到 selector 关心 事件为 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            //这里我们等待 1 秒，如果没有事件发生, 返回
            if(selector.select(3000) == 0) { //没有事件发生
                System.out.println("服务器等待了 1 秒，无连接");
                continue;
            }
            //如果返回的>0, 就获取到相关的 selectionKey 集合
        //1.如果返回的>0， 表示已经获取到关注的事件
        //2. selector.selectedKeys() 返回关注事件的集合
        // 通过 selectionKeys 反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
        //遍历 Set<SelectionKey>, 使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
            //获取到 SelectionKey
                SelectionKey key = keyIterator.next();
            //根据 key 对应的通道发生的事件做相应处理
                //如果是 OP_ACCEPT, 有新的客户端连接
                if(key.isAcceptable()) {
                    //该该客户端生成一个 SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println(" 客 户 端 连 接 成 功 生 成 了 一 个 socketChannel " +
                            socketChannel.hashCode());
                    //将 SocketChannel 设置为非阻塞
                    socketChannel.configureBlocking(false);
                //将 socketChannel 注册到 selector, 关注事件为 OP_READ， 同时给 socketChannel
                //关联一个 Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if(key.isReadable()) { //发生 OP_READ
                    //通过 key 反向获取到对应 channel
                    SocketChannel channel = (SocketChannel)key.channel();
                    //获取到该 channel 关联的 buffer
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    channel.read(buffer);
                    System.out.println("form 客户端 " + new String(buffer.array()));
                }
                //手动从集合中移动当前的 selectionKey, 防止重复操作
                keyIterator.remove();
            }

        }
    }
```
客户端
```java
 public static void main(String[] args) throws  Exception{
//        得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
//提供服务器端的 ip 和 端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8888);

        //连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其它工作..");
            }
        }
        //...如果连接成功，就发送数据
        String str = "hello, xs-shuai";
        //Wraps a byte array into a buffer
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        //发送数据，将 buffer 数据写入 channel
        socketChannel.write(buffer);
        System.in.read();


    }
```
**SelectionKey**
SelectionKey，表示 Selector 和网络通道的注册关系, 共四种:
int OP_ACCEPT：有新的网络连接可以 accept，值为 16
int OP_CONNECT：代表连接已经建立，值为 8
int OP_READ：代表读操作，值为 1
int OP_WRITE：代表写操作，值为 4
源码中：
```java

public static final int OP_READ = 1 << 0;
public static final int OP_WRITE = 1 << 2;
public static final int OP_CONNECT = 1 << 3;
public static final int OP_ACCEPT = 1 << 4;

```
常用方法:
```java
Selector selector();//得到关联的selector对象
SelectableChannel  channel();//得到与之关联的通道
Object attchment();//得到与之关联的共享数据
SelectionKey interstOps(int ops);//设置或修改监听事件

boolean isAcceptable();//是否为可以accept
boolean isReadanble();//是否可读
boolean isWriteable();//是否可写


```
ServerSocketChannel: 在服务器端监听新的客户端 Socket 连接 
open(),//获得一个ServerSocketChannel 通道
bind(SocketAddress local),//设置服务器端口号
configureBlocking(boolean block);//设置为是否阻塞式 false 为非阻塞
accept(); //接受一个连接
register(Selector sel,int Ops);//注册一个选择器并设置监听事件

SocketChannel:网络 IO 通道，具体负责进行读写操作。NIO 把缓冲区的数据写入通道，或者把通道里的数
据读到缓冲区。
open(),//获得一个SocketChannel 通道
configureBlocking(boolean block);//设置为是否阻塞式 false 为非阻塞
connect(SocketAddress local),//连接服务
finnishConnect(); //如果连接失败,那么适应此方法完成连接操作

write(ByteBuf data );//往通道写数据
read(ByteBuf data);//从通道读取数据
register(Selector sel,int Ops,Object att);//注册一个选择器并设置监听事件 最后一个参数为,设置共享数据
close();//关闭通道


群聊系统:

//todo


## NIO与零拷贝

常用的零拷贝有 mmap(内存映射) 和 sendFile。

mmap:

将文件映射到内核缓冲区，同时， 用户空间可以共享内核空间的数据。这样，在进行网络传输时，
就可以减少内核空间到用户空间的拷贝次数

![mmap](..\img\netty\mmap.png)
sendFile:
数据根本不经过用户态，直接从内核缓冲区进入到
Socket Buffer，同时，由于和用户态完全无关，就减少了一次上下文切换
![sendfile](..\img\netty\sendfile.png)

**零拷贝从操作系统角度，是没有 cpu 拷贝**
Linux 在 2.4 版本中，做了一些修改，避免了从 内核缓冲区拷贝到 Socket buffer 的操作，直接拷贝到协议栈，
从而再一次减少了数据拷贝 
![sendfile2](..\img\netty\sendfile2.png)

这里其实有 一次 cpu 拷贝
kernel buffer -> socket buffer
但是，拷贝的信息很少，比如 lenght , offset , 消耗低，可以忽略

零拷贝:  
是从 操作系统的角度来说的。因为内核缓冲区之间，没有数据是重复的   
不仅仅带来更少的数据复制，还能带来其他的性能优势，例如更少的上下文切换，更少的 CPU 缓存伪
共享以及无 CPU 校验和计算

mmap 和 sendFile 的区别
- mmap 适合小数据量读写，sendFile 适合大文件传输。
- mmap 需要 4 次上下文切换，3 次数据拷贝；sendFile 需要 3 次上下文切换，最少 2 次数据拷贝。
- sendFile 可以利用 DMA 方式，减少 CPU 拷贝，mmap 则不能（必须从内核拷贝到 Socket 缓冲区）。

# Netty 概述

NIO 的API较为复杂,需要熟悉使用Selector、SelectionKey、ServerScoketChannel和SocketChannel
更需要使用java多线程,必须对多线程和网络编程非常熟悉,
开发的工作量大,难度也非常巨大

**优点:**
//todo

## 线程模型:

**传统的io模型**
1. 原理
黄色的框表示对象， 蓝色的框表示线程
白色的框表示方法(API)
2. 模型特点
采用阻塞IO模式获取输入的数据
每个连接都需要独立的线程完成数据的输入，业务处理,数据返回
3. 问题分析
当并发数很大，就会创建大量的线程，占用很大系统资源
连接创建后，如果当前线程暂时没有数据可读，该线程会阻塞在read 操作，造成线程资源浪费

![io](..\img\netty\Io.png)

**reactor模式**:(反应器,分发者,通知者)

![reactor](..\img\netty\reactor.png)

1. Reactor 模式，通过一个或多个输入同时传递给服务处理器的模式(基于事件驱动)
2. 服务器端程序处理传入的多个请求,并将它们同步分派到相应的处理线程， 因此Reactor模式也叫 Dispatcher模式
3. Reactor 模式使用IO复用监听事件, 收到事件后，分发给某个线程(进程), 这点就是网络服务器高并发处理关键


**单reactor单线程:**  
![reactor1](..\img\netty\reactor1.png)
1. Select 是前面 I/O 复用模型介绍的标准网络编程 API，可以实现应用程序通过一个阻塞对象监听多路连接请求
2. Reactor 对象通过 Select 监控客户端请求事件(接受请求)，收到事件后通过 Dispatch 进行分发
3. 如果是建立连接请求事件，则由 Acceptor 通过 Accept 处理连接请求，然后创建一个 Handler 对象处理连接完成后的后续业务处理
4. 如果不是建立连接事件，则 Reactor 会分发调用连接对应的 Handler 来响应
5. Handler 会完成 Read→业务处理→Send 的完整业务流程

> 用一个线程通过多路复用搞定所有的 IO 操作（包括连接，读、写等），编码简单，清晰明了，但是如果客户端连接数量较多，将无法支撑，  
>优点：模型简单，没有多线程、进程通信、竞争的问题，全部都在一个线程中完成  
>缺点:性能问题，只有一个线程，无法完全发挥多核 CPU 的性能。Handler 在处理某个连接上的业务时，整个进程无法处理其他连接事件，很容易导致性能瓶颈  
>缺点：可靠性问题，线程意外终止，或者进入死循环，会导致整个系统通信模块不可用，不能接收和处理外部消息，造成节点故障  

**单reactor多线程**  
![reactor2](..\img\netty\reactor2.png)

1. Reactor 对象通过select 监控客户端请求事件, 收到事件后，通过dispatch进行分发
2. 如果建立连接请求, 则右Acceptor 通过accept 处理连接请求, 然后创建一个Handler对象处理完成连接后的各种事件
3. 如果不是连接请求，则由reactor分发调用连接对应的handler 来处理
4. handler 只负责响应事件，不做具体的业务处理, 通过read 读取数据后，会分发给后面的worker线程池的某个线程处理业务
5. worker 线程池会分配独立线程完成真正的业务，并将结果返回给handler
6. handler收到响应后，通过send 将结果返回给client

>优点：可以充分的利用多核cpu 的处理能力  
>缺点：多线程数据共享和访问比较复杂， reactor 处理所有的事件的监听和响应，在单线程运行， 在高并发场景容易出现性能瓶颈.


**主从reactor多线程**
![reactor3](..\img\netty\reactor3.png)

1. Reactor主线程 MainReactor 对象通过select 监听连接事件, 收到事件后，通过Acceptor 处理连接事件
2. 当 Acceptor  处理连接事件后，MainReactor 将连接分配给SubReactor 
3. subreactor 将连接加入到连接队列进行监听,并创建handler进行各种事件处理
4. 当有新事件发生时， subreactor 就会调用对应的handler处理
5. handler 通过read 读取数据，分发给后面的worker 线程处理
6. worker 线程池分配独立的worker 线程进行业务处理，并返回结果
7. handler 收到响应的结果后，再通过send 将结果返回给client
8. Reactor 主线程可以对应多个Reactor 子线程, 即MainRecator 可以关联多个SubReactor

>优点：父线程与子线程的数据交互简单职责明确，父线程只需要接收新连接，子线程完成后续的业务处理。  
 优点：父线程与子线程的数据交互简单，Reactor 主线程只需要把新连接传给子线程，子线程无需返回数据。  
 缺点：编程复杂度较高  

## netty模型


![nettymodel](..\img\netty\nettymodel.png)

1. Netty抽象出两组线程池 BossGroup 专门负责接收客户端的连接, WorkerGroup 专门负责网络的读写  
2. BossGroup 和 WorkerGroup 类型都是 NioEventLoopGroup  
3. NioEventLoopGroup 相当于一个事件循环组, 这个组中含有多个事件循环 ，每一个事件循环是 NioEventLoop
4. NioEventLoop 表示一个不断循环的执行处理任务的线程， 每个NioEventLoop 都有一个selector , 用于监听绑定在其上的socket的网络通讯
5. NioEventLoopGroup 可以有多个线程, 即可以含有多个NioEventLoop
6. 每个Boss NioEventLoop 循环执行的步骤有3步
  - 轮询accept 事件
  - 处理accept 事件 , 与client建立连接 , 生成NioScocketChannel , 并将其注册到某个worker NIOEventLoop 上的 selector 
  - 处理任务队列的任务 ， 即 runAllTasks
7. 每个 Worker NIOEventLoop 循环执行的步骤
 - 轮询read, write 事件
 - 处理i/o事件， 即read , write 事件，在对应NioScocketChannel 处理
 - 处理任务队列的任务 ， 即 runAllTasks
8. 每个Worker NIOEventLoop  处理业务时，会使用pipeline(管道), pipeline 中包含了 channel , 即通过pipeline 可以获取到对应通道, 管道中维护了很多的 处理器


**TCP实例**
```java
package com.xs.netty.nio.netty001;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * @program: learn_root
 * @description: netty服务端
 * @author: xs-shuai.com
 * @create: 2020-06-13 00:12
 **/
public class NettyServer {
    public static void main(String[] args) throws Exception {

        //创建 BossGroup 和 WorkerGroup
        //说明
        //1. 创建两个线程组 bossGroup 和 workerGroup
        //2. bossGroup 只是处理连接请求 , 真正的和客户端业务处理，会交给 workerGroup 完成
        //3. 两个都是无限循环
        //4. bossGroup 和 workerGroup 含有的子线程(NioEventLoop)的个数
        // 默认实际 cpu 核数 * 2
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<SocketChannel>() {//创建一个通道测试对象(匿名对象)
            //给 pipeline 设置处理器
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new NettyServerHandler());
            }
        };

        try {
        //创建服务器端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();
        //使用链式编程来进行设置
        
        bootstrap.group(bossGroup, workerGroup) //设置两个线程组
                .channel(NioServerSocketChannel.class) //使用 NioSocketChannel 作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到连接个数
                .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
                .childHandler(channelInitializer); // 给我们的 workerGroup 的 EventLoop 对应的管道设置处理器
        System.out.println(".....服务器 is ready...");
        //绑定一个端口并且同步, 生成了一个 ChannelFuture 对象
        //启动服务器(并绑定端口)
        ChannelFuture cf = bootstrap.bind(6668).sync();
        //对关闭通道进行监听
        cf.channel().closeFuture().sync();
    }finally {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
    }

}
```
NettyServerHandler
```java
package com.xs.netty.nio.netty001;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-06-13 00:24
 **/
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /*
1. ChannelHandlerContext ctx:上下文对象, 含有 管道 pipeline , 通道 channel, 地址
2. Object msg: 就是客户端发送的数据 默认 Object
*/
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程 " + Thread.currentThread().getName());
        System.out.println("server ctx =" + ctx);
        System.out.println("看看 channel 和 pipeline 的关系");
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline(); //本质是一个双向链接, 出站入站
        //将 msg 转成一个 ByteBuf
        //ByteBuf 是 Netty 提供的，不是 NIO 的 ByteBuffer.
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息是:" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址:" + channel.remoteAddress());
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush 是 write + flush
        //将数据写入到缓存，并刷新
        //一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵", CharsetUtil.UTF_8));
    }

    //处理异常, 一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


}

```
```java

package com.xs.netty.nio.netty001;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @program: learn_root
 * @description: 客户端
 * @author: xs-shuai.com
 * @create: 2020-06-13 00:31
 **/
public class NettyClient {
    public static void main(String[] args) throws Exception {
        //客户端需要一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建客户端启动对象
            //注意客户端使用的不是 ServerBootstrap 而是 Bootstrap
            Bootstrap bootstrap = new Bootstrap();
            //设置相关参数
            bootstrap.group(group) //设置线程组
                    .channel(NioSocketChannel.class) // 设置客户端通道的实现类(反射)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler()); //加入自己的处理器
                        }
                    });
            System.out.println("客户端 ok..");
            //启动客户端去连接服务器端
            //关于 ChannelFuture 要分析，涉及到 netty 的异步模型
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
            //给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
        }
    }

```
```java

package com.xs.netty.nio.netty001;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-06-13 00:34
 **/
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    //当通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client " + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, server: (>^ω^<)喵", CharsetUtil.UTF_8));
    }
    //当通道有读取事件时，会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息:" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址： "+ ctx.channel().remoteAddress());
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
```

**Task**
在处理耗时任务是,channel会出现阻塞的情况,处理的方式主要有,
1. 用户程序自定义的普通任务
2. 用户自定义定时任务
3. 非当前 Reactor 线程调用 Channel 的各种方法
```java
  public class NettyServerHandler extends ChannelInboundHandlerAdapter {
//读取数据实际(这里我们可以读取客户端发送的消息)
/*
1. ChannelHandlerContext ctx:上下文对象, 含有 管道 pipeline , 通道 channel, 地址
2. Object msg: 就是客户端发送的数据 默认 Object
*/

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//比如这里我们有一个非常耗时长的业务-> 异步执行 -> 提交该 channel 对应的
//NIOEventLoop 的 taskQueue 中,
// 解决方案 1 用户程序自定义的普通任务
            ctx.channel().eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5 * 1000);
                        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵 2", CharsetUtil.UTF_8));
                        System.out.println("channel code=" + ctx.channel().hashCode());
                    } catch (Exception ex) {
                        System.out.println("发生异常" + ex.getMessage());
                    }
                }
            });
            ctx.channel().eventLoop().execute(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(5 * 1000);
                        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵 3", CharsetUtil.UTF_8));
                        System.out.println("channel code=" + ctx.channel().hashCode());
                    } catch (Exception ex) {
                        System.out.println("发生异常" + ex.getMessage());
                    }
                }
            });
// 解决方案 2 : 务 用户自定义定时任务 -》 》 到 该任务是提交到 scheduledTaskQueue 中
            ctx.channel().eventLoop().schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5 * 1000);
                        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵 4", CharsetUtil.UTF_8));
                        System.out.println("channel code=" + ctx.channel().hashCode());
                    } catch (Exception ex) {
                        System.out.println("发生异常" + ex.getMessage());
                    }
                }
            }, 5, TimeUnit.SECONDS);
            System.out.println("go on ...");
// System.out.println("服务器读取线程 " + Thread.currentThread().getName());
// System.out.println("server ctx =" + ctx);
// System.out.println("看看 channel 和 pipeline 的关系");
// Channel channel = ctx.channel();
// ChannelPipeline pipeline = ctx.pipeline(); //本质是一个双向链接, 出站入站
//
//
// //将 msg 转成一个 ByteBuf
// //ByteBuf 是 Netty 提供的，不是 NIO 的 ByteBuffer.
// ByteBuf buf = (ByteBuf) msg;
// System.out.println("客户端发送消息是:" + buf.toString(CharsetUtil.UTF_8));
// System.out.println("客户端地址:" + channel.remoteAddress());
        }

        //数据读取完毕
        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//writeAndFlush 是 write + flush
//将数据写入到缓存，并刷新

//一般讲，我们对这个发送的数据进行编码
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵 1", CharsetUtil.UTF_8));
        }

        //处理异常, 一般是需要关闭通道
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }
```
1. Netty 抽象出两组线程池，BossGroup 专门负责接收客户端连接，WorkerGroup 专门负责网络读写操作。
2. NioEventLoop 表示一个不断循环执行处理任务的线程，每个 NioEventLoop 都有一个 selector，用于监听绑定在其上的 socket 网络通道。  
3. NioEventLoop 内部采用串行化设计，从消息的读取->解码->处理->编码->发送，始终由 IO 线程 NioEventLoop负责  
  -  NioEventLoopGroup 下包含多个 NioEventLoop
  - 每个 NioEventLoop 中包含有一个 Selector，一个 taskQueue
  - 每个 NioEventLoop 的 Selector 上可以注册监听多个 NioChannel
  - 每个 NioChannel 只会绑定在唯一的 NioEventLoop 上
  - 每个 NioChannel 都绑定有一个自己的 ChannelPipeline

//todo

**future**
表示异步执行的结果,通过他提供的方法来检测是都执行完成,
ChannelFuture 是一个接口 ： public interface ChannelFuture extends Future<Void>我们可以添 加监听器，当监听的事件发生时，就会通知到监听器.

```java

//给 cf 注册监听器，监控我们关心的事件
cf.addListener(new ChannelFutureListener() {
@Override
public void operationComplete(ChannelFuture future) throws Exception {
if (cf.isSuccess()) {
System.out.println("监听端口 6668 成功");
} else {
System.out.println("监听端口 6668 失败");
}
}
});
```

**核心组件**
- bootstrap 和ServerBootstrap
> Bootstrap 类是客户端程序的启动引导类，ServerBootstrap 是服务端启动引导类
```java
public ServerBootstrap group(EventLoopGroup parentGroup, EventLoopGroup childGroup);//该方法用于服务器端，用来设置两个 EventLoop
public B group(EventLoopGroup group);//该方法用于客户端,用来设置一个 EventLoop
public B channel(Class<? extends C> channelClass);//该方法用来设置一个服务器端的通道实现
public <T> B option(ChannelOption<T> option, T value);//用来给 ServerChannel 添加配置
public <T> ServerBootstrap childOption(ChannelOption<T> childOption, T value);//用来给接收到的通道添加配置
public ServerBootstrap childHandler(ChannelHandler childHandler);//该方法用来设置业务处理类（自定义的 handler）
public ChannelFuture bind(int inetPort);//该方法用于服务器端，用来设置占用的端口号
public ChannelFuture connect(String inetHost, int inetPort);//该方法用于客户端，用来连接服务器端

```
-  Future、ChannelFuture
> 具体的实现就是通过 Future 和 ChannelFutures，他们可以注册一个监听，当操作执行成功或失败时监听会自动触发注册的监听事件
```java
Channel channel();返回当前正在进行 IO 操作的通道
ChannelFuture sync();等待异步操作执行完毕
Netty 网络通信的组件,能够用于执行网络 I/O 操作
```
- channel
> 通过Channel 可获得当前网络连接的通道的状态  
> 通过Channel 可获得 网络连接的配置参数(例如接收缓冲区大小)  
> Channel 提供异步的网络 I/O 操作(如建立连接,读写,绑定端口)异步调用意味着任何 I/O 调用都将立即返回,并且不保证在调用结束时所请求的 I/O 操作已完成  
> 调用立即返回一个 ChannelFuture 实例,通过注册监听器到 ChannelFuture 上,可以 I/O 操作成功,失败或取消时回调通知调用方
> 不同的协议类型,不同的阻塞类型,对应不同的channel类型
```java
NioSocketChannel,异步的客户端 TCP Socket 连接
NioServerSocketChannel,异步的服务器端 TCP Socket 连接
NioDatagramChannel,异步的 UDP 连接
NioSctpChannel,异步的客户端 Sctp 连接
NioSctpServerChannel,异步的 Sctp 服务器端连接,这些通道涵盖了 UDP 和 TCP 网络 IO 以及文件 IO,

```
- Selector
>通过 Selector 一个线程可以监听多个连接的 Channel 事件。  
>当向一个 Selector 中注册 Channel 后，Selector 内部的机制就可以自动不断地查询(Select) 这些注册的 Channel 是否有已就绪的 I/O 事件（例如可读，可写，网络连接完成等），这样程序就可以很简单地使用一个线程高效地管理多个 Channel 
- ChannelHandler
>ChannelHandler 是一个接口，处理 I/O 事件或拦截 I/O 操作，并将其转发到其 ChannelPipeline(业务处理链)中的下一个处理程序。  
>ChannelHandler 本身并没有提供很多方法，因为这个接口有许多的方法需要实现，方便使用期间，可以继承它的子类
```java
ChannelInboundHandler 用于处理入站 I/O 事件
ChannelOutboundHandler 用于处理出站 I/O 操作

//适配器
ChannelInboundHandlerAdapter 用于处理入站 I/O 事件
ChannelOutboundHandlerAdapter 用于处理出站 I/O 操作
ChannelDuplexHandler 用于处理入站和出站事件
```
- Pipeline 和 ChannelPipeline
>ChannelPipeline 是一个 Handler 的集合  
>也可以这样理解：ChannelPipeline 是 保存 ChannelHandler 的 List，用于处理或拦截 Channel 的入站事件和出站操作  
>ChannelPipeline 实现了一种高级形式的拦截过滤器模式，使用户可以完全控制事件的处理方式，以及 Channel 中各个的 ChannelHandler 如何相互交互  
>每个 Channel 都有且仅有一个 ChannelPipeline 与之对应  
```java

serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitializer());
//1.一个 Channel 包含了一个 ChannelPipeline

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //2.ChannelPipeline 中又维护了一个由 ChannelHandlerContext 
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("http",new HttpHandler());
    }
}
public class HttpHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
            System.out.println("客户端地址:"+ctx.channel().remoteAddress());

    }
}

一个 Channel 包含了一个 ChannelPipeline,而 ChannelPipeline 中又维护了一个由 ChannelHandlerContext 组成的双向链表,并且每个 ChannelHandlerContext 中又关联着一个 ChannelHandler

入站事件和出站事件在一个双向链表中,入站事件会从链表 head 往后传递到最后一个入站的 handler,出站事件会从链表 tail 往前传递到最前一个出站的 handler,两种类型的 handler 互不干扰

ChannelPipeline addFirst(ChannelHandler... handlers);把一个业务处理类(handler)添加到链中的第一个位置
ChannelPipeline addLast(ChannelHandler... handlers);把一个业务处理类(handler)添加到链中的最后一个位置

```
- ChannelHandlerContext  
> 保存 Channel 相关的所有上下文信息，同时关联一个ChannelHandler对象  
> ChannelHandlerContext中包含一个具体的事件处理器ChannelHandler同时ChannelHandlerContext中也绑定了对应的pipeline和Channel的信息，
```java
ChannelFuture close(),关闭通道
ChannelOutboundInvoker flush()刷新
ChannelFuture writeAndFlush(Object msg),将 数 据 写 到 ChannelPipeline 中 当 前
ChannelHandler 的下一个 ChannelHandler 开始处理(出栈)

```
- ChannelOption
>在创建 Channel 实例后,一般都需要设置 ChannelOption 参数
```java
ChannelOption.SO_BACKLOG:对应 TCP/IP 协议 listen 函数中的 backlog 参数,用来初始化服务器可连接队列大小.服
务端处理客户端连接请求是顺序处理的,所以同一时间只能处理一个客户端连接,多个客户
端来的时候,服务端将不能处理的客户端连接请求放在队列中等待处理,backlog 参数指定
了队列的大小.
ChannelOption.SO_KEEPALIVE:一直保持连接活动状态
```
- EventLoopGroup NioEventLoopGroup
> EventLoopGroup 是一组 EventLoop 的抽象，Netty 为了更好的利用多核 CPU 资源，一般会有多个 EventLoop 同时工作，每个 EventLoop 维护着一个 Selector 实例    
> EventLoopGroup 提供 next 接口，可以从组里面按照一定规则获取其中一个 EventLoop来处理任务。在 Netty 服务器端编程中，我们一般都需要提供两个 EventLoopGroup，例如：BossEventLoopGroup 和 WorkerEventLoopGroup。  
> 通常一个服务端口即一个 ServerSocketChannel对应一个Selector 和一个EventLoop线程。BossEventLoop 负责接收客户端的连接并将 SocketChannel 交给 WorkerEventLoopGroup 来进行 IO 处理，如下图所示  
![eventloop](..\img\netty\eventloop.jpg)  

1. BossEventLoopGroup 通常是一个单线程的 EventLoop，EventLoop 维护着一个注册了ServerSocketChannel 的 Selector 实例BossEventLoop 不断轮询 Selector 将连接事件分离出来  
2. 通常是 OP_ACCEPT 事件，然后将接收到的 SocketChannel 交给 WorkerEventLoopGroup  
3. WorkerEventLoopGroup 会由 next 选择其中一个 EventLoop来将这个 SocketChannel 注册到其维护的 Selector 并对其后续的 IO 事件进行处理  

```java
public NioEventLoopGroup(),构造方法
public Future<?> shutdownGracefully(),断开连接,关闭线程
```
- Unpooled
 > Netty 提供一个专门用来操作缓冲区(即Netty的数据容器)的工具类
```java
//通过给定的数据和字符编码返回一个 ByteBuf 对象（类似于 NIO 中的 ByteBuffer 但有区别）
public static ByteBuf copiedBuffer(CharSequence string, Charset charset)
```
//todo 与ByteBuf

## 应用
**群聊系统**


点对点聊天


心跳检测

# Google Protobuf

## 编码 & 解码

编码 >> 二进制(传输) >>解码 

1. Netty 自身提供了(编解码器 解码器)
2. Netty 提供的编码器
  - StringEncoder，对字符串数据进行编码   StringDecoder, 对字符串数据进行解码
  - ObjectEncoder，对 Java 对象进行编码 ObjectDecoder，对 Java 对象进行解码
3.  ObjectDecoder 和 ObjectEncoder 可以用来实现 POJO 对象或各种业务对象的编码和解码，
   底层使用的仍是 Java 序列化技术 , 而 Java 序列化技术本身效率就不高，存在如下问题
   无法跨语言
   序列化后的体积太大，是二进制编码的 5 倍多。
   序列化性能太低
   使用 :google Protobuf

## Protobuf

1. 一种轻便高效的结构化数据存储格式，可以用于结构化数据串行化，或者说序列化。它很适合做数据存储或 RPC(远程过程调用 remote procedurecall)数据交换格式
    http +json ->tcp + Protobuf
2. 跨语言
3. 以 message 的方式来管理数据的
4. 高性能，高可靠性
java ->protobuf(编译) ->xx.java -> 编码器  ==>解码器



# Netty 编解码器和 handler 的调用机制
Netty 的主要组件有 Channel、EventLoop、ChannelFuture、ChannelHandler、ChannelPipe

他们都实现了 ChannelInboundHadnler 或者 ChannelOutboundHandler 接口。
在这些类中，channelRead 方法已经被重写了。以入站为例，对于每个从入站 Channel 读取的消息，这个方法会
被调用。随后，它将调用由解码器所提供的 decode()方法进行解码，并将已经解码的字节转发给 ChannelPipeline
中的下一个 ChannelInboundHandler。
## 解码器

## Netty 的 handler 链的调用机制


# 源码解读
1.
```java
EventLoopGroup bossGroup = new NioEventLoopGroup(1);
EventLoopGroup workerGroup = new NioEventLoopGroup();

new NioEventLoopGroup(1)//创建指定线程

//默认cpu核数*2
protected MultithreadEventExecutorGroup(int nThreads, Executor executor, Object... args) {
        this(nThreads, executor, DefaultEventExecutorChooserFactory.INSTANCE, args);
    }



protected MultithreadEventExecutorGroup(int nThreads, Executor executor,
                                            EventExecutorChooserFactory chooserFactory, Object... args) {
        if (nThreads <= 0) {
            throw new IllegalArgumentException(String.format("nThreads: %d (expected: > 0)", nThreads));
        }

        if (executor == null) {
            executor = new ThreadPerTaskExecutor(newDefaultThreadFactory());
        }

        children = new EventExecutor[nThreads];

        for (int i = 0; i < nThreads; i ++) {
            boolean success = false;
            try {
                children[i] = newChild(executor, args);//excutor
                success = true;
            } catch (Exception e) {
                // TODO: Think about if this is a good exception type
                throw new IllegalStateException("failed to create a child event loop", e);
            } finally {
                if (!success) {
                    for (int j = 0; j < i; j ++) {
                        children[j].shutdownGracefully();
                    }

                    for (int j = 0; j < i; j ++) {
                        EventExecutor e = children[j];
                        try {
                            while (!e.isTerminated()) {
                                e.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
                            }
                        } catch (InterruptedException interrupted) {
                            // Let the caller handle the interruption.
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }
            }
        }

        chooser = chooserFactory.newChooser(children);

        final FutureListener<Object> terminationListener = new FutureListener<Object>() {
            @Override
            public void operationComplete(Future<Object> future) throws Exception {
                if (terminatedChildren.incrementAndGet() == children.length) {
                    terminationFuture.setSuccess(null);
                }
            }
        };

        for (EventExecutor e: children) {
            e.terminationFuture().addListener(terminationListener);
        }

        Set<EventExecutor> childrenSet = new LinkedHashSet<EventExecutor>(children.length);
        Collections.addAll(childrenSet, children);
        readonlyChildren = Collections.unmodifiableSet(childrenSet);
    }



```






 












