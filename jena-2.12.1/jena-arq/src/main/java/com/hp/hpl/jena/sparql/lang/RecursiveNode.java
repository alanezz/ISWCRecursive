package com.hp.hpl.jena.sparql.lang;

import java.util.*;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.compose.Union;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sparql.core.DatasetGraph;
import com.hp.hpl.jena.sparql.graph.GraphFactory;
import com.hp.hpl.jena.tdb.TDBFactory;

public class RecursiveNode {
	
	static public final String NL = System.getProperty("line.separator") ; 
	public ArrayList<String> InnerQueries;
	public ArrayList<String[]> QueriesConstruct;
	public String Prefix;
	public String OuterQuery;
	public RecursiveNode()
	{
		InnerQueries = new ArrayList<String>();
		QueriesConstruct = new ArrayList<String[]>();
		Prefix = "";
		OuterQuery = "";
	}
	
	public void init(String queryString)
	{
		//Obtenemos el prefijo
		String aux = queryString;
		while(aux.contains("PREFIX"))
		{
			int index = aux.indexOf('>');
			Prefix = Prefix + aux.substring(0,index+1) + NL;
			aux = aux.substring(index+1);
			aux = aux.trim();
		}
		aux = aux.trim();
		while(aux.contains("WITH RECURSIVE"))
		{
			Stack<String> brackets = new Stack<String>();
			for (int i = 0; i<aux.length();i++)
			{
				if(aux.charAt(i)=='{')
				{
					brackets.push("{");
				}
				else if(aux.charAt(i)=='}')
				{
					brackets.pop();
					if(brackets.size()==0)
					{
						String s = aux.substring(0,i+1);
						aux = aux.substring(i+1);
						aux = aux.trim();
						InnerQueries.add(s);
						break;
					}
				}
			}
		}
		OuterQuery = Prefix + NL + aux;
		
		String PrefixCopy = new String(Prefix);
		HashMap<String, String> prefixes = new HashMap<String, String>();
		PrefixCopy = PrefixCopy.trim();
		while(PrefixCopy.length()!=0)
		{
			int a = PrefixCopy.indexOf(" ");
			PrefixCopy = PrefixCopy.substring(a).trim();
			int b = PrefixCopy.indexOf(":");
			String daKey = PrefixCopy.substring(0,b).trim();
			PrefixCopy = PrefixCopy.substring(b+1).trim();
			int c = PrefixCopy.indexOf(">");
			String daValue = PrefixCopy.substring(1,c);
			PrefixCopy = PrefixCopy.substring(c+1).trim();
			prefixes.put(daKey, daValue);
			
		}
		
		
		for(String query : InnerQueries)
		{
			String[] auxArray = new String[2];
			String auxQuery = query;
			auxQuery = auxQuery.replace("WITH RECURSIVE", "");
			auxQuery = auxQuery.trim();
			int auxIndex = auxQuery.indexOf(" ");
			auxArray[0] = auxQuery.substring(0,auxIndex);
			
			//Para permitir prefix en la declaración del with recursive...
			if(auxArray.length>0)
			{
				if(!auxArray[0].contains("http://"))
				{
					String[] nextURI = auxArray[0].split(":");
					auxArray[0] = prefixes.get(nextURI[0])+nextURI[1];

				}
			}
			auxQuery = auxQuery.substring(auxIndex);
			auxQuery = auxQuery.replace("AS", "");
			auxQuery = auxQuery.trim();
			auxArray[1] = Prefix + NL + auxQuery.substring(1,auxQuery.length()-1);
			QueriesConstruct.add(auxArray);
		}
		
	}
	
	public static void execQueryWithoutUnion(Dataset ds, Query recursiveQuery, String recursiveURI)
	{
		System.out.println(recursiveQuery);
		int countPrev = 0;
		int countNow = 0;
		Model workingGraph = ModelFactory.createDefaultModel();
		//dsg.addGraph(NodeFactory.createURI(recursiveURI), workingGraph);
		//Query queryRecursive = QueryFactory.create(recursiveQuery);
		
		
		Model baseGraph;
		if(ds.containsNamedModel(recursiveURI))
		{
			baseGraph = ds.getNamedModel(recursiveURI);	
		}
		else
		{
			baseGraph = ModelFactory.createDefaultModel();
		}
		
		QueryExecution qexec;
		
		while(true)
		{
			
			qexec = QueryExecutionFactory.create(recursiveQuery, ds);
			Model aux = qexec.execConstruct();
			countNow = aux.getGraph().size();
			if(countNow == countPrev)
			{
				break;
			}
			else
			{
				workingGraph = aux;
				Model union = workingGraph.union(baseGraph);
				ds.addNamedModel(recursiveURI, union);
				countPrev = countNow;
				
			}
			

		}
		
		//Model returnGraph;
		
		
		

		
	}

