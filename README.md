# DuplicateLineRemover

Computes two HASH values for every line using following formula:

HASH1:

∑<sub>i=1</sub><sup>l/2</sup> [ (b<sub>i-1</sub> * i + (1234-b<sub>i</sub>) / i)%127 ]

HASH2:

∑<sub>i=l/2</sub><sup>l</sup> [ ((b<sub>i</sub> * i) - (b<sub>i</sub> / i))%127 ]

Through Merge sort while using these two hash value sorts the file and then removes every duplicate line.



There are two interfaces:

CLI: ![CLI](https://github.com/mahir1010/DuplicateLineRemover/blob/SCRSHOTS/SCRSHOTS/CLI.png)
GUI: ![GUI](https://github.com/mahir1010/DuplicateLineRemover/blob/SCRSHOTS/SCRSHOTS/Computing%20Hash.png)

Processed File : ![Processed File](https://github.com/mahir1010/DuplicateLineRemover/blob/SCRSHOTS/SCRSHOTS/Duplicate%20Files%20removed.png)
