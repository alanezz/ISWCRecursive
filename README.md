# ISWCRecursive

This repository is for recursive SPARQL project. You have to import it in a Eclipse project.
It comes with all the libraries.
Notes:

###In jena-arq/src-examples - arq.examples:

Be careful with the folders!

- LoadTDB.java -> Loads NT files to a TDB folder

- ProvDiscExperiments.java -> Tests the materialized experiments (Yago, LMDB)

- ProvMemExperiments.java -> Test the memory experiments (Provenance)

###In jena-arq/src/main/java - com.hp.hpl.jena.query:

- QueryExcecutionFactory.java -> Edit the strategy (Memory, with limit, materialized)

###In jena-arq/src/main/java - com.hp.hpl.jena.sparql.lang:

- RecursiveNode.java -> Edit the /path/to/recursive/folder. Is the aux folder for materialized recursion.

