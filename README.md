# Guessing Game Project

A multi-player guessing game built with Java where one player hosts a room and others join to guess a secret word.

## Prerequisites

- Java 21 or higher
- Gradle (or use the Gradle wrapper `./gradlew`)

## How to Run

### 1. Start the Server

Open a terminal and run:
```bash
./gradlew runServer
```

### 2. Start Client(s)

Open additional terminals for each client and run:
```bash
./gradlew runClient
```

**Note:** You need at least one server and one or more clients running simultaneously.

## Game Flow

1. **Server**: Must be started first
2. **Client**: Choose to either:
   - Host a room (challenger)
   - Join an existing room (player)

### Host a Room
- Select a game genre
- Enter a secret word
- Set number of attempts (optional)
- Answer yes/no questions from players

### Join a Room
- Select from available rooms
- Ask questions to guess the secret word
- Make your final guess

## Troubleshooting

If you encounter input issues, try running with:
```bash
./gradlew runServer --console=plain
./gradlew runClient --console=plain
```

## Project Structure

- `Server.java` - Game server handling connections and room management
- `Client.java` - Client interface for players and challengers
- Other supporting classes for game logic (`ClientHandler.java`, `Player.java`, `Challenger.java`, `Room.java`, `RoomManager.java`)
