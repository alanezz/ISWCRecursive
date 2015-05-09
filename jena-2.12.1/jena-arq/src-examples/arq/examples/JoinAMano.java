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
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.core.Quad;
import com.hp.hpl.jena.tdb.TDBFactory;

public class JoinAMano {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String directory = "/Users/adriansotosuarez/Desktop/TDB";
		Dataset dataset = TDBFactory.createDataset(directory);


		/*String queryString = "SELECT *"
				+"{"
				+  "<http://fact.com/Kevin_Bacon>  (<http://relationship.com/actedIn> / ^<http://relationship.com/actedIn>)*  ?z"
				+"}";*/
		
		/*String queryString = "SELECT DISTINCT ?act WHERE{{?mov <http://data.linkedmdb.org/resource/movie/actor> <http://data.linkedmdb.org/resource/actor/29539> . ?mov <http://data.linkedmdb.org/resource/movie/actor> ?act}"

							+ "UNION { ?mov <http://data.linkedmdb.org/resource/movie/actor> <http://data.linkedmdb.org/resource/actor/29539> . ?mov <http://data.linkedmdb.org/resource/movie/actor> ?act2  . ?mov2 <http://data.linkedmdb.org/resource/movie/actor> ?act2 . ?mov2 <http://data.linkedmdb.org/resource/movie/actor> ?act}"

							+ "UNION {?mov <http://data.linkedmdb.org/resource/movie/actor> <http://data.linkedmdb.org/resource/actor/29539> . ?mov <http://data.linkedmdb.org/resource/movie/actor> ?act3  . ?mov2 <http://data.linkedmdb.org/resource/movie/actor> ?act3 . ?mov2 <http://data.linkedmdb.org/resource/movie/actor> ?act2 . ?mov3 <http://data.linkedmdb.org/resource/movie/actor> ?act2 . ?mov3 <http://data.linkedmdb.org/resource/movie/actor> ?act  }"

							+ "UNION {?mov <http://data.linkedmdb.org/resource/movie/actor> <http://data.linkedmdb.org/resource/actor/29539> . ?mov <http://data.linkedmdb.org/resource/movie/actor> ?act4  . ?mov2 <http://data.linkedmdb.org/resource/movie/actor> ?act4 . ?mov2 <http://data.linkedmdb.org/resource/movie/actor> ?act3 . ?mov3 <http://data.linkedmdb.org/resource/movie/actor> ?act3 . ?mov3 <http://data.linkedmdb.org/resource/movie/actor> ?act2 . ?mov4 <http://data.linkedmdb.org/resource/movie/actor> ?act2  . ?mov4 <http://data.linkedmdb.org/resource/movie/actor> ?act  }}";
		*/
		
