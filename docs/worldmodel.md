# World Model

## Region-Level

## Block-Level

In order to rapidly evaluate what movements are possible from a given block,
we pre-compute data at every block which facilitates these computations.

The data computed is listed below:

| Datum        | Bit Length | Meaning                                                                                                                            |
|--------------|:----------:|------------------------------------------------------------------------------------------------------------------------------------|
| Stand Height |     2      | 0 = intangible (e.g. air, flowers)<br />1 = special (e.g. carpet)<br />2 = half-block (e.g. slab)<br />3 = full block (e.g. stone) |
| Headroom     |     2      | 0, 1, 2, or "3+" blocks of air above this block                                                                                    |
| Slow         |     1      | Whether walking on this block will slow the player (e.g. block is soul sand, OR has cobweb on top of it)                           |
| Hardness     |     3      | Stores $floor(log_2(min(hardness, 31)*8))+1$ for approximating break difficulty                                                    |

