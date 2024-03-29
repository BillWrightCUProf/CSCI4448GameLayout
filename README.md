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

### Version 1.4

* Added a graphic image to represent the room. Here's an example:

![MazeWithImageRooms.png](sampleLayouts%2FMazeWithImageRooms.png) 
(I have a free account here)

### Version 1.5

* Added more images for the rooms and each is used in a rotation
* <a href="https://www.freepik.com/free-vector/underground-waterfall-cave-scenery-landscape-water-stream-fall-from-rocky-cliff-mountain-lake-falling-river-jet-cascade-pour-from-pond-with-stones-around-cartoon-vector-background_20514441.htm#from_view=detail_alsolike">Image by upklyak on Freepik</a>



### TODO:
* Add check for panels too large and rooms too large for the panel
* Try to get the room images as circles, so that arrows end at the image boundary, though that will be less good for the text... maybe keep it as is.
* Make the images for the rooms persistent. Now they change on every update of the Maze
