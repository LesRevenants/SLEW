/**
 * Copyright (C) 2015 BITPlan GmbH 
 * 
 * Pater-Delp-Str. 1 
 * D-47877 Willich-Schiefbahn 
 * 
 * http://www.bitplan.com 
 *  
 */

package DataExtraction.wikipedia; 
 
import java.io.File; 
import java.io.IOException; 
import java.net.URL; 
import java.text.DateFormat; 
import java.text.SimpleDateFormat; 
import java.util.Calendar; 
import java.util.Date; 
import java.util.List; 
import java.util.Map; 
import java.util.TimeZone; 
import java.util.logging.Level; 
 
import javax.security.auth.login.LoginException;

import org.wikipedia.Wiki;

import com.bitplan.mediawiki.japi.MediaWikiApiImpl; 
import com.bitplan.mediawiki.japi.MediawikiApi; 
import com.bitplan.mediawiki.japi.SiteInfo; 
import com.bitplan.mediawiki.japi.api.Api; 
import com.bitplan.mediawiki.japi.api.Bl; 
import com.bitplan.mediawiki.japi.api.Delete; 
import com.bitplan.mediawiki.japi.api.Edit; 
import com.bitplan.mediawiki.japi.api.General; 
import com.bitplan.mediawiki.japi.api.Ii; 
import com.bitplan.mediawiki.japi.api.Im; 
import com.bitplan.mediawiki.japi.api.Imageinfo; 
import com.bitplan.mediawiki.japi.api.Img; 
import com.bitplan.mediawiki.japi.api.Iu; 
import com.bitplan.mediawiki.japi.api.Login; 
import com.bitplan.mediawiki.japi.api.P; 
import com.bitplan.mediawiki.japi.api.S; 


public class WikipediaToText extends MediaWikiApiImpl implements MediawikiApi { 
  // delegate to the one class Wiki solution 
  Wiki wiki; 
 
  private String domain; 
 
  private String scriptPath; 
 
  private String siteurl; 
 
  private boolean debug; 
 
  @Override 
  public String getSiteurl() { 
    return siteurl; 
  } 
 
  @Override 
  public void setSiteurl(String siteurl) throws Exception { 
    this.siteurl = siteurl; 
    URL url = new URL(siteurl); 
    domain = url.getHost(); 
    domain = domain.startsWith("www.") ? domain.substring(4) : domain; 
    scriptPath = url.getPath(); 
    if ("".equals(scriptPath)) { 
      scriptPath = "/w"; 
    } 
    String prot = url.getProtocol(); 
    wiki = new Wiki(domain,scriptPath,prot+"://");    
    // zip doesn't work as of 2015-01-20 
    wiki.setUsingCompressedRequests(false); 

  } 
 
  /**
   * @return the scriptPath 
   */ 
  public String getScriptPath() { 
    return scriptPath; 
  } 
 
  /**
   * @param scriptPath 
   *          the scriptPath to set 
   */ 
  public void setScriptPath(String scriptPath) { 
    this.scriptPath = scriptPath; 
  } 
 
  @Override 
  public void init(String siteurl, String scriptPath) throws Exception { 
    setSiteurl(siteurl + "/" + scriptPath); 
  } 
 
  @Override 
  public String getVersion() throws Exception { 
    String version=this.getSiteInfo().getVersion(); 
    return version; 
  } 
 
  /**
   * get the siteinfo 
   * @return 
   * @throws Exception 
   */ 
  public General getGeneral() throws Exception { 
    Map<String, Object> siteinfo = wiki.getSiteInfo(); 
    String xml = (String) siteinfo.get("xml"); 
    Api api = super.fromXML(xml); 
    General result = api.getQuery().getGeneral(); 
    return result; 
  } 
 
  @Override 
  public Login login(String username, String password) throws Exception { 
    wiki.login(username, password); 
    Login result = new Login(); 
    return result; 
  } 
 
  @Override 
  public boolean isLoggedIn() { 
    boolean result = wiki.getCurrentUser() != null; 
    return result; 
  } 
 
