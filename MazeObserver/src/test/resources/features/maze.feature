Feature: Maze layout
  As a game player
  I want to see the maze state in a graphical window
  So that I can see how best to move around

  Scenario: A maze can be viewed
    Given a fully-connected maze with 4 rooms

    When the game sends an update message of "Hello, World!"

    Then the maze displays the message