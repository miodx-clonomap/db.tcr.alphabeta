# db.tcr

[![](http://github-release-version.herokuapp.com/github/ohnosequences/db.tcr.alphabeta/release.svg)](https://github.com/ohnosequences/db.tcr.alphabeta/releases/latest)
[![](https://img.shields.io/badge/license-AGPLv3-blue.svg)](https://tldrlegal.com/license/gnu-affero-general-public-license-v3-%28agpl-3.0%29)

A database including germline TCR (α and β) VDJ genes for Human and Mouse.


## NCBI Gene entries

Sequences for each loci are derived from the corresponding GenBank entries.

#### Human

- [TCR-α][TCRA-human-locus]
- [TCR-β][TCRB-human-locus]

#### Mouse

- [TCR-α][TCRA-mouse-locus]
- [TCR-β][TCRB-mouse-locus]

## Data generation

The generation of the data is done through release-only tests that generate the databases and automatically upload them to S3.

See [`src/test/scala/genericTests.scala`](src/test/scala/genericTests.scala) for more information.

<!-- refs -->
[TCRA-human-locus]: https://www.ncbi.nlm.nih.gov/gene/6955
[TCRA-gene-family]: http://www.genenames.org/cgi-bin/genefamilies/set/371

[TCRB-human-locus]: https://www.ncbi.nlm.nih.gov/gene/6957
[TCRB-gene-family]: http://www.genenames.org/cgi-bin/genefamilies/set/372

[TCRA-mouse-locus]: https://www.ncbi.nlm.nih.gov/gene/21473
[TCRB-mouse-locus]: https://www.ncbi.nlm.nih.gov/gene/21577
