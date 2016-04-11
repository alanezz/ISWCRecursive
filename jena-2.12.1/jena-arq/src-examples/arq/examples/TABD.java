package arq.examples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.core.Quad;
import com.hp.hpl.jena.tdb.TDBFactory;

public class TABD {

	/**
	 * This is for tdb recursive queries.
	 * You have to use MAT option in QueryExecutionFactory.java (jena.query)
	 * 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String directory = "/Users/adriansotosuarez/Desktop/Bind";
		Dataset dataset = TDBFactory.createDataset(directory);
		
		String queryString = "PREFIX  dc:  <http://purl.org/dc/elements/1.1/>" +
							"PREFIX  ns:  <http://example.org/ns#>" +  
							"SELECT  ?title ?price" +
							"{  ?x ns:price ?p ." +
							"   ?x ns:discount ?discount" +
							"   BIND (?p*(1-?discount) AS ?price)" +
							"   FILTER(?price < 20)" +
							"   ?x dc:title ?title . " +
							"}";
		
		String nobindquery = "SELECT * WHERE {?x ?y ?z}";

		
		Query query = QueryFactory.create(queryString);
		long start = System.currentTimeMillis();
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		
		int triples = 0;
		try {
            // Assumption: it's a SELECT query.
            ResultSet rs = qexec.execSelect() ;
            
            
            // The order of results is undefined. 
            for ( ; rs.hasNext() ; )
            {
                QuerySolution rb = rs.nextSolution() ;
                
                // Get title - variable names do not include the '?' (or '$')
                RDFNode x = rb.get("title") ;
                RDFNode y = rb.get("price") ;
                RDFNode z = rb.get("z") ;
                
                
                // Check the type of the result value

                // Uncomment to watch results
                System.out.println(x+" | "+y+" | "+z) ;
                triples++;
                
				

            }
            System.out.println(triples);
        }
        finally
        {
            // QueryExecution objects should be closed to free any system resources 
            qexec.close() ;
        }
		long time = System.currentTimeMillis() - start;
		System.out.printf("Took: "+time);
        
        
        
        
        
        
        //br.close();
	}

}