		/*String queryString = "SELECT DISTINCT ?act ?dir WHERE {"
	+"{?mov <http://data.linkedmdb.org/resource/movie/actor> <http://data.linkedmdb.org/resource/actor/29539> . ?mov <http://data.linkedmdb.org/resource/movie/actor> ?act . ?mov <http://data.linkedmdb.org/resource/movie/director> ?dir }"
	+"UNION {"
	+"?mov <http://data.linkedmdb.org/resource/movie/actor> <http://data.linkedmdb.org/resource/actor/29539> . ?mov <http://data.linkedmdb.org/resource/movie/actor> ?act2 . ?mov2 <http://data.linkedmdb.org/resource/movie/actor> ?act2 . ?mov2 <http://data.linkedmdb.org/resource/movie/actor> ?act . ?mov <http://data.linkedmdb.org/resource/movie/director> ?dir . ?mov2 <http://data.linkedmdb.org/resource/movie/director> ?dir"
	+"}"
+"}";*/
		String queryString = "SELECT DISTINCT ?act WHERE{"
				+ "{?mov <http://data.linkedmdb.org/resource/movie/actor> <http://data.linkedmdb.org/resource/actor/29539> . ?mov <http://data.linkedmdb.org/resource/movie/actor> ?act . ?mov <http://data.linkedmdb.org/resource/movie/director> ?dir . ?dir <http://data.linkedmdb.org/resource/movie/director_name> ?x . ?y <http://data.linkedmdb.org/resource/movie/actor_name> ?x}"
				+ "UNION{"
				+	"{?mov <http://data.linkedmdb.org/resource/movie/actor> <http://data.linkedmdb.org/resource/actor/29539> . ?mov <http://data.linkedmdb.org/resource/movie/actor> ?act2 . ?mov2 <http://data.linkedmdb.org/resource/movie/actor> ?act2 . ?mov2 <http://data.linkedmdb.org/resource/movie/actor> ?act . ?mov <http://data.linkedmdb.org/resource/movie/director> ?dir . ?dir <http://data.linkedmdb.org/resource/movie/director_name> ?x . ?y <http://data.linkedmdb.org/resource/movie/actor_name> ?x . ?mov2 <http://data.linkedmdb.org/resource/movie/director> ?dir2 . ?dir2 <http://data.linkedmdb.org/resource/movie/director_name> ?x2 . ?y2 <http://data.linkedmdb.org/resource/movie/actor_name> ?x2}"
				+"}"
				+ "UNION{"
				+	"{?mov <http://data.linkedmdb.org/resource/movie/actor> <http://data.linkedmdb.org/resource/actor/29539> . ?mov <http://data.linkedmdb.org/resource/movie/actor> ?act3 . ?mov2 <http://data.linkedmdb.org/resource/movie/actor> ?act3 . ?mov2 <http://data.linkedmdb.org/resource/movie/actor> ?act2 . ?mov3 <http://data.linkedmdb.org/resource/movie/actor> ?act2 . ?mov3 <http://data.linkedmdb.org/resource/movie/actor> ?act . ?mov <http://data.linkedmdb.org/resource/movie/director> ?dir . ?dir <http://data.linkedmdb.org/resource/movie/director_name> ?x . ?y <http://data.linkedmdb.org/resource/movie/actor_name> ?x . ?mov2 <http://data.linkedmdb.org/resource/movie/director> ?dir2 . ?dir2 <http://data.linkedmdb.org/resource/movie/director_name> ?x2 . ?y2 <http://data.linkedmdb.org/resource/movie/actor_name> ?x2 . ?mov3 <http://data.linkedmdb.org/resource/movie/director> ?dir3 . ?dir3 <http://data.linkedmdb.org/resource/movie/director_name> ?x3 . ?y3 <http://data.linkedmdb.org/resource/movie/actor_name> ?x3}"
				+"}"
				+ "UNION{"
				+	"{?mov <http://data.linkedmdb.org/resource/movie/actor> <http://data.linkedmdb.org/resource/actor/29539> . ?mov <http://data.linkedmdb.org/resource/movie/actor> ?act4 . ?mov2 <http://data.linkedmdb.org/resource/movie/actor> ?act4 . ?mov2 <http://data.linkedmdb.org/resource/movie/actor> ?act3 . ?mov3 <http://data.linkedmdb.org/resource/movie/actor> ?act3 . ?mov3 <http://data.linkedmdb.org/resource/movie/actor> ?act2 . ?mov4 <http://data.linkedmdb.org/resource/movie/actor> ?act2 . ?mov4 <http://data.linkedmdb.org/resource/movie/actor> ?act . ?mov <http://data.linkedmdb.org/resource/movie/director> ?dir . ?dir <http://data.linkedmdb.org/resource/movie/director_name> ?x . ?y <http://data.linkedmdb.org/resource/movie/actor_name> ?x . ?mov2 <http://data.linkedmdb.org/resource/movie/director> ?dir2 . ?dir2 <http://data.linkedmdb.org/resource/movie/director_name> ?x2 . ?y2 <http://data.linkedmdb.org/resource/movie/actor_name> ?x2 . ?mov3 <http://data.linkedmdb.org/resource/movie/director> ?dir3 . ?dir3 <http://data.linkedmdb.org/resource/movie/director_name> ?x3 . ?y3 <http://data.linkedmdb.org/resource/movie/actor_name> ?x3 . ?mov4 <http://data.linkedmdb.org/resource/movie/director> ?dir4 . ?dir4 <http://data.linkedmdb.org/resource/movie/director_name> ?x4 . ?y4 <http://data.linkedmdb.org/resource/movie/actor_name> ?x4}"
				+"}"
				+"}";
		
		Query query = QueryFactory.create(queryString);

		long start = System.currentTimeMillis();
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);

		System.out.println("BEGIN");

		int triples = 0;
		try {
			// Assumption: it's a SELECT query.
			ResultSet rs = qexec.execSelect() ;


			// The order of results is undefined. 
			for ( ; rs.hasNext() ; )
			{
				QuerySolution rb = rs.nextSolution() ;

				// Get title - variable names do not include the '?' (or '$')
				RDFNode x = rb.get("act") ;
				RDFNode y = rb.get("dir") ;

				// SI QUIERES MOSTRAR EL OUTPUT DESCOMENTA ESTA LÍNEA

				//System.out.println(x+" "+y) ;
				triples++;

			}
			System.out.println(triples);
		}
		finally
		{
			qexec.close() ;
		}
		long time = System.currentTimeMillis() - start;
		System.out.printf("Took: "+time);

	}

}
