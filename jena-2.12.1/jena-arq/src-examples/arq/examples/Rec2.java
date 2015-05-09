package arq.examples;

import org.apache.jena.riot.RDFDataMgr;

import com.hp.hpl.jena.graph.Triple ;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.ResultSetRewindable;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.sparql.core.DatasetGraph;
import com.hp.hpl.jena.sparql.core.DatasetGraphFactory;
import com.hp.hpl.jena.sparql.core.Quad;
import com.hp.hpl.jena.sparql.graph.GraphFactory;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.DC;
import com.sun.org.apache.xerces.internal.util.URI;
import com.sun.xml.internal.bind.v2.runtime.property.PropertyFactory;

public class Rec2 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String directory = "/Users/adriansotosuarez/Desktop/TDB";
		DatasetGraph dataset = TDBFactory.createDatasetGraph(directory);
		DatasetGraph d =TDBFactory.createDatasetGraph("/Users/adriansotosuarez/Desktop/Rec");
	

		// assume we want the default model, or we could get a named model here

		// read the input file - only needs to be done once
		//String source = "/Users/adriansotosuarez/Desktop/linkedmdb-latest-dump.nt";
		//FileManager.get().readModel( tdb, source, "N-TRIPLES" );
		
		
		Query query = QueryFactory.create("CONSTRUCT {<http://data.linkedmdb.org/resource/cinematographer/1> ?y ?z} WHERE {<http://data.linkedmdb.org/resource/cinematographer/1> ?y ?z}");
		Query query3 = QueryFactory.create("CONSTRUCT {<http://data.linkedmdb.org/resource/cinematographer/2> ?y ?z} WHERE {<http://data.linkedmdb.org/resource/cinematographer/1> ?y ?z}");
		
		//Dataset ds = DatasetFactory.create(dsg);
		//QueryExecution qexec = QueryExecutionFactory.create(query, ds);
		QueryExecution qexec = QueryExecutionFactory.create(query, DatasetFactory.create(dataset));
		
		Iterator<Triple> it = qexec.execConstructTriples();
		
		while(it.hasNext())
		{
			Triple t = it.next();
			d.add(NodeFactory.createURI("http://modelo.com/recursivo"),t.getSubject(),t.getObject(),t.getPredicate());
		}
		
		
		QueryExecution qexec3 = QueryExecutionFactory.create(query3, DatasetFactory.create(dataset));
		
		Iterator<Triple> it2 = qexec3.execConstructTriples();
		
		while(it2.hasNext())
		{
			Triple t = it2.next();
			d.add(NodeFactory.createURI("http://modelo.com/recursivo"),t.getSubject(),t.getObject(),t.getPredicate());
		}
		
		
		Query query2 = QueryFactory.create("SELECT ?x ?y ?z FROM NAMED <http://modelo.com/recursivo> WHERE{GRAPH <http://modelo.com/recursivo> {?x ?y ?z}}");
		QueryExecution qexec2 = QueryExecutionFactory.create(query2,DatasetFactory.create(d));
		try {
            // Assumption: it's a SELECT query.
            ResultSet rs = qexec2.execSelect() ;
            
            
            // The order of results is undefined. 
            for ( ; rs.hasNext() ; )
            {
                QuerySolution rb = rs.nextSolution() ;
                
                // Get title - variable names do not include the '?' (or '$')
                RDFNode x = rb.get("x") ;
                RDFNode y = rb.get("y") ;
                RDFNode z = rb.get("z") ;
                
                
                // Check the type of the result value

                
                System.out.println(x+"|"+y+"|"+z) ;
               
                
				

            }
        }
        finally
        {
            // QueryExecution objects should be closed to free any system resources 
            qexec.close() ;
        }
        
        
        qexec.close();
        qexec2.close();
        qexec3.close();
        dataset.close();
        d.close();
        
        
       
	}

	public static Model createModel()
    {
        Model m = ModelFactory.createDefaultModel() ;
        
        Resource r1 = m.createResource("http://example.org/book#1") ;
        Resource r2 = m.createResource("http://example.org/book#2") ;
        
        
        
        r1.addProperty(DC.title, "SPARQL - the book")
          .addProperty(DC.description, "A book about SPARQL") ;
        
        r2.addProperty(DC.title, "Advanced techniques for SPARQL") ;
        
        return m ;
    }

}
