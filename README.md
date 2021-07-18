# external-sort
How do you sort data that doesn't fit into your memory?

# Usage

cat unsorted_data | java CreateRuns M| java MergeRuns N > sorted

where M and N are user-specified number which represent the available memory can be used(M) and the number of temporyary files to merge all partially sorted data(N) respectively.
