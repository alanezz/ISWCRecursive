package arq.examples;

import org.apache.jena.riot.RDFDataMgr;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

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
import com.sun.xml.internal.bind.v2.runtime.property.PropertyFactory;

public class ExQuerySelect1 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String directory = "/Users/adriansotosuarez/Desktop/TDB";
		Dataset dataset = TDBFactory.createDataset(directory);

		
		 
		/*BufferedReader br = new BufferedReader(new FileReader("/Users/adriansotosuarez/Documents/Investigación/Otros/Prov/Prov1/prov.txt"));

		Graph g = GraphFactory.createDefaultGraph();
		String sCurrentLine;
		while ((sCurrentLine = br.readLine()) != null) {
			String[] aux = sCurrentLine.split("\\|");
			if(aux.length >2)
				g.add(new Triple(NodeFactory.createURI(aux[0]),NodeFactory.createURI(aux[1]),NodeFactory.createURI(aux[2])));
			else
				System.out.println(aux[0]);
			
		}
		DatasetGraph dsg = DatasetGraphFactory.create(g);*/
		
		//String queryString = "SELECT ?x ?y ?z WHERE {?x ?y  <http://data.linkedmdb.org/resource/movie/film>}";
		
		String queryString = "SELECT *"
								+"{"
								+  "<http://data.linkedmdb.org/resource/actor/29539>  (^<http://data.linkedmdb.org/resource/movie/actor> / <http://data.linkedmdb.org/resource/movie/actor>)*  ?z"
								+"}";
		
		
		/*String queryString = "SELECT *"
				+"{"
				+  "?x  (<http://relationship.com/wasGeneratedBy> / <http://relationship.com/used>)*  ?z"
				+"}";
		//String queryString = "SELECT ?x ?y ?z WHERE {?x ?y <http://edit.com/comment525401652> . <http://edit.com/comment525401652> <http://relationship.com/used> ?z}";
		
		
		/*String queryString = "WITH RECURSIVE http://db.ing.puc.cl/temp AS"
				+ "{"
				+ "CONSTRUCT {?x <http://relationship.com/WU> ?z} "
				+ "FROM <"+Quad.defaultGraphIRI+"> "
				+ "FROM NAMED <http://db.ing.puc.cl/temp> "
				+ "WHERE "
				+ "{"
					+ "{?x <http://relationship.com/wasGeneratedBy> ?y . ?y <http://relationship.com/used> ?z} "
					+ "UNION "
					+ "{{?y <http://relationship.com/wasGeneratedBy> ?r . ?r <http://relationship.com/used> ?z .} . GRAPH <http://db.ing.puc.cl/temp> {?x <http://relationship.com/WU> ?y}} "
				+ "}"
			+ "}"
			+ "SELECT ?x ?y ?z FROM NAMED <http://db.ing.puc.cl/temp> "
			+ "WHERE {GRAPH <http://db.ing.puc.cl/temp> {?x ?y ?z}}";*/
		
		/*String queryString = "WITH RECURSIVE http://db.ing.puc.cl/temp AS"
		+ "{"
		+ "CONSTRUCT {?x <http://relationship.com/wasRevisionOf> ?z} "
		+ "FROM <"+Quad.defaultGraphIRI+"> "
		+ "FROM NAMED <http://db.ing.puc.cl/temp> "
		+ "WHERE "
		+ "{"
			+ "{?x <http://relationship.com/wasRevisionOf> ?z} "
			+ "UNION "
			+ "{{?y <http://relationship.com/wasRevisionOf> ?z} . GRAPH <http://db.ing.puc.cl/temp> {?x <http://relationship.com/wasRevisionOf> ?y}} "
		+ "}"
	+ "}"
	+ "SELECT ?x ?y ?z FROM NAMED <http://db.ing.puc.cl/temp> "
	+ "WHERE {GRAPH <http://db.ing.puc.cl/temp> {?x ?y ?z}}";*/
		
		//KB = 29539
		
		//String queryString = "SELECT DISTINCT ?z WHERE{?x <http://data.linkedmdb.org/resource/movie/actor> ?z}";
		/*String queryString = "WITH RECURSIVE http://db.ing.puc.cl/temp AS"
		+ "{"
		+ "CONSTRUCT {<http://data.linkedmdb.org/resource/actor/29539> <http://relationship.com/collab> ?act} "
		+ "FROM NAMED <http://db.ing.puc.cl/temp> "
		+ "FROM <"+Quad.defaultGraphIRI+"> "
		+ "WHERE "
		+ "{"
			+ "{?mov <http://data.linkedmdb.org/resource/movie/actor> <http://data.linkedmdb.org/resource/actor/29539> . ?mov <http://data.linkedmdb.org/resource/movie/actor> ?act} "
			+ "UNION "
			+ "{{?mov <http://data.linkedmdb.org/resource/movie/actor> ?act1} . {?mov <http://data.linkedmdb.org/resource/movie/actor> ?act} . GRAPH <http://db.ing.puc.cl/temp> {<http://data.linkedmdb.org/resource/actor/29539> <http://relationship.com/collab> ?act1}} "
		+ "}"
	+ "}"
	+ "SELECT ?z FROM NAMED <http://db.ing.puc.cl/temp> "
	+ "WHERE {GRAPH <http://db.ing.puc.cl/temp> {<http://data.linkedmdb.org/resource/actor/29539> <http://relationship.com/collab> ?z}}";
	*/	
		
	/*String queryString = "WITH RECURSIVE http://db.ing.puc.cl/temp2 AS"
				+ "{"
					+ "CONSTRUCT {?x ?g ?z} "
					+"FROM <"+Quad.defaultGraphIRI+"> "
					+ "FROM NAMED <http://db.ing.puc.cl/temp2> "
					+ "WHERE "
					+ "{"
						+ "{?x <http://relationship.com/wasRevisionOf> ?z . "
						+ "?x <http://relationship.com/wasGeneratedBy> ?q . "
						+ "?q <http://relationship.com/wasAssociatedWith> ?g}"
					+ "UNION "
						+ "{?x <http://relationship.com/wasRevisionOf> ?y . "
						+ "?x <http://relationship.com/wasGeneratedBy> ?q . "
						+ "?q <http://relationship.com/wasAssociatedWith> ?g . "  
						+ "GRAPH <http://db.ing.puc.cl/temp2> {?y ?g ?z}} "
					+ "}"
				+ "}"
				+ "SELECT ?x ?y ?z FROM NAMED <http://db.ing.puc.cl/temp2> "
				+ "WHERE {GRAPH <http://db.ing.puc.cl/temp2> {?x ?y ?z}}";
		*/
		
		Query query = QueryFactory.create(queryString);
		//Dataset ds = DatasetFactory.create(dsg);
		long start = System.currentTimeMillis();
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		//QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		
		
		System.out.println("holi");
		int triples = 0;
		try {
            // Assumption: it's a SELECT query.
            ResultSet rs = qexec.execSelect() ;
            
            
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
