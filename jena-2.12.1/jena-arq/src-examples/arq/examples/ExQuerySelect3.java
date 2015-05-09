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
import com.sun.xml.internal.bind.v2.runtime.property.PropertyFactory;

public class ExQuerySelect3 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Model model = ModelFactory.createDefaultModel();
		Graph g = GraphFactory.createDefaultGraph();
		String sCurrentLine;
		 
		BufferedReader br = new BufferedReader(new FileReader("/Users/adriansotosuarez/Desktop/prov.txt"));

		while ((sCurrentLine = br.readLine()) != null) {
			String[] aux = sCurrentLine.split("\\|");
			g.add(new Triple(NodeFactory.createURI(aux[0]),NodeFactory.createURI(aux[1]),NodeFactory.createURI(aux[2])));
			
			
		}
		DatasetGraph dsg = DatasetGraphFactory.create(g);
		Query query = QueryFactory.create("SELECT ?x ?v ?g WHERE {?x <http://relationship.com/wasRevisionOf> ?y . "+
											"?y <http://relationship.com/wasRevisionOf> ?z . "+
											"?x <http://relationship.com/wasGeneratedBy> ?q . "+
											"?q <http://relationship.com/wasAssociatedWith> ?g . "
											+ "?y <http://relationship.com/wasGeneratedBy> ?r . "
											+ "?r <http://relationship.com/wasAssociatedWith> ?g . "
											+ "?z <http://relationship.com/wasRevisionOf> ?w . "
											+ "?z <http://relationship.com/wasGeneratedBy> ?o . "
											+ "?o <http://relationship.com/wasAssociatedWith> ?g ."
											+ "?w <http://relationship.com/wasRevisionOf> ?v . "
											+ "?w <http://relationship.com/wasGeneratedBy> ?s . "
											+ "?s <http://relationship.com/wasAssociatedWith> ?g}");
		Dataset ds = DatasetFactory.create(dsg);
		QueryExecution qexec = QueryExecutionFactory.create(query, ds);

		try {
            // Assumption: it's a SELECT query.
            ResultSet rs = qexec.execSelect() ;
            
            // The order of results is undefined. 
            for ( ; rs.hasNext() ; )
            {
                QuerySolution rb = rs.nextSolution() ;
                
                // Get title - variable names do not include the '?' (or '$')
                RDFNode x = rb.get("x") ;
                //RDFNode y = rb.get("y") ;
                RDFNode v = rb.get("v") ;
                RDFNode ge = rb.get("g") ;
                
                
                // Check the type of the result value

                
                System.out.println(">"+x+" || "+v+"<") ;
                System.out.println("Oh my gosh: "+ge);


            }
        }
        finally
        {
            // QueryExecution objects should be closed to free any system resources 
            qexec.close() ;
        }
        
        
        
        
        
        
        
        br.close();
	}

}