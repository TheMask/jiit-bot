/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proj;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.jsoup.Jsoup;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.net.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 * @author raj
 */
public class Crawl{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        List<String> dis = new ArrayList<String>();
        Document doc = Jsoup.connect("http://www.jiit.ac.in/department.php").timeout(0).get();
        
       Elements fac=doc.getElementsByClass("services_list1");
      
        Elements deplinks= fac.select("a[href]");
        for(Element link: deplinks)
        {   int i=link.attr("href").indexOf("dep.php");
            if(i!= -1 )
            {
            String linkstr = link.attr("href");
         System.out.println(linkstr); 
            dis.add(linkstr.toString());
            }
        }
        getfaculty(dis);
    }
    
    public  static void getfaculty (List<String> a) throws Exception
    {int i=0;
        Iterator it=a.iterator();
       while(it.hasNext() && i++!=1)
        { 
            String str1=(String)it.next();
           // System.out.println(str1);
            
            String str2;
            str2=str1.replaceAll(" ","%20");
            str2="http://www.jiit.ac.in/"+str2;
              // System.out.println(str2); 
                //Socket s = new Socket();
     // System.out.println(s.getSoTimeout());
               
                 Document doc = Jsoup.connect(str2).timeout(0).get();
                Elements fac1=doc.getElementsByClass("services_list1");
       String linkstr="";
        Elements l= fac1.select("a[href]");
        for(Element link: l)
        {   int j=link.attr("href").indexOf("faculty.php");
            if(j!= -1 )
            {
            linkstr = link.attr("href");
         
            linkstr="http://www.jiit.ac.in/"+linkstr;
            System.out.println(linkstr); 
            }
        }
                
        Document doc1= Jsoup.connect(linkstr).timeout(0).get();
        Elements k=doc1.select("div#showFacultyNames").select("a[href]");
        for(Element eachfac: k)
        {
            getinfo(eachfac.attr("href"));
            
        }
       // System.out.println(k); 
              
        }
    }
    
    public static void getinfo(String facultyurl) throws Exception
    {
         facultyurl="http://www.jiit.ac.in/"+facultyurl;
        Document d= Jsoup.connect(facultyurl).timeout(0).get();
        Elements headings=d.select("div.faculty_box");
//Elements headtext=headings.select("strong");
int n=0;
        for(Element htext: headings )
        {   
            Element name=htext.select("strong").get(0);
            Element smallbio=htext.getElementsByTag("p").get(0);
            Element fullbio=htext.getElementsByTag("p").get(7);
            System.out.println(name.text()); 
              System.out.println(smallbio);
                   System.out.println(fullbio.text());
                    insertdb(name.text(),smallbio.toString(),fullbio.text());
        }
        
      
      
    }            
            
    /*
    public static void findsym(Document d) throws Exception
    {
                 int n;
                 String symstr="";
           Elements item= d.getElementsByClass("resultitem");
              for(Element res:item)
                 {  //System.out.println(res); 
                    Elements item1=res.getElementsByClass("articletitle");
                    System.out.println(item1.text().substring(3)); 
                      Elements item2= res.getElementsByClass("resultsymptoms");
                     Elements item3= item2.select("li");
                 n=0;
                     for(Element sym:item3)
                     {  
                           if(n++==0)
                           symstr= sym.text();
                           else 
                           {symstr=symstr+"--"+sym.text(); 
                           n++;
                           }
                             
                                 
            
                     }
                      System.out.println(symstr); 
                       insertdb(item1.text().substring(3),symstr);
                 }   
              
            
        }*/
    public static Connection conn = null;
     public static String TABLE = "faculty";
    public static void insertdb(String name,String smb,String fmb) throws Exception
    {
           
            String url = "jdbc:mysql://localhost:3306";
             String dbName = "/crawl";
             String driver = "com.mysql.jdbc.Driver";
              String userName = "root";
             String password = "";
              smb=smb.replaceAll("\"","'");
           Class.forName(driver).newInstance();
    conn = DriverManager.getConnection(url+dbName,userName,password);
    
   /* if(disease.startsWith(" "))
    { System.out.println("hahahahahahahah"); 
      disease= disease.replaceFirst(" ", "");
    }*/
          String insertString = "INSERT INTO " + TABLE + " VALUES (\""+name+"\",\""+smb+"\",\""+fmb+"\")";
  System.out.println(insertString);
  
    Statement stmt = conn.createStatement();
    stmt.executeUpdate(insertString);
    stmt.close();
  } 
 
    
  }

    
  
