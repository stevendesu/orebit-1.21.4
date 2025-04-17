## Primer on RAM

Every program on your computer must share the same RAM. In order to ensure
programs play nicely, the Operating System assigns sections of RAM to each
process. For example, when I launch a program the Operating System may set
aside 50 megabytes of the 16,000 megabytes available to my computer and say
"this memory is __exclusively__ for use by program A".

Throughout its execution, the program may decide it needs more memory in order
to complete its tasks. When this happens, the program asks the Operating System
for additional RAM and the Operating System finds an unused section of memory,
assigning it to the program. This process is known as "memory allocation".

Memory allocation can take some time. Using memory that is already assigned to
your program is much, much faster.

When your program is finished with memory, it has the option to give it back to
the Operating System so that another program can use it later. This process is
called "memory deallocation". This is significantly faster than memory
allocation. A completely different issue arises here, though.

Theoretically every piece of memory should be allocated once and deallocated
once. However subtle bugs in code can lead to allocating memory and never
deallocating it (memory leak) or attempting to deallocate it twice (double
free). You might also try to read from memory afer it has been deallocated
(segmentation fault).

To save developers from these mistakes, many modern programming languages
(Java included) have what's known as a "Garbage Collector". The Garabge
Collector's job is to scan your memory occasionally looking for memory that is
no longer being used and is safe to deallocate.

The problem with Garbage Collectors is that they're slow. __Very__ slow. So the
more we can avoid them, the faster we'll be.

## Object Pooling

TBD