  @Override 
  public String getPageContent(String pageTitle) throws Exception { 
    String result = wiki.getPageText(pageTitle); 
    return result; 
  } 
 
  @Override 
  public String getSectionText(String pageTitle, int sectionNumber) 
      throws Exception { 
    return wiki.getSectionText(pageTitle, sectionNumber); 
  } 
 
  @Override 
  public void logout() throws Exception { 
    wiki.logout(); 
  } 
 
  @Override 
  public Edit edit(String pageTitle, String text, String summary) 
      throws Exception { 
    Edit result = this.edit(pageTitle, text, summary, true, false, -2,null,null); 
    return result; 
  } 
 
  @Override 
  public Edit edit(String pageTitle, String text, String summary, 
      boolean minor, boolean bot, int sectionNumber,String sectionTitle, Calendar basetime) 
      throws Exception { 
 
    Edit result = new Edit(); 
    String pageContent = getPageContent(pageTitle); 
    if (pageContent != null && pageContent.contains(protectionMarker)) { 
      LOGGER.log(Level.WARNING, "page " + pageTitle + " is protected!"); 
    } else { 
      if (sectionNumber==-1) 
        wiki.edit(pageTitle, text, sectionTitle, minor, bot, sectionNumber,basetime); 
      else 
        wiki.edit(pageTitle, text, summary, minor, bot, sectionNumber,basetime); 
    } 
    return result; 
  } 
 
  @Override 
  /**
   * get a current IsoTimeStamp 
   * FIXME redundant implementation same functioin com.bitplan.mediawiki.japi.api 
   * @return - the current timestamp 
   */ 
  public String getIsoTimeStamp() { 
    TimeZone tz = TimeZone.getTimeZone("UTC"); 
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX"); 
    df.setTimeZone(tz); 
    String nowAsISO = df.format(new Date()); 
    return nowAsISO; 
  } 
 
  @Override 
  public void setDebug(boolean pDebug) { 
    this.debug = pDebug; 
  } 
 
  @Override 
  public boolean isDebug() { 
    return debug; 
  } 
 
  @Override 
  public void upload(File file, String filename, String contents, String reason) 
      throws Exception { 
    // simply delegate 
    this.wiki.upload(file, filename, contents, reason); 
  } 
 
  @Override 
  public List<P> getAllPages(String apfrom, int aplimit) throws Exception { 
    // FIXME - implement 
    return null; 
  } 
 
  @Override 
  public List<S> getSections(String pageTitle) throws Exception { 
    // FIXME implement 
    return null; 
  } 
 
  @Override 
  public Ii getImageInfo(String pageTitle) throws Exception { 
    // FIXME implement 
    return null; 
  } 
 
  @Override 
  public void upload(Ii ii, String fileName, String pageContent) 
      throws Exception { 
    // FIXME implement 
  } 
 
  @Override 
  public SiteInfo getSiteInfo() throws Exception { 
    // FIXME implement 
    return null; 
  } 
 
  @Override 
  public Delete delete(String title, String reason) throws Exception { 
    Delete result = new Delete(); 
    this.wiki.delete(title, reason); 
    return result; 
  } 
 
  @Override 
  public List<Img> getAllImagesByTimeStamp(String aistart, String aiend, int ailimit) { 
   // FIXME implement 
    return null; 
  } 
 
  @Override 
  public List<Bl> getBacklinks(String pageTitle, String params, int limit) 
      throws Exception { 
    // TODO Auto-generated method stub 
    return null; 
  } 
 
  @Override 
  public List<Iu> getImageUsage(String imageTitle, String params, int limit) 
      throws Exception { 
    // TODO Auto-generated method stub 
    return null; 
  } 
 
  @Override 
  public List<Im> getImagesOnPage(String pageTitle, int imLimit) throws Exception { 
    return null; 
  } 
 
  @Override 
  public List<Ii> getImageInfosForPage(String pageTitle, int limit) 
      throws Exception { 
    // TODO Auto-generated method stub 
    return null; 
  } 

    @Override
    public String getPageHtml(String pageTitle) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   
 
}
