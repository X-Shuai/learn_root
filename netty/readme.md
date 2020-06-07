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


