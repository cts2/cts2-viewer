package edu.mayo.cts2Viewer.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gwt.thirdparty.guava.common.io.Files;

public class CTS2DownloadServlet extends HttpServlet {

    private static final long serialVersionUID = 3457906406134591880L;
    private static Logger logger = Logger.getLogger(CTS2DownloadServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
            handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        processDownloadAlgorithm(request, response);
    }

    private void processDownloadAlgorithm(HttpServletRequest request, HttpServletResponse response) 
    {
        String zipFileName = request.getParameter("ZipFileName");
        
        String downloadType = request.getParameter("downloadType");
        
        String ext = ".xls";
        if ("json".equalsIgnoreCase(downloadType))
        {
        	ext = "_json.txt";
        	zipFileName += "_JSON.zip";
        }
        else if ("xml".equalsIgnoreCase(downloadType))
        {
        	ext = ".xml";
        	zipFileName += "_XML.zip";
        }
        else
        	zipFileName += "_CSV.zip";
        
        String vsNamesStr = request.getParameter("valueSetNames");
        
        String[] valueSets = vsNamesStr.split(",");

        //File algorithmFile = new File(zipPath);
        logger.info("Download requested: " + zipFileName);

        //logger.info("Sending file " + algorithmFile.getAbsolutePath());
        response.setContentType("application/zip");
        //response.setContentLength((int) algorithmFile.length());
        response.setHeader("Content-Disposition",
               "attachment; filename=\"" + zipFileName + "\"");

        
        try 
        {
        	ServletOutputStream out  = response.getOutputStream();
        	ZipOutputStream zipout = new ZipOutputStream(out);
        	
        	for (String vs : valueSets)
        	{
        		zipout.putNextEntry(new ZipEntry(vs + ext));
        		zipout.write(createValueSetContent(downloadType, vs).getBytes());
        		zipout.closeEntry();
        	}
        	
        	zipout.flush();
        	zipout.close();
            out.close();
        } 
        catch (IOException ioe) 
        {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                logger.log(Level.WARNING,
                        "Unable to write the file to the response output stream.", ioe);
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    private String createValueSetContent(String type, String valueSetName)
    {
    	if (valueSetName == null)
    		return "ValueSet Name is null!!";
    	
    	String urlStr = "http://informatics.mayo.edu/cts2/services/xsltserver/transform?xsltname=tsv.xsl" +
    			"&encoding=text/plain" +
    			"&xmlurl=http://informatics.mayo.edu/cts2/services/phinvads/valueset/" + 
    			valueSetName + "/resolution";
    	
        if ("json".equalsIgnoreCase(type))
        {
        	urlStr = "http://informatics.mayo.edu/cts2/services/phinvads/valueset/" + 
        			valueSetName + "/resolution?format=json";
        }
        else if ("xml".equalsIgnoreCase(type))
        {
        	urlStr = "http://informatics.mayo.edu/cts2/services/phinvads/valueset/" + 
        			valueSetName + "/resolution";
        }

    	String msg = null;
    	try 
    	{
			return getContentResult(new URL(urlStr));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg = e.getMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg = e.getMessage();
		}
    	
    	return "Failed to get content for value set '" + valueSetName + "'\n" + msg;
    }
    
    private static String getContentResult(URL url) throws IOException
    {
        InputStream in = url.openStream();
        StringBuffer sb = new StringBuffer();

        byte [] buffer = new byte[256];

        while(true){
            int byteRead = in.read(buffer);
            if(byteRead == -1)
                break;
            for(int i = 0; i < byteRead; i++){
                sb.append((char)buffer[i]);
            }
        }
        return sb.toString();
    }

}