	public static void execQueryWithoutUnionOpt(Dataset ds, Query recursiveQuery, String recursiveURI)
	{
		System.out.println(recursiveQuery);
		Query[] laQuery = parseOpt(recursiveQuery);
		
		//DEBUG
		//System.out.println("METODO");
		/*for(Query q : laQuery)
		{
			System.out.println(q);
		}*/

		if(laQuery.length==1)
		{
			int countPrev = 0;
			int countNow = 0;
			Model workingGraph = ModelFactory.createDefaultModel();

			//dsg.addGraph(NodeFactory.createURI(recursiveURI), workingGraph);
			//Query queryRecursive = QueryFactory.create(recursiveQuery);




			Model baseGraph;

			/*if(ds.containsNamedModel(recursiveURI))
			{
				baseGraph = ds.getNamedModel(recursiveURI);	
			}
			else
			{*/
				baseGraph = ModelFactory.createDefaultModel();
				System.out.println("Cree Default");
			//}

			QueryExecution qexec;
			
			while(true)
			{
				System.out.println("ASDASD");
				qexec = QueryExecutionFactory.create(laQuery[0], ds);
				Model aux = qexec.execConstruct();
				countNow = aux.getGraph().size();
				System.out.println(countNow);
				if(countNow == countPrev)
				{
					break;
				}
				else
				{
					workingGraph = aux;
					Model union = workingGraph.union(baseGraph);
					ds.addNamedModel(recursiveURI, union);
					countPrev = countNow;

				}


			}
			qexec.close();
		}
		else
		{
			Model returnGraph = ModelFactory.createDefaultModel();
			QueryExecution qexecBase = QueryExecutionFactory.create(laQuery[0], ds);
			QueryExecution qexecRec;
			
			Model baseModel = qexecBase.execConstruct();
			ds.addNamedModel(recursiveURI, baseModel);
			
			returnGraph = returnGraph.union(baseModel);
			
			int countPrev = 0;
			int countNow = 0;
			//int count = 0;
			while(true)
			{
				//count++;
				//System.out.println(count);
				qexecRec = QueryExecutionFactory.create(laQuery[1], ds);
				long startTime1 = System.currentTimeMillis();
				Model recModel = qexecRec.execConstruct();
				long endTime = System.currentTimeMillis();
				System.out.println("LAAKSKLDJALKSDJI");
				if(endTime - startTime1 >1000)
					System.out.println("EXEC REC: "+(endTime - startTime1));
	            
				
				
				//long startTime2 = System.currentTimeMillis();
				returnGraph.add(recModel);
				

				countNow = (int) returnGraph.size();
						if(countNow == countPrev)
						{
							break;
						}

				countPrev = countNow;
				//returnGraph = returnGraph.union(recModel);
				//long endTime2 = System.currentTimeMillis();
	            //System.out.println("UNION: "+(endTime2 - startTime2));
				
				//long startTime3 = System.currentTimeMillis();
				ds.removeNamedModel(recursiveURI);
				ds.addNamedModel(recursiveURI, recModel);
				//long endTime3 = System.currentTimeMillis();
	            //System.out.println("REMOVE ADD: "+(endTime3 - startTime3));

			}
			ds.removeNamedModel(recursiveURI);
			ds.addNamedModel(recursiveURI, returnGraph);
			qexecBase.close();
		}
		//Model returnGraph;




		
	}
	
