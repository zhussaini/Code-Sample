Spaceship Escape

Overview
--------

You find yourself on an exploding planet. You’re fortunate. You’ve anticipated this day, and saved an old frigate to escape in! Unfortunately your frigate doesn’t have the latest navigation systems, so you’re going to have to chart the escape course manually.

Your frigate can fire thrusters to accelerate, coast at your existing speed, or fire thrusters to decelerate at any discrete time t. We’re going to describe our escape course as a function of discrete time t which starts at t = 0. Plotting an escape course means entering an array, indexed from time t = 0, with instructions for the thrusters:

Fire your thrusters to accelerate... 1
Coast at your existing speed... 0
Fire your thrusters to decelerate... -1

You plot your course by entering JSON into the frigate’s computer, for example:

[0, 0, 1, 1, -1, 0, 1]

The planet has an asteroid field circling it, and the asteroids can easily destroy your ship. In addition, the planet's blast radius will also follow your ship at a fixed rate given by t_per_blast_move, which is the amount of time it takes for the blast radius to increase by one position. The initial blast radius is 0. If your spaceship is inside the blast radius, you will die. Finally, it’s possible that you can drive your ship directly into the planet’s surface itself, in which case you will also be destroyed.

You have a chart of these fields, so you know when the asteroids will be in your path. The asteroids circle the planet in concentric rings. Each ring contains exactly one asteroid, moving clockwise as t increases. Each asteroid has an offset which represents the orientation of the asteroid at t = 0, and a t_per_asteroid_cycle, which is the number of orientations the asteroid will successively assume as t increases.

The planet itself is always at position -1.  There are no asteroids on it, but the blast will soon consume it. The frigate is at position 0 at t = 0. The asteroids are at positions 1, 2, 3... 

Your ship travels along offset 0.  Don’t worry about hitting asteroids between positions when you’re going fast (v > 1); they are between orientations and therefore cannot be hit.

Algorithm
---------

The original problem is abstracted to the problem of finding a path in a 
directed, unweighted graph G. The nodes of G are the possible states of the 
spaceship. The spaceship's state is described by three integers - its 
position, velocity, and time - which we can write as an ordered triple 
(p, v, t). In the graph G, there is an edge from node A to B if it is possible 
to reach B from A with a single instruction. If we start at a state (p, v, t) 
and issue the instruction to accelerate by a, then we will arrive at a state 
(p + v + a, v + a, t + 1). By iterating over all possible accelerations 
(a = -1, 0, 1), we can generate all of the states that are reachable from a 
given starting state. 

In our problem, the spaceship's initial state is (0, 0, 0). Using breadth 
first search, we can explore nodes until we reach a state that is outside of 
the asteroid belt. For each state that is explored during the search, the 
previous state is stored in a hash table. When we finally reach a state that 
is outside the asteroid belt, the hash table is used to retrace the path and 
calculate the values of acceleration that were used to escape. 

Because each node (p, v, t) only generates nodes that have time t + 1, breadth 
first search naturally explores the nodes in time order. The first node that 
we find that is outside the asteroid belt also represents the fastest escape 
path. Therefore, this algorithm is optimal and always finds the fastest escape 
path. 


Time and Space Complexity
-------------------------

Let d be the length of the course that is used to escape. Breadth first search 
will explore all paths from the initial state that have length d. The 
branching factor of our graph is three. Each node of G generates three new 
nodes because there are three values of acceleration to choose. After d levels 
of breadth first search, we will have explored 3^d nodes. Therefore the time 
complexity is O(3^d). Because we are using a hash table to keep track of each 
node we have explored, the space complexity is also O(3^d).

Code Overview
-------------

SpaceshipEscape.java:
   * contains main method
   * main method is split into three parts
      1. Read input data from file using gson to parse json
      2. Calculate escape course using a CourseCalculator object
      3. Write escape course to file using gson to convert to json
CourseCalculator.java:
   * contains one public method, calculateEscapeCourse(), which contains the 
     breadth first search code
   * breadth first search uses SpaceshipState objects as graph nodes
   * private helper function isValidSpaceshipState(SpaceshipState) used to 
     determine whether to continue searching from a given state based on 
	 whether the spaceship dies at this state
SpaceshipState.java:
   * represents graph node for breadth first search
   * accelerate(int) method takes a value of acceleration and generates next 
     SpaceshipState
Asteroid.java:
   * represents asteroid, can query for current asteroid orientation


Dependencies
------------

gson-2.3.1 is used to read and write json files.


Classes
-------

SpaceshipState
   methods:
      +main(String[]): void

CourseCalculator
   variables:
      -timePerBlastMove: int
      -asteroids: ArrayList<Integer>
      -lastAsteroidPosition: int
      -MIN_ACCELERATION: int = -1
      -MAX_ACCELERATION: int = 1
   methods:
      +CourseCalculator(int, ArrayList<Asteroid>)
      +calculateEscapeCourse(): LinkedList<Integer>
      -isValidSpaceshipState(SpaceshipState): boolean

SpaceshipState
   variables:
      -position: int
      -velocity: int
      -time: int
   methods:
      +SpaceshipState(int, int, int)
      +accelerate(int): SpaceshipState
      +getPosition(): int
      +getVelocity(): int
      +getTime(): int
      +equals(Object): boolean
      +hashCode(): int
      +toString(): String

Asteroid
   variables:
      -offset
      -timePerAsteroidCycle
   methods:
      +Asteroid(int, int)
      +currentOrientation(int): int


Author
------

Zahra Hussaini, zahrahussaini89@gmail.com

