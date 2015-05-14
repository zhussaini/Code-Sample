Spaceship Escape

Overview
--------

You are in a spaceship trying to escape from an exploding planet. The planet is surrounded by asteroids, which your spaceship must avoid. You control the spaceship's acceleration at discrete time steps. You give the spaceship a course using json. There are three possible commands for each time step. Entering a 1 into the spaceship's computer instructs it to fire the thrusters and accelerate by one unit. Entering -1 makes the spaceship accelerate in reverse by one unit. Entering 0 will do nothing and the spaceship will continue coasting at its current velocity. 

Ways to die:

Hitting an asteroid. 
Asteroids orbit the planet with a fixed period and initial offset given in the input json file. The asteroids can only occupy discrete positions, so you don't need to worry about hitting them when they are in between positions when you are going fast (v > 1). 

Hitting the planet.
If you accelerate in reverse, you can hit the planet.

Being consumed by the planet's explosion.
The planet is exploding at a rate given in the input json file (t_per_blast_move). Each t_per_blast_move seconds, the planet's blast radius increases by one position. If the spaceship is inside the blast radius, then you die.

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

REVISE THIS TO INCLUDE EFFECT OF BLAST RADIUS. BLAST RADIUS SHOULD REDUCE TIME AND SPACE COMPLEXITY TO POSSIBLY SOMETHING POLYNOMIAL.

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

