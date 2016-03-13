package com.ht.baselib.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 常规Base64加密解密实用工具类
 * @author zmingchun
 * @version 1.0(2015/10/22)
 */
public class Base64Util {
      /**
       *  把普通字符编码成64bit
       * @param s 待处理字符串
       * @return
       */
    public static String strToBase64(String s){
          try {
              if (s == null){
                  return null;
              }else{
                    return Base64.encodeBytes(s.getBytes("UTF-8"));
              }
          }catch (UnsupportedEncodingException e) {
                  e.printStackTrace();
            }
            return null;
    }
    /**
       *  把普通字节数组编码成64编码字符串
       * @param s 待处理字符串
       * @return
       */
    public static String bytesToBase64(byte[] s){
          if (s == null){
                return null;
            }else{
                  return Base64.encodeBytes(s);
            }
    }
    /**
     * 把64bit编码转化成普通字符串
     * @param s 待处理字符串
     * @return
     */
    public static String base64ToStr(String s) {
          try {
              if (s == null){
                  return null;
              }else{
                  byte[] b = Base64.decode(s);
                  return new String(b, "UTF-8");
              }
        }catch (IOException e) {
                  e.printStackTrace();
            }
            return null;
    }
    /**
     * 把64bit编码转化成字节数组
     * @param s 待处理字符串
     * @return
     */
    public static byte[] base64ToBytes(String s) {
          try {
              if (s == null){
                  return null;
              }else{
                  byte[] b = Base64.decode(s.getBytes("UTF-8"));
                  return b;
              }
        }catch (IOException e) {
                  e.printStackTrace();
            }
            return null;
    }
      /**
       * 将一个输入流转化为字符串
       * @param tInputStream 输入流
       */
      public static String getStreamToString(InputStream tInputStream) {
            if (tInputStream != null) {
                  try {
                        BufferedReader tBufferedReader = new BufferedReader(
                                    new InputStreamReader(tInputStream));
                        StringBuffer tStringBuffer = new StringBuffer();
                        String sTempOneLine = new String("");
                        while ((sTempOneLine = tBufferedReader.readLine()) != null) {
                              tStringBuffer.append(sTempOneLine);
                        }
                        return tStringBuffer.toString();
                  } catch (Exception ex) {
                        ex.printStackTrace();
                  }
            }
            return null;
      }
      /**
       * 将字符串转换为流
       * @param s 待处理字符串
       * @return
       */
      public static ByteArrayInputStream getStringToStream(String s) { 
          if (s != null && !s.equals("")) { 
              try { 
                  ByteArrayInputStream stringInputStream = new ByteArrayInputStream( 
                          s.getBytes()); 
                  return stringInputStream; 
              } catch (Exception e) { 
                  e.printStackTrace(); 
              } 
          } 
          return null; 
      }
      /**
       * 流转字节数组
       * @param input 输入流
       * @return
       * @throws IOException 发生I/O读写异常
       */
        public static byte[] steamToByte(InputStream input) throws IOException{
              ByteArrayOutputStream baos = new ByteArrayOutputStream();
              int len = 0;
              byte[] b = new byte[1024];
              while ((len = input.read(b, 0, b.length)) != -1) {                     
                  baos.write(b, 0, len);
              }
              byte[] buffer =  baos.toByteArray();
              return buffer;
        }
        /**
         * 字节数组转流
         * @param buf 字节数组
         * @return
         */
        public static final ByteArrayInputStream byteTostream(byte[] buf) {  
              return new ByteArrayInputStream(buf);  
        }
        /**
         * 转换base64字符串为文件
         * @param base64str 待处理字符串
         * @param savePath 文件生成路径
         */
        public static void saveBase64strToFile(String base64str, String savePath){
              if (base64str == null){
                  return ;
              }
              try 
              {           
                  byte[] b = Base64.decode(base64str);
                  for(int i=0;i<b.length;++i)
                  {
                      if(b[i]<0){
                          b[i]+=256;
                      }
                  }            
                  OutputStream out = new FileOutputStream(savePath);    
                  out.write(b);
                  out.flush();
                  out.close();           
              } 
              catch (Exception e){
                 e.printStackTrace();
              }
          }
}