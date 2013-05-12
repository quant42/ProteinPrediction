=== Run information ===

Scheme:weka.clusterers.EM -I 100 -N -1 -M 1.0E-6 -S 100
Relation:     proteinInteraction-weka.filters.unsupervised.attribute.Remove-R1-3,6-8
Instances:    18665
Attributes:   3
              ncbiTaxaId
              regna
              stat1
Test mode:evaluate on training data

=== Model and evaluation on training set ===


EM
==

Number of clusters selected by cross validation: 7


                    Cluster
Attribute                 0           1           2           3           4           5           6
                      (0.1)      (0.15)      (0.07)      (0.25)      (0.07)      (0.16)       (0.2)
====================================================================================================
ncbiTaxaId
  mean             3634.0715   9872.2094  400371.408  29640.6887  205219.534   9559.7944  83333.0754
  std. dev.         281.1525     47.0953 138121.4795  40184.5224  18785.9697    377.6634       0.264

regna
  vertebrates              1           1      1.0001      1.0178           1    806.9821           1
  bacteria            1.0005           1   1213.0071   4323.0819    224.2849           1   3749.6256
  human                    1           1      1.0001      1.0099           1     1543.99           1
  fungi                    1           1         139           1           1           1           1
  mammals                  1   2823.8222           1       1.002           1      1.1758           1
  plants           1879.9868           1      1.0001      1.0131           1           1           1
  archaea             1.0008           1       1.122     254.038   1079.8392           1           1
  rodents                  1           1      1.0001      1.0211           1    638.9788           1
  [total]          1886.9881   2830.8222   1358.1295   4583.1838   1310.1241   2995.1267   3756.6256
stat1
  U                 299.0001    312.9913     79.4327    181.1086    147.4874    150.0062    280.9737
  L                        1           1      1.0052     13.9952     41.9998     17.9998           1
  H                 352.9972    674.9559     537.327   1583.0413    510.8523    649.0314   1452.7949
  I                        1           1           1       1.001           1           1       5.999
  1                 712.9927    779.9081    333.2456   1533.8173    334.0269   1710.0687    908.9408
  2                 517.9981   1058.9669    404.1189   1268.2204    272.7577    465.0207   1104.9172
  [total]          1884.9881   2828.8222   1356.1295   4581.1838   1308.1241   2993.1267   3754.6256


Time taken to build model (full training data) : 327.94 seconds

=== Model and evaluation on training set ===

Clustered Instances

0       1879 ( 10%)
1       2823 ( 15%)
2       1250 (  7%)
3       4611 ( 25%)
4       1366 (  7%)
5       2987 ( 16%)
6       3749 ( 20%)


Log likelihood: -10.75784


