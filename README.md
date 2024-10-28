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

### Version 1.7

* Converted interface from Lists to Sets for the rooms and connections

### Version 1.8

* Converted images to circles so that the connections line up better with the edges.
* Added the capability to set the background, room, and text colors in the builder.
* Removed references to Arcane game, as this is not tied to any specific game.
* Expanded the status message to be a list of strings

### Version 1.9

* Added more room images and the code tries to match room names to image names

### Version 1.10
* Code automatically searches resources/images for room images, making it easy to add more images
* Add inline layout
* Added separate panel for status above the maze layout so that it can't interfere with the maze 
* Added ability for clients to add their own custom images for the maze.

### Version 1.11
* Added ability to set height and width of maze separately
* If room size isn't specified, it is calculate to make rooms as big as possible

### Version 1.11.1
Fixed the "zip file is closed" error.

### Version 1.11.2
Trying to fix getting a null pointer error if the parent program ends while I'm still drawing

### Version 2.0.0
* Changed the IMazeSubject interface to remove the erroneous
static variables. Now implementors of this interface need to 
create the observers list.
* Changed the name of a method in the IMaze interface

* ![FullyConnected16RoomLayout.grid.png](sampleLayouts/FullyConnected16RoomLayout.grid.png)


### TODO:
* Add check for panels too large and rooms too large for the panel
* Try to get the room images as circles, so that arrows end at the image boundary, though that will be less good for the text... maybe keep it as is.
* Make the images for the rooms persistent. Now they change on every update of the Maze
