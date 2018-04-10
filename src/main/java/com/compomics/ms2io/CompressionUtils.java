/*
 * Copyright 2018 Genet.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.compomics.ms2io;



import java.io.ByteArrayOutputStream;  
 import java.io.File;  
 import java.io.FileInputStream;  
 import java.io.FileOutputStream;  
 import java.io.IOException;  
 import java.io.InputStream;  
 import java.util.List;  
 import java.util.Map;  
import java.util.logging.Logger;
 import java.util.zip.DataFormatException;  
 import java.util.zip.Deflater;  
 import java.util.zip.Inflater;  
/**
 *
 * @author Genet
 */
public class CompressionUtils {
    
    
   private static final Logger LOG = Logger.getLogger(CompressionUtils.class.getName());  
  public static byte[] compress(byte[] data) throws IOException {  
   Deflater deflater = new Deflater();  
   deflater.setInput(data);  
   ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);   
   deflater.finish();  
   byte[] buffer = new byte[1024];   
   while (!deflater.finished()) {  
    int count = deflater.deflate(buffer); // returns the generated code... index  
    outputStream.write(buffer, 0, count);   
   }  
   outputStream.close();  
   byte[] output = outputStream.toByteArray();  
   LOG.info("Original: " + data.length / 1024 + " Kb");  
   LOG.info("Compressed: " + output.length / 1024 + " Kb");  
   return output;  
  }  
  public static byte[] decompress(byte[] data) throws IOException, DataFormatException {  
   Inflater inflater = new Inflater();   
   inflater.setInput(data);  
   ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);  
   byte[] buffer = new byte[1024];  
   while (!inflater.finished()) {  
    int count = inflater.inflate(buffer);  
    outputStream.write(buffer, 0, count);  
   }  
   outputStream.close();  
   byte[] output = outputStream.toByteArray();  
   LOG.info("Original: " + data.length);  
   LOG.info("Compressed: " + output.length);  
   return output;  
  }  
 



    
}
