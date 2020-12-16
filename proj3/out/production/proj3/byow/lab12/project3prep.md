# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer: Our implementation differed from the given solution by our solution placing hexagons row by row. This
is very inefficient however. We should have been more clever about thinking through the solution.

-----

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer: Tessellating hexagons is like randomly generating a world using rooms and hallways, because things need to connect in a way
where it fits, due to the curvature of the hexagons and the "random generation". We can't just have objects overlap. The hexagon
would be the rooms and hallways and the tesselation would be the fitting process. 

-----
**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer: The first method we would think of writing first is how to randomly generate a room. This would go into one method,
and eventually, the random generation will use that method multiple times to generate rooms.

-----
**What distinguishes a hallway from a room? How are they similar?**

Answer: Rooms are places which are open rectangularly shaped areas, although they can be other shapes. Hallways on the other hand
are 1-width corridors, with walls, which can intersect other hallways. Besides the intersection property, hallways are like
1-width, strictly rectangular, rooms.
