package ca.nrc.cisti.lusql.test.db;

/*
     Derby - WwdEmbedded.java

       Licensed to the Apache Software Foundation (ASF) under one
           or more contributor license agreements.  See the NOTICE file
           distributed with this work for additional information
           regarding copyright ownership.  The ASF licenses this file
           to you under the Apache License, Version 2.0 (the
           "License"); you may not use this file except in compliance
           with the License.  You may obtain a copy of the License at

             http://www.apache.org/licenses/LICENSE-2.0

           Unless required by applicable law or agreed to in writing,
           software distributed under the License is distributed on an
           "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
           KIND, either express or implied.  See the License for the
           specific language governing permissions and limitations
           under the License.    

*/
/*											 
 **  This sample program is described in the Getting Started With Derby Manual
*/
//   ## INITIALIZATION SECTION ##
//  Include the java SQL classes 

import java.sql.*;
import java.io.*;
import java.util.*;

public class DerbyTest2
{
    public static void main(String[] args)
   {
       Properties p = System.getProperties();
       p.put("derby.infolog.append", "false");
   //   ## DEFINE VARIABLES SECTION ##
   // define the driver to use 
      String driver = "org.apache.derby.jdbc.EmbeddedDriver";
   // the database name  
      String dbName="testdb/jdbcDemoDB";
   // define the Derby connection URL to use 
      String connectionURL = "jdbc:derby:" + dbName + ";create=true";

      Connection conn = null;
      Statement s;
      PreparedStatement psInsert;
      ResultSet myWishes;
      ResultSet rs;
      String printLine = "  __________________________________________________";
      
      //   ## LOAD DRIVER SECTION ##
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
            System.out.println("Connected to database " + dbName);
            //   Create a statement to issue simple commands.  
            s = conn.createStatement();
	    boolean tableExists = false;

	    CreateTables ct = new CreateTables();
	    Map<String, String> sql = ct.makeCreateTableSQL(args[0]);

	    Iterator<String>it = sql.keySet().iterator();
	    while(it.hasNext())
	    {
		String tableName = it.next();
		String value = sql.get(tableName);
		
		if(!tableExists(conn, tableName))
		{ 
                  System.out.println (" . . . . creating table");
		  System.out.println(value);
                  s.execute(value);
		}
	    }
	    s.close();
	    
            
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

    static void printMetadata(Connection conn)
	throws SQLException
	{
	    DatabaseMetaData md= conn.getMetaData();
	    ResultSet rs = md.getCatalogs();
	    System.out.println("--Catalog--");
	    printResultSets(rs,1);

	    System.out.println("--Schemas--");
	    rs= md.getSchemas();
	    printResultSets(rs,1);

	    System.out.println("--Tables--");
	    rs = md.getTables(null,"APP",null,null);
	    printResultSets(rs,1);
	}

    
    static void printResultSets(ResultSet rs, int depth)
	throws SQLException
	{
	    StringBuffer sb = new StringBuffer();
	    for(int i=0; i<depth; i++)
		sb.append("   ");
	    int i=0;

	    while(rs.next())
	    {
		sb.append(getResultSet(rs, depth));
	    }
	    System.out.println(sb);
	}
	
    static String getResultSet(ResultSet rs, int depth)
	throws SQLException
	{
	    ResultSetMetaData rsmd = rs.getMetaData();
	    StringBuffer sb = new StringBuffer();
	    for(int i=1; i<=rsmd.getColumnCount(); i++)
		sb.append(rsmd.getColumnCount() + ":"
			  + i + ":" + rsmd.getColumnName(i)
			  + "="
			  + rs.getString(i) + "; ");
	    return sb.toString() + "\n";
	    
	}

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
	
}
