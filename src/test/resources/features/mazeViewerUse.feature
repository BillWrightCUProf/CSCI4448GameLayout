Feature: Creation and use of Maze Viewer
  As a game player
  I want to a graphical layout of the cave
  So that I can better understand where things are

  @disabled
  Scenario: MazeViewer creation
    Given a Maze Viewer
    When I pass it the following JSON:
    """
  {
   "name": "Four Room",
   "turn": 3,
    "rooms": [
      {
      "name": "Crystal Palace",
      "neighbors": [
          "Pool of Lava",
          "Swamp",
          "Stalactite Cave"
      ],
      "contents": [
         "leather-Armor"
      ]
      },
      {
      "name": "Pool of Lava",
      "neighbors": [
      "Crystal Palace",
      "Swamp",
      "Stalactite Cave"
      ],
      "contents": [
      "Professor(health: 7.50)"
      ]
      },
    {
    "name": "Swamp",
    "neighbors": [
    "Crystal Palace",
    "Pool of Lava"
    ],
    "contents": [
    "Frodo(health: 8.35)",
    "chainmail-Armored Lady Brienne(health: 9.47)",
    "Arwen(health: 5.80)",
    "apple(1.3)"
    ]
    },
    {
    "name": "Stalactite Cave",
    "neighbors": [
    "Crystal Palace",
    "Pool of Lava"
    ],
    "contents": [
    "Dragon(health: 3.00)",
    "Satan(health: 13.50)",
    "salad(1.7)",
    "fries(1.3)"
    ]
    }
    ],
    "availableCommands": [
    "DO NOTHING",
    "MOVE"
    ]
  }
  """
    Then I see the layout on the screen