# Curated Data Import

The raw input consists of the files under the `data/` folder. We will do first an alpha release having *only* TCR-β sequences; the rest of the document makes this assumption. There should not be any significant difference in the process for TCR-α data.

## Extract coding sequences from the GenBank entry

The entry `human-trb-locus.gb` contains all the TCR-β sequences we are going to include. I have used [Feature Extract](http://www.cbs.dtu.dk/services/FeatureExtract/) for extracting a fasta file containing the coding sequences for all V, D, J segments.

> **IMPORTANT** make sure to check the checkbox "Splice all intron containing seqeunces" when running Feature Extract if you want to replicate this

This fasta file `data/human-trb-locus.fasta` has the "gene" names as headers:

```
>TRBV1
ATGGGCTGAAGTCTCCACTGTGGTGTGGTCCATTGTCTCAGGCTCCATGGATACTGGAAT
TACCCAGACACCAAAATACCTGGTCACAGCAATGGGGAGTAAAAGGACAATGAAACGTGA
GCATCTGGGACATGATTCTATGTATTGGTACAGACAGAAAGCTAAGAAATCCCTGGAGTT
CATGTTTTACTACAACTGTAAGGAATTCATTGAAAACAAGACTGTGCCAAATCACTTCAC
ACCTGAATGCCCTGACAGCTCTCGCTTATACCTTCATGTGGTCGCACTGCAGCAAGAAGA
CTCAGCTGCGTATCTCTGCACCAGCAGCCAAGA
>TRBV2
ATGGATACCTGGCTCGTATGCTGGGCAATTTTTAGTCTCTTGAAAGCAGGACTCACAGAA
CCTGAAGTCACCCAGACTCCCAGCCATCAGGTCACACAGATGGGACAGGAAGTGATCTTG
CGCTGTGTCCCCATCTCTAATCACTTATACTTCTATTGGTACAGACAAATCTTGGGGCAG
AAAGTCGAGTTTCTGGTTTCCTTTTATAATAATGAAATCTCAGAGAAGTCTGAAATATTC
GATGATCAATTCTCAGTTGAAAGGCCTGATGGATCAAATTTCACTCTGAAGATCCGGTCC
ACAAAGCTGGAGGACTCAGCCATGTACTTCTGTGCCAGCAGTGAAGC
```

These are the sequences which we need to include.

## Annotation

We annotate them using IgBLAST so that the IgBLAST-provided CDRx positions are correct. It would be good to have another independent way of checking that these CDRx positions are correct for our database.

### V genes

These sequences are longer than those found in the IMGT database: we need to identify the signal peptide and remove it from all V sequences.

We will do this based on *web* IgBLAST results (the alignment with the best V hit).

Some of our V genes wil lbe identical (modulo signal peptide) to those in the IGMT database, some will not be there; in both cases sequences keep the original name they had. We will store both the complete V sequences and those without the signal peptide.

### D genes

Join each D gene with a V gene which is in the IMGT database, and run IgBLAST on the corresponding construct.

### J genes

Join each J gene with a V and D gene which are in the IMGT database, and run IgBLAST on the corresponding construct. As before, we keep the original names.

#### Coding frame

The coding frame can be extracted from the GenBank annotation file (`data/human-trb-locus.gb` in this case): in the `CDS` annotation field we have

```
CDS             <551487..>551539
                 /gene="TRBJ1-6"
                 /gene_synonym="TCRBJ1S6; TRBJ16"
                 /exception="rearrangement required for product"
                 /codon_start=2
                 /db_xref="GeneID:28630"
                 /db_xref="HGNC:HGNC:12167"
                 /db_xref="IMGT/GENE-DB:TRBJ1-6"
```

we need to take the `codon_start` value and convert it to a 0-based frame (substract 1).

#### CDR3 stop

This position should be derived from the IgBLAST alignment. It should be expressed as a 0-based position.

### CDR3 auxiliary info

> This only applies to **J* genes

Quoting from IgBLAST README:

> 2.  Optional files (download from the release/ directory):
This is the file to indicate germline J gene coding frame start position (position is 0-based), the J gene type,
and the CDR3 end position for each sequence in the germline J sequence database (Fields are tab-delimited).
>
>Note that the supplied file contains only information for NCBI or IMGT  germline gene sequence database
(including gene names as well as the sequences).   If you search your own database and if it contains different
sequences or sequence identifiers, then you need to edit that file (or supply your own file) to reflect that
or you won't get proper frame status or CDR3 information (other results will still be shown correctly).
See human_gl.aux or mouse_gl.aux for examples.  Enter -1 if the frame information is unknown.

Here's a fragment of that `human_gl.aux` file:

```
# The chain type, first coding frame start position, chain type, CDR3 stop.
#positions are 0-based

TRBJ1-1*01	2	JB	16
TRBJ1-2*01	2	JB	16
TRBJ1-3*01	1	JB	18
TRBJ1-4*01	2	JB	19
TRBJ1-5*01	1	JB	18
TRBJ1-6*01	1	JB	21
TRBJ1-6*02	1	JB	21
TRBJ2-1*01	1	JB	18
TRBJ2-2*01	2	JB	19
TRBJ2-2P*01	0	JB	20
TRBJ2-3*01	0	JB	17
TRBJ2-4*01	1	JB	18
TRBJ2-5*01	2	JB	16
TRBJ2-6*01	1	JB	21
TRBJ2-7*01	1	JB	15
```
