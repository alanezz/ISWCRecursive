/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package arq.examples;


// The ARQ application API.
import org.apache.jena.atlas.io.IndentedWriter ;
import org.apache.jena.riot.RDFDataMgr;

import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.Query ;
import com.hp.hpl.jena.query.QueryExecution ;
import com.hp.hpl.jena.query.QueryExecutionFactory ;
import com.hp.hpl.jena.query.QueryFactory ;
import com.hp.hpl.jena.query.ResultSetFactory ;
import com.hp.hpl.jena.query.ResultSetFormatter ;
import com.hp.hpl.jena.query.ResultSetRewindable ;
import com.hp.hpl.jena.rdf.model.Model ;
import com.hp.hpl.jena.rdf.model.ModelFactory ;
import com.hp.hpl.jena.rdf.model.Resource ;
import com.hp.hpl.jena.sparql.core.DatasetGraph;
import com.hp.hpl.jena.sparql.core.DatasetGraphFactory;
import com.hp.hpl.jena.sparql.core.Quad;
import com.hp.hpl.jena.vocabulary.DC ;

/** Example 2 : Execute a simple SELECT query on a model
 *  to find the DC titles contained in a model. 
 *  Show how to print results twice. */

public class ExRecursive1
{
    static public final String NL = System.getProperty("line.separator") ; 
    
    public static void main(String[] args)
    {
        
    	Model model = RDFDataMgr.loadModel("/Users/adriansotosuarez/RDFfiles/prueba.ttl") ;
    	
        String queryString = "PREFIX dbpuc: <http://db.ing.puc.cl/>   WITH RECURSIVE dbpuc:temp1 AS "+
        	   "{"+
        			"CONSTRUCT {?x ?y ?z} "+
        			"FROM NAMED dbpuc:temp1 "+
        			"WHERE "+
        			"{"+
        				"{?x ?y ?z} "+
        				"UNION "+
        				"{?w ?u ?y . GRAPH dbpuc:temp1 {?x ?w ?z}} "+
        			"}"+
        		"} "+
        		"WITH RECURSIVE dbpuc:temp2 AS"+
        		"{"+
        			"CONSTRUCT { ?x ?y ?z} "+
        			"FROM NAMED dbpuc:temp1 "+
        			"FROM NAMED dbpuc:temp2 "+
        			"WHERE "+
        			"{"+
        				"{GRAPH dbpuc:temp1 {?x ?y ?z}} "+
        					"UNION "+
        				"{GRAPH dbpuc:temp2 {?x ?y ?w} . GRAPH dbpuc:temp1 {?w ?y ?z}} "+
        			"}"+
        		"}"+
        		"SELECT ?x ?y ?z FROM NAMED dbpuc:temp2 "+
                "WHERE {GRAPH dbpuc:temp2 { ?x ?y ?z}}";

        Query query = QueryFactory.create(queryString) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, DatasetFactory.create(model)) ;
        ResultSetRewindable rs = ResultSetFactory.makeRewindable(qexec.execSelect());
        ResultSetFormatter.out(rs) ;
        System.out.println();
        qexec.close();
    }
    
}
