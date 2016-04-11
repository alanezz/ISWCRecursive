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

public class ProvDiscExperiments {

	/**
	 * This is for tdb recursive queries.
	 * You have to use MAT option in QueryExecutionFactory.java (jena.query)
	 * 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String directory = "/Users/adriansotosuarez/Desktop/TDB";
		Dataset dataset = TDBFactory.createDataset(directory);



		/*String queryString = "WITH RECURSIVE http://db.ing.puc.cl/temp AS"
				+ "{"
				+ "CONSTRUCT {<http://ente.com/Kevin_Bacon> <http://relationship.com/collab> ?act} "
				+ "FROM NAMED <http://db.ing.puc.cl/temp> "
				+ "FROM <"+Quad.defaultGraphIRI+"> "
				+ "WHERE "
				+ "{"
				+ "{<http://ente.com/Kevin_Bacon> <http://relationship.com/actedIn> ?mov . ?act <http://relationship.com/actedIn> ?mov} "
				+ "UNION "
				+ "{{?act1 <http://relationship.com/actedIn> ?mov} . {?act <http://relationship.com/actedIn> ?mov} . GRAPH <http://db.ing.puc.cl/temp> {<http://ente.com/Kevin_Bacon> <http://relationship.com/collab> ?act1}} "
				+ "}"
				+ "}"
				+ "SELECT ?z FROM NAMED <http://db.ing.puc.cl/temp> "
				+ "WHERE {GRAPH <http://db.ing.puc.cl/temp> {<http://ente.com/Kevin_Bacon> <http://relationship.com/collab> ?z}}";
*/

/*		String queryString = "WITH RECURSIVE http://db.ing.puc.cl/temp AS"
		+ "{"
		+ "CONSTRUCT {<http://fact.com/Kevin_Bacon> <http://relationship.com/collab> ?act} "
		+ "FROM NAMED <http://db.ing.puc.cl/temp> "
		+ "FROM <"+Quad.defaultGraphIRI+"> "
		+ "WHERE "
		+ "{"
		+ "{<http://fact.com/Kevin_Bacon> <http://relationship.com/actedIn> ?mov . ?act <http://relationship.com/actedIn> ?mov} "
		+ "UNION "
		+ "{{?act1 <http://relationship.com/actedIn> ?mov} . {?act <http://relationship.com/actedIn> ?mov} . GRAPH <http://db.ing.puc.cl/temp> {<http://fact.com/Kevin_Bacon> <http://relationship.com/collab> ?act1}} "
		+ "}"
		+ "}"
		+ "SELECT ?z FROM NAMED <http://db.ing.puc.cl/temp> "
		+ "WHERE {GRAPH <http://db.ing.puc.cl/temp> {<http://fact.com/Kevin_Bacon> <http://relationship.com/collab> ?z}}";
*/
		/*String queryString = "WITH RECURSIVE http://db.ing.puc.cl/temp AS"
		+ "{"
		+ "CONSTRUCT {?x <http://relationship.com/colleague> ?y} "
		+ "FROM <"+Quad.defaultGraphIRI+"> "
		+ "WHERE "
		+ "{"
		+ "{?x <http://relationship.com/actedIn> ?mov . ?y <http://relationship.com/actedIn> ?mov} "
		+ "}"
		+ "}"
		+ "WITH RECURSIVE http://db.ing.puc.cl/temp2 AS"
		+ "{"
		+ "CONSTRUCT {?x <http://relationship.com/lahice> <http://relationship.com/wn>} "
		+ "FROM NAMED <http://db.ing.puc.cl/temp2> "
		+ "FROM NAMED <http://db.ing.puc.cl/temp> "
		+ "FROM <"+Quad.defaultGraphIRI+"> "
		+ "WHERE "
		+ "{"
		+ "{<http://fact.com/Kevin_Bacon> <http://relationship.com/actedIn> ?mov . ?x <http://relationship.com/actedIn> ?mov} "
		+ "UNION "
		+ "{GRAPH <http://db.ing.puc.cl/temp> {?y <http://relationship.com/colleague> ?x}.GRAPH <http://db.ing.puc.cl/temp2> {?y <http://relationship.com/lahice> <http://relationship.com/wn>}} "
		+ "}"
		+ "}"
		+ "SELECT ?z FROM NAMED <http://db.ing.puc.cl/temp2> "
		+ "WHERE {GRAPH <http://db.ing.puc.cl/temp2> {?z <http://relationship.com/lahice> <http://relationship.com/wn>}}";		
		*/
		/*String queryString = "SELECT *"
				+"{"
				+  "<http://ente.com/Kevin_Bacon>  (<http://relationship.com/actedIn> / ^<http://relationship.com/actedIn>)*  ?z"
				+"}";*/

