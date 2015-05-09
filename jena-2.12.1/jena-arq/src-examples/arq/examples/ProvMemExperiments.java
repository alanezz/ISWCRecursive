package arq.examples;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.shared.AddDeniedException;
import com.hp.hpl.jena.sparql.core.DatasetGraph;
import com.hp.hpl.jena.sparql.core.DatasetGraphFactory;
import com.hp.hpl.jena.sparql.graph.GraphFactory;

public class ProvMemExperiments {

	public static void main(String[] args) throws AddDeniedException, IOException {
		// TODO Auto-generated method stub

		BufferedReader br = new BufferedReader(new FileReader("/path/to/provenance/txt"));

		Graph g = GraphFactory.createDefaultGraph();
		String sCurrentLine;
		while ((sCurrentLine = br.readLine()) != null) {
			String[] aux = sCurrentLine.split("\\|");
			if(aux.length >2)
				g.add(new Triple(NodeFactory.createURI(aux[0]),NodeFactory.createURI(aux[1]),NodeFactory.createURI(aux[2])));
			else
				System.out.println(aux[0]);
			
		}
		DatasetGraph dsg = DatasetGraphFactory.create(g);
		
		/*String queryString = "WITH RECURSIVE http://db.ing.puc.cl/temp AS"
				+ "{"
				+ "CONSTRUCT {?x <http://relationship.com/wasRevisionOf> ?z} "
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
		
		/*String queryString = "WITH RECURSIVE http://db.ing.puc.cl/temp AS"
				+ "{"
				+ "CONSTRUCT {?x <http://relationship.com/WU> ?z} "
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
		
		String queryString = "WITH RECURSIVE http://db.ing.puc.cl/temp1 AS"
				+ "{"
				+ "CONSTRUCT {?x ?g ?y} "
					+ "WHERE "
						+ "{?x <http://relationship.com/wasRevisionOf> ?y . "
						+ "?x <http://relationship.com/wasGeneratedBy> ?q . "
						+ "?q <http://relationship.com/wasAssociatedWith> ?g}"
				+ "}"
				+ "WITH RECURSIVE http://db.ing.puc.cl/temp2 AS"
				+ "{"
					+ "CONSTRUCT {?x ?g ?z} "
					+ "FROM NAMED <http://db.ing.puc.cl/temp1> "
					+ "FROM NAMED <http://db.ing.puc.cl/temp2> "
					+ "WHERE "
					+ "{"
						+ "{GRAPH <http://db.ing.puc.cl/temp1> {?x ?g ?z}} "
						+ "UNION "
						+ "{GRAPH <http://db.ing.puc.cl/temp1> {?x ?g ?y} . GRAPH <http://db.ing.puc.cl/temp2> {?y ?g ?z}} "
					+ "}"
				+ "}"
				+ "SELECT ?x ?y ?z FROM NAMED <http://db.ing.puc.cl/temp2> "
				+ "WHERE {GRAPH <http://db.ing.puc.cl/temp2> {?x ?y ?z}}";
		
		//String queryString = "SELECT ?x ?y ?z  WHERE {  { ?x <http://relationship.com/wasRevisionOf> ?z } UNION  {    { ?x <http://relationship.com/wasRevisionOf> ?w1 . ?w1  <http://relationship.com/wasRevisionOf> ?z }    UNION  {    {  ?x <http://relationship.com/wasRevisionOf> ?w1 .  ?w1 <http://relationship.com/wasRevisionOf> ?w2 .  ?w2 <http://relationship.com/wasRevisionOf> ?z  }  UNION  {{  ?x <http://relationship.com/wasRevisionOf> ?w1 .  ?w1 <http://relationship.com/wasRevisionOf> ?w2 .  ?w2 <http://relationship.com/wasRevisionOf> ?w3 .  ?w3 <http://relationship.com/wasRevisionOf> ?z  }  UNION  {{  ?x <http://relationship.com/wasRevisionOf> ?w1 .  ?w1 <http://relationship.com/wasRevisionOf> ?w2 .  ?w2 <http://relationship.com/wasRevisionOf> ?w3 .  ?w3 <http://relationship.com/wasRevisionOf> ?w4 .  ?w4 <http://relationship.com/wasRevisionOf> ?z  }  UNION  {{  ?x <http://relationship.com/wasRevisionOf> ?w1 .  ?w1 <http://relationship.com/wasRevisionOf> ?w2 .  ?w2 <http://relationship.com/wasRevisionOf> ?w3 .  ?w3 <http://relationship.com/wasRevisionOf> ?w4 .  ?w4 <http://relationship.com/wasRevisionOf> ?w5 .  ?w5 <http://relationship.com/wasRevisionOf> ?z  }  UNION  {  ?x <http://relationship.com/wasRevisionOf> ?w1 .  ?w1 <http://relationship.com/wasRevisionOf> ?w2 .  ?w2 <http://relationship.com/wasRevisionOf> ?w3 .  ?w3 <http://relationship.com/wasRevisionOf> ?w4 .  ?w4 <http://relationship.com/wasRevisionOf> ?w5 .  ?w5 <http://relationship.com/wasRevisionOf> ?w6 .  ?w6 <http://relationship.com/wasRevisionOf> ?z  }}}}}}  } ";
		
		/*String queryString = "SELECT *"
				+"{"
				+  "?x  (<http://relationship.com/wasGeneratedBy> / <http://relationship.com/used>)*  ?z"
				+"}";*/
		
		/*String queryString = "SELECT *"
		+"{"
		+  "?x  (<http://relationship.com/wasGeneratedBy>)*  ?z"
		+"}";*/
		
		Query query = QueryFactory.create(queryString);
		Dataset ds = DatasetFactory.create(dsg);
		long start = System.currentTimeMillis();
		QueryExecution qexec = QueryExecutionFactory.create(query, ds);
		
		
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

                
                //System.out.println(x+"|"+y+"|"+z) ;
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
        
        
        
        
        
        
	
		
	}

}
