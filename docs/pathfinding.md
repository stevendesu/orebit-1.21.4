# Pathfinding

TBD: Explanation for how it works

## Region-level

Explanation

## Block-level

Explanation

### Movements

Offsets below are listed as:

```
(direction of motion)
(vertical axis)
(orthogonal axis)
```

In other words, we assume the movement is being  taken in the X
direction then record the X,Y,Z  offset.

Offsets with a colon (`:`) indicate a range of possible values.
For instance, `-1:2` implies values of -1, 0, 1, or 2 (inclusive).

| Move     |  Valid Directions  |     Offset      |
|:---------|:------------------:|:---------------:|
| Walk     |   4 (N, E, S, W)   |     1, 0, 0     |
| Diagonal | 4 (NE, SE, NW, SW) |     1, 0, 1     |
| Ascend   |   4 (N, E, S, W)   |     1, 1, 0     |
| Descend  |   4 (N, E, S, W)   |    1, -1, 0     |
| Jump_1   |   12 (see image)   |   2, 0, -1:1    |
| Jump_2   |   16 (see image)   |   3, 0, -1:1    |
| Jump_3   |   24 (see image)   |   4, 0, -1:1    |
| Climb    |         2          | 0, (-1 or 1), 0 |
| Fall     |         1          |    0, -1, 0     |
| Break    |        109*        |     0, 0, 0     |
| Place    |         8          |     0, 0, 0     |

**Walk:** As the name implies, this is an instruction to walk
from one block to another. This is only possible if the blocks
are at the same height and are adjacent to one another.

**Diagonal:** As the name implies, this is an instruction to walk
diagonally. This instruction requires that four blocks all share
a height: the starting position, the destination position, and
the two cardinal directions. For example, to move northeast there
must be flat ground to the north *and* the east.

**Ascend:** This refers to traveling up a 1-block stair step.
This motion requires both a movement and a jump.

**Descend:** This refers to traveling *down* a 1-block stair step.
This motion only requires walking, as gravity will handle the
change in height.

**Jump_X:** This refers to jumping over a gap and landing on a
block at the same height. As this movement travels more than a
single block
