# quarkus-data-source

## Description

Maven project to demonstrate some things regarding Quarkus and data sources 
(mostly agroal related).

The project is divíded into functions. They can be called by using the function
name as first command line parameter. 

Some functions might require further parameters.

After building the project using `mvn package` the functions can be called as 
follows:

`java -jar target/quarkus-app/quarkus-run.jar <function> ...`

## agroal-result-set-leaked

This function shows how the agroal pool issues a warning message when the 
result set from a sql select statement processing is not closed explicitly.

And it shows that this message is not issued when the result set is also programmed 
using advanced resource management in a nested try-with-resources block.

So when calling ...

`java -jar target/quarkus-app/quarkus-run.jar agroal-result-set-leaked`


... one notices an output of ...

```
2024-06-29 22:11:25,888 INFO  [de.quo.qua.dat.sou.App] (main) Insert customer record: [{UPDATE_COUNT=1}]
2024-06-29 22:11:25,922 WARN  [io.agr.pool] (main) Datasource '<default>': JDBC resources leaked: 1 ResultSet(s) and 0 Statement(s)
2024-06-29 22:11:25,923 INFO  [de.quo.qua.dat.sou.App] (main) Select customer records: [{ID=0, NAME=Clemens Quoß}]
2024-06-29 22:11:25,924 INFO  [de.quo.qua.dat.sou.App] (main) Select customer records (nested-arm): [{ID=0, NAME=Clemens Quoß}]
2024-06-29 22:11:25,925 INFO  [de.quo.qua.dat.sou.App] (main) Drop customer table: [{UPDATE_COUNT=0}]
```

I consider the behaviour of the agroal pool a bug, as the result set is closed implicitly when the statement is closed.

But on the other hand leads the explicitly closing of the result set to clearer coding maybe, though some boilerplate
code is needed. Just answer this for yourself.
 