		/*String queryString = "WITH RECURSIVE http://db.ing.puc.cl/temp AS"
		+ "{"
		+ "CONSTRUCT {<http://ente.com/Kevin_Bacon> ?dir ?act} "
		+ "FROM NAMED <http://db.ing.puc.cl/temp> "
		+ "FROM <"+Quad.defaultGraphIRI+"> "
		+ "WHERE "
		+ "{"
		+ "{<http://ente.com/Kevin_Bacon> <http://relationship.com/actedIn> ?mov . ?act <http://relationship.com/actedIn> ?mov . ?dir <http://relationship.com/directed> ?mov} "
		+ "UNION "
		+ "{{?act1 <http://relationship.com/actedIn> ?mov} . {?dir <http://relationship.com/directed> ?mov} . {?act <http://relationship.com/actedIn> ?mov} . GRAPH <http://db.ing.puc.cl/temp> {<http://ente.com/Kevin_Bacon> ?dir ?act1}} "
		+ "}"
		+ "}"
		+ "SELECT ?y ?z FROM NAMED <http://db.ing.puc.cl/temp> "
		+ "WHERE {GRAPH <http://db.ing.puc.cl/temp> {<http://ente.com/Kevin_Bacon> ?y ?z}}";
*/


		/*String queryString = "WITH RECURSIVE http://db.ing.puc.cl/temp AS"
				+ "{"
				+ "CONSTRUCT {<http://ente.com/Kevin_Bacon> <http://relationship.com/collab> ?act} "
				+ "FROM NAMED <http://db.ing.puc.cl/temp> "
				+ "FROM <"+Quad.defaultGraphIRI+"> "
				+ "WHERE "
				+ "{"
				+ "{<http://ente.com/Kevin_Bacon> <http://relationship.com/actedIn> ?mov . ?act <http://relationship.com/actedIn> ?mov . ?dir <http://relationship.com/directed> ?mov . ?dir <http://relationship.com/actedIn> ?mov1} "
				+ "UNION "
				+ "{{?act1 <http://relationship.com/actedIn> ?mov} . {?dir <http://relationship.com/directed> ?mov} . {?dir <http://relationship.com/actedIn> ?mov1} . {?act <http://relationship.com/actedIn> ?mov} . GRAPH <http://db.ing.puc.cl/temp> {<http://ente.com/Kevin_Bacon> <http://relationship.com/collab> ?act1}} "
				+ "}"
				+ "}"
				+ "SELECT ?y ?z FROM NAMED <http://db.ing.puc.cl/temp> "
				+ "WHERE {GRAPH <http://db.ing.puc.cl/temp> {<http://ente.com/Kevin_Bacon> ?y ?z}}";*/

				
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
		
		
		//String queryString = "SELECT ?x ?y ?z WHERE {?x <http://relationship.com/RealizaCurso> ?y . ?y <http://relationship.com/PerteneceA> <http://entity.com/DCC>}";
		/*String queryString = "SELECT ?x ?y ?z WHERE {?x <http://relationship.com/RealizaCurso> ?y " + 
							". OPTIONAL {?x <http://relationship.com/Mail> ?z}}";
		
		*/
		String queryString = "SELECT ?x ?y ?z WHERE {{?x <http://relationship.com/RealizaCurso> <http://entity.com/Discretas>} UNION {?x <http://relationship.com/EsAyudanteDe> <http://entity.com/Discretas>}}";
		
		
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

                //Uncomment to watch results
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
