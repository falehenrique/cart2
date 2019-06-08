package br.com.exmart.util;

import java.util.*;
import java.io.*;
class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    
    StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }
    
    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null){
            }
            } catch (IOException ioe)
              {
                ioe.printStackTrace();  
              }
    }
}
public class ExecShell
{

	public int exec(String cmd){
		int exitVal=-1;
        try
        {            
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            StreamGobbler errorGobbler = new 
                StreamGobbler(proc.getErrorStream(), "ERROR");            
            StreamGobbler outputGobbler = new 
                StreamGobbler(proc.getInputStream(), "OUTPUT");
                
            // kick them off
            errorGobbler.start();
            outputGobbler.start();
                                    
            // any error???
            exitVal = proc.waitFor();
        } catch (Throwable t)
          {
            t.printStackTrace();
          }
		return exitVal;
    }
}