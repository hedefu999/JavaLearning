package com.concurrency.javadxcbchxjs;

import java.io.*;

public class _8PipedCommunication {
    //3.1.12 管道通信 - 字节流
    static class SimplePipedWriterReader{
        static class ReadWriter{
            public void write(PipedOutputStream out){
                try {
                    System.out.println("write");
                    for (int i = 0; i < 30; i++) {
                        String outData = ""+(i+1);
                        out.write(outData.getBytes());
                    }
                    System.out.println();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            public void read(PipedInputStream input){
                try {
                    System.out.println("read");
                    byte[] dataBytes = new byte[20];
                    /**
                     * 这里并没有同步等待的机制，但读线程会在下面一行一直卡到写线程写完20字节的数据，才会继续向下走
                     * java.io.PipedInputStream#read(byte[], int, int)
                     * 在java.io.PipedInputStream#read()方法就是读线程在等待第一个输入的操作
                     * JDK为开发者做好了线程的同步通知唤醒
                     */
                    int readLength = input.read(dataBytes);
                    System.out.println("first readLength = "+readLength);
                    while ((readLength ) != -1){
                        String data = new String(dataBytes,0,readLength);
                        System.out.println(data);
                        readLength = input.read(dataBytes);
                        System.out.println("readLenght = "+readLength);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        static class WriteThread extends Thread{
            private ReadWriter readWriter;
            private PipedOutputStream outputStream;
            public WriteThread(ReadWriter readWriter, PipedOutputStream outputStream){
                this.readWriter = readWriter;
                this.outputStream = outputStream;
            }
            @Override
            public void run() {
                readWriter.write(outputStream);
            }
        }
        static class ReadThread extends Thread{
            private ReadWriter readWriter;
            private PipedInputStream inputStream;
            public ReadThread(ReadWriter readWriter, PipedInputStream outputStream){
                this.readWriter = readWriter;
                this.inputStream = outputStream;
            }
            @Override
            public void run() {
                readWriter.read(inputStream);
            }
        }
        public static void main(String[] args) {
            try {
                ReadWriter readWriter = new ReadWriter();
                PipedInputStream inputStream = new PipedInputStream();
                PipedOutputStream outputStream = new PipedOutputStream();
                //缺少连接操作会报：java.io.IOException: Pipe not connected，以下任选一行
                //outputStream.connect(inputStream);
                inputStream.connect(outputStream);
                ReadThread readThread = new ReadThread(readWriter, inputStream);
                WriteThread writeThread = new WriteThread(readWriter,outputStream);
                readThread.start();
                Thread.sleep(20000);//读线程不论是先启动还是后启动都会等到写线程写数据
                writeThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //3.1.13 管道通信 字符流
    static class PipedCommuByChar{
        static class ReadWriter{
            public void write(PipedWriter out){
                try {
                    System.out.println("write");
                    for (int i = 0; i < 30; i++) {
                        String outData = ""+(i+1);
                        out.write(outData);
                    }
                    System.out.println();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            public void read(PipedReader input){
                try {
                    System.out.println("read");
                    char[] chars = new char[20];
                    int readLength = input.read(chars);
                    System.out.println("first readLength = "+readLength);
                    while ((readLength ) != -1){
                        String data = new String(chars,0,readLength);
                        System.out.println(data);
                        readLength = input.read(chars);
                        System.out.println("readLenght = "+readLength);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        public static void main(String[] args) throws Exception {
            ReadWriter readWriter = new ReadWriter();
            PipedReader pipedReader = new PipedReader();
            PipedWriter pipedWriter = new PipedWriter();
            pipedReader.connect(pipedWriter);
            new Thread(){
                @Override
                public void run() {
                    readWriter.read(pipedReader);
                }
            }.start();
            Thread.sleep(2000);
            new Thread(){
                @Override
                public void run() {
                    readWriter.write(pipedWriter);
                }
            }.start();


        }
    }
}
