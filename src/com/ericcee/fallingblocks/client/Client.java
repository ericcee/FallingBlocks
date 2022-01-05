package com.ericcee.fallingblocks.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import com.ericcee.fallingblocks.JSONObjects.Command;
import com.ericcee.fallingblocks.JSONObjects.Score;
import com.ericcee.fallingblocks.JSONObjects.ScoreWithDifficulty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

public class Client {
	private String ip;
	private int port;
	
	public Client(String ip,int port){
		this.ip=ip;
		this.port=port;
	}
	
	public Score requestScores(int diff) throws JsonSyntaxException, IOException, InterruptedException{
		Gson g = new GsonBuilder().disableHtmlEscaping().create();
		Score ret= null;
		Command m = new Command("getScores", "", diff, null,null);
		String dbg;
		int tries=0;
		
		while((dbg=sendData(g.toJson(m)))==null && tries<=10){
			tries++;
		}
		System.out.println(dbg);
		Command returnCmd = g.fromJson(dbg, Command.class);
		
		return returnCmd.getScore();
	} 
	
	public Command requestSaveEntry(ScoreWithDifficulty score) throws JsonSyntaxException, IOException, InterruptedException{
		score.name=escapeHtml4(score.name);
		Gson g = new GsonBuilder().disableHtmlEscaping().create();
		Command cmd = new Command("saveScore", "", 200, null, score);
		String reqStr = g.toJson(cmd);
		String dbg;
		
		int tries=0;
		while((dbg=sendData(reqStr))==null && tries<=10){
			tries++;
		}
		
		return g.fromJson(dbg, Command.class);
	}
	
	private String sendData(String data) throws IOException{
		
		URL url = new URL(ip+":"+port);
		HttpURLConnection u = (HttpURLConnection)url.openConnection();
		u.setDoOutput(true);
		u.setRequestMethod("POST");
		u.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		
		u.setRequestProperty("Content-Language", "en-US");
		u.setRequestProperty("Content-Length", 
		        Integer.toString(data.getBytes().length));
		
	    
	    DataOutputStream wr = new DataOutputStream (u.getOutputStream());
	    
	    wr.writeBytes(data);
	    wr.flush();
	    wr.close();
	    InputStream is = null;
	    try{
	    	is = u.getInputStream();
	    }
	    catch(IOException e){
	    	return null;
	    }
	    
	    byte [] b  = new byte[1024],full = new byte[100*1024];
	    
	    byte [] lenghtcut;
    	int br=0,len =0;
    	while((br=is.read(b))!=-1){
    		System.arraycopy(b, 0, full, len, br);
    		len+=br;
    	}
    	
    	lenghtcut = Arrays.copyOf(full, len);
    	
    	is.close();
    	u.disconnect();
    	
    	return new String(lenghtcut);
	}
	
	public boolean checkIfServerIsAlive(){
		try{
			URL url = new URL(ip+":"+port);
			URLConnection u = url.openConnection();
			u.setDoOutput(true);
			u.getOutputStream();
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
