/**
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

package org.apache.jena.query.text;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.text.assembler.TextAssembler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hp.hpl.jena.assembler.Assembler;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * This class defines a setup configuration for a dataset that uses a keyword analyzer with a Lucene index.
 */
public class TestDatasetWithKeywordAnalyzer extends AbstractTestDatasetWithTextIndexBase {
	private static final String INDEX_PATH = "target/test/TestDatasetWithLuceneIndex";
	private static final File indexDir = new File(INDEX_PATH);
	
	private static final String SPEC_BASE = "http://example.org/spec#";
	private static final String SPEC_ROOT_LOCAL = "lucene_text_dataset";
	private static final String SPEC_ROOT_URI = SPEC_BASE + SPEC_ROOT_LOCAL;

	private static String makeSpec(String analyzer) {
	    return StrUtils.strjoinNL(
					"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> ",
					"prefix ja:   <http://jena.hpl.hp.com/2005/11/Assembler#> ",
					"prefix tdb:  <http://jena.hpl.hp.com/2008/tdb#>",
					"prefix text: <http://jena.apache.org/text#>",
					"prefix :     <" + SPEC_BASE + ">",
					"",
					"[] ja:loadClass    \"org.apache.jena.query.text.TextQuery\" .",
				    "text:TextDataset      rdfs:subClassOf   ja:RDFDataset .",
				    "text:TextIndexLucene  rdfs:subClassOf   text:TextIndex .",
				    
				    ":" + SPEC_ROOT_LOCAL,
				    "    a              text:TextDataset ;",
				    "    text:dataset   :dataset ;",
				    "    text:index     :indexLucene ;",
				    "    .",
				    "",
                    ":dataset",
                    "    a               ja:RDFDataset ;",
                    "    ja:defaultGraph :graph ;",
                    ".",
                    ":graph",
                    "    a               ja:MemoryModel ;",
                    ".",
                    "",
				    ":indexLucene",
                    "    a text:TextIndexLucene ;",
				    "    text:directory <file:" + INDEX_PATH + "> ;",
				    "    text:entityMap :entMap ;",
				    "    .",
                    "",
				    ":entMap",
                    "    a text:EntityMap ;",
				    "    text:entityField      \"uri\" ;",
				    "    text:defaultField     \"label\" ;",
				    "    text:map (",
				    "         [ text:field \"label\" ; ",
				    "           text:predicate rdfs:label ;",
				    "           text:analyzer [ a " + analyzer + " ]",
				    "         ]",
				    "         [ text:field \"comment\" ; text:predicate rdfs:comment ]",
				    "         ) ."
				    );
	}      
	
	public static void init(String analyzer) {
		Reader reader = new StringReader(makeSpec(analyzer));
		Model specModel = ModelFactory.createDefaultModel();
		specModel.read(reader, "", "TURTLE");
		TextAssembler.init();			
		deleteOldFiles();
		indexDir.mkdirs();
		Resource root = specModel.getResource(SPEC_ROOT_URI);
		dataset = (Dataset) Assembler.general.open(root);
	}
	
	
	public static void deleteOldFiles() {
		if (indexDir.exists()) TextSearchUtil.emptyAndDeleteDirectory(indexDir);
	}	

	@BeforeClass public static void beforeClass() {
		init("text:KeywordAnalyzer");
	}	
	
	@AfterClass public static void afterClass() {
		deleteOldFiles();
	}
	
	@Test
	public void testKeywordAnalyzerDoesNotSplitTokensAtSpace() {
		final String testName = "testKeywordAnalyzerDoesNotSplitTokensAtSpace";
		final String turtle = StrUtils.strjoinNL(
				TURTLE_PROLOG,
				"<" + RESOURCE_BASE + testName + ">",
				"  rdfs:label 'EC1V 9BE'",
				"."
				);
		String queryString = StrUtils.strjoinNL(
				QUERY_PROLOG,
				"SELECT ?s",
				"WHERE {",
				"    ?s text:query ( rdfs:label 'EC1V' 10 ) .",
				"}"
				);
		Set<String> expectedURIs = new HashSet<>() ;
		doTestSearch(turtle, queryString, expectedURIs);
	}
	
	@Test
	public void testKeywordAnalyzerMatchesWholeField() {
		final String testName = "testKeywordAnalyzerMatchesWholeField";
		final String turtle = StrUtils.strjoinNL(
				TURTLE_PROLOG,
				"<" + RESOURCE_BASE + testName + ">",
				"  rdfs:label 'EC2V 9BE'",
				"."
				);
		String queryString = StrUtils.strjoinNL(
				QUERY_PROLOG,
				"SELECT ?s",
				"WHERE {",
				"    ?s text:query ( rdfs:label '\"EC2V 9BE\"' 10 ) .",
				"}"
				);
		Set<String> expectedURIs = new HashSet<>() ;
		expectedURIs.addAll( Arrays.asList(RESOURCE_BASE + testName)) ;
		doTestSearch(turtle, queryString, expectedURIs);
	}
}
