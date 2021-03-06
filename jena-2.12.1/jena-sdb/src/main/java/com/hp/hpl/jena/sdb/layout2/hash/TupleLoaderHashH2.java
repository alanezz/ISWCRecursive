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

package com.hp.hpl.jena.sdb.layout2.hash;
/* H2 contribution from Martin HEIN (m#)/March 2008 */
import com.hp.hpl.jena.sdb.layout2.TableDescNodes;
import com.hp.hpl.jena.sdb.sql.SDBConnection;
import com.hp.hpl.jena.sdb.store.TableDesc;

public class TupleLoaderHashH2 extends TupleLoaderHashBase {

	public TupleLoaderHashH2(SDBConnection connection, TableDesc tableDesc,
			int chunkSize) {
		super(connection, tableDesc, chunkSize);
	}
	
	@Override
    public String[] getNodeColTypes() {
		return new String[] {"BIGINT", "CLOB", "VARCHAR(10)", "VARCHAR("+ TableDescNodes.DatatypeUriLength+ ")", "INT"};
	}
	
	@Override
    public String getTupleColType() {
		return "BIGINT";
	}
	
	@Override
    public String[] getCreateTempTable() {
		return new String[] { "CREATE TEMPORARY TABLE" , "ON COMMIT DELETE ROWS" };
	}

        // Contradicts above, but something is up with removal.	
	@Override
	public boolean clearsOnCommit() { return false; }
	
	@Override
	public String getNodeLoader() {
		return super.getNodeLoader() + hashCode();

	}
	
	@Override
	public String getTupleLoader() {
		return super.getTupleLoader() + hashCode();
	}
}
