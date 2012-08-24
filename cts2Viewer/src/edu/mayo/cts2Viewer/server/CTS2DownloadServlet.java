package edu.mayo.cts2Viewer.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.mayo.bsi.cts.cts2connector.cts2search.ConvenienceMethods;
import edu.mayo.bsi.cts.cts2connector.cts2search.aux.CTS2Utils;
import edu.mayo.bsi.cts.cts2connector.cts2search.aux.ServiceResultFormat;

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
        String zipFileName = null;

        String ext = ".xls";
        if ("json".equalsIgnoreCase(request.getParameter("downloadType"))) 
        {
        	ext = ".json";
        	zipFileName = request.getParameter("ZipFileName") + "_JSON.zip"; 
        }
        else if ("xml".equalsIgnoreCase(request.getParameter("downloadType")))   
        { 
        	ext = ".xml"; 
        	zipFileName = request.getParameter("ZipFileName") + "_XML.zip";  
        }
        else 
        	zipFileName = request.getParameter("ZipFileName") +  "_CSV.zip";
        
        //String vsNamesStr = request.getParameter("valueSetNames");
         
        if (CTS2Utils.isNull(request.getParameter("valueSetNames")))
        	return; 
        
        String[] valueSets = request.getParameter("valueSetNames").split(",");

        //File algorithmFile = new File(zipPath);
        logger.info("Download requested: " + zipFileName);

        //logger.info("Sending file " + algorithmFile.getAbsolutePath());
        response.setContentType("application/zip");
        //response.setContentLength((int) algorithmFile.length());
        response.setHeader("Content-Disposition",
               "attachment; filename=\"" + zipFileName + "\""); 

        initCM(request.getParameter("serviceName"));
        
        try 
        {
        	ServletOutputStream out  = response.getOutputStream();
        	ZipOutputStream zipout = new ZipOutputStream(out);
        	
        	for (String vs : valueSets)
        	{
        		zipout.putNextEntry(new ZipEntry(vs + ext));
        		zipout.write(createValueSetContent(request.getParameter("downloadType"), vs).getBytes());
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
    	String msg = null;
    	try
    	{
    		return cm.getValueSetMembers(valueSetName, getFormatFromString(type));
    	}
    	catch(Exception e)
    	{
    		msg = e.getMessage();
    		e.printStackTrace();
    	}
    	
    	return "Failed to get content for value set '" + valueSetName + "'\n" + msg;
    }
    
    private ServiceResultFormat getFormatFromString(String formatType)
    {
    	if (CTS2Utils.isNull(formatType))
    		return ServiceResultFormat.XML;
    	
    	if (formatType.toLowerCase().indexOf("json") != -1)
    		return ServiceResultFormat.JSON;
    	
    	if (formatType.toLowerCase().indexOf("csv") != -1)
    		return ServiceResultFormat.CSV;

    	return ServiceResultFormat.XML;
    }
    
    /*
    
        private String createValueSetContent(String type, String valueSetName)
    {
    	String msg = null;
    	
    	
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
   */
    
	private static final String SERVER_PROPERTIES_FILE = "CTS2Profiles.properties";
	
	private ConvenienceMethods cm = null;

	private void initCM(String serviceName)
	{
			try 
			{
				if (this.cm == null)
					this.cm = ConvenienceMethods.instance(this.getServletContext().getContextPath() + "data/" + SERVER_PROPERTIES_FILE);
				
				if ((!CTS2Utils.isNull(serviceName))&&(!(cm.getCurrentProfileName().equals(serviceName))))
					cm.setCurrentProfileName(serviceName);
			} 
			catch (Exception ex) 
			{
					logger.log(Level.SEVERE, ex.getMessage(), ex);
			}
	}
}
