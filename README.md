## Compression of 3D coordinates of macromolecular structures
This repository contains the compression methods described in the paper "**Towards an efficient compression of 3D coordinates of macromolecular structures**". These methods provide the foundation for a novel standard to represent macromolecular coordinates in a *MacroMolecular Transmission Format (MMTF)* for 3D structures (http://mmtf.rcsb.org). This format allows a compact representation and interactive visualization of the largest macromolecular complexes that are currently in the Protein Data Bank.

## Install from git repository
You can get the latest source code using git and build the project using [Maven](http://maven.apache.org/guides/getting-started/index.html#What_is_Maven).
```
$ git clone git@github.com:rcsb/coordinates-compression.git
$ cd coordinates-compression
$ mvn install
```

## How to get 3D structures for

a Hadoop sequence file with the PDB structures in MMTF format
