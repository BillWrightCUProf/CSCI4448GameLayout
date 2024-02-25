# CSCI-4448/5448: Game Layout Library

This library is intended for use in the University of Colorado's Computer Science
course ___CSCI-4448/5448: Object-Oriented Analysis and Design___.

This is a small library that uses the Java 2D Graphics library to
layout a series of connected rooms that form a maze. The connections are
one-way and have arrows indicating the direction.

The library is structured as an example of the Observer Pattern,
where the subject (not part of this library) implements the 
IMazeSubject, included in this library.

A complete example (ExampleSubject) is included, which shows 
how to use the library.

### Version 1.3

I simplified things a bit and made them more complicated too:
* Maze and Room now just have a single width parameter (they are both square)
* I eliminated the IRoom interface and just have IMaze interface, which returns information about the rooms
* Added a builder pattern to the creation of the MazeObserver so that layout, size, room shape, and delay can be set
* Added a delay in the viewer -- seems better than building it into the game
* Added more tests and fixed layout of rooms for all sizes and all layouts (grid and radial)


### TODO:
* Add check for panels too large and rooms too large for the panel
