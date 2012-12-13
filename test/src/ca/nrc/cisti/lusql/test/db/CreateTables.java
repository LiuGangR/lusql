package ca.nrc.cisti.lusql.test.db;

/*

/*											 
 **  This sample program is described in the Getting Started With Derby Manual
*/
//   ## INITIALIZATION SECTION ##
//  Include the java SQL classes 

import java.sql.*;
import java.io.*;
import java.util.*;

public class CreateTables
{

    public CreateTables()
	{

	}


    // should only have Connection, no driver, url.
    public void loadTables(String connectionURL, String driver, String tableFileName)
	{
	    Properties p = System.getProperties();
	    p.put("derby.infolog.append", "false");
	    //String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	    //String dbName="testdb/jdbcDemoDB";
	    Connection conn = null;
	    Statement statement = null;
	    PreparedStatement psInsert;
	    ResultSet myWishes;
	    ResultSet rs;
	    String printLine = "  __________________________________________________";
	    
	    try	        
	    {
		/*
		**  Load the Derby driver. 
		**     When the embedded Driver is used this action start the Derby engine.
		**  Catch an error and suggest a CLASSPATH problem
		*/
		Class.forName(driver); 
		System.out.println(driver + " loaded. ");
		
	    } 
	    catch(java.lang.ClassNotFoundException e)     
	    {
		System.err.print("ClassNotFoundException: ");
		System.err.println(e.getMessage());
		System.out.println("\n    >>> Please check your CLASSPATH variable   <<<\n");
	    }
	    //  Beginning of Primary DB access section
	    //   ## BOOT DATABASE SECTION ##
	    try {
		// Create (if needed) and connect to the database
		conn = DriverManager.getConnection(connectionURL);		 
		conn.setAutoCommit(false);
		System.out.println("Connected to database ");

		BufferedReader input =  new BufferedReader(new FileReader(tableFileName));

		try 
		{
		    String line = null; 
		    while (( line = input.readLine()) != null)
		    {
			String tableName = line;
			String createString = input.readLine();

			if(tableExists(conn, tableName))
			   {
			       System.out.println(tableName + " exists");
			       continue;
			   }
			   statement = conn.createStatement();
			   statement.execute(createString);
		    }
		}
		finally {
		    input.close();
		}
		conn.commit();
		statement.close();
		conn.close();						
	    }
	    catch (Throwable ex){
		ex.printStackTrace();
	    }

	    
	    System.out.println("Closed connection");
	    
	    //   ## DATABASE SHUTDOWN SECTION ## 
	    /*** In embedded mode, an application should shut down Derby.
		 Shutdown throws the XJ015 exception to confirm success. ***/			
	    try
	    {
	    if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
		boolean gotSQLExc = false;
		try {
		    DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException se)  {	
		    if ( se.getSQLState().equals("XJ015") ) {		
                     gotSQLExc = true;
                  }
               }
               if (!gotSQLExc) {
               	  System.out.println("Database did not shut down normally");
               }  else  {
                  System.out.println("Database shut down normally");	
               }  
            }
            
         //  Beginning of the primary catch block: uses errorPrint method
	}  catch (Throwable e)  {   
            /*       Catch all exceptions and pass them to 
            **       the exception reporting method             */
            System.out.println(" . . . exception thrown:");
	    e.printStackTrace();
            errorPrint(e);
         }
         System.out.println("Getting Started With Derby JDBC program ending.");
      }
     //   ## DERBY EXCEPTION REPORTING CLASSES  ## 
    /***     Exception reporting methods
    **      with special handling of SQLExceptions
    ***/
      static void errorPrint(Throwable e) {
         if (e instanceof SQLException) 
            SQLExceptionPrint((SQLException)e);
         else {
            System.out.println("A non SQL error occured.");
            e.printStackTrace();
         }   
      }  // END errorPrint 

    //  Iterates through a stack of SQLExceptions 
      static void SQLExceptionPrint(SQLException sqle) {
         while (sqle != null) {
            System.out.println("\n---SQLException Caught---\n");
            System.out.println("SQLState:   " + (sqle).getSQLState());
            System.out.println("Severity: " + (sqle).getErrorCode());
            System.out.println("Message:  " + (sqle).getMessage()); 
            sqle.printStackTrace();  
            sqle = sqle.getNextException();
         }
   }  //  END SQLExceptionPrint   	


    static boolean tableExists(Connection conn, String name)
	throws SQLException
	{
	    return tableExists(conn, null, "APP", name);
	}

    static boolean tableExists(Connection conn, String catalog, 
			       String schema, String name)
	throws SQLException
	{
	    DatabaseMetaData md= conn.getMetaData();
	    ResultSet rs = md.getTables(catalog, schema,null,null);

	    while(rs.next())
	    {
		if(rs.getString("TABLE_NAME").equals(name))
		    return true;
	    }
	    return false;
	}


    Map<String, String> makeCreateTableSQL(String filename)
	{
	    Map<String, String>sql = new HashMap<String, String>();
	    try
	    {
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		String line;
		boolean first = true;
		String key = null;
		while((line = r.readLine()) != null)
		{
		    if(first == true)
		    {
			first = !first;
			key = line.trim();
		    }
		    else
		    {
			first = !first;
			sql.put(key, line);
		    }
			
		}
		r.close();

	    }
	    catch(Throwable t)
		{
		    t.printStackTrace();
		    return null;
		}
	    
	    return sql;
		
	}


    /**
     * Describe <code>main</code> method here.
     *
     * @param args a <code>String</code> value
     */
    public static final void main(final String[] args) {
	CreateTables ct = new CreateTables();
	    Map<String, String> sql = ct.makeCreateTableSQL(args[0]);
	    System.out.println(sql);
    }

}
