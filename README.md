## Compression of 3D coordinates of macromolecular structures
This repository contains the compression methods described in the paper "**Towards an efficient compression of 3D coordinates of macromolecular structures**". These methods provide the foundation for a novel standard to represent macromolecular coordinates in a *MacroMolecular Transmission Format (MMTF)* for 3D structures (http://mmtf.rcsb.org). This format allows a compact representation and interactive visualization of the largest macromolecular complexes that are currently in the Protein Data Bank.

## Install from git repository
You can get the latest source code using git. Then you can execute the *install* goal with [Maven](http://maven.apache.org/guides/getting-started/index.html#What_is_Maven) to build the project.
```
$ git clone git@github.com:rcsb/coordinates-compression.git
$ cd coordinates-compression
$ mvn install
```
The *install* goal will compile, test, and package the project’s code and then copy it into the local dependency repository which Maven maintains on your local machine.

## How to run the analysis
Maven **exec** plugin lets you run the main method of a Java class in the project, with the project dependencies automatically included in the classpath.

The proposed approaches are implement as two types of strategies: (i) intramolecular compression that operates on the sequence of atoms within a polymer chain; and (ii) intermolecular compression designed for the compression of special cases of multiple chains with identical atoms, such as NMR models and structures with repeated identical subunits.

### Run intramolecular compression analysis
```
mvn exec:java -Dexec.mainClass="org.rcsb.mmtf.analysis.RunTotalAnalysis" -Dexec.args="arg1 arg2"
```
`arg1`: path to a Hadoop sequence file with the PDB structures in MMTF format

`arg2`: path to the folder with results

### Run intermolecular compression analysis
```
mvn exec:java -Dexec.mainClass="org.rcsb.mmtf.analysis.RunEnsamblesAnalysis" -Dexec.args="arg1 arg2"
```
`arg1`: path to a Hadoop sequence file with the PDB structures in MMTF format

`arg2`: path to the folder with results

## How to get 3D structures in MMTF format
You can download a Hadoop sequence file with the PDB structures in MMTF format.
```
$ wget http://mmtf.rcsb.org/v1.0/hadoopfiles/full.tar
$ tar -xvf full.tar
```
This will get and unpack the content of the Hadoop Sequence File to a full folder.
