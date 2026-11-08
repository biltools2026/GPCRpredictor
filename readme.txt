This is a computational method for predicting GPCR transmembrane regions. 

TransGPCR modular of the package is used for predicting the transmembrane regions of GPCR proteins. More than 6,000 expert-annotated GPCR transmembrane region data entries were screened from the latest version of the UniProt database, and the Neural network algorithm was used for thorough modeling. The resulting model achieved a tested accuracy of over 90%.

Install
The program can be after the perl is installed. The perl program is deposited in the install directory.

Running command
perl gpcrrun.pl -gpcr E:\Udisk128\projects\MetaSS\example\seq.fasta

The sequence path should be absolute directory.

The output result is gpcr_trans.txt, in which O stands for non-transmembrane region
and M stands for transmembrane region.

Download link
https://pan.baidu.com/s/1IwsqQBPW_bUbmtCzp2wqmA?pwd=1234
code: 1234

Currently, the program can only be run in Windows systems.

Performance
The model was trained on a data set consisting of thousands of GPCR proteins.
And the accuracy of Q2 exceeds 90%.