	public static void execQueryWithoutUnionOptMat(Dataset ds, Query recursiveQuery, String recursiveURI)
	{
		Query[] laQuery = parseOpt(recursiveQuery);
		DatasetGraph dsg = ds.asDatasetGraph();
		dsg.removeGraph(NodeFactory.createURI(recursiveURI));

		if(laQuery.length==1)
		{
			
			DatasetGraph dsgRecursive =TDBFactory.createDatasetGraph("/Users/adriansotosuarez/Desktop/Rec");
			if(dsgRecursive.containsGraph(NodeFactory.createURI("http://modelo.com/recursivo")))
			{
				dsgRecursive.removeGraph(NodeFactory.createURI("http://modelo.com/recursivo"));
			}
			if(dsgRecursive.containsGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp")))
			{
				dsgRecursive.removeGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp"));
			}
			int countPrev = 0;
			int countNow = 0;

			QueryExecution qexec;
			
			while(true)
			{
				
				qexec = QueryExecutionFactory.create(laQuery[0], ds);
				Iterator<Triple> aux = qexec.execConstructTriples();
				while(aux.hasNext())
				{
					Triple tr = aux.next();
					dsgRecursive.add(NodeFactory.createURI("http://modelo.com/recursiveTemp"), tr.getSubject(), tr.getPredicate(), tr.getObject());
				}
				dsg.addGraph(NodeFactory.createURI(recursiveURI),dsgRecursive.getGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp")));
				dsgRecursive.removeGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp"));
				countNow = dsg.getGraph(NodeFactory.createURI(recursiveURI)).size();

				if(countNow == countPrev)
				{
					break;
				}
				countPrev = countNow;


			}
			dsgRecursive.close();
			qexec.close();
		}
		else
		{
			DatasetGraph dsgRecursive =TDBFactory.createDatasetGraph("/Users/adriansotosuarez/Desktop/Rec");
			if(dsgRecursive.containsGraph(NodeFactory.createURI("http://modelo.com/recursivo")))
			{
				dsgRecursive.removeGraph(NodeFactory.createURI("http://modelo.com/recursivo"));
			}
			if(dsgRecursive.containsGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp")))
			{
				dsgRecursive.removeGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp"));
			}
			QueryExecution qexecBase = QueryExecutionFactory.create(laQuery[0], ds);
			QueryExecution qexecRec;
			
			Iterator<Triple> baseModel = qexecBase.execConstructTriples();
			while(baseModel.hasNext())
			{
				Triple tr = baseModel.next();
				dsgRecursive.add(NodeFactory.createURI("http://modelo.com/recursiveTemp"), tr.getSubject(), tr.getPredicate(), tr.getObject());
				dsgRecursive.add(NodeFactory.createURI("http://modelo.com/recursivo"), tr.getSubject(), tr.getPredicate(), tr.getObject());
			}
			dsg.addGraph(NodeFactory.createURI(recursiveURI),dsgRecursive.getGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp")));
			dsgRecursive.removeGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp"));
			
			System.out.println(dsgRecursive.getGraph(NodeFactory.createURI("http://modelo.com/recursivo")).size());
			
			int countNow = 0;
			int countPrev = 0;
			
			boolean primera = false;
			while(true)
			{
				qexecRec = QueryExecutionFactory.create(laQuery[1], ds);
				Iterator<Triple> recModel = qexecRec.execConstructTriples();
	            
				//System.out.println("asdasdasdasd");
				
				while(recModel.hasNext())
				{
					
					Triple tr = recModel.next();
					/*if(primera)
					{
						System.out.println(dsgRecursive.contains(NodeFactory.createURI("http://modelo.com/recursivo"), tr.getSubject(), tr.getPredicate(), tr.getObject()));
					}*/
					
					dsgRecursive.add(NodeFactory.createURI("http://modelo.com/recursiveTemp"), tr.getSubject(), tr.getPredicate(), tr.getObject());
					dsgRecursive.add(NodeFactory.createURI("http://modelo.com/recursivo"), tr.getSubject(), tr.getPredicate(), tr.getObject());
				}
				/*if(primera)
					System.out.println("asd");
				else
				{
					primera = true;
					System.out.println("holooo");
				}*/
				
				countNow = dsgRecursive.getGraph(NodeFactory.createURI("http://modelo.com/recursivo")).size();
				System.out.println("COUNT REC: "+countNow);
				
				if(countNow == countPrev)
				{
					break;
				}
				countPrev = countNow;
				
				dsg.removeGraph(NodeFactory.createURI(recursiveURI));
				dsg.addGraph(NodeFactory.createURI(recursiveURI),dsgRecursive.getGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp")));
				dsgRecursive.removeGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp"));
				
				

			}
			dsg.removeGraph(NodeFactory.createURI(recursiveURI));
			dsg.addGraph(NodeFactory.createURI(recursiveURI),dsgRecursive.getGraph(NodeFactory.createURI("http://modelo.com/recursivo")));
			qexecBase.close();
			dsgRecursive.close();
		}




	}
	
	public static void execQueryWithoutUnionOptMatLim(Dataset ds, Query recursiveQuery, String recursiveURI, int domagoj)
	{
		Query[] laQuery = parseOpt(recursiveQuery);
		DatasetGraph dsg = ds.asDatasetGraph();
		dsg.removeGraph(NodeFactory.createURI(recursiveURI));

		if(laQuery.length==1)
		{
			
			DatasetGraph dsgRecursive =TDBFactory.createDatasetGraph("/path/to/recursive/folder");
			if(dsgRecursive.containsGraph(NodeFactory.createURI("http://modelo.com/recursivo")))
			{
				dsgRecursive.removeGraph(NodeFactory.createURI("http://modelo.com/recursivo"));
			}
			if(dsgRecursive.containsGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp")))
			{
				dsgRecursive.removeGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp"));
			}
			int countPrev = 0;
			int countNow = 0;
			int juan = 0;

			QueryExecution qexec;
			
			while(true)
			{
				
				qexec = QueryExecutionFactory.create(laQuery[0], ds);
				Iterator<Triple> aux = qexec.execConstructTriples();
				while(aux.hasNext())
				{
					Triple tr = aux.next();
					dsgRecursive.add(NodeFactory.createURI("http://modelo.com/recursiveTemp"), tr.getSubject(), tr.getPredicate(), tr.getObject());
				}
				dsg.addGraph(NodeFactory.createURI(recursiveURI),dsgRecursive.getGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp")));
				dsgRecursive.removeGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp"));
				countNow = dsg.getGraph(NodeFactory.createURI(recursiveURI)).size();

				if(countNow == countPrev)
				{
					break;
				}
				if(domagoj==juan)
				{
					break;
				}
				countPrev = countNow;
				juan++;

			}
			dsgRecursive.close();
			qexec.close();
		}
		else
		{
			DatasetGraph dsgRecursive =TDBFactory.createDatasetGraph("/path/to/recursive/folder");
			if(dsgRecursive.containsGraph(NodeFactory.createURI("http://modelo.com/recursivo")))
			{
				dsgRecursive.removeGraph(NodeFactory.createURI("http://modelo.com/recursivo"));
			}
			if(dsgRecursive.containsGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp")))
			{
				dsgRecursive.removeGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp"));
			}
			QueryExecution qexecBase = QueryExecutionFactory.create(laQuery[0], ds);
			QueryExecution qexecRec;
			
			Iterator<Triple> baseModel = qexecBase.execConstructTriples();
			while(baseModel.hasNext())
			{
				Triple tr = baseModel.next();
				dsgRecursive.add(NodeFactory.createURI("http://modelo.com/recursiveTemp"), tr.getSubject(), tr.getPredicate(), tr.getObject());
				dsgRecursive.add(NodeFactory.createURI("http://modelo.com/recursivo"), tr.getSubject(), tr.getPredicate(), tr.getObject());
			}
			dsg.addGraph(NodeFactory.createURI(recursiveURI),dsgRecursive.getGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp")));
			dsgRecursive.removeGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp"));
			
			System.out.println(dsgRecursive.getGraph(NodeFactory.createURI("http://modelo.com/recursivo")).size());
			
			int countNow = 0;
			int countPrev = 0;
			int juan = 0;
			
			boolean primera = false;
			while(true)
			{
				qexecRec = QueryExecutionFactory.create(laQuery[1], ds);
				Iterator<Triple> recModel = qexecRec.execConstructTriples();
	            
				//System.out.println("asdasdasdasd");
				
				while(recModel.hasNext())
				{
					
					Triple tr = recModel.next();
					/*if(primera)
					{
						System.out.println(dsgRecursive.contains(NodeFactory.createURI("http://modelo.com/recursivo"), tr.getSubject(), tr.getPredicate(), tr.getObject()));
					}*/
					
					dsgRecursive.add(NodeFactory.createURI("http://modelo.com/recursiveTemp"), tr.getSubject(), tr.getPredicate(), tr.getObject());
					dsgRecursive.add(NodeFactory.createURI("http://modelo.com/recursivo"), tr.getSubject(), tr.getPredicate(), tr.getObject());
				}
				/*if(primera)
					System.out.println("asd");
				else
				{
					primera = true;
					System.out.println("holooo");
				}*/
				
				countNow = dsgRecursive.getGraph(NodeFactory.createURI("http://modelo.com/recursivo")).size();
				System.out.println("COUNT REC: "+countNow);
				
				if(countNow == countPrev)
				{
					break;
				}
				if(domagoj==juan)
				{
					System.out.println("El KI de Juan alcanzó al de Domagoj");
					break;
				}
				juan++;
				countPrev = countNow;
				
				dsg.removeGraph(NodeFactory.createURI(recursiveURI));
				dsg.addGraph(NodeFactory.createURI(recursiveURI),dsgRecursive.getGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp")));
				dsgRecursive.removeGraph(NodeFactory.createURI("http://modelo.com/recursiveTemp"));
				
				

			}
			dsg.removeGraph(NodeFactory.createURI(recursiveURI));
			dsg.addGraph(NodeFactory.createURI(recursiveURI),dsgRecursive.getGraph(NodeFactory.createURI("http://modelo.com/recursivo")));
			qexecBase.close();
			dsgRecursive.close();
		}




	}
	
	public static void execQueryWithoutUnionOptStop(Dataset ds, Query recursiveQuery, String recursiveURI, int stop)
	{
		Query[] laQuery = parseOpt(recursiveQuery);
		
		//DEBUG
		//System.out.println("METODO");
		/*for(Query q : laQuery)
		{
			System.out.println(q);
		}*/

		if(laQuery.length==1)
		{
			int countPrev = 0;
			int countNow = 0;
			Model workingGraph = ModelFactory.createDefaultModel();

			//dsg.addGraph(NodeFactory.createURI(recursiveURI), workingGraph);
			//Query queryRecursive = QueryFactory.create(recursiveQuery);




			Model baseGraph;

			if(ds.containsNamedModel(recursiveURI))
			{
				baseGraph = ds.getNamedModel(recursiveURI);	
			}
			else
			{
				baseGraph = ModelFactory.createDefaultModel();
			}

			QueryExecution qexec;
			
			while(true)
			{
				
				qexec = QueryExecutionFactory.create(laQuery[0], ds);
				Model aux = qexec.execConstruct();
				countNow = aux.getGraph().size();
				if(countNow == countPrev)
				{
					break;
				}
				else
				{
					workingGraph = aux;
					Model union = workingGraph.union(baseGraph);
					ds.addNamedModel(recursiveURI, union);
					countPrev = countNow;

				}


			}
			qexec.close();
		}
		else
		{
			int countIt = 0;
			Model returnGraph = ModelFactory.createDefaultModel();
			QueryExecution qexecBase = QueryExecutionFactory.create(laQuery[0], ds);
			QueryExecution qexecRec;
			
			Model baseModel = qexecBase.execConstruct();
			ds.addNamedModel(recursiveURI, baseModel);
			
			returnGraph = returnGraph.union(baseModel);
			
			int countPrev = 0;
			int countNow = 0;
			//int count = 0;
			while(true)
			{
				//count++;
				//System.out.println(count);
				System.out.println("LULULU");
				qexecRec = QueryExecutionFactory.create(laQuery[1], ds);
				long startTime1 = System.currentTimeMillis();
				Model recModel = qexecRec.execConstruct();
				long endTime = System.currentTimeMillis();
				if(endTime - startTime1 >1000)
					System.out.println("EXEC REC: "+(endTime - startTime1));
	            


				//long startTime2 = System.currentTimeMillis();
				returnGraph.add(recModel);


				countNow = (int) returnGraph.size();
				if(countNow == countPrev)
				{
					break;
				}
				
				if(countIt == stop)
				{
					break;
				}

				countPrev = countNow;
				//returnGraph = returnGraph.union(recModel);
				//long endTime2 = System.currentTimeMillis();
	            //System.out.println("UNION: "+(endTime2 - startTime2));
				
				//long startTime3 = System.currentTimeMillis();
				ds.removeNamedModel(recursiveURI);
				ds.addNamedModel(recursiveURI, recModel);
				//long endTime3 = System.currentTimeMillis();
	            //System.out.println("REMOVE ADD: "+(endTime3 - startTime3));
				countIt++;
			}
			ds.removeNamedModel(recursiveURI);
			ds.addNamedModel(recursiveURI, returnGraph);
			qexecBase.close();
		}
		//Model returnGraph;




		
	}
	
	public static Query[] parseOpt(Query inQuery)
	{
		String s = inQuery.toString();
		s = s.replace("\n", "");
		String[] splitQuery = s.split("WHERE");
		String preQuery = splitQuery[0].trim();
		String whereQuery = splitQuery[1].trim();
		System.out.println(whereQuery);
		whereQuery = whereQuery.substring(1,whereQuery.length()-1);
		
		if(!(whereQuery.contains("UNION")))
		{
			Query[] retQuery = new Query[1];
			retQuery[0] = inQuery;

			return retQuery;
		}
		else
		{
			String[] recursiveConstruct = whereQuery.split("UNION");
			String baseQuery = recursiveConstruct[0].trim();
			String recursiveStepQuery = recursiveConstruct[1].trim();
			String qs1 = preQuery +" WHERE "+baseQuery;
			String qs2 = preQuery +" WHERE {"+recursiveStepQuery+"}";
			Query q1 = QueryFactory.create(qs1);
			Query q2 = QueryFactory.create(qs2);
			Query[] retQuery = new Query[2];
			retQuery[0] = q1;
			retQuery[1] = q2;

			return retQuery;
		}
		
	}
	
	
	
}
