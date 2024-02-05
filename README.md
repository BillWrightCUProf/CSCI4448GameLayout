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

### TODO:
* Add example of Adaptor pattern, to adaptor subjects Rooms to the IConnectedRoom interface.
