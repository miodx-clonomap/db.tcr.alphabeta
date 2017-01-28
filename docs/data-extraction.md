# Data extraction

The input GenBank entries are under `data/` inside this repo. For each of them we need to

1. retrieve the "segment" annotations
2. extract from the entry sequence the regions that the annotations link to

## Segment annotations

The feature names are `V_segment, D_segment, J_segment, C_region`. All of them contain a `/standard_name="xyz"` attribute; this is the ID we want. Here are a couple of samples

```
C_region         join(554193..554579,555021..555038,555191..555297,
                 555620..555640)
                 /gene="TRBC1"
                 /gene_synonym="BV05S1J2.2; TCRB; TCRBC1"
                 /standard_name="TRBC1"
```

```
V_segment        complement(join(575084..575375,575742..575784))
                 /gene="TRBV30"
                 /gene_synonym="TCRBV20S1A1N2; TCRBV30S1"
                 /standard_name="TRBV30"
```

### Segment sequences

We just need to extract from the entry sequence the corresponding location, which in the last example above is `complement(join(575084..575375,575742..575784))`. Working with this will be part of the bio4j/data.genbank` parser.

See the GenBank entry format docs for [Location](http://www.insdc.org/files/feature_table.html#3.4) and [Location examples](http://www.insdc.org/files/feature_table.html#3.4.3).
