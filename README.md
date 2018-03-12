## Structured Information on State and Evolution of Dockerfiles - Online Appendix
This is the online appendix of our submission. It provides additional information to our dataset, how to build and run our toolchain to collect the data, and a link to our database dump.

### Table of Contents
1. **[Database Dump](#database-dump)**<br>
2. **[Entity Relationship Diagram](#entity-relationship-diagram)**<br>
3. **[Tables](#tables)**<br>
4. **[Example Queries](#example-queries)**<br>
5. **[Run Instructions](#run-instructions)**<br>
6. **[Build Instructions](#build-instructions)**<br>


### Database Dump
A compressed database dump (~700 MB) is available [here](https://drive.google.com/file/d/1YFEDeizmInuwThrFjfixBc_owWV4Dlz5/view?usp=sharing). Use `gunzip -c msr18_dump.sql.gz | psql dbname` to decompress and import into a database named `dbname`. <br>
**Note**: the database requires ~20 GB of storage.

### Entity Relationship Diagram
Here you find a simplified entity relationship diagram of our data model. Due to space reasons and to foster readability we omitted some links to _single instruction_ and _multiple instructions_ tables. As the name implies, a single _Snapshot_ can have a single link to a single-instruction table (e.g., _Entrypoint_, _CMD_). In addition, all links to a multiple-instruction table are of the form _one to many_, (i.e., a _Snapshot_ can have multiple _RUN_ instructions).

![https://raw.githubusercontent.com/juice457/DFA/master/erd.png](https://raw.githubusercontent.com/juice457/DFA/master/erd.png)

#### Tables
In the following you will find descriptions of tables, for a comprehensive overview we refer to the attached ERD and a SQL schema (file _schema.sql_).

##### Project
Each Git Repository is modelled as a _Project_ and includes meta information about the project (number of followers, forks etc.)

##### Dockerfile
A _Project_ includes one or more _Dockerfiles_. A _Dockerfile_ is a text document that contains all the commands a
user could execute on the command line to assemble an image.
 
##### Snapshot
A _Snapshot_ represents a state of a Dockerfile at a certain time, i.e., if a Dockerfile has been changed 8 times, 
then there will be 8 snapshots. Basically, a Snapshot can be seen as a "Commit".

##### Diff
Each _Snapshot_ has two _Diffs_. The first _Diff_ connects the old Snapshot with the current one, 
and the second one connects the current one with the new one. Basically, a Diff is the "Transition" of 
one Snapshot to a newer one. A _Diff_ contains one or more _Diff_Types_.

##### DiffType
DiffType show what type of change have been done on a Snapshot. It includes the old and new state as a string. 
 
### Example Queries
```SQL
###################################################################################################################################
##1. Dependencies
###################################################################################################################################
##[1] Most frequent used ports
SELECT port, count(port)
FROM df_expose 
WHERE current = true
GROUP BY port
ORDER BY count DESC

##[2] How often do dependencies change
SELECT *
FROM diff_type NATURAL JOIN diff NATURAL JOIN snap_diff NATURAL JOIN snap_id
WHERE change_type LIKE '%Updat%' AND instruction = 'RUN'

##[3.1] Which images are preferred
SELECT imagename, count(imagename)
FROM df_from 
WHERE current = true
GROUP BY imagename
ORDER BY count DESC

##[3.2] Which image versions are preferred (NUMERIC) 
SELECT imageversionnumber, count(imageversionnumber)
FROM df_from 
WHERE current = true
GROUP BY imageversionnumber
ORDER BY count DESC

##[3.3] Which image versions are preferred  (NOMINAL)
SELECT imageversionstring, count(imageversionstring)
FROM df_from 
WHERE current = true
GROUP BY imageversionstring
ORDER BY count DESC


##[4] How many images use the :latest tag?
SELECT imageversionnumber, count(imageversionnumber)
FROM df_from 
WHERE current = true
GROUP BY imageversionnumber
ORDER BY count DESC

##[5] Which parameters are most frequently used in RUN instructions?
SELECT run_params, count(run_params)
FROM run_params
GROUP BY run_params
ORDER BY count DESC

##[5] Top RUN instructions
SELECT executable,count(executable), run_params
FROM df_run df NATURAL JOIN run_params rp
WHERE df.current=true
GROUP BY executable, run_params
ORDER BY count(executable) DESC


###################################################################################################################################
##2. Churn and Co-Evolution
###################################################################################################################################
##[1] How often do Dockerfiles change (average)
SELECT avg(count)
FROM (
SELECT dock_id, count(*)
FROM dockerfile NATURAL JOIN snapshot 
GROUP BY dock_id) s

##[2] How many times do Dockerfiles change with other files?
SELECT avg(count)
FROM(
SELECT dock_id, count(dock_id)
FROM(SELECT s.snap_id, s.dock_id, count(s.snap_id)
FROM snapshot s NATURAL JOIN changed_files c
WHERE c.range_index = 0
GROUP BY s.snap_id
ORDER BY count ASC) g NATURAL JOIN dockerfile
WHERE g.count = 1
GROUP BY dock_id) f

SELECT avg(count)
FROM(
SELECT dock_id, count(dock_id)
FROM(SELECT s.snap_id, s.dock_id, count(s.snap_id)
FROM snapshot s NATURAL JOIN changed_files c
WHERE c.range_index = 0
GROUP BY s.snap_id
ORDER BY count ASC) g NATURAL JOIN dockerfile
WHERE g.count > 1
GROUP BY dock_id) f

##[2.1] Which changes are made when a Dockerfile changes alone 
SELECT change_type, count(change_type)
FROM(
SELECT s.snap_id, s.dock_id, count(s.snap_id)
FROM snapshot s NATURAL JOIN changed_files c
WHERE c.range_index = 0
GROUP BY s.snap_id
ORDER BY count ASC) f NATURAL JOIN snap_diff NATURAL JOIN  diff NATURAL JOIN diff_type
WHERE diff_state = 'COMMIT_COMMIT'
GROUP BY change_type
ORDER BY count DESC

##[2.1] Which changes are made when a Dockerfile changes together with other files
SELECT change_type, count(change_type)
FROM(
SELECT s.snap_id, s.dock_id, count(s.snap_id)
FROM snapshot s NATURAL JOIN changed_files c
WHERE c.range_index > 1
GROUP BY s.snap_id
ORDER BY count ASC) f NATURAL JOIN snap_diff NATURAL JOIN  diff NATURAL JOIN diff_type
WHERE diff_state = 'COMMIT_COMMIT'
GROUP BY change_type
ORDER BY count DESC

##[3] Which files and file types are changed when a Dockerfile is changed?
SELECT full_file_name, count(full_file_name)
FROM changed_files
WHERE range_index = 0
GROUP BY full_file_name
ORDER BY count DESC

SELECT file_type, count(file_type)
FROM changed_files
WHERE range_index = 0
GROUP BY file_type
ORDER BY count DESC

##[9] Which files are changed within a certain range_index?
SELECT full_file_name
FROM changed_files
WHERE range_index = -1
INTERSECT
SELECT full_file_name
FROM changed_files
WHERE range_index = 0
INTERSECT
SELECT full_file_name
FROM changed_files
WHERE range_index = 1
INTERSECT
SELECT full_file_name
FROM changed_files
WHERE range_index = 2

##[10] How many files change in average together with a Dockerfile (index= 0)
SELECT avg(snap_id)
FROM (
SELECT snap_id, count(snap_id)
FROM changed_files
WHERE range_index = 0
GROUP BY snap_id
ORDER BY count(snap_id) DESC ) s

##[11] List of most changed instructions
SELECT instruction, count(instruction)
FROM diff_type
WHERE change_type LIKE '%Update%'
GROUP BY instruction
ORDER BY count(instruction) DESC

##[12] How many commits per year and per month
SELECT count(*), date_trunc('year', to_timestamp(commit_date)) s
from snapshot
group by date_trunc( 'year', to_timestamp(commit_date) )
ORDER BY s ASC

SELECT count(*), date_trunc('month', to_timestamp(commit_date)) s
from snapshot
group by date_trunc( 'month', to_timestamp(commit_date) )
ORDER BY s ASC

###################################################################################################################################
## Others
###################################################################################################################################
##[2] Which rules are violated according best practices?
SELECT violated_rules, count(violated_rules) 
FROM violated_rules 
GROUP BY violated_rules 
ORDER BY count DESC

##[3] Docker usage adoption rate according USERS/ORGANIZATIONS ?
SELECT count(*), date_trunc('year', to_timestamp(first_docker_commit)) s, i_owner_type
FROM dockerfile
group by date_trunc( 'year', to_timestamp(first_docker_commit)), i_owner_type
ORDER BY s  ASC

##[6] Most used words in comments
SELECT word, count(*)
FROM ( 
  SELECT regexp_split_to_table(comment, '\s') as word
  FROM df_comment
) t
GROUP BY word
ORDER BY count DESC

##[7] Which instructions are commented more frequently?
SELECT instruction, count(instruction)
FROM df_comment NATURAL JOIN snapshot
WHERE index = true AND instruction LIKE '%before%'
GROUP by instruction
ORDER BY count DESC

##[8] Preferred source and destination of ADD and COPY instructions
SELECT source, count(source) c
FROM df_add
WHERE current=true
GROUP BY source
ORDER BY c DESC

SELECT source, count(source) c
FROM df_copy
WHERE current=true
GROUP BY source
ORDER BY c DESC

SELECT destination, count(destination) c
FROM df_copy
WHERE current=true
GROUP BY destination
ORDER BY c DESC

SELECT destination, count(destination) c
FROM df_add
WHERE current=true
GROUP BY destination
ORDER BY c DESC
```

### Run Instructions
There are different possibilities on how the toolchain can be executed. The tool _dfa_tool.jar_ can be found in the  _/tool_ folder):

1. Demo purpose, a default GitHub project gets analyzed
   * Arguments: No parameter
   * Example: `java -jar dfa_tool.jar` 
2. Analyze a specified GitHub project that includes at least one Dockerfile
   * Arguments: {Github project url} (i.g. https://github.com/raiden-network/raiden)
   * Example: `java -jar dfa_tool.jar https://github.com/raiden-network/raiden`
2. Analyze a specified GitHub project and save results into a postgres database
   * Arguments: {Github project url} -db
   * Requirements: Setup a local postgres db with port `5432`, username `postgres`, password `postgres`, and an empty database with name `dfa`
   * Example: `java -jar dfa_tool.jar https://github.com/raiden-network/raiden -db`
2. Analyze a specified GitHub project and save results into a json file
    * Arguments: {Github project url} -json
    * Hint: you find the json file in root folder of this project
    * Example: `java -jar dfa_tool.jar https://github.com/raiden-network/raiden -json`
    
### Build Instructions
Build project with `mvn install`
