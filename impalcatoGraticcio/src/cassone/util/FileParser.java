package cassone.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;

import cassone.model.Condizione;

public class FileParser {

  private BufferedReader in;
  private String fileName;
  private LinkedHashSet sezioni; 


  public FileParser() {}
  
  
  /**
   * 
   * @return
   */
  public int getNumeroCondizioni(){
  	Iterator iter = sezioni.iterator();
  	Set lista;
  	if ( iter.hasNext() ){
  		lista = ( Set )iter.next();
  		return lista.size();
  	}
  	else return 0;
  }
  
  /**
   * 
   * @return
   */
  public int getNumeroSezioni(){
 	return sezioni.size();
  }
  
    

  
  
  /**
   *
   * @param fileName
   * @throws java.lang.Exception
   */
  public FileParser( String fileName )  throws Exception {
    this.fileName = fileName;
    String line = null;
    String temp;
    LinkedHashSet condizioni = new LinkedHashSet();
    sezioni = new LinkedHashSet();
    open();    
    
    while ( ( line = in.readLine() ) != null ){
    	//se siamo all'interno di una sezione
    	temp = line.toLowerCase();
    	if ( temp.indexOf( "end" ) == -1 && !temp.equals( "" )  ){
    		condizioni.add( parseRow( line ) );    		
    	}
    	//se c'è una nuova sezione
    	else{
    		sezioni.add( condizioni );
    		condizioni = new LinkedHashSet();
    	}    	
    }   
    
    in.close();
  }

  /**
   *
   * @param line
   * @return
   * @throws java.lang.Exception
   */
  public Condizione parseRow( String line )  throws Exception {

    StringTokenizer token = new StringTokenizer( line );
    if ( token.countTokens() != 7 ){
     throw new Exception( "Numero di token uguale a " + token.countTokens() );
    }
    Condizione cond = new Condizione();
    cond.id = Double.parseDouble( token.nextToken() );
    cond.descrizione = token.nextToken();
    cond.N =  Double.parseDouble( token.nextToken() );
    cond.V = Double.parseDouble( token.nextToken() );
    cond.M = Double.parseDouble( token.nextToken() );
    cond.Nsoletta = Double.parseDouble( token.nextToken() );


    return cond;
  }

  /**
   *
   * @return
   */
  public Iterator getSezioni(){ return sezioni.iterator(); }

  /**
   *
   * @throws FileNotFoundException
   */
  public void open() throws FileNotFoundException {
    in = new BufferedReader(new FileReader( fileName ));

  }

}